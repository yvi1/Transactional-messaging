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
}