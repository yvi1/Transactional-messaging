package ru.yvi.transactional_kafka_jdbc_sync.order_service.util;

import lombok.experimental.UtilityClass;
import ru.yvi.transactional_kafka_jdbc_sync.order_service.model.CartEntity;
import ru.yvi.transactional_kafka_jdbc_sync.order_service.model.OrderEntity;
import ru.yvi.transactionalkafkajdbcsync.commonlibs.http.order.OrderStatus;
import ru.yvi.transactionalkafkajdbcsync.commonlibs.http.order.request.CreateCartRequestDTO;
import ru.yvi.transactionalkafkajdbcsync.commonlibs.http.order.request.CreateOrderRequestDTO;
import ru.yvi.transactionalkafkajdbcsync.commonlibs.http.order.response.CartResponseDTO;
import ru.yvi.transactionalkafkajdbcsync.commonlibs.http.order.response.OrderResponseDTO;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Instant;
import java.util.Collections;
import java.util.Set;
import java.util.UUID;

@UtilityClass
public class DataUtils {

    public static final UUID UUID_CUSTOMER_1 = UUID.fromString("56899759-2adb-4586-a493-f3bb1d7fcc69");
    public static final UUID UUID_CUSTOMER_2 = UUID.fromString("4eefbabc-907f-45fd-8230-cabb8c69c50d");
    public static final UUID CUSTOMER_ID_WITHOUT_ORDERS = UUID.fromString("22222222-2222-2222-2222-222222222222");

    public static final UUID UUID_ORDER_1 = UUID.fromString("de1a887d-c2b8-4476-8cd7-af9bbe653e45");
    public static final UUID UUID_ORDER_2 = UUID.fromString("22222222-2222-2222-2222-222222222222");
    public static final UUID UUID_ORDER_3 = UUID.fromString("33333333-3333-3333-3333-333333333333");

    public static OrderEntity getOrderOneTransient() {
        return OrderEntity.builder()
                .customerId(UUID_CUSTOMER_1)
                .address("123 Main St., Boston, MA, USA")
                .cartEntities(Set.of(
                        CartEntity.builder()
                                .itemId(1L)
                                .name("Laptop")
                                .quantity(2)
                                .priceAtPurchase(new BigDecimal("50.00").setScale(2, RoundingMode.HALF_UP))
                                .build(),
                        CartEntity.builder()
                                .itemId(2L)
                                .name("Desktop")
                                .quantity(1)
                                .priceAtPurchase(new BigDecimal("100.00").setScale(2, RoundingMode.HALF_UP))
                                .build()
                ))
                .build();
    }

    public static OrderEntity getOrderTwoTransient() {
        return OrderEntity.builder()
                .customerId(UUID_CUSTOMER_2)
                .address("456 Maple St., Ney York, NY, USA")
                .cartEntities(Set.of(
                        CartEntity.builder()
                                .itemId(3L)
                                .name("T-shirt")
                                .quantity(1)
                                .priceAtPurchase(new BigDecimal("10.00").setScale(2, RoundingMode.HALF_UP))
                                .build(),
                        CartEntity.builder()
                                .itemId(4L)
                                .name("Sneakers")
                                .quantity(1)
                                .priceAtPurchase(new BigDecimal("60.00").setScale(2, RoundingMode.HALF_UP))
                                .build()
                ))
                .build();
    }

    public static OrderEntity getOrderThreeTransient() {
        return OrderEntity.builder()
                .customerId(UUID_CUSTOMER_1)
                .address("123 Main St., Boston, MA, USA")
                .cartEntities(Collections.emptySet())
                .build();
    }

    public static OrderEntity getOrderOnePersistent() {
        return OrderEntity.builder()
                .id(UUID_ORDER_1)
                .customerId(UUID_CUSTOMER_1)
                .address("123 Main St., Boston, MA, USA")
                .cartEntities(Set.of(
                        CartEntity.builder()
                                .itemId(1L)
                                .name("Laptop")
                                .quantity(2)
                                .priceAtPurchase(new BigDecimal("50.00").setScale(2, RoundingMode.HALF_UP))
                                .build(),
                        CartEntity.builder()
                                .itemId(2L)
                                .name("Desktop")
                                .quantity(1)
                                .priceAtPurchase(new BigDecimal("100.00").setScale(2, RoundingMode.HALF_UP))
                                .build()
                ))
                .build();
    }

    public static OrderEntity getOrderTwoPersistent() {
        return OrderEntity.builder()
                .id(UUID_ORDER_2)
                .customerId(UUID_CUSTOMER_1)
                .address("456 Maple St., Ney York, NY, USA")
                .cartEntities(Set.of(
                        CartEntity.builder()
                                .itemId(3L)
                                .name("Item3")
                                .quantity(3)
                                .priceAtPurchase(new BigDecimal("10.00").setScale(2, RoundingMode.HALF_UP))
                                .build(),
                        CartEntity.builder()
                                .itemId(4L)
                                .name("Item4")
                                .quantity(4)
                                .priceAtPurchase(new BigDecimal("20.00").setScale(2, RoundingMode.HALF_UP))
                                .build()
                ))
                .build();
    }

    public static OrderEntity getOrderThreePersistent() {
        return OrderEntity.builder()
                .id(UUID_ORDER_3)
                .customerId(UUID_CUSTOMER_2)
                .address("123 Main St., Boston, MA, USA")
                .cartEntities(Set.of(
                        CartEntity.builder()
                                .itemId(4L)
                                .name("Item4")
                                .quantity(1)
                                .priceAtPurchase(new BigDecimal("55.00").setScale(2, RoundingMode.HALF_UP))
                                .build(),
                        CartEntity.builder()
                                .itemId(5L)
                                .name("Item5")
                                .quantity(1)
                                .priceAtPurchase(new BigDecimal("45.00").setScale(2, RoundingMode.HALF_UP))
                                .build()
                ))
                .build();
    }

    public static CreateOrderRequestDTO getCreateOrderRequestDTO() {
        return new CreateOrderRequestDTO(
                UUID_CUSTOMER_1,
                "Address",
                Set.of(
                        new CreateCartRequestDTO(1L, "Item1", 2),
                        new CreateCartRequestDTO(2L, "Item2", 1)
                )
        );
    }

    public static CreateOrderRequestDTO getCreateOrderTwoRequestDTO() {
        return new CreateOrderRequestDTO(
                UUID_CUSTOMER_1,
                "Address",
                Set.of(
                        new CreateCartRequestDTO(3L, "Item3", 3),
                        new CreateCartRequestDTO(4L, "Item4", 4)
                )
        );
    }

    public static CreateOrderRequestDTO getCreateOrderThreeRequestDTO() {
        return new CreateOrderRequestDTO(
                UUID_CUSTOMER_2,
                "Address2",
                Set.of(
                        new CreateCartRequestDTO(5L, "Item5", 1),
                        new CreateCartRequestDTO(6L, "Item6", 1)
                )
        );
    }

    public static OrderResponseDTO getOrderResponseDTO() {
        var instantNow = Instant.now();
        return new OrderResponseDTO(
                UUID_ORDER_1,
                UUID_CUSTOMER_1,
                "Address",
                OrderStatus.PENDING_PAYMENT,
                new BigDecimal("150.00"),
                instantNow,
                instantNow,
                Set.of(
                        new CartResponseDTO(1L, "Item1", 2, new BigDecimal("100.00")),
                        new CartResponseDTO(2L, "Item2", 1, new BigDecimal("50.00"))
                )
        );
    }

    public static OrderResponseDTO getOrderTwoResponseDTO() {
        var instantNow = Instant.now();
        return new OrderResponseDTO(
                UUID_ORDER_2,
                UUID_CUSTOMER_1,
                "Address",
                OrderStatus.PENDING_PAYMENT,
                new BigDecimal("200.00"),
                instantNow,
                instantNow,
                Set.of(
                        new CartResponseDTO(1L, "Item3", 3, new BigDecimal("10.00")),
                        new CartResponseDTO(2L, "Item4", 4, new BigDecimal("5.00"))
                )
        );
    }

    public static OrderResponseDTO getOrderThreeResponseDTO() {
        var instantNow = Instant.now();
        return new OrderResponseDTO(
                UUID_ORDER_3,
                UUID_CUSTOMER_2,
                "Address2",
                OrderStatus.PENDING_PAYMENT,
                new BigDecimal("100.00"),
                instantNow,
                instantNow,
                Set.of(
                        new CartResponseDTO(1L, "Item5", 1, new BigDecimal("55.00")),
                        new CartResponseDTO(2L, "Item6", 1, new BigDecimal("45.00"))
                )
        );
    }
}
