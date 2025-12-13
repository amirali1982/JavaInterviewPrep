# Event Listening System Module

## Overview
This module demonstrates a flexible, type-safe Event Dispatching system using the **Observer Pattern**, written in **Java 17**. It supports both synchronous and asynchronous event processing, showcasing advanced Java concurrency usage.

## Key Features
1.  **Generics**: The `EventListener<T>` interface uses Java Generics to ensure type safety. Listeners only receive events they are interested in.
2.  **Concurrency / Thread Safety**:
    *   `ConcurrentHashMap`: Used for the listener registry to allow concurrent registration/lookup.
    *   `CopyOnWriteArrayList`: Used for the list of listeners.
3.  **Asynchronous Processing**: The `EventDispatcher` can optionally use an `ExecutorService` to offload event processing to a thread pool.

## Implementation Details
-   **Java 17**: Uses modern syntax and library features.
-   **`CopyOnWriteArrayList`**: A specialized list where all mutative operations (add, set, remove) implement a fresh copy of the underlying array.

## Implementation-Specific Interview Questions

### 1. In `EventDispatcher`, you used `CopyOnWriteArrayList`. what is the trade-off?
**Refers to**: `com.interview.event.EventDispatcher.listeners`
**Answer**:
- **Benefit**: It allows traversing the list (dispatching events) without locking, which is extremely fast and prevents `ConcurrentModificationException`.
- **Cost**: **Memory & Write Performance**. Every time a listener is added or removed, the *entire* array is copied.
- **Relevance**: This is the perfect choice for an Event System where **events are fired frequently (Thousands/sec)** but **listeners are registered rarely (Startup time)**. If listeners were added/removed constantly at runtime, this would be a poor choice due to memory churn.

### 2. How does `ExecutorService` change the exception handling behavior?
**Refers to**: `com.interview.event.EventDispatcher.dispatch()`
**Answer**:
- **Synchronous (Direct call)**: If a listener throws an unchecked Exception, it propagates up the stack and could crash the dispatcher loop (stopping other listeners).
- **Asynchronous (`executor.submit`)**: The Exception is captured within the `Future` returned by submit (which we are ignoring here) or handled by the Thread Pool's `UncaughtExceptionHandler`. It isolates the failure; one broken listener won't stop others from executing their separate tasks.

### 3. Can you explain the `<? extends Event>` wildcard in the Map definition?
**Refers to**: `private final Map<Class<? extends Event>, List<EventListener<? extends Event>>> listeners`
**Answer**: This relies on **Upper-Bounded Wildcards**.
- It guarantees that keys are specifically subclasses of `Event` (not just any Object).
- It allows the values to be Lists of Listeners that handle *some subtype* of Event.
- This provides type safety at compile time, ensuring we don't accidentally register a String listener into an Event map.

### 4. What happens if two threads call `register()` for the same event type at the same time?
**Refers to**: `listeners.computeIfAbsent(...)`
**Answer**: We use `ConcurrentHashMap.computeIfAbsent()`. This method is **atomic**. Use of this method ensures that if multiple threads try to initialize the list for a new Event type simultaneously, only *one* thread will create the new `CopyOnWriteArrayList` and put it in the map. The other thread will get the existing list. This prevents "lost updates" race conditions.

---

## Broader Conceptual Interview Questions (Concurrency)

### 5. `synchronized` vs `ReentrantLock` — When to use which?
**Concept**: Advanced Locking.
**Answer**:
- **`synchronized`**: Native keyword. Simpler, automatically releases lock on exception/return. Preferred for most cases due to readability and compiler optimizations.
- **`ReentrantLock`**: A class. Provides advanced features:
- **Fairness**: Can ensure FIFO locking order (prevent starvation).
- **`tryLock()`**: Can attempt to get a lock and give up (or wait with timeout), preventing deadlocks.
- **Unpaired locking**: Can lock in one method and unlock in another (risky but flexible).

### 6. What is the `volatile` keyword?
**Concept**: Memory Visibility.
**Answer**: `volatile` guarantees **Visibility** but not **Atomicity**.
- When a field is `volatile`, any write to it is immediately flushed to main memory, and any read is fetched from main memory (bypassing CPU caches).
- **Use Case**: Flags (e.g., `volatile boolean running = true`).
- **Limitation**: `volatile int count++` is **NOT** thread-safe because `++` is three steps (Read-Modify-Write). You need `AtomicInteger` for that.

### 7. `Runnable` vs `Callable`?
**Concept**: Threading.
**Answer**:
- **`Runnable`**: Functional interface `void run()`. Does not return a result and cannot throw checked exceptions. Used with `new Thread(runnable)`.
- **`Callable<T>`**: Functional interface `T call()`. Returns a result and can throw Exception. Used with `ExecutorService.submit(callable)` which returns a `Future<T>`.

### 8. What is a Daemon Thread?
**Concept**: JVM Threads.
**Answer**: A thread that runs in the background and **does not prevent the JVM from exiting**.
- **Example**: Garbage Collector, heartbeat monitors.
- **Behavior**: If only Daemon threads are left running, the JVM shuts down abruptly (effectively killing them).
- **Code**: `thread.setDaemon(true)` before starting it.

### 9. Compare `CachedThreadPool` vs `FixedThreadPool`.
**Refers to**: `EventDispatcher` constructor logic.
**Answer**:
- **FixedThreadPool(n)**: Reuses `n` threads. Good for predictable load.
- **CachedThreadPool**: Creates new threads as needed, reuses idle ones. Good for many short-lived tasks but risky (can spawn infinite threads).
- **ScheduledThreadPool**: For delayed or periodic tasks.
- **CompletableFuture**: Modern, functional way to handle async tasks (Promises) compared to raw `Future`.

### 10. Explain the PECS Principle (Producer Extends, Consumer Super).
**Concept**: Generics.
**Answer**:
- **Producer Extends**: If you want to read T modifiers from a collection, use `<? extends T>`. Example: `List<? extends Number>`. You can get Numbers out, but cannot put them in (because it might be a List of Integers).
- **Consumer Super**: If you want to put T items into a collection, use `<? super T>`.
- **In our Code**: `EventListener<? extends Event>` means the listener consumes specific subtypes of Event.
