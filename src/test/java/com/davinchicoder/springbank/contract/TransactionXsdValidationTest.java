package com.davinchicoder.springbank.contract;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TransactionXsdValidationTest {

    private static XmlSchemaValidator validator;

    @BeforeAll
    static void setup() {
        validator = new XmlSchemaValidator("xsd/transaction.xsd");
    }

    @Test
    void shouldValidateCorrectXml() {
        assertDoesNotThrow(() ->
                validator.validate("contract/valid-request.xml")
        );
    }

    @Test
    void shouldFailWhenMissingRequiredField() {
        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                validator.validate("contract/invalid-request-missing-field.xml")
        );

        assertTrue(ex.getMessage().contains("cvc-complex-type"));
    }

    @Test
    void shouldFailWhenDateIsInvalid() {
        RuntimeException ex = assertThrows(RuntimeException.class, () ->
                validator.validate("contract/invalid-request-bad-date.xml")
        );

        assertTrue(ex.getMessage().contains("dateTime"));
    }
}
