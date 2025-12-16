package com.interview.portfolio.service;

import com.interview.portfolio.domain.Asset;
import com.interview.portfolio.domain.Stock;
import java.math.BigDecimal;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * "Cheat Sheet" for Java Streams API.
 * Demonstrates common interview patterns for processing collections.
 */
public class StreamAnalytics {

    /**
     * Pattern 1: Basic Filtering and Mapping.
     * "Get symbols of all assets with price > threshold, sorted alphabetically."
     */
    public List<String> getHighValueSymbols(List<Asset> assets, BigDecimal priceThreshold) {
        return assets.stream()
                .filter(a -> a.getPrice().compareTo(priceThreshold) > 0)
                .map(Asset::getSymbol)
                .sorted()
                .collect(Collectors.toList());
    }

    /**
     * Pattern 2: Grouping By.
     * "Group Stocks by Sector."
     * Note: Inputs are generic Assets, need to filter for Stocks first.
     */
    public Map<String, List<Stock>> groupStocksBySector(List<Asset> assets) {
        return assets.stream()
                .filter(a -> a instanceof Stock)
                .map(a -> (Stock) a)
                .collect(Collectors.groupingBy(Stock::getSector));
    }

    /**
     * Pattern 3: Grouping By with Downstream Collector.
     * "Count how many stocks are in each sector."
     */
    public Map<String, Long> countStocksBySector(List<Asset> assets) {
        return assets.stream()
                .filter(a -> a instanceof Stock)
                .map(a -> (Stock) a)
                .collect(Collectors.groupingBy(Stock::getSector, Collectors.counting()));
    }

    /**
     * Pattern 4: Partitioning By.
     * "Split assets into two lists: Expensive (> 1000) and Cheap (<= 1000)."
     */
    public Map<Boolean, List<Asset>> partitionByValuation(List<Asset> assets) {
        BigDecimal threshold = new BigDecimal("1000");
        return assets.stream()
                .collect(Collectors.partitioningBy(a -> a.getPrice().compareTo(threshold) > 0));
    }

    /**
     * Pattern 5: Summary Statistics.
     * "Get Min, Max, Average price of all assets."
     */
    public DoubleSummaryStatistics getPriceStatistics(List<Asset> assets) {
        return assets.stream()
                .map(Asset::getPrice)
                .mapToDouble(BigDecimal::doubleValue) // Convert to primitive double for stats
                .summaryStatistics();
    }

    /**
     * Pattern 6: To Map (Handling Duplicates).
     * "Create a Map of Symbol -> Asset. If duplicate symbols exist, keep the one
     * with higher price."
     */
    public Map<String, Asset> createSymbolMapHandlingDuplicates(List<Asset> assets) {
        return assets.stream()
                .collect(Collectors.toMap(
                        Asset::getSymbol, // Key Mapper
                        Function.identity(), // Value Mapper (the asset itself)
                        (existing, newer) -> existing.getPrice().compareTo(newer.getPrice()) >= 0 ? existing : newer // Merge
                                                                                                                     // Function
                ));
    }

    /**
     * Pattern 7: FlatMap.
     * "Given a list of portfolios (each having a list of assets), return a unique
     * set of all distinct sectors owned."
     */
    public Set<String> getAllDistinctSectors(List<List<Asset>> multiplePortfolios) {
        return multiplePortfolios.stream()
                .flatMap(List::stream) // Flatten Stream<List<Asset>> to Stream<Asset>
                .filter(a -> a instanceof Stock)
                .map(a -> ((Stock) a).getSector())
                .collect(Collectors.toSet());
    }
}
