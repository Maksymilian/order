package com.example.order.config

import org.slf4j.LoggerFactory
import org.springframework.boot.autoconfigure.web.WebProperties
import org.springframework.boot.web.error.ErrorAttributeOptions
import org.springframework.boot.webflux.autoconfigure.error.AbstractErrorWebExceptionHandler
import org.springframework.boot.webflux.error.ErrorAttributes
import org.springframework.context.ApplicationContext
import org.springframework.core.annotation.Order
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.codec.ServerCodecConfigurer
import org.springframework.stereotype.Component
import org.springframework.web.reactive.function.BodyInserters
import org.springframework.web.reactive.function.server.HandlerFunction
import org.springframework.web.reactive.function.server.RequestPredicates
import org.springframework.web.reactive.function.server.RouterFunction
import org.springframework.web.reactive.function.server.RouterFunctions
import org.springframework.web.reactive.function.server.ServerRequest
import org.springframework.web.reactive.function.server.ServerResponse
import reactor.core.publisher.Mono
import java.util.Map

@Component
@Order(-2)
class GlobalFunctionalExceptionHandler(
    errorAttributes: ErrorAttributes,
    applicationContext: ApplicationContext,
    serverCodecConfigurer: ServerCodecConfigurer,
) : AbstractErrorWebExceptionHandler(errorAttributes, WebProperties.Resources(), applicationContext) {
    companion object {
        private val logger = LoggerFactory.getLogger(GlobalFunctionalExceptionHandler::class.java)
    }

    init {
        super.setMessageWriters(serverCodecConfigurer.getWriters())
        super.setMessageReaders(serverCodecConfigurer.getReaders())
    }

    override fun getRoutingFunction(errorAttributes: ErrorAttributes): RouterFunction<ServerResponse> {
        return RouterFunctions.route<ServerResponse>(
            RequestPredicates.all(),
            HandlerFunction { request -> renderErrorResponse(request) },
        )
    }

    private fun renderErrorResponse(request: ServerRequest): Mono<ServerResponse> {
        val errorAttributes = getErrorAttributes(request, ErrorAttributeOptions.defaults())
        val status = HttpStatus.valueOf(errorAttributes.getOrDefault("status", 500) as Int)
        var message = errorAttributes.get("message") as String
        if (status.is5xxServerError()) {
            System.err.println("Unexpected Server Error: " + message)
            message = "An unexpected server error occurred."
        }
        val responseBody =
            Map.of<String?, String?>(
                "code",
                status.value().toString(),
                "error",
                status.getReasonPhrase(),
                "message",
                message,
            )

        return ServerResponse.status(status)
            .contentType(MediaType.APPLICATION_JSON)
            .body(BodyInserters.fromValue<MutableMap<String, String>>(responseBody))
    }
}
