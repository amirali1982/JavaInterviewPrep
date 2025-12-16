package com.interview.portfolio;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class StreamAnalyticsTest {

    private final StreamAnalytics analytics = new StreamAnalytics();

    // Data Setup
    private final Stock apple = new Stock("AAPL", "Apple", "Tech", new BigDecimal("150"));
    private final Stock google = new Stock("GOOGL", "Google", "Tech", new BigDecimal("2800"));
    private final Stock coke = new Stock("KO", "Coca Cola", "Consumer", new BigDecimal("60"));
    private final Bond bond = new Bond("US10Y", new BigDecimal("100"), new BigDecimal("0.05")); // Price 100

    private final List<Asset> assets = List.of(apple, google, coke, bond);

    @Test
    void testGetHighValueSymbols() {
        // Threshold 200. Should find Google (2800).
        List<String> result = analytics.getHighValueSymbols(assets, new BigDecimal("200"));
        assertEquals(1, result.size());
        assertEquals("GOOGL", result.get(0));
    }

    @Test
    void testGroupStocksBySector() {
        Map<String, List<Stock>> result = analytics.groupStocksBySector(assets);

        // Tech: Apple, Google
        // Consumer: Coke
        // Bond is ignored
        assertEquals(2, result.size());
        assertEquals(2, result.get("Tech").size());
        assertEquals(1, result.get("Consumer").size());
    }

    @Test
    void testCountStocksBySector() {
        Map<String, Long> result = analytics.countStocksBySector(assets);
        assertEquals(2L, result.get("Tech"));
        assertEquals(1L, result.get("Consumer"));
    }

    @Test
    void testPartitionByValuation() {
        // Threshold 1000.
        // True (>1000): Google
        // False (<=1000): Apple, Coke, Bond
        Map<Boolean, List<Asset>> result = analytics.partitionByValuation(assets);

        assertEquals(1, result.get(true).size());
        assertEquals(3, result.get(false).size());
        assertTrue(result.get(true).contains(google));
    }

    @Test
    void testPriceStatistics() {
        DoubleSummaryStatistics stats = analytics.getPriceStatistics(assets);

        // Count: 4
        // Min: 60 (Coke)
        // Max: 2800 (Google)
        // Avg: (150+2800+60+100)/4 = 3110/4 = 777.5
        assertEquals(4, stats.getCount());
        assertEquals(60.0, stats.getMin());
        assertEquals(2800.0, stats.getMax());
        assertEquals(777.5, stats.getAverage());
    }

    @Test
    void testCreateSymbolMapHandlingDuplicates() {
        // Add a duplicate Apple with higher price
        Stock expensiveApple = new Stock("AAPL", "Apple Future", "Tech", new BigDecimal("200"));
        List<Asset> mixedList = new ArrayList<>(assets);
        mixedList.add(expensiveApple);

        Map<String, Asset> result = analytics.createSymbolMapHandlingDuplicates(mixedList);

        // Should have "AAPL" mapped to expensiveApple (200 > 150)
        assertEquals(4, result.size()); // AAPL, GOOGL, KO, US10Y
        assertEquals(new BigDecimal("200"), result.get("AAPL").getPrice());
    }

    @Test
    void testGetAllDistinctSectors_FlatMap() {
        List<Asset> portfolio1 = List.of(apple); // Tech
        List<Asset> portfolio2 = List.of(coke); // Consumer

        Set<String> sectors = analytics.getAllDistinctSectors(List.of(portfolio1, portfolio2));

        assertEquals(2, sectors.size());
        assertTrue(sectors.contains("Tech"));
        assertTrue(sectors.contains("Consumer"));
    }
}
