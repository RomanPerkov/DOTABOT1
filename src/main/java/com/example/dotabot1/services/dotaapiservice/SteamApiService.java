package com.example.dotabot1.services.dotaapiservice;

import com.example.dotabot1.model.dto.matches.RequestHistory;
import com.example.dotabot1.model.dto.matchesdetails.MatchDetails;
import com.example.dotabot1.model.dto.player.Request;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


/**
 * Класс с реактивынми методами для API
 */
@Service
public class SteamApiService {

    private final WebClient webClientSteamApi;

    public SteamApiService(@Qualifier("webClientSteamApi") WebClient webClientSteamApi) {
        this.webClientSteamApi = webClientSteamApi;
    }


    /**
     * Метод получает список матчей
     * @param steamId
     * @param key
     * @return реактивный поток с сущностью истории матчей
     */
    public Mono<RequestHistory> getPlayerMatchHistory(String steamId, String key) {
        return webClientSteamApi.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/IDOTA2Match_570/GetMatchHistory/v1")
                        .queryParam("key", key)
                        .queryParam("account_id", steamId)
                        .build())
                .retrieve()             // запускает выполнение http запроса
                .bodyToMono(RequestHistory.class);
    }


    /**
     * Метод получает сущность стим аккаунта
     * @param steamId стим айди стим аккаунта
     * @param key   стим АПИ ключ разработчика
     * @return  реактивный поток с сущностью стим аккаунта
     */
    public Mono<Request> getPlayerStatus(String steamId, String key) {
        return webClientSteamApi.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/ISteamUser/GetPlayerSummaries/v0002/")
                        .queryParam("key", key)
                        .queryParam("steamids", steamId)
                        .build())
                .retrieve()
                .bodyToMono(Request.class);
    }

    /**
     * метод получает подробности матча
     * @param matchId  айди матча в доте
     * @param key  стим АПИ ключ разработчика
     * @return реактивный поток с сущностью матча
     */

    public Mono<MatchDetails> getMatchDetails(String matchId, String key) {
        return webClientSteamApi.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("key", key)
                        .queryParam("match_id",matchId)
                        .path("/IDOTA2Match_570/GetMatchDetails/v001/")
                        .build())
                .retrieve()
                .bodyToMono(MatchDetails.class);
    }


}