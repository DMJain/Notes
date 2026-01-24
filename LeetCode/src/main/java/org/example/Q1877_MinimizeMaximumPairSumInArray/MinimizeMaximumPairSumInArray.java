package org.example.Q1877_MinimizeMaximumPairSumInArray;

import java.util.Arrays;

public class MinimizeMaximumPairSumInArray {

    /**
     * Optimal Solution: Sort + Two Pointer
     * Time: O(n log n) for sorting
     * Space: O(1) or O(n) depending on sort implementation
     */
    public int minPairSum(int[] nums) {
        Arrays.sort(nums);
        int maxSum = 0;
        int left = 0, right = nums.length - 1;

        while (left < right) {
            maxSum = Math.max(maxSum, nums[left++] + nums[right--]);
        }

        return maxSum;
    }

    public static void main(String[] args) {
        MinimizeMaximumPairSumInArray solution = new MinimizeMaximumPairSumInArray();

        // Example 1: Expected output = 7
        int[] nums1 = { 3, 5, 2, 3 };
        System.out.println("Example 1: " + solution.minPairSum(nums1)); // 7

        // Example 2: Expected output = 8
        int[] nums2 = { 3, 5, 4, 2, 4, 6 };
        System.out.println("Example 2: " + solution.minPairSum(nums2)); // 8

        // Edge case: Two elements
        int[] nums3 = { 1, 5 };
        System.out.println("Edge case (2 elements): " + solution.minPairSum(nums3)); // 6

        // Case with all same elements
        int[] nums4 = { 4, 4, 4, 4 };
        System.out.println("Same elements: " + solution.minPairSum(nums4)); // 8
    }
}
