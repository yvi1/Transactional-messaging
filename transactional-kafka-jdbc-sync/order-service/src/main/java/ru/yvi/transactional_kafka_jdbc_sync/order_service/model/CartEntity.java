package ru.yvi.transactional_kafka_jdbc_sync.order_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.Objects;

import static ru.yvi.transactional_kafka_jdbc_sync.order_service.util.HibernateUtils.getEffectiveClass;

@Data
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@AllArgsConstructor
@Entity
@Table(name = "items")
public class CartEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private OrderEntity order;

    @Column(name = "item_id")
    private Long itemId;

    @Column(name = "quantity")
    private Integer quantity;

    @Column(name = "price_at_purchase")
    private BigDecimal priceAtPurchase;

    @Override
    public boolean equals(Object o) {
        return this == o || o instanceof CartEntity item
                && getEffectiveClass(this) == getEffectiveClass(item)
                && getId() != null
                && Objects.equals(getId(), item.getId());
    }

    @Override
    public int hashCode() {
        return getEffectiveClass(this).hashCode();
    }
}