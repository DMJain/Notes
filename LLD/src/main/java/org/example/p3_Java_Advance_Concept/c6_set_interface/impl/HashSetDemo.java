package org.example.p3_Java_Advance_Concept.c6_set_interface.impl;

import org.example.p3_Java_Advance_Concept.c6_set_interface.contracts.ISetDemo;
import java.util.*;

/**
 * Demonstrates HashSet behavior and O(1) operations.
 */
public class HashSetDemo implements ISetDemo {

    @Override
    public String getDemoName() {
        return "HashSet Demo";
    }

    @Override
    public void execute() {
        System.out.println("┌─────────────────────────────────────────────────────────┐");
        System.out.println("│                     HASHSET DEMO                         │");
        System.out.println("└─────────────────────────────────────────────────────────┘");
        System.out.println();

        demonstrateBasics();
        demonstrateDuplicateRejection();
        demonstrateNoOrdering();
    }

    private void demonstrateBasics() {
        System.out.println("📋 BASIC OPERATIONS");
        System.out.println("─────────────────────────────────────────────");

        Set<String> set = new HashSet<>();

        // Add elements
        System.out.println("   add(\"Apple\"): " + set.add("Apple") + " ← true (new)");
        System.out.println("   add(\"Banana\"): " + set.add("Banana"));
        System.out.println("   add(\"Cherry\"): " + set.add("Cherry"));
        System.out.println("   Set: " + set);

        // Contains - O(1)
        System.out.println("   contains(\"Apple\"): " + set.contains("Apple") + " ← O(1)!");

        // Remove - O(1)
        System.out.println("   remove(\"Banana\"): " + set.remove("Banana"));
        System.out.println("   After remove: " + set);

        System.out.println();
    }

    private void demonstrateDuplicateRejection() {
        System.out.println("🚫 DUPLICATE REJECTION");
        System.out.println("─────────────────────────────────────────────");

        Set<String> set = new HashSet<>();
        set.add("Apple");

        // Try to add duplicate
        boolean added = set.add("Apple");
        System.out.println("   add(\"Apple\") again: " + added + " ← false (duplicate rejected)");
        System.out.println("   Set still has: " + set);
        System.out.println("   Size: " + set.size() + " (not 2!)");

        // Common pattern: deduplicate a list
        List<String> listWithDupes = Arrays.asList("A", "B", "A", "C", "B");
        Set<String> deduped = new HashSet<>(listWithDupes);
        System.out.println("   List with dupes: " + listWithDupes);
        System.out.println("   Deduplicated: " + deduped);

        System.out.println();
    }

    private void demonstrateNoOrdering() {
        System.out.println("🔀 NO ORDERING GUARANTEE");
        System.out.println("─────────────────────────────────────────────");

        Set<Integer> set = new HashSet<>();
        set.add(3);
        set.add(1);
        set.add(2);

        System.out.println("   Added: 3, 1, 2");
        System.out.println("   Iteration order: " + set);
        System.out.println("   ⚠️ Order may vary between runs!");
        System.out.println("   ⚠️ Use LinkedHashSet for insertion order");
        System.out.println("   ⚠️ Use TreeSet for sorted order");

        System.out.println();
    }
}
