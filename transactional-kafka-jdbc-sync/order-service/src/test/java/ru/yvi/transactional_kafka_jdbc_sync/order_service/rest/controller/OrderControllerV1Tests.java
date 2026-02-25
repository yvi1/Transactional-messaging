package ru.yvi.transactional_kafka_jdbc_sync.order_service.rest.controller;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import ru.yvi.transactional_kafka_jdbc_sync.order_service.exception.OrderNotFoundException;
import ru.yvi.transactional_kafka_jdbc_sync.order_service.service.OrderProcessor;
import ru.yvi.transactional_kafka_jdbc_sync.order_service.util.DataUtils;
import ru.yvi.transactionalkafkajdbcsync.commonlibs.http.order.OrderStatus;
import tools.jackson.databind.ObjectMapper;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static ru.yvi.transactional_kafka_jdbc_sync.order_service.util.DataUtils.*;

@WebMvcTest(OrderControllerV1.class)
class OrderControllerV1Tests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private OrderProcessor orderProcessor;

    @Test
    @DisplayName("Test for create order functionality")
    public void givenCreateOrderRequestDTO_whenCreateOrder_thenSuccessResponse() throws Exception {
        //given
        var requestDTO = DataUtils.getCreateOrderRequestDTO();
        var responseDTO = DataUtils.getOrderResponseDTO();
        given(orderProcessor.createOrder(requestDTO))
                .willReturn(responseDTO);
        //when
        ResultActions result = mockMvc.perform(post("/api/v1/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO))
        );
        //then
        result
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(jsonPath("$.id", is(UUID_ORDER_1.toString())))
                .andExpect(jsonPath("$.customerId",  is(UUID_CUSTOMER_1.toString())))
                .andExpect(jsonPath("$.address", is("Address")))
                .andExpect(jsonPath("$.orderStatus", is(OrderStatus.PENDING_PAYMENT.name())))
                .andExpect(jsonPath("$.totalAmount", is(150.00)))
                .andExpect(jsonPath("$.items").exists())
                .andExpect(jsonPath("$.items.length()", is(2)))

                .andExpect(jsonPath("$.items[*].name", containsInAnyOrder("Item1", "Item2")))
                .andExpect(jsonPath("$.items[*].quantity", containsInAnyOrder(1, 2)))
                .andExpect(jsonPath("$.items[*].priceAtPurchase", containsInAnyOrder(100.00, 50.00)));
    }

    @Test
    @DisplayName("Test for update order functionality")
    public void givenOrderDTO_whenUpdateOrder_thenSuccessResponse() throws Exception {
        //given
        var requestDTO = DataUtils.getCreateOrderRequestDTO();
        var responseDTO = DataUtils.getOrderResponseDTO();
        given(orderProcessor.updateOrder(requestDTO))
                .willReturn(responseDTO);
        //when
        ResultActions result = mockMvc.perform(put("/api/v1/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO))
        );
        //then
        result
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(jsonPath("$.id", is(UUID_ORDER_1.toString())))
                .andExpect(jsonPath("$.customerId",  is(UUID_CUSTOMER_1.toString())))
                .andExpect(jsonPath("$.address", is("Address")))
                .andExpect(jsonPath("$.orderStatus", is(OrderStatus.PENDING_PAYMENT.name())))
                .andExpect(jsonPath("$.totalAmount", is(150.00)))
                .andExpect(jsonPath("$.items").exists())
                .andExpect(jsonPath("$.items.length()", is(2)));

    }

    @Test
    @DisplayName("Test for update order with incorrect id functionality")
    public void givenOrderDTOWithIncorrectId_whenUpdateOrder_thenErrorResponse() throws Exception {
        //given
        var requestDTO = DataUtils.getCreateOrderRequestDTO();
        given(orderProcessor.updateOrder(requestDTO))
                .willThrow(new OrderNotFoundException("Order not found"));
        //when
        ResultActions result = mockMvc.perform(put("/api/v1/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDTO))
        );
        //then
        result
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(jsonPath("$.name", is(HttpStatus.NOT_FOUND.name())))
                .andExpect(jsonPath("$.code", is(HttpStatus.NOT_FOUND.value())))
                .andExpect(jsonPath("$.message", is("Order not found")))
                .andExpect(jsonPath("$.description", is("Order not found")));

    }

    @Test
    @DisplayName("Test for get orders by customer id functionality")
    public void givenCustomerId_whenGetOrdersById_thenSuccessResponse() throws Exception {
        //given
        var order1 = DataUtils.getOrderResponseDTO();
        var order2 = DataUtils.getOrderTwoResponseDTO();
        given(orderProcessor.getAllById(UUID_CUSTOMER_1))
                .willReturn(List.of(order1, order2));
        //when
        ResultActions result = mockMvc.perform(get("/api/v1/orders/customer/{customerId}", UUID_CUSTOMER_1.toString())
                .contentType(MediaType.APPLICATION_JSON)
        );
        //then
        result
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                // Assert first order
                .andExpect(jsonPath("$[0].id", is(UUID_ORDER_1.toString())))
                .andExpect(jsonPath("$[0].customerId",  is(UUID_CUSTOMER_1.toString())))
                .andExpect(jsonPath("$[0].address",  is("Address")))
                .andExpect(jsonPath("$[0].orderStatus",  is(OrderStatus.PENDING_PAYMENT.name())))
                .andExpect(jsonPath("$[0].totalAmount",  is(150.00)))
                .andExpect(jsonPath("$[0].items.length()", is(2)))
                .andExpect(jsonPath("$[0].items[*].itemId", containsInAnyOrder(1, 2)))
                .andExpect(jsonPath("$[0].items[*].name", containsInAnyOrder("Item1", "Item2")))
                .andExpect(jsonPath("$[0].items[*].quantity", containsInAnyOrder(2, 1)))
                .andExpect(jsonPath("$[0].items[*].priceAtPurchase", containsInAnyOrder(100.00, 50.00)))
                // Assert second order
                .andExpect(jsonPath("$[1].id", is(UUID_ORDER_2.toString())))
                .andExpect(jsonPath("$[1].customerId",  is(UUID_CUSTOMER_1.toString())))
                .andExpect(jsonPath("$[1].address",  is("Address")))
                .andExpect(jsonPath("$[1].orderStatus",  is(OrderStatus.PENDING_PAYMENT.name())))
                .andExpect(jsonPath("$[1].totalAmount",  is(200.00)))
                .andExpect(jsonPath("$[1].items.length()", is(2)))
                .andExpect(jsonPath("$[1].items[*].itemId", containsInAnyOrder(1, 2)))
                .andExpect(jsonPath("$[1].items[*].name", containsInAnyOrder("Item3", "Item4")))
                .andExpect(jsonPath("$[1].items[*].quantity", containsInAnyOrder(3, 4)))
                .andExpect(jsonPath("$[1].items[*].priceAtPurchase", containsInAnyOrder(10.00, 5.00)));
    }

    @Test
    @DisplayName("Test for get orders by incorrect customer id functionality")
    public void givenIncorrectCustomerId_whenGetOrdersById_thenEmptyListReturned() throws Exception {
        //given
        given(orderProcessor.getAllById(UUID_CUSTOMER_2))
                .willReturn(Collections.emptyList());
        //when
        ResultActions result = mockMvc.perform(get("/api/v1/orders/customer/{customerId}", UUID_CUSTOMER_2.toString())
                .contentType(MediaType.APPLICATION_JSON)
        );
        //then
        result
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$", hasSize(0)));;
    }

    @Test
    @DisplayName("Test for get order by id functionality")
    public void givenOrderId_whenGetOrder_thenSuccessResponse() throws Exception {
        //given
        var order1 = DataUtils.getOrderResponseDTO();
        given(orderProcessor.getOrderOrThrow(UUID_ORDER_1))
                .willReturn(order1);
        //when
        ResultActions result = mockMvc.perform(get("/api/v1/orders/{id}", UUID_ORDER_1.toString())
                .contentType(MediaType.APPLICATION_JSON)
        );
        //then
        result
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.id", is(UUID_ORDER_1.toString())))
                .andExpect(jsonPath("$.customerId",  is(UUID_CUSTOMER_1.toString())))
                .andExpect(jsonPath("$.address",  is("Address")))
                .andExpect(jsonPath("$.orderStatus",  is(OrderStatus.PENDING_PAYMENT.name())))
                .andExpect(jsonPath("$.totalAmount",  is(150.00)))
                .andExpect(jsonPath("$.items.length()", is(2)))
                .andExpect(jsonPath("$.items[*].itemId", containsInAnyOrder(1, 2)))
                .andExpect(jsonPath("$.items[*].name", containsInAnyOrder("Item1", "Item2")))
                .andExpect(jsonPath("$.items[*].quantity", containsInAnyOrder(2, 1)))
                .andExpect(jsonPath("$.items[*].priceAtPurchase", containsInAnyOrder(100.00, 50.00)));
    }

    @Test
    @DisplayName("Test for get order by incorrect id functionality")
    public void givenIncorrectOrderId_whenGetOrder_thenErrorResponse() throws Exception {
        //given
        var order1 = DataUtils.getOrderResponseDTO();
        given(orderProcessor.getOrderOrThrow(UUID_ORDER_2))
                .willThrow(new OrderNotFoundException("Order not found"));
        //when
        ResultActions result = mockMvc.perform(get("/api/v1/orders/{id}", UUID_ORDER_2.toString())
                .contentType(MediaType.APPLICATION_JSON)
        );
        //then
        result
                .andDo(MockMvcResultHandlers.print())
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(jsonPath("$.name", is(HttpStatus.NOT_FOUND.name())))
                .andExpect(jsonPath("$.code", is(HttpStatus.NOT_FOUND.value())))
                .andExpect(jsonPath("$.message", is("Order not found")))
                .andExpect(jsonPath("$.description", is("Order not found")));
    }
}