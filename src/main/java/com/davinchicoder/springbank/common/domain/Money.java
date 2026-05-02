package com.davinchicoder.springbank.common.domain;


import java.math.BigDecimal;
import java.util.Currency;

public record Money(long amountInCents, Currency currency) {

    public static Money of(BigDecimal amount, Currency currency) {
        return new Money(amount.movePointRight(2).longValueExact(), currency);
    }

    public BigDecimal toBigDecimal() {
        return BigDecimal.valueOf(amountInCents, 2);
    }

    public Money add(Money other) {
        validateSameCurrency(other);
        return new Money(this.amountInCents + other.amountInCents, currency);
    }

    public Money subtract(Money other) {
        validateSameCurrency(other);
        return new Money(this.amountInCents - other.amountInCents, currency);
    }

    private void validateSameCurrency(Money other) {
        if (!currency.equals(other.currency)) {
            throw new IllegalArgumentException("Different currencies");
        }
    }
}
