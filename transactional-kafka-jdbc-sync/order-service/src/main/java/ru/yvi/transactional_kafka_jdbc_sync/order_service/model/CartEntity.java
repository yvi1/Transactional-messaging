package ru.yvi.transactional_kafka_jdbc_sync.order_service.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.Objects;

import static ru.yvi.transactional_kafka_jdbc_sync.order_service.util.HibernateUtils.getEffectiveClass;

@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Data
@Entity
@Table(name = "carts")
public class CartEntity extends BaseEntity {

    @ToString.Exclude
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private OrderEntity order;

    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "item_name", nullable = false, length = 50)
    private String name;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "price_at_purchase")
    private BigDecimal priceAtPurchase;

    @Override
    public boolean equals(Object o) {
        return this == o || o instanceof CartEntity cart
                && getEffectiveClass(this) == getEffectiveClass(cart)
                && this.getId() != null
                && Objects.equals(this.getId(), cart.getId());
    }

    @Override
    public int hashCode() {
        return getEffectiveClass(this).hashCode();
    }
}