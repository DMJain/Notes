package org.example.Q3453_SeparateSquaresI;

// Binary Search on Answer
// The function "Area Below(y)" is monotonically increasing.
// We want to find y such that AreaBelow(y) == TotalArea / 2.
public class SeparateSquaresI {

    public double separateSquares(int[][] squares) {
        double totalArea = 0;
        double low = 2e9; // Initialize with a large value
        double high = 0;

        // 1. Calculate Total Area and initial bounds
        for (int[] sq : squares) {
            double y = sq[1];
            double l = sq[2];

            // Cast to double BEFORE multiplying to prevent Integer Overflow
            totalArea += l * l;

            low = Math.min(low, y);
            high = Math.max(high, y + l);
        }

        double targetArea = totalArea / 2.0;

        // 2. Binary Search with fixed iterations (sufficient for precision)
        // 100 iterations reduces the range by 2^100, which gives extreme precision.
        for (int i = 0; i < 100; i++) {
            double mid = low + (high - low) / 2.0;

            if (calculateAreaBelow(squares, mid) >= targetArea) {
                high = mid; // Area is sufficient, try to lower the line
            } else {
                low = mid; // Area is too small, need to raise the line
            }
        }

        return high;
    }

    // Helper function to calculate area below the line 'currentY'
    private double calculateAreaBelow(int[][] squares, double currentY) {
        double area = 0;
        for (int[] sq : squares) {
            double y = sq[1];
            double l = sq[2];
            double top = y + l;

            if (y >= currentY) {
                // Case 1: Square is completely above the line -> Area below is 0
                continue;
            } else if (top <= currentY) {
                // Case 2: Square is completely below the line -> Full area contributes
                area += l * l;
            } else {
                // Case 3: Line cuts through the square
                // We take the width (l) * the height of the bottom portion (currentY - y)
                area += l * (currentY - y);
            }
        }
        return area;
    }

    public static void main(String[] args) {
        SeparateSquaresI solver = new SeparateSquaresI();

        int[][] example1 = { { 0, 0, 1 }, { 2, 2, 1 } };
        System.out.println("Example 1: " + solver.separateSquares(example1)); // Expected ~1.00000

        int[][] example2 = { { 0, 0, 2 }, { 1, 1, 1 } };
        System.out.println("Example 2: " + solver.separateSquares(example2)); // Expected ~1.16667
    }
}
