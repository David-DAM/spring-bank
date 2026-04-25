package com.davinchicoder.springbank.account.infrastructure;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AccountQueryRepository extends JpaRepository<AccountEntity, String> {

    Optional<AccountEntity> findByIban(String iban);
}
