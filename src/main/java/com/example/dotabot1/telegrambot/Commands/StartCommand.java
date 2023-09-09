package com.example.dotabot1.telegrambot.Commands;


import com.example.dotabot1.services.PlayerService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StartCommand implements Command {

    private final  PlayerService playerService;



    @Override
    public String getName() {
        return "/start";
    }

    @Override
    public void executeCommand(Long chatId) {
        playerService.registerNewPlayer(chatId);
    }
}
