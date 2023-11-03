package com.example.dotabot1.services;

import com.example.dotabot1.dto.matches.Matches;
import com.example.dotabot1.dto.matchesdetails.MatchDetails;
import com.example.dotabot1.dto.matchesdetails.Players;
import com.example.dotabot1.entity.users.User;
import com.example.dotabot1.repository.PlayerRepository;
import com.example.dotabot1.services.dotaapiservice.SteamApiService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Класс реализует логику получения информации о истории матчей и о информации конкретного матча от стим АПИ
 */


@Service
@RequiredArgsConstructor
public class GameMatchService {
    private final SteamApiService steamApiService;
    private final MessageGeneratorService messageGeneratorService;
    private final PlayerRepository playerRepository;
    private static final Logger logger = LoggerFactory.getLogger(GameMatchService.class);

    /**
     * Метод совершает обращенеи к БД завернутое в fromCallable для избежание блокировки потоков
     *
     * @param chatId чат айди пользователя
     * @return возвращает реактивный поток
     */
    public Mono<User> getUserByChatId(Long chatId) {
        return Mono.fromCallable(() -> playerRepository.findByChatId(chatId))
                .subscribeOn(Schedulers.boundedElastic());
    }

    /**
     * Метод преобразовывает 64 битный стим айди в 32 битное представление
     *
     * @param bit строчное представление 64- битного стим айди
     * @return 32 - битный стим айди
     */
    private String to32bitAccountId(String bit) {
        long steamId64 = Long.parseLong(bit);
        long steamId32 = steamId64 - 76561197960265728L;
        return String.valueOf(steamId32);
    }

    /**
     * Получает реактивный поток с сущностью списка сыграных матчей
     *
     * @return реактивный поток с значенеим матч айди
     */
    public Mono<List<Matches>> getIdMatchesList(User user) {
        if (user.getSteamApiKey() != null && user.getSteamId() != null) {
            return steamApiService.getPlayerMatchHistory(user.getSteamId(), user.getSteamApiKey())
                    .map(requestHistory -> requestHistory.getResult().getMatches());
        } else {
            return Mono.error(new IllegalStateException("Стим АПИ ключ или стим айди null"));
        }
    }

    /**
     * Метод сравнивает айди целевого стим аккаунта с айди игроков в матче , по найденному игроку определяется выйграл он или проиграл
     * @param accountId айди игрока взятое из БД
     * @param matchId   айди матча
     * @return Возвращает реактивный поток с булевым значением о том выйграл игрок или проиграл
     * Выйгрыш игрока определяется тем в какой команде он играл и сравнивается с тем выйграла ли команда света Radiant
     */
    public Mono<Boolean> didPlayerWin(String accountId, String matchId, User user) {
        return steamApiService.getMatchDetails(matchId, user.getSteamApiKey())
                .flatMap(match -> {
                    for (Players player : match.getResult().getPlayers()) {
                        if (player.getAccount_id().equals(to32bitAccountId(accountId))) {
                            return Mono.just(player.playerIsRadiant(player.getPlayer_slot()) == match.getResult().isRadiant_win());
                        }
                    }
                    return Mono.error(new IllegalArgumentException("Игрок с таким ID не найден в этом матче."));
                });
    }

    /**
     * Метод получает пользователя и проверяет выйграл ли он последний матч*
     *
     * @return возвращает Mono булеан для исполльзования его в finalStatsMessage
     */
    public Mono<Boolean> checkLastMatchWin(User user) {
        return getIdMatchesList(user) // реактивный запрос возвращающий Mono тип лист типа матчей
                .map(matchesList -> matchesList.get(0).getMatchId()) // получаем последний сыграный матч
                .flatMap(lastMatchId -> didPlayerWin(user.getSteamId(), String.valueOf(lastMatchId), user)); // преобразовываем в другой реактивный поток для вложения его в метод didPlayerWin для определения результата матча
    }

    /**
     * Метод получает
     *
     * @param user  пользователя
     * @param match объект конкретного матча
     * @return возвращает реаткивный поток с объектом Mono булеан , по которому определяется выйграл игрок или проиграл
     */
    public Mono<Boolean> checkMatchWin(User user, MatchDetails match) {
        String matchId = match.getResult().getMatch_id().toString();
        return didPlayerWin(user.getSteamId(), matchId, user);
    }

    /**
     * В метод передается
     *
     * @param chatId   телеграм айди пользователя
     * @param userMono реактивный поток Mono с типом User
     * @return возвращает реаткивный поток Void
     */

    public Mono<Void> matchPlayedOverThePast24Hours(Long chatId, Mono<User> userMono) {

        return userMono.flatMap(user -> {  // достаем объект User из реактивного потока
            long currentTimestamp = System.currentTimeMillis() / 1000;
            long twentyFourHoursAgo = currentTimestamp - (72 * 60 * 60);
            // Используем полученный объект User, чтобы получить список его матчей
            return getIdMatchesList(user)
                    // Преобразуем список матчей в реактивный поток (Flux)
                    .flatMapMany(matchesList -> Flux.fromIterable(matchesList.stream() // преобразовыываем в флукс итератор для эммитирования для того что бы в последситвии обработать каждый эелемент
                            // Отфильтровываем матчи, чтобы оставить только те, что были сыграны за последние 24 часа
                            .filter(match -> match.getStart_time() > twentyFourHoursAgo)
                            .collect(Collectors.toList())))
                    // Для каждого матча из потока делаем запрос на детали матча
                    .concatMap(match -> steamApiService.getMatchDetails(String.valueOf(match.getMatchId()), user.getSteamApiKey())
                            // Проверяем, выиграл ли пользователь этот матч
                            .flatMap(matchdetails -> checkMatchWin(user, matchdetails)
                                    // В зависимости от результата (победа или поражение), отправляем соответствующее сообщение пользователю
                                    .flatMap(isWin -> {
                                        if (isWin) {
                                            return Mono.fromRunnable(() -> messageGeneratorService.congratsOnWin(chatId, matchdetails));
                                        } else {
                                            return Mono.fromRunnable(() -> messageGeneratorService.betterLuckNextTime(chatId, matchdetails));
                                        }
                                    })
                            )
                    ).then()
                    .onErrorResume(e -> {           // Локальная обработка исключения
                        logger.error("Ошибка при обработке матча", e);
                        return Mono.empty();
                    });
        });
    }

    /**
     * Метод отправляет сообщение пользователю о том выйграл ли игрок последний сыгранный матч или проиграл на основе данных
     * полученных из цепочки вызовов
     *
     * @param chatId чат айди пользователя телеграм
     * @user Объект пользователя
     */

    public void finalStatsMessage(Long chatId, User user) {
        checkLastMatchWin(user)
                .doOnNext(isWin -> {
                    if (isWin) {
                        messageGeneratorService.playerIsWin(chatId);  // Отправляем сообщение о том, что игрок выиграл
                        logger.info("Игрок выйграл последний сыграный матч ");
                    } else {
                        messageGeneratorService.playerIsLose(chatId); // Отправляем сообщение о том, что игрок проиграл
                        logger.info("Игрок проиграл последнйи сыграный матч");
                    }
                })
                .doOnSuccess(result -> {
                    logger.info("Successfully completed the operation for chatId {}", chatId); // Логируем успешное завершение потока
                })
                .doOnError(e -> {
                    logger.error("An error occurred for chatId {}: {}", chatId, e.getMessage()); // Логируем ошибку
                })
                .subscribe();
    }


}
