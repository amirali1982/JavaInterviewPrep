# Event Listening System Module

## Overview
This module demonstrates distinct event handling patterns in **Java 17**, evolving from classic Observer to Modern Reactive Streams:

1.  **EventBus** (Classic): Pub/Sub using standard listeners.
2.  **ReactiveEventBus** (Modern): **Java 9 Flow API** (`Publisher`, `Subscriber`) implementation.
3.  **EventDispatcher** (Targeted): Observer Pattern (One-to-Many).

## Key Concepts

### 1. ReactiveEventBus (Java 9 Flow API)
*   **Philosophy**: "I want to handle streams of events asynchronously with backpressure."
*   **Standard**: Implements `java.util.concurrent.Flow`.
*   **Mechanism**: Uses `SubmissionPublisher<T>` (JDK 9+ default publisher).
*   **Key Feature - Backpressure**: If the subscriber is too slow, the publisher can buffer, drop, or block, preventing system overload. Standard listeners just crash or OOM if overwhelmed.

### 2. EventBus ("Town Square")
*   **Philosophy**: "I don't care who is listening, and I don't care who is talking."
*   **Pattern**: Publish-Subscribe (Pub/Sub).
*   **Topology**: Many-to-Many.
*   **Mechanism**: A centralized hub where publishers throw events and subscribers pick them up by type. Completely decoupled.
*   **Use Case**: Global app events (e.g., `UserLoggedInEvent`, `StockSplitEvent`) where the sender doesn't need to know the receiver.

### 3. EventDispatcher ("Megaphone")
*   **Philosophy**: "I (the specific component) am telling my specific listeners that something happened to me."
*   **Pattern**: Observer Pattern.
*   **Topology**: One-to-Many.
*   **Mechanism**: attached to a specific source object (e.g., `button.onClick`, `downloader.onProgress`). Listeners subscribe to that specific instance. Tighter coupling.
*   **Use Case**: Component-specific events where context matters (e.g., UI interactions, specific background task progress).

### Visual Comparison

```mermaid
graph TD
    subgraph Reactive [Reactive Flow]
    P[Publisher] -->|onNext| S[Subscriber]
    S -.->|request(n)| P
    end

    subgraph PubSub [Legacy EventBus]
    P1[Publisher A] -->|Event X| EB(Event Bus)
    EB -->|Event X| S1[Subscriber 1]
    end
```

## Implementation Details

### ReactiveEventBus Features
-   **Standardization**: Uses `Flow.Publisher` and `Flow.Subscriber` interfaces introduced in Java 9.
-   **Async**: Native support for asynchronous processing.
-   **Backpressure**: Subscribers explicitly `request(n)` items, controlling the flow rate.

### EventBus Features
-   **Generics**: `Map<Class<? extends Event>, List<EventListener<?>>>` for type-safe dispatching.
-   **Concurrency**: `ConcurrentHashMap` and `CopyOnWriteArrayList` for thread safety.
-   **Async**: Optional `ExecutorService`.

### Advanced: Java NIO (Non-Blocking IO)
We implemented `NioEventLogger` to demonstrate high-performance file writing.
- **Components**: `FileChannel`, `ByteBuffer`.
- **Difference**: Standard IO (`FileWriter`) blocks the thread while writing. NIO Channels allow bulk transfers and can be used with `Selector` for non-blocking operations on a single thread.
- **Code Snippet**: `buffer.flip(); channel.write(buffer);`

## Implementation-Specific Interview Questions

### 1. What is Backpressure in Reactive Streams?
**Refers to**: `ReactiveEventBus`
**Answer**: Backpressure is the ability of a Subscriber to signal to the Publisher how much data it can handle (`subscription.request(n)`).
- Without backpressure: A fast publisher overwhelms a slow subscriber (OutOfMemory).
- With backpressure: The publisher slows down, buffers, or drops data to match the subscriber's speed.

### 2. In `EventBus`, you used `CopyOnWriteArrayList`. what is the trade-off?
**Answer**:
- **Benefit**: Lock-free traversal (fast reads/dispatch). No `ConcurrentModificationException`.
- **Cost**: Expensive writes (copy array on add/remove).
- **Relevance**: Ideal for Event systems where **reads (dispatching) >> writes (registering)**.

### 3. When would you use EventDispatcher over EventBus?
**Answer**:
- Use **EventDispatcher** when the event is tightly coupled to a specific *instance* of an object (e.g., "This specific file finished downloading").
- Use **EventBus** when the event is system-wide and the specific source doesn't matter (e.g., "Someone logged in").
- Use **ReactiveEventBus** when you have a high volume of events and need buffering/backpressure.

### 4. How does `ExecutorService` change exception handling in EventBus?
**Answer**:
- **Sync**: Exceptions propagate and can crash the loop.
- **Async**: Exceptions are captured in `Future`. Need explicit handling (logging/UncaughtExceptionHandler) to avoid silent failures.

### 5. Blocking vs Non-Blocking Dispatch?
**Answer**:
- **Blocking (Synchronous)**: The caller waits for all listeners to finish. Simple, consistent, but slow listeners delay the system.
- **Non-Blocking (Asynchronous)**: The caller fires and forgets (`ExecutorService.submit()`). Fast, but error handling is harder (need `Future.get()` or callbacks).
- **Modern**: Use `CompletableFuture` to chain async actions without blocking main threads.
