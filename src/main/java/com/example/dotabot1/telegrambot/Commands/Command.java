package com.example.dotabot1.telegrambot.Commands;


/**
 * Интерфейс комманд
 */
public interface Command {

     String getName();
    void executeCommand(Long chatId);
}
