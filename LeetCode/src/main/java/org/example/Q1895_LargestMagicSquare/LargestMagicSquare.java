package org.example.Q1895_LargestMagicSquare;

/**
 * LeetCode 1895 - Largest Magic Square
 * 
 * Approach: Prefix Sums + Check from largest k down
 * - Use row prefix sums and column prefix sums to compute row/column sums in
 * O(1)
 * - Start from the largest possible k (min of rows, cols) and work down
 * - For each potential square top-left (r, c), verify all row sums, column
 * sums, and both diagonals
 * - Return immediately when first valid magic square is found (guarantees
 * largest)
 * 
 * Time: O(m * n * min(m,n)^2) - For each square we check O(k) rows, columns,
 * and diagonals
 * Space: O(m * n) - For prefix sum arrays
 */
public class LargestMagicSquare {

    public int largestMagicSquare(int[][] grid) {
        int n = grid.length, m = grid[0].length;
        long[][] rowPrefix = new long[n][m + 1];
        long[][] colPrefix = new long[n + 1][m];

        // Prefix sum for rows
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                rowPrefix[i][j + 1] = rowPrefix[i][j] + grid[i][j];
            }
        }

        // Prefix sum for columns
        for (int j = 0; j < m; j++) {
            for (int i = 0; i < n; i++) {
                colPrefix[i + 1][j] = colPrefix[i][j] + grid[i][j];
            }
        }

        // Iterate from max possible size k down to 2
        for (int k = Math.min(n, m); k > 1; k--) {
            for (int r = 0; r <= n - k; r++) {
                for (int c = 0; c <= m - k; c++) {

                    // Target sum = first row sum
                    long target = rowPrefix[r][c + k] - rowPrefix[r][c];
                    boolean isMagic = true;

                    // Check all row sums
                    for (int i = 0; i < k; i++) {
                        long currRowSum = rowPrefix[r + i][c + k] - rowPrefix[r + i][c];
                        if (currRowSum != target) {
                            isMagic = false;
                            break;
                        }
                    }
                    if (!isMagic)
                        continue;

                    // Check all column sums
                    for (int j = 0; j < k; j++) {
                        long currColSum = colPrefix[r + k][c + j] - colPrefix[r][c + j];
                        if (currColSum != target) {
                            isMagic = false;
                            break;
                        }
                    }
                    if (!isMagic)
                        continue;

                    // Check both diagonals
                    long d1 = 0, d2 = 0;
                    for (int i = 0; i < k; i++) {
                        d1 += grid[r + i][c + i]; // Top-left to bottom-right
                        d2 += grid[r + i][c + k - 1 - i]; // Top-right to bottom-left
                    }

                    if (d1 == target && d2 == target) {
                        return k; // Found largest magic square!
                    }
                }
            }
        }

        return 1; // 1x1 is always a magic square
    }

    public static void main(String[] args) {
        LargestMagicSquare solution = new LargestMagicSquare();

        // Test case 1
        int[][] grid1 = {
                { 7, 1, 4, 5, 6 },
                { 2, 5, 1, 6, 4 },
                { 1, 5, 4, 3, 2 },
                { 1, 2, 7, 3, 4 }
        };
        System.out.println("Test 1: " + solution.largestMagicSquare(grid1)); // Expected: 3

        // Test case 2
        int[][] grid2 = {
                { 5, 1, 3, 1 },
                { 9, 3, 3, 1 },
                { 1, 3, 3, 8 }
        };
        System.out.println("Test 2: " + solution.largestMagicSquare(grid2)); // Expected: 2
    }
}
