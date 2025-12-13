package com.interview.structures;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Queue implementation using two Stacks.
 * Key Concept: Input vs Output Stacks.
 * Pop Complexity: Amortized O(1).
 */
public class QueueUsingStack {
    private final Deque<Integer> inputStack;
    private final Deque<Integer> outputStack;

    public QueueUsingStack() {
        inputStack = new ArrayDeque<>();
        outputStack = new ArrayDeque<>();
    }

    // Push element x to the back of queue. O(1)
    public void push(int x) {
        inputStack.push(x);
    }

    // Removes the element from in front of queue and returns that element.
    // Amortized O(1)
    public int pop() {
        shiftStacks();
        if (outputStack.isEmpty())
            throw new RuntimeException("Queue is empty");
        return outputStack.pop();
    }

    // Get the front element.
    // Amortized O(1)
    public int peek() {
        shiftStacks();
        if (outputStack.isEmpty())
            throw new RuntimeException("Queue is empty");
        return outputStack.peek();
    }

    // Returns whether the queue is empty. O(1)
    public boolean empty() {
        return inputStack.isEmpty() && outputStack.isEmpty();
    }

    // Helper method to move elements from Input to Output
    // This happens only when Output is empty.
    private void shiftStacks() {
        if (outputStack.isEmpty()) {
            while (!inputStack.isEmpty()) {
                outputStack.push(inputStack.pop());
            }
        }
    }
}
