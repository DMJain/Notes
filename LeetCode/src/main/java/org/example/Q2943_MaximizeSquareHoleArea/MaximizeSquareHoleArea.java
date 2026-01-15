package org.example.Q2943_MaximizeSquareHoleArea;

import java.util.Arrays;

public class MaximizeSquareHoleArea {

    public int maximizeSquareHoleArea(int n, int m, int[] hBars, int[] vBars) {
        Arrays.sort(hBars);
        Arrays.sort(vBars);

        int hx = 1;
        int l = 0;
        for (int i = 1; i < hBars.length; i++) {
            if (hBars[i - 1] + 1 == hBars[i]) {
                hx = Math.max(hx, i - l + 1);
            } else {
                l = i;
            }
        }

        int vx = 1;
        l = 0;
        for (int i = 1; i < vBars.length; i++) {
            if (vBars[i - 1] + 1 == vBars[i]) {
                vx = Math.max(vx, i - l + 1);
            } else {
                l = i;
            }
        }

        l = Math.min(hx + 1, vx + 1);
        return l * l;
    }

    public static void main(String[] args) {
        MaximizeSquareHoleArea solver = new MaximizeSquareHoleArea();

        // Example 1
        System.out.println("Test 1: " + solver.maximizeSquareHoleArea(2, 1, new int[] { 2, 3 }, new int[] { 2 })); // Expected:
                                                                                                                   // 4

        // Example 2
        System.out.println("Test 2: " + solver.maximizeSquareHoleArea(1, 1, new int[] { 2 }, new int[] { 2 })); // Expected:
                                                                                                                // 4

        // Example 3
        System.out.println("Test 3: " + solver.maximizeSquareHoleArea(2, 3, new int[] { 2, 3 }, new int[] { 2, 4 })); // Expected:
                                                                                                                      // 4
    }
}
