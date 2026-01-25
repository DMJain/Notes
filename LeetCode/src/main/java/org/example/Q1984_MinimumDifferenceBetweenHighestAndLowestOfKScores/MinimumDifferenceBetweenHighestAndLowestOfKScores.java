package org.example.Q1984_MinimumDifferenceBetweenHighestAndLowestOfKScores;

import java.util.Arrays;

public class MinimumDifferenceBetweenHighestAndLowestOfKScores {

    public int minimumDifference(int[] nums, int k) {
        if (k == 1)
            return 0;
        Arrays.sort(nums);
        int minDiff = Integer.MAX_VALUE;
        for (int i = 0; i <= nums.length - k; i++) {
            int currentDiff = nums[i + k - 1] - nums[i];
            minDiff = Math.min(minDiff, currentDiff);
        }
        return minDiff;
    }

    public static void main(String[] args) {
        MinimumDifferenceBetweenHighestAndLowestOfKScores solution = new MinimumDifferenceBetweenHighestAndLowestOfKScores();

        // Test case 1
        int[] nums1 = { 90 };
        System.out.println("Test 1: " + solution.minimumDifference(nums1, 1)); // Expected: 0

        // Test case 2
        int[] nums2 = { 9, 4, 1, 7 };
        System.out.println("Test 2: " + solution.minimumDifference(nums2, 2)); // Expected: 2

        // Test case 3: Larger example
        int[] nums3 = { 1, 4, 7, 9, 10, 15 };
        System.out.println("Test 3 (k=3): " + solution.minimumDifference(nums3, 3)); // Expected: 3 ([7,9,10])

        // Test case 4: All same elements
        int[] nums4 = { 5, 5, 5, 5 };
        System.out.println("Test 4: " + solution.minimumDifference(nums4, 2)); // Expected: 0
    }
}
