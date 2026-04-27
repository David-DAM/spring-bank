package com.davinchicoder.springbank.acceptance;

import com.bank.transaction.ObjectFactory;
import com.bank.transaction.TransactionType;
import com.bank.transaction.TransactionTypeEnum;
import com.davinchicoder.springbank.ledger.infrastructure.database.LedgerEntryRepository;
import com.davinchicoder.springbank.transaction.domain.Transaction;
import com.davinchicoder.springbank.transaction.domain.TransactionStatus;
import com.davinchicoder.springbank.transaction.infrastructure.database.TransactionRepository;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import io.cucumber.spring.ScenarioScope;
import jakarta.xml.bind.JAXBElement;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestClient;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.util.GregorianCalendar;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ScenarioScope
@CucumberContextConfiguration
@RequiredArgsConstructor
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TransferFeatureStepsCaseIT {

    public static final String FROM_ACCOUNT = "ES3601520826";
    public static final String TO_ACCOUNT = "ES3601520827";
    private final RestClient restClient;
    private final LedgerEntryRepository ledgerEntryRepository;
    private final TransactionRepository transactionRepository;

    @Given("An account with {int} euros")
    public void an_account_with_euros(Integer actualBalance) {
        assertNotNull(actualBalance);
    }

    @When("I transfer {int} euros to another")
    public void i_transfer_euros_to_another(Integer quantity) throws DatatypeConfigurationException {

        JAXBElement<TransactionType> requestBody = createTransactionRequest(quantity);

        restClient.post()
                .uri("/api/v1/transaction")
                .body(requestBody)
                .header("Idempotency-Key", UUID.randomUUID().toString())
                .exchange((_, response) -> {
                    assertEquals(200, response.getStatusCode().value());
                    return null;
                });
    }

    @Then("The final amount is {int} euros")
    public void the_final_amount_is_euros(Integer expected) {
        Long balanceInCents = ledgerEntryRepository.calculateBalanceInCents(FROM_ACCOUNT);
        assertEquals(expected, balanceInCents.intValue() / 100);
    }

    @When("I transfer {int} euros to another twice by slow internet")
    public void i_transfer_euros_to_another_twice_by_slow_internet(Integer quantity) throws DatatypeConfigurationException {

        String idempotencyKey = UUID.randomUUID().toString();

        JAXBElement<TransactionType> requestBody = createTransactionRequest(quantity);

        restClient.post()
                .uri("/api/v1/transaction")
                .body(requestBody)
                .header("Idempotency-Key", idempotencyKey)
                .exchange((_, response) -> {
                    assertEquals(200, response.getStatusCode().value());
                    return null;
                });

        restClient.post()
                .uri("/api/v1/transaction")
                .body(requestBody)
                .header("Idempotency-Key", idempotencyKey)
                .exchange((_, response) -> {
                    assertEquals(200, response.getStatusCode().value());
                    return null;
                });
    }

    @Then("The final amount is still {int} euros and not {int}")
    public void the_final_amount_is_euros(Integer expected, Integer unexpected) {
        Long balanceInCents = ledgerEntryRepository.calculateBalanceInCents(FROM_ACCOUNT);
        assertEquals(expected, balanceInCents.intValue() / 100);
        assertNotEquals(unexpected, balanceInCents.intValue() / 100);
    }

    @When("I try to transfer {int} euros to non existent account")
    public void i_try_to_transfer_euros_to_non_existent_account(Integer quantity) throws DatatypeConfigurationException {

        JAXBElement<TransactionType> requestBody = createTransactionRequest(quantity);

        restClient.post()
                .uri("/api/v1/transaction")
                .body(requestBody)
                .header("Idempotency-Key", UUID.randomUUID().toString())
                .exchange((_, response) -> {
                    assertEquals(404, response.getStatusCode().value());
                    return null;
                });

    }

    @Then("I got an error and i am not able")
    public void i_got_an_error_and_i_am_not_able() {
        Transaction transaction = transactionRepository.findAll().getFirst();
        assertEquals(TransactionStatus.FAILED, transaction.getStatus());
    }

    private @NonNull JAXBElement<TransactionType> createTransactionRequest(Integer quantity) throws DatatypeConfigurationException {
        TransactionType transactionType = new TransactionType();
        transactionType.setId(UUID.randomUUID().toString());
        transactionType.setType(TransactionTypeEnum.DEBIT);
        transactionType.setAmount(BigDecimal.valueOf(quantity));
        transactionType.setFromAccount(FROM_ACCOUNT);
        transactionType.setToAccount(TO_ACCOUNT);
        transactionType.setAmount(BigDecimal.valueOf(quantity));
        transactionType.setCreatedAt(DatatypeFactory.newInstance().newXMLGregorianCalendar(GregorianCalendar.from(Instant.now().atZone(ZoneId.systemDefault()))));

        return new ObjectFactory().createTransaction(transactionType);
    }
}
