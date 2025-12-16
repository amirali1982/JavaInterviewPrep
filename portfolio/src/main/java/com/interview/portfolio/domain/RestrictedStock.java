package com.interview.portfolio.domain;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Represents a Restricted Stock Unit (RSU).
 * Extends Stock, demonstrating multi-level inheritance (RestrictedStock ->
 * Stock -> Asset).
 */
public class RestrictedStock extends Stock {
    private final int lockupPeriodMonths;

    public RestrictedStock(String symbol, String name, String sector, BigDecimal price, int lockupPeriodMonths) {
        super(symbol, name, sector, price);
        if (lockupPeriodMonths < 0) {
            throw new IllegalArgumentException("Lockup period cannot be negative");
        }
        this.lockupPeriodMonths = lockupPeriodMonths;
    }

    public int getLockupPeriodMonths() {
        return lockupPeriodMonths;
    }

    @Override
    public String toString() {
        return "RestrictedStock{" +
                "symbol='" + getSymbol() + '\'' +
                ", name='" + getName() + '\'' +
                ", sector='" + getSector() + '\'' +
                ", price=" + getPrice() +
                ", lockupPeriodMonths=" + lockupPeriodMonths +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof RestrictedStock))
            return false;
        if (!super.equals(o))
            return false;
        RestrictedStock that = (RestrictedStock) o;
        return lockupPeriodMonths == that.lockupPeriodMonths;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), lockupPeriodMonths);
    }
}
