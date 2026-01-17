package org.example.Q3047_FindLargestAreaOfSquareInsideTwoRectangles;

public class FindLargestAreaOfSquareInsideTwoRectangles {

    public long largestSquareArea(int[][] bottomLeft, int[][] topRight) {
        long ans = 0;
        int n = bottomLeft.length;
        for (int i = 0; i < n; i++) {
            int[] firstRectBL = bottomLeft[i];
            int[] firstRectTR = topRight[i];
            for (int j = i + 1; j < n; j++) {
                int[] secondRectBL = bottomLeft[j];
                int[] secondRectTR = topRight[j];
                
                // Check if they are disjoint
                if (secondRectBL[0] >= firstRectTR[0] || secondRectTR[0] <= firstRectBL[0]) continue;
                if (secondRectTR[1] <= firstRectBL[1] || secondRectBL[1] >= firstRectTR[1]) continue;
                
                // Calculate Intersection Rectangle
                // Max of lefts, Min of rights
                int pntAx = Math.max(firstRectBL[0], secondRectBL[0]);
                int pntAy = Math.max(firstRectBL[1], secondRectBL[1]);
                int pntBx = Math.min(firstRectTR[0], secondRectTR[0]);
                int pntBy = Math.min(firstRectTR[1], secondRectTR[1]);
                
                int sideA = pntBx - pntAx;
                int sideB = pntBy - pntAy;
                
                // The largest square that fits has side = min(width, height)
                long side = Math.min(sideA, sideB);
                if (side > 0) {
                    ans = Math.max(ans, side);
                }
            }
        }
        return ans * ans;
    }

    public static void main(String[] args) {
        FindLargestAreaOfSquareInsideTwoRectangles solver = new FindLargestAreaOfSquareInsideTwoRectangles();

        // Example 1
        int[][] bl1 = {{1,1},{2,2},{3,1}};
        int[][] tr1 = {{3,3},{4,4},{6,6}};
        System.out.println("Test 1: " + solver.largestSquareArea(bl1, tr1)); // Expected: 1

        // Example 2
        int[][] bl2 = {{1,1},{1,3},{1,5}};
        int[][] tr2 = {{5,5},{5,7},{5,9}};
        System.out.println("Test 2: " + solver.largestSquareArea(bl2, tr2)); // Expected: 4
        
        // Example 3
        int[][] bl3 = {{1,1},{2,2},{1,2}};
        int[][] tr3 = {{3,3},{4,4},{3,4}};
        System.out.println("Test 3: " + solver.largestSquareArea(bl3, tr3)); // Expected: 1
        
        // Example 4
        int[][] bl4 = {{1,1},{3,3},{3,1}};
        int[][] tr4 = {{2,2},{4,4},{4,2}};
        System.out.println("Test 4: " + solver.largestSquareArea(bl4, tr4)); // Expected: 0
    }
}
