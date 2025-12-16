package com.interview.event;

import java.util.concurrent.Flow;
import java.util.concurrent.SubmissionPublisher;

/**
 * EventBus implementation using Java 9 Flow API (Reactive Streams).
 * Demonstrates standard JDK 9+ reactive patterns with Backpressure support.
 */
public class ReactiveEventBus implements AutoCloseable {

    // SubmissionPublisher is a standard JDK class that implements Flow.Publisher
    private final SubmissionPublisher<Event> publisher;

    public ReactiveEventBus() {
        // Use common ForkJoinPool or custom executor
        this.publisher = new SubmissionPublisher<>();
    }

    public void publish(Event event) {
        // offer() is non-blocking. submit() blocks if buffer is full.
        // We use submit for guaranteed delivery in this demo.
        publisher.submit(event);
    }

    public void subscribe(Flow.Subscriber<? super Event> subscriber) {
        publisher.subscribe(subscriber);
    }

    @Override
    public void close() {
        publisher.close();
    }

    // Helper to create a simple subscriber that prints events
    public static class SimpleSubscriber implements Flow.Subscriber<Event> {
        private Flow.Subscription subscription;
        private final String name;

        public SimpleSubscriber(String name) {
            this.name = name;
        }

        @Override
        public void onSubscribe(Flow.Subscription subscription) {
            this.subscription = subscription;
            System.out.println(name + " subscribed.");
            // Request 1 item to start
            subscription.request(1);
        }

        @Override
        public void onNext(Event item) {
            System.out.println(name + " received: " + item);
            // Request next item (backpressure control)
            subscription.request(1);
        }

        @Override
        public void onError(Throwable throwable) {
            throwable.printStackTrace();
        }

        @Override
        public void onComplete() {
            System.out.println(name + " completed.");
        }
    }
}
