package ru.yvi.transactional_kafka_jdbc_sync.order_service.rest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ControllerAdvice;
import ru.yvi.transactional_kafka_jdbc_sync.order_service.exception.OrderNotFoundException;
import ru.yvi.transactional_kafka_jdbc_sync.order_service.rest.dto.response.ErrorResponseDTO;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler({OrderNotFoundException.class})
    protected ResponseEntity<ErrorResponseDTO> handleOrderNotFoundException(RuntimeException e) {
        return buildErrorResponse(e, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({Exception.class})
    protected ResponseEntity<ErrorResponseDTO> handleException(RuntimeException e) {
        return buildErrorResponse(e, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    private ResponseEntity<ErrorResponseDTO> buildErrorResponse(Exception exception, HttpStatus httpStatus) {
        return new ResponseEntity<>(
                new ErrorResponseDTO(
                        exception.getMessage(),
                        httpStatus.value()),
                httpStatus);
    }
}
