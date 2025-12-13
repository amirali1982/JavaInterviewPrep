package com.interview.structures;

import java.util.ArrayDeque;
import java.util.Deque;

/**
 * Classic Stack Problem: Balanced Parentheses.
 * Validates that brackets are balanced even when interspersed with other
 * characters.
 * Example: "function() { return [1, 2]; }" is valid.
 * Time Complexity: O(n)
 * Space Complexity: O(n)
 */
public class BalancedParentheses {

    public boolean isValid(String s) {
        Deque<Character> stack = new ArrayDeque<>();

        for (char c : s.toCharArray()) {
            // 1. Handle Opening Brackets
            if (c == '(')
                stack.push(')');
            else if (c == '{')
                stack.push('}');
            else if (c == '[')
                stack.push(']');

            // 2. Handle Closing Brackets explicitly
            // If the character is a closing bracket, the stack must not be empty,
            // and the top of the stack must match (which we stored as the expectation).
            else if (c == ')' || c == '}' || c == ']') {
                if (stack.isEmpty() || stack.pop() != c) {
                    return false;
                }
            }

            // 3. Any other character is implicitly ignored
        }

        return stack.isEmpty();
    }
}
