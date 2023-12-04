package com.example.dotabot1.telegrambot.states;


import com.example.dotabot1.model.entity.users.States.PlayerState;

/**
 * Обработчик состояний
 */
public interface StateHandler {

         PlayerState getNameState();
        void executeState( Long chatId, String messageText);
}
