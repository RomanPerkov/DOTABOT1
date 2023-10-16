package com.example.dotabot1.telegrambot;


import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.telegram.telegrambots.meta.TelegramBotsApi;
import org.telegram.telegrambots.updatesreceivers.DefaultBotSession;


/**
 * Класс инициализатор бота
 */
@Component
@RequiredArgsConstructor
public class BotInit {

    private final DotaBot dotabot;

    @EventListener({ApplicationReadyEvent.class})   // выполнения метода после запуска приложения
    @SneakyThrows
    public void init() {
        TelegramBotsApi telegramBotsApi = new TelegramBotsApi(DefaultBotSession.class);
        try {
            telegramBotsApi.registerBot(dotabot);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
