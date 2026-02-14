package org.example.Q0799_ChampagneTower;

/**
 * 799. Champagne Tower
 *
 * Approach: Bottom-Up Simulation DP
 *
 * Model the pyramid as a 2D array. Pour all champagne into A[0][0].
 * Process row by row, top to bottom: if a glass has more than 1.0 cup,
 * cap it at 1.0 and split the excess equally to the two children below.
 *
 * Why 102×102? query_row can be up to 99, so r+1 can reach 100.
 * Size 102 avoids any off-by-one worries.
 *
 * Time: O(row²) — process at most 1+2+...+(row+1) glasses
 * Space: O(row²) — the 2D array (fixed 102×102 ≈ 83 KB)
 */
public class ChampagneTower {

    public double champagneTower(int poured, int query_row, int query_glass) {
        double[][] A = new double[102][102];
        A[0][0] = (double) poured;

        // Process each row top→bottom, propagating overflow
        for (int r = 0; r <= query_row; r++) {
            for (int c = 0; c <= r; c++) {
                if (A[r][c] > 1.0) {
                    // Excess splits equally to the two children below
                    double excess = (A[r][c] - 1.0) / 2.0;
                    A[r][c] = 1.0; // cap this glass at full
                    A[r + 1][c] += excess; // left child
                    A[r + 1][c + 1] += excess; // right child
                }
            }
        }

        return A[query_row][query_glass];
    }

    // ==================== TEST CASES ====================
    public static void main(String[] args) {
        ChampagneTower sol = new ChampagneTower();

        // Example 1: poured=1, no overflow → glass (1,1) is empty
        System.out.println("Example 1: poured=1, row=1, glass=1");
        System.out.println("Expected: 0.0, Got: " + sol.champagneTower(1, 1, 1));

        // Example 2: poured=2, 1 cup overflow splits → each glass in row 1 = 0.5
        System.out.println("\nExample 2: poured=2, row=1, glass=1");
        System.out.println("Expected: 0.5, Got: " + sol.champagneTower(2, 1, 1));

        // Example 3: massive pour, deep row → glass is full
        System.out.println("\nExample 3: poured=100000009, row=33, glass=17");
        System.out.println("Expected: 1.0, Got: " + sol.champagneTower(100000009, 33, 17));

        // Edge: nothing poured
        System.out.println("\nEdge 1: poured=0, row=0, glass=0");
        System.out.println("Expected: 0.0, Got: " + sol.champagneTower(0, 0, 0));

        // Walkthrough example: poured=4, row=2
        System.out.println("\nWalkthrough: poured=4, row=2, glass=0");
        System.out.println("Expected: 0.25, Got: " + sol.champagneTower(4, 2, 0));

        System.out.println("\nWalkthrough: poured=4, row=2, glass=1");
        System.out.println("Expected: 0.5, Got: " + sol.champagneTower(4, 2, 1));
    }
}
