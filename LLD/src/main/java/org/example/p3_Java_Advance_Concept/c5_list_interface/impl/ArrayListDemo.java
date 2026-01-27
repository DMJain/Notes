package org.example.p3_Java_Advance_Concept.c5_list_interface.impl;

import org.example.p3_Java_Advance_Concept.c5_list_interface.contracts.IListDemo;
import java.util.*;

/**
 * Demonstrates ArrayList internal behavior and operations.
 */
public class ArrayListDemo implements IListDemo {

    @Override
    public String getDemoName() {
        return "ArrayList Demo";
    }

    @Override
    public void execute() {
        System.out.println("┌─────────────────────────────────────────────────────────┐");
        System.out.println("│                    ARRAYLIST DEMO                        │");
        System.out.println("└─────────────────────────────────────────────────────────┘");
        System.out.println();

        demonstrateBasicOperations();
        demonstrateInitialCapacity();
        demonstrateRandomAccess();
    }

    private void demonstrateBasicOperations() {
        System.out.println("📋 BASIC OPERATIONS");
        System.out.println("─────────────────────────────────────────────");

        List<String> list = new ArrayList<>();

        // Add elements
        list.add("Apple");
        list.add("Banana");
        list.add("Cherry");
        System.out.println("   After add(): " + list);

        // Access by index - O(1)
        System.out.println("   get(1): " + list.get(1) + " ← O(1) random access!");

        // Set by index - O(1)
        list.set(1, "Blueberry");
        System.out.println("   set(1, \"Blueberry\"): " + list);

        // Insert at index - O(n) due to shifting
        list.add(1, "Avocado");
        System.out.println("   add(1, \"Avocado\"): " + list + " ← O(n) shifting!");

        // Remove by index - O(n)
        list.remove(2);
        System.out.println("   remove(2): " + list);

        System.out.println();
    }

    private void demonstrateInitialCapacity() {
        System.out.println("📏 INITIAL CAPACITY");
        System.out.println("─────────────────────────────────────────────");

        // Default capacity is 10
        List<Integer> defaultList = new ArrayList<>();
        System.out.println("   new ArrayList<>() → default capacity: 10");

        // Custom capacity to avoid resizing
        List<Integer> customList = new ArrayList<>(1000);
        System.out.println("   new ArrayList<>(1000) → avoids resize for 1000 elements");

        // Demonstrate growth pattern
        System.out.println("   Growth pattern: 10 → 15 → 22 → 33 → 49 → ...");
        System.out.println("   Formula: newCap = oldCap + (oldCap >> 1) ≈ 1.5x");

        System.out.println();
    }

    private void demonstrateRandomAccess() {
        System.out.println("🎯 RANDOM ACCESS (RandomAccess interface)");
        System.out.println("─────────────────────────────────────────────");

        List<Integer> list = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            list.add(i * 10);
        }

        System.out.println("   List: " + list);
        System.out.println("   list.get(0): " + list.get(0) + " ← O(1)");
        System.out.println("   list.get(5): " + list.get(5) + " ← O(1)");
        System.out.println("   list.get(9): " + list.get(9) + " ← O(1)");

        // Check if implements RandomAccess
        boolean isRandomAccess = list instanceof RandomAccess;
        System.out.println("   Implements RandomAccess? " + isRandomAccess);

        System.out.println();
    }
}
