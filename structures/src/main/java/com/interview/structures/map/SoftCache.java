package com.interview.structures.map;

import java.lang.ref.SoftReference;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * A Cache that uses SoftReferences.
 * Key Concept: Objects in this cache will be cleared by the Garbage Collector
 * only if the JVM is running low on memory.
 * Useful for caching images, file contents, etc. without causing
 * OutOfMemoryError.
 */
public class SoftCache<K, V> {

    // We store the Value wrapped in a SoftReference
    private final Map<K, SoftReference<V>> map = new HashMap<>();

    public void put(K key, V value) {
        map.put(key, new SoftReference<>(value));
    }

    public Optional<V> get(K key) {
        SoftReference<V> ref = map.get(key);
        if (ref == null) {
            return Optional.empty();
        }

        V value = ref.get();
        if (value == null) {
            // GC has cleared it
            map.remove(key); // Cleanup
            return Optional.empty();
        }
        return Optional.of(value);
    }

    public int size() {
        return map.size();
    }
}
