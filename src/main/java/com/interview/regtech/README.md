# RegTech (Validation Rules) Module

## Overview
This module implements a flexible Rules Engine for validating Share Transfers. It ensures compliance with business rules before any transaction is processed.

## Features
- **Extensible Rules Engine**: Uses the Strategy pattern to allow adding new rules without modifying the engine.
- **Fail-Fast Validation**: Stops at the first failed rule to report errors immediately.
- **Clean Architecture**: Decoupled dependencies using interfaces (`PortfolioRepository`, `MarketProvider`).

## Implementation Details
- **`Rule<T>` Interface**: The core abstraction for any validation logic.
- **`RegTechEngine`**: Manages and executes a collection of rules.
- **`TransferContext`**: An immutable record holding the context for validation (Sender, Receiver, Stock, Quantity).
- **Concrete Rules**:
    - `SenderHasBalanceRule`: Checks sufficiency of funds/stock.
    - `ReceiverExistsRule`: Validates the destination account.
    - `MarketIsOpenRule`: Ensures trading is allowed at the current time.

## Top Interview Questions & Answers

### 1. Why use a Rules Engine instead of `if-else` blocks in the Service?
**Answer**: Using a Rules Engine (Strategy/Composite pattern) adheres to the **Open/Closed Principle**. We can add new rules (e.g., "Daily Limit Exceeded", "Stock Restricted") by creating new classes without modifying the core `TransferService` or `RegTechEngine` logic. It also improves testability as each rule can be unit-tested in isolation.

### 2. How did you ensure thread safety?
**Answer**: The `RegTechEngine` itself is stateless during validation (if rules, are stateless). The `Rule` implementations rely on repositories. If `PortfolioRepository` uses `ConcurrentHashMap` (as seen in `Portfolio` class), updates and reads are thread-safe. `TransferContext` is an immutable record, safe for passing between threads.

### 3. How would you handle rules that need to query an external High-Latency API?
**Answer**: 
- **Async Validation**: Run rules asynchronously (CompletableFuture).
- **Caching**: Cache results if the data changes infrequently (e.g., Market Status).
- **ordering**: Run fast, in-memory rules first (Sender Balance) and expensive rules last (Feature flags, External Compliance Checks).

### 4. Why use Records for `TransferContext`?
**Answer**: Java Records provide a terse syntax for immutable data carriers. They automatically generate `equals`, `hashCode`, and `toString`, reducing boilerplate and ensuring the context implementation cannot be accidentally mutated during the validation chain.
