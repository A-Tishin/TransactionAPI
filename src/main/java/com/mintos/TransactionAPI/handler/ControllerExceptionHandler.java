package com.mintos.TransactionAPI.handler;

import com.mintos.TransactionAPI.exception.CurrencyPairExchangeNotSupportedException;
import com.mintos.TransactionAPI.exception.EntityNotFoundException;
import com.mintos.TransactionAPI.exception.InsufficientFundsException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.client.HttpClientErrorException;

@ControllerAdvice
public class ControllerExceptionHandler {

    @ExceptionHandler({
            EntityNotFoundException.class,
            InsufficientFundsException.class
    })
    public ResponseEntity<String> handleException(RuntimeException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }

    @ExceptionHandler({
            HttpClientErrorException.class,
            CurrencyPairExchangeNotSupportedException.class
    })
    public ResponseEntity<String> handleExternalServiceException(RuntimeException e) {
        return ResponseEntity
                .status(HttpStatus.SERVICE_UNAVAILABLE)
                .body(e.getMessage());
    }

}
