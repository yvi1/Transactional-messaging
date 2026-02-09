package ru.yvi.transactionalkafkajdbcsync.commonlibs.http.order.request;

import jakarta.validation.constraints.NotNull;

import java.util.Set;
import java.util.UUID;

public record CreateOrderRequestDTO(
        @NotNull UUID customerId,
        String address,
        Set<CreateCartRequestDTO> items
) { }