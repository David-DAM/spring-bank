package com.davinchicoder.springbank.transaction.infrastructure.api;

import com.bank.transaction.TransactionType;
import com.davinchicoder.springbank.transaction.application.NewTransactionRequest;
import com.davinchicoder.springbank.transaction.application.TransactionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionMapper mapper;
    private final TransactionService service;

    @PostMapping(consumes = MediaType.APPLICATION_XML_VALUE)
    public ResponseEntity<Void> createCustomer(@RequestBody TransactionType transactionType) {
        NewTransactionRequest request = mapper.toNewTransactionRequest(transactionType);

        service.createTransaction(request);

        return ResponseEntity.accepted().build();
    }

}
