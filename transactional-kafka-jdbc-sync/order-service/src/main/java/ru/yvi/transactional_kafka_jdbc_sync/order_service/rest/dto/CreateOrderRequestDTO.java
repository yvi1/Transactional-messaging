package ru.yvi.transactional_kafka_jdbc_sync.order_service.rest.dto;

import jakarta.validation.constraints.NotNull;

import java.util.Set;
import java.util.UUID;

public record CreateOrderRequestDTO(
        @NotNull UUID customerId,
        String address,
        Set<CreateCartRequestDTO> items
) { }