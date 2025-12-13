package com.interview.regtech;

import java.math.BigDecimal;

public record TransferContext(String senderId, String receiverId, String stockSymbol, int quantity) {
    public TransferContext {
        if (quantity <= 0) {
            throw new IllegalArgumentException("Quantity must be positive");
        }
    }
}
