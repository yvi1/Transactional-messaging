package ru.yvi.transactional_kafka_jdbc_sync.order_service.rest.dto;

import ru.yvi.transactional_kafka_jdbc_sync.order_service.model.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public record OrderResponseDTO(
        UUID id,
        UUID customerId,
        String address,
        OrderStatus orderStatus,
        BigDecimal totalAmount,
        Instant createdAt,
        Instant updatedAt,
        Set<CartResponseDTO> items
) {}
