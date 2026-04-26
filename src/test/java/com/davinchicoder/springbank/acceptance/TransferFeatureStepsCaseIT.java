package com.davinchicoder.springbank.acceptance;

import com.bank.transaction.ObjectFactory;
import com.bank.transaction.TransactionType;
import com.bank.transaction.TransactionTypeEnum;
import com.davinchicoder.springbank.ledger.infrastructure.LedgerEntryRepository;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.spring.CucumberContextConfiguration;
import io.cucumber.spring.ScenarioScope;
import jakarta.xml.bind.JAXBElement;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.client.RestClient;

import javax.xml.datatype.DatatypeConfigurationException;
import javax.xml.datatype.DatatypeFactory;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.ZoneId;
import java.util.GregorianCalendar;
import java.util.UUID;

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

    @Given("An account with {int} euros")
    public void an_account_with_euros(Integer actualBalance) {
        assertNotNull(actualBalance);
    }

    @When("I transfer {int} euros to another")
    public void i_transfer_euros_to_another(Integer quantity) throws DatatypeConfigurationException {

        TransactionType transactionType = new TransactionType();
        transactionType.setId(UUID.randomUUID().toString());
        transactionType.setType(TransactionTypeEnum.DEBIT);
        transactionType.setAmount(BigDecimal.valueOf(quantity));
        transactionType.setFromAccount(FROM_ACCOUNT);
        transactionType.setToAccount(TO_ACCOUNT);
        transactionType.setAmount(BigDecimal.valueOf(quantity));
        transactionType.setCreatedAt(DatatypeFactory.newInstance().newXMLGregorianCalendar(GregorianCalendar.from(Instant.now().atZone(ZoneId.systemDefault()))));

        JAXBElement<TransactionType> requestBody = new ObjectFactory().createTransaction(transactionType);

        restClient.post()
                .uri("/api/v1/transaction")
                .body(requestBody)
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
}
