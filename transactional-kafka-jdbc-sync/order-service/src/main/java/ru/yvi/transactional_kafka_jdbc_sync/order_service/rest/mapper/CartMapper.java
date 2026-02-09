package ru.yvi.transactional_kafka_jdbc_sync.order_service.rest.mapper;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import ru.yvi.transactional_kafka_jdbc_sync.order_service.model.CartEntity;
import ru.yvi.transactionalkafkajdbcsync.commonlibs.http.order.response.CartResponseDTO;
import ru.yvi.transactionalkafkajdbcsync.commonlibs.http.order.request.CreateCartRequestDTO;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface CartMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "order", ignore = true)
    @Mapping(target = "priceAtPurchase", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    @Mapping(target = "updatedAt", ignore = true)
    CartEntity toEntity(CreateCartRequestDTO dto);

    CartResponseDTO toResponseDTO(CartEntity entity);
}
