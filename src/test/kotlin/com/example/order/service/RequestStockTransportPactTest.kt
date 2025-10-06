package com.example.order.service

import au.com.dius.pact.consumer.dsl.PactDslWithProvider
import au.com.dius.pact.consumer.junit5.PactConsumerTestExt
import au.com.dius.pact.consumer.junit5.PactTestFor
import au.com.dius.pact.core.model.V4Pact
import au.com.dius.pact.core.model.annotations.Pact
import com.example.order.model.Deduction
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.web.reactive.function.client.WebClient
import reactor.test.StepVerifier

@ExtendWith(PactConsumerTestExt::class)
@PactTestFor(providerName = "StockProvider", port = "8096")
class RequestStockTransportPactTest {
    @Pact(provider = "StockProvider", consumer = "test_consumer")
    fun requestTransport(builder: PactDslWithProvider) =
        builder
            .given("Prepared order")
            .uponReceiving("a request to request transport")
            .path("/requestTransport")
            .method("POST")
            .matchHeader(
                "Authorization",
                "^Bearer [A-Za-z0-9_-]+\\.[A-Za-z0-9_-]+\\.[A-Za-z0-9_-]+$",
                "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3O" +
                    "DkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWUsImlhdCI6MTUxNjIzO" +
                    "TAyMn0.KMUFsIDTnFmyG3nMiGM6H9FNFUROf3wh7SmqJp-QV30",
            )
            .body("""{"orderNumber":"20251004-00042","deductions":[{"sku":"TSHIRT-REG-BLU-L-25","quantity":4}]}""")
            .willRespondWith()
            .status(204)
            .headers(mapOf("Content-Type" to "application/json"))
            .toPact(V4Pact::class.java)

    @Test
    fun testStockTransportRequestPact() {
        val webClient =
            WebClient.builder()
                .baseUrl("http://localhost:8096")
                .defaultHeader(
                    "Authorization",
                    "Bearer eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3O" +
                        "DkwIiwibmFtZSI6IkpvaG4gRG9lIiwiYWRtaW4iOnRydWUsImlhdCI6MTUxNjIzO" +
                        "TAyMn0.KMUFsIDTnFmyG3nMiGM6H9FNFUROf3wh7SmqJp-QV30",
                )
                .build()
        val port = StockWebClientPort(webClient)
        val request =
            ReserveClientRequest(
                orderNumber = "20251004-00042",
                listOf(Deduction(sku = "TSHIRT-REG-BLU-L-25", quantity = 4)),
            )
        StepVerifier.create(port.reserveStock(request))
            .expectNextMatches { it is StockPortResult.StockPortSuccess }
            .verifyComplete()
    }
}
