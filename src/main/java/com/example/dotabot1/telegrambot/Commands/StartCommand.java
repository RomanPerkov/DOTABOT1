package com.example.dotabot1.telegrambot.Commands;


import com.example.dotabot1.services.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.dotabot1.constants.Constants.CommandsConstants.START_COMMAND;


/**
 * Класс реализует интерфейс Command и предназначен для обработки команды START.
 * Аннотация @RequiredArgsConstructor генерирует конструктор с одним параметром для каждого final поля,
 * что позволяет Spring автоматически внедрять зависимости через этот конструктор.
 */
@Service
@RequiredArgsConstructor
public class StartCommand implements Command {

    private final  PlayerService playerService;


    /**
     * Возвращает имя команды, которую этот класс обрабатывает.
     * @return имя команды
     */
    @Override
    public String getName() {
        return START_COMMAND;
    }

    /**
     * Метод выполняет действие, связанное с командой START — регистрирует нового игрока.
     * @param chatId идентификатор чата, в котором необходимо выполнить команду.
     */
    @Override
    public void executeCommand(Long chatId) {
        playerService.registerNewPlayer(chatId);
    }
}
