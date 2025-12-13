package com.interview.portfolio;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class MapBasedStockRepositoryTest {

    private MapBasedStockRepository repository;

    @BeforeEach
    void setUp() {
        repository = new MapBasedStockRepository();
        repository.save(new Stock("AAPL", "Apple", "Technology", new BigDecimal("150.00")));
        repository.save(new Stock("MSFT", "Microsoft", "Technology", new BigDecimal("300.00")));
        repository.save(new Stock("XOM", "Exxon", "Energy", new BigDecimal("100.00")));
    }

    @Test
    void testFindBySymbol() {
        Optional<Stock> stock = repository.findBySymbol("AAPL");
        assertTrue(stock.isPresent());
        assertEquals("Apple", stock.get().name());
    }

    @Test
    void testFindBySector() {
        List<Stock> techStocks = repository.findBySector("Technology");
        assertEquals(2, techStocks.size());

        List<Stock> energyStocks = repository.findBySector("Energy");
        assertEquals(1, energyStocks.size());
    }

    @Test
    void testCaseInsensitivity() {
        List<Stock> stocks = repository.findBySector("technology");
        assertEquals(2, stocks.size());
    }
}
