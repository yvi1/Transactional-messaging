package ru.yvi.transactional_kafka_jdbc_sync.order_service.rest.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import ru.yvi.transactional_kafka_jdbc_sync.order_service.model.OrderEntity;
import ru.yvi.transactional_kafka_jdbc_sync.order_service.model.OrderStatus;

import java.math.BigDecimal;
import java.util.Set;
import java.util.UUID;

/**
 * DTO for {@link OrderEntity}
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public record OrderDTO(
        UUID id,
        UUID customerId,
        String address,
        BigDecimal totalAmount,
        OrderStatus orderStatus,
        Set<CartDTO> cartEntities) { }