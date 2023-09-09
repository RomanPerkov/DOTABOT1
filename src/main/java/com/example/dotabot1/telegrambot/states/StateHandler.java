package com.example.dotabot1.telegrambot.states;


import com.example.dotabot1.entity.users.States.PlayerState;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Обработчик состояний
 */
public interface StateHandler {

        public PlayerState getNameState();
        void executeState(Update update, Long chatId, String messageText);
}
