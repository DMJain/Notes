package org.example.Q1752_CheckIfArrayIsSortedAndRotated;

/**
 * LeetCode 1752 - Check if Array Is Sorted and Rotated
 * 
 * Key Insight: A sorted rotated array has at most ONE break point
 * where nums[i-1] > nums[i]. Additionally, the wrap-around
 * (nums[last] > nums[first]) counts as a break too.
 */
public class CheckIfArrayIsSortedAndRotated {

    /**
     * Counts "break points" where order is violated.
     * For a valid sorted-rotated array: breakPoints <= 1
     */
    public boolean check(int[] nums) {
        int breakPoints = 0;

        // Count breaks in the middle
        for (int i = 1; i < nums.length; i++) {
            if (nums[i - 1] > nums[i]) {
                breakPoints++;
            }
        }

        // Check wrap-around: last element > first element is also a break
        if (nums[nums.length - 1] > nums[0]) {
            breakPoints++;
        }

        return breakPoints <= 1;
    }

    public static void main(String[] args) {
        CheckIfArrayIsSortedAndRotated solution = new CheckIfArrayIsSortedAndRotated();

        // Test Case 1: Rotated sorted array
        int[] test1 = { 3, 4, 5, 1, 2 };
        System.out.println("Test 1: " + java.util.Arrays.toString(test1));
        System.out.println("Expected: true, Got: " + solution.check(test1));

        // Test Case 2: Not a rotated sorted array
        int[] test2 = { 2, 1, 3, 4 };
        System.out.println("\nTest 2: " + java.util.Arrays.toString(test2));
        System.out.println("Expected: false, Got: " + solution.check(test2));

        // Test Case 3: Already sorted (0 rotation)
        int[] test3 = { 1, 2, 3 };
        System.out.println("\nTest 3: " + java.util.Arrays.toString(test3));
        System.out.println("Expected: true, Got: " + solution.check(test3));

        // Test Case 4: Single element
        int[] test4 = { 1 };
        System.out.println("\nTest 4: " + java.util.Arrays.toString(test4));
        System.out.println("Expected: true, Got: " + solution.check(test4));

        // Test Case 5: With duplicates
        int[] test5 = { 2, 2, 2, 1, 2 };
        System.out.println("\nTest 5: " + java.util.Arrays.toString(test5));
        System.out.println("Expected: true, Got: " + solution.check(test5));

        // Test Case 6: All same elements
        int[] test6 = { 1, 1, 1 };
        System.out.println("\nTest 6: " + java.util.Arrays.toString(test6));
        System.out.println("Expected: true, Got: " + solution.check(test6));
    }
}
