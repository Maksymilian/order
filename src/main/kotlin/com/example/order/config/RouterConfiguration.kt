package com.example.mongo_webflux.restaurant.config

import com.example.mongo_webflux.restaurant.api.OrderRouteHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.MediaType.APPLICATION_JSON
import org.springframework.web.reactive.function.server.router

@Configuration
internal class RouterConfiguration {

    @Bean
    fun apiRoutes(handler: OrderRouteHandler) = router {
        accept(APPLICATION_JSON).nest {
            POST("/api/order/place", handler::placeOrder)
        }
    }
}
