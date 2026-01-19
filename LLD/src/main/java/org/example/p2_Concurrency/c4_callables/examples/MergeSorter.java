package org.example.p2_Concurrency.c4_callables.examples;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * Multi-threaded Merge Sort using Callable and Future.
 * 
 * This demonstrates the power of Callable:
 * - Each Sorter returns a sorted sublist
 * - Parent thread waits for both halves using Future.get()
 * - Then merges them
 * 
 * Visualization:
 * [7, 3, 1, 2, 4, 6, 17, 12]
 * │
 * ┌───────────┴───────────┐
 * ▼ ▼
 * [7, 3, 1, 2] [4, 6, 17, 12]
 * Thread 1 Thread 2
 * │ │
 * [1, 2, 3, 7] [4, 6, 12, 17]
 * │ │
 * └───────────┬───────────┘
 * ▼
 * [1, 2, 3, 4, 6, 7, 12, 17]
 */
public class MergeSorter implements Callable<List<Integer>> {
    private final List<Integer> arr;
    private final ExecutorService executor;

    public MergeSorter(List<Integer> arr, ExecutorService executor) {
        this.arr = arr;
        this.executor = executor;
    }

    @Override
    public List<Integer> call() throws Exception {
        // Base case: already sorted
        if (arr.size() <= 1) {
            return arr;
        }

        System.out.println(Thread.currentThread().getName() +
                " sorting: " + arr);

        // Divide
        int mid = arr.size() / 2;
        List<Integer> leftArr = new ArrayList<>(arr.subList(0, mid));
        List<Integer> rightArr = new ArrayList<>(arr.subList(mid, arr.size()));

        // Conquer: sort both halves in parallel!
        MergeSorter leftSorter = new MergeSorter(leftArr, executor);
        MergeSorter rightSorter = new MergeSorter(rightArr, executor);

        // Submit both to executor (will run in parallel if threads available)
        Future<List<Integer>> leftFuture = executor.submit(leftSorter);
        Future<List<Integer>> rightFuture = executor.submit(rightSorter);

        // Wait for results (this is where Future shines!)
        leftArr = leftFuture.get(); // Blocks until left half sorted
        rightArr = rightFuture.get(); // Blocks until right half sorted

        // Merge the sorted halves
        List<Integer> merged = merge(leftArr, rightArr);
        System.out.println(Thread.currentThread().getName() +
                " merged:  " + merged);

        return merged;
    }

    private List<Integer> merge(List<Integer> left, List<Integer> right) {
        List<Integer> result = new ArrayList<>();
        int i = 0, j = 0;

        while (i < left.size() && j < right.size()) {
            if (left.get(i) < right.get(j)) {
                result.add(left.get(i++));
            } else {
                result.add(right.get(j++));
            }
        }

        // Add remaining elements
        while (i < left.size())
            result.add(left.get(i++));
        while (j < right.size())
            result.add(right.get(j++));

        return result;
    }

    /**
     * Test the merge sorter
     */
    public static void main(String[] args) throws Exception {
        List<Integer> list = new ArrayList<>(List.of(7, 3, 1, 2, 4, 6, 17, 12));

        System.out.println("=".repeat(60));
        System.out.println("MULTI-THREADED MERGE SORT");
        System.out.println("=".repeat(60));
        System.out.println("\nOriginal: " + list);
        System.out.println("\nSorting process:");
        System.out.println("-".repeat(40));

        ExecutorService executor = Executors.newCachedThreadPool();
        MergeSorter sorter = new MergeSorter(list, executor);

        Future<List<Integer>> future = executor.submit(sorter);
        List<Integer> sorted = future.get(); // Wait for final result

        System.out.println("-".repeat(40));
        System.out.println("\nFinal sorted: " + sorted);

        executor.shutdown();
    }
}
