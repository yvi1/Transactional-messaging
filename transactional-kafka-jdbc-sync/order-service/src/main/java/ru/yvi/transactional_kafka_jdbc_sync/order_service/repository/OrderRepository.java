package ru.yvi.transactional_kafka_jdbc_sync.order_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yvi.transactional_kafka_jdbc_sync.order_service.model.OrderEntity;

import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {
}