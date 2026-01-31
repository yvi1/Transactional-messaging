package ru.yvi.transactional_kafka_jdbc_sync.order_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.yvi.transactional_kafka_jdbc_sync.order_service.model.CartEntity;

@Repository
public interface CartRepository extends JpaRepository<CartEntity, Long> {
}