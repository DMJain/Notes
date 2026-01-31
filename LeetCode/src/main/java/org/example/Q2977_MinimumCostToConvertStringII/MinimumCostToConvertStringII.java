package org.example.Q2977_MinimumCostToConvertStringII;

import java.util.Arrays;

/**
 * LeetCode 2977 - Minimum Cost to Convert String II (Hard)
 * 
 * Uses Trie + Floyd-Warshall + Dynamic Programming to find minimum conversion
 * cost.
 * 
 * Key Concepts:
 * 1. Trie: Efficiently maps variable-length strings to unique integer IDs
 * 2. Floyd-Warshall: Precomputes shortest path between all string pairs
 * 3. DP: Finds optimal way to convert source to target with non-overlapping
 * substrings
 */
public class MinimumCostToConvertStringII {

    // Standard Trie Node
    class TrieNode {
        TrieNode[] next = new TrieNode[26];
        int id = -1; // Unique ID for this string (-1 if not a complete word)
    }

    private int uniqueIDCounter = 0;

    public long minimumCost(String source, String target, String[] original, String[] changed, int[] cost) {
        // 1. Build Trie and Map Strings to Integer IDs
        TrieNode root = new TrieNode();
        for (String s : original)
            insert(root, s);
        for (String s : changed)
            insert(root, s);

        // 2. Initialize Distance Matrix (Graph)
        int numNodes = uniqueIDCounter;
        long[][] dist = new long[numNodes][numNodes];
        for (int i = 0; i < numNodes; i++) {
            Arrays.fill(dist[i], Long.MAX_VALUE);
            dist[i][i] = 0; // Distance to itself is 0
        }

        // Fill initial costs from transformation rules
        for (int i = 0; i < cost.length; i++) {
            int u = getID(root, original[i]);
            int v = getID(root, changed[i]);
            dist[u][v] = Math.min(dist[u][v], (long) cost[i]);
        }

        // 3. Floyd-Warshall (All-Pairs Shortest Path)
        // Try each node k as intermediate node in path i -> k -> j
        for (int k = 0; k < numNodes; k++) {
            for (int i = 0; i < numNodes; i++) {
                if (dist[i][k] == Long.MAX_VALUE)
                    continue; // Skip if no path i->k
                for (int j = 0; j < numNodes; j++) {
                    if (dist[k][j] == Long.MAX_VALUE)
                        continue; // Skip if no path k->j
                    dist[i][j] = Math.min(dist[i][j], dist[i][k] + dist[k][j]);
                }
            }
        }

        // 4. Dynamic Programming - dp[i] = min cost to convert source[0..i-1] to
        // target[0..i-1]
        int n = source.length();
        long[] dp = new long[n + 1];
        Arrays.fill(dp, Long.MAX_VALUE);
        dp[0] = 0; // Empty prefix costs 0

        for (int i = 0; i < n; i++) {
            if (dp[i] == Long.MAX_VALUE)
                continue; // Can't reach this state

            // Option A: Character Match (Free) - if chars are same, no cost needed
            if (source.charAt(i) == target.charAt(i)) {
                dp[i + 1] = Math.min(dp[i + 1], dp[i]);
            }

            // Option B: Trie Search for Substring Replacement
            // Try all substrings starting at position i
            TrieNode p1 = root; // Pointer for source substring
            TrieNode p2 = root; // Pointer for target substring

            for (int j = i; j < n; j++) {
                int charS = source.charAt(j) - 'a';
                int charT = target.charAt(j) - 'a';

                p1 = p1.next[charS];
                p2 = p2.next[charT];

                // If either path falls off the Trie, we can't match further
                if (p1 == null || p2 == null)
                    break;

                // If both pointers point to valid word IDs, check if conversion exists
                if (p1.id != -1 && p2.id != -1) {
                    if (dist[p1.id][p2.id] != Long.MAX_VALUE) {
                        dp[j + 1] = Math.min(dp[j + 1], dp[i] + dist[p1.id][p2.id]);
                    }
                }
            }
        }

        return dp[n] == Long.MAX_VALUE ? -1 : dp[n];
    }

    /**
     * Inserts a string into the Trie, assigning a unique ID if it's a new word
     */
    private void insert(TrieNode root, String s) {
        TrieNode node = root;
        for (char c : s.toCharArray()) {
            if (node.next[c - 'a'] == null) {
                node.next[c - 'a'] = new TrieNode();
            }
            node = node.next[c - 'a'];
        }
        if (node.id == -1) { // Only assign ID if this is a new word
            node.id = uniqueIDCounter++;
        }
    }

    /**
     * Returns the unique ID of a string in the Trie
     */
    private int getID(TrieNode root, String s) {
        TrieNode node = root;
        for (char c : s.toCharArray()) {
            node = node.next[c - 'a'];
        }
        return node.id;
    }

    public static void main(String[] args) {
        MinimumCostToConvertStringII solution = new MinimumCostToConvertStringII();

        // Example 1: Single-char transformations with chaining
        String source1 = "abcd";
        String target1 = "acbe";
        String[] original1 = { "a", "b", "c", "c", "e", "d" };
        String[] changed1 = { "b", "c", "b", "e", "b", "e" };
        int[] cost1 = { 2, 5, 5, 1, 2, 20 };
        System.out.println("Example 1: " + solution.minimumCost(source1, target1, original1, changed1, cost1));
        // Expected: 28

        // Reset counter for new instance needed
        solution = new MinimumCostToConvertStringII();

        // Example 2: Multi-char substring transformations
        String source2 = "abcdefgh";
        String target2 = "acdeeghh";
        String[] original2 = { "bcd", "fgh", "thh" };
        String[] changed2 = { "cde", "thh", "ghh" };
        int[] cost2 = { 1, 3, 5 };
        System.out.println("Example 2: " + solution.minimumCost(source2, target2, original2, changed2, cost2));
        // Expected: 9

        solution = new MinimumCostToConvertStringII();

        // Example 3: Impossible due to overlapping constraints
        String source3 = "abcdefgh";
        String target3 = "addddddd";
        String[] original3 = { "bcd", "defgh" };
        String[] changed3 = { "ddd", "ddddd" };
        int[] cost3 = { 100, 1578 };
        System.out.println("Example 3: " + solution.minimumCost(source3, target3, original3, changed3, cost3));
        // Expected: -1
    }
}
