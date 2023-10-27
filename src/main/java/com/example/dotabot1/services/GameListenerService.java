package com.example.dotabot1.services;


import com.example.dotabot1.dto.player.Player;
import com.example.dotabot1.dto.player.Request;
import com.example.dotabot1.entity.users.States.DotaState;
import com.example.dotabot1.entity.users.User;
import com.example.dotabot1.repository.PlayerRepository;
import com.example.dotabot1.services.dotaapiservice.SteamApiService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.stream.Stream;


/**
 * Класс GameListenerService служит для взаимодействия с API игры Dota и обновления статуса игры пользователей.
 * <p>
 * Особенности:
 * - Использует внедрение зависимости DotaApiService для работы с API Dota.
 * - Использует PlayerRepository для доступа к данным пользователей в базе данных.
 * - Метод dotaApiCall() выполняет следующие шаги:
 * 1. Получает поток (Stream) всех пользователей из базы данных.
 * 2. Проверяет наличие Steam ID и API Key для каждого пользователя.
 * 3. Выполняет асинхронный запрос к API Dota для получения статуса игры.
 * 4. Обновляет статус игры в объекте пользователя.
 * <p>
 * В случае возникновения исключений в методах, информация об ошибке логируется.
 */
@Service
@RequiredArgsConstructor
public class GameListenerService {
    private final SteamApiService steamApiService;
    private final PlayerRepository playerRepository;
    private static final Logger logger = LoggerFactory.getLogger(GameListenerService.class);


    // @Scheduled(fixedRate = 60000)
    @Transactional
    public void dotaApiCall() {
        try (Stream<User> users = playerRepository.streamAllUsers()) { // используем стрим для получения списка пользователей из БД
            users.forEach(user -> {
                if (user.getSteamId() == null || user.getSteamApiKey() == null) {                   // обходим стрим, пропускаем сущности у которых нет одного из параметров
                    logger.warn("Skipping user {} due to missing Steam ID or API Key", user.getChatId());
                    return;
                }
                Mono<Request> requestMono = steamApiService.getPlayerStatus(user.getSteamId(), user.getSteamApiKey()); // ждя каждого объекта в стриме делаем запрос к апи
                //используем оптионал для нулл
                requestMono.subscribe(request ->                    //подписываемся на поток НАЧАЛО ПЕРВОЙ ЛЯМБЫ subscribe
                                Optional.ofNullable(request)
                                        // создаем оптиона с реквест или пусто
                                        .map(Request::getResponse)                   // преобразовываем реквест в респонс
                                        .map(r -> r.getPlayers().get(0))              //преобразовываем респонс в плауер
                                        .map(Player::getGameid)                        // преобразовыываем плауер в getGameid
                                        .ifPresentOrElse(gameId ->
                                                {
                                                    user.getDotaStatsId().setStatus(DotaState.INGAME);// меняем статус если не пустой оптионал
                                                    playerRepository.save(user);
                                                },
                                                () -> {                                                             // если пустой , то выполняем вторую лямбду
                                                    if (user.getDotaStatsId().getStatus() != DotaState.NONGAME) {   //меняем статус на нон гейм если таковой уже не стоит
                                                        user.getDotaStatsId().setStatus(DotaState.NONGAME);
                                                        playerRepository.save(user);
                                                    }
                                                }),  // КОНЕЦ ПЕРВОЙ ЛЯМБЫ
                        error -> {                  // ВТОРАЯ ЛЯМБА ОБРАБОТКА ОШИБОК
                            // Здесь логирование ошибки
                            logger.error("An error occurred while processing user: {}", user.getChatId(), error);
                        },                  // КОНЕЦ ВТОРОЙ ЛЯМБДЫ
                        () -> logger.info("Processing complete for user {}", user.getChatId())); // ТРЕТЬЯ ЛЯМБДА запись о нормальном завершении потока

            });

        } catch (Exception e) {
            logger.error("Exception", e);
        }
    }
}




