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


@Service
@RequiredArgsConstructor
public class UpdateSteamIdState implements StateHandler {
    private final PlayerRepository playerRepository;
    private final DotaApiService dotaApiService;
    private final MessageGeneratorService messageGeneratorService;
    private static final Logger logger = LoggerFactory.getLogger(UpdateSteamIdState.class);


    @Override
    public PlayerState getNameState() {
        return PlayerState.UPDATE_STEAM_ID;
    }


    @Override
    public void executeState(Update update, Long chatId, String messageText) {
        User user = playerRepository.findByChatId(chatId);

        if (user.getSteamApiKey() == null) {
            logger.warn("Steam API Key is null for user with chat ID: {}", chatId);
            messageGeneratorService.noApiKeyMessage(chatId);
            user.setState(PlayerState.DEFAULT);
            playerRepository.save(user);
            return;             // выход из метода
        }

        dotaApiService.getPlayerStatus(messageText, user.getSteamApiKey())
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
