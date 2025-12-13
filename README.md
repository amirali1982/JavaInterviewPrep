# Java Interview Preparation Project

## Overview
This repository contains a set of Java modules designed to demonstrate core competencies required for a Senior Java Developer role. It focuses on **Clean Architecture**, **Concurrency**, **Object-Oriented Design**, and **Testability**. The code serves as both a reference implementation for common interview tasks and a study guide for technical discussions.

## Project Structure
The codebase is organized into domain-specific packages under `src/main/java/com/interview`:

```
src/main/java/com/interview/
├── event/          # Event Dispatcher System (Observer Pattern, Concurrency)
│   ├── Event.java
│   ├── EventDispatcher.java
│   └── EventListener.java
├── portfolio/      # Portfolio Management (Thread usage, Collections)
│   ├── Portfolio.java
│   ├── Stock.java (Record)
│   └── PortfolioService.java
└── regtech/        # Validation Rules Engine (Strategy Pattern, SOLID)
    ├── RegTechEngine.java
    ├── Rule.java
    └── rules/
```

## Key Modules

### 1. Portfolio & Stocks (`com.interview.portfolio`)
Demonstrates core OO modeling and thread-safe state management.
- **Key Feature**: `Portfolio` uses `ConcurrentHashMap` to handle concurrent additions/removals of stock.
- **Modern Java**: Uses **Java Records** (`Stock`) for immutable data carriers.

### 2. Event System (`com.interview.event`)
A generic, thread-safe Event Bus implementation.
- **Key Feature**: Supports both **synchronous** and **asynchronous** dispatching via `ExecutorService`.
- **Concurrency**: Uses `CopyOnWriteArrayList` for listener storage to allow safe iteration during updates.

### 3. RegTech Validation Engine (`com.interview.regtech`)
A flexible rules engine for validating financial transactions.
- **Key Feature**: Implements the **Strategy Pattern** to define validation logic.
- **Design Principle**: Adheres to **Open/Closed Principle**—new rules can be added without modifying the engine.

---

## Senior Java Developer Concepts
This project implements several advanced concepts discussed in interviews:

### 1. Concurrency & Multithreading
- **`ConcurrentHashMap`**: Used in `Portfolio` and `EventDispatcher` for thread-safe map operations without locking the entire map (unlike `Hashtable` or `Collections.synchronizedMap`).
- **`CopyOnWriteArrayList`**: Used for `listeners` list. Ideal for "read-mostly" scenarios where traversal happens frequently (dispatching) but modification (registering) is rare. It prevents `ConcurrentModificationException`.
- **`ExecutorService`**: Decouples task submission from execution, allowing async event handling.

### 2. Design Patterns
- **Strategy Pattern**: The `Rule<T>` interface allows swapping validation logic at runtime.
- **Observer Pattern**: The `EventDispatcher` allows components to react to events without tight coupling.
- **Immutable Objects**: `Stock` and `TransferContext` are `Records`. Immutability is crucial for multithreaded safety and hash key consistency.

### 3. Clean Architecture
- **Dependency Inversion**: High-level modules (`RegTechEngine`) depend on abstractions (`Rule`, `PortfolioRepository`) rather than concrete details.
- **Interface Segregation**: Small, focused interfaces like `MarketProvider`.

---

## Interview Questions & Answers

### Q1: Why use `ConcurrentHashMap` over `Hashtable` or `synchronizedMap`?
**Answer**: `Hashtable` and `synchronizedMap` lock the entire map for every operation, causing contention in high-concurrency scenarios. `ConcurrentHashMap` uses **lock striping** (bucket-level locking) and CAS (Compare-And-Swap) operations, allowing multiple threads to read and write concurrently with high throughput.

### Q2: Explain the Open/Closed Principle with an example from this project.
**Answer**: The **Open/Closed Principle** states that software entities should be open for extension but closed for modification. In the `RegTech` module, the `RegTechEngine` is "closed" because its logic doesn't change. However, it is "open" for extension because we can add new validation requirements (e.g., `RestrictedStockRule`) simply by creating a new class implementing `Rule` and registering it, without touching the engine code.

### Q3: When should you use Java Records?
**Answer**: Records (introduced in Java 14) are ideal for **immutable data carriers** (DTOs, Value Objects). They automatically provide `equals()`, `hashCode()`, `toString()`, and getters. They should be used when a class is primarily just data, like `Stock` or `TransferContext` in this project. They are *not* suitable if you need mutable state or inheritance (records cannot extend other classes).

### Q4: How does `CopyOnWriteArrayList` ensure thread safety during iteration?
**Answer**: When the list is modified (add/remove), it creates a fresh copy of the underlying array, modifies the copy, and then atomically replaces the original array reference. Iterators created before the modification continue pointing to the *old* array snapshot. This eliminates `ConcurrentModificationException` without explicit locking during iteration, making it perfect for Event Listeners but inefficient for lists with frequent writes.

### Q5: How would you test a class that depends on an external service (like Market Status)?
**Answer**: We use **Dependency Injection** and **Mocking**. Instead of instantiating the external service directly (`new MarketService()`), we inject an interface (`MarketProvider`) into the constructor. In unit tests (e.g., `MarketIsOpenRuleTest`), we use a library like **Mockito** to create a mock of `MarketProvider` and define its behavior (`when(provider.isMarketOpen()).thenReturn(true)`), allowing us to test our logic in isolation without relying on the real external system.
