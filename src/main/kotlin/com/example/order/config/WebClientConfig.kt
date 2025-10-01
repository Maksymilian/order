package com.example.order.config

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
        clientRegistrationRepository: ClientRegistrationRepository,
        authorizedClientRepository: OAuth2AuthorizedClientRepository,
        webClientBuilder: WebClient.Builder,
    ) = webClientBuilder
        .baseUrl("http://api.servicea.com")
        .filter(
            ServletOAuth2AuthorizedClientExchangeFilterFunction(
                clientRegistrationRepository,
                authorizedClientRepository,
            ).also {
                it.setDefaultClientRegistrationId("inventory-service-client")
                it.setDefaultOAuth2AuthorizedClient(true)
            },
        ).build()
}
