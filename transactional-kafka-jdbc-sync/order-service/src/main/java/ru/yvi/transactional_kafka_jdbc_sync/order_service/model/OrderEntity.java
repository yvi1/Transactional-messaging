package ru.yvi.transactional_kafka_jdbc_sync.order_service.model;

import jakarta.persistence.*;
import lombok.*;
import lombok.experimental.SuperBuilder;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import static ru.yvi.transactional_kafka_jdbc_sync.order_service.util.HibernateUtils.getEffectiveClass;

@SuperBuilder
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Data
@Entity
@Table(name = "orders")
@NamedEntityGraph(
        name = "order_with_cart",
        attributeNodes = { @NamedAttributeNode("cartEntities")}
)
public class OrderEntity extends BaseEntity {

    @Column(name = "customer_id")
    private UUID customerId;

    @Column(name = "address")
    private String address;

    @Column(name = "total_amount", precision = 19, scale = 4)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false)
    private OrderStatus orderStatus;

    @ToString.Exclude
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    @Builder.Default
    private Set<CartEntity> cartEntities = new LinkedHashSet<>();

    @Override
    public boolean equals(Object o) {
        return this == o || o instanceof OrderEntity order
                && getEffectiveClass(this) == getEffectiveClass(order)
                && this.getId() != null
                && Objects.equals(this.getId(), order.getId());
    }

    @Override
    public int hashCode() {
        return getEffectiveClass(this).hashCode();
    }

}
