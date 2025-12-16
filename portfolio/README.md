# Portfolio and Stocks Module

## Overview
This module demonstrates a clean, thread-safe implementation of a simple Portfolio management system using **Java 17**. It highlights modern Java features (Sealed Classes, Records, CompletableFuture) and solid object-oriented design principles suitable for coding interviews.

## Key Features
1.  **Sealed Classes** (Java 17): `Asset` is a `sealed` class, strictly permitting only `Stock` and `Bond` subclasses. This enforces a known hierarchy.
2.  **Modern Async**: `AnalysisService` uses `CompletableFuture` for non-blocking, parallel market analysis.
3.  **Generics**: `GenericRepository<T extends Asset>` demonstrates usage of **Bounded Type Parameters** and ID-based registry logic.
4.  **Thread Safety**: The `Portfolio` class uses `ConcurrentHashMap` to manage holdings, allowing safe concurrent updates.
5.  **Encapsulation**: The internal map is not exposed directly; only business methods are public.

## Implementation Concepts

### Sealed Interface Hierarchy (Java 17)
We refactored the legacy abstract class to a **Sealed Class Hierarchy**:

```java
public abstract sealed class Asset permits Stock, Bond {}
public non-sealed class Stock extends Asset {}
public final class Bond extends Asset {}
```

- **Sealed (`Asset`)**: We control exactly who extends `Asset`. No random external classes can break our contract.
- **Non-Sealed (`Stock`)**: We explicitly allow extension (open) for `Stock` to support `RestrictedStock`.
- **Final (`Bond`)**: We close the hierarchy for Bonds.

### Asynchronous Pipeline (`AnalysisService`)
We moved beyond simple threads to **CompletableFuture** pipelines:
- **`supplyAsync`**: Fetches data in a background thread pool.
- **`thenApply`**: Processes data (e.g., calculating future value) as soon as it arrives.
- **`exceptionally`**: Handles errors gracefully without crashing the main thread.
- **`allOf`**: Combines multiple independent futures (parallel fetches) into a single result map.

### Generics & Registry
`GenericRepository` not only provides CRUD but maintains a **Class-Based Registry**.
- `add(entity)`: Automatically indexes the entity by its ID (if it's an Asset) AND by its Class type.
- This allows looking up "All Stocks" or "All Assets" efficiently without iterating the whole value set.

### Stream API "Cheat Sheet" (`StreamAnalytics`)
We implemented `StreamAnalytics.java` to demonstrate common interview patterns using the Java Stream API:
- **Filtering/Mapping**: `filter(price > 100).map(Symbol)`
- **Grouping**: `groupingBy(Stock::getSector)` -> `Map<String, List<Stock>>`
- **Counting**: `groupingBy(Sector, counting())` -> `Map<String, Long>`
- **Partitioning**: `partitioningBy(price > 1000)` -> `Map<Boolean, List<Asset>>` (Cheap vs Expensive)
- **Statistics**: `summaryStatistics()` -> Min, Max, Average value in one pass.
- **Handling Duplicates**: `toMap(key, val, mergeFunction)` to resolve collisions cleanly.

### Advanced: Meta-Programming (Dynamic Proxies)
We implemented a **Dynamic Proxy** (`LoggingHandler`) to demonstrate how frameworks like Spring work under the hood.
- **Concept**: A `java.lang.reflect.InvocationHandler` intercepts method calls at runtime.
- **Demo**: We wrap `GenericRepository` with a proxy. When `@Logged` methods are called, the proxy prints timing metrics (Aspect Oriented Programming - AOP).
- **Key Pattern**: This is the "Magic" behind `@Transactional`, `@Autowired`, and Lazy Loading.

## Implementation-Specific Interview Questions

### 1. Why Sealed Classes? Why not just `final`?
**Refers to**: `Asset` vs `Bond`
**Answer**:
- `final` prevents **all** inheritance.
- `sealed` allows **controlled** inheritance.
- Allows the compiler to perform **exhaustiveness checks** in `switch` expressions (e.g., if you switch on `Asset`, you don't need a `default` case if you handle `Stock` and `Bond`).

### 2. Difference between `Future.get()` and `CompletableFuture`?
**Refers to**: `AnalysisService`
**Answer**:
- `Future.get()` is **blocking**. It freezes the current thread until the result is ready.
- `CompletableFuture` allows **callback-style** (non-blocking) transformations (`thenApply`, `thenAccept`). You can describe the entire data pipeline before any data is even fetched.

### 3. In `Portfolio.java`, why is `getHoldings()` returning `Collections.unmodifiableMap`?
**Refers to**: `com.interview.portfolio.Portfolio.getHoldings()`
**Answer**: This is **Defensive Copying / Immutable View**.
If we returned the `ConcurrentHashMap` directly, a caller could do `portfolio.getHoldings().clear()`, wiping out the user's assets bypassing the `removeAsset` validation logic. wrapping it in `unmodifiableMap` ensures encapsulation is preserved—the outside world can *look* but cannot *touch*.

### 4. Can `Stock` be a key in a HashMap? What if `Stock` was a mutable class?
**Refers to**: `com.interview.portfolio.Stock`
**Answer**: `Stock` is designed as an **immutable class**, making it a perfect key.
If it were a mutable class, and we utilized it as a key:
1. We put `Stock("AAPL", $100)` into the map. It goes into Bucket A.
2. We change the price to `$200`. Its hashcode changes.
3. The map implementation now looks for it in Bucket B.
4. The entry is effectively lost (memory leak).
**Lesson**: Always use immutable objects for Map keys.

### 5. Explain strict encapsulation in the `Portfolio` class.
**Refers to**: `com.interview.portfolio.Portfolio`
**Answer**: The `Portfolio` class follows the "Tell, Don't Ask" principle. We don't ask for the map and manipulate it. We tell the portfolio to `addAsset`.
This allows `Portfolio` to enforce **Invariants**: "Quantity must be positive", "Cannot sell more than you own". If we exposed the raw list/map, we couldn't guarantee these rules are followed.

### 6. How does `Stream` API in repository compare to a `for` loop?
**Refers to**: `com.interview.portfolio.PortfolioService` / Repository
**Answer**:
- **Declarative**: Streams say *what* we want (`filter(sector).collect()`) rather than *how* (indexes, temporary lists).
- **Parallizable**: We can easily switch to `parallelStream()` if the dataset is huge (though not always faster).
- **Readability**: For complex filtering/mapping chains, Streams are often more readable than nested loops.

---

## Broader Conceptual Interview Questions (Collections & Streams)

### 7. How does a HashMap handle collisions internally?
**Concept**: Algorithms & Data Structures.
**Answer**:
- **Chaining**: When multiple keys have the same hash (Collision), they are stored in a linked list (chain) at that bucket index.
- **Treeification (Java 8+)**: If a chain grows too long (threshold is 8), Java converts the Linked List into a **Red-Black Tree**. This improves lookup performance from O(n) (list scan) to O(log n) (tree search), preventing Denial-of-Service attacks based on hash collisions.

### 8. Difference between `map()` and `flatMap()` in Streams?
**Concept**: Functional Programming.
**Answer**:
- **`map`**: Transforms each element one-to-one.
    - `Stream<String> -> map(s -> s.length()) -> Stream<Integer>`
- **`flatMap`**: Transforms each element one-to-many. It "flattens" the resulting streams into a single stream.
    - Example: You have a `List<Order>`. Each `Order` has `List<LineItem>`.
    - `orders.stream().flatMap(order -> order.getLineItems().stream())` gives you a single `Stream<LineItem>`.

### 9. Checked vs Unchecked Exceptions?
**Concept**: Error Handling.
**Answer**:
- **Checked (extends `Exception`)**: Compiler forces you to `catch` or `throws`. Represents recoverable error conditions (e.g., `IOException` file not found).
- **Unchecked (extends `RuntimeException`)**: Compiler does not force handling. Represents programming errors (e.g., `NullPointerException`, `IndexOutOfBounds`).
- **Trend**: Modern framworks (Spring) and libraries favour Unchecked exceptions to reduce boilerplate and "catch-ignore" blocks.

### 10. What is the contract between `equals()` and `hashCode()`?
**Refers to**: Essential for `Stock` to work as a Map key.
**Answer**:
1. If `x.equals(y)` is true, `x.hashCode()` MUST equal `y.hashCode()`.
2. If `hashCode()` is different, objects are definitely not equal.
3. If `hashCode()` is same, objects MIGHT be equal (collision).
**Violation**: If you override equals but not hashCode, storing objects in a HashMap will break (you'll lose items).

### 11. Why should you avoid `Optional` in fields?
**Concept**: Java 8 Best Practices.
**Answer**:
- **Serializable**: `Optional` does not implement `Serializable`. If your class needs to be serialized (e.g., for caching/sending over network), `Optional` fields will fail.
- **Memory**: It adds logical overhead (wrapping object).
- **Best Practice**: Use `Optional` only as a **return type** for methods to indicate "no result", forcing the caller to handle nullability.
