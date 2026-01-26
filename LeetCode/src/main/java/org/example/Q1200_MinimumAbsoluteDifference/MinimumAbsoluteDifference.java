package org.example.Q1200_MinimumAbsoluteDifference;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MinimumAbsoluteDifference {
    public List<List<Integer>> minimumAbsDifference(int[] arr) {
        Arrays.sort(arr); // Sort to ensure minimum difference is between adjacent elements

        List<List<Integer>> ans = new ArrayList<>();

        // Initialize with the first pair
        int abs = arr[1] - arr[0];
        ans.add(new ArrayList<>(List.of(arr[0], arr[1])));

        for (int i = 2; i < arr.length; i++) {
            int temp = arr[i] - arr[i - 1];

            // If we find a smaller difference, discard not-so-optimal previous pairs
            if (temp < abs) {
                ans = new ArrayList<>();
                abs = temp;
                ans.add(new ArrayList<>(List.of(arr[i - 1], arr[i])));
            }
            // If we find the same minimum difference, append to the list
            else if (temp == abs) {
                ans.add(new ArrayList<>(List.of(arr[i - 1], arr[i])));
            }
        }
        return ans;
    }

    public static void main(String[] args) {
        MinimumAbsoluteDifference solution = new MinimumAbsoluteDifference();

        // Example 1
        int[] arr1 = { 4, 2, 1, 3 };
        System.out.println(solution.minimumAbsDifference(arr1));
        // Output: [[1, 2], [2, 3], [3, 4]]

        // Example 2
        int[] arr2 = { 1, 3, 6, 10, 15 };
        System.out.println(solution.minimumAbsDifference(arr2));
        // Output: [[1, 3]]

        // Example 3
        int[] arr3 = { 3, 8, -10, 23, 19, -4, -14, 27 };
        System.out.println(solution.minimumAbsDifference(arr3)); // Output: [[-14, -10], [19, 23], [23, 27]]
    }
}
