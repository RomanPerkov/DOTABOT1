package com.example.dotabot1.services.keyboard;

import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.ArrayList;
import java.util.List;

@Service
public class MainMenuKeyboard implements KeyboardBuilder {
    // Конкретная реализация для главного меню

    @Override
    public KeyboardType getName() {
        return KeyboardType.MAIN_MENU;
    }

    @Override
    public void build(SendMessage sendMessage) {
        InlineKeyboardMarkup inlineKeyboardMarkup = new InlineKeyboardMarkup();

        List<List<InlineKeyboardButton>> keyboard = new ArrayList<>();

        List<InlineKeyboardButton> row1 = new ArrayList<>();
        InlineKeyboardButton inlineKeyboardButton1 = new InlineKeyboardButton("Ввести API ключ");
        inlineKeyboardButton1.setCallbackData("/updatesteamapikey");
        InlineKeyboardButton inlineKeyboardButton2 = new InlineKeyboardButton("Ввести ID целевого аккаунта");
        inlineKeyboardButton2.setCallbackData("/updatesteamid");
        row1.add(inlineKeyboardButton1);
        row1.add(inlineKeyboardButton2);


        keyboard.add(row1);

        inlineKeyboardMarkup.setKeyboard(keyboard);
        sendMessage.setReplyMarkup(inlineKeyboardMarkup);
    }
}
