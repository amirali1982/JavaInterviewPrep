package com.interview.structures;

/**
 * A Fixed-Size Stack implementation using primitive arrays.
 * Demonstrates "Under the Hood" stack mechanics.
 */
public class CustomStack {
    private int maxSize;
    private int[] stackArray;
    private int top;

    public CustomStack(int s) {
        maxSize = s;
        stackArray = new int[maxSize];
        top = -1; // -1 indicates the stack is empty
    }

    public void push(int j) {
        if (top == maxSize - 1) {
            throw new RuntimeException("Stack Overflow");
        }
        stackArray[++top] = j; // Increment top, then insert
    }

    public int pop() {
        if (top == -1) {
            throw new RuntimeException("Stack Underflow");
        }
        return stackArray[top--]; // Return value, then decrement
    }

    public int peek() {
        if (top == -1) {
            throw new RuntimeException("Stack is empty");
        }
        return stackArray[top];
    }

    public boolean isEmpty() {
        return (top == -1);
    }

    public int size() {
        return top + 1;
    }
}
