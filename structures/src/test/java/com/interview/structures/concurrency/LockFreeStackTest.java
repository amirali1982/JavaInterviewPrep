package com.interview.structures.concurrency;

import org.junit.jupiter.api.Test;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.*;

class LockFreeStackTest {

    @Test
    void testConcurrentPushPop() throws InterruptedException {
        LockFreeStack<Integer> stack = new LockFreeStack<>();
        int threadCount = 10;
        int operationsPerThread = 1000;

        ExecutorService executor = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        // Concurrent Pushing
        for (int i = 0; i < threadCount; i++) {
            executor.submit(() -> {
                for (int j = 0; j < operationsPerThread; j++) {
                    stack.push(1);
                }
                latch.countDown();
            });
        }

        latch.await();

        // Use a single thread to pop all and count
        int count = 0;
        while (stack.pop() != null) {
            count++;
        }

        // Verify no data lost
        assertEquals(threadCount * operationsPerThread, count);
        executor.shutdown();
    }
}
