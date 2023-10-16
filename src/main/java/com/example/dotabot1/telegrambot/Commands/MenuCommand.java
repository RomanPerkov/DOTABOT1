package com.example.dotabot1.telegrambot.Commands;

import com.example.dotabot1.services.keyboard.KeyboardGeneratorService;
import com.example.dotabot1.services.keyboard.KeyboardType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.dotabot1.constants.Constants.CommandsConstants.MENU_COMMAND;


/**
 * Класс реализует интерфейс Command и предназначен для обработки команды MENU.
 * Аннотация @RequiredArgsConstructor генерирует конструктор с одним параметром для каждого final поля,
 * что позволяет Spring автоматически внедрять зависимости через этот конструктор.
 */
@Service
@RequiredArgsConstructor
public class MenuCommand implements Command {

    private final KeyboardGeneratorService keyboardGeneratorService;

    /**
     * Возвращает имя команды, которую этот класс обрабатывает.
     * @return имя команды
     */
    @Override
    public String getName() {
        return MENU_COMMAND;
    }


    /**
     * Метод выполняет действие, связанное с командой MENU — отправляет пользователю клавиатуру с основным меню.
     * @param chatId идентификатор чата, в котором необходимо выполнить команду.
     */
    @Override
    public void executeCommand(Long chatId) {
        keyboardGeneratorService.sendKeyboard(KeyboardType.MAIN_MENU,chatId,"Выберите действие");
    }


}
