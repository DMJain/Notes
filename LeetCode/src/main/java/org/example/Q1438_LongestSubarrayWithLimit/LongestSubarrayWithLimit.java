package org.example.Q1438_LongestSubarrayWithLimit;

import java.util.ArrayDeque;
import java.util.Deque;

// Sliding Window + Monotonic Deques: Track min/max in window, shrink when diff > limit
public class LongestSubarrayWithLimit {

    public int longestSubarray(int[] nums, int limit) {
        Deque<Integer> maxq = new ArrayDeque<>(); // Monotonic decreasing: front = max
        Deque<Integer> minq = new ArrayDeque<>(); // Monotonic increasing: front = min
        int n = nums.length;
        int j = 0; // Left pointer
        int ans = 0;

        for (int i = 0; i < n; i++) {
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
        return ans;
    }

    public static void main(String[] args) {
        LongestSubarrayWithLimit solution = new LongestSubarrayWithLimit();

        System.out.println(solution.longestSubarray(new int[] { 8, 2, 4, 7 }, 4)); // Output: 2
        System.out.println(solution.longestSubarray(new int[] { 10, 1, 2, 4, 7, 2 }, 5)); // Output: 4
        System.out.println(solution.longestSubarray(new int[] { 4, 2, 2, 2, 4, 4, 2, 2 }, 0)); // Output: 3
    }
}
