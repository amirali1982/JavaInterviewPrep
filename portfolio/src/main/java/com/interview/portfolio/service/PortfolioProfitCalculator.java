package com.interview.portfolio.service;

import com.interview.portfolio.domain.Asset;
import com.interview.portfolio.domain.Bond;
import com.interview.portfolio.domain.Stock;
import java.math.BigDecimal;

/**
 * Demonstrates Modern Java Syntax (Java 14 - 17 Standard Features).
 * 1. Pattern Matching for instanceof (Java 16)
 * 2. Switch Expressions (Java 14)
 */
public class PortfolioProfitCalculator {

    /**
     * Calculates tax using Pattern Matching for instanceof.
     * Prevents the need for explicit casting: 'Stock s = (Stock) asset;'
     */
    public BigDecimal calculateEstimatedTax(Asset asset) {
        // Pattern Matching: 'Stock s' is declared and assigned implicitly if check
        // passes
        if (asset instanceof Stock s) {
            // Logic: Stocks taxed at 15% of price (simplified capital gains)
            return s.getPrice().multiply(new BigDecimal("0.15"));
        } else if (asset instanceof Bond b) {
            // Logic: Bonds taxed at 10%
            // Note: We can access 'b' directly here
            return b.getPrice().multiply(new BigDecimal("0.10"));
        } else {
            return BigDecimal.ZERO;
        }
    }

    /**
     * Evaluates sector risk using Switch Expressions.
     * Demonstrates: Arrow syntax (->), Yielding values, Exhaustiveness (if Enum
     * used).
     */
    public String evaluateSectorRisk(String sector) {
        return switch (sector) {
            case "Tech", "Crypto" -> "High Risk";
            case "Consumer", "Utilities" -> "Low Risk";
            case "Finance" -> {
                // Multi-line block example
                String risk = "Medium Risk";
                yield risk; // 'yield' is the new 'return' for switch expressions
            }
            default -> "Unknown Risk";
        };
    }
}
