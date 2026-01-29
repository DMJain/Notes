package org.example.Q2976_MinimumCostToConvertStringI;

import java.util.Arrays;

/**
 * 2976. Minimum Cost to Convert String I
 * 
 * Approach: Floyd-Warshall All-Pairs Shortest Path
 * - Model 26 lowercase letters as nodes in a graph
 * - Transformation rules are weighted directed edges
 * - Precompute minimum cost between all 26×26 pairs
 * - Sum up costs for each differing position
 * 
 * Time: O(26³ + n) ≈ O(n)
 * Space: O(26²) = O(1)
 */
public class MinimumCostToConvertStringI {

    public long minimumCost(String source, String target, char[] original, char[] changed, int[] cost) {
        // dist[i][j] = minimum cost to convert character i to character j
        long[][] dist = new long[26][26];
        long INF = Long.MAX_VALUE / 2; // Use half to avoid overflow in addition

        // Initialize: all pairs unreachable except self
        for (long[] row : dist) {
            Arrays.fill(row, INF);
        }
        for (int i = 0; i < 26; i++) {
            dist[i][i] = 0;
        }

        // Build initial edges from transformation rules
        // Handle duplicates by keeping minimum cost
        for (int i = 0; i < original.length; i++) {
            int u = original[i] - 'a';
            int v = changed[i] - 'a';
            dist[u][v] = Math.min(dist[u][v], cost[i]);
        }

        // Floyd-Warshall: find shortest paths via intermediate nodes
        for (int k = 0; k < 26; k++) {
            for (int i = 0; i < 26; i++) {
                if (dist[i][k] == INF)
                    continue; // Optimization: skip unreachable
                for (int j = 0; j < 26; j++) {
                    if (dist[k][j] != INF) {
                        dist[i][j] = Math.min(dist[i][j], dist[i][k] + dist[k][j]);
                    }
                }
            }
        }

        // Calculate total conversion cost
        long totalCost = 0;
        int n = source.length();

        for (int i = 0; i < n; i++) {
            int u = source.charAt(i) - 'a';
            int v = target.charAt(i) - 'a';
            if (u == v)
                continue; // Same character, no cost
            if (dist[u][v] == INF)
                return -1; // Unreachable
            totalCost += dist[u][v];
        }

        return totalCost;
    }

    public static void main(String[] args) {
        MinimumCostToConvertStringI solution = new MinimumCostToConvertStringI();

        // Example 1: Multi-hop paths
        System.out.println("Test 1: " + solution.minimumCost(
                "abcd", "acbe",
                new char[] { 'a', 'b', 'c', 'c', 'e', 'd' },
                new char[] { 'b', 'c', 'b', 'e', 'b', 'e' },
                new int[] { 2, 5, 5, 1, 2, 20 })); // Expected: 28

        // Example 2: Same transformation repeated
        System.out.println("Test 2: " + solution.minimumCost(
                "aaaa", "bbbb",
                new char[] { 'a', 'c' },
                new char[] { 'c', 'b' },
                new int[] { 1, 2 })); // Expected: 12

        // Example 3: Unreachable character
        System.out.println("Test 3: " + solution.minimumCost(
                "abcd", "abce",
                new char[] { 'a' },
                new char[] { 'e' },
                new int[] { 10000 })); // Expected: -1
    }
}
