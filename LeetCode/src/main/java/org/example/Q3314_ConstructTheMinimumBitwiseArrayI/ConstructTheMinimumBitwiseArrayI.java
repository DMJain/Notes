package org.example.Q3314_ConstructTheMinimumBitwiseArrayI;

import java.util.Arrays;
import java.util.List;

public class ConstructTheMinimumBitwiseArrayI {

    /**
     * For each prime number n in nums:
     * - If n is even: impossible → return -1
     * - If n is odd: ans = n & ~(((n + 1) & ~n) >> 1)
     * 
     * The key insight is that we need to find the lowest set bit pattern
     * that allows x | (x+1) = n
     */
    public int[] minBitwiseArray(List<Integer> nums) {
        int[] res = new int[nums.size()];
        for (int i = 0; i < nums.size(); i++) {
            int n = nums.get(i);
            if ((n & 1) == 1)
                res[i] = n & ~(((n + 1) & ~n) >> 1);
            else
                res[i] = -1;
        }
        return res;
    }

    public static void main(String[] args) {
        ConstructTheMinimumBitwiseArrayI solver = new ConstructTheMinimumBitwiseArrayI();

        // Example 1
        List<Integer> nums1 = Arrays.asList(2, 3, 5, 7);
        System.out.println("Test 1: " + Arrays.toString(solver.minBitwiseArray(nums1)));
        // Expected: [-1, 1, 4, 3]

        // Example 2
        List<Integer> nums2 = Arrays.asList(11, 13, 31);
        System.out.println("Test 2: " + Arrays.toString(solver.minBitwiseArray(nums2)));
        // Expected: [9, 12, 15]
    }
}
