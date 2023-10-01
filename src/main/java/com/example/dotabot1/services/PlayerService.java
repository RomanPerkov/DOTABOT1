package com.example.dotabot1.services;

import com.example.dotabot1.entity.users.DotaStats;
import com.example.dotabot1.entity.users.States.PlayerState;
import com.example.dotabot1.entity.users.User;
import com.example.dotabot1.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


/**
 * Класс PlayerService отвечает за взаимодействие с сущностью игрока (User) в базе данных.
 * Этот класс служит для выполнения операций CRUD и других бизнес-логик, связанных с игроками.
 *
 * Особенности:
 * - Использует JpaRepository для взаимодействия с базой данных.
 * - Взаимодействует с классом MessageGeneratorService для генерации сообщений.
 *
 */
@Service
@RequiredArgsConstructor
public class PlayerService {


    private final PlayerRepository playerRepository;
    private final MessageGeneratorService messageGeneratorService;



     /**
     * Метод registerNewPlayer создает нового пользователя и сохраняет его в базе данных.
     *
     * Шаги выполнения:
     * 1. Проверяет, существует ли пользователь с указанным chatId в базе данных.
     * 2. Если пользователя не существует, создает новую запись User и сохраняет в базе данных.
     * 3. Генерирует приветственное сообщение или сообщение о том, что пользователь уже существует.
     *
     * @param chatId уникальный идентификатор чата пользователя.
     */

    public void registerNewPlayer(Long chatId) {
        // Проверяем, есть ли уже такой пользователь в БД
        if (playerRepository.findByChatId(chatId) == null) { // создаем нового пользователя если такого нет в БД
            DotaStats dotaStats = new DotaStats();
            User player = User.builder()
                    .chatId(chatId)
                    .dotaStatsId(dotaStats)
                    .state(PlayerState.DEFAULT)
                    .build();
            playerRepository.save(player);

            messageGeneratorService.generateWelcomeMessage(chatId);  // приветственное сообщение
        } else {
            messageGeneratorService.userHasAlreadyBeenCreatedMessage(chatId); // пользователь уже существует
        }
    }
}
