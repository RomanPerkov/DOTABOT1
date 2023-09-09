package com.example.dotabot1.telegrambot;

import org.telegram.telegrambots.meta.api.objects.Update;


/**
 * абстракция для обработки сообщений ботом каждый класс реализующий интерфейс обрабатывает входящее сообещние
 *  своим образом в зависимости от того что пришло
 */
public interface UpdateHandler {
    void handleUpdate(Update update);
}
