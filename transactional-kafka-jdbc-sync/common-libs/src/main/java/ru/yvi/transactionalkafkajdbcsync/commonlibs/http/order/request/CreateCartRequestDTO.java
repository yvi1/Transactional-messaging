package ru.yvi.transactionalkafkajdbcsync.commonlibs.http.order.request;

import jakarta.validation.constraints.*;

public record CreateCartRequestDTO(
        @NotNull Long itemId,
        @NotBlank @Size(max = 50) String name,
        @NotNull @Positive Integer quantity
) { }