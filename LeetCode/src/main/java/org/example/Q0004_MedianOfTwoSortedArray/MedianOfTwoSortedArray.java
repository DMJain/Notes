package org.example.Q0004_MedianOfTwoSortedArray;

// Binary Search on smaller array to partition both arrays such that left half <= right half
public class MedianOfTwoSortedArray {
    public static double findMedianSortedArrays(int[] nums1, int[] nums2) {
        int m = nums1.length;
        int n = nums2.length;
        // Always binary search on smaller array for O(log(min(m,n)))
        if (m > n)
            return findMedianSortedArrays(nums2, nums1);

        int partition = (m + n + 1) / 2; // Total elements needed in left half
        int low = 0; // Binary search bounds on nums1
        int high = m;

        while (low <= high) {
            int cut1 = (low + high) / 2; // Elements taken from nums1 in left half
            int cut2 = partition - cut1; // Remaining elements needed from nums2

            // Edge cases: if cut is at boundary, use MIN/MAX to handle empty partitions
            int l1 = (cut1 == 0) ? Integer.MIN_VALUE : nums1[cut1 - 1]; // Max of left in nums1
            int r1 = (cut1 == m) ? Integer.MAX_VALUE : nums1[cut1]; // Min of right in nums1
            int l2 = (cut2 == 0) ? Integer.MIN_VALUE : nums2[cut2 - 1]; // Max of left in nums2
            int r2 = (cut2 == n) ? Integer.MAX_VALUE : nums2[cut2]; // Min of right in nums2

            // Valid partition: all left elements <= all right elements
            if (l1 <= r2 && l2 <= r1) {
                int total = m + n;
                int maxLeft = Math.max(l1, l2); // Largest in left half
                int maxRight = Math.min(r1, r2); // Smallest in right half
                // Even total: median = avg(maxLeft, minRight). Odd: median = maxLeft
                return (total % 2 == 0) ? (double) (0.5 * (maxLeft + maxRight)) : (double) (maxLeft);
            } else if (l1 > r2) {
                high = cut1 - 1; // Too many from nums1, move left
            } else {
                low = cut1 + 1; // Too few from nums1, move right
            }
        }
        return (double) 0.0;
    }

    public static void main(String[] args) {
        int[] num1 = new int[] { 1, 2 };
        int[] num2 = new int[] { 3, 4 };

        System.out.println(findMedianSortedArrays(num1, num2));
    }
}
