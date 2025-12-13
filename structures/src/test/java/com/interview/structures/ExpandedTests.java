package com.interview.structures;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.concurrent.atomic.AtomicInteger;

class ExpandedTests {

    @Test
    void testDynamicArray() {
        DynamicArray<Integer> list = new DynamicArray<>();
        for (int i = 0; i < 20; i++) {
            list.add(i);
        }
        assertEquals(20, list.size());
        assertEquals(0, list.get(0));
        assertEquals(19, list.get(19));
    }

    @Test
    void testCustomHashMap() {
        CustomHashMap<String, String> map = new CustomHashMap<>();
        map.put("key1", "val1");
        map.put("key2", "val2");
        map.put("key1", "updated");

        assertEquals("updated", map.get("key1"));
        assertEquals("val2", map.get("key2"));
        assertNull(map.get("missing"));
    }

    @Test
    void testMinHeap() {
        MinHeap heap = new MinHeap();
        heap.add(10);
        heap.add(5);
        heap.add(20);
        heap.add(2);

        assertEquals(2, heap.poll());
        assertEquals(5, heap.poll());
        assertEquals(10, heap.poll());
        assertEquals(20, heap.poll());
    }

    @Test
    void testLRUCache() {
        LRUCache<Integer, String> cache = new LRUCache<>(2);
        cache.put(1, "A");
        cache.put(2, "B");
        assertEquals("A", cache.get(1)); // 1 is now MRU

        cache.put(3, "C"); // Should evict 2 (LRU)
        assertNull(cache.get(2));
        assertEquals("A", cache.get(1));
        assertEquals("C", cache.get(3));
    }

    @Test
    void testBlockingQueue() throws InterruptedException {
        SimpleBlockingQueue<Integer> queue = new SimpleBlockingQueue<>(2);
        AtomicInteger result = new AtomicInteger(0);

        Thread consumer = new Thread(() -> {
            try {
                result.set(queue.take());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        });

        consumer.start();
        Thread.sleep(100); // Ensure consumer is waiting
        queue.put(42);
        consumer.join(1000);

        assertEquals(42, result.get());
    }
}
