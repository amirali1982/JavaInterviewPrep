# Portfolio and Stocks Module

## Overview
This module demonstrates a clean, thread-safe implementation of a simple Portfolio management system. It highlights modern Java features and solid object-oriented design principles suitable for coding interviews.

## Key Features
1.  **Immutability**: The `Stock` entity is implemented as a Java `record`, ensuring data immutability and reducing boilerplate code (equals, hashCode, toString).
2.  **Thread Safety**: The `Portfolio` class uses `ConcurrentHashMap` to manage holdings, allowing safe concurrent updates (e.g., buying/selling stocks simultaneously) without explicit `synchronized` blocks for read/write.
3.  **Abstraction**: Core logic is separated into `StockRepository` (data access) and `PortfolioService` (business logic), enabling easy mocking and testing.
4.  **Functional Programming**: The `MapBasedStockRepository` utilizes Java Streams for efficient filtering and categorization of stocks by sector.
5.  **Defensive Coding**: Validations (`Objects.requireNonNull`, non-negative checks) prevent invalid state.

## Implementation Details
-   **Stock**: A `record` representing the immutable state of a stock.
-   **Portfolio**: Manages the quantity of stocks owned. Uses `merge()` and `compute()` for atomic updates to the concurrent map.
-   **PortfolioService**: Business logic layer that orchestrates data retrieval and value calculation.
-   **MapBasedStockRepository**: An in-memory implementation of the repository pattern, demonstrating `Stream` filtering.

## Interview Questions & Answers

### 1. Why use a `record` for Stock instead of a standard `class`?
**Answer**: Records are ideal for data carriers that are immutable. They automatically provide `equals()`, `hashCode()`, `toString()`, and constructor logic, reducing boilerplate. This signals to the interviewer that you are up-to-date with modern Java (Java 14+).

### 2. Why did you use `ConcurrentHashMap` in `Portfolio`?
**Answer**: `Portfolio` might be accessed by multiple threads (e.g., automated trading updates). `ConcurrentHashMap` provides better performance than `Collections.synchronizedMap` or `Hashtable` because it uses lock stripping (segment locking) rather than locking the entire map for every operation.

### 3. How does `holdings.merge()` work?
**Answer**: `merge()` atomically updates the value for a key. If the key doesn't exist, it puts the value. If it does, it applies the remapping function (e.g., `Integer::sum`). This avoids race conditions where two threads read the same old value and write back, overwriting each other's changes.

### 4. How would you handle a Database in a real scenario?
**Answer**: I would create a `JpaStockRepository` or `JdbcStockRepository` implementing the `StockRepository` interface. The `PortfolioService` would remain unchanged, demonstrating the value of the dependency injection and interface abstraction.

### 5. Why use `Optional` in `findBySymbol`?
**Answer**: It forces the caller to explicitly handle the case where a value might be missing, avoiding `NullPointerException`. It makes the API contract clear: "this method might return nothing".
