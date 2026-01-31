package ru.yvi.transactional_kafka_jdbc_sync.order_service.rest;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import ru.yvi.transactional_kafka_jdbc_sync.order_service.model.OrderEntity;
import ru.yvi.transactional_kafka_jdbc_sync.order_service.rest.dto.OrderDTO;
import ru.yvi.transactional_kafka_jdbc_sync.order_service.rest.mapper.OrderMapper;
import ru.yvi.transactional_kafka_jdbc_sync.order_service.service.OrderProcessor;

import java.util.UUID;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderControllerV1 {

    private final OrderProcessor orderProcessor;
    private final OrderMapper orderMapper;

    @PostMapping
    public OrderDTO create(@RequestBody OrderDTO orderRequest) {
        var entityToSave = orderMapper.toEntity(orderRequest);
        var saved = orderProcessor.create(entityToSave);
        return orderMapper.toOrderDTO(saved);
    }

    @GetMapping("/{id}")
    public OrderEntity getOne(@PathVariable UUID id) {
        return orderProcessor.getOrderOrThrow(id);
    }
}
