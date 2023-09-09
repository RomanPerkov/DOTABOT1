package com.example.dotabot1.telegrambot;

import com.example.dotabot1.entity.users.States.PlayerState;
import com.example.dotabot1.repository.PlayerRepository;
import com.example.dotabot1.telegrambot.Commands.CommandHandler;
import com.example.dotabot1.telegrambot.states.StateHandler;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Реализация UpdateHandler для обработки сообщений
 */
@Service
//@RequiredArgsConstructor
public class MessageUpdateHandler implements UpdateHandler {


    private final CommandHandler commandHandler;
    private final PlayerRepository playerRepository;
    private final Map<PlayerState, StateHandler> stateHandlerMap = new HashMap<>();

    public MessageUpdateHandler(@Lazy CommandHandler commandHandler, @Lazy PlayerRepository playerRepository, List<StateHandler> stateHandlers) {
        this.commandHandler = commandHandler;
        this.playerRepository = playerRepository;
        for (StateHandler handlers : stateHandlers) {
            stateHandlerMap.put(handlers.getNameState(), handlers);   // заполняекм мапу
        }
    }


    @Override
    public void handleUpdate(Update update) {
        if (update.hasMessage()) {
            Long chatId = update.getMessage().getChatId();

            // Проверяем, является ли сообщение командой
            String text = update.getMessage().getText();
            if (text.startsWith("/")) {
                commandHandler.handleCommand(chatId, text); // Передаем команду в CommandHandler
            } else {
                PlayerState currentState = playerRepository.findByChatId(chatId).getState();
                StateHandler stateHandler = stateHandlerMap.get(currentState);
                if (stateHandler != null) {
                    stateHandler.executeState(update, chatId, text);
                }
            }
        }
    }
}