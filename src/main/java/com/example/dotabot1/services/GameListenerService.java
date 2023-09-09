package com.example.dotabot1.services;


import com.example.dotabot1.dto.player.Request;
import com.example.dotabot1.entity.users.User;
import com.example.dotabot1.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class GameListenerService {
    private final DotaApiService dotaApiService;
    private final PlayerRepository playerRepository;


    public void dotaApiCall() {
        try (Stream<User> users = playerRepository.streamAll()) {
            users.forEach(user -> {
                Request request = dotaApiService.getPlayerStatus(user.getSomeIdField(), "YOUR_API_KEY");
                System.out.println(request.getResponse().getPlayers().get(0).getPersonaname());
            });
        }
    }

//    //@Scheduled(fixedRate = 60000)
//    public void dotaApiCall(){
//       Mono<Request> mono =dotaApiService.getPlayerStatus("76561197971528467","1702DA19FEC3590A779EF2BBABD3AF47");
//        mono.subscribe(Request-> System.out.println(Request.getResponse().getPlayers().get(0).getPersonaname()));
//    }


}
