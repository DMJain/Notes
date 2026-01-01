package org.example.Q0001_TwoSum;

import java.util.Arrays;
import java.util.HashMap;

// HashMap approach: Store complement (target - num) to find pair in O(1) lookup
public class TwoSum {
    public static int[] twoSum(int[] nums, int target) {
        int[] ans = new int[2];
        HashMap<Integer, Integer> map = new HashMap<>(); // Stores number -> index mapping
        for (int i = 0; i < nums.length; i++) {
            // Check if complement (target - current number) exists in map
            if (map.containsKey(target - nums[i])) {
                ans[0] = map.get(target - nums[i]); // Index of complement
                ans[1] = i; // Current index
                return ans;
            }
            map.put(nums[i], i); // Store current number with its index for future lookups
        }

        return ans;
    }

    public static void main(String[] args) {
        int[] numArray1 = { 2, 7, 11, 15 };
        int target1 = 9;

        System.out.println(Arrays.toString(twoSum(numArray1, target1)));
    }
}
