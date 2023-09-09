package com.example.dotabot1.services;

import com.example.dotabot1.telegrambot.DotaBot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

@Service
@RequiredArgsConstructor
public class MessageGeneratorService {

    private final DotaBot dotaBot;


    public void messageSendler(Long chatId, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);
        try {
            dotaBot.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    public void generateWelcomeMessage(Long chatId) {
        messageSendler(chatId, "Добро пожаловать в DotaBot! Для завершения регистрации сядьте на бутылку.");
    }

    public void userHasAlreadyBeenCreated(Long chatId) {
        messageSendler(chatId, "Пошел нахуй пидор, хуйли ты жмешь то блядь, иди на бутылку сядь лучше.");
    }


    // Другие методы для создания разных типов сообщений
}