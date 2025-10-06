package com.example.order

import com.example.order.mapper.Mapper
import com.example.order.model.CreateOrder
import com.example.order.model.Deduction
import com.example.order.model.OrderNotPlaced
import com.example.order.model.OrderPlaced
import com.example.order.model.OrderResult
import com.example.order.service.OrderDocumentSaveSuccess
import com.example.order.service.OrderNumberService
import com.example.order.service.OrderPort
import com.example.order.service.OrderPortResult
import com.example.order.service.PlaceOrderService
import com.example.order.service.ReserveClientRequest
import com.example.order.service.StockPort
import com.example.order.service.StockPortResult
import com.example.order.service.StockPortResult.StockPortFailure
import com.example.order.service.StockPortResult.StockPortSuccess
import com.example.order.service.StockTransportRequestSuccess
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Spy
import org.mockito.junit.jupiter.MockitoExtension
import reactor.core.publisher.Mono
import reactor.test.StepVerifier
import java.math.BigDecimal
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.util.List

@ExtendWith(MockitoExtension::class)
internal class PlaceOrderServiceTest {
    private lateinit var service: PlaceOrderService

    @Spy
    private val mapper = Mapper()

    @Mock
    private lateinit var orderNumberService: OrderNumberService

    @Mock
    private lateinit var orderPort: OrderPort

    @Mock
    private lateinit var stockPort: StockPort

    @BeforeEach
    fun setUp() {
        service = PlaceOrderService(mapper, orderPort, orderNumberService, stockPort)
    }

    @Test
    @DisplayName("✅ Successfully reserved stock in inventory service and stored order in order service")
    fun testPlaceOrderService() {
//        arrange

        val paymentBankAccountNumber = "123456789"
        val totalAmount = BigDecimal("100.0")
        val number = "ORD-001"
        val deductions = List.of<Deduction>(Deduction("STR-AR4-BLU-10", 3), Deduction("ALR-BE0-WHT-42", 1))
        val orderNumber = "ORD-001"
        val fixedInstant = this.fixedDate
        Mockito.`when`<Mono<String>>(orderNumberService.generateNextNumber())
            .thenReturn(Mono.just<String>(orderNumber))
        val success = StockPortSuccess(StockTransportRequestSuccess())
        Mockito.`when`<Mono<StockPortResult<StockTransportRequestSuccess>>>(
            stockPort.reserveStock(
                ReserveClientRequest(orderNumber, deductions),
            ),
        )
            .thenReturn(Mono.just<StockPortResult<StockTransportRequestSuccess>>(success))
        val orderPortResult =
            OrderPortResult.OrderPortResultSuccess<OrderDocumentSaveSuccess>(
                OrderDocumentSaveSuccess("409b508e-dd65-402c-8fc7-5a120dcaf80a", fixedInstant, orderNumber),
            )
        Mockito.`when`<Mono<OrderPortResult<OrderDocumentSaveSuccess>>>(
            orderPort.storeOrder(
                CreateOrder(
                    paymentBankAccountNumber,
                    totalAmount,
                    number,
                    deductions,
                ),
            ),
        )
            .thenReturn(Mono.just<OrderPortResult<OrderDocumentSaveSuccess>>(orderPortResult))

        //        act
        val command = CreateOrder(paymentBankAccountNumber, totalAmount, number, deductions)

        //        assert
        StepVerifier.create<OrderResult>(service.placeOrder(command))
            .expectNext(OrderPlaced(fixedInstant, orderNumber))
            .verifyComplete()
    }

    @Test
    @DisplayName("❌ Inventory service fails to reserve stock")
    fun testInventoryPortFailure() {
//        arrange

        val paymentBankAccountNumber = "123456789"
        val totalAmount = BigDecimal("100.0")
        val number = "ORD-001"
        val deductions = List.of<Deduction?>(Deduction("STR-AR4-BLU-10", 3), Deduction("ALR-BE0-WHT-42", 1))
        val orderNumber = "ORD-001"
        val fixedInstant = this.fixedDate
        Mockito.`when`<Mono<String>>(orderNumberService.generateNextNumber())
            .thenReturn(Mono.just<String>(orderNumber))
        val inventoryPortRequest = ReserveClientRequest(orderNumber, deductions)
        val inventoryPortResult = StockPortFailure("Service is currently down")
        Mockito.`when`<Mono<StockPortResult<StockTransportRequestSuccess>>>(
            stockPort.reserveStock(
                inventoryPortRequest,
            ),
        ).thenReturn(Mono.just<StockPortResult<StockTransportRequestSuccess>>(inventoryPortResult))
        val orderPortResult =
            OrderPortResult.OrderPortResultSuccess<OrderDocumentSaveSuccess>(
                OrderDocumentSaveSuccess("409b508e-dd65-402c-8fc7-5a120dcaf80a", fixedInstant, orderNumber),
            )
        Mockito.`when`<Mono<OrderPortResult<OrderDocumentSaveSuccess>>>(
            orderPort.storeOrder(
                CreateOrder(
                    paymentBankAccountNumber,
                    totalAmount,
                    number,
                    deductions,
                ),
            ),
        )
            .thenReturn(Mono.just<OrderPortResult<OrderDocumentSaveSuccess>>(orderPortResult))

        //        act
        val command = CreateOrder(paymentBankAccountNumber, totalAmount, number, deductions)

        //        assert
        StepVerifier.create<OrderResult>(service.placeOrder(command))
            .expectNext(OrderNotPlaced("Inventory service problem"))
            .verifyComplete()
        Mockito.verify<StockPort?>(stockPort, Mockito.only()).reserveStock(inventoryPortRequest)
    }

    val fixedDate: Instant
        get() {
            val fixedDate = LocalDate.of(2025, 10, 2)
            val zone = ZoneId.of("America/New_York")
            val startOfDayInZone = fixedDate.atStartOfDay(zone)
            return startOfDayInZone.toInstant()
        }
}
