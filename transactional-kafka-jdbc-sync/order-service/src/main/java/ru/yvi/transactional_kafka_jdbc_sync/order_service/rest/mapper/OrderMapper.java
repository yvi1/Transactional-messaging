package ru.yvi.transactional_kafka_jdbc_sync.order_service.rest.mapper;

import org.mapstruct.*;
import ru.yvi.transactional_kafka_jdbc_sync.order_service.model.OrderEntity;
import ru.yvi.transactional_kafka_jdbc_sync.order_service.rest.dto.CreateOrderRequestDTO;
import ru.yvi.transactional_kafka_jdbc_sync.order_service.rest.dto.OrderResponseDTO;

@Mapper(
        unmappedTargetPolicy = ReportingPolicy.IGNORE,
        componentModel = MappingConstants.ComponentModel.SPRING,
        uses = CartMapper.class
)
public interface OrderMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "orderStatus", ignore = true)
    @Mapping(target = "totalAmount", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    @Mapping(target = "cartEntities", source = "items")
    OrderEntity toEntity(CreateOrderRequestDTO createOrderRequestDTO);

    @Mapping(target = "items", source = "cartEntities")
    @Mapping(target = "createdAt", source = "createdAt")
    @Mapping(target = "updatedAt", source = "updatedAt")
    OrderResponseDTO toResponseDTO(OrderEntity orderEntity);

    @AfterMapping
    default void linkCartEntities(@MappingTarget OrderEntity orderEntity) {
        if (orderEntity.getCartEntities() != null) {
            orderEntity.getCartEntities().forEach(cartEntity -> cartEntity.setOrder(orderEntity));
        }
    }
}