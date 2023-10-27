package com.example.dotabot1.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.reactive.function.client.WebClient;


/**
 * Конфигурационный класс
 */
@Configuration
@EnableScheduling
public class WebClientConfig {



    @Bean(name = "webClientSteamApi")
    public WebClient webClientSteamApi(@Value("${dota.api.urlDota}") String baseUrl) {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    @Bean(name = "webClientOpenDotaApi")
    public WebClient webClientOpenDotaApi(@Value("${dota.api.urlDotaAPI}") String baseUrl) {
        return WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }
}