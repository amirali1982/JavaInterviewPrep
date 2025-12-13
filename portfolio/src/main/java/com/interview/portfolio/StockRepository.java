package com.interview.portfolio;

import java.util.List;
import java.util.Optional;

public interface StockRepository {
    void save(Stock stock);

    Optional<Stock> findBySymbol(String symbol);

    List<Stock> findAll();

    /**
     * Finds stocks belonging to a specific sector.
     */
    List<Stock> findBySector(String sector);
}
