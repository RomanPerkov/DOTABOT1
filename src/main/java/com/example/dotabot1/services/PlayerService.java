package com.example.dotabot1.services;

import com.example.dotabot1.entity.users.DotaStats;
import com.example.dotabot1.entity.users.States.PlayerState;
import com.example.dotabot1.entity.users.User;
import com.example.dotabot1.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


/**
 * Класс создан для для взаимодейстия с БД CRUD и другие.
 */
@Service
@RequiredArgsConstructor
public class PlayerService {


    private final PlayerRepository playerRepository;
    private final MessageGeneratorService messageGeneratorService;


    /**
     * Создаем нового пользователя и записываем его в БД
     *
     * @param chatId айди чата
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
