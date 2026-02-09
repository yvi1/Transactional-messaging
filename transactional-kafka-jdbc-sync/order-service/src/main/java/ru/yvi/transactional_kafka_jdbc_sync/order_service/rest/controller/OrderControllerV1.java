package ru.yvi.transactional_kafka_jdbc_sync.order_service.rest.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.yvi.transactional_kafka_jdbc_sync.order_service.rest.api.OrderAPI;
import ru.yvi.transactionalkafkajdbcsync.commonlibs.http.order.request.CreateOrderRequestDTO;
import ru.yvi.transactionalkafkajdbcsync.commonlibs.http.order.response.OrderResponseDTO;
import ru.yvi.transactional_kafka_jdbc_sync.order_service.service.OrderProcessor;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderControllerV1 implements OrderAPI {

    private final OrderProcessor orderProcessor;

    /**
     * POST /api/v1/orders : Create new order
     *
     * @param order (required)
     * @return Order created (status code 201)
     */
    @Override
    public ResponseEntity<OrderResponseDTO> createOrder(@Valid @RequestBody CreateOrderRequestDTO order) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderProcessor.create(order));
    }

    /**
     * GET /api/v1/orders/customer/{id} : Find all orders by customer id
     *
     * @param id (required)
     * @return All orders found (status code 200)
     */
    @Override
    public ResponseEntity<List<OrderResponseDTO>> getAllOrdersByCustomerId(UUID id) {
        return ResponseEntity.ok(orderProcessor.getAllById(id));
    }

    /**
     * GET /api/v1/orders/{id} : Find order by id
     *
     * @param id (required)
     * @return Order found (status code 200)
     */
    @Override
    public ResponseEntity<OrderResponseDTO> getOrderById(UUID id) {
        return ResponseEntity.ok().body(orderProcessor.getOrderOrThrow(id));
    }
}
