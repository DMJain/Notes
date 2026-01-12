package org.example.Q0022_GenerateParentheses;

import java.util.ArrayList;
import java.util.List;

// Backtracking: Keep track of open (o) and closed (c) counts. 
// Can add '(' if o < n. Can add ')' if c < o.
public class GenerateParentheses {

    public List<String> generateParenthesis(int n) {
        List<String> res = new ArrayList<>();
        backTrack(0, 0, n, new StringBuilder(), res);
        return res;
    }

    private void backTrack(int o, int c, int n, StringBuilder sb, List<String> res) {
        // Base case: we have used n open and n closed brackets
        if (o == n && c == n) {
            res.add(sb.toString());
            return;
        }

        // We can add an open bracket if we haven't used all n yet
        if (o < n) {
            sb.append('(');
            backTrack(o + 1, c, n, sb, res);
            sb.deleteCharAt(sb.length() - 1); // Backtrack
        }

        // We can add a close bracket if we have more open than closed so far
        if (c < o) {
            sb.append(')');
            backTrack(o, c + 1, n, sb, res);
            sb.deleteCharAt(sb.length() - 1); // Backtrack
        }
    }

    public static void main(String[] args) {
        GenerateParentheses solution = new GenerateParentheses();

        System.out.println(solution.generateParenthesis(3));
        // Output: [((())), (()()), (())(), ()(()), ()()()]

        System.out.println(solution.generateParenthesis(1));
        // Output: [()]
    }
}
