package org.example.Q0712_MinimumDeleteSum;

// DP (LCS Variation): Maximize the ASCII sum of the common subsequence.
// Min Deleted Sum = Total ASCII Sum - 2 * (Max Common ASCII Sum)
public class MinimumDeleteSum {

    public int minimumDeleteSum(String s1, String s2) {
        int n = s1.length();
        int m = s2.length();

        // dp[i][j] stores the maximum ASCII sum of common subsequence for s1[0...i-1]
        // and s2[0...j-1]
        int[][] dp = new int[n + 1][m + 1];

        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                if (s1.charAt(i) == s2.charAt(j)) {
                    // Characters match: add to common sum
                    dp[i + 1][j + 1] = dp[i][j] + s1.charAt(i);
                } else {
                    // Characters don't match: take max from either skipping char in s1 or s2
                    dp[i + 1][j + 1] = Math.max(dp[i][j + 1], dp[i + 1][j]);
                }
            }
        }

        // Calculate total ASCII sum of both strings
        int total = 0;
        for (char c : s1.toCharArray())
            total += c;
        for (char c : s2.toCharArray())
            total += c;

        // Result is Total - 2 * (Common Part)
        // Because the common part was counted twice in 'total' (once for s1, once for
        // s2)
        // but should remain once in the final equal string.
        // Wait, logic check:
        // Deleted = (Sum_s1 - Common) + (Sum_s2 - Common)
        // = Sum_s1 + Sum_s2 - 2 * Common
        return total - 2 * dp[n][m];
    }

    public static void main(String[] args) {
        MinimumDeleteSum solution = new MinimumDeleteSum();

        System.out.println(solution.minimumDeleteSum("sea", "eat")); // Output: 231
        System.out.println(solution.minimumDeleteSum("delete", "leet")); // Output: 403
    }
}
