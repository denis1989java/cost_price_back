package com.monich.cost_price.core.repository;

import com.monich.cost_price.core.domain.Account;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.Repository;

import java.util.List;

public interface AccountRepository extends Repository<Account, Long>, QuerydslPredicateExecutor<Account> {

    Account findByAccountNumber(String accountNumber);

    Account findById(Long id);

    <T extends Account> T save(T account);
}
