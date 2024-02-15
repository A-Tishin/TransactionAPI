package com.mintos.TransactionAPI.utils;

import com.mintos.TransactionAPI.dto.AccountDto;
import com.mintos.TransactionAPI.persistence.entity.AccountEntity;
import com.mintos.TransactionAPI.persistence.entity.UserEntity;

import java.math.BigDecimal;

public class ObjectUtil {

    public static AccountEntity convertDtoToAccountEntity(AccountDto dto, UserEntity user) {
        AccountEntity account = new AccountEntity();
        account.setUserEntity(user);
        account.setAccountCurrency(dto.getCurrency());
        account.setBalance(dto.getBalance() != null ? dto.getBalance() : BigDecimal.ZERO);
        return account;
    }
}
