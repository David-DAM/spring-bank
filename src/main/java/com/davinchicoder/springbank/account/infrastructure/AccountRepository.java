package com.davinchicoder.springbank.account.infrastructure;

import com.davinchicoder.springbank.account.domain.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class AccountRepository {

    private final AccountQueryRepository repository;
    private final AccountMapper mapper;

    public Optional<Account> findByIban(String iban) {
        return repository.findByIban(iban).map(mapper::toAccount);
    }

    public Optional<Account> findById(String id) {
        return repository.findById(id).map(mapper::toAccount);
    }

    public Account upsert(Account account) {
        AccountEntity entity = mapper.toEntity(account);
        AccountEntity saved = repository.save(entity);
        return mapper.toAccount(saved);
    }

}
