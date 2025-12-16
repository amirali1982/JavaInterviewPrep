package com.interview.structures.concurrency;

import java.util.concurrent.atomic.AtomicReference;

/**
 * A Thread-Safe Stack that uses Non-Blocking Synchronization (CAS).
 * Unlike 'synchronized', this does not block threads. It uses a spin-loop
 * (retry)
 * if standard contention occurs.
 */
public class LockFreeStack<T> {

    // The Head of the stack is stored in an AtomicReference
    private final AtomicReference<Node<T>> head = new AtomicReference<>();

    public void push(T value) {
        Node<T> newHead = new Node<>(value);
        Node<T> currentHead;

        // CAS Loop: Try to update head until successful
        do {
            currentHead = head.get();
            newHead.next = currentHead;
            // compareAndSet(expected, update)
            // If head == currentHead, set head = newHead and return true.
            // Else, return false (head was changed by another thread), loop and retry.
        } while (!head.compareAndSet(currentHead, newHead));
    }

    public T pop() {
        Node<T> currentHead;
        Node<T> newHead;

        do {
            currentHead = head.get();
            if (currentHead == null) {
                return null; // Stack is empty
            }
            newHead = currentHead.next;
        } while (!head.compareAndSet(currentHead, newHead));

        return currentHead.value;
    }

    public T peek() {
        Node<T> current = head.get();
        return current != null ? current.value : null;
    }

    private static class Node<T> {
        final T value;
        Node<T> next;

        Node(T value) {
            this.value = value;
        }
    }
}
