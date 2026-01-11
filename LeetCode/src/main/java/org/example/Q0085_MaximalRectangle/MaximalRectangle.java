package org.example.Q0085_MaximalRectangle;

import java.util.Stack;

// Histogram Approach: Treat each row as a base for a histogram, then use "Largest Rectangle in Histogram" logic (Monotonic Stack)
public class MaximalRectangle {

    public int maximalRectangle(char[][] matrix) {
        if (matrix == null || matrix.length == 0 || matrix[0].length == 0)
            return 0;

        int rows = matrix.length;
        int cols = matrix[0].length;

        // Heights array representing the histogram for the current row
        int[] heights = new int[cols];
        int maxArea = 0;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                // If it's '1', increase height; if '0', reset height to 0
                if (matrix[i][j] == '1') {
                    heights[j] += 1;
                } else {
                    heights[j] = 0;
                }
            }

            // Calculate max area for the current row's histogram
            maxArea = Math.max(maxArea, largestCommonArea(heights));
        }

        return maxArea;
    }

    // Solves "Largest Rectangle in Histogram" using Monotonic Stack
    private int largestCommonArea(int[] heights) {
        int n = heights.length;
        int maxA = 0;
        Stack<Integer> stack = new Stack<>();

        for (int i = 0; i <= n; i++) {
            // Use 0 as a sentinel value at the end to pop remaining bars
            int h = (i == n) ? 0 : heights[i];

            // Maintain monotonic increasing stack
            while (!stack.isEmpty() && h < heights[stack.peek()]) {
                int height = heights[stack.pop()];
                int width = stack.isEmpty() ? i : i - stack.peek() - 1;
                maxA = Math.max(maxA, height * width);
            }
            stack.push(i);
        }
        return maxA;
    }

    public static void main(String[] args) {
        MaximalRectangle solution = new MaximalRectangle();

        char[][] matrix1 = {
                { '1', '0', '1', '0', '0' },
                { '1', '0', '1', '1', '1' },
                { '1', '1', '1', '1', '1' },
                { '1', '0', '0', '1', '0' }
        };
        System.out.println(solution.maximalRectangle(matrix1)); // Output: 6

        char[][] matrix2 = { { '0' } };
        System.out.println(solution.maximalRectangle(matrix2)); // Output: 0

        char[][] matrix3 = { { '1' } };
        System.out.println(solution.maximalRectangle(matrix3)); // Output: 1
    }
}
