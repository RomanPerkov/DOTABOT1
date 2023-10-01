package com.example.dotabot1.services;

import com.example.dotabot1.telegrambot.DotaBot;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;


/**
 * Класс генератор сообщений
 */
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

    public void userHasAlreadyBeenCreatedMessage(Long chatId) {
        messageSendler(chatId, "для того что посмотреть список доступных команд введите /help.");
    }

    public void helpMessage(Long chatId){
        messageSendler(chatId, "список доступных команд:" +
                "/updatesteamapikey - ввести свой стим API ключ" +
                "/updatesteamid - ввести стим айди целевого аккаунта");

    }

    public void updateSteamApiMessage(Long chatId){
        messageSendler(chatId,"введите Ваш Api ключ");
    }

    public void updateSteamIdMessage(Long chatId){
        messageSendler(chatId,"введите steamId целевого аккаунта Steam");
    }


    public void badApiKeyMessage(Long chatId) {
        messageSendler(chatId, "Вы ввели неправипльный API токен");
    }

    public void apiKeySuccessfullyAddedMessage(Long chatId) {
        messageSendler(chatId, "API токен успешно добавлен");
    }

    public void badSteamIdMessage(Long chatId) {
        messageSendler(chatId, "Вы ввели неправипльный Steam ID ");
    }

     public void steamIdSuccessfullyAddedMessage(Long chatId) {
        messageSendler(chatId, "Steam ID успешно добавлен ");
    }



 public void noApiKeyMessage(Long chatId) {
        messageSendler(chatId, "Вы не ввели API токен ");
    }


    // Другие методы для создания разных типов сообщений
}