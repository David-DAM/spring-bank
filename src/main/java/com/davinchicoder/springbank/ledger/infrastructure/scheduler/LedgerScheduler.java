package com.davinchicoder.springbank.ledger.infrastructure.scheduler;

import com.davinchicoder.springbank.ledger.infrastructure.database.LedgerEntryRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@RequiredArgsConstructor
@Component
public class LedgerScheduler {

    private final LedgerEntryRepository repository;

    @Scheduled(cron = "0 */5 * * * *")
    public void reconcile() {
        List<String> broken = repository.findUnbalancedTransactions();

        if (!broken.isEmpty()) {
            log.error("Unbalanced transactions: {}", broken);

        }
    }
}
