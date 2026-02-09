package ru.yvi.transactionalkafkajdbcsync.commonlibs.http.error;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ErrorResponseDTO(
        @NotEmpty String name,
        @NotNull @Size(min = 100, max = 599) Integer code,
        @NotEmpty String description,
        @NotEmpty String message
) { }
