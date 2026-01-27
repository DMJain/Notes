package org.example.Q3650_MinimumCostPathWithEdgeReversals;

import java.util.*;

/**
 * LeetCode 3650 - Minimum Cost Path with Edge Reversals
 * 
 * Key Insight: Model edge reversals by adding reverse edges with 2x cost.
 * For each edge u → v with weight w:
 * - Add normal edge: u → v with cost w
 * - Add reverse edge: v → u with cost 2w (represents using the "switch" to
 * reverse)
 * 
 * This transforms into standard Dijkstra's shortest path problem.
 */
public class MinimumCostPathWithEdgeReversals {

    public int minCost(int n, int[][] edges) {
        // Build adjacency list with both original and reversed edges
        List<int[]>[] graph = new ArrayList[n];
        for (int i = 0; i < n; i++) {
            graph[i] = new ArrayList<>();
        }

        for (int[] e : edges) {
            int u = e[0], v = e[1], w = e[2];
            graph[u].add(new int[] { v, w }); // Original edge: u → v, cost w
            graph[v].add(new int[] { u, 2 * w }); // Reversed edge: v → u, cost 2w
        }

        // Dijkstra's algorithm
        long[] dist = new long[n];
        Arrays.fill(dist, Long.MAX_VALUE);
        dist[0] = 0;

        // Priority queue: [cost, node]
        PriorityQueue<long[]> pq = new PriorityQueue<>(Comparator.comparingLong(a -> a[0]));
        pq.offer(new long[] { 0, 0 });

        while (!pq.isEmpty()) {
            long[] cur = pq.poll();
            long cost = cur[0];
            int node = (int) cur[1];

            // Skip if we've found a better path
            if (cost > dist[node])
                continue;

            // Explore neighbors
            for (int[] nxt : graph[node]) {
                int v = nxt[0];
                int w = nxt[1];
                if (dist[v] > cost + w) {
                    dist[v] = cost + w;
                    pq.offer(new long[] { dist[v], v });
                }
            }
        }

        return dist[n - 1] == Long.MAX_VALUE ? -1 : (int) dist[n - 1];
    }

    public static void main(String[] args) {
        MinimumCostPathWithEdgeReversals solution = new MinimumCostPathWithEdgeReversals();

        // Test Case 1
        int n1 = 4;
        int[][] edges1 = { { 0, 1, 3 }, { 3, 1, 1 }, { 2, 3, 4 }, { 0, 2, 2 } };
        int result1 = solution.minCost(n1, edges1);
        System.out.println("Test 1: Expected 5, Got: " + result1 + " " + (result1 == 5 ? "✅" : "❌"));

        // Test Case 2
        int n2 = 4;
        int[][] edges2 = { { 0, 2, 1 }, { 2, 1, 1 }, { 1, 3, 1 }, { 2, 3, 3 } };
        int result2 = solution.minCost(n2, edges2);
        System.out.println("Test 2: Expected 3, Got: " + result2 + " " + (result2 == 3 ? "✅" : "❌"));

        // Edge case: Direct path
        int n3 = 2;
        int[][] edges3 = { { 0, 1, 5 } };
        int result3 = solution.minCost(n3, edges3);
        System.out.println("Test 3: Expected 5, Got: " + result3 + " " + (result3 == 5 ? "✅" : "❌"));

        // Edge case: Need reversal
        int n4 = 2;
        int[][] edges4 = { { 1, 0, 3 } }; // Only edge goes wrong direction
        int result4 = solution.minCost(n4, edges4);
        System.out.println("Test 4: Expected 6, Got: " + result4 + " " + (result4 == 6 ? "✅" : "❌"));
    }
}
