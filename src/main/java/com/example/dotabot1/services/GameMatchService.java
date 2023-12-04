package com.example.dotabot1.services;

import com.example.dotabot1.model.dto.matches.Matches;
import com.example.dotabot1.model.dto.matchesdetails.MatchDetails;
import com.example.dotabot1.model.dto.matchesdetails.Players;
import com.example.dotabot1.model.entity.users.User;
import com.example.dotabot1.repository.PlayerRepository;
import com.example.dotabot1.services.dotaapiservice.SteamApiService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

import java.time.Duration;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
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
     * Конвертер юникс времени в нормальное
     * @param unixTimestamp временная точка в юникс времени
     * @return преобрахует юникс время в нормальное
     */
    public static String convertUnixToReadable(long unixTimestamp) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Instant instant = Instant.ofEpochSecond(unixTimestamp);
        LocalDateTime dateTime = LocalDateTime.ofInstant(instant, ZoneId.systemDefault());
        return formatter.format(dateTime);
    }

    /**
     * Конвертер юникс длительности в нормальную
     * @param unixTimestamp продолжительность матча в юникс формате
     * @return преобразует юникс продолжительность в нормальную
     */
    public static String convertDuration(long unixTimestamp) {
        Duration duration = Duration.ofSeconds(unixTimestamp);
        return String.format("%d:%02d:%02d",
                duration.toHours(),
                duration.toMinutesPart(),
                duration.toSecondsPart());

    }

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
     * @param chatId   телеграм айди пользователя
     * @param userMono реактивный поток Mono с типом User
     * @return возвращает реаткивный поток Void
     */

    public Mono<Void> matchPlayedOverThePastHours(Long chatId, Mono<User> userMono, Long hours) {

        return userMono.flatMap(user -> {  // достаем объект User из реактивного потока
            long currentTimestamp = System.currentTimeMillis() / 1000;
            Long twentyFourHoursAgo = currentTimestamp - hours;
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
     * Высчитывает сколько длилась игровая сессия
     * @param startSession время старта сессии
     * @return разницу между сейчас и временем старта + 5 минут
     */
    public Long durationSession(Long startSession){
        return (Instant.now().getEpochSecond()-startSession);
    }

}
