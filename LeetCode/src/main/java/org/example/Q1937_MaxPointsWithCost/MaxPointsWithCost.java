package org.example.Q1937_MaxPointsWithCost;

// DP with Optimization: Break absolute difference into left/right passes to avoid O(N^2) transition
public class MaxPointsWithCost {

    public long maxPoints(int[][] points) {
        int rows = points.length;
        int cols = points[0].length;
        long[] dp = new long[cols];

        // Initialize first row
        for (int i = 0; i < cols; i++) {
            dp[i] = points[0][i];
        }

        // Process remaining rows
        for (int r = 1; r < rows; r++) {
            long[] leftMax = new long[cols];
            long[] rightMax = new long[cols];
            long[] newDp = new long[cols];

            // Left-to-right pass: Maximize (prev_dp[k] + k)
            // We want max(prev_dp[k] - (i - k)) = max(prev_dp[k] + k) - i
            leftMax[0] = dp[0];
            for (int i = 1; i < cols; i++) {
                leftMax[i] = Math.max(leftMax[i - 1], dp[i] + i);
            }

            // Right-to-left pass: Maximize (prev_dp[k] - k)
            // We want max(prev_dp[k] - (k - i)) = max(prev_dp[k] - k) + i
            rightMax[cols - 1] = dp[cols - 1] - (cols - 1);
            for (int i = cols - 2; i >= 0; i--) {
                rightMax[i] = Math.max(rightMax[i + 1], dp[i] - i);
            }

            // Calculate new DP values for current row
            for (int i = 0; i < cols; i++) {
                // Best from left: leftMax[i] - i
                // Best from right: rightMax[i] + i
                newDp[i] = Math.max(leftMax[i] - i, rightMax[i] + i) + points[r][i];
            }

            dp = newDp;
        }

        // Find max in the last row
        long result = Long.MIN_VALUE;
        for (long value : dp) {
            result = Math.max(result, value);
        }

        return result;
    }

    public static void main(String[] args) {
        MaxPointsWithCost solution = new MaxPointsWithCost();

        int[][] points1 = { { 1, 2, 3 }, { 1, 5, 1 }, { 3, 1, 1 } };
        System.out.println(solution.maxPoints(points1)); // Output: 9

        int[][] points2 = { { 1, 5 }, { 2, 3 }, { 4, 2 } };
        System.out.println(solution.maxPoints(points2)); // Output: 11
    }
}
