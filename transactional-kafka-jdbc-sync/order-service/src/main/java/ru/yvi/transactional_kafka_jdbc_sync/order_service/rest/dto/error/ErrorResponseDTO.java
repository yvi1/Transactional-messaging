package ru.yvi.transactional_kafka_jdbc_sync.order_service.rest.dto.error;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.List;

public record ErrorResponseDTO(
        @NotEmpty String name,
        @NotNull @Size(min = 100, max = 599) Integer code,
        @NotEmpty String description,
        @NotEmpty String message
) { }
