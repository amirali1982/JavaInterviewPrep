package com.interview.portfolio.service;

import com.interview.portfolio.domain.Asset;
import com.interview.portfolio.domain.Portfolio;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

/**
 * Service to demonstrate Modern Async Programming (Java 8+ CompletableFuture).
 * Simulates fetching data from remote APIs and processing it in parallel.
 */
public class AnalysisService {

    private final ExecutorService executor = Executors.newFixedThreadPool(10);

    /**
     * Simulates a potentially slow remote API call to get market predictions.
     * Demonstrates: supplyAsync (Running in background)
     */
    public CompletableFuture<BigDecimal> predictFutureValue(Asset asset, int months) {
        return CompletableFuture.supplyAsync(() -> {
            simulateLatency();
            // Mock logic: Price * (1 + (0.01 * months))
            // Simulates a 1% growth per month
            BigDecimal growthFactor = BigDecimal.valueOf(1 + (0.01 * months));
            return asset.getPrice().multiply(growthFactor).setScale(2, RoundingMode.HALF_UP);
        }, executor).exceptionally(ex -> {
            // Demonstrate error handling
            System.err.println("Failed to predict for " + asset.getSymbol() + ": " + ex.getMessage());
            return BigDecimal.ZERO; // Fallback
        });
    }

    /**
     * Analyzes the entire portfolio in parallel.
     * Demonstrates: fan-out (creating many futures) and fan-in (allOf / join).
     */
    public CompletableFuture<Map<String, BigDecimal>> analyzePortfolio(Portfolio portfolio) {
        // 1. Convert Map entries to a List of Asset objects (mocking retrieving them
        // from repo/holdings)
        // In this simple model, we assume we just analyze the symbols we have.
        // For demonstration, we just parse key strings for now or need a way to get
        // Assets.
        // LIMITATION: Portfolio only stores Map<String, Integer> (holdings). We need
        // Assets.
        // Assuming we pass in a list of Assets for analysis, or we modify signature.
        throw new UnsupportedOperationException("Needs list of Assets, but Portfolio only holds symbols");
    }

    // Fixed signature to accept list of Assets
    public CompletableFuture<Map<String, BigDecimal>> analyzeAssets(List<Asset> assets, int months) {
        List<CompletableFuture<Map.Entry<String, BigDecimal>>> futures = assets.stream()
                .map(asset -> predictFutureValue(asset, months)
                        .thenApply(predictedPrice -> Map.entry(asset.getSymbol(), predictedPrice)))
                .toList();

        return CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]))
                .thenApply(v -> futures.stream()
                        .map(CompletableFuture::join)
                        .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue)));
    }

    private void simulateLatency() {
        try {
            // 50-200ms sleep
            Thread.sleep((long) (Math.random() * 150 + 50));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    public void shutdown() {
        executor.shutdown();
    }
}
