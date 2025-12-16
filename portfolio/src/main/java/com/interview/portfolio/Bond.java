package com.interview.portfolio;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Represents a Bond asset.
 * Extends Asset directly.
 */
public final class Bond extends Asset {
    private final BigDecimal interestRate;

    public Bond(String symbol, BigDecimal price, BigDecimal interestRate) {
        super(symbol, price);
        this.interestRate = Objects.requireNonNull(interestRate, "Interest rate cannot be null");
        if (interestRate.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Interest rate cannot be negative");
        }
    }

    public BigDecimal getInterestRate() {
        return interestRate;
    }

    @Override
    public String toString() {
        return "Bond{" +
                "symbol='" + getSymbol() + '\'' +
                ", price=" + getPrice() +
                ", interestRate=" + interestRate +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Bond))
            return false;
        if (!super.equals(o))
            return false;
        Bond bond = (Bond) o;
        return Objects.equals(interestRate, bond.interestRate);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), interestRate);
    }
}
