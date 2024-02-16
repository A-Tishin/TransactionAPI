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
import org.springframework.web.client.HttpClientErrorException;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping(path = "/accounts")
public class AccountController {
    private final AccountService accountService;
    private final AuditService auditService;

    public AccountController(AccountService accountService, AuditService auditService) {
        this.accountService = accountService;
        this.auditService = auditService;
    }

    @PostMapping(consumes = "application/json")
    AccountEntity createAccount(@RequestBody AccountDto accountDto) {
        return accountService.create(accountDto);
    }

    @PutMapping(value = "/transfer")
    void transferFunds(@RequestParam Long senderId,
                       @RequestParam Long receiverId,
                       @RequestParam BigDecimal amount) {
        accountService.transferFunds(senderId, receiverId, amount);
    }

    @GetMapping(value = "/{accountId}/transactionHistory")
    List<AccountEntity> accountHistory(@PathVariable Long accountId,
                                       @RequestParam int pageSize,
                                       @RequestParam int page) {
        return auditService.getAccountHistory(accountId, pageSize, page);
    }

    @ExceptionHandler({
            EntityNotFoundException.class,
            InsufficientFundsException.class,
            CurrencyPairExchangeNotSupportedException.class,
            HttpClientErrorException.class})
    public ResponseEntity<String> handleException(RuntimeException e) {
        return ResponseEntity
                .status(HttpStatus.NOT_FOUND)
                .body(e.getMessage());
    }

}
