package org.example.Q3651_MinimumCostPathWithTeleportations;

import java.util.Arrays;

/**
 * LeetCode 3651 - Minimum Cost Path with Teleportations
 * 
 * Key Insight: Use DP with suffix minimum optimization for teleportations.
 * 
 * Algorithm:
 * 1. Process teleportation usage layer by layer (0 to k)
 * 2. f[j+1] = minimum cost to reach column j in current row
 * 3. minF[x] = minimum cost to reach any cell with value x
 * 4. sufMinF[x] = suffix minimum - min cost to reach any cell with value >= x
 * (enables O(1) teleportation lookup: from value V, can teleport to any cell <=
 * V)
 * 
 * Teleportation trick: From cell with value V, we can teleport to any cell
 * with value <= V for free. sufMinF[V] gives the minimum cost to reach any
 * such cell in previous teleportation layer.
 */
public class MinimumCostPathWithTeleportations {

    public int minCost(int[][] grid, int k) {
        int m = grid.length;
        int n = grid[0].length;

        // Edge case: Can teleport directly if start >= end value
        if (k > 0 && grid[0][0] >= grid[m - 1][n - 1]) {
            return 0;
        }

        // Find maximum value in grid (for suffix array sizing)
        int mx = 0;
        for (int[] row : grid) {
            for (int x : row) {
                mx = Math.max(mx, x);
            }
        }

        // sufMinF[x] = min cost to reach any cell with value >= x (after teleportation)
        int[] sufMinF = new int[mx + 2];
        Arrays.fill(sufMinF, Integer.MAX_VALUE);

        // minF[x] = min cost to reach any cell with value exactly x
        int[] minF = new int[mx + 1];

        // f[j] = min cost to reach column j-1 in current row
        int[] f = new int[n + 1];

        // Process each teleportation layer (t = number of teleportations used)
        for (int t = 0; t <= k; t++) {
            Arrays.fill(minF, Integer.MAX_VALUE);
            Arrays.fill(f, Integer.MAX_VALUE / 2); // Avoid overflow when adding
            f[1] = -grid[0][0]; // Start position (will add grid[0][0] back)

            // Process grid row by row
            for (int[] row : grid) {
                for (int j = 0; j < n; j++) {
                    int x = row[j]; // Current cell value

                    // Option 1: Normal move (from left or above) + current cell cost
                    // Option 2: Teleport from previous layer (sufMinF[x] = min cost to reach value
                    // >= x)
                    f[j + 1] = Math.min(Math.min(f[j], f[j + 1]) + x, sufMinF[x]);

                    // Track minimum cost to reach each value
                    minF[x] = Math.min(minF[x], f[j + 1]);
                }
            }

            // Build suffix minimum for next teleportation layer
            // sufMinF[i] = min cost to reach any cell with value >= i
            boolean done = true;
            for (int i = mx; i >= 0; i--) {
                int mn = Math.min(sufMinF[i + 1], minF[i]);
                if (mn < sufMinF[i]) {
                    sufMinF[i] = mn;
                    done = false; // Made an update, may need more iterations
                }
            }

            // Early termination: no improvement means further teleportations won't help
            if (done) {
                break;
            }
        }

        return f[n];
    }

    public static void main(String[] args) {
        MinimumCostPathWithTeleportations solution = new MinimumCostPathWithTeleportations();

        // Test Case 1: Basic teleportation example
        int[][] grid1 = { { 1, 3, 3 }, { 2, 5, 4 }, { 4, 3, 5 } };
        int k1 = 2;
        int result1 = solution.minCost(grid1, k1);
        System.out.println("Test 1: Expected 7, Got: " + result1 + " " + (result1 == 7 ? "✅" : "❌"));

        // Test Case 2: Limited teleportation, prefer normal moves
        int[][] grid2 = { { 1, 2 }, { 2, 3 }, { 3, 4 } };
        int k2 = 1;
        int result2 = solution.minCost(grid2, k2);
        System.out.println("Test 2: Expected 9, Got: " + result2 + " " + (result2 == 9 ? "✅" : "❌"));

        // Test Case 3: Direct teleport when start >= end
        int[][] grid3 = { { 5, 1 }, { 1, 2 } };
        int k3 = 1;
        int result3 = solution.minCost(grid3, k3);
        System.out.println("Test 3: Expected 0, Got: " + result3 + " " + (result3 == 0 ? "✅" : "❌"));

        // Test Case 4: No teleportation allowed
        int[][] grid4 = { { 1, 2, 3 }, { 4, 5, 6 } };
        int k4 = 0;
        int result4 = solution.minCost(grid4, k4);
        System.out.println("Test 4: Expected 11, Got: " + result4 + " " + (result4 == 11 ? "✅" : "❌"));
    }
}
