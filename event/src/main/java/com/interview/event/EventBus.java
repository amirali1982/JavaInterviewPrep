package com.interview.event;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.ExecutorService;

/**
 * EventBus implementation (formerly EventDispatcher).
 * Pub-Sub pattern where listeners allow filtering by event type.
 * Supports synchronous and asynchronous event dispatching.
 * Thread-safe: Uses ConcurrentHashMap and CopyOnWriteArrayList.
 */
public class EventBus {

    // Map event class type to a list of listeners.
    // Uses CopyOnWriteArrayList to allow safe iteration while modification happens
    // (add/remove listeners).
    private final Map<Class<? extends Event>, List<EventListener<? extends Event>>> listeners = new ConcurrentHashMap<>();

    private final ExecutorService executor;

    /**
     * Creates a synchronous event bus.
     */
    public EventBus() {
        this.executor = null;
    }

    /**
     * Creates an asynchronous event bus using the provided executor.
     */
    public EventBus(ExecutorService executor) {
        this.executor = executor;
    }

    /**
     * Registers a listener for a specific event type.
     */
    public <T extends Event> void register(Class<T> eventType, EventListener<T> listener) {
        listeners.computeIfAbsent(eventType, k -> new CopyOnWriteArrayList<>()).add(listener);
    }

    /**
     * Dispatches an event to all registered listeners for that specific event type.
     */
    @SuppressWarnings("unchecked")
    public <T extends Event> void dispatch(T event) {
        List<EventListener<? extends Event>> eventListeners = listeners.get(event.getClass());
        if (eventListeners != null) {
            for (EventListener<? extends Event> listener : eventListeners) {
                // Type safety is ensured by the register method's signature,
                // but we cast here because the map holds generic wildcards.
                EventListener<T> typedListener = (EventListener<T>) listener;

                if (executor != null) {
                    executor.submit(() -> typedListener.onEvent(event));
                } else {
                    typedListener.onEvent(event);
                }
            }
        }
    }

    public void shutdown() {
        if (executor != null) {
            executor.shutdown();
        }
    }
}
