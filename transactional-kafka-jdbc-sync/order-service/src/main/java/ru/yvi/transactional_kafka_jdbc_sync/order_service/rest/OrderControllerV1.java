package ru.yvi.transactional_kafka_jdbc_sync.order_service.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.yvi.transactional_kafka_jdbc_sync.order_service.model.OrderEntity;
import ru.yvi.transactional_kafka_jdbc_sync.order_service.rest.dto.CreateOrderRequestDTO;
import ru.yvi.transactional_kafka_jdbc_sync.order_service.rest.dto.OrderResponseDTO;
import ru.yvi.transactional_kafka_jdbc_sync.order_service.service.OrderProcessor;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderControllerV1 {

    private final OrderProcessor orderProcessor;

    @PostMapping
    public OrderResponseDTO create(@RequestBody CreateOrderRequestDTO order) {
        return orderProcessor.create(order);
    }

    @GetMapping("/{id}")
    public OrderEntity getOne(@PathVariable UUID id) {
        return orderProcessor.getOrderOrThrow(id);
    }

    @GetMapping
    public ResponseEntity<List<OrderResponseDTO>> getAll() {
        return ResponseEntity.ok(orderProcessor.getAll());
    }
}
