package org.example.p3_Java_Advance_Concept.c4_collection_framework.impl;

import org.example.p3_Java_Advance_Concept.c4_collection_framework.contracts.ICollectionDemo;

import java.util.*;

/**
 * Demonstrates the overview of Java Collections Framework.
 * Shows the hierarchy and basic usage of different collection types.
 */
public class CollectionOverviewDemo implements ICollectionDemo {

    @Override
    public String getDemoName() {
        return "Collection Framework Overview";
    }

    @Override
    public void execute() {
        System.out.println("┌─────────────────────────────────────────────────────────┐");
        System.out.println("│           COLLECTION FRAMEWORK OVERVIEW                  │");
        System.out.println("└─────────────────────────────────────────────────────────┘");
        System.out.println();

        demonstrateList();
        demonstrateSet();
        demonstrateQueue();
        demonstrateMap();
        demonstrateHierarchy();
    }

    private void demonstrateList() {
        System.out.println("📋 LIST - Ordered collection, allows duplicates");
        System.out.println("─────────────────────────────────────────────");

        List<String> list = new ArrayList<>();
        list.add("Apple");
        list.add("Banana");
        list.add("Apple"); // Duplicate allowed!

        System.out.println("   ArrayList: " + list);
        System.out.println("   ✓ Maintains insertion order");
        System.out.println("   ✓ Allows duplicates: 'Apple' appears twice");
        System.out.println("   ✓ Index-based access: list.get(0) = " + list.get(0));
        System.out.println();
    }

    private void demonstrateSet() {
        System.out.println("🎯 SET - Unique elements, no duplicates");
        System.out.println("─────────────────────────────────────────────");

        Set<String> set = new HashSet<>();
        set.add("Apple");
        set.add("Banana");
        set.add("Apple"); // Duplicate ignored!

        System.out.println("   HashSet: " + set);
        System.out.println("   ✓ Only unique elements");
        System.out.println("   ✓ Duplicate 'Apple' was ignored");
        System.out.println("   ✗ No guaranteed order (HashSet)");
        System.out.println();
    }

    private void demonstrateQueue() {
        System.out.println("📥 QUEUE - FIFO (First-In-First-Out)");
        System.out.println("─────────────────────────────────────────────");

        Queue<String> queue = new LinkedList<>();
        queue.offer("First");
        queue.offer("Second");
        queue.offer("Third");

        System.out.println("   Queue: " + queue);
        System.out.println("   ✓ First added = First removed");
        System.out.println("   ✓ poll() returns: " + queue.poll());
        System.out.println("   ✓ After poll(): " + queue);
        System.out.println();
    }

    private void demonstrateMap() {
        System.out.println("🗺️  MAP - Key-Value pairs (NOT a Collection!)");
        System.out.println("─────────────────────────────────────────────");

        Map<String, Integer> map = new HashMap<>();
        map.put("Alice", 25);
        map.put("Bob", 30);
        map.put("Charlie", 35);

        System.out.println("   HashMap: " + map);
        System.out.println("   ✓ Store key-value pairs");
        System.out.println("   ✓ Get by key: map.get(\"Alice\") = " + map.get("Alice"));
        System.out.println("   ⚠️  Map does NOT extend Collection interface!");
        System.out.println();
    }

    private void demonstrateHierarchy() {
        System.out.println("📊 HIERARCHY DEMONSTRATION");
        System.out.println("─────────────────────────────────────────────");

        // All these implement Iterable
        List<String> list = new ArrayList<>();
        Set<String> set = new HashSet<>();
        Queue<String> queue = new ArrayDeque<>();

        // Demonstrate Iterable compatibility
        list.add("Test");
        set.add("Test");
        queue.add("Test");

        System.out.println("   All Collection types implement Iterable:");
        System.out.print("   • List (ArrayList): ");
        for (String s : list)
            System.out.print(s + " ");
        System.out.println("← for-each works!");

        System.out.print("   • Set (HashSet): ");
        for (String s : set)
            System.out.print(s + " ");
        System.out.println("← for-each works!");

        System.out.print("   • Queue (ArrayDeque): ");
        for (String s : queue)
            System.out.print(s + " ");
        System.out.println("← for-each works!");

        System.out.println();
        System.out.println("   ℹ️  Map is iterable via keySet(), values(), or entrySet()");
    }
}
