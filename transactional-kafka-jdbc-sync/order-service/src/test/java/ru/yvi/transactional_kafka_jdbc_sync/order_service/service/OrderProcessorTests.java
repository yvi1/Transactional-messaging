package ru.yvi.transactional_kafka_jdbc_sync.order_service.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yvi.transactional_kafka_jdbc_sync.order_service.exception.OrderNotFoundException;
import ru.yvi.transactional_kafka_jdbc_sync.order_service.model.OrderEntity;
import ru.yvi.transactional_kafka_jdbc_sync.order_service.repository.OrderRepository;
import ru.yvi.transactional_kafka_jdbc_sync.order_service.rest.mapper.OrderMapper;
import ru.yvi.transactionalkafkajdbcsync.commonlibs.http.order.response.OrderResponseDTO;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static ru.yvi.transactional_kafka_jdbc_sync.order_service.util.DataUtils.*;

@ExtendWith(MockitoExtension.class)
public class OrderProcessorTests {

    @Mock
    private OrderRepository orderRepository;

    @Mock
    private OrderMapper orderMapper;

    @InjectMocks
    private OrderProcessor serviceUnderTest;

    @Test
    @DisplayName("Test save order functionality")
    public void givenOrderToSave_whenSaveOrder_thenRepositoryIsCalled() {

        //given
        var requestDTO = getCreateOrderRequestDTO();
        var responseDTO = getOrderResponseDTO();
        var expectedEntity = getOrderOnePersistent();

        given(orderMapper.toEntity(requestDTO)).willReturn(expectedEntity);
        given(orderRepository.save(expectedEntity)).willReturn(expectedEntity);
        given(orderMapper.toResponseDTO(expectedEntity)).willReturn(responseDTO);
        //when
        OrderResponseDTO result = serviceUnderTest.createOrder(requestDTO);
        //then
        assertThat(result).isNotNull();
    }

    @Test
    @DisplayName("Test update order functionality")
    public void givenOrderForUpdate_whenSave_thenRepositoryIsCalled() {
        //given
        var requestDTO = getCreateOrderRequestDTO();
        var responseDTO = getOrderResponseDTO();
        var orderToUpdate = getOrderOnePersistent();

        given(orderMapper.toEntity(requestDTO)).willReturn(orderToUpdate);
        given(orderRepository.existsById(any(UUID.class))).willReturn(true);
        given(orderRepository.save(any(OrderEntity.class))).willReturn(orderToUpdate);
        given(orderMapper.toResponseDTO(orderToUpdate)).willReturn(responseDTO);
        //when
        OrderResponseDTO result = serviceUnderTest.updateOrder(requestDTO);
        //then
        assertThat(result).isNotNull();
        then(orderRepository)
                .should(times(1))
                .save(any(OrderEntity.class));
    }

    @Test
    @DisplayName("Test update order with incorrect id functionality")
    public void givenOrderToUpdateWithIncorrectId_whenUpdateOrder_thenExceptionIsThrown() {
        //given
        var requestDTO = getCreateOrderRequestDTO();
        var orderToUpdate = getOrderOnePersistent();
        given(orderMapper.toEntity(requestDTO)).willReturn(orderToUpdate);
        given(orderRepository.existsById(any(UUID.class))).willReturn(false);
        //when
        assertThrows(OrderNotFoundException.class, () -> serviceUnderTest.updateOrder(requestDTO));
        //then
        then(orderRepository)
                .should(never())
                .save(any(OrderEntity.class));
    }

    @Test
    @DisplayName("Test get order by id functionality.")
    public void givenId_whenGetById_thenOrderIsReturned() {
        //given
        var responseDTO = getOrderResponseDTO();
        var entity = getOrderOnePersistent();
        UUID fixedUuid = UUID.fromString("bdc5055e-c296-4b7f-818a-293c1e9a4ec0");
        given(orderRepository.findById(fixedUuid)).willReturn(Optional.of(entity));
        given(orderMapper.toResponseDTO(entity)).willReturn(responseDTO);
        //when
        OrderResponseDTO order = serviceUnderTest.getOrderOrThrow(fixedUuid);
        //then
        assertThat(order).isNotNull();
        assertThat(order).isEqualTo(responseDTO);
    }

    @Test
    @DisplayName("Test get order by incorrect id functionality.")
    public void givenIncorrectId_whenGetById_thenExceptionIsThrown() {
        //given
        UUID fixedUuid = UUID.fromString("bdc5055e-c296-4b7f-818a-293c1e9a4ec0");

        given(orderRepository.findById(fixedUuid))
                .willThrow(OrderNotFoundException.class);
        //when
        assertThrows(OrderNotFoundException.class, () -> serviceUnderTest.getOrderOrThrow(fixedUuid));
        //then
    }

    @Test
    @DisplayName("Test get all orders by customer id functionality.")
    public void givenTwoOrders_whenGetAllById_thenAllCustomerOrdersAreReturned() {
        //given
        var orderEntity1 = getOrderOnePersistent();
        var orderEntity2 = getOrderTwoPersistent();

        var order1 = getOrderResponseDTO();
        var order2 = getOrderTwoResponseDTO();

        List<OrderEntity> orders = List.of(orderEntity1, orderEntity2);
        given(orderRepository.findAllByCustomerId(UUID_CUSTOMER_1))
                .willReturn(orders);

        given(orderMapper.toResponseDTO(orderEntity1)).willReturn(order1);
        given(orderMapper.toResponseDTO(orderEntity2)).willReturn(order2);
        //when
        List<OrderResponseDTO> orderResults = serviceUnderTest.getAllById(UUID_CUSTOMER_1);
        //then
        assertThat(orderResults).isNotEmpty();
        assertThat(orderResults.size()).isEqualTo(2);
    }

    @Test
    @DisplayName("Test order soft-delete by id functionality.")
    public void givenId_whenSoftDeleteById_thenRepositorySaveMethodIsCalled() {
        //given
        var entity = getOrderOnePersistent();
        UUID fixedUuid = UUID.fromString("bdc5055e-c296-4b7f-818a-293c1e9a4ec0");
        given(orderRepository.findById(fixedUuid))
                .willReturn(Optional.of(entity));
        //when
        serviceUnderTest.softDeleteById(fixedUuid);
        //then
        then(orderRepository)
                .should(times(1))
                .save(any(OrderEntity.class));
        then(orderRepository)
                .should(never())
                .deleteById(fixedUuid);
    }

    @Test
    @DisplayName("Test order soft-delete by incorrect id functionality.")
    public void givenIncorrectId_whenSoftDeleteById_thenExceptionIsThrown() {
        //given
        UUID fixedUuid = UUID.randomUUID();
        given(orderRepository.findById(fixedUuid))
                .willReturn(Optional.empty());
        //when
        assertThrows(OrderNotFoundException.class, () -> serviceUnderTest.softDeleteById(fixedUuid));
        //then
        then(orderRepository)
                .should(never())
                .save(any(OrderEntity.class));
    }

    @Test
    @DisplayName("Test order hard-delete by id functionality")
    public void givenCorrectId_whenHardDeleteById_thenDeleteRepositoryMethodIsCalled() {
        //given
        var entity = getOrderOnePersistent();
        UUID fixedUuid = UUID.fromString("bdc5055e-c296-4b7f-818a-293c1e9a4ec0");
        given(orderRepository.findById(fixedUuid))
                .willReturn(Optional.of(entity));
        //when
        serviceUnderTest.hardDeleteById(fixedUuid);
        //then
        then(orderRepository)
                .should(never())
                .save(any(OrderEntity.class));
        then(orderRepository)
                .should(times(1))
                .deleteById(entity.getId());
    }

    @Test
    @DisplayName("Test order hard-delete by incorrect id functionality.")
    public void givenIncorrectId_whenHardDeleteById_thenExceptionIsThrown() {
        //given
        UUID fixedUuid = UUID.fromString("bdc5055e-c296-4b7f-818a-293c1e9a4ec0");
        given(orderRepository.findById(fixedUuid))
                .willReturn(Optional.empty());
        //when
        assertThrows(OrderNotFoundException.class, () -> serviceUnderTest.hardDeleteById(fixedUuid));
        //then
        then(orderRepository)
                .should(never())
                .deleteById(fixedUuid);
    }
}