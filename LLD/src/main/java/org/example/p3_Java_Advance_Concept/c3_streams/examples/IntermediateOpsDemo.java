package org.example.p3_Java_Advance_Concept.c3_streams.examples;

import java.util.*;
import java.util.stream.*;

import org.example.p3_Java_Advance_Concept.c3_streams.model.SampleData;

/**
 * Demonstrates intermediate stream operations.
 * These operations are LAZY and return a new Stream.
 */
public class IntermediateOpsDemo {

    /**
     * Demonstrates filter() - keep elements matching condition.
     */
    public static void demoFilter() {
        List<Integer> numbers = SampleData.getNumbers();

        System.out.println("   🔹 filter() - Keep even numbers:");
        List<Integer> evens = numbers.stream()
                .filter(n -> n % 2 == 0)
                .collect(Collectors.toList());
        System.out.println("      Input: " + numbers);
        System.out.println("      Output: " + evens);
    }

    /**
     * Demonstrates map() - transform each element.
     */
    public static void demoMap() {
        List<Integer> numbers = SampleData.getNumbers();
        List<String> names = SampleData.getNames();

        System.out.println("\n   🔹 map() - Square each number:");
        List<Integer> squares = numbers.stream()
                .map(n -> n * n)
                .collect(Collectors.toList());
        System.out.println("      Input: " + numbers);
        System.out.println("      Output: " + squares);

        System.out.println("\n   🔹 map() - String to length:");
        List<Integer> lengths = names.stream()
                .map(String::length)
                .collect(Collectors.toList());
        System.out.println("      Input: " + names);
        System.out.println("      Output: " + lengths);
    }

    /**
     * Demonstrates flatMap() - flatten nested structures.
     */
    public static void demoFlatMap() {
        System.out.println("\n   🔹 flatMap() - Flatten nested lists:");
        List<List<Integer>> nested = Arrays.asList(
                Arrays.asList(1, 2),
                Arrays.asList(3, 4),
                Arrays.asList(5, 6));
        List<Integer> flat = nested.stream()
                .flatMap(List::stream)
                .collect(Collectors.toList());
        System.out.println("      Input: " + nested);
        System.out.println("      Output: " + flat);
    }

    /**
     * Demonstrates sorted() - order elements.
     */
    public static void demoSorted() {
        List<String> names = SampleData.getNames();

        System.out.println("\n   🔹 sorted() - By string length:");
        List<String> byLength = names.stream()
                .sorted(Comparator.comparingInt(String::length))
                .collect(Collectors.toList());
        System.out.println("      Input: " + names);
        System.out.println("      Output: " + byLength);
    }

    /**
     * Demonstrates distinct() - remove duplicates.
     */
    public static void demoDistinct() {
        System.out.println("\n   🔹 distinct() - Remove duplicates:");
        List<Integer> withDups = Arrays.asList(1, 2, 2, 3, 3, 3, 4, 4, 4, 4);
        List<Integer> unique = withDups.stream()
                .distinct()
                .collect(Collectors.toList());
        System.out.println("      Input: " + withDups);
        System.out.println("      Output: " + unique);
    }

    /**
     * Demonstrates limit() and skip().
     */
    public static void demoLimitSkip() {
        List<Integer> numbers = SampleData.getNumbers();

        System.out.println("\n   🔹 limit() and skip():");
        List<Integer> first3 = numbers.stream().limit(3).collect(Collectors.toList());
        List<Integer> skipFirst3 = numbers.stream().skip(3).collect(Collectors.toList());
        System.out.println("      Input: " + numbers);
        System.out.println("      limit(3): " + first3);
        System.out.println("      skip(3): " + skipFirst3);
    }

    /**
     * Runs all demos.
     */
    public static void runAll() {
        System.out.println("\n📌 INTERMEDIATE OPERATIONS\n");
        demoFilter();
        demoMap();
        demoFlatMap();
        demoSorted();
        demoDistinct();
        demoLimitSkip();
    }
}
