package com.interview.structures.concurrency;

/**
 * Demonstrates a classic Deadlock scenario and how to avoid it.
 * 
 * Concept:
 * Thread 1 locks A, waits for B.
 * Thread 2 locks B, waits for A.
 * Result: Forever waiting.
 * 
 * Solution: Always acquire locks in the same order (e.g., A then B).
 */
public class DeadlockDemo {

    private final Object lockA = new Object();
    private final Object lockB = new Object();

    public void triggerDeadlock() {
        Thread t1 = new Thread(() -> {
            synchronized (lockA) {
                System.out.println("Thread 1: Holding lock A...");
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                }
                System.out.println("Thread 1: Waiting for lock B...");
                synchronized (lockB) {
                    System.out.println("Thread 1: Acquired lock B!");
                }
            }
        });

        Thread t2 = new Thread(() -> {
            synchronized (lockB) {
                System.out.println("Thread 2: Holding lock B...");
                try {
                    Thread.sleep(50);
                } catch (InterruptedException e) {
                }
                System.out.println("Thread 2: Waiting for lock A...");
                synchronized (lockA) {
                    System.out.println("Thread 2: Acquired lock A!");
                }
            }
        });

        t1.start();
        t2.start();
    }

    // SAFE method: deadlock prevention by ordering
    public void safeExecution() {
        Thread t1 = new Thread(() -> {
            synchronized (lockA) {
                synchronized (lockB) {
                    System.out.println("Safe Thread 1: Acquired both!");
                }
            }
        });

        Thread t2 = new Thread(() -> {
            // key fix: Thread 2 also acquires A then B, not B then A
            synchronized (lockA) {
                synchronized (lockB) {
                    System.out.println("Safe Thread 2: Acquired both!");
                }
            }
        });

        t1.start();
        t2.start();
    }
}
