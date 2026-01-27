package org.example.p3_Java_Advance_Concept.c6_set_interface.impl;

import org.example.p3_Java_Advance_Concept.c6_set_interface.contracts.ISetDemo;
import java.util.*;

/**
 * Demonstrates TreeSet with sorted elements and NavigableSet methods.
 */
public class TreeSetDemo implements ISetDemo {

    @Override
    public String getDemoName() {
        return "TreeSet Demo";
    }

    @Override
    public void execute() {
        System.out.println("┌─────────────────────────────────────────────────────────┐");
        System.out.println("│                     TREESET DEMO                         │");
        System.out.println("└─────────────────────────────────────────────────────────┘");
        System.out.println();

        demonstrateSortedOrder();
        demonstrateNavigableSetMethods();
        demonstrateComparator();
    }

    private void demonstrateSortedOrder() {
        System.out.println("📊 SORTED ORDER (Natural Ordering)");
        System.out.println("─────────────────────────────────────────────");

        TreeSet<Integer> set = new TreeSet<>();
        set.add(50);
        set.add(30);
        set.add(70);
        set.add(20);
        set.add(40);

        System.out.println("   Added: 50, 30, 70, 20, 40");
        System.out.println("   Iteration: " + set + " ← Always sorted!");
        System.out.println("   first(): " + set.first());
        System.out.println("   last(): " + set.last());

        System.out.println();
    }

    private void demonstrateNavigableSetMethods() {
        System.out.println("🧭 NAVIGABLESET METHODS");
        System.out.println("─────────────────────────────────────────────");

        TreeSet<Integer> set = new TreeSet<>();
        set.addAll(Arrays.asList(10, 20, 30, 40, 50));
        System.out.println("   Set: " + set);

        // Navigation
        System.out.println("   floor(25):   " + set.floor(25) + " ← largest ≤ 25");
        System.out.println("   ceiling(25): " + set.ceiling(25) + " ← smallest ≥ 25");
        System.out.println("   lower(30):   " + set.lower(30) + " ← largest < 30");
        System.out.println("   higher(30):  " + set.higher(30) + " ← smallest > 30");

        // Range views
        System.out.println("   headSet(30):      " + set.headSet(30) + " ← elements < 30");
        System.out.println("   tailSet(30):      " + set.tailSet(30) + " ← elements ≥ 30");
        System.out.println("   subSet(20, 40):   " + set.subSet(20, 40) + " ← 20 ≤ x < 40");

        System.out.println();
    }

    private void demonstrateComparator() {
        System.out.println("🔄 CUSTOM COMPARATOR");
        System.out.println("─────────────────────────────────────────────");

        // Reverse order
        TreeSet<Integer> descending = new TreeSet<>(Comparator.reverseOrder());
        descending.addAll(Arrays.asList(1, 3, 2));
        System.out.println("   Reverse order: " + descending);

        // Custom: by string length
        TreeSet<String> byLength = new TreeSet<>(Comparator.comparingInt(String::length));
        byLength.add("Apple");
        byLength.add("Kiwi");
        byLength.add("Strawberry");
        System.out.println("   By length: " + byLength);

        System.out.println();
    }
}
