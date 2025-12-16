# Structures Module

## Overview
This module explores fundamental **Data Structures** by implementing them from scratch or solving classic interview problems. It avoids standard Java Collections to demonstrate lower-level understanding of memory, pointers, and algorithmic complexity.

## Package Organization
To maintain clarity, the module is refactored into logical sub-packages:

### `com.interview.structures.list`
- `DynamicArray`: Resizable generic array.
- `ParallelSmartList`: Optimized parallel stream processing.

### `com.interview.structures.tree`
- `BinarySearchTree`: Recursive BST.
- `MinHeap`: Array-based binary heap.
- `Trie`: Prefix tree for string operations.

### `com.interview.structures.map`
- `CustomHashMap`: Chaining implementation.
- `LRUCache`: LinkedHashMap simulation.
- `SoftCache`: Memory-sensitive caching.

### `com.interview.structures.linear`
- `CustomStack`, `MinStack`: Array and Multi-Stack implementations.
- `MyDeque`, `QueueUsingStack`: Node-based and Algorithmic queues.
- `SimpleBlockingQueue`: Concurrency primitives.
- `BalancedParentheses`: Stack application.

### `com.interview.structures.graph`
- `Graph`: Adjacency list implementation (BFS/DFS).

## Components

### 1. `ParallelSmartList` (Custom Spliterator)
A list that implements a custom `Spliterator` to optimize parallel stream processing.
- **Concept**: `Spliterator` (Split-Iterator) is the engine behind `parallelStream()`.
- **Implementation**: `trySplit()` recursively divides the array into chunks ([0..50], [50..100]) so multiple threads can process chunks independently.
- **Why?**: Demonstrates deep understanding of Java Streams beyond just "using" them.

### 2. `SoftCache` (Reference Objects)
A Cache using `SoftReference<V>`.
- **Concept**: Objects are kept in memory but can be reclaimed by the Garbage Collector (GC) if memory runs low.
- **Use Case**: Caching large images or files. Prevents `OutOfMemoryError` by sacrificing cache hits when under pressure.

### 3. `MyDeque` (Doubly Linked List)
A custom Deque implementation built from scratch using explicit `Node` class with `prev` and `next` pointers.
- **Why?**: To demonstrate how `java.util.LinkedList` works under the hood.
- **Complexity**: O(1) for adding/removing from both ends (`addFirst`, `removeLast`, etc.).

### 4. `CustomStack` (Array Implementation)
A fixed-size stack using a primitive `int[]` array.
- **Core Concept**: **LIFO** (Last-In, First-Out).
- **Why avoid `java.util.Stack`?**: The legacy `Stack` class extends `Vector`, making every method `synchronized`. This causes valid performance overhead in single-threaded apps. Modern Java prefers `Deque` (e.g., `ArrayDeque`).

### 5. `MinStack` (O(1) Retrieval)
A stack that supports `getMin()` in constant time.
- **Strategy**: **Two Stacks**.
    - `stack`: Stores all elements.
    - `minStack`: Stores *only* the minimums.
- **Logic**: When pushing `val`, if `val <= minStack.peek()`, push it to `minStack` too. When popping, if `val == minStack.peek()`, pop from both.

### 6. `QueueUsingStack` (Amortized Analysis)
A Queue (FIFO) implemented using only Stacks (LIFO).
- **Strategy**: **Input vs Output**.
    - `inputStack`: Accepts new elements.
    - `outputStack`: Provides elements for `pop`.
- **Shift Logic**: Only when `outputStack` is empty do we moving *all* elements from `input` to `output`. This reverses the order (LIFO + LIFO = FIFO).
- **Amortized Complexity**: Although a single `pop` can take O(n) (moving elements), each element is moved exactly once. Over time, the cost per operation is **Amortized O(1)**.

### 7. `BalancedParentheses`
Solves the classic validation problem `{[()]}` using a Stack.
- **Mixed Content**: The algorithm explicitly ignores non-bracket characters, allowing it to validate complex strings like code snippets (`if (a[0]) { return; }`) or JSON.

### 8. `DynamicArray` (Resizing List)
A generic array wrapper that mimics `ArrayList`.
- **Logic**: When full, `grow()` creates a new array of double size and copies elements.
- **Complexity**: Amortized O(1) add, O(1) get.

### 9. `CustomHashMap` (Chaining)
A Key-Value store attempting to clear up "How HashMaps work".
- **Concept**: Array of buckets (Linked Lists).
- **Collision**: Uses chaining. If two keys hash to the same bucket, they share a list.

### 10. `MinHeap` (Binary Heap)
Value-ordered binary tree stored in an array (Basis for PriorityQueue).
- **Logic**: Parent is always smaller than children. Validates Heap Property.
- **Operations**: `add` (bubble-up), `poll` (move last to root, sift-down).

### 11. `SimpleBlockingQueue` (Concurrency)
Demonstrates thread-safe queuing using explicit Locks.
- **Mechanics**: Uses `ReentrantLock` and `Condition` (`notFull`, `notEmpty`).
- **Flow**: `put()` waits if full, `take()` waits if empty. Effectively implements Producer-Consumer pattern.

### 12. `LRUCache`
Least Recently Used replacement policy (Simulates `LinkedHashMap`).
- **Design**: HashMap + Doubly Linked List.
- **O(1)** access and update. New/accessed items move to head; old items evicted from tail.

### 13. `BinarySearchTree` (Sorted Tree)
Recursive tree implementation.
- **Logic**: Left child < Parent < Right child.
- **Use Case**: Foundation for `TreeMap`/`TreeSet` (though those use Red-Black balancing).

### 14. `Graph` (Network)
Adjacency List implementation (`Map<T, List<T>>`).
- **Algorithms**:
    - **BFS** (Queue): Shortest path in unweighted graphs.
    - **DFS** (Recursion): Path finding, cycle detection.

### 15. `Trie` (Prefix Tree)
Tree optimized for string searching.
- **Complexity**: O(L) where L is word length. Faster than HashMap for prefix searches.
- **Use Case**: Autocomplete, Spell Checker.

### 16. `LockFreeStack` (CAS / Atomics)
A Thread-Safe Stack that uses **No Locks** (`synchronized`).
- **Mechanism**: `AtomicReference` + `Compare-And-Swap` (CAS).
- **Algorithm**: Read Head -> Create New Node -> Try to Swap Head.
- **Loop**: `while(!head.compareAndSet(old, new)) check...`
- **Why**: "Wait-free" or "Lock-free" algorithms prevent thread starvation and deadlock.

### 17. `FastBeanCopier` (MethodHandles)
Demonstrates **Modern Reflection** (`java.lang.invoke`).
- **Concept**: `MethodHandles.Lookup` creates direct pointers to methods.
- **Performance**: Faster than legacy `Method.invoke()` because the JVM can optimize/inline them better.

### 18. `LargeFileProcessor` (Memory Mapped IO)
Demonstrates **Zero-Copy IO** (`MappedByteBuffer`).
- **Concept**: Maps a file directly into the application's virtual memory address space.
- **Benefit**: The OS handles page swapping. The app reads "memory" without copying data from Kernel buffer to User buffer. Essential for high-performance DBs (like Kafka/Cassandra).

---

## Interview Questions & Answers

### Q1. WeakReference vs SoftReference?
**Refers to**: `SoftCache`
**Answer**:
- **Strong Reference** (Standard): Object is never GC'd as long as it's reachable.
- **SoftReference**: Object is GC'd only if memory is **low**. Great for caches.
- **WeakReference**: Object is GC'd on **next GC cycle** aggressively. Good for metadata maps (e.g., `WeakHashMap`).
- **PhantomReference**: Used for cleanup scheduling (post-mortem).

### Q2. How does `parallelStream()` split work?
**Refers to**: `ParallelSmartList`
**Answer**: creating a thread pool isn't enough. The source data must be **splittable**.
- `Spliterator.trySplit()` slices the data source into two independent halves.
- Java's ForkJoinPool recursively calls `trySplit()` until chunks are small enough to process sequentially.
- If your `trySplit` is poor (e.g., unbalanced), one thread works while others idle (Skew).

### Q3: Why is `java.util.Stack` considered legacy?
**Answer**: It violates the standard "Vector vs ArrayList" principle. `Stack` extends `Vector`, making all operations `synchronized`. For most single-threaded use cases, this locking overhead is unnecessary. The recommended replacement is `Deque<T> stack = new ArrayDeque<>()`.

### Q4: Explain the "Amortized O(1)" complexity of `QueueUsingStack`.
**Refers to**: `pop()` method.
**Answer**:
- **Best Case**: O(1) if `outputStack` is not empty.
- **Worst Case**: O(n) if we must move all elements from `inputStack`.
- **Amortized**: Since we only move an item to `output` *once* during its entire lifecycle in the queue, we spread that O(n) cost across all N elements. Thus, the average cost per element works out to constant time O(1).

### Q5: What is the trade-off in `MinStack`?
**Answer**: We trade **Space for Time**.
- We get O(1) time complexity for `getMin()`.
- We pay O(n) space complexity for the auxiliary `minStack` to store helper data.

### Q6: When would you use a Linked List Stack over an Array Stack?
**Refers to**: `MyDeque` vs `CustomStack`.
**Answer**:
- **Array Stack**: Better cache locality (contiguous memory), less memory overhead (no pointers). But requires resizing (copying array) when full.
- **Linked List**: Dynamic sizing (no resize cost), but higher memory per element (storing pointers) and worse cache locality (nodes scattered in heap).
