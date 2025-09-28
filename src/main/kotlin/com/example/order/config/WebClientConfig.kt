package com.example.order.config

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientConfig {
    @Bean
    fun inventoryServiceWebClient(webClientBuilder: WebClient.Builder): WebClient {
        return webClientBuilder //                TODO fix: use configuratoin property to connect to the service
            .baseUrl("http://api.servicea.com")
            .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE)
            .build()
    }
}
