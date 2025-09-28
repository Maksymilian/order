package com.example.order.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient inventoryServiceWebClient(WebClient.Builder webClientBuilder) {
        return webClientBuilder
//                TODO fix: use configuratoin property to connect to the service
                .baseUrl("http://api.servicea.com")
                .defaultHeader(ACCEPT, APPLICATION_JSON_VALUE)
                .build();
    }
}