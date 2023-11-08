package com.example.dotabot1.telegrambot.Commands;


import com.example.dotabot1.services.GameMatchService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.dotabot1.constants.Constants.CommandsConstants.GET_MATCHES_LIST_COMMAND;


/**
 * Класс команда для получения информации о сыгранных матчах
 */
@Service
@RequiredArgsConstructor
public class GetMatchesIDListCommand implements Command {
    private final GameMatchService gameMatchService;




    @Override
    public String getName() {
        return GET_MATCHES_LIST_COMMAND;
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
        gameMatchService.matchPlayedOverThePastHours(chatId, gameMatchService.getUserByChatId(chatId), 72F).subscribe();
    }
}
