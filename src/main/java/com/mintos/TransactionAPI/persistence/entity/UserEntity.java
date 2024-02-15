package com.mintos.TransactionAPI.persistence.entity;

import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name = "USERS")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;

    @OneToMany(mappedBy="userEntity", fetch = FetchType.EAGER)
    private Set<AccountEntity> userAccounts;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<AccountEntity> getUserAccounts() {
        return userAccounts;
    }

    public void addUserAccount(AccountEntity userAccount) {
        if (this.userAccounts == null) {
            this.userAccounts = new HashSet<>();
        }

        userAccounts.add(userAccount);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserEntity that = (UserEntity) o;
        return Objects.equals(id, that.id) && Objects.equals(userAccounts, that.userAccounts);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, userAccounts);
    }

    @Override
    public String toString() {
        return "UserEntity{" +
                "id=" + id +
                ", userAccounts=" + userAccounts +
                '}';
    }
}
