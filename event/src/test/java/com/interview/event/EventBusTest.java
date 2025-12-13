package com.interview.event;

import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

import static org.junit.jupiter.api.Assertions.assertTrue;

class EventBusTest {

    static class UserCreatedEvent implements Event {
        final String username;

        UserCreatedEvent(String username) {
            this.username = username;
        }
    }

    static class OrderPlacedEvent implements Event {
    }

    @Test
    void testSynchronousDispatch() {
        EventBus eventBus = new EventBus();
        AtomicBoolean handled = new AtomicBoolean(false);

        eventBus.register(UserCreatedEvent.class, event -> {
            if ("alice".equals(event.username)) {
                handled.set(true);
            }
        });

        eventBus.dispatch(new UserCreatedEvent("alice"));

        assertTrue(handled.get(), "Listener should have been called synchronously");
    }

    @Test
    void testAsyncDispatch() throws InterruptedException {
        ExecutorService executor = Executors.newFixedThreadPool(2);
        EventBus eventBus = new EventBus(executor);
        CountDownLatch latch = new CountDownLatch(1);
        AtomicBoolean handled = new AtomicBoolean(false);

        eventBus.register(OrderPlacedEvent.class, event -> {
            try {
                // Simulate work
                Thread.sleep(50);
                handled.set(true);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } finally {
                latch.countDown();
            }
        });

        eventBus.dispatch(new OrderPlacedEvent());

        // Wait for async execution
        boolean success = latch.await(1, TimeUnit.SECONDS);
        assertTrue(success, "Latch should have counted down");
        assertTrue(handled.get(), "Listener should have been called");

        eventBus.shutdown();
    }
}
