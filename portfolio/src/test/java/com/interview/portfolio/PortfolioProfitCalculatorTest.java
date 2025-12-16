package com.interview.portfolio;

import com.interview.portfolio.service.PortfolioProfitCalculator;
import com.interview.portfolio.domain.Asset;
import com.interview.portfolio.domain.Stock;
import com.interview.portfolio.domain.Bond;
import org.junit.jupiter.api.Test;
import java.math.BigDecimal;
import static org.junit.jupiter.api.Assertions.*;

class PortfolioProfitCalculatorTest {

    private final PortfolioProfitCalculator calculator = new PortfolioProfitCalculator();

    @Test
    void testPatternMatching() {
        Asset stock = new Stock("AAPL", "Apple", "Tech", new BigDecimal("100"));
        Asset bond = new Bond("US10Y", new BigDecimal("100"), new BigDecimal("0.05"));

        // Stock tax: 100 * 0.15 = 15.00
        assertEquals(0, new BigDecimal("15.00").compareTo(calculator.calculateEstimatedTax(stock)));

        // Bond tax: 100 * 0.10 = 10.00
        assertEquals(0, new BigDecimal("10.00").compareTo(calculator.calculateEstimatedTax(bond)));
    }

    @Test
    void testSwitchExpression() {
        assertEquals("High Risk", calculator.evaluateSectorRisk("Tech"));
        assertEquals("Low Risk", calculator.evaluateSectorRisk("Utilities"));
        assertEquals("Medium Risk", calculator.evaluateSectorRisk("Finance"));
        assertEquals("Unknown Risk", calculator.evaluateSectorRisk("BioTech"));
    }
}
