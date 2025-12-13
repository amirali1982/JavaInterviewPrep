package com.interview.structures.concurrency;

import org.junit.jupiter.api.Test;
import java.util.concurrent.ExecutionException;
import static org.junit.jupiter.api.Assertions.*;

class ConcurrencyTests {

    @Test
    void testCompletableFuture() throws ExecutionException, InterruptedException {
        CompletableFutureDemo demo = new CompletableFutureDemo();

        assertEquals("Processed Data & Saved", demo.chainTasks());
        assertEquals("Default Value", demo.handleException());
        assertEquals("Price: 100 + Tax: 10", demo.combineTasks());
    }

    // Note: We do not typically test 'triggerDeadlock' because it hangs the test
    // suite.
    // Ideally, we would run 'safeExecution' and assert it finishes.
    @Test
    void testSafeDeadlockAvoidance() {
        DeadlockDemo demo = new DeadlockDemo();
        // This should complete without hanging
        demo.safeExecution();
    }
}
