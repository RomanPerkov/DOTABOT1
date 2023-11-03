package com.example.dotabot1.telegrambot.Commands;


import com.example.dotabot1.services.GameMatchService;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import static com.example.dotabot1.constants.Constants.CommandsConstants.GET_MATCHES_LIST;


/**
 * Класс команда для получения информации о сыгранных матчах
 */
@Service
@RequiredArgsConstructor
public class GetMatchesIDListCommand implements Command {
    private final GameMatchService gameMatchService;

    private static final Logger logger = LoggerFactory.getLogger(GetMatchesIDListCommand.class);


    @Override
    public String getName() {
        return GET_MATCHES_LIST;
    }


    /**
     * В этом классе вызывается метод (цпочка методов которая получает данные сыгранных матчей и
     * определяет выйграл или проиграл игрок, отправляет сообщение с этой информцией пользователю)
     * @param chatId телеграм айди пользователя
     *               передается в метод matchPlayedOverThePast24Hours
     *               в аргментах метода так же передается реактивный поток Mono c типом User, далее в теле метода этот пользователь извлекается и передается цепочке
     *               методов для использования его в работе
     */
    public void executeCommand(Long chatId) {
        gameMatchService.matchPlayedOverThePast24Hours(chatId, gameMatchService.getUserByChatId(chatId)).subscribe();
    }
}
