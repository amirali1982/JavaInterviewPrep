package com.interview.structures;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Thread-safe Blocking Queue implementation using ReentrantLock.
 * Demonstrates: Concurrency control, Condition await/signal.
 * 
 * @param <E> Element type
 */
public class SimpleBlockingQueue<E> {
    private final Queue<E> queue;
    private final int capacity;
    private final ReentrantLock lock = new ReentrantLock();
    // Condition to wait on when queue is full (Producers wait)
    private final Condition notFull = lock.newCondition();
    // Condition to wait on when queue is empty (Consumers wait)
    private final Condition notEmpty = lock.newCondition();

    public SimpleBlockingQueue(int capacity) {
        this.capacity = capacity;
        this.queue = new LinkedList<>();
    }

    public void put(E e) throws InterruptedException {
        lock.lock();
        try {
            while (queue.size() == capacity) {
                // Wait until space is available
                notFull.await();
            }
            queue.add(e);
            // Signal consumers that item is available
            notEmpty.signal();
        } finally {
            lock.unlock();
        }
    }

    public E take() throws InterruptedException {
        lock.lock();
        try {
            while (queue.isEmpty()) {
                // Wait until item is available
                notEmpty.await();
            }
            E item = queue.remove();
            // Signal producers that space is available
            notFull.signal();
            return item;
        } finally {
            lock.unlock();
        }
    }
}
