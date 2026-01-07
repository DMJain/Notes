package org.example.Q1423_MaxPointsFromCards;

// Sliding Window: Take k from left first, then swap one-by-one with right side
public class MaxPointsFromCards {

    public int maxScore(int[] cardPoints, int k) { // @viz:input
        // Start by taking all k cards from the left
        int currSum = 0; // @viz:var(currSum)
        for (int i = 0; i < k; i++) { // @viz:loop(i,cardPoints)
            currSum += cardPoints[i];
        }

        int maxSum = currSum; // @viz:var(maxSum)
        int r = cardPoints.length - 1; // @viz:var(r) // Pointer to rightmost card

        // Swap: remove one from left, add one from right
        for (int i = k - 1; i >= 0; i--) {
            // @viz:highlight(cardPoints,i)
            // @viz:highlight(cardPoints,r)
            currSum += cardPoints[r] - cardPoints[i]; // Add right, remove left
            maxSum = Math.max(maxSum, currSum);
            r--;
        }

        return maxSum; // @viz:result(Max points)
    }

    public static void main(String[] args) {
        MaxPointsFromCards solution = new MaxPointsFromCards();

        System.out.println(solution.maxScore(new int[] { 1, 2, 3, 4, 5, 6, 1 }, 3)); // Output: 12
    }
}
