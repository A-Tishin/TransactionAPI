package com.mintos.TransactionAPI.controller;

import com.mintos.TransactionAPI.dto.AccountDto;
import com.mintos.TransactionAPI.exception.CurrencyPairExchangeNotSupportedException;
import com.mintos.TransactionAPI.exception.EntityNotFoundException;
import com.mintos.TransactionAPI.exception.InsufficientFundsException;
import com.mintos.TransactionAPI.persistence.entity.AccountEntity;
import com.mintos.TransactionAPI.service.AccountService;
import com.mintos.TransactionAPI.service.AuditService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
public class AccountController {
    private final AccountService accountService;
    private final AuditService auditService;

    public AccountController(AccountService accountService, AuditService auditService) {
        this.accountService = accountService;
        this.auditService = auditService;
    }

    @PostMapping(value = "/account/create", consumes = "application/json")
    void createAccount(@RequestBody AccountDto accountDto) {
        accountService.create(accountDto);
    }

    @PatchMapping(value = "/account/transfer")
    void transferFunds(@RequestParam Long senderId,
                       @RequestParam Long receiverId,
                       @RequestParam BigDecimal amount) {
        accountService.transferFunds(senderId, receiverId, amount);
    }

    @GetMapping(value = "/account/transactionHistory")
    List<AccountEntity> accountHistory(@RequestParam Long accountId,
                                       @RequestParam int pageSize,
                                       @RequestParam int page) {
        return auditService.getAccountHistory(accountId, pageSize, page);
    }

    @ExceptionHandler({
            EntityNotFoundException.class,
            InsufficientFundsException.class,
            CurrencyPairExchangeNotSupportedException.class})
    public ResponseEntity<String> handleException(RuntimeException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }

}
