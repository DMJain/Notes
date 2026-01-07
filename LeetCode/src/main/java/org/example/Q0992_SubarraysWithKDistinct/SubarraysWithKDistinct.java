package org.example.Q0992_SubarraysWithKDistinct;

import java.util.HashMap;
import java.util.Map;

// Sliding Window with prefix count: Track shrinkable start positions
public class SubarraysWithKDistinct {

    public int subarraysWithKDistinct(int[] nums, int k) { // @viz:input
        int res = 0; // @viz:var(res)
        int prefix = 0; // @viz:var(prefix)
        int start = 0; // @viz:var(start)
        int distinctCount = 0; // @viz:var(distinctCount)
        Map<Integer, Integer> countMap = new HashMap<>(); // @viz:map(countMap)

        for (int right = 0; right < nums.length; right++) { // @viz:loop(right,nums)
            int rightNum = nums[right]; // @viz:var(rightNum)

            // Add new element - if it's a new distinct number, increment count
            if (!countMap.containsKey(rightNum) || countMap.get(rightNum) == 0) {
                distinctCount++;
            }
            countMap.put(rightNum, countMap.getOrDefault(rightNum, 0) + 1);

            // If too many distinct, shrink window (mandatory shrink)
            if (distinctCount > k) {
                int startNum = nums[start];
                start++;
                prefix = 0; // Reset prefix since we broke the chain
                countMap.put(startNum, countMap.get(startNum) - 1);
                distinctCount--;
            }

            // Shrink from left while we have duplicates (optional shrink for counting)
            while (countMap.get(nums[start]) > 1) {
                int startNum = nums[start++];
                countMap.put(startNum, countMap.get(startNum) - 1);
                prefix++; // Each shrink adds a valid subarray starting point
            }

            // If we have exactly k distinct, count all valid subarrays
            if (distinctCount == k) {
                res += prefix + 1; // +1 for current window, +prefix for all shrunk variants
            }
        }
        return res; // @viz:result(Subarray count)
    }

    public static void main(String[] args) {
        SubarraysWithKDistinct solution = new SubarraysWithKDistinct();

        System.out.println(solution.subarraysWithKDistinct(new int[] { 1, 2, 1, 2, 3 }, 2)); // Output: 7
    }
}
