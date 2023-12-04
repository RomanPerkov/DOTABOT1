package com.example.dotabot1.telegrambot.commands;

import com.example.dotabot1.model.entity.users.States.PlayerState;
import com.example.dotabot1.model.entity.users.User;
import com.example.dotabot1.repository.PlayerRepository;
import com.example.dotabot1.services.MessageGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.example.dotabot1.constants.Constants.CommandsConstants.UPDATE_STEAM_API_KEY_COMMAND;


/**
 * Класс реализует интерфейс Command и предназначен для обработки команды UPDATE_STEAM_API_KEY.
 * Аннотация @RequiredArgsConstructor генерирует конструктор с одним параметром для каждого final поля,
 * что позволяет Spring автоматически внедрять зависимости через этот конструктор.
 */
@Service
@RequiredArgsConstructor
public class UpdateSteamApiKeyCommand implements Command {

    private final PlayerRepository playerRepository;
    private final MessageGeneratorService messageGeneratorService;

    /**
     * Возвращает имя команды, которую этот класс обрабатывает.
     * @return имя команды
     */
    @Override
    public String getName() {
        return UPDATE_STEAM_API_KEY_COMMAND;
    }

    /**
     * Метод выполняет действие, связанное с командой UPDATE_STEAM_API_KEY — обновляет Steam API Key пользователя в сущности.
     * @param chatId идентификатор чата, в котором необходимо выполнить команду.
     */
    @Override
    public void executeCommand(Long chatId) {
        User user =  playerRepository.findByChatId(chatId);
        user.setState(PlayerState.UPDATE_API_KEY);
        playerRepository.save(user);
        messageGeneratorService.updateSteamApiMessage(chatId);

    }
}


