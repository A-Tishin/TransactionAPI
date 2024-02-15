package com.mintos.TransactionAPI.service;

import com.mintos.TransactionAPI.persistence.entity.AccountEntity;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.envers.AuditReader;
import org.hibernate.envers.AuditReaderFactory;
import org.hibernate.envers.query.AuditEntity;
import org.hibernate.envers.query.AuditQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuditService {

    @Autowired
    private EntityManagerFactory factory;

    public List<AccountEntity> getAccountHistory(Long accountId, int pageSize, int pageNum) {
        AuditReader reader = AuditReaderFactory.get(factory.createEntityManager());
        AuditQuery queryHistoryOfAccount = reader.createQuery()
                .forRevisionsOfEntity(AccountEntity.class, true, true)
                .add(AuditEntity.property("id").eq(accountId))
                .addOrder(AuditEntity.revisionNumber().desc())
                .setFirstResult((pageNum - 1) * pageSize)
                .setMaxResults(pageSize);

        return queryHistoryOfAccount.getResultList();
    }
}
