package com.example.dotabot1.telegrambot.commands;


/**
 * Интерфейс комманд
 */
public interface Command {

     String getName();
    void executeCommand(Long chatId);
}
