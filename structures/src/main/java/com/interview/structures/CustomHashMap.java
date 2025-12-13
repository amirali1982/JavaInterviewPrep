package com.interview.structures;

/**
 * A simple HashMap implementation using Array of Linked Lists (Chaining).
 * Demonstrates: hashCode(), index calculation, collision resolution.
 *
 * @param <K> Key type
 * @param <V> Value type
 */
public class CustomHashMap<K, V> {
    private static final int CAPACITY = 16;

    private static class Entry<K, V> {
        final K key;
        V value;
        Entry<K, V> next;

        Entry(K key, V value) {
            this.key = key;
            this.value = value;
        }
    }

    @SuppressWarnings("unchecked")
    private Entry<K, V>[] buckets = new Entry[CAPACITY];

    public void put(K key, V value) {
        if (key == null)
            return; // Simplified: No null keys support for this example

        int bucketIndex = getBucketIndex(key);
        Entry<K, V> head = buckets[bucketIndex];

        // Check availability
        while (head != null) {
            if (head.key.equals(key)) {
                head.value = value; // Update existing
                return;
            }
            head = head.next;
        }

        // Insert new entry at head of chain (O(1))
        Entry<K, V> newEntry = new Entry<>(key, value);
        newEntry.next = buckets[bucketIndex];
        buckets[bucketIndex] = newEntry;
    }

    public V get(K key) {
        if (key == null)
            return null;

        int bucketIndex = getBucketIndex(key);
        Entry<K, V> head = buckets[bucketIndex];

        while (head != null) {
            if (head.key.equals(key)) {
                return head.value;
            }
            head = head.next;
        }
        return null;
    }

    private int getBucketIndex(K key) {
        return Math.abs(key.hashCode()) % CAPACITY;
    }
}
