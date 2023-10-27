package com.example.dotabot1.telegrambot.Commands;


import com.example.dotabot1.repository.PlayerRepository;
import com.example.dotabot1.services.GameMatchService;
import com.example.dotabot1.services.MessageGeneratorService;
import com.example.dotabot1.services.dotaapiservice.SteamApiService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static com.example.dotabot1.constants.Constants.CommandsConstants.GET_MATCHES_LIST;

@Service
@RequiredArgsConstructor
public class GetMatchesIDListCommand implements Command {

    private final SteamApiService steamApiService;
    private final MessageGeneratorService messageGeneratorService;
    private final PlayerRepository playerRepository;

    private final GameMatchService gameMatchService;

    private static final Logger logger = LoggerFactory.getLogger(GetMatchesIDListCommand.class);



    @Override
    public String getName() {
        return GET_MATCHES_LIST;
    }

    public void executeCommand(Long chatId) {
        gameMatchService.checkLastMatchWin(chatId)
                .doOnNext(isWin -> {
                    if (isWin) {
                        messageGeneratorService.playerIsWin(chatId);  // Отправляем сообщение о том, что игрок выиграл
                        logger.info("Player with chatId {} won the game", chatId);
                    } else {
                        messageGeneratorService.playerIsLose(chatId); // Отправляем сообщение о том, что игрок проиграл
                        logger.info("Player with chatId {} lost the game", chatId);
                    }
                })
                .doOnSuccess(result -> {
                    // Логируем успешное завершение потока
                    logger.info("Successfully completed the operation for chatId {}", chatId);
                })
                .doOnError(e -> {
                    // Логируем ошибку
                    logger.error("An error occurred for chatId {}: {}", chatId, e.getMessage());
                })
                .subscribe();
    }

}