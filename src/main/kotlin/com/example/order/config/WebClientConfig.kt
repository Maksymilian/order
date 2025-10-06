package com.example.order.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.oauth2.client.ReactiveOAuth2AuthorizedClientManager
import org.springframework.security.oauth2.client.web.reactive.function.client.ServerOAuth2AuthorizedClientExchangeFilterFunction
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientConfig {
    @Bean
    fun inventoryServiceWebClient(
        authorizedClientManager: ReactiveOAuth2AuthorizedClientManager,
        @Value("\${stock.url}") stockUrl: String,
        webClientBuilder: WebClient.Builder,
    ) = webClientBuilder
        .baseUrl(stockUrl)
        .filter(
            ServerOAuth2AuthorizedClientExchangeFilterFunction(authorizedClientManager)
                .also { authorizedClientManager -> authorizedClientManager.setDefaultClientRegistrationId("shop") },
        )
        .build()
}
