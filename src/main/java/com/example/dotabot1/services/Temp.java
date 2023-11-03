//package com.example.dotabot1.services;
//
//import com.example.dotabot1.dto.matches.Matches;
//import com.example.dotabot1.dto.matchesdetails.MatchDetails;
//import com.example.dotabot1.dto.matchesdetails.Players;
//import com.example.dotabot1.entity.users.User;
//import com.example.dotabot1.repository.PlayerRepository;
//import com.example.dotabot1.services.dotaapiservice.SteamApiService;
//import lombok.RequiredArgsConstructor;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.stereotype.Service;
//import reactor.core.publisher.Flux;
//import reactor.core.publisher.Mono;
//import reactor.core.scheduler.Schedulers;
//
//import java.util.List;
//import java.util.stream.Collectors;
//
///**
// * Класс реализует логику получения информации о истории матчей и о информации конкретного матча от стим АПИ
// */
//
//
//@Service
//@RequiredArgsConstructor
//public class GameMatchService {
//    private final SteamApiService steamApiService;
//    private final MessageGeneratorService messageGeneratorService;
//    private final PlayerRepository playerRepository;
//    private static final Logger logger = LoggerFactory.getLogger(GameMatchService.class);
//
////    long currentTimestamp = System.currentTimeMillis() / 1000; // текущее время в UNIX-формате
////    long twentyFourHoursAgo = currentTimestamp - (72 * 60 * 60); // 24 часа назад
//
//
//    /**
//     * Метод преобразовывает 64 битный стим айди в 32 битное представление
//     *
//     * @param bit строчное представление 64- битного стим айди
//     * @return 32 - битный стим айди
//     */
//    private String to32bitAccountId(String bit) {
//        long steamId64 = Long.parseLong(bit);
//        long steamId32 = steamId64 - 76561197960265728L;
//        return String.valueOf(steamId32);
//    }
//
//
//    /**
//     * Получает реактивный поток с сущностью списка сыграных матчей
//     *
//     * @param chatId чат айди пользователя бота
//     * @return реактивный поток с значенеим матч айди
//     */
//    public Mono<List<Matches>> getIdMatchesList(Long chatId) {
//        return Mono.fromCallable(() -> playerRepository.findByChatId(chatId))
//                .subscribeOn(Schedulers.boundedElastic())
//                .flatMap(user -> {
//                    if (user.getSteamApiKey() != null && user.getSteamId() != null) {
//                        return steamApiService.getPlayerMatchHistory(user.getSteamId(), user.getSteamApiKey())
//                                .flatMap(requestHistory -> {
//                                    List<Matches> lastMatchId = requestHistory.getResult().getMatches();// Получаем ID последнего матча
//                                    return Mono.just(lastMatchId);
//                                });
//                    } else {
//                        return Mono.error(new IllegalStateException("Steam API Key or Steam ID is null."));
//                    }
//                });
//    }
//
//    /**
//     * Метод сравнивает айди целевого стим аккаунта с айди игроков в матче , по найденному игроку определяется выйграл он или проиграл
//     * так же отправляет результат матча пользователю
//     *
//     * @param accountId айди игрока взятое из БД
//     * @param matchId   айди матча
//     * @param chatId    айди пользователя
//     * @return Возвращает реактивный поток с булевым значением о том выйграл игрок или проиграл
//     * Выйгрыш игрока определяется тем в какой команде он играл и сравнивается с тем выйграла ли команда света Radiant
//     */
//
//    public Mono<Boolean> didPlayerWin(String accountId, String matchId, Long chatId) {
//        return Mono.fromCallable(() -> playerRepository.findByChatId(chatId).getSteamApiKey())
//                .subscribeOn(Schedulers.boundedElastic())
//                .flatMap(steamApi -> steamApiService.getMatchDetails(matchId, steamApi))
//                .flatMap(match -> {
//                    System.out.println(matchId +"  "+"1111");
//                    ;
//                    for (Players player : match.getResult().getPlayers()) {
//                        if (player.getAccount_id().equals(to32bitAccountId(accountId))) {
//                            //messageGeneratorService.playerMatchsStats(chatId, match);
//                            return Mono.just(player.playerIsRadiant(player.getPlayer_slot()) == match.getResult().isRadiant_win());
//                        }
//                    }
//                    return Mono.error(new IllegalArgumentException("Игрок с таким ID не найден в этом матче."));
//                });
//    }
//
//
//    /**
//     * Метод получает айди последнего матча дял дальнейшего использования в цепочке методов
//     *
//     * @param chatId айди пользователя
//     * @return
//     */
//    public Mono<Boolean> checkLastMatchWin(Long chatId) {
//        return Mono.fromCallable(() -> playerRepository.findByChatId(chatId))
//                .subscribeOn(Schedulers.boundedElastic())
//                .flatMap(user -> getIdMatchesList(chatId)
//                        .flatMap(lastMatchId -> {
//                            String lastMatch = String.valueOf(lastMatchId.get(0).getMatchId());
//                            return didPlayerWin(user.getSteamId(), lastMatch, chatId);
//
//                        })
//                );
//    }
//
//    public Mono<Boolean> checkMatchWin(User user, MatchDetails match, Long chatId) {
//        String matchId = match.getResult().getMatch_id().toString();
//        return didPlayerWin(user.getSteamId(), matchId, chatId);
//    }
//
//
//    //Этот метод используется для получения матчей, сыгранных за последние 24 часа для определенного пользователя.
//
//    public Mono<Void> matchPlayedOverThePast24Hours(Long chatId) {
//        return Mono.fromCallable(() -> playerRepository.findByChatId(chatId))
//                // Указываем, что предыдущий этап должен выполняться в boundedElastic scheduler.
//                // Это обычно используется для блокирующих операций или задач, требующих много времени.
//                .subscribeOn(Schedulers.boundedElastic())
//                // После получения информации о пользователе, делаем запрос на получение списка матчей этого пользователя.
//                .flatMap(user -> getIdMatchesList(chatId))
//                // После получения списка матчей преобразуем его во Flux, чтобы обработать каждый матч по отдельности.
//                .flatMapMany(matchesList -> {
//                    long currentTimestamp = System.currentTimeMillis() / 1000; // текущее время в UNIX-формате
//                    long twentyFourHoursAgo = currentTimestamp - (72 * 60 * 60); // 24 часа назад
//                    // Фильтруем матчи, которые начались после времени "24 часа назад", и создаем из них Flux.
//                    return Flux.fromIterable(matchesList.stream()
//                            .filter(match -> match.getStart_time() > twentyFourHoursAgo)
//                            .collect(Collectors.toList()));
//                })
//                // Для каждого отфильтрованного матча делаем запрос на получение его деталей. Используем concat для последовательной обработки
//                .concatMap(match -> {
//                    User user = playerRepository.findByChatId(chatId);
//                    System.out.println(String.valueOf(match.getMatchId())+ "  "+"22222");
//                    return steamApiService.getMatchDetails(String.valueOf(match.getMatchId()), playerRepository.findByChatId(chatId).getSteamApiKey())
//                            .flatMap(matchdetails -> checkMatchWin(playerRepository.findByChatId(chatId),matchdetails,chatId)
//                                    .flatMap(isWin -> {
//                                        if (isWin) {
//
//                                            // Твой код для случая, когда игрок выиграл последний матч.
//                                            // Например, отправка сообщения "Поздравляю с победой!"
//                                            return Mono.fromRunnable(() -> messageGeneratorService.congratsOnWin(chatId, matchdetails));
//                                        } else {
//                                            // Твой код для случая, когда игрок проиграл последний матч.
//                                            // Например, отправка сообщения "Не унывай, в следующий раз повезет!"
//                                            return Mono.fromRunnable(() -> messageGeneratorService.betterLuckNextTime(chatId, matchdetails));
//                                        }
//                                    })
//                            );
//                } ).then(); // Возвращает Mono<Void> после обработки всех матчей
//    }
//
//
//
//
////    public Mono<Void> matchPlayedOverThePast24Hours(Long chatId) {
////        return Mono.fromCallable(() -> playerRepository.findByChatId(chatId))
////                .subscribeOn(Schedulers.boundedElastic())
////                .flatMap(user -> getIdMatchesList(chatId)
////                        .flatMapMany(matchesList -> {
////                            long currentTimestamp = System.currentTimeMillis() / 1000; // текущее время в UNIX-формате
////                            long twentyFourHoursAgo = currentTimestamp - (72 * 60 * 60); // 24 часа назад
////                            return Flux.fromIterable(
////                                    matchesList.stream()
////                                            .filter(match -> match.getStart_time() > twentyFourHoursAgo)
////                                            .map(match -> Tuples.of(user, match))
////                                            .collect(Collectors.toList())
////                            );
////                        })
////                )
////                .concatMap(tuple -> {
////                    User user = tuple.getT1();
////                    MatchDetails match = tuple.getT2();
////                    return steamApiService.getMatchDetails(String.valueOf(match.getMatchId()), user.getSteamApiKey())
////                            .flatMap(details -> checkLastMatchWin(user, match, chatId)
////                                    .flatMap(isWin -> {
////                                        if (isWin) {
////                                            return Mono.fromRunnable(() -> messageGeneratorService.congratsOnWin(chatId, details));
////                                        } else {
////                                            return Mono.fromRunnable(() -> messageGeneratorService.betterLuckNextTime(chatId, details));
////                                        }
////                                    })
////                            );
////                });
////    }
//
//
//    /**
//     * Метод отправляет сообщение пользователю о том выйграл ли игрок или проиграл на основе данных
//     * полученных из цепочки вызово
//     *
//     * @param chatId чат айди пользователя телеграм
//     */
//
//    public void finalStatsMessage(Long chatId) {
//        checkLastMatchWin(chatId)
//                .doOnNext(isWin -> {
//                    if (isWin) {
//                        messageGeneratorService.playerIsWin(chatId);  // Отправляем сообщение о том, что игрок выиграл
//                        logger.info("Player with chatId {} won the game", chatId);
//                    } else {
//                        messageGeneratorService.playerIsLose(chatId); // Отправляем сообщение о том, что игрок проиграл
//                        logger.info("Player with chatId {} lost the game", chatId);
//                    }
//                })
//                .doOnSuccess(result -> {
//                    logger.info("Successfully completed the operation for chatId {}", chatId); // Логируем успешное завершение потока
//                })
//                .doOnError(e -> {
//                    logger.error("An error occurred for chatId {}: {}", chatId, e.getMessage()); // Логируем ошибку
//                })
//                .subscribe();
//    }
//
//

// ПЛОХОЙ МЕТОД ФУ ЕГО
//    public Mono<Void> matchPlayedOverThePast24Hours(Long chatId) {
//        return Mono.fromCallable(() -> playerRepository.findByChatId(chatId))
//                .subscribeOn(Schedulers.boundedElastic())
//                .flatMap(user -> getIdMatchesList(chatId)
//                        .flatMapMany(matchesList -> {
//                            long currentTimestamp = System.currentTimeMillis() / 1000; // текущее время в UNIX-формате
//                            long twentyFourHoursAgo = currentTimestamp - (72 * 60 * 60); // 24 часа назад
//                            return Flux.fromIterable(
//                                    matchesList.stream()
//                                            .filter(match -> match.getStart_time() > twentyFourHoursAgo)
//                                            .map(match -> Tuples.of(user, match))
//                                            .collect(Collectors.toList())
//                            );
//                        })
//                )
//                .concatMap(tuple -> {
//                    User user = tuple.getT1();
//                    MatchDetails match = tuple.getT2();
//                    return steamApiService.getMatchDetails(String.valueOf(match.getMatchId()), user.getSteamApiKey())
//                            .flatMap(details -> checkLastMatchWin(user, match, chatId)
//                                    .flatMap(isWin -> {
//                                        if (isWin) {
//                                            return Mono.fromRunnable(() -> messageGeneratorService.congratsOnWin(chatId, details));
//                                        } else {
//                                            return Mono.fromRunnable(() -> messageGeneratorService.betterLuckNextTime(chatId, details));
//                                        }
//                                    })
//                            );
//                });
//    }
//}
