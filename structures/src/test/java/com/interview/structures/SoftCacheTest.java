package com.interview.structures;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class SoftCacheTest {

    @Test
    void testCacheBehavior() {
        SoftCache<String, String> cache = new SoftCache<>();
        cache.put("key", "value");

        assertTrue(cache.get("key").isPresent());
        assertEquals("value", cache.get("key").get());

        // Note: We cannot deterministically test GC clearing SoftRef without
        // allocating massive memory to force pressure, which is flaky.
        // We verify the basic functionality.
    }
}
