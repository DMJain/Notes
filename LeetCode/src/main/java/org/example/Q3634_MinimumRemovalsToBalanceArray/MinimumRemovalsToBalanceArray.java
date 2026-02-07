package org.example.Q3634_MinimumRemovalsToBalanceArray;

import java.util.Arrays;

/**
 * LeetCode 3634 - Minimum Removals to Balance Array
 * 
 * Key Insight: After sorting, valid balanced subsets become CONTIGUOUS
 * subarrays.
 * Use Two Pointers to find the longest such subarray efficiently.
 */
public class MinimumRemovalsToBalanceArray {

    /**
     * Optimal: Sorting + Two Pointers
     * 
     * After sorting: min is at left, max is at right of any window.
     * Expand right pointer, shrink left when nums[j] > nums[i] * k.
     * 
     * Time: O(n log n) - sorting dominates
     * Space: O(log n) - in-place sort
     */
    public int minRemoval(int[] nums, int k) {
        // Sort: valid subsets become contiguous blocks
        Arrays.sort(nums);

        int i = 0; // Left pointer (min element)
        int maxLen = 0; // Track longest balanced window

        for (int j = 0; j < nums.length; j++) {
            // Shrink left while condition violated
            // Use long to prevent overflow: 10^9 * 10^5 = 10^14 > INT_MAX
            while ((long) nums[j] > (long) nums[i] * k) {
                i++;
            }
            // Current window [i, j] is valid
            maxLen = Math.max(maxLen, j - i + 1);
        }

        // Min removals = total - max kept
        return nums.length - maxLen;
    }

    public static void main(String[] args) {
        MinimumRemovalsToBalanceArray solution = new MinimumRemovalsToBalanceArray();

        // Test 1: [2,1,5], k=2 → Remove 5 → Answer: 1
        System.out.println("Test 1: " + solution.minRemoval(new int[] { 2, 1, 5 }, 2)); // Expected: 1

        // Test 2: [1,6,2,9], k=3 → Remove 1,9 → Answer: 2
        System.out.println("Test 2: " + solution.minRemoval(new int[] { 1, 6, 2, 9 }, 3)); // Expected: 2

        // Test 3: [4,6], k=2 → Already balanced → Answer: 0
        System.out.println("Test 3: " + solution.minRemoval(new int[] { 4, 6 }, 2)); // Expected: 0
    }
}
