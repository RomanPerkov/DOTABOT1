package com.example.dotabot1.telegrambot.Commands;

import com.example.dotabot1.services.MessageGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.dotabot1.constants.Constants.CommandsConstants.HELP_COMMAND;


/**
 * Класс реализует интерфейс Command и предназначен для обработки команды HELP.
 * Аннотация @RequiredArgsConstructor генерирует конструктор с одним параметром для каждого final поля,
 * что позволяет Spring автоматически внедрять зависимости через этот конструктор.
 */
@Service
@RequiredArgsConstructor
public class HelpCommand implements Command {

    private final MessageGeneratorService messageGeneratorService;


    /**
     * Возвращает имя команды, которую этот класс обрабатывает.
     * @return имя команды
     */
    @Override
    public String getName() {
        return HELP_COMMAND;
    }


    /**
     * Метод выполняет действие, связанное с командой HELP — выводит справочное сообщение.
     * @param chatId идентификатор чата, в котором необходимо выполнить команду.
     */
    @Override
    public void executeCommand(Long chatId) {
        messageGeneratorService.helpMessage(chatId);
    }
}
