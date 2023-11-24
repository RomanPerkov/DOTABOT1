package com.example.dotabot1.services.keyboardservice;


import com.example.dotabot1.telegrambot.DotaBot;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.send.SendMessage;
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageReplyMarkup;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 Класс KeyboardGeneratorService отвечает за генерацию и управление клавиатурами в боте..
 */
@Service
public class KeyboardGeneratorService {

    private final DotaBot dotaBot;    // Поле для доступа к функциональности бота

    private final Map<KeyboardType, KeyboardBuilder> keyboardBuilders;   // Карта для хранения различных видов клавиатур, где каждый тип клавиатуры ассоциирован с его конструктором

    /**
     * Конструктор для KeyboardGeneratorService.
     *
     * - Spring Framework автоматически добавляет в коллекцию Map<KeyboardType, KeyboardBuilder> все классы, реализующие интерфейс KeyboardBuilder. Это позволяет легко добавлять новые типы клавиатур.
     * @param dotaBot экземпляр класса DotaBot
     * @param keyboardBuildersList список всех конструкторов клавиатур
     */
    public KeyboardGeneratorService(DotaBot dotaBot, List<KeyboardBuilder> keyboardBuildersList) {
        this.dotaBot = dotaBot;
        this.keyboardBuilders = new HashMap<>();
        for (KeyboardBuilder builder : keyboardBuildersList) {          // Инициализация карты конструкторов клавиатур
            this.keyboardBuilders.put(builder.getName(), builder);
        }
    }


    /**
     * Удаление клавиатуры из сообщения.
     *
     * @param chatId ID чата, в котором находится сообщение
     * @param messageId ID сообщения, из которого нужно удалить клавиатуру
     */
    public void removeKeyboard(Long chatId, Integer messageId) {
        EditMessageReplyMarkup editMarkup = new EditMessageReplyMarkup();
        editMarkup.setChatId(chatId.toString());
        editMarkup.setMessageId(messageId);
        editMarkup.setReplyMarkup(null);
        try {
            dotaBot.execute(editMarkup);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Отправка клавиатуры в чат.
     *
     * @param keyboardName тип клавиатуры, которую нужно отправить
     * @param chatId ID чата, в который нужно отправить клавиатуру
     * @param message сообщение, которое будет отправлено вместе с клавиатурой
     */
    public void sendKeyboard(KeyboardType keyboardName,Long chatId, String message) {
        SendMessage sendMessage = new SendMessage();
        sendMessage.setChatId(chatId);
        sendMessage.setText(message);

        KeyboardBuilder keyboardBuilder = keyboardBuilders.get(keyboardName);
        if (keyboardBuilder != null) {
            keyboardBuilder.build(sendMessage);
        } else {
            // TODO: Обработать случай, когда нужной клавиатуры нет в списке
        }

        try {
            dotaBot.execute(sendMessage);
        } catch (TelegramApiException e) {
            throw new RuntimeException(e);
        }
    }

}
