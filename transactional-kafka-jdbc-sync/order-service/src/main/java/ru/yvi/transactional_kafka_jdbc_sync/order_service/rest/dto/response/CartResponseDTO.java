package ru.yvi.transactional_kafka_jdbc_sync.order_service.rest.dto.response;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record CartResponseDTO(
        @NotNull @Valid Long itemId,
        @NotEmpty @Size(max = 50) String name,
        Integer quantity,
        @PositiveOrZero BigDecimal priceAtPurchase
) {}
