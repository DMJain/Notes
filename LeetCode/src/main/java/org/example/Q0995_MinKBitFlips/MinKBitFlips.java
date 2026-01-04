package org.example.Q0995_MinKBitFlips;

// Greedy + Sliding Window: Track flip state with XOR, flip greedily when current bit is 0
public class MinKBitFlips {

    public int minKBitFlips(int[] nums, int k) {
        int n = nums.length;
        int flipped = 0; // Current flip state (0 = even flips, 1 = odd flips)
        int res = 0;
        int[] isFlipped = new int[n]; // isFlipped[i] = 1 if we started a flip at index i

        for (int i = 0; i < n; i++) {
            // Remove the effect of flip that started k positions ago (slides out of window)
            if (i >= k) {
                flipped ^= isFlipped[i - k];
            }

            // Current effective value = nums[i] XOR flipped
            // If effective value is 0, we need to flip
            if (flipped == nums[i]) {
                // Can't flip if window would exceed array
                if (i + k > n) {
                    return -1;
                }
                // Start a flip at position i
                isFlipped[i] = 1;
                flipped ^= 1;
                res++;
            }
        }
        return res;
    }

    public static void main(String[] args) {
        MinKBitFlips solution = new MinKBitFlips();

        System.out.println(solution.minKBitFlips(new int[] { 0, 1, 0 }, 1)); // Output: 2
        System.out.println(solution.minKBitFlips(new int[] { 1, 1, 0 }, 2)); // Output: -1
        System.out.println(solution.minKBitFlips(new int[] { 0, 0, 0, 1, 0, 1, 1, 0 }, 3)); // Output: 3
    }
}
