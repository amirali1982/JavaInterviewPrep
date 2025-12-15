package com.interview.portfolio;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * In-memory implementation of StockRepository.
 * Demonstrates:
 * - Thread-safe storage (ConcurrentHashMap)
 * - Stream API for filtering
 * - Optional usage
 */
public class MapBasedStockRepository implements StockRepository {
    private final Map<String, Stock> storage = new ConcurrentHashMap<>();

    @Override
    public void save(Stock stock) {
        storage.put(stock.getSymbol(), stock);
    }

    @Override
    public Optional<Stock> findBySymbol(String symbol) {
        return Optional.ofNullable(storage.get(symbol));
    }

    @Override
    public List<Stock> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public List<Stock> findBySector(String sector) {
        if (sector == null)
            return List.of();

        return storage.values().stream()
                .filter(stock -> sector.equalsIgnoreCase(stock.getSector()))
                .collect(Collectors.toList());
    }
}
