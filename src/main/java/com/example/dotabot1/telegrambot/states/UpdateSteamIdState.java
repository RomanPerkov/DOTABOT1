package com.example.dotabot1.telegrambot.states;

import com.example.dotabot1.entity.users.States.PlayerState;
import com.example.dotabot1.entity.users.User;
import com.example.dotabot1.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;


@Service
@RequiredArgsConstructor
public class UpdateSteamIdState implements StateHandler {
    private final PlayerRepository playerRepository;


    @Override
    public PlayerState getNameState() {
        return PlayerState.UPDATE_STEAM_ID;
    }

    @Override
    public void executeState(Update update, Long chatId, String messageText) {

        User user = playerRepository.findByChatId(chatId);
        user.setSteamId(messageText);
        playerRepository.save(user);


    }
}
