package ru.yvi.transactional_kafka_jdbc_sync.order_service.exception;

public class OrderException extends  RuntimeException {

    public OrderException(String message) {
        super(message);
    }

    public OrderException(String message, Object... args) {
        super(String.format(message, args));
    }
}
