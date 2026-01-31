package ru.yvi.transactional_kafka_jdbc_sync.order_service.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

import static ru.yvi.transactional_kafka_jdbc_sync.order_service.util.HibernateUtils.getEffectiveClass;


@Data
@Builder
@NoArgsConstructor(access = AccessLevel.PRIVATE, force = true)
@AllArgsConstructor
@Entity
@Table(name = "orders")
public class OrderEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id", nullable = false)
    private UUID id;

    @Column(name = "customer_id")
    private UUID customerId;

    @Column(name = "address")
    private String address;

    @Column(name = "total_amount", precision = 19, scale = 4)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status", nullable = false)
    private OrderStatus orderStatus;

    @OneToMany(mappedBy = "order", cascade = CascadeType.PERSIST)
    private Set<CartEntity> cartEntities = new LinkedHashSet<>();


    @Override
    public boolean equals(Object o) {
        return this == o || o instanceof OrderEntity order
                && getEffectiveClass(this) == getEffectiveClass(order)
                && getId() != null
                && Objects.equals(getId(), order.getId());
    }

    @Override
    public int hashCode() {
        return getEffectiveClass(this).hashCode();
    }

}
