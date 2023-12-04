package com.example.dotabot1.telegrambot.states;

import com.example.dotabot1.model.entity.users.States.PlayerState;
import com.example.dotabot1.repository.PlayerRepository;
import com.example.dotabot1.services.MessageGeneratorService;
import com.example.dotabot1.services.dotaapiservice.SteamApiService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


/**
 * Класс UpdateSteamIdState реализует интерфейс StateHandler и отвечает за обработку
 * состояния обновления Steam ID пользователя.
 */
@Service
@RequiredArgsConstructor
public class UpdateSteamApiKeyState implements StateHandler {
    private final StateExecutor stateExecutor;
    private final PlayerRepository playerRepository;
    private final MessageGeneratorService messageGeneratorService;

    private final SteamApiService steamApiService;
    private static final Logger logger = LoggerFactory.getLogger(UpdateSteamApiKeyState.class);

    @Override
    public PlayerState getNameState() {
        return PlayerState.UPDATE_API_KEY;
    }


    /**
     * Метод выполняет метод выполняет асинхронный запрос к API
     * @param chatId айди чата в телеграмм
     * @param messageText передаваемый пользователем API ключ
     */
    @Override
    public void executeState( Long chatId, String messageText) {
        stateExecutor.execute(
                playerRepository.findByChatId(chatId),          // получает пользователя из БД
                chatId,
                messageText,
                steamApiService.getPlayerStatus("76561197971528467", messageText),  // выполняет асинхронный запрос к АПИ
                (user, text) -> {
                    user.setSteamApiKey(text);
                    user.setState(PlayerState.DEFAULT);
                    playerRepository.save(user);
                    messageGeneratorService.apiKeySuccessfullyAddedMessage(chatId);
                },
                (user, text) -> {
                    logger.error("Error while updating Steam API Key for user {}", chatId);
                    user.setState(PlayerState.DEFAULT);
                    playerRepository.save(user);
                    messageGeneratorService.badApiKeyMessage(chatId);
                }

        );
    }
}
