package com.example.dotabot1.telegrambot.Commands;

public interface Command {

    public String getName();
    void executeCommand(Long chatId);
}
