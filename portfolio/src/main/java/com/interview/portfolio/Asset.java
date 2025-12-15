package com.interview.portfolio;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Abstract base class for all assets in the portfolio.
 * Serves as the root of the hierarchy for Generic constraints.
 */
public abstract class Asset {
    private final String symbol;
    private final BigDecimal price;

    protected Asset(String symbol, BigDecimal price) {
        this.symbol = Objects.requireNonNull(symbol, "Symbol cannot be null");
        this.price = Objects.requireNonNull(price, "Price cannot be null");
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
    }

    public String getSymbol() {
        return symbol;
    }

    public BigDecimal getPrice() {
        return price;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Asset))
            return false;
        Asset asset = (Asset) o;
        return Objects.equals(symbol, asset.symbol) && Objects.equals(price, asset.price);
    }

    @Override
    public int hashCode() {
        return Objects.hash(symbol, price);
    }
}
