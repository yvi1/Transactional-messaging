package ru.yvi.transactional_kafka_jdbc_sync.order_service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import ru.yvi.transactional_kafka_jdbc_sync.order_service.model.OrderEntity;
import ru.yvi.transactional_kafka_jdbc_sync.order_service.model.OrderStatus;
import ru.yvi.transactional_kafka_jdbc_sync.order_service.repository.OrderRepository;
import ru.yvi.transactional_kafka_jdbc_sync.order_service.rest.dto.CreateOrderRequestDTO;
import ru.yvi.transactional_kafka_jdbc_sync.order_service.rest.dto.OrderResponseDTO;
import ru.yvi.transactional_kafka_jdbc_sync.order_service.rest.mapper.OrderMapper;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@RequiredArgsConstructor
@Service
@Transactional(readOnly = true)
public class OrderProcessor {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;

    @Transactional
    public OrderResponseDTO create(CreateOrderRequestDTO orderDTO) {

        log.info("Received order: {}", orderDTO);
        var entityToSave = orderMapper.toEntity(orderDTO);

        calculateTotalAmount(entityToSave);

        entityToSave.setOrderStatus(OrderStatus.PENDING_PAYMENT);
        var saved = orderRepository.save(entityToSave);
        log.info("Saved order: {}", saved);

        return orderMapper.toResponseDTO(saved);
    }

    public OrderEntity getOrderOrThrow(UUID id) {
        var orderEntityOptional = orderRepository.findById(id);
        return orderEntityOptional
                .orElseThrow(() -> {
                    log.error("Entity with id `{}` not found", id);
                    return new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id));
                });
    }

    private void calculateTotalAmount(OrderEntity entity) {

        // artificial delay to simulate external service call
        try {
            long delay = ThreadLocalRandom.current().nextLong(100, 501);
            Thread.sleep(delay);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("Interrupted during artificial delay when calculating total amount");
            throw new RuntimeException("Interrupted during artificial delay", e);
        }

        // assign random price to each item
        BigDecimal totalAmount = BigDecimal.ZERO;
        if (!entity.getCartEntities().isEmpty()) {

            for (var item : entity.getCartEntities()) {
                double randomPrice = ThreadLocalRandom.current().nextDouble(100, 1000);
                BigDecimal price = BigDecimal.valueOf(randomPrice)
                        .setScale(4, RoundingMode.HALF_UP);

                item.setPriceAtPurchase(price);

                BigDecimal itemTotal = price.multiply(BigDecimal.valueOf(item.getQuantity()))
                        .setScale(4, RoundingMode.HALF_UP);

                totalAmount = totalAmount.add(itemTotal);
            }
        }
        entity.setTotalAmount(totalAmount);
        log.debug("Calculated total amount: {} for entity: {}", totalAmount, entity);
    }
}
