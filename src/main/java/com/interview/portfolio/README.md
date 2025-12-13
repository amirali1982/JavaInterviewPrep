# Portfolio and Stocks Module

## Overview
This module demonstrates a clean, thread-safe implementation of a simple Portfolio management system using **Java 17**. It highlights modern Java features and solid object-oriented design principles suitable for coding interviews.

## Key Features
1.  **Immutability**: The `Stock` entity is implemented as a **Java 17 Record**, ensuring data immutability and reducing boilerplate code.
2.  **Thread Safety**: The `Portfolio` class uses `ConcurrentHashMap` to manage holdings, allowing safe concurrent updates.
3.  **Encapsulation**: The internal map is not exposed directly; only business methods (`addStock`, `removeStock`) are public.

## Implementation Concepts

### Java Records (`Stock`)
We chose `record Stock(...)` because stocks in this context are **Value Objects**. Their identity is defined by their data (Symbol, Price). Records are immutable by default, which is crucial when instances are shared across threads or used as keys.

### ConcurrentHashMap (`holdings`)
We use `holdings.merge()` and `holdings.compute()`. These are **atomic operations**. Even without `synchronized`, `merge` guarantees that the read-modify-write cycle (Read qty -> Add -> Write qty) happens atomically for that key.

## Interview Questions & Answers

### 1. In `Portfolio.java`, why is `getHoldings()` returning `Collections.unmodifiableMap`?
**Refers to**: `com.interview.portfolio.Portfolio.getHoldings()`
**Answer**: This is **Defensive Copying / Immutable View**.
If we returned the `ConcurrentHashMap` directly, a caller could do `portfolio.getHoldings().clear()`, wiping out the user's assets bypassing the `removeStock` validation logic. wrapping it in `unmodifiableMap` ensures encapsulation is preserved—the outside world can *look* but cannot *touch*.

### 2. Can `Stock` be a key in a HashMap? What if `Stock` was a mutable class?
**Refers to**: `com.interview.portfolio.Stock` (Record)
**Answer**: `Stock` is a **Record**, so it's a perfect key: it's immutable and implements `hashCode/equals`.
If it were a mutable class, and we utilized it as a key:
1. We put `Stock("AAPL", $100)` into the map. It goes into Bucket A.
2. We change the price to `$200`. Its hashcode changes.
3. The map implementation now looks for it in Bucket B.
4. The entry is effectively lost (memory leak).
**Lesson**: Always use immutable objects for Map keys.

### 3. Explain strict encapsulation in the `Portfolio` class.
**Refers to**: `com.interview.portfolio.Portfolio`
**Answer**: The `Portfolio` class follows the "Tell, Don't Ask" principle. We don't ask for the map and manipulate it. We tell the portfolio to `addStock`.
This allows `Portfolio` to enforce **Invariants**: "Quantity must be positive", "Cannot sell more than you own". If we exposed the raw list/map, we couldn't guarantee these rules are followed.

### 4. How does `Stream` API in `MapBasedStockRepository` compare to a `for` loop?
**Refers to**: `com.interview.portfolio.PortfolioService` / Repository
**Answer**:
- **Declarative**: Streams say *what* we want (`filter(sector).collect()`) rather than *how* (indexes, temporary lists).
- **Parallizable**: We can easily switch to `parallelStream()` if the dataset is huge (though not always faster).
- **Readability**: For complex filtering/mapping chains, Streams are often more readable than nested loops.
