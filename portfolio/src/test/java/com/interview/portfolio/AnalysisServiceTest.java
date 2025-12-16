package com.interview.portfolio;

import com.interview.portfolio.service.AnalysisService;
import com.interview.portfolio.domain.Asset;
import com.interview.portfolio.domain.Stock;
import com.interview.portfolio.domain.Bond;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

import static org.junit.jupiter.api.Assertions.*;

class AnalysisServiceTest {

    private AnalysisService service;

    @BeforeEach
    void setUp() {
        service = new AnalysisService();
    }

    @AfterEach
    void tearDown() {
        service.shutdown();
    }

    @Test
    void testPredictFutureValue_Success() throws ExecutionException, InterruptedException {
        Asset apple = new Stock("AAPL", "Apple", "Tech", new BigDecimal("100.00"));

        CompletableFuture<BigDecimal> future = service.predictFutureValue(apple, 10);

        // This blocks, but it's a test
        BigDecimal result = future.get();

        // 100 * (1 + 0.10) = 110.00
        assertEquals(new BigDecimal("110.00"), result);
    }

    @Test
    void testAnalyzeAssets_Parallel() throws ExecutionException, InterruptedException {
        Asset apple = new Stock("AAPL", "Apple", "Tech", new BigDecimal("100.00"));
        Asset bond = new Bond("US10Y", new BigDecimal("1000.00"), new BigDecimal("0.05"));

        CompletableFuture<Map<String, BigDecimal>> future = service.analyzeAssets(List.of(apple, bond), 12);

        Map<String, BigDecimal> results = future.get();

        assertEquals(2, results.size());

        // Apple: 100 * 1.12 = 112.00
        assertEquals(new BigDecimal("112.00"), results.get("AAPL"));
        // Bond: 1000 * 1.12 = 1120.00 (Logic is simplistic for demo)
        assertEquals(new BigDecimal("1120.00"), results.get("US10Y"));
    }
}
