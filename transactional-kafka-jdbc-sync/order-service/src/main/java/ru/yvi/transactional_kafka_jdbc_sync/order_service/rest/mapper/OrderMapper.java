package ru.yvi.transactional_kafka_jdbc_sync.order_service.rest.mapper;

import org.mapstruct.*;
import ru.yvi.transactional_kafka_jdbc_sync.order_service.model.OrderEntity;
import ru.yvi.transactional_kafka_jdbc_sync.order_service.rest.dto.OrderDTO;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING
)
public interface OrderMapper {
    OrderEntity toEntity(OrderDTO orderDTO);

    @AfterMapping
    default void linkItemEntities(@MappingTarget OrderEntity orderEntity) {
        orderEntity.getCartEntities().forEach(cartEntity -> cartEntity.setOrder(orderEntity));
    }

    OrderDTO toOrderDTO(OrderEntity orderEntity);
}