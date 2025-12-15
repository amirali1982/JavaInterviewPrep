package com.interview.portfolio;

import java.math.BigDecimal;
import java.util.Map;

public class PortfolioService {
    private final GenericRepository<Asset> assetRepository;

    public PortfolioService(GenericRepository<Asset> assetRepository) {
        this.assetRepository = assetRepository;
    }

    public BigDecimal calculateTotalValue(Portfolio portfolio) {
        BigDecimal total = BigDecimal.ZERO;
        for (Map.Entry<String, Integer> entry : portfolio.getHoldings().entrySet()) {
            String symbol = entry.getKey();
            int quantity = entry.getValue();

            Asset asset = assetRepository.findById(symbol)
                    .orElseThrow(() -> new IllegalStateException("Asset not found for symbol: " + symbol));

            total = total.add(asset.getPrice().multiply(BigDecimal.valueOf(quantity)));
        }
        return total;
    }
}
