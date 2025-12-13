package com.interview.event;

import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * EventDispatcher implementation (The "Megaphone").
 * Pattern: Observer.
 * Topology: One-to-Many.
 * 
 * Philosophy: "I (the specific component) am telling my specific listeners that
 * something happened to me."
 * 
 * Usage:
 * This class is designed to be a member of a specific component (e.g., Button,
 * Downloader).
 * Listeners subscribe to this specific dispatcher instance.
 * 
 * @param <E> The type of event this dispatcher handles.
 */
public class EventDispatcher<E extends Event> {

    // Thread-safe list of listeners
    private final List<EventListener<E>> listeners = new CopyOnWriteArrayList<>();

    /**
     * Registers a listener to this specific dispatcher.
     */
    public void addListener(EventListener<E> listener) {
        listeners.add(listener);
    }

    /**
     * Removes a listener.
     */
    public void removeListener(EventListener<E> listener) {
        listeners.remove(listener);
    }

    /**
     * Dispatches the event to all listeners registered with THIS dispatcher.
     */
    public void dispatch(E event) {
        for (EventListener<E> listener : listeners) {
            listener.onEvent(event);
        }
    }
}
