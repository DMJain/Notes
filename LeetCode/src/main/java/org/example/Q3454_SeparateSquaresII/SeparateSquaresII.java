package org.example.Q3454_SeparateSquaresII;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

// Approach: Sweep Line Algorithm
// 1. Collect all Y-coordinates (bottom and top of squares) as events.
// 2. Sort events by Y.
// 3. Iterate through events, maintaining a set of "active x-intervals".
// 4. For each strip between y_i and y_{i+1}, calculate the length of the UNION of active X-intervals.
// 5. Accumulate area until we reach half the total area.
public class SeparateSquaresII {

    // Helper class to represent active X-intervals
    private static class Interval implements Comparable<Interval> {
        int start, end;

        Interval(int start, int end) {
            this.start = start;
            this.end = end;
        }

        // Needed for sort
        public int compareTo(Interval other) {
            if (this.start != other.start)
                return Integer.compare(this.start, other.start);
            return Integer.compare(this.end, other.end);
        }

        // Needed for removing specific objects from ArrayList
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            Interval interval = (Interval) o;
            return start == interval.start && end == interval.end;
        }
    }

    // Helper class for Sweep Line events
    private static class Event implements Comparable<Event> {
        int y;
        int type; // 1 for start (bottom), -1 for end (top)
        int xStart, xEnd;

        Event(int y, int type, int xStart, int xEnd) {
            this.y = y;
            this.type = type;
            this.xStart = xStart;
            this.xEnd = xEnd;
        }

        public int compareTo(Event other) {
            return Integer.compare(this.y, other.y);
        }
    }

    public double separateSquares(int[][] squares) {
        List<Event> sweepEvents = new ArrayList<>();
        for (int[] sq : squares) {
            int x = sq[0];
            int y = sq[1];
            int l = sq[2];
            sweepEvents.add(new Event(y, 1, x, x + l));
            sweepEvents.add(new Event(y + l, -1, x, x + l));
        }

        Collections.sort(sweepEvents);

        List<Interval> activeIntervals = new ArrayList<>();
        // Store strips as: [y_bottom, height, union_width]
        List<double[]> processedStrips = new ArrayList<>();

        double totalArea = 0;
        int prevY = sweepEvents.get(0).y;

        for (Event event : sweepEvents) {
            // Process the gap (strip) between the previous event and this one
            if (event.y > prevY) {
                double unionWidth = getUnionWidth(activeIntervals);
                double height = (double) event.y - prevY;

                if (unionWidth > 0) {
                    processedStrips.add(new double[] { prevY, height, unionWidth });
                    totalArea += height * unionWidth;
                }
            }

            // Update active intervals list
            Interval currentInterval = new Interval(event.xStart, event.xEnd);
            if (event.type == 1) {
                activeIntervals.add(currentInterval);
            } else {
                activeIntervals.remove(currentInterval);
            }

            prevY = event.y;
        }

        // Second Pass: Find the split point
        double targetArea = totalArea / 2.0;
        double accumulatedArea = 0;

        for (double[] strip : processedStrips) {
            double bottomY = strip[0];
            double height = strip[1];
            double width = strip[2];
            double stripArea = height * width;

            if (accumulatedArea + stripArea >= targetArea) {
                double missingArea = targetArea - accumulatedArea;
                return bottomY + (missingArea / width);
            }
            accumulatedArea += stripArea;
        }

        return 0.0;
    }

    // Brute force union width calculation: O(K log K) where K is active squares
    private double getUnionWidth(List<Interval> intervals) {
        if (intervals.isEmpty())
            return 0;

        // Create a copy to sort, so we don't mess up the main list order unnecessarily
        List<Interval> sorted = new ArrayList<>(intervals);
        Collections.sort(sorted);

        double unionLength = 0;
        double currentEnd = -1e18; // Negative infinity

        for (Interval iv : sorted) {
            if (iv.start >= currentEnd) {
                // Disjoint interval
                unionLength += (iv.end - iv.start);
                currentEnd = iv.end;
            } else if (iv.end > currentEnd) {
                // Overlapping interval
                unionLength += (iv.end - currentEnd);
                currentEnd = iv.end;
            }
        }
        return unionLength;
    }

    public static void main(String[] args) {
        SeparateSquaresII solver = new SeparateSquaresII();

        // Example 1: Disjoint squares
        int[][] squares1 = { { 0, 0, 1 }, { 2, 2, 1 } };
        System.out.println("Example 1: " + solver.separateSquares(squares1));
        // Expected: 1.0 (Area1=1, Area2=1, Total=2, Half=1. Line at 1.0 gives 1 below)

        // Example 2: Overlapping squares
        int[][] squares2 = { { 0, 0, 2 }, { 1, 1, 1 } };
        System.out.println("Example 2: " + solver.separateSquares(squares2));
        // S1: 2x2 at (0,0). Area 4.
        // S2: 1x1 at (1,1). Overlaps S1 completely.
        // Union at y=0..1: Width 2. Area 2.
        // Union at y=1..2: Width 2. Area 2.
        // Total Union Area: 4. Half: 2.
        // Line y=1.0 gives area 2. Correct.
    }
}
