package com.example.order.security

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
import kotlin.collections.get

@Component
class JwtConverter : Converter<Jwt, Mono<AbstractAuthenticationToken>> {
    @Value("\${shop.clientId}")
    private val clientId: String? = null

    override fun convert(jwt: Jwt): Mono<AbstractAuthenticationToken> {
        return Mono.fromSupplier {
            val springAuthorities = JwtGrantedAuthoritiesConverter().convert(jwt)
            val authorities = (springAuthorities?.toSet() ?: emptySet()) + customAuthorities(jwt)
            JwtAuthenticationToken(jwt, authorities, jwt.subject)
        }
    }

    private fun customAuthorities(jwt: Jwt): Set<GrantedAuthority> {
        val resourceAccessClaim = jwt.getClaim<Any>("resource_access")
        val resourceAccess = (resourceAccessClaim as? Map<*, *>) ?: return emptySet()
        val clientResource = clientId?.let { resourceAccess[it] as? Map<*, *> } ?: return emptySet()
        val roles = clientResource["roles"] as? List<*> ?: return emptySet()
        return roles.asSequence()
            .mapNotNull { it?.toString() }
            .map { it.replace('-', '_') }
            .map { it.uppercase() }
            .map(::SimpleGrantedAuthority)
            .toSet()
    }
}
