package com.example.dotabot1.telegrambot;

import com.example.dotabot1.telegrambot.updatehandler.UpdateHandler;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.meta.api.objects.Update;
import java.util.List;


/**
 * Класс DotaBot наследует от TelegramLongPollingBot и представляет собой основную логику бота.
 *
 * Основная функциональность:
 * - Прием и обработка входящих обновлений (сообщений, команд и т.д.) от Telegram API.
 *
 */
@Service
public class DotaBot extends TelegramLongPollingBot {

    // Spring автоматически внедряет все бины, реализующие интерфейс UpdateHandler(это механизм самого спринга он самм выполянет
    // add всех найденых бинов которые реализуют интерфейс UpdateHandler)
    private final List<UpdateHandler> updateHandlers;

    private final String botUsername;

    private final String botToken;


    public DotaBot(@Value("${bot.username}") String botUsername,                // через конструктор для того что бы поля были финал
                   @Value("${bot.token}") String botToken,
                  @Lazy List<UpdateHandler> updateHandlers) {
        this.botUsername = botUsername;
        this.botToken = botToken;
        this.updateHandlers = updateHandlers;
    }

    @Override
    public void onUpdateReceived(Update update) {
        for (UpdateHandler handler : updateHandlers) { //перебор листа и применение к каждому элементу листа своей реализации UpdateHandler
            handler.handleUpdate(update);
        }
    }




    @Override
    public String getBotUsername() {
        return botUsername;
    }

    @Override
    public String getBotToken() {
        return botToken;
    }


    }
