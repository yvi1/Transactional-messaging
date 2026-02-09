package ru.yvi.transactionalkafkajdbcsync.commonlibs.http.order;

public enum OrderStatus {
    PENDING_PAYMENT,
    PAYMENT_CONFIRMED,
    PAYMENT_FAILED,
    REFUNDED,
}
