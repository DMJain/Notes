package org.example.Q0526_BeautifulArrangement;

// Backtracking: Try placing each value at each position, prune invalid placements early
public class BeautifulArrangement {

    private int result = 0; // @viz:var(result)

    public int countArrangement(int n) { // @viz:input
        int[] nums = new int[n + 1]; // @viz:array(nums) // nums[i] = value at position i
        dfs(nums, 1, n);
        return result; // @viz:result(Beautiful arrangements count)
    }

    // Try to place 'val' at some valid position
    private void dfs(int[] nums, int val, int n) {
        // Base case: all values 1..n are placed
        if (val > n) {
            result++;
            return;
        }

        // Try placing 'val' at each position i
        for (int i = 1; i <= n; i++) {
            // Position i is empty AND (val divisible by i OR i divisible by val)
            if (nums[i] == 0 && (val % i == 0 || i % val == 0)) {
                nums[i] = val; // Place val at position i
                // @viz:highlight(nums,i)
                dfs(nums, val + 1, n); // Try placing next value
                nums[i] = 0; // Backtrack: remove val from position i
            }
        }
    }

    public static void main(String[] args) {
        BeautifulArrangement solution = new BeautifulArrangement();

        System.out.println(solution.countArrangement(3)); // Output: 3
    }
}
