package com.example.order.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository
import org.springframework.security.oauth2.client.web.OAuth2AuthorizedClientRepository
import org.springframework.security.oauth2.client.web.reactive.function.client.ServletOAuth2AuthorizedClientExchangeFilterFunction
import org.springframework.web.reactive.function.client.WebClient

@Configuration
class WebClientConfig {
    @Bean
    fun inventoryServiceWebClient(
        @Value("\${order.clientId}") clientId: String,
        @Value("\${stock.url}") stockUrl: String,
        clientRegistrationRepository: ClientRegistrationRepository,
        authorizedClientRepository: OAuth2AuthorizedClientRepository,
        webClientBuilder: WebClient.Builder,
    ) = webClientBuilder
        .baseUrl(stockUrl)
        .filter(
            ServletOAuth2AuthorizedClientExchangeFilterFunction(
                clientRegistrationRepository,
                authorizedClientRepository,
            ).also {
                it.setDefaultClientRegistrationId(clientId)
                it.setDefaultOAuth2AuthorizedClient(true)
            },
        ).build()
}
