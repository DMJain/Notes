package org.example.Q0744_FindSmallestLetterGreaterThanTarget;

/**
 * 744. Find Smallest Letter Greater Than Target
 * 
 * Uses Binary Search to find the ceiling (first letter > target).
 * Key insight: We need to find the FIRST element STRICTLY GREATER than target.
 * This is a classic "upper bound" binary search pattern.
 */
public class FindSmallestLetterGreaterThanTarget {

    /**
     * Binary Search approach - O(log n) time, O(1) space
     * 
     * The algorithm finds the smallest letter greater than target.
     * If target >= largest letter, wraps around to return first letter.
     */
    public char nextGreatestLetter(char[] letters, char target) {
        // Edge case: if target is >= last letter, wrap around
        if (letters[letters.length - 1] <= target) {
            return letters[0];
        }

        int l = 0;
        int r = letters.length - 1;

        // Binary search for first element > target
        while (r >= l) {
            int mid = l + (r - l) / 2;

            if (letters[mid] > target) {
                // Found a candidate, but check if there's a smaller one to the left
                r = mid - 1;
            } else {
                // letters[mid] <= target, need to go right
                l = mid + 1;
            }
        }

        // l now points to the first element > target
        // Modulo handles wrap-around (though edge case above already handles this)
        return letters[l % letters.length];
    }

    public static void main(String[] args) {
        FindSmallestLetterGreaterThanTarget solution = new FindSmallestLetterGreaterThanTarget();

        // Test Case 1: target < all elements
        char[] letters1 = { 'c', 'f', 'j' };
        System.out.println("Test 1: " + solution.nextGreatestLetter(letters1, 'a')); // Expected: c

        // Test Case 2: target equals an element
        char[] letters2 = { 'c', 'f', 'j' };
        System.out.println("Test 2: " + solution.nextGreatestLetter(letters2, 'c')); // Expected: f

        // Test Case 3: target > all elements (wrap around)
        char[] letters3 = { 'x', 'x', 'y', 'y' };
        System.out.println("Test 3: " + solution.nextGreatestLetter(letters3, 'z')); // Expected: x

        // Test Case 4: target between elements
        char[] letters4 = { 'c', 'f', 'j' };
        System.out.println("Test 4: " + solution.nextGreatestLetter(letters4, 'd')); // Expected: f

        // Test Case 5: duplicates with target matching
        char[] letters5 = { 'e', 'e', 'e', 'k', 'q', 'q', 'q', 'v', 'v', 'y' };
        System.out.println("Test 5: " + solution.nextGreatestLetter(letters5, 'e')); // Expected: k
    }
}
