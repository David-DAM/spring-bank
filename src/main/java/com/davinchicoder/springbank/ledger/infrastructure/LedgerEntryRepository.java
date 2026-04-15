package com.davinchicoder.springbank.ledger.infrastructure;

import com.davinchicoder.springbank.ledger.domain.LedgerEntry;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@RequiredArgsConstructor
public class LedgerEntryRepository {

    private final LedgerEntryQueryRepository repository;
    private final LedgerEntryEntityMapper mapper;

    public Optional<LedgerEntry> findById(String id) {
        return repository.findById(id).map(mapper::toLedgerEntry);
    }

    public LedgerEntry upsert(LedgerEntry customer) {
        LedgerEntryEntity entity = mapper.toLedgerEntryEntity(customer);

        LedgerEntryEntity saved = repository.save(entity);

        return mapper.toLedgerEntry(saved);
    }

    public void upsertAll(List<LedgerEntry> customers) {
        List<LedgerEntryEntity> entities = customers.stream()
                .map(mapper::toLedgerEntryEntity)
                .collect(Collectors.toList());

        repository.saveAll(entities);
    }

    public List<LedgerEntry> findAll() {
        return repository.findAll().stream()
                .map(mapper::toLedgerEntry)
                .collect(Collectors.toList());
    }
}
