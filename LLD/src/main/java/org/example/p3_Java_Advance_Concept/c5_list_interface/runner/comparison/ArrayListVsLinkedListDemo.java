package org.example.p3_Java_Advance_Concept.c5_list_interface.runner.comparison;

import org.example.p3_Java_Advance_Concept.c5_list_interface.contracts.IListDemo;
import java.util.*;

/**
 * Performance comparison between ArrayList and LinkedList.
 */
public class ArrayListVsLinkedListDemo implements IListDemo {

    private static final int TEST_SIZE = 10000;

    @Override
    public String getDemoName() {
        return "ArrayList vs LinkedList Comparison";
    }

    @Override
    public void execute() {
        System.out.println("┌─────────────────────────────────────────────────────────┐");
        System.out.println("│           ARRAYLIST vs LINKEDLIST COMPARISON             │");
        System.out.println("└─────────────────────────────────────────────────────────┘");
        System.out.println();

        compareRandomAccess();
        compareAddAtBeginning();
        compareAddAtEnd();
        printSummary();
    }

    private void compareRandomAccess() {
        System.out.println("🎯 RANDOM ACCESS (get by index)");
        System.out.println("─────────────────────────────────────────────");

        // Prepare lists
        List<Integer> arrayList = new ArrayList<>();
        List<Integer> linkedList = new LinkedList<>();
        for (int i = 0; i < TEST_SIZE; i++) {
            arrayList.add(i);
            linkedList.add(i);
        }

        // Test ArrayList
        long start = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            arrayList.get(TEST_SIZE / 2);
        }
        long arrayTime = System.nanoTime() - start;

        // Test LinkedList
        start = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            linkedList.get(TEST_SIZE / 2);
        }
        long linkedTime = System.nanoTime() - start;

        System.out.println("   ArrayList:  " + arrayTime / 1000 + " μs ← O(1) direct array access");
        System.out.println("   LinkedList: " + linkedTime / 1000 + " μs ← O(n) must traverse!");
        System.out.println("   Winner: ArrayList 🏆");
        System.out.println();
    }

    private void compareAddAtBeginning() {
        System.out.println("⬅️ ADD AT BEGINNING (add(0, element))");
        System.out.println("─────────────────────────────────────────────");

        List<Integer> arrayList = new ArrayList<>();
        List<Integer> linkedList = new LinkedList<>();

        // Test ArrayList
        long start = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            arrayList.add(0, i);
        }
        long arrayTime = System.nanoTime() - start;

        // Test LinkedList
        start = System.nanoTime();
        for (int i = 0; i < 1000; i++) {
            linkedList.addFirst(i);
        }
        long linkedTime = System.nanoTime() - start;

        System.out.println("   ArrayList:  " + arrayTime / 1000 + " μs ← O(n) shift all elements!");
        System.out.println("   LinkedList: " + linkedTime / 1000 + " μs ← O(1) just update head");
        System.out.println("   Winner: LinkedList 🏆");
        System.out.println();
    }

    private void compareAddAtEnd() {
        System.out.println("➡️ ADD AT END (add(element))");
        System.out.println("─────────────────────────────────────────────");

        List<Integer> arrayList = new ArrayList<>();
        List<Integer> linkedList = new LinkedList<>();

        // Test ArrayList
        long start = System.nanoTime();
        for (int i = 0; i < TEST_SIZE; i++) {
            arrayList.add(i);
        }
        long arrayTime = System.nanoTime() - start;

        // Test LinkedList
        start = System.nanoTime();
        for (int i = 0; i < TEST_SIZE; i++) {
            linkedList.add(i);
        }
        long linkedTime = System.nanoTime() - start;

        System.out.println("   ArrayList:  " + arrayTime / 1000 + " μs ← O(1) amortized");
        System.out.println("   LinkedList: " + linkedTime / 1000 + " μs ← O(1)");
        System.out.println("   Winner: Both similar (ArrayList often faster due to cache locality)");
        System.out.println();
    }

    private void printSummary() {
        System.out.println("📊 SUMMARY TABLE");
        System.out.println("─────────────────────────────────────────────");
        System.out.println("   Operation          | ArrayList | LinkedList");
        System.out.println("   ───────────────────┼───────────┼───────────");
        System.out.println("   get(index)         |   O(1) 🏆 |   O(n)");
        System.out.println("   add at end         |   O(1)    |   O(1)");
        System.out.println("   add at beginning   |   O(n)    |   O(1) 🏆");
        System.out.println("   add in middle      |   O(n)    |   O(n)");
        System.out.println("   Memory per element |   ~8B     |   ~24B");
        System.out.println();
        System.out.println("   💡 Rule: Use ArrayList unless you need frequent add/remove at head");
        System.out.println();
    }
}
