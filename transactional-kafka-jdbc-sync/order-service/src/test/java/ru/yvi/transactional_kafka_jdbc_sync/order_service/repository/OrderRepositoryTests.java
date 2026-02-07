package ru.yvi.transactional_kafka_jdbc_sync.order_service.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.boot.jpa.test.autoconfigure.TestEntityManager;
import ru.yvi.transactional_kafka_jdbc_sync.order_service.model.OrderEntity;
import ru.yvi.transactional_kafka_jdbc_sync.order_service.model.OrderStatus;
import ru.yvi.transactional_kafka_jdbc_sync.order_service.util.DataUtils;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.LinkedHashSet;
import java.util.NoSuchElementException;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.yvi.transactional_kafka_jdbc_sync.order_service.util.DataUtils.getOrderOneTransient;

@DataJpaTest
public class OrderRepositoryTests {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private OrderRepository orderRepository;

    @BeforeEach
    public void setUp() { orderRepository.deleteAll(); }

    @Test
    @DisplayName("Test saving order functionality")
    public void givenOrder_whenSave_thenOrderSaved() {
        // given
        OrderEntity orderToSave = getOrderOneTransient();
        // when
        OrderEntity orderSaved = orderRepository.save(orderToSave);
        // then
        assertThat(orderSaved).isNotNull();
        assertThat(orderSaved.getId()).isNotNull();
        assertThat(orderSaved.getCartEntities()).isNotEmpty();
    }

    @Test
    @DisplayName("Test updating order functionality")
    public void givenOrderForUpdate_whenSave_thenOrderInformationChanged() {
        // given
        var order = DataUtils.getOrderOneTransient();
        var initialCartItems = new LinkedHashSet<>(
                Set.of(
                        DataUtils.getCartItemOneTransient(),
                        DataUtils.getCartItemTwoTransient()
                )
        );
        order.setCartEntities(initialCartItems);
        order.setOrderStatus(OrderStatus.PENDING_PAYMENT);
        orderRepository.save(order);

        // when
        var savedOrder = orderRepository.findById(order.getId()).orElseThrow(() -> new NoSuchElementException("Order not found"));
        savedOrder.setAddress("234 Elm Street");
        savedOrder.setTotalAmount(new BigDecimal("300.00").setScale(2, RoundingMode.HALF_UP));

        var foundCartItem2 = savedOrder.getCartEntities().stream()
                .filter(cart -> cart.getItemId().equals(2L))
                .findAny()
                .orElse(null);
        foundCartItem2.setQuantity(3);
        var updatedOrder = orderRepository.save(savedOrder);
        // then
        assertThat(updatedOrder.getAddress()).isEqualTo("234 Elm Street");
        assertThat(updatedOrder.getTotalAmount()).isEqualTo(new BigDecimal("300.00"));
        assertThat(updatedOrder.getOrderStatus()).isEqualTo(OrderStatus.PENDING_PAYMENT);

        var updatedCartItem = updatedOrder.getCartEntities().stream()
                .filter(cart -> cart.getItemId().equals(2L))
                .findAny()
                .orElseThrow(() -> new IllegalArgumentException("Cart items not found"));

        assertThat(updatedCartItem.getQuantity()).isEqualTo(3);
        assertThat(updatedCartItem.getPriceAtPurchase()).isEqualTo(new BigDecimal("100.00"));
    }
}