package com.example.dotabot1.сontroller;

import com.example.dotabot1.model.entity.users.User;
import com.example.dotabot1.repository.PlayerRepository;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class PlayerController {

    private final PlayerRepository playerRepository;

    public PlayerController(PlayerRepository playerRepository) {
        this.playerRepository = playerRepository;
    }

    @PostMapping("/players")
    public User addPlayer(@RequestBody User player) {
        return playerRepository.save(player);
    }
}