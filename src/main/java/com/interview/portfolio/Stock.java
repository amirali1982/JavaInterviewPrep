package com.interview.portfolio;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Clean immutable data carrier for Stock information.
 * Demonstrates use of Java Records (Java 14+).
 */
public record Stock(String symbol, String name, String sector, BigDecimal price) {
    public Stock {
        Objects.requireNonNull(symbol, "Symbol cannot be null");
        Objects.requireNonNull(name, "Name cannot be null");
        Objects.requireNonNull(sector, "Sector cannot be null");
        Objects.requireNonNull(price, "Price cannot be null");
        if (price.compareTo(BigDecimal.ZERO) < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
    }
}
