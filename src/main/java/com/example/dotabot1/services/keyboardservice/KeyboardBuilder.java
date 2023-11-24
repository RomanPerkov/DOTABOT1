package com.example.dotabot1.services.keyboardservice;

import org.telegram.telegrambots.meta.api.methods.send.SendMessage;


/**
 * Интерфейс клавиатур , его имплементируют классы описывающие клавиатуры
 */
public interface KeyboardBuilder {

    KeyboardType getName();

    void  build(SendMessage sendMessage);
}
