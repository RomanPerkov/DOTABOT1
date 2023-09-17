package com.example.dotabot1.telegrambot.Commands;

import com.example.dotabot1.entity.users.States.PlayerState;
import com.example.dotabot1.repository.PlayerRepository;
import com.example.dotabot1.services.MessageGeneratorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;


@Service
@RequiredArgsConstructor
public class UpdateSteamIdCommand implements Command {
    private final PlayerRepository playerRepository;
    private final MessageGeneratorService messageGeneratorService;


    @Override
    public String getName() {
        return "/updatesteamid";
    }

    @Override
    public void executeCommand(Long chatId) {
        playerRepository.findByChatId(chatId).setState(PlayerState.UPDATE_STEAM_ID);
        messageGeneratorService.updateSteamIdMessage(chatId);
    }
}

