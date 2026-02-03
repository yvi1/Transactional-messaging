package ru.yvi.transactional_kafka_jdbc_sync.order_service.rest.dto.response;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import ru.yvi.transactional_kafka_jdbc_sync.order_service.model.OrderStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Set;
import java.util.UUID;

public record OrderResponseDTO(

        @NotNull @Valid UUID id,
        UUID customerId,
        @Size(max = 100) String address,
        OrderStatus orderStatus,
        @PositiveOrZero BigDecimal totalAmount,
        @NotNull Instant createdAt,
        @NotNull Instant updatedAt,
        Set<CartResponseDTO> items
) {}
