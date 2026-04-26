package com.davinchicoder.springbank.transaction.infrastructure.api;


import com.bank.transaction.ObjectFactory;
import com.bank.transaction.TransactionResponseType;
import com.bank.transaction.TransactionType;
import com.davinchicoder.springbank.transaction.application.TransactionService;
import com.davinchicoder.springbank.transaction.application.request.NewTransactionRequest;
import com.davinchicoder.springbank.transaction.application.request.NewTransactionResponse;
import jakarta.xml.bind.JAXBElement;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/transaction")
@RequiredArgsConstructor
public class TransactionController {

    private final TransactionMapper mapper;
    private final TransactionService service;

    @PostMapping(consumes = MediaType.APPLICATION_XML_VALUE)
    public JAXBElement<TransactionResponseType> createTransaction(@RequestBody TransactionType transactionType, @RequestHeader("Idempotency-Key") String idempotencyKey) {
        NewTransactionRequest request = mapper.toNewTransactionRequest(transactionType, idempotencyKey);

        NewTransactionResponse transaction = service.createTransaction(request);

        TransactionResponseType responseType = mapper.toTransactionResponseType(transaction);
        return new ObjectFactory().createTransactionResponse(responseType);
    }

}
