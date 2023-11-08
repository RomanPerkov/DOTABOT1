package com.example.dotabot1.telegrambot.Commands;

import com.example.dotabot1.entity.users.User;
import com.example.dotabot1.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.dotabot1.constants.Constants.CommandsConstants.STOP_COMMAND;

/**
 * Класс удаляет стим айди пользователя из БД таким образом останавливая слежение за аккаунтом
 */
@Service
@RequiredArgsConstructor
public class StopCommand implements Command {

    private final PlayerRepository playerRepository;

    @Override
    public String getName() {
        return STOP_COMMAND;
    }

    @Override
    public void executeCommand(Long chatId) {
        User user = playerRepository.findByChatId(chatId);
        user.setSteamId(null);
        playerRepository.save(user);
    }
}
