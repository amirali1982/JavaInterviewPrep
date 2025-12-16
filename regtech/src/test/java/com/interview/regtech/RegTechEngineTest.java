package com.interview.regtech;

import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RegTechEngineTest {

    @Test
    void testLoadRulesFromSPI() throws NoSuchFieldException, IllegalAccessException {
        RegTechEngine engine = new RegTechEngine();

        // Initially empty
        assertEquals(0, getRulesCount(engine));

        // Load from SPI
        engine.loadRulesFromSPI();

        // Should have loaded MarketIsOpenRule and ReceiverExistsRule (2 rules)
        // Adjust expectation based on META-INF content
        int count = getRulesCount(engine);
        assertTrue(count >= 2, "Should load at least 2 rules from SPI");
    }

    private int getRulesCount(RegTechEngine engine) throws NoSuchFieldException, IllegalAccessException {
        Field rulesField = RegTechEngine.class.getDeclaredField("rules");
        rulesField.setAccessible(true);
        List<?> rules = (List<?>) rulesField.get(engine);
        return rules.size();
    }
}
