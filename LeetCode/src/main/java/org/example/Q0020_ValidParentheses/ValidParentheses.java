package org.example.Q0020_ValidParentheses;

import java.util.Stack;

// Stack: Push open brackets, pop matching close brackets. If mismatch or stack empty/non-empty at end -> invalid.
public class ValidParentheses {

    public boolean isValid(String s) {
        Stack<Character> stack = new Stack<>();

        for (int i = 0; i < s.length(); i++) {
            char c = s.charAt(i);

            if (c == '[' || c == '{' || c == '(') {
                stack.push(c);
            } else {
                if (stack.isEmpty())
                    return false; // Extra closing bracket

                if (c == ']' && stack.peek() == '[')
                    stack.pop();
                else if (c == '}' && stack.peek() == '{')
                    stack.pop();
                else if (c == ')' && stack.peek() == '(')
                    stack.pop();
                else
                    return false; // Mismatched bracket
            }
        }

        return stack.isEmpty();
    }

    public static void main(String[] args) {
        ValidParentheses solution = new ValidParentheses();

        System.out.println(solution.isValid("()")); // true
        System.out.println(solution.isValid("()[]{}")); // true
        System.out.println(solution.isValid("(]")); // false
        System.out.println(solution.isValid("([])")); // true
        System.out.println(solution.isValid("([)]")); // false
        System.out.println(solution.isValid("]")); // false
    }
}
