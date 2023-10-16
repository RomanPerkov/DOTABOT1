package com.example.dotabot1.telegrambot.states;


import com.example.dotabot1.entity.users.States.PlayerState;
import com.example.dotabot1.entity.users.User;
import com.example.dotabot1.repository.PlayerRepository;
import com.example.dotabot1.services.DotaApiService;
import com.example.dotabot1.services.MessageGeneratorService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.telegram.telegrambots.meta.api.objects.Update;

/**
 * Класс UpdateSteamApiKeyState реализует интерфейс StateHandler и отвечает за обработку
 * состояния обновления Steam API ключа пользователя.
 * Аннотация @RequiredArgsConstructor генерирует конструктор с одним параметром для каждого final поля,
 * что позволяет Spring автоматически внедрять зависимости через этот конструктор.
 */
@Service
@RequiredArgsConstructor
public class UpdateSteamApiKeyState implements StateHandler {


    private final PlayerRepository playerRepository;
    private final DotaApiService dotaApiService;
    private final MessageGeneratorService messageGeneratorService;
    private final static Logger logger = LoggerFactory.getLogger(UpdateSteamApiKeyState.class);

    /**
     * Возвращает имя состояния, которое этот класс обрабатывает.
     * @return имя состояния
     */
    @Override
    public PlayerState getNameState() {      // Получение текущего состояния (ENUM значение)
        return PlayerState.UPDATE_API_KEY;
    }

    /**
     * Метод выполняет действие, связанное с обновлением Steam API ключа.
     * @param update Объект Update из Telegram API
     * @param chatId идентификатор чата, в котором необходимо выполнить обновление.
     * @param messageText текст сообщения
     */
    @Override
    public void executeState(Update update, Long chatId, String messageText) {
        User user = playerRepository.findByChatId(chatId);          // Вызываем метод из dotaApiService и подписываемся на результат
        dotaApiService.getPlayerStatus("76561197971528467", messageText)
                .subscribe(
                        request -> {
                            // Успешное выполнение
                            user.setSteamApiKey(messageText);
                            user.setState(PlayerState.DEFAULT);
                            playerRepository.save(user);
                            messageGeneratorService.apiKeySuccessfullyAddedMessage(chatId);
                        },
                        error -> {                                                   // вторая лямбда , обработка ощибки
                            logger.error("Error while updating Steam API Key for user {}: {}", chatId, error.getMessage(), error);
                            messageGeneratorService.badApiKeyMessage(chatId);
                            user.setState(PlayerState.DEFAULT);
                            playerRepository.save(user);
                        },
                        () -> logger.info("Completed Processing complete for chat ID: {}", chatId)// третья лямбда, успешное завершение

                );

    }
}

