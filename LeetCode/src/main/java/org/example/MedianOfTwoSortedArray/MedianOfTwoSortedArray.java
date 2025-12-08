package org.example.MedianOfTwoSortedArray;

public class MedianOfTwoSortedArray {
    public static double findMedianSortedArrays(int[] nums1, int[] nums2) {
        int m = nums1.length;
        int n = nums2.length;
        if (m > n)
            return findMedianSortedArrays(nums2, nums1);

        int partition = (m + n + 1) / 2;                                // 2
        int low = 0;                                                    // 0 | 2
        int high = m;                                                   // 2 | 2
        while (low <= high) {                                           // T | T
            int cut1 = (low + high) / 2;                                // 1 | 2
            int cut2 = partition - cut1;                                // 1 | 0
            int l1 = (cut1 == 0) ? Integer.MIN_VALUE : nums1[cut1 - 1]; // 1 | 2
            int r1 = (cut1 == m) ? Integer.MAX_VALUE : nums1[cut1];     // 2 | MAX
            int l2 = (cut2 == 0) ? Integer.MIN_VALUE : nums2[cut2 - 1]; // 3 | MIN
            int r2 = (cut2 == n) ? Integer.MAX_VALUE : nums2[cut2];     // 4 | 3
            if (l1 <= r2 && l2 <= r1) {
                int total = m + n;
                int maxLeft = Math.max(l1, l2);
                int maxRight = Math.min(r1, r2);
                return (total % 2 == 0) ? (double) (0.5 * (maxLeft + maxRight)) : (double) (maxLeft);
            } else if (l1 > r2) {
                high = cut1 - 1;
            } else {
                low = cut1 + 1;
            }
        }
        return (double) 0.0;
    }

    public static void main(String[] args) {
        int[] num1 = new int[]{1,2};
        int[] num2 = new int[]{3,4};

        System.out.println(findMedianSortedArrays(num1, num2));
    }
}
