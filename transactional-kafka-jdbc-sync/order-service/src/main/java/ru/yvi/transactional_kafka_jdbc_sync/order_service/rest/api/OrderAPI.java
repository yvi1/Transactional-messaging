package ru.yvi.transactional_kafka_jdbc_sync.order_service.rest.api;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yvi.transactional_kafka_jdbc_sync.order_service.rest.dto.request.CreateOrderRequestDTO;
import ru.yvi.transactional_kafka_jdbc_sync.order_service.rest.dto.response.OrderResponseDTO;

import java.util.List;
import java.util.UUID;

@Validated
@Tag(name = "Order Management", description = "Operations related to orders")
public interface OrderAPI {

    String PATH_CREATE_ORDER = "";
    /**
     * POST /api/v1/orders : Create new order
     *
     * @param createOrderRequestDTO  (required)
     * @return Order created (status code 201)
     */
    @Operation(
            operationId = "createOrder",
            summary = "Create new order",
            responses = {
                    @ApiResponse(responseCode = "201", description = "Order created", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = OrderResponseDTO.class))
                    }),
                    @ApiResponse(responseCode = "400", description = "Bad request", content = {
                            @Content(mediaType = "application/json", examples = {
                                    @ExampleObject(name = "Invalid input data example", value = """
                                    {
                                      "name": "BAD_REQUEST",
                                      "code": "400",
                                      "description": "The provided order is invalid",
                                      "message": "<error message>"
                                    }
                                    """)
                            })
                    })
            }
    )
    @RequestMapping(
            method = RequestMethod.POST,
            value = OrderAPI.PATH_CREATE_ORDER,
            produces = { "application/json" },
            consumes = "application/json"
    )
    ResponseEntity<OrderResponseDTO> createOrder(
            @Parameter(name = "CreateOrderRequestDTO", description = "Create order request message", required = true) @Valid @RequestBody CreateOrderRequestDTO createOrderRequestDTO
    );

    String PATH_FIND_ALL_BY_CUSTOMER_ID = "/customer/{id}";
    /**
     * GET /api/v1/orders/customer/{id} : Find all orders by customer id
     *
     * @param id  (required)
     * @return All orders found (status code 200)
     */
    @Operation(
            operationId = "getAllOrdersByCustomerId",
            summary = "Find all orders by customer id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Successful response", content = {
                            @Content(
                                    mediaType = "application/json",
                                    array = @ArraySchema(schema = @Schema(implementation = OrderResponseDTO.class)))
                    })
            }
    )
    @RequestMapping(
           method = RequestMethod.GET,
            value = OrderAPI.PATH_FIND_ALL_BY_CUSTOMER_ID,
            produces = { "application/json" }
    )
    ResponseEntity<List<OrderResponseDTO>> getAllOrdersByCustomerId(
            @NotNull @Parameter(name = "id", description = "Customer UUID", required = true, in = ParameterIn.PATH) @PathVariable("id") UUID id
    );


    String PATH_FIND_BY_ID = "{id}";
    /**
     * GET /api/v1/orders/{id} : Find order by id
     *
     * @param id  (required)
     * @return Order found (status code 200)
     */
    @Operation(
            operationId = "getOrderById",
            summary = "Find order by id",
            responses = {
                    @ApiResponse(responseCode = "200", description = "Order found response", content = {
                            @Content(mediaType = "application/json", schema = @Schema(implementation = OrderResponseDTO.class))
                    }),
                    @ApiResponse(responseCode = "404", description = "Order not found", content = {
                            @Content(mediaType = "application/json", examples = {
                                    @ExampleObject(name = "Order not found example", value = """
                                            {
                                              "name": "NOT_FOUND",
                                              "code": 404,
                                              "description": "Order not found",
                                              "message": "Order with id=`<UUID>` not found"
                                            }
                                            """)
                            })
                    })
            }
    )
    @RequestMapping(
            method = RequestMethod.GET,
            value = OrderAPI.PATH_FIND_BY_ID,
            produces = { "application/json" }
    )
    ResponseEntity<OrderResponseDTO> getOrderById(
            @NotNull @Parameter(name = "id", description = "order UUID", required = true, in = ParameterIn.PATH) @PathVariable("id") UUID id
    );
}
