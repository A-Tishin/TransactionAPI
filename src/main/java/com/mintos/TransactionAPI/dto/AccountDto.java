package com.mintos.TransactionAPI.dto;

import com.mintos.TransactionAPI.utils.Currency;

import java.math.BigDecimal;

public class AccountDto {
    private Currency currency;
    private BigDecimal balance;
    private Long userId;

    public Currency getCurrency() {
        return currency;
    }

    public void setCurrency(Currency currency) {
        this.currency = currency;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }
}
