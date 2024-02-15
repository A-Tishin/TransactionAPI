package com.mintos.TransactionAPI.persistence.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.mintos.TransactionAPI.utils.Currency;
import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import java.math.BigDecimal;

@Entity
@Table(name = "ACCOUNTS")
@Audited
public class AccountEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id")
    @JsonIgnore
    @NotAudited
    private UserEntity userEntity;

    @Enumerated(EnumType.STRING)
    private Currency accountCurrency;

    @Column(columnDefinition = "NUMERIC(20, 2) default 0")
    @PositiveOrZero
    private BigDecimal balance;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public UserEntity getUserEntity() {
        return userEntity;
    }

    public void setUserEntity(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public Currency getAccountCurrency() {
        return accountCurrency;
    }

    public void setAccountCurrency(Currency accountCurrency) {
        this.accountCurrency = accountCurrency;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        return "AccountEntity{" +
                "id=" + id +
                ", accountCurrency=" + accountCurrency +
                ", balance=" + balance +
                '}';
    }
}
