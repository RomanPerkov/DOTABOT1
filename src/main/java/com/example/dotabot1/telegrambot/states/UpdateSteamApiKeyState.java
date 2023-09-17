package com.example.dotabot1.telegrambot.states;


import com.example.dotabot1.entity.users.States.PlayerState;
import com.example.dotabot1.entity.users.User;
import com.example.dotabot1.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Класс комманда для добавления АПИ ключа
 */
@Service
@RequiredArgsConstructor
public class UpdateSteamApiKeyState implements StateHandler {


    private final PlayerRepository playerRepository;


    @Override
    public PlayerState getNameState() {
        return PlayerState.UPDATE_API_KEY;
    }

    @Override
    public void executeState(Update update, Long chatId, String messageText) {

        User user = playerRepository.findByChatId(chatId);
        user.setSteamApiKey(messageText);
        user.setState(PlayerState.DEFAULT);
        playerRepository.save(user);



    }
}

