package com.interview.event;

import org.junit.jupiter.api.Test;
import java.util.concurrent.Flow;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

class ReactiveEventBusTest {

    static class TestEvent implements Event {
        private final String message;

        TestEvent(String message) {
            this.message = message;
        }

        @Override
        public String toString() {
            return message;
        }
    }

    @Test
    void testReactiveFlow() throws InterruptedException {
        // Latch to wait for async processing
        CountDownLatch latch = new CountDownLatch(3);
        AtomicInteger receivedCount = new AtomicInteger(0);

        try (ReactiveEventBus bus = new ReactiveEventBus()) {

            bus.subscribe(new Flow.Subscriber<Event>() {
                private Flow.Subscription sub;

                @Override
                public void onSubscribe(Flow.Subscription subscription) {
                    this.sub = subscription;
                    sub.request(1);
                }

                @Override
                public void onNext(Event item) {
                    receivedCount.incrementAndGet();
                    latch.countDown();
                    sub.request(1);
                }

                @Override
                public void onError(Throwable throwable) {
                }

                @Override
                public void onComplete() {
                }
            });

            bus.publish(new TestEvent("E1"));
            bus.publish(new TestEvent("E2"));
            bus.publish(new TestEvent("E3"));

            // Wait for subscribers
            assertTrue(latch.await(5, TimeUnit.SECONDS), "Timed out waiting for events");
            assertEquals(3, receivedCount.get());
        }
    }
}
