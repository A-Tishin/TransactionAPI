package com.mintos.TransactionAPI.controller;

import com.mintos.TransactionAPI.dto.AccountDto;
import com.mintos.TransactionAPI.persistence.entity.AccountEntity;
import com.mintos.TransactionAPI.service.AccountService;
import com.mintos.TransactionAPI.service.AuditService;
import org.springframework.web.bind.annotation.*;

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

}
