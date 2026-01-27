package org.example.p3_Java_Advance_Concept.c9_iterators.impl;

import org.example.p3_Java_Advance_Concept.c9_iterators.contracts.IIteratorDemo;
import java.util.*;

/**
 * Demonstrates safe removal during iteration using Iterator.
 */
public class IteratorDemo implements IIteratorDemo {

    @Override
    public String getDemoName() {
        return "Iterator Demo";
    }

    @Override
    public void execute() {
        System.out.println("┌─────────────────────────────────────────────────────────┐");
        System.out.println("│                    ITERATOR DEMO                         │");
        System.out.println("└─────────────────────────────────────────────────────────┘");
        System.out.println();

        demonstrateSafeRemoval();
        demonstrateRemoveIf();
    }

    private void demonstrateSafeRemoval() {
        System.out.println("🔄 SAFE REMOVAL WITH ITERATOR");
        System.out.println("─────────────────────────────────────────────");

        List<String> list = new ArrayList<>(Arrays.asList("a", "b", "c", "d"));
        System.out.println("   Original: " + list);

        // Safe removal using Iterator
        Iterator<String> it = list.iterator();
        while (it.hasNext()) {
            String s = it.next();
            if (s.equals("b") || s.equals("c")) {
                it.remove(); // Safe!
            }
        }
        System.out.println("   After removing b, c: " + list);

        System.out.println("   ⚠️ Never call list.remove() inside for-each!");
        System.out.println("   ✅ Use iterator.remove() for safe removal");

        System.out.println();
    }

    private void demonstrateRemoveIf() {
        System.out.println("🎯 REMOVEIF (Java 8+)");
        System.out.println("─────────────────────────────────────────────");

        List<Integer> numbers = new ArrayList<>(Arrays.asList(1, 2, 3, 4, 5, 6));
        System.out.println("   Original: " + numbers);

        // Remove all even numbers
        numbers.removeIf(n -> n % 2 == 0);
        System.out.println("   After removeIf(even): " + numbers);

        System.out.println("   ✅ Cleanest way for conditional removal");

        System.out.println();
    }
}
