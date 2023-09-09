//package com.example.dotabot1.config;
//
//import lombok.Getter;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.context.properties.ConfigurationProperties;
//import org.springframework.stereotype.Component;
//
///**
// * Класс содержит конфигурационные данные токена ручки и т д , сами данные находятся в application.properties
// */
//@Component
//@ConfigurationProperties(prefix = "app")
//@Getter
//public class AppProperties {
//    private String apiKey;
//    private String apiSecret;
//
//    @Autowired
//    private AppProperties appProperties;
//
//    public void getApi() {
//        String apiKey = appProperties.getApiKey();
//    }
//
//
//}
