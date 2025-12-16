package com.interview.portfolio;

import com.interview.portfolio.repository.MapBasedGenericRepository;
import com.interview.portfolio.domain.Asset;
import com.interview.portfolio.domain.Stock;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

class MapBasedGenericRepositoryTest {

    @Test
    void testAddAsset_PopulatesIdMapAndRegistry() {
        MapBasedGenericRepository<Asset> repository = new MapBasedGenericRepository<>();
        Stock apple = new Stock("AAPL", "Apple", "Tech", new BigDecimal("100"));

        repository.add(apple);

        // Verify ID lookup (Standard functionality)
        Optional<Asset> retrieved = repository.findById("AAPL");
        assertTrue(retrieved.isPresent());
        assertEquals(apple, retrieved.get());

        // Verify Registry lookup (Reflection needed to check internal state or we
        // presume it works if we can't access it?
        // Actually, the user's snippet implied internal logic. We can't access
        // 'assetRegistry' directly unless we make it package-private or use reflection.
        // Given this is an interview prep project, using Reflection to test internal
        // state is a valid "Advanced" technique to demonstrate.)

        List<Asset> registryList = getRegistryList(repository, Stock.class);
        assertNotNull(registryList);
        assertEquals(1, registryList.size());
        assertEquals(apple, registryList.get(0));
    }

    @Test
    void testAddNonAsset_PopulatesRegistryOnly() {
        MapBasedGenericRepository<Object> repository = new MapBasedGenericRepository<>();
        String testString = "Hello World";

        repository.add(testString);

        // Registry check
        List<Object> registryList = getRegistryList(repository, String.class);
        assertNotNull(registryList);
        assertEquals(1, registryList.size());
        assertEquals(testString, registryList.get(0));
    }

    @SuppressWarnings("unchecked")
    private <T> List<T> getRegistryList(MapBasedGenericRepository<?> repository, Class<?> key) {
        try {
            Field registryField = MapBasedGenericRepository.class.getDeclaredField("assetRegistry");
            registryField.setAccessible(true);
            Map<Class<?>, List<?>> registry = (Map<Class<?>, List<?>>) registryField.get(repository);
            return (List<T>) registry.get(key);
        } catch (Exception e) {
            throw new RuntimeException("Failed to access internal registry via reflection", e);
        }
    }
}
