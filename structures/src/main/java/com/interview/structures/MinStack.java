package com.interview.structures;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * MinStack implementation supporting O(1) Push, Pop, Top, and GetMin.
 * Uses the "Two Stack" approach (Memory for Speed trade-off).
 */
public class MinStack {
    // Main stack for data
    private final Deque<Integer> stack;
    // Auxiliary stack for tracking minimums
    private final Deque<Integer> minStack;

    public MinStack() {
        stack = new ArrayDeque<>();
        minStack = new ArrayDeque<>();
    }

    public void push(int val) {
        stack.push(val);

        // CRITICAL: We use <= here.
        // If we push a duplicate of the current min (e.g., another 2),
        // we must record it so that popping one 2 doesn't lose the record of the other.
        if (minStack.isEmpty() || val <= minStack.peek()) {
            minStack.push(val);
        }
    }

    public void pop() {
        // If the stack is empty, return (or throw exception)
        if (stack.isEmpty())
            return;

        int poppedValue = stack.pop();

        // If the value we removed was the current minimum, remove it from minStack too
        if (poppedValue == minStack.peek()) {
            minStack.pop();
        }
    }

    public int top() {
        if (stack.isEmpty())
            throw new RuntimeException("Stack is empty");
        return stack.peek();
    }

    public int getMin() {
        if (minStack.isEmpty())
            throw new RuntimeException("Stack is empty");
        return minStack.peek();
    }
}
