package ru.yvi.transactional_kafka_jdbc_sync.order_service.repository;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.yvi.transactional_kafka_jdbc_sync.order_service.model.OrderEntity;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<OrderEntity, UUID> {

    @Query("SELECT o FROM OrderEntity o")
    @EntityGraph("order_with_cart")
    List<OrderEntity> findAllByEntityGraph();
}