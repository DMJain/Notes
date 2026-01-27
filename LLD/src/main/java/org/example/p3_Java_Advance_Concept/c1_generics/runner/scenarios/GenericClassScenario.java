package org.example.p3_Java_Advance_Concept.c1_generics.runner.scenarios;

import org.example.p3_Java_Advance_Concept.c1_generics.impl.Box;
import org.example.p3_Java_Advance_Concept.c1_generics.impl.Pair;

/**
 * Demonstrates generic class usage: Box&lt;T&gt; and Pair&lt;K,V&gt;.
 */
public class GenericClassScenario {

    public void execute() {
        System.out.println("┌────────────────────────────────────────┐");
        System.out.println("│      SCENARIO 1: GENERIC CLASSES       │");
        System.out.println("└────────────────────────────────────────┘\n");

        demonstrateBox();
        demonstratePair();
    }

    private void demonstrateBox() {
        System.out.println("▸ Box<T> - Single Type Parameter");
        System.out.println("  -------------------------------------");

        // String Box
        Box<String> stringBox = new Box<>();
        System.out.println("  Created: Box<String>");
        System.out.println("  isEmpty: " + stringBox.isEmpty());

        stringBox.add("Hello, Generics!");
        System.out.println("  Added: \"Hello, Generics!\"");
        System.out.println("  get(): " + stringBox.get());
        System.out.println("  toString(): " + stringBox);

        // Integer Box
        Box<Integer> intBox = new Box<>(42);
        System.out.println("\n  Created: Box<Integer>(42)");
        System.out.println("  get(): " + intBox.get());

        // Our custom type
        Box<Pair<String, Integer>> pairBox = new Box<>();
        pairBox.add(new Pair<>("count", 100));
        System.out.println("\n  Created: Box<Pair<String, Integer>>");
        System.out.println("  Nested generics work! get(): " + pairBox.get());
        System.out.println();
    }

    private void demonstratePair() {
        System.out.println("▸ Pair<K, V> - Multiple Type Parameters");
        System.out.println("  -------------------------------------");

        // String -> Integer pair
        Pair<String, Integer> age = new Pair<>("Alice", 25);
        System.out.println("  Created: Pair<String, Integer>(\"Alice\", 25)");
        System.out.println("  getKey(): " + age.getKey());
        System.out.println("  getValue(): " + age.getValue());

        // Factory method
        Pair<Integer, String> mapping = Pair.of(1, "First");
        System.out.println("\n  Using factory: Pair.of(1, \"First\")");
        System.out.println("  Result: " + mapping);

        // Immutable update
        Pair<String, Integer> updatedAge = age.withValue(26);
        System.out.println("\n  Immutable update: age.withValue(26)");
        System.out.println("  Original: " + age);
        System.out.println("  Updated:  " + updatedAge);
        System.out.println();
    }
}
