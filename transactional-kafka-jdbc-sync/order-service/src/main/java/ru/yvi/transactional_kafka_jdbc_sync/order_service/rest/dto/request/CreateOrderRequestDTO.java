package ru.yvi.transactional_kafka_jdbc_sync.order_service.rest.dto.request;

import jakarta.annotation.Nullable;
import jakarta.validation.constraints.NotNull;

import java.util.Set;
import java.util.UUID;

public record CreateOrderRequestDTO(
        @NotNull UUID customerId,
        @Nullable String address,
        Set<CreateCartRequestDTO> items
) { }