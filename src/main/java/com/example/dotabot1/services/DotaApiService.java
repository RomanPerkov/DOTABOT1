package com.example.dotabot1.services;

import com.example.dotabot1.dto.player.Request;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


/**
 * Класс с методами для API
 */
@Service
@RequiredArgsConstructor// создаем конструктор с финальными полями для спринга
public class DotaApiService {

    private final WebClient webClient;

    public Mono<String> getPlayerMatchHistory(Long steamId, String key) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/IDOTA2Match_570/GetMatchHistory/v1")
                        .queryParam("key", key)
                        .queryParam("account_id", steamId)
                        .build())
                .retrieve()             // запускает выполнение http запроса
                .bodyToMono(String.class);
    }

    public Mono<String> getMatchDetails(Long matchId, String key) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/IDOTA2Match_570/GetMatchDetails/v1")
                        .queryParam("key", key)
                        .queryParam("match_id", matchId)
                        .build())
                .retrieve()
                .bodyToMono(String.class);
    }


    public Mono<Request> getPlayerStatus(String steamId, String key) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/ISteamUser/GetPlayerSummaries/v0002/")
                        .queryParam("key", key)
                        .queryParam("steamids", steamId)
                        .build())
                .retrieve()
                .bodyToMono(Request.class);
    }

//    public Mono<Request> getStatus (Long steamId, String key){
//        return webClient.get()
//                .uri(uriBuilder -> uriBuilder
//                .path("/ISteamUser/GetPlayerSummaries/v0002/")
//                        .queryParam("key", key)
//                        .queryParam("steamids", steamId)
//                        .build())
//                .retrieve()
//                .bodyToMono(Request.class);
//    }

}