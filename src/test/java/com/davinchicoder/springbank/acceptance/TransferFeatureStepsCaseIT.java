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
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

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
        await().atMost(5, TimeUnit.SECONDS)
                .pollInterval(200, TimeUnit.MILLISECONDS)
                .untilAsserted(() -> {
                    Long balanceInCents = ledgerEntryRepository.calculateBalanceInCents(FROM_ACCOUNT);
                    assertNotNull(balanceInCents);
                    assertEquals(expected, balanceInCents.intValue() / 100);
                });
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

    @When("I try to transfer {int} euros to non existent account")
    public void i_try_to_transfer_euros_to_non_existent_account(Integer quantity) throws DatatypeConfigurationException {

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

    @Then("I got an error and my transaction is failed")
    public void i_got_an_error_and_my_transaction_is_failed() {
        await().atMost(5, TimeUnit.SECONDS)
                .pollInterval(200, TimeUnit.MILLISECONDS)
                .untilAsserted(() -> {
                    Transaction transaction = transactionRepository.findAll().getFirst();
                    assertEquals(TransactionStatus.FAILED, transaction.getStatus());
                });
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

    @When("I try to transfer {int} euros twice in different transactions very quickly")
    public void i_try_to_transfer_euros_twice_in_different_transactions_very_quickly(Integer quantity) throws InterruptedException, DatatypeConfigurationException {

        JAXBElement<TransactionType> request1 = createTransactionRequest(quantity);
        JAXBElement<TransactionType> request2 = createTransactionRequest(quantity);

        CountDownLatch ready = new CountDownLatch(2);
        CountDownLatch start = new CountDownLatch(1);
        CountDownLatch done = new CountDownLatch(2);

        List<Integer> responses = Collections.synchronizedList(new ArrayList<>());

        Runnable task1 = () -> {
            try {
                ready.countDown();
                start.await();

                int status = restClient.post()
                        .uri("/api/v1/transaction")
                        .body(request1)
                        .header("Idempotency-Key", UUID.randomUUID().toString())
                        .retrieve()
                        .toBodilessEntity()
                        .getStatusCode()
                        .value();

                responses.add(status);

            } catch (Exception e) {
                responses.add(500);
            } finally {
                done.countDown();
            }
        };

        Runnable task2 = () -> {
            try {
                ready.countDown();
                start.await();

                int status = restClient.post()
                        .uri("/api/v1/transaction")
                        .body(request2)
                        .header("Idempotency-Key", UUID.randomUUID().toString())
                        .retrieve()
                        .toBodilessEntity()
                        .getStatusCode()
                        .value();

                responses.add(status);

            } catch (Exception e) {
                responses.add(500);
            } finally {
                done.countDown();
            }
        };

        new Thread(task1).start();
        new Thread(task2).start();

        ready.await();
        start.countDown();
        done.await();

        assertEquals(2, responses.size());

        long success = responses.stream().filter(s -> s == 200).count();
        long failed = responses.stream().filter(s -> s != 200).count();

        assertEquals(2, success);
        assertEquals(0, failed);
    }
}
