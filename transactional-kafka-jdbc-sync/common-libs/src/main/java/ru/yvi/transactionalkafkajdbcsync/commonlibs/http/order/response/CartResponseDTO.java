package ru.yvi.transactionalkafkajdbcsync.commonlibs.http.order.response;

import jakarta.validation.Valid;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;

public record CartResponseDTO(
        @NotNull @Valid Long itemId,
        @NotEmpty @Size(max = 50) String name,
        @Positive Integer quantity,
        @PositiveOrZero BigDecimal priceAtPurchase
) {}
