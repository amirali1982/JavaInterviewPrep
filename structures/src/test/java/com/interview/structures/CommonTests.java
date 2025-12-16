package com.interview.structures;

import com.interview.structures.linear.*;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class CommonTests {

    @Test
    void testCustomStack() {
        CustomStack stack = new CustomStack(5);
        stack.push(10);
        stack.push(20);
        assertEquals(20, stack.pop());
        assertEquals(10, stack.peek());
        assertFalse(stack.isEmpty());
        stack.pop();
        assertTrue(stack.isEmpty());
    }

    @Test
    void testMinStack() {
        MinStack ms = new MinStack();
        ms.push(5);
        ms.push(2);
        ms.push(10);
        ms.push(2); // Duplicate min

        assertEquals(2, ms.getMin());
        ms.pop(); // Remove duplicate 2
        assertEquals(2, ms.getMin());
        ms.pop(); // Remove 10
        assertEquals(2, ms.getMin());
        ms.pop(); // Remove 2
        assertEquals(5, ms.getMin());
    }

    @Test
    void testQueueUsingStack() {
        QueueUsingStack q = new QueueUsingStack();
        q.push(1);
        q.push(2);
        q.push(3);

        assertEquals(1, q.peek());
        assertEquals(1, q.pop()); // Output stack fills here
        assertEquals(2, q.pop());
        q.push(4);
        assertEquals(3, q.pop()); // Remaining from first batch
        assertEquals(4, q.pop()); // New batch
    }

    @Test
    void testBalancedParentheses() {
        BalancedParentheses bp = new BalancedParentheses();
        assertTrue(bp.isValid("()"));
        assertTrue(bp.isValid("{[]}"));
        assertTrue(bp.isValid("{[()]}"));
        assertTrue(bp.isValid("function() { if (true) { return [1, 2]; } }")); // Real code snippet
        assertFalse(bp.isValid("([)]"));
        assertFalse(bp.isValid("{[}"));
    }

    @Test
    void testMyDeque() {
        MyDeque<String> deque = new MyDeque<>();
        deque.addFirst("A");
        deque.addLast("B"); // A, B
        deque.addFirst("C"); // C, A, B

        assertEquals("C", deque.peekFirst());
        assertEquals("B", deque.peekLast());
        assertEquals("C", deque.removeFirst());
        assertEquals("B", deque.removeLast());
        assertEquals("A", deque.removeFirst());
        assertTrue(deque.isEmpty());
    }
}
