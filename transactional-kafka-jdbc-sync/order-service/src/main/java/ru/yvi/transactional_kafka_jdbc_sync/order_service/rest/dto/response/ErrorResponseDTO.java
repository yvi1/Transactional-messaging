package ru.yvi.transactional_kafka_jdbc_sync.order_service.rest.dto.response;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ErrorResponseDTO(
        @NotEmpty String message,
        @NotNull @Size(min = 100, max = 599) Integer status
) { }
