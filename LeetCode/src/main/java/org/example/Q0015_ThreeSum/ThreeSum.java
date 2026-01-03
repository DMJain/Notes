package org.example.Q0015_ThreeSum;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Sort + Two Pointers: Fix one element, use two pointers to find pairs that sum to its negative
public class ThreeSum {

    public List<List<Integer>> threeSum(int[] nums) {
        List<List<Integer>> ans = new ArrayList<>();
        Arrays.sort(nums); // Sort for two-pointer technique and duplicate skipping

        for (int i = 0; i < nums.length - 1; i++) {
            // Optimization: if nums[i] > 0, no three positive numbers can sum to 0
            if (nums[i] > 0)
                break;

            // Skip duplicates for first element
            if (i > 0 && nums[i - 1] == nums[i])
                continue;

            int l = i + 1;
            int r = nums.length - 1;

            while (l < r) {
                int threeSum = nums[i] + nums[l] + nums[r];

                if (threeSum > 0) {
                    r--; // Sum too big, need smaller number
                } else if (threeSum < 0) {
                    l++; // Sum too small, need bigger number
                } else {
                    // Found a triplet!
                    ans.add(new ArrayList<>(Arrays.asList(nums[i], nums[l], nums[r])));
                    l++;

                    // Skip duplicates for second element
                    while (nums[l] == nums[l - 1] && l < r) {
                        l++;
                    }
                }
            }
        }

        return ans;
    }

    public static void main(String[] args) {
        ThreeSum solution = new ThreeSum();

        System.out.println(solution.threeSum(new int[] { -1, 0, 1, 2, -1, -4 }));
        // Output: [[-1,-1,2],[-1,0,1]]

        System.out.println(solution.threeSum(new int[] { 0, 1, 1 }));
        // Output: []

        System.out.println(solution.threeSum(new int[] { 0, 0, 0 }));
        // Output: [[0,0,0]]
    }
}
