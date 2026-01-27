package org.example.p3_Java_Advance_Concept.c1_generics.runner.scenarios;

import org.example.p3_Java_Advance_Concept.c1_generics.util.GenericUtils;

import java.util.Arrays;
import java.util.List;

/**
 * Demonstrates generic method usage.
 */
public class GenericMethodScenario {

    public void execute() {
        System.out.println("┌────────────────────────────────────────┐");
        System.out.println("│      SCENARIO 2: GENERIC METHODS       │");
        System.out.println("└────────────────────────────────────────┘\n");

        demonstrateArrayMethods();
        demonstrateListMethods();
        demonstrateBoundedMethods();
    }

    private void demonstrateArrayMethods() {
        System.out.println("▸ Array Operations with Generic Methods");
        System.out.println("  -------------------------------------");

        Integer[] intArray = { 1, 2, 3, 4, 5 };
        String[] stringArray = { "apple", "banana", "cherry" };

        System.out.print("  Integer array: ");
        GenericUtils.printArray(intArray);

        System.out.print("  String array:  ");
        GenericUtils.printArray(stringArray);

        System.out.println("\n  getMiddle(intArray): " + GenericUtils.getMiddle(intArray));
        System.out.println("  getMiddle(stringArray): " + GenericUtils.getMiddle(stringArray));

        // Swap demonstration
        System.out.println("\n  Before swap: ");
        System.out.print("    ");
        GenericUtils.printArray(stringArray);
        GenericUtils.swap(stringArray, 0, 2);
        System.out.println("  After swap(0, 2): ");
        System.out.print("    ");
        GenericUtils.printArray(stringArray);
        System.out.println();
    }

    private void demonstrateListMethods() {
        System.out.println("▸ List Operations with Generic Methods");
        System.out.println("  -------------------------------------");

        List<String> names = Arrays.asList("Alice", "Bob", "Charlie");
        List<Integer> numbers = Arrays.asList(10, 20, 30, 40, 50);

        System.out.println("  names: " + names);
        System.out.println("  getFirst(names): " + GenericUtils.getFirst(names));
        System.out.println("  getLast(names): " + GenericUtils.getLast(names));

        System.out.println("\n  numbers: " + numbers);
        System.out.println("  getFirst(numbers): " + GenericUtils.getFirst(numbers));
        System.out.println("  getLast(numbers): " + GenericUtils.getLast(numbers));
        System.out.println();
    }

    private void demonstrateBoundedMethods() {
        System.out.println("▸ Bounded Generic Methods <T extends Comparable<T>>");
        System.out.println("  ------------------------------------------------");

        Integer[] numbers = { 3, 1, 4, 1, 5, 9, 2, 6 };
        String[] words = { "delta", "alpha", "gamma", "beta" };

        System.out.print("  numbers: ");
        GenericUtils.printArray(numbers);
        System.out.println("  findMax(numbers): " + GenericUtils.findMax(numbers));
        System.out.println("  findMin(numbers): " + GenericUtils.findMin(numbers));

        System.out.print("\n  words: ");
        GenericUtils.printArray(words);
        System.out.println("  findMax(words): " + GenericUtils.findMax(words));
        System.out.println("  findMin(words): " + GenericUtils.findMin(words));

        System.out.println("\n  💡 Works because Integer and String implement Comparable!");
        System.out.println();
    }
}
