# Java Interview Preparation Project

## Overview
This repository contains a set of **Java 17** modules designed to demonstrate core competencies required for a Senior Java Developer role. It focuses on **Clean Architecture**, **Concurrency**, **Object-Oriented Design**, and **Testability**. The code serves as both a reference implementation for common interview tasks and a study guide for technical discussions.

## Project Structure
The codebase is a **Maven Multi-Module Project** organized as follows:

```
javainterviewprep/
├── pom.xml             # Root Parent POM
├── portfolio/          # Module: Stock & Portfolio Management
│   ├── pom.xml
│   └── src/main/java/com/interview/portfolio/...
├── event/              # Module: Event Dispatching System
│   ├── pom.xml
│   └── src/main/java/com/interview/event/...
├── regtech/            # Module: Validation Rules Engine
│   ├── pom.xml
│   └── src/main/java/com/interview/regtech/...
└── structures/         # Module: Data Structures (Stack, Deque, Queue)
    ├── pom.xml
    └── src/main/java/com/interview/structures/...
```

### Installation & Build
To build the entire project including all modules, run from the root:
```sh
mvn clean install
```

## Key Modules

### 1. Portfolio & Stocks (`com.interview.portfolio`)
Demonstrates core OO modeling, thread-safe state management, and modern language features.
- **Key Feature**: `GenericRepository<T extends Asset>` demonstrates **Bounded Generics** and Type Safety.
- **Concepts**: **Reflection** (Runtime inspection), **Inheritance** (`Asset -> Stock -> RestrictedStock`), and **Polymorphism**.
- **Modern Java**: Uses **Java 17 Records** and Sealed-like hierarchies.

### 2. Event System (`com.interview.event`)
A generic, thread-safe Event Bus implementation.
- **Key Feature**: Supports both **synchronous** and **asynchronous** dispatching via `ExecutorService`.
- **Concurrency**: Uses `CopyOnWriteArrayList` for listener storage to allow safe iteration during updates.

### 3. RegTech Validation Engine (`com.interview.regtech`)
A flexible rules engine for validating financial transactions.
- **Key Feature**: Implements the **Strategy Pattern** to define validation logic.
- **Design Principle**: Adheres to **Open/Closed Principle**—new rules can be added without modifying the engine.

### 4. Data Structures (`com.interview.structures`)
Focuses on low-level data structure implementations to demonstrate "Under the Hood" understanding.
- **Core Collections**: `DynamicArray` (Resizing), `CustomHashMap` (Chaining), and `MyDeque` (Linked List).
- **Concurrency**: `SimpleBlockingQueue` demonstrating `ReentrantLock` and `Conditions`.
- **Algorithms**: **LRUCache** (LinkedHashMap), **O(1) MinStack**, and **MinHeap** (PriorityQueue).

---

## Senior Java Developer / Engineer Concepts
This project implements several advanced concepts discussed in interviews:

### 1. Concurrency & Multithreading
- **Lock Striping**: Used in `Portfolio`'s `ConcurrentHashMap`. Unlike `Hashtable` which locks the whole object, `ConcurrentHashMap` locks only specific buckets (segments), allowing multiple threads to write to different parts of the map simultaneously.
- **Snapshot Iteration**: Used in `EventDispatcher` via `CopyOnWriteArrayList`. This ensures that when we iterate through listeners to dispatch an event, we are looking at a stable "snapshot" of the list. Even if another thread adds a listener during dispatch, it won't affect the current loop and won't throw `ConcurrentModificationException`.
- **Happens-Before Relationship**: `ExecutorService` in `EventDispatcher` establishes a happens-before relationship between the submission of a task (event) and its execution, ensuring visibility of data across threads.

### 2. Algorithmic Complexity
- **Amortized Analysis**: Demonstrated in `QueueUsingStack` (O(1) amortized pop) and `DynamicArray` (O(1) amortized add).
- **Space-Time Tradeoff**: Demonstrated in `MinStack` (Auxiliary stack for O(1) min) and `LRUCache` (Map + List for O(1) access).
- **Hashing Internals**: `CustomHashMap` demonstrates collision resolution (Chaining) and bucket indexing `(hash % capacity)`.

### 3. Design Patterns
- **Strategy Pattern**: In `RegTech`, the `Rule<T>` interface is the strategy. The `RegTechEngine` accepts any "strategy" (Rule) to validate the context. This decouples the *what* (validation logic) from the *how* (engine execution).
- **Observer Pattern**: The `EventDispatcher` is the subject and `EventListener`s are observers. Key for decoupling components—the code firing an event doesn't need to know who is listening.
- **Immutable Objects**: `Stock` and `TransferContext` are implemented as **Java Records**. Immutable objects are inherently thread-safe because their state cannot change after creation, eliminating synchronization needs for read-only data.

---

## Interview Questions & Answers (Code Specific)

### Q1: In the `EventDispatcher`, why use `CopyOnWriteArrayList` instead of a wrapper like `Collections.synchronizedList`?
**Refers to**: `com.interview.event.EventDispatcher`
**Answer**: `Collections.synchronizedList` makes individual operations atomic, but *iteration* requires manual locking. If we used it in `dispatch()`, we would have to lock the entire list while looping through listeners. This blocks any thread trying to register a new listener until dispatch finishes (performance bottleneck).
`CopyOnWriteArrayList`, used in the example, allows lock-free iteration. Iterators work on a snapshot of the array, so dispatching is fast and non-blocking, which is ideal for event systems where reading (dispatching) happens much more often than writing (registering).

### Q2: How does the `RegTechEngine` demonstrate the Open/Closed Principle?
**Refers to**: `com.interview.regtech.RegTechEngine`
**Answer**: The **Open/Closed Principle** states code should be open for extension but closed for modification.
In the example, if business requirements change and we need to "Block transfers for restricted stocks", we do **not** modify `RegTechEngine.java`. Instead, we create a new class `RestrictedStockRule implements Rule`, and add it to the engine setup. The engine code remains untouched, reducing the risk of regression bugs in core logic.

### Q3: Why is `Stock` defined as a `record`? What does this imply for HashMaps?
**Refers to**: `com.interview.portfolio.Stock`
**Answer**: `Stock` is a **Java Record**, which makes it immutable and automatically generates `equals()` and `hashCode()` based on all fields.
This is critical for `HashMap` (or `ConcurrentHashMap`) keys. If `Stock` were used as a key and was mutable, changing a field (like `price`) would change its `hashCode`. The map would then look in the wrong bucket for the object, effectively "losing" it. The immutability of Records guarantees the hash code is stable, making them perfect candidates for Map keys or Set elements.

### Q4: The `Portfolio` class has a map. Why doesn't it just extend `ConcurrentHashMap`?
**Refers to**: `com.interview.portfolio.Portfolio`
**Answer**: This is **Composition over Inheritance**.
If `Portfolio` extended `ConcurrentHashMap`, it would expose *all* map methods (clear, remove, put) to the outside world, allowing any caller to corrupt the internal state (e.g., removing a stock without checking logic).
By *containing* a map (`private final Map holdings`), the `Portfolio` class encapsulates the state. It only exposes controlled methods (`addStock`, `removeStock`) that enforce business invariants (like checking for negative quantities), which is a key principle of OO encapsulation.

### Q5: How does `ExecutorService` in `EventDispatcher` help with system stability?
**Refers to**: `com.interview.event.EventDispatcher`
**Answer**: If we created a `new Thread()` for every event, a surge in events could exhaust system memory limits (Out of Memory Error).
By using an `ExecutorService` (in the constructor), we can pass in a bounded pool (e.g., `Executors.newFixedThreadPool(10)`). This limits the maximum concurrency to 10 active threads. Excess tasks queue up rather than crashing the JVM, providing **Backpressure** management and predictable resource usage.

### Q6: Why avoid `java.util.Stack`?
**Refers to**: `com.interview.structures.CustomStack`
**Answer**: `java.util.Stack` is a legacy class that extends `Vector`. This means every single method is `synchronized`, causing unnecessary thread-locking overhead in single-threaded applications.
**Modern Approach**: Use `Deque<T> stack = new ArrayDeque<>()`. It is faster, not synchronized, and provides a cleaner API (stack and queue methods).

---

## Broader Conceptual Interview Questions (JVM & General)

### Q6: Explain the Java Memory Model (Stack vs Heap).
**Concept**: JVM Internals.
**Answer**:
- **Heap**: Shared memory area where *all Objects* are stored. It is garbage collected. In our project, the `Portfolio` instance and all `Stock` objects live here.
- **Stack**: Thread-local memory storage. Stores primitive local variables and method call frames. References to objects exist on the stack, but point to the Heap.
- **Visibility**: Changes in Heap memory (shared) might not be strictly visible to all threads instantly unless specific barriers (like `volatile`, `synchronized`, or `concurrent locks`) are used to enforce a "Happens-Before" relationship.

### Q7: How does Garbage Collection worked? (Generational Hypothesis)
**Concept**: Memory Management.
**Answer**: Java GC is based on the **Weak Generational Hypothesis**: "Most objects die young".
- **Young Generation (Eden + Survivor spaces)**: New objects are allocated here. Minor GC runs frequently here and is very fast (copying collectors).
- **Old Generation**: Objects that survive multiple Minor GCs are moved here. Major GC/Full GC runs here, which is slower.
- Modern GCs (G1, ZGC) optimize this to minimize "Stop-The-World" pauses. In our application, short-lived events (`TransferContext`) would be collected cheaply in Eden, while the long-lived `Portfolio` stays in Old Gen.

### Q8: What are the key features introduced in Java 17 (LTS)?
**Concept**: Java Ecosystem.
**Answer**:
- **Records (JEP 395)**: Immutable data carriers (used in our `Stock` class).
- **Sealed Classes (JEP 409)**: Restrict which other classes may extend or implement them, allowing for exhaustive pattern matching.
- **Pattern Matching for switch (Preview in 17, Standard later)**: Simplifies switch statements.
- **Strong Encapsulation of JDK Internals**: Prevents illegal access to internal APIs via reflection (Project Jigsaw completion).

### Q9: Fail-Fast vs Fail-Safe Iterators.
**Concept**: Concurrency and Collections.
**Answer**:
- **Fail-Fast**: Throws `ConcurrentModificationException` immediately if the collection is modified while iterating (e.g., `ArrayList`, `HashMap`). Uses an internal `modCount`.
- **Fail-Safe**: Works on a clone or snapshot of the collection, avoiding exceptions (e.g., `CopyOnWriteArrayList`, `ConcurrentHashMap`).

### Q10: Explanation of Java Reference Types.
**Concept**: Memory Management.
**Answer**:
- **Strong Reference**: Standard `Object o = new Object()`. Never collected while reachable.
- **Soft Reference**: Collected only if JVM is almost out of memory (Good for Caching).
- **Weak Reference**: Collected eagerly on next GC cycle (Used in `WeakHashMap`).
- **Phantom Reference**: Used for post-mortem cleanup interactions.

### Q11: Method Area: PermGen vs Metaspace.
**Concept**: JVM Memory Structure.
**Answer**:
- **PermGen (Java 7 and older)**: Stored class metadata (static vars, method code). It was contiguous with the Heap and had a fixed size, often causing `java.lang.OutOfMemoryError: PermGen space`.
- **Metaspace (Java 8+)**: Replaced PermGen. It uses **Native Memory** (OS memory), not Heap. It grows automatically by default, significantly reducing OOM errors related to class loading.

### Q12: Basic GC Tuning Flags.
**Concept**: Performance Engineering.
**Answer**:
- **`-Xms` / `-Xmx`**: Initial and Max Heap size. Setting them equal prevents resizing overhead.
- **`-XX:+UseG1GC`**: Use G1 Garbage Collector (Standard for server apps > 4GB Heap).
- **`-XX:MaxGCPauseMillis=200`**: Sets a target for max pause time (soft goal).
- **`-XX:+HeapDumpOnOutOfMemoryError`**: Critical for production to capture state when a crash occurs.
