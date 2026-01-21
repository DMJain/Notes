package org.example.Q1292_MaximumSideLengthOfASquareWithSumLessThanOrEqualToThreshold;

public class MaximumSideLengthOfASquareWithSumLessThanOrEqualToThreshold {
    public int maxSideLength(int[][] mat, int threshold) {
        int m = mat.length;
        int n = mat[0].length;

        // Prefix Sum Matrix: P[i][j] stores the sum of the rectangle from (0,0) to
        // (i-1, j-1)
        int[][] P = new int[m + 1][n + 1];

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                P[i][j] = mat[i - 1][j - 1] + P[i - 1][j] + P[i][j - 1] - P[i - 1][j - 1];
            }
        }

        int maxSide = 0;

        // Iterate through all possible bottom-right corners (i, j)
        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                // Try to extend the current max side length
                int len = maxSide + 1;

                if (i >= len && j >= len) {
                    // Top-left corner coordinates (1-based index for P)
                    int r1 = i - len + 1;
                    int c1 = j - len + 1;

                    // Calculate sum of square side length `len` ending at (i, j)
                    int total = P[i][j] - P[r1 - 1][j] - P[i][c1 - 1] + P[r1 - 1][c1 - 1];

                    if (total <= threshold) {
                        maxSide++;
                    }
                }
            }
        }

        return maxSide;
    }

    public static void main(String[] args) {
        MaximumSideLengthOfASquareWithSumLessThanOrEqualToThreshold solver = new MaximumSideLengthOfASquareWithSumLessThanOrEqualToThreshold();

        int[][] mat1 = { { 1, 1, 3, 2, 4, 3, 2 }, { 1, 1, 3, 2, 4, 3, 2 }, { 1, 1, 3, 2, 4, 3, 2 } };
        int threshold1 = 4;
        System.out.println("Example 1: " + solver.maxSideLength(mat1, threshold1)); // Expected: 2

        int[][] mat2 = { { 2, 2, 2, 2, 2 }, { 2, 2, 2, 2, 2 }, { 2, 2, 2, 2, 2 }, { 2, 2, 2, 2, 2 },
                { 2, 2, 2, 2, 2 } };
        int threshold2 = 1;
        System.out.println("Example 2: " + solver.maxSideLength(mat2, threshold2)); // Expected: 0
    }
}
