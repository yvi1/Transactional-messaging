package ru.yvi.transactional_kafka_jdbc_sync.order_service.repository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.data.jpa.test.autoconfigure.DataJpaTest;
import org.springframework.util.CollectionUtils;
import ru.yvi.transactional_kafka_jdbc_sync.order_service.model.CartEntity;
import ru.yvi.transactional_kafka_jdbc_sync.order_service.model.OrderEntity;
import ru.yvi.transactionalkafkajdbcsync.commonlibs.http.order.OrderStatus;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.yvi.transactional_kafka_jdbc_sync.order_service.util.DataUtils.*;

@DataJpaTest
public class OrderRepositoryTests {

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
        var order = getOrderOneTransient();
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

    @Test
    @DisplayName("Test getting order by id functionality")
    public void givenOrderCreated_whenGetById_thenOrderIsReturned() {
        // given
        OrderEntity orderToSave = getOrderOneTransient();
        orderRepository.save(orderToSave);
        //when
        OrderEntity obtainedOrder = orderRepository.findById(orderToSave.getId()).orElse(null);
        //then
        assertThat(obtainedOrder).isNotNull();
        assertThat(obtainedOrder.getCustomerId()).isEqualTo(UUID_CUSTOMER_1);
    }

    @Test
    @DisplayName("Test order not found functionality")
    public void givenOrderIsNotCreated_whenGetById_thenOptionalIsEmpty() {
        //given
        UUID incorrectId = UUID.fromString("de1a887d-c2b8-4476-8cd7-af9bbe653e45");
        //when
        OrderEntity obtainedOrder = orderRepository.findById(incorrectId).orElse(null);
        //then
        assertThat(obtainedOrder).isNull();
    }

    @Test
    @DisplayName("Test get all orders functionality")
    public void givenTwoOrdersAreStored_whenFindAll_thenAllOrdersAreReturned() {
        //given
        OrderEntity order1 = getOrderOneTransient();
        order1.setOrderStatus(OrderStatus.PENDING_PAYMENT);

        OrderEntity order2 = getOrderTwoTransient();
        order2.setOrderStatus(OrderStatus.PENDING_PAYMENT);

        orderRepository.saveAll(List.of(order1, order2));
        //when
        List<OrderEntity> obtainedOrders = orderRepository.findAll();
        //then
        assertThat(CollectionUtils.isEmpty(obtainedOrders)).isFalse();
        assertThat(obtainedOrders).hasSize(2);
    }

    @Test
    @DisplayName("Test get all orders by customer id with EntityGraph functionality")
    public void givenOrdersSaved_whenGetByCustomerId_thenOnlyAllCustomerOrdersAreReturned() {
        //given
        OrderEntity order1 = getOrderOneTransient();
        order1.setOrderStatus(OrderStatus.PENDING_PAYMENT);

        OrderEntity order2 = getOrderTwoTransient();
        order2.setOrderStatus(OrderStatus.PENDING_PAYMENT);

        OrderEntity order3 = getOrderThreeTransient();
        order3.setOrderStatus(OrderStatus.PENDING_PAYMENT);

        orderRepository.saveAll(List.of(order1, order2, order3));
        //when
        var customerOrderList = orderRepository.findAllByCustomerId(UUID_CUSTOMER_1);
        //then
        assertThat(CollectionUtils.isEmpty(customerOrderList)).isFalse();
        assertThat(customerOrderList).hasSize(2);
        assertThat(customerOrderList.getFirst().getCartEntities()).extracting(CartEntity::getName)
                .containsExactlyInAnyOrder("Laptop", "Desktop");
        assertThat(customerOrderList.getLast().getCartEntities()).isEmpty();
    }

    @Test
    @DisplayName("Test get orders by customer id, who has no orders")
    public void givenCustomerIdWithoutOrders_whenGetByCustomerId_thenReturnOrderEmptyList() {
        // given

        // when
        List<OrderEntity> orders = orderRepository.findAllByCustomerId(CUSTOMER_ID_WITHOUT_ORDERS);

        // then
        assertThat(orders).isEmpty();
    }

    @Test
    @DisplayName("Test delete an order by id from the database")
    public void givenOrderIsSaved_whenDeleteById_thenOrderIsRemovedFromDB() {
        //given
        OrderEntity order = getOrderOneTransient();
        orderRepository.save(order);
        //when
        orderRepository.deleteById(order.getId());
        //then
        OrderEntity obtainedOrder = orderRepository.findById(order.getId()).orElse(null);
        assertThat(obtainedOrder).isNull();
    }
}