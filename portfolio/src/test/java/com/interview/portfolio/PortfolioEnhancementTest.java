package com.interview.portfolio;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Tests for the Generics and Reflection enhancements.
 */
class PortfolioEnhancementTest {

    @Test
    void testGenericRepositoryWithStock() {
        GenericRepository<Stock> repository = new MapBasedGenericRepository<>();
        Stock apple = new Stock("AAPL", "Apple Inc.", "Technology", new BigDecimal("150.00"));

        repository.save(apple);
        Optional<Stock> retrieved = repository.findById("AAPL");

        assertTrue(retrieved.isPresent());
        assertEquals("Apple Inc.", retrieved.get().getName());
    }

    @Test
    void testReflectiveAssetInspector() {
        ReflectiveAssetInspector inspector = new ReflectiveAssetInspector();

        assertTrue(inspector.checkSubclass(Stock.class, Asset.class), "Stock should be a subclass of Asset");
        assertFalse(inspector.checkSubclass(String.class, Asset.class), "String is not a subclass of Asset");
        assertFalse(inspector.checkSubclass(Asset.class, Asset.class),
                "Asset is not a subclass of itself (strict check)");

        assertDoesNotThrow(() -> inspector.validateAssetType(Stock.class));
        assertThrows(IllegalArgumentException.class, () -> inspector.validateAssetType(String.class));
    }

    @Test
    void testAssetHierarchy() {
        Stock stock = new Stock("GOOGL", "Alphabet", "Tech", new BigDecimal("2000"));
        assertTrue(stock instanceof Asset, "Stock instance should be an instance of Asset");
        assertEquals("GOOGL", stock.getSymbol());
    }

    @Test
    void testBondAndRestrictedStock() {
        Bond bond = new Bond("US10Y", new BigDecimal("100.00"), new BigDecimal("3.5"));
        RestrictedStock rsu = new RestrictedStock("META", "Meta", "Tech", new BigDecimal("300.00"), 12);

        // Verify inheritance
        assertTrue(bond instanceof Asset);
        assertTrue(rsu instanceof Stock);
        assertTrue(rsu instanceof Asset);

        // Verify Reflection Inspector on new types
        ReflectiveAssetInspector inspector = new ReflectiveAssetInspector();
        assertTrue(inspector.checkSubclass(Bond.class, Asset.class));
        assertTrue(inspector.checkSubclass(RestrictedStock.class, Stock.class));
        assertTrue(inspector.checkSubclass(RestrictedStock.class, Asset.class));

        // Use Generic Repository with new types
        GenericRepository<Asset> assetRepo = new MapBasedGenericRepository<>();
        assetRepo.save(bond);
        assetRepo.save(rsu);

        assertEquals(bond, assetRepo.findById("US10Y").get());
        assertEquals(rsu, assetRepo.findById("META").get());
    }
}
