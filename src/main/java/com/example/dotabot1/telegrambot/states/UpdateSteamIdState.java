package com.example.dotabot1.telegrambot.states;

import com.example.dotabot1.model.entity.users.States.PlayerState;
import com.example.dotabot1.model.entity.users.User;
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
public class UpdateSteamIdState implements StateHandler {
    private final PlayerRepository playerRepository;
    private final SteamApiService steamApiService;
    private final MessageGeneratorService messageGeneratorService;
    private final StateExecutor stateExecutor;
    private static final Logger logger = LoggerFactory.getLogger(UpdateSteamIdState.class);

    /**
     * Возвращает имя состояния, которое этот класс обрабатывает.
     *
     * @return имя состояния
     */
    @Override
    public PlayerState getNameState() {
        return PlayerState.UPDATE_STEAM_ID;
    }


    @Override
    public void executeState ( Long chatId, String messageText){
        User user = playerRepository.findByChatId(chatId);
        if (user.getSteamApiKey() == null) {            // Проверка наличия Steam API ключа
            logger.warn("Steam API Key is null for user with chat ID: {}", chatId);
            messageGeneratorService.noApiKeyMessage(chatId);
            user.setState(PlayerState.DEFAULT);
            playerRepository.save(user);
            return;             // выход из метода
        }
        stateExecutor.execute(
                user,
                chatId,
                messageText,
                steamApiService.getPlayerStatus(messageText, user.getSteamApiKey()),
                (inuser, text) -> {

                    user.setSteamId(messageText);
                    user.setState(PlayerState.DEFAULT);
                    playerRepository.save(user);
                    messageGeneratorService.steamIdSuccessfullyAddedMessage(chatId);
                },
                (inuser, text)->
                {
                    // Обработка ошибки
                    logger.error("Error while updating Steam API Key for user {}", chatId);
                    user.setState(PlayerState.DEFAULT);
                    playerRepository.save(user);
                    messageGeneratorService.badSteamIdMessage(chatId);
                }
                );
    }



}
