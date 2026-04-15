package com.davinchicoder.springbank.acceptance;

import com.bank.transaction.ObjectFactory;
import com.bank.transaction.TransactionType;
import com.bank.transaction.TransactionTypeEnum;
import com.davinchicoder.springbank.ledger.domain.EntryType;
import com.davinchicoder.springbank.ledger.domain.LedgerEntry;
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
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ScenarioScope
@CucumberContextConfiguration
@RequiredArgsConstructor
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
public class TransferFeatureStepsCaseIT {

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
        transactionType.setFromAccount("ES3601520826");
        transactionType.setToAccount("ES3601520826");
        transactionType.setAmount(BigDecimal.valueOf(quantity));
        transactionType.setCreatedAt(DatatypeFactory.newInstance().newXMLGregorianCalendar(GregorianCalendar.from(Instant.now().atZone(ZoneId.systemDefault()))));

        JAXBElement<TransactionType> requestBody =
                new ObjectFactory().createTransaction(transactionType);

        restClient.post()
                .uri("/api/v1/transaction")
                .body(requestBody)
                .exchange((request, response) -> {
                    assertEquals(200, response.getStatusCode().value());
                    return null;
                });
    }

    @Then("The final amount is {int} euros")
    public void the_final_amount_is_euros(Integer expected) {
        Map<EntryType, List<LedgerEntry>> groupedEntries = ledgerEntryRepository.findAll()
                .stream()
                .collect(Collectors.groupingBy(LedgerEntry::getType));

        LedgerEntry debit = groupedEntries.get(EntryType.DEBIT).getFirst();
        LedgerEntry credit = groupedEntries.get(EntryType.CREDIT).getFirst();

        BigDecimal finalBalance = debit.getAmount().subtract(credit.getAmount());

        assertEquals(0, finalBalance.intValue());
    }
}
