package com.interview.portfolio;

import com.interview.portfolio.service.PortfolioService;
import com.interview.portfolio.repository.GenericRepository;
import com.interview.portfolio.domain.Stock;
import com.interview.portfolio.domain.Asset;
import com.interview.portfolio.domain.Portfolio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PortfolioServiceTest {

    @Mock
    private GenericRepository<Asset> assetRepository;

    private PortfolioService portfolioService;

    @BeforeEach
    void setUp() {
        portfolioService = new PortfolioService(assetRepository);
    }

    @Test
    void testCalculateTotalValue() {
        // Arrange
        Stock apple = new Stock("AAPL", "Apple Inc.", "Tech", new BigDecimal("150.00"));
        Stock tesla = new Stock("TSLA", "Tesla Inc.", "Auto", new BigDecimal("200.00"));

        Portfolio portfolio = new Portfolio();
        portfolio.addAsset(apple, 10); // 1500
        portfolio.addAsset(tesla, 5); // 1000

        when(assetRepository.findById("AAPL")).thenReturn(Optional.of(apple));
        when(assetRepository.findById("TSLA")).thenReturn(Optional.of(tesla));

        // Act
        BigDecimal totalValue = portfolioService.calculateTotalValue(portfolio);

        // Assert
        assertEquals(new BigDecimal("2500.00"), totalValue);
    }

    @Test
    void testCalculateTotalValue_UnknownStock() {
        Portfolio portfolio = new Portfolio();
        // We add a stock to portfolio physically, but the repo doesn't know about it.
        // In a real app, adding to portfolio might require repo validation first,
        // but here we test the service's reaction to missing data.
        Stock unknown = new Stock("UNKNOWN", "Unknown", "None", new BigDecimal("10"));
        portfolio.addAsset(unknown, 1);

        when(assetRepository.findById("UNKNOWN")).thenReturn(Optional.empty());

        assertThrows(IllegalStateException.class, () -> portfolioService.calculateTotalValue(portfolio));
    }
}
