package org.example.p3_Java_Advance_Concept.c7_queue_interface.impl;

import org.example.p3_Java_Advance_Concept.c7_queue_interface.contracts.IQueueDemo;
import java.util.*;

/**
 * Demonstrates PriorityQueue (min-heap and max-heap).
 */
public class PriorityQueueDemo implements IQueueDemo {

    @Override
    public String getDemoName() {
        return "PriorityQueue Demo";
    }

    @Override
    public void execute() {
        System.out.println("┌─────────────────────────────────────────────────────────┐");
        System.out.println("│                  PRIORITYQUEUE DEMO                      │");
        System.out.println("└─────────────────────────────────────────────────────────┘");
        System.out.println();

        demonstrateMinHeap();
        demonstrateMaxHeap();
        demonstrateIteratorGotcha();
    }

    private void demonstrateMinHeap() {
        System.out.println("📉 MIN-HEAP (default)");
        System.out.println("─────────────────────────────────────────────");

        PriorityQueue<Integer> minHeap = new PriorityQueue<>();
        minHeap.addAll(Arrays.asList(30, 10, 50, 20, 40));

        System.out.println("   Added: 30, 10, 50, 20, 40");
        System.out.println("   peek(): " + minHeap.peek() + " ← Always smallest");

        System.out.print("   poll() order: ");
        while (!minHeap.isEmpty()) {
            System.out.print(minHeap.poll() + " ");
        }
        System.out.println("← Sorted!");

        System.out.println();
    }

    private void demonstrateMaxHeap() {
        System.out.println("📈 MAX-HEAP (reverse order)");
        System.out.println("─────────────────────────────────────────────");

        PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Comparator.reverseOrder());
        maxHeap.addAll(Arrays.asList(30, 10, 50, 20, 40));

        System.out.println("   Using Comparator.reverseOrder()");
        System.out.println("   peek(): " + maxHeap.peek() + " ← Always largest");

        System.out.print("   poll() order: ");
        while (!maxHeap.isEmpty()) {
            System.out.print(maxHeap.poll() + " ");
        }
        System.out.println("← Descending!");

        System.out.println();
    }

    private void demonstrateIteratorGotcha() {
        System.out.println("⚠️ ITERATOR GOTCHA");
        System.out.println("─────────────────────────────────────────────");

        PriorityQueue<Integer> pq = new PriorityQueue<>(Arrays.asList(3, 1, 4, 1, 5));

        System.out.print("   for-each iteration: ");
        for (int n : pq) {
            System.out.print(n + " ");
        }
        System.out.println("← NOT sorted!");

        System.out.println("   ⚠️ Iterator does NOT guarantee sorted order!");
        System.out.println("   ✅ Use poll() repeatedly for sorted order");

        System.out.println();
    }
}
