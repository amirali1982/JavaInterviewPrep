package com.interview.event;

/**
 * Functional interface for event listeners.
 * 
 * @param <T> The type of event to listen for.
 */
@FunctionalInterface
public interface EventListener<T extends Event> {
    void onEvent(T event);
}
