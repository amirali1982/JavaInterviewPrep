package com.interview.portfolio.domain;

import java.math.BigDecimal;
import java.util.Objects;

/**
 * Clean immutable data carrier for Stock information.
 * Extends Asset to demonstrate inheritance and is used with restricted
 * Generics.
 */
public non-sealed class Stock extends Asset {
    private final String name;
    private final String sector;

    public Stock(String symbol, String name, String sector, BigDecimal price) {
        super(symbol, price);
        this.name = Objects.requireNonNull(name, "Name cannot be null");
        this.sector = Objects.requireNonNull(sector, "Sector cannot be null");
    }

    public String getName() {
        return name;
    }

    public String getSector() {
        return sector;
    }

    @Override
    public String toString() {
        return "Stock{" +
                "symbol='" + getSymbol() + '\'' +
                ", name='" + name + '\'' +
                ", sector='" + sector + '\'' +
                ", price=" + getPrice() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof Stock))
            return false;
        if (!super.equals(o))
            return false;
        Stock stock = (Stock) o;
        return Objects.equals(name, stock.name) && Objects.equals(sector, stock.sector);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), name, sector);
    }
}
