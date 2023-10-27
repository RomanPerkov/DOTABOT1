package com.example.dotabot1.telegrambot.states;

import com.example.dotabot1.entity.users.User;

/**
 * Функциональный интерфейс создан для реализации лямбд-выражений в реактивных запросах.
 * Позволяет определить действие на основе состояния пользователя и входного текста в контексте Telegram-бота.
 */
@FunctionalInterface
public interface StateAction {
    void apply(User user, String text);
}
