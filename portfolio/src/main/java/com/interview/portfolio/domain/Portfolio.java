package com.interview.portfolio.domain;

import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Represents a user's portfolio.
 * Uses ConcurrentHashMap to ensure thread safety for concurrent access/updates.
 */
public class Portfolio {
    private final Map<String, Integer> holdings = new ConcurrentHashMap<>();

    public void addAsset(Asset asset, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        holdings.merge(asset.getSymbol(), quantity, Integer::sum);
    }

    public void removeAsset(Asset asset, int quantity) {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
        holdings.compute(asset.getSymbol(), (symbol, currentQty) -> {
            if (currentQty == null || currentQty < quantity) {
                throw new IllegalStateException("Insufficient healthy quantity to sell");
            }
            int newQty = currentQty - quantity;
            return newQty == 0 ? null : newQty;
        });
    }

    public Map<String, Integer> getHoldings() {
        return Collections.unmodifiableMap(holdings);
    }
}
