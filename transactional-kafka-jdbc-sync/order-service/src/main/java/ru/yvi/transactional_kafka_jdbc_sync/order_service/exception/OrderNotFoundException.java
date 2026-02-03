package ru.yvi.transactional_kafka_jdbc_sync.order_service.exception;

public class OrderNotFoundException extends RuntimeException {

    public OrderNotFoundException(String message) {
        super(message);
    }
}
