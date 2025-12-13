# RegTech (Validation Rules) Module

## Overview
This module implements a flexible **Rules Engine** for validating Share Transfers, written in **Java 17**. It ensures compliance with business rules (KYC, Balance, Market Status) before any transaction is processed.

## Features
- **Extensible Rules Engine**: Uses the **Strategy Pattern** to allow adding new rules without modifying the engine.
- **Fail-Fast Validation**: Stops at the first failed rule to report errors immediately.
- **Clean Architecture**: Decoupled dependencies using interfaces (`PortfolioRepository`, `MarketProvider`).

## Implementation Details
- **`Rule<T>` Interface**: The core abstraction (Strategy) for any validation logic.
- **`RegTechEngine`**: Context class that manages and executes the strategies.
- **`TransferContext`**: A **Java Record** holding immutable context (Sender, Receiver, Stock, Quantity).
- **Fail-Fast vs Fail-Safe**: The engine implements fail-fast logic (returns on first error). This is efficient for blocking transactions but can be switched to "collect all errors" if needed for UI feedback.

## Top Interview Questions & Answers

### 1. Why use a Rules Engine (Strategy Pattern) instead of a simple `switch` or `if-else` block?
**Refers to**: `com.interview.regtech.RegTechEngine`
**Answer**: A giant `if-else` block violates the **Single Responsibility Principle** (the service knows too much) and the **Open/Closed Principle** (you must modify the service to add a rule).
In this example, `RegTechEngine` doesn't know *what* the rules are, only *how to run them*. Each Rule is a separate class (`SenderHasBalanceRule`), making them easier to test in isolation, reuse in other contexts, and maintain by different teams.

### 2. How did you ensure thread safety in the Engine?
**Refers to**: `com.interview.regtech.Rule` implementations
**Answer**: The `RegTechEngine` is stateless regarding the *data* (it just passes `TransferContext` through).
Thread safety depends on the Rules.
- `SenderHasBalanceRule` delegates to `PortfolioRepository`. If that repository uses safe structures (like `ConcurrentHashMap` in the Portfolio module), the rule is safe.
- `TransferContext` is a **Record**, so it is immutable. Immutable objects can be safely shared between multiple threads without synchronization, preventing race conditions on the input data.

### 3. How would you handle rules that depend on each other (e.g., Rule B runs only if Rule A passes)?
**Refers to**: `com.interview.regtech.RegTechEngine` structure
**Answer**: The current design is a linear chain. For dependencies, we could:
1.  **Composite Pattern**: Create a `CompositeRule` that internally holds Rule A and Rule B and manages their execution order.
2.  **Order Property**: Add `getOrder()` to the `Rule` interface and sort the list in the Engine before execution.
3.  **Stateful Context**: Enrich `TransferContext` with results from previous rules (though this reduces purity).

### 4. What is the benefit of `TransferContext` being a Record?
**Refers to**: `com.interview.regtech.TransferContext`
**Answer**: Since Java 14, **Records** reduce boilerplate for data carriers. In a Rules Engine, the context is purely for carrying data to rules. Using a Record ensures:
- **Immutability**: A rule cannot accidentally change the `quantity` and affect subsequent rules.
- **Readability**: The intent "this is just data" is clear.
