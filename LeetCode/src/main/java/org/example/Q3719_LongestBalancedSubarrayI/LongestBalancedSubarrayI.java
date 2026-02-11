package org.example.Q3719_LongestBalancedSubarrayI;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 3719. Longest Balanced Subarray I
 *
 * Approach: O(n²) enumeration with incremental diff tracking
 * - For each starting index l, extend right pointer r
 * - Track diff = distinctEven - distinctOdd using a HashSet
 * - Only update diff when element is NEW (not yet seen)
 * - diff == 0 means balanced → update maxLen
 * - Early termination: skip l if remaining length < maxLen
 *
 * Time: O(n²) — two nested loops
 * Space: O(n) — HashSet stores up to n elements
 */
public class LongestBalancedSubarrayI {

    public int longestBalanced(int[] nums) {
        int n = nums.length;
        Set<Integer> seen = new HashSet<>();
        int maxLen = 0;

        for (int l = 0; l < n; l++) {
            // Early termination: remaining elements can't beat current best
            if (l > n - maxLen)
                break;

            int diff = 0;
            for (int r = l; r < n; r++) {
                int x = nums[r];
                if (!seen.contains(x)) {
                    // +1 for new even, -1 for new odd
                    diff += (x % 2 == 0) ? 1 : -1;
                    seen.add(x);
                }
                // diff == 0 means equal distinct evens and odds
                if (diff == 0) {
                    maxLen = Math.max(maxLen, r - l + 1);
                }
            }
            seen.clear(); // Reset for next starting index
        }
        return maxLen;
    }

    // ==================== TEST CASES ====================
    public static void main(String[] args) {
        LongestBalancedSubarrayI solution = new LongestBalancedSubarrayI();

        // Example 1: 2 distinct evens {2,4}, 2 distinct odds {5,3}
        int[] test1 = { 2, 5, 4, 3 };
        System.out.println("Example 1: " + Arrays.toString(test1));
        System.out.println("Expected: 4, Got: " + solution.longestBalanced(test1));

        // Example 2: Full array balanced (dup 2 doesn't add to distinct count)
        int[] test2 = { 3, 2, 2, 5, 4 };
        System.out.println("\nExample 2: " + Arrays.toString(test2));
        System.out.println("Expected: 5, Got: " + solution.longestBalanced(test2));

        // Example 3: Subarray [2,3,2] has 1 distinct even, 1 distinct odd
        int[] test3 = { 1, 2, 3, 2 };
        System.out.println("\nExample 3: " + Arrays.toString(test3));
        System.out.println("Expected: 3, Got: " + solution.longestBalanced(test3));

        // Edge: Single element → can't have both even and odd
        int[] test4 = { 1 };
        System.out.println("\nEdge 1 (single): " + Arrays.toString(test4));
        System.out.println("Expected: 0, Got: " + solution.longestBalanced(test4));

        // Edge: All same parity → never balanced
        int[] test5 = { 2, 4, 6 };
        System.out.println("\nEdge 2 (all even): " + Arrays.toString(test5));
        System.out.println("Expected: 0, Got: " + solution.longestBalanced(test5));

        // Edge: Minimal balanced pair
        int[] test6 = { 1, 2 };
        System.out.println("\nEdge 3 (minimal): " + Arrays.toString(test6));
        System.out.println("Expected: 2, Got: " + solution.longestBalanced(test6));
    }
}
