package com.interview.portfolio;

import java.math.BigDecimal;
import java.util.Map;

public class PortfolioService {
    private final StockRepository stockRepository;

    public PortfolioService(StockRepository stockRepository) {
        this.stockRepository = stockRepository;
    }

    public BigDecimal calculateTotalValue(Portfolio portfolio) {
        BigDecimal total = BigDecimal.ZERO;
        for (Map.Entry<String, Integer> entry : portfolio.getHoldings().entrySet()) {
            String symbol = entry.getKey();
            int quantity = entry.getValue();

            Stock stock = stockRepository.findBySymbol(symbol)
                    .orElseThrow(() -> new IllegalStateException("Stock not found for symbol: " + symbol));

            total = total.add(stock.price().multiply(BigDecimal.valueOf(quantity)));
        }
        return total;
    }
}
