package com.example.inventory.security

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod.POST
import org.springframework.security.config.web.server.ServerHttpSecurity
import org.springframework.security.oauth2.jwt.NimbusReactiveJwtDecoder
import org.springframework.security.oauth2.jwt.ReactiveJwtDecoder
import org.springframework.security.web.server.SecurityWebFilterChain
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository


// TODO A potential enhancement: implement with Spring Kotlin Bean Definition DSL
@Configuration
class SecurityConfig {

    @Bean
    fun reactiveJwtDecoder(
        @Value("\${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
        issuerUri: String
    ): ReactiveJwtDecoder = NimbusReactiveJwtDecoder.withIssuerLocation(issuerUri)
        .build()

    @Bean
    fun securityWebFilterChain(
        http: ServerHttpSecurity,
        jwtConverter: JwtConverter,
        reactiveJwtDecoder: ReactiveJwtDecoder
    ): SecurityWebFilterChain =
        http.csrf { it.disable() }
            .oauth2ResourceServer { oauth2 ->
                oauth2.jwt { jwt ->
                    jwt.jwtAuthenticationConverter(jwtConverter)
                    jwt.jwtDecoder(reactiveJwtDecoder)
                }
            }.authorizeExchange { exchanges ->
                exchanges
                    .pathMatchers(POST, "/api/order/place").hasAuthority("ADD_ORDER")
                    .anyExchange().authenticated()
            }.securityContextRepository(NoOpServerSecurityContextRepository.getInstance())
            .build()
}