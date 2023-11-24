package com.example.dotabot1.telegrambot.commands;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Класс CommandHandler является центральным обработчиком команд в Telegram боте.
 * Он отвечает за маршрутизацию команд на соответствующие исполнители.
 *
 * Особенности:
 * - Использует Map для хранения доступных команд и их обработчиков.
 * - Метод handleCommand выполняет поиск и выполнение конкретной команды на основе входящего текста.
 *
 * Обратите внимание:
 * - Благодаря Spring, в конструктор автоматически инжектируется список всех бинов, реализующих интерфейс Command.
 *   Это позволяет автоматически инициализировать Map с доступными командами.
 */
@Service
public class CommandHandler {
    private final  Map<String, Command> commandMap = new HashMap<>();

    /**
     * Конструктор класса. Принимает список команд и добавляет их в commandMap.
     * Список команд автоматически инжектируется Spring'ом.
     * @param commands Список команд для обработки.
     */
    public CommandHandler(List<Command> commands) {
        for (Command command : commands) {
            commandMap.put(command.getName(), command);
        }

    }

    /**
     * Метод handleCommand отвечает за поиск и выполнение команды на основе входящего текста.
     *
     * Шаги выполнения:
     * 1. Осуществляет поиск команды в commandMap по ключу (имя команды).
     * 2. Если команда найдена, вызывает метод executeCommand для выполнения действий.
     *
     * @param chatId уникальный идентификатор чата пользователя.
     * @param command текстовое представление команды.
     */
    public void handleCommand(Long chatId, String command) {   // тут происходит выборка какую комманду использовать
        Command handler = commandMap.get(command);
        if (handler != null) {
            handler.executeCommand(chatId);
        }
    }
}