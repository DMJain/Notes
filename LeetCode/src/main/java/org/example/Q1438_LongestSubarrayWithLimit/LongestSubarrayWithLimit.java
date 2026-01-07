package org.example.Q1438_LongestSubarrayWithLimit;

import java.util.ArrayDeque;
import java.util.Deque;

// Sliding Window + Monotonic Deques: Track min/max in window, shrink when diff > limit
public class LongestSubarrayWithLimit {

    public int longestSubarray(int[] nums, int limit) { // @viz:input
        Deque<Integer> maxq = new ArrayDeque<>(); // Monotonic decreasing: front = max
        Deque<Integer> minq = new ArrayDeque<>(); // Monotonic increasing: front = min
        int n = nums.length;
        int j = 0; // @viz:var(j) // Left pointer
        int ans = 0; // @viz:var(ans)

        for (int i = 0; i < n; i++) { // @viz:loop(i,nums)
            // @viz:highlight(nums,i)
            // Maintain maxq: remove smaller elements from back
            while (!maxq.isEmpty() && nums[i] > maxq.peekLast()) {
                maxq.pollLast();
            }
            maxq.addLast(nums[i]);

            // Maintain minq: remove larger elements from back
            while (!minq.isEmpty() && nums[i] < minq.peekLast()) {
                minq.pollLast();
            }
            minq.addLast(nums[i]);

            // If window invalid (max - min > limit), shrink from left
            if (maxq.peekFirst() - minq.peekFirst() > limit) {
                if (nums[j] == maxq.peekFirst())
                    maxq.pollFirst();
                if (nums[j] == minq.peekFirst())
                    minq.pollFirst();
                j++;
            }

            ans = Math.max(ans, i - j + 1);
        }
        return ans; // @viz:result(Longest subarray length)
    }

    public static void main(String[] args) {
        LongestSubarrayWithLimit solution = new LongestSubarrayWithLimit();

        System.out.println(solution.longestSubarray(new int[] { 8, 2, 4, 7 }, 4)); // Output: 2
    }
}
