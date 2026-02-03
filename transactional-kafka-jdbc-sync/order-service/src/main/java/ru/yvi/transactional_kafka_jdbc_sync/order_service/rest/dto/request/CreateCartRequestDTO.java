package ru.yvi.transactional_kafka_jdbc_sync.order_service.rest.dto.request;

import jakarta.validation.constraints.*;

public record CreateCartRequestDTO(
        @NotNull Long itemId,
        @NotBlank @Size(max = 50) String name,
        @NotNull @Positive Integer quantity
) { }