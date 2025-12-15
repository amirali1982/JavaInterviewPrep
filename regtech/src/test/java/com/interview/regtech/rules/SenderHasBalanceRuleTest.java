package com.interview.regtech.rules;

import com.interview.portfolio.Portfolio;
import com.interview.portfolio.Stock;
import com.interview.regtech.RuleResult;
import com.interview.regtech.TransferContext;
import com.interview.regtech.repository.PortfolioRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SenderHasBalanceRuleTest {

    @Mock
    private PortfolioRepository portfolioRepository;

    private SenderHasBalanceRule rule;
    private TransferContext context;
    private Stock stock;

    @BeforeEach
    void setUp() {
        rule = new SenderHasBalanceRule(portfolioRepository);
        context = new TransferContext("sender1", "receiver1", "AAPL", 10);
        stock = new Stock("AAPL", "Apple", "Tech", BigDecimal.TEN);
    }

    @Test
    void shouldFailWhenSenderNotFound() {
        when(portfolioRepository.findById("sender1")).thenReturn(Optional.empty());

        RuleResult result = rule.validate(context);

        assertFalse(result.isValid());
        assertEquals("Sender portfolio not found", result.errorMessage());
    }

    @Test
    void shouldFailWhenInsufficientBalance() {
        Portfolio portfolio = new Portfolio();
        // 5 is less than required 10
        portfolio.addAsset(stock, 5);
        when(portfolioRepository.findById("sender1")).thenReturn(Optional.of(portfolio));

        RuleResult result = rule.validate(context);

        assertFalse(result.isValid());
        assertEquals("Insufficient balance for symbol: AAPL", result.errorMessage());
    }

    @Test
    void shouldPassWhenSufficientBalance() {
        Portfolio portfolio = new Portfolio();
        portfolio.addAsset(stock, 10);
        when(portfolioRepository.findById("sender1")).thenReturn(Optional.of(portfolio));

        RuleResult result = rule.validate(context);

        assertTrue(result.isValid());
        assertNull(result.errorMessage());
    }
}
