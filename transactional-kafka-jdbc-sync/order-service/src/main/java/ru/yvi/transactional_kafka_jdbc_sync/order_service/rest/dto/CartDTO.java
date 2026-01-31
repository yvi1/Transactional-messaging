package ru.yvi.transactional_kafka_jdbc_sync.order_service.rest.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.yvi.transactional_kafka_jdbc_sync.order_service.model.CartEntity;

import java.math.BigDecimal;

/**
 * DTO for {@link CartEntity}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record CartDTO(
        Long id,
        Long itemId,
        Integer quantity,
        BigDecimal priceAtPurchase) { }