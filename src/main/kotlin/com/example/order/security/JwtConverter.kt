package com.example.inventory.security

import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.core.convert.converter.Converter
import org.springframework.security.authentication.AbstractAuthenticationToken
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.oauth2.jwt.Jwt
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter
import org.springframework.stereotype.Component
import reactor.core.publisher.Mono
import java.util.stream.Collectors

@Component
class JwtConverter : Converter<Jwt, Mono<AbstractAuthenticationToken>> {

    @Value("\${clientId}")
    private val clientId: String? = null

    override fun convert(jwt: Jwt): Mono<AbstractAuthenticationToken> {
        return Mono.fromSupplier {
            val springAuthorities = JwtGrantedAuthoritiesConverter().convert(jwt)
            val authorities = (springAuthorities?.toSet() ?: emptySet()) + customAuthorities(jwt)
            JwtAuthenticationToken(jwt, authorities, jwt.subject)
        }
    }

    private fun customAuthorities(jwt: Jwt): Set<GrantedAuthority> {
        val resourceAccess = jwt.getClaim<Map<String, Any>>("resource_access").toMap()[clientId] as Map<*, *>
        val roles = resourceAccess["roles"] as List<*>
        return roles.asSequence()
            .map { it.toString() }
            .map { it.replace('-', '_') }
            .map { it.uppercase() }
            .map(::SimpleGrantedAuthority)
            .toSet()
    }
}