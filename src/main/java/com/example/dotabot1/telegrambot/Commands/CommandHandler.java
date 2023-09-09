package com.example.dotabot1.telegrambot.Commands;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Класс обработчик комманд входит в состав MessageUpdateHandler который обрабатывает сообщения с текстом.
 */
@Service
//@RequiredArgsConstructor
public class CommandHandler {
    private final  Map<String, Command> commandMap = new HashMap<>();

    public CommandHandler(List<Command> commands) {
        for (Command command : commands) {
            commandMap.put(command.getName(), command);
        }

    }

    public void handleCommand(Long chatId, String command) {   // тут происходит выборка какую комманду использовать
        Command handler = commandMap.get(command);
        if (handler != null) {
            handler.executeCommand(chatId);
        }
    }
}