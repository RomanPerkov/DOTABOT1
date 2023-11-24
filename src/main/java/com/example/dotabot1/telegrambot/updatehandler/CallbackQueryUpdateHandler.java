package com.example.dotabot1.telegrambot.updatehandler;

import com.example.dotabot1.services.keyboardservice.KeyboardGeneratorService;
import com.example.dotabot1.telegrambot.DotaBot;
import com.example.dotabot1.telegrambot.commands.CommandHandler;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.meta.api.objects.Update;
import org.telegram.telegrambots.meta.exceptions.TelegramApiException;

/**
 * Класс CallbackQueryUpdateHandler реализует интерфейс UpdateHandler и служит для обработки callback-запросов
 * от inline клавиатуры в Telegram.
 */
@Service
public class CallbackQueryUpdateHandler implements UpdateHandler {

    private final KeyboardGeneratorService keyboardGeneratorService;

    private final CommandHandler commandHandler;

    private final DotaBot dotaBot;

    /**
     * Конструктор класса. Spring автоматически внедряет зависимости через этот конструктор.
     *
     * @param commandHandler           Обработчик команд
     * @param keyboardGeneratorService Сервис для работы с клавиатурой
     * @param dotaBot экземпляр бота
     */
    public CallbackQueryUpdateHandler(@Lazy CommandHandler commandHandler, KeyboardGeneratorService keyboardGeneratorService, DotaBot dotaBot) {
        this.keyboardGeneratorService = keyboardGeneratorService;
        this.commandHandler = commandHandler;
        this.dotaBot = dotaBot;
    }

    /**
     * Метод для обработки callback-запросов от Telegram API. Вызывается при нажатии на кнопку inline клавиатуры.
     * @param update Объект Update из Telegram API.
     */
    @Override
    public void handleUpdate(Update update) {
        if (update.hasCallbackQuery()) {
            Long chatId = update.getCallbackQuery().getMessage().getChatId();
            String callbackData = update.getCallbackQuery().getData();
            commandHandler.handleCommand(chatId, callbackData);  // передаем команду в CommandHandler
            // Отправляем AnswerCallbackQuery
            AnswerCallbackQuery answer = new AnswerCallbackQuery();
            answer.setCallbackQueryId(update.getCallbackQuery().getId());
            try {
                dotaBot.execute(answer);
            } catch (TelegramApiException e) {
                e.printStackTrace();
            }
            // Удаление клавиатуры
            keyboardGeneratorService.removeKeyboard(chatId, update.getCallbackQuery().getMessage().getMessageId());

        }

    }
}
