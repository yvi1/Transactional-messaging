package ru.yvi.transactional_kafka_jdbc_sync.order_service.rest.dto;

import java.math.BigDecimal;

public record CartResponseDTO(
        Long itemId,
        String name,
        Integer quantity,
        BigDecimal priceAtPurchase
) {}
