package com.mintos.TransactionAPI.exception;

public class CurrencyPairExchangeNotSupportedException extends RuntimeException {
    public CurrencyPairExchangeNotSupportedException(String message) {
        super(message);
    }
}
