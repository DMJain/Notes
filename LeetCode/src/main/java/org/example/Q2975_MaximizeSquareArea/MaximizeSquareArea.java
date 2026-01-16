package org.example.Q2975_MaximizeSquareArea;

import java.util.Arrays;
import java.util.HashSet;

public class MaximizeSquareArea {

    public int maximizeSquareArea(int m, int n, int[] hFences, int[] vFences) {
        if (m == n) {
            return (m - 1) * (n - 1);
        }
        int[] h = new int[hFences.length + 2];
        int[] v = new int[vFences.length + 2];

        h[0] = 1;
        h[h.length - 1] = m;
        v[0] = 1;
        v[v.length - 1] = n;

        for (int i = 0; i < hFences.length; i++) {
            h[i + 1] = hFences[i];
        }
        for (int i = 0; i < vFences.length; i++) {
            v[i + 1] = vFences[i];
        }

        Arrays.sort(h);
        Arrays.sort(v);
        HashSet<Integer> set = new HashSet<>();

        for (int i = 0; i < h.length; i++) {
            for (int j = i + 1; j < h.length; j++) {
                set.add(h[j] - h[i]);
            }
        }
        int side = Integer.MIN_VALUE;
        for (int i = 0; i < v.length; i++) {
            for (int j = i + 1; j < v.length; j++) {
                if (set.contains(v[j] - v[i])) {
                    side = Math.max(side, v[j] - v[i]);
                }
            }
        }

        if (side == Integer.MIN_VALUE)
            return -1;
        long mod = 1000000007;
        return (int) ((1L * side * side) % mod);
    }

    public static void main(String[] args) {
        MaximizeSquareArea solver = new MaximizeSquareArea();

        // Example 1
        System.out.println("Test 1: " + solver.maximizeSquareArea(4, 3, new int[] { 2, 3 }, new int[] { 2 })); // Expected:
                                                                                                               // 4

        // Example 2
        System.out.println("Test 2: " + solver.maximizeSquareArea(6, 7, new int[] { 2 }, new int[] { 4 })); // Expected:
                                                                                                            // -1
    }
}
