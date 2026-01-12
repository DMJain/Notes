package org.example.Q1007_MinimumDominoRotations;

// Greedy Approach: The target value MUST be either tops[0] or bottoms[0].
// Time: O(N), Space: O(1)
public class MinimumDominoRotations {

    public int minDominoRotations(int[] tops, int[] bottoms) {
        // Step 1: Try to make all values equal to tops[0]
        int result = check(tops[0], tops, bottoms);

        // Step 2: If tops[0] works, return result.
        // If it failed (-1) OR if we found a valid rotation count, we still need to
        // check bottoms[0]
        // because bottoms[0] might give a SMALLER number of rotations.
        // Wait, actually: if tops[0] works, does bottoms[0] offer a better solution?
        // If tops[0] == bottoms[0], it's the same check.
        // If tops[0] != bottoms[0], we should check both and take the valid one.
        // The problem asks for MINIMUM rotations.
        // However, usually if a solution exists for X, checking X gives the min
        // rotations for X.
        // If solutions exist for both X and Y? Impossible unless all tiles are
        // identical X/X or X/Y?
        // Actually, if tops[0] works, we return it. If not, we try bottoms[0].
        // This is based on the logic that if a solution exists, the target MUST be at
        // index 0.

        if (result != -1)
            return result;

        // Step 3: If tops[0] failed, try bottoms[0]
        return check(bottoms[0], tops, bottoms);
    }

    // Step 4: Helper function to count rotations to make all values = target
    private int check(int target, int[] tops, int[] bottoms) {
        int rotateTop = 0; // Rotations needed to bring target to top
        int rotateBottom = 0; // Rotations needed to bring target to bottom

        // Step 5: Loop through all dominoes
        for (int i = 0; i < tops.length; i++) {
            // Step 6: If target is not on either side, it's impossible for this target
            if (tops[i] != target && bottoms[i] != target) {
                return -1;
            }

            // Step 7: If top doesn't have the target, it must be rotated (if we want top
            // row to correspond to target)
            // But wait, 'rotateTop' means "rotations to make TOP row all targets".
            // So if tops[i] != target, we MUST swap from bottom (which we confirmed has
            // target).
            if (tops[i] != target) {
                rotateTop++;
            }

            // Step 8: If bottom doesn't have the target, it must be rotated (if we want
            // bottom row to correspond to target)
            if (bottoms[i] != target) {
                rotateBottom++;
            }
        }

        // Step 9: Return the minimum of the two rotation counts
        // If we want TOP row to be target, cost is rotateTop.
        // If we want BOTTOM row to be target, cost is rotateBottom.
        return Math.min(rotateTop, rotateBottom);
    }

    public static void main(String[] args) {
        MinimumDominoRotations solver = new MinimumDominoRotations();

        int[] tops1 = { 2, 1, 2, 4, 2, 2 };
        int[] bottoms1 = { 5, 2, 6, 2, 3, 2 };
        System.out.println("Test 1: " + solver.minDominoRotations(tops1, bottoms1)); // Expected: 2

        int[] tops2 = { 3, 5, 1, 2, 3 };
        int[] bottoms2 = { 3, 6, 3, 3, 4 };
        System.out.println("Test 2: " + solver.minDominoRotations(tops2, bottoms2)); // Expected: -1
    }
}
