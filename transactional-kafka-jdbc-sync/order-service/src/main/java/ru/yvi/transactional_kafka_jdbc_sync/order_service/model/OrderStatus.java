package ru.yvi.transactional_kafka_jdbc_sync.order_service.model;

public enum OrderStatus {
    PENDING_PAYMENT,
    PAYMENT_CONFIRMED,
    PAYMENT_FAILED,
    REFUNDED,
}
