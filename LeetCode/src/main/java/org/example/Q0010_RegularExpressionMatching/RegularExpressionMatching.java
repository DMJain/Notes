package org.example.Q0010_RegularExpressionMatching;

// DP: dp[i][j] = does s[0..i-1] match p[0..j-1]? Handle '.' and '*' specially
public class RegularExpressionMatching {

    public boolean isMatch(String s, String p) { // @viz:input
        if (s == null || p == null)
            return false;

        // dp[i][j] = true if s[0..i-1] matches p[0..j-1]
        boolean[][] dp = new boolean[s.length() + 1][p.length() + 1];

        dp[0][0] = true; // Empty string matches empty pattern

        // Handle patterns like "a*", "a*b*", etc. that can match empty string
        for (int i = 0; i < p.length(); i++) { // @viz:loop(i,p)
            if (p.charAt(i) == '*' && dp[0][i - 1]) {
                dp[0][i + 1] = true; // x* can match zero occurrences
            }
        }

        for (int i = 0; i < s.length(); i++) {
            for (int j = 0; j < p.length(); j++) { // @viz:loop(j,p)
                // Case 1: '.' matches any single character
                if (p.charAt(j) == '.') {
                    dp[i + 1][j + 1] = dp[i][j];
                }

                // Case 2: Exact character match
                if (p.charAt(j) == s.charAt(i)) {
                    dp[i + 1][j + 1] = dp[i][j];
                }

                // Case 3: '*' - match zero or more of preceding element
                if (p.charAt(j) == '*') {
                    if (p.charAt(j - 1) != s.charAt(i) && p.charAt(j - 1) != '.') {
                        // Preceding char doesn't match → use zero occurrences
                        dp[i + 1][j + 1] = dp[i + 1][j - 1];
                    } else {
                        // Preceding char matches → three options:
                        dp[i + 1][j + 1] = (dp[i + 1][j] || dp[i][j + 1] || dp[i + 1][j - 1]);
                    }
                }
            }
        }

        return dp[s.length()][p.length()]; // @viz:result(Match result)
    }

    public static void main(String[] args) {
        RegularExpressionMatching solution = new RegularExpressionMatching();

        System.out.println(solution.isMatch("aa", "a*")); // Output: true
    }
}
