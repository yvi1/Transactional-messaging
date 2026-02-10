package ru.yvi.transactional_kafka_jdbc_sync.order_service.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.yvi.transactional_kafka_jdbc_sync.order_service.repository.OrderRepository;
import ru.yvi.transactional_kafka_jdbc_sync.order_service.rest.mapper.OrderMapper;
import ru.yvi.transactionalkafkajdbcsync.commonlibs.http.order.response.OrderResponseDTO;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
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
        // When
        OrderResponseDTO result = serviceUnderTest.createOrder(requestDTO);
        //then
        assertThat(result).isNotNull();
    }
}