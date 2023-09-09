package com.example.dotabot1.config;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
@EnableScheduling
public class WebClientConfig {

    @Value("${dota.api.url}")
    private String dotaApiUrl;

    @Bean
    public WebClient webClient() {
        return WebClient.builder()
                .baseUrl(dotaApiUrl)
                .build();
    }
}