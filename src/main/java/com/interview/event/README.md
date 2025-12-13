# Event Listening System Module

## Overview
This module demonstrates a flexible, type-safe Event Dispatching system using the Observer pattern. It supports both synchronous and asynchronous event processing, showcasing advanced Java concurrency usage.

## Key Features
1.  **Generics**: The `EventListener<T>` interface uses Java Generics to ensure type safety. Listeners only receive events they are interested in, eliminating the need for `instanceof` checks and casting in the client code.
2.  **Concurrency / Thread Safety**:
    *   `ConcurrentHashMap`: Used for the listener registry to allow concurrent registration/lookup.
    *   `CopyOnWriteArrayList`: Used for the list of listeners for a specific event. This is crucial because it allows iterating over listeners (during dispatch) while other threads might be adding/removing listeners, preventing `ConcurrentModificationException` without explicit locking.
3.  **Asynchronous Processing**: The `EventDispatcher` can optionally use an `ExecutorService` (Strategy Pattern) to offload event processing to a thread pool, decoupling event emission from execution.

## Implementation Details
-   **Event**: A marker interface to identify event objects.
-   **EventListener**: A functional interface making it easy to use with Lambdas.
-   **EventDispatcher**: The core engine. It manages a map of `Class<? extends Event>` to `List<EventListener>`.

## Interview Questions & Answers

### 1. Why use `CopyOnWriteArrayList` for the listeners list?
**Answer**: `CopyOnWriteArrayList` is thread-safe and efficient for scenarios where **reads (event dispatching) vastly outnumber writes (listener registration)**. When a listener is added, it creates a new copy of the underlying array, ensuring that any iterator currently traversing the list (e.g., during a dispatch) sees a stable snapshot and doesn't throw `ConcurrentModificationException`.

### 2. Can you explain the `<? extends Event>` wildcard in the Map?
**Answer**: The Map is defined as `Map<Class<? extends Event>, List<EventListener<? extends Event>>>`. This upper-bounded wildcard allows the map to hold keys that are subclasses of `Event` and values that are lists of listeners for those events. It provides flexibility while maintaining a connection to the `Event` hierarchy.

### 3. Usage of `ExecutorService` vs creating `new Thread()`?
**Answer**: Creating a new thread for every event is expensive (stack allocation, OS overhead). An `ExecutorService` (like a cached or fixed thread pool) reuses existing threads, providing better resource management and preventing the system from crashing under load (e.g., "Out of Memory" from too many threads).

### 4. How does the generic type safety work here?
**Answer**: Although the internal Map stores listeners with wildcards due to type erasure, the public API `register(Class<T> type, EventListener<T> listener)` enforces that the listener matches the event class at compile time. Inside `dispatch`, we perform a safe cast because we know the structure invariant holds true.
