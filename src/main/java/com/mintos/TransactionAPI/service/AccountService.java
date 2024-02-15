package com.mintos.TransactionAPI.service;

import com.mintos.TransactionAPI.dto.AccountDto;
import com.mintos.TransactionAPI.exception.EntityNotFoundException;
import com.mintos.TransactionAPI.exception.InsufficientFundsException;
import com.mintos.TransactionAPI.persistence.entity.AccountEntity;
import com.mintos.TransactionAPI.persistence.entity.UserEntity;
import com.mintos.TransactionAPI.persistence.repository.AccountRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

import static com.mintos.TransactionAPI.utils.ObjectUtil.convertDtoToAccountEntity;

@Service
@Transactional
public class AccountService {

    private final AccountRepository accountRepository;
    private final UserService userService;
    private final CurrencyExchangeRateService currencyExchangeRateService;

    public AccountService(AccountRepository accountRepository,
                          UserService userService,
                          CurrencyExchangeRateService currencyExchangeRateService) {
        this.accountRepository = accountRepository;
        this.userService = userService;
        this.currencyExchangeRateService = currencyExchangeRateService;
    }

    public AccountEntity create(AccountDto dto) {
        Optional<UserEntity> optional = dto.getUserId() != null ?
                userService.findUser(dto.getUserId()) : Optional.empty();

        UserEntity user = optional.orElse(userService.create());
        AccountEntity account = convertDtoToAccountEntity(dto, user);
        return accountRepository.save(account);
    }

    public void transferFunds(Long senderId, Long receiverId, BigDecimal amount) {

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new InsufficientFundsException("Transfer amount cannot be less or equal to 0");
        }

        amount = amount.setScale(2, RoundingMode.FLOOR);

        Optional<AccountEntity> optionalFrom = accountRepository.findById(senderId);
        Optional<AccountEntity> optionalTo = accountRepository.findById(receiverId);

        if (optionalFrom.isEmpty() || optionalTo.isEmpty()) {
            throw new EntityNotFoundException("One or both account is not found");
        }

        AccountEntity sender = optionalFrom.get();
        BigDecimal senderBalance = sender.getBalance();

        if (senderBalance.compareTo(amount) < 0) {
            throw new InsufficientFundsException("Transfer amount cannot exceed account balance");
        }

        BigDecimal newSenderBalance = senderBalance.subtract(amount);
        sender.setBalance(newSenderBalance);

        AccountEntity receiver = optionalTo.get();
        if (sender.getAccountCurrency() != receiver.getAccountCurrency()) {
            BigDecimal exchangeRate = currencyExchangeRateService
                    .getRateForCurrencyPair(sender.getAccountCurrency(), receiver.getAccountCurrency());
            amount = amount.multiply(exchangeRate).setScale(2, RoundingMode.FLOOR);
        }

        BigDecimal receiverBalance = receiver.getBalance();
        receiver.setBalance(receiverBalance.add(amount));

        accountRepository.saveAllAndFlush(List.of(sender, receiver));
    }

}
