package com.example.dotabot1.services.dotaapiservice;

import com.example.dotabot1.dto.matchesdetails.MatchDetails;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
public class OpenDotaApiService {

    private final WebClient webClientOpenDotaApi;

    public OpenDotaApiService(@Qualifier("webClientOpenDotaApi") WebClient webClientOpenDotaApi) {
        this.webClientOpenDotaApi = webClientOpenDotaApi;
    }

    public Mono<MatchDetails> getMatchDetails(String matchId, String key) {
        System.out.println(2222222);
        return webClientOpenDotaApi.get()
                .uri(uriBuilder -> uriBuilder
                        .queryParam("key", key)
                        .path("/api/matches/" + matchId)
                        .build())
                .retrieve()
                .bodyToMono(MatchDetails.class);
    }
}
