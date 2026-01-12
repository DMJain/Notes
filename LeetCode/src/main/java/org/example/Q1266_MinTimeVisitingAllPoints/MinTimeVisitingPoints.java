package org.example.Q1266_MinTimeVisitingAllPoints;

// Math / Geometry: Chebyshev distance. Max(abs(dx), abs(dy)) is the time to move between two points.
public class MinTimeVisitingPoints {

    public int minTimeToVisitAllPoints(int[][] points) {
        int ans = 0;

        for (int i = 1; i < points.length; i++) {
            // Calculate absolute difference in x and y coordinates
            int dx = Math.abs(points[i][0] - points[i - 1][0]);
            int dy = Math.abs(points[i][1] - points[i - 1][1]);

            // The time required is the maximum of dx and dy because diagonal moves satisfy
            // both dx and dy simultaneously.
            ans += Math.max(dx, dy);
        }

        return ans;
    }

    public static void main(String[] args) {
        MinTimeVisitingPoints solution = new MinTimeVisitingPoints();

        int[][] points1 = { { 1, 1 }, { 3, 4 }, { -1, 0 } };
        System.out.println(solution.minTimeToVisitAllPoints(points1)); // Output: 7

        int[][] points2 = { { 3, 2 }, { -2, 2 } };
        System.out.println(solution.minTimeToVisitAllPoints(points2)); // Output: 5
    }
}
