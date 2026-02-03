package ru.yvi.transactional_kafka_jdbc_sync.order_service.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yvi.transactional_kafka_jdbc_sync.order_service.rest.dto.request.CreateOrderRequestDTO;
import ru.yvi.transactional_kafka_jdbc_sync.order_service.rest.dto.response.OrderResponseDTO;
import ru.yvi.transactional_kafka_jdbc_sync.order_service.service.OrderProcessor;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderControllerV1 {

    private final OrderProcessor orderProcessor;

    @PostMapping
    public ResponseEntity<OrderResponseDTO> createOrder(@RequestBody CreateOrderRequestDTO order) {
        return ResponseEntity.status(HttpStatus.CREATED).body(orderProcessor.create(order));
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderResponseDTO> getOrderById(@PathVariable UUID id) {
        return ResponseEntity.ok().body(orderProcessor.getOrderOrThrow(id));
    }

    @GetMapping("/client/{id}")
    public ResponseEntity<List<OrderResponseDTO>> getAllOrdersByClientId(@PathVariable("id") Long id) {
        return ResponseEntity.ok(orderProcessor.getAllById(id));
    }
}
