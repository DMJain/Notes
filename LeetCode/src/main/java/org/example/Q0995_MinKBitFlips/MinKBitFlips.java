package org.example.Q0995_MinKBitFlips;

// Greedy + Sliding Window: Track flip state with XOR, flip greedily when current bit is 0
public class MinKBitFlips {

    public int minKBitFlips(int[] nums, int k) { // @viz:input
        int n = nums.length;
        int flipped = 0; // @viz:var(flipped) // Current flip state (0 = even, 1 = odd)
        int res = 0; // @viz:var(res)
        int[] isFlipped = new int[n]; // @viz:array(isFlipped) // isFlipped[i] = 1 if flip started at i

        for (int i = 0; i < n; i++) { // @viz:loop(i,nums)
            // Remove the effect of flip that started k positions ago (slides out of window)
            if (i >= k) {
                flipped ^= isFlipped[i - k];
            }

            // Current effective value = nums[i] XOR flipped
            // If effective value is 0, we need to flip
            if (flipped == nums[i]) {
                // Can't flip if window would exceed array
                if (i + k > n) {
                    return -1; // @viz:result(Impossible)
                }
                // Start a flip at position i
                isFlipped[i] = 1;
                // @viz:highlight(nums,i)
                flipped ^= 1;
                res++;
            }
        }
        return res; // @viz:result(Min flips)
    }

    public static void main(String[] args) {
        MinKBitFlips solution = new MinKBitFlips();

        System.out.println(solution.minKBitFlips(new int[] { 0, 1, 0 }, 1)); // Output: 2
    }
}
