package org.example.Q0514_FreedomTrail;

import java.util.ArrayList;
import java.util.List;

// DP + Memoization: For each key char, find min rotation from all matching ring positions
public class FreedomTrail {

    @SuppressWarnings("unchecked")
    public int findRotateSteps(String ring, String key) { // @viz:input
        char[] r = ring.toCharArray();
        List<Integer>[] positions = new List[26]; // @viz:var(positions) // positions[i] = indices of char 'a'+i

        // Precompute positions of each character in the ring
        for (int i = 0; i < r.length; i++) { // @viz:loop(i,ring)
            int c = r[i] - 'a';
            if (positions[c] == null)
                positions[c] = new ArrayList<>();
            positions[c].add(i);
        }

        // dp[i][j] = min steps to spell key[i:] when ring position is j
        int[][] dp = new int[key.length()][r.length];

        return helper(0, 0, positions, key.toCharArray(), r, dp); // @viz:result(Min rotation steps)
    }

    // Returns min steps to spell key[index:] starting from ring position 'pos'
    int helper(int index, int pos, List<Integer>[] positions, char[] key, char[] ring, int[][] dp) {
        if (index == key.length)
            return 0; // All characters spelled

        if (dp[index][pos] > 0)
            return dp[index][pos]; // Return cached result

        char target = key[index];
        List<Integer> possiblePositions = positions[target - 'a'];

        int minSteps = Integer.MAX_VALUE;

        for (int nextPos : possiblePositions) {
            // Calculate rotation distance: min of clockwise and anticlockwise
            int steps = Math.min(Math.abs(nextPos - pos), ring.length - Math.abs(nextPos - pos));

            // Recursively solve for remaining key characters
            int totalSteps = steps + helper(index + 1, nextPos, positions, key, ring, dp);

            minSteps = Math.min(minSteps, totalSteps);
        }

        return dp[index][pos] = minSteps + 1; // +1 for pressing the button
    }

    public static void main(String[] args) {
        FreedomTrail solution = new FreedomTrail();

        System.out.println(solution.findRotateSteps("godding", "gd")); // Output: 4
    }
}
