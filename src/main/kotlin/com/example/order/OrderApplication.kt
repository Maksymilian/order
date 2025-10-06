package com.example.order

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.config.EnableMongoAuditing
import org.springframework.data.mongodb.repository.config.EnableReactiveMongoRepositories
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity

@SpringBootApplication
@EnableReactiveMongoRepositories
@EnableMongoAuditing
@EnableWebFluxSecurity
class OrderApplication

fun main(args: Array<String>) {
    runApplication<OrderApplication>(*args)
}
