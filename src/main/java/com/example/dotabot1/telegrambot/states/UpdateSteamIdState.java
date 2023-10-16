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
 * Класс UpdateSteamIdState реализует интерфейс StateHandler и отвечает за обработку
 * состояния обновления Steam ID пользователя.
 * Аннотация @RequiredArgsConstructor генерирует конструктор с одним параметром для каждого final поля,
 * что позволяет Spring автоматически внедрять зависимости через этот конструктор.
 */
@Service
@RequiredArgsConstructor
public class UpdateSteamIdState implements StateHandler {
    private final PlayerRepository playerRepository;
    private final DotaApiService dotaApiService;
    private final MessageGeneratorService messageGeneratorService;
    private static final Logger logger = LoggerFactory.getLogger(UpdateSteamIdState.class);

    /**
     * Возвращает имя состояния, которое этот класс обрабатывает.
     * @return имя состояния
     */
    @Override
    public PlayerState getNameState() {
        return PlayerState.UPDATE_STEAM_ID;
    }

    /**
     * Метод выполняет действие, связанное с обновлением Steam ID.
     * @param update Объект Update из Telegram API
     * @param chatId идентификатор чата, в котором необходимо выполнить обновление.
     * @param messageText текст сообщения
     */
    @Override
    public void executeState(Update update, Long chatId, String messageText) {
        User user = playerRepository.findByChatId(chatId);
        if (user.getSteamApiKey() == null) {            // Проверка наличия Steam API ключа
            logger.warn("Steam API Key is null for user with chat ID: {}", chatId);
            messageGeneratorService.noApiKeyMessage(chatId);
            user.setState(PlayerState.DEFAULT);
            playerRepository.save(user);
            return;             // выход из метода
        }

        dotaApiService.getPlayerStatus(messageText, user.getSteamApiKey())      // Запрос к DotaApiService
                .subscribe(
                        request -> {
                            // Успешное выполнение
                            user.setSteamId(messageText);
                            user.setState(PlayerState.DEFAULT);
                            playerRepository.save(user);
                            messageGeneratorService.steamIdSuccessfullyAddedMessage(chatId);
                        },
                        error -> {
                            // Обработка ошибки
                            logger.error("Error while updating Steam API Key for user {}: {}", chatId, error.getMessage(), error);
                            messageGeneratorService.badSteamIdMessage(chatId);
                            user.setState(PlayerState.DEFAULT);
                            playerRepository.save(user);
                        },
                        () -> {
                            // Успешное завершение
                            logger.info("Processing complete for chat ID: {}", chatId);
                        }
                );
    }
}
