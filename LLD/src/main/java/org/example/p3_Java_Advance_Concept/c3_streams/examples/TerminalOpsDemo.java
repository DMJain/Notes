package org.example.p3_Java_Advance_Concept.c3_streams.examples;

import java.util.*;
import java.util.stream.*;

import org.example.p3_Java_Advance_Concept.c3_streams.model.SampleData;

/**
 * Demonstrates terminal stream operations.
 * These operations are EAGER and trigger stream processing.
 */
public class TerminalOpsDemo {

    /**
     * Demonstrates forEach() - perform action for each element.
     */
    public static void demoForEach() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);

        System.out.println("   🔹 forEach() - Print each:");
        System.out.print("      ");
        numbers.stream().forEach(n -> System.out.print(n + " "));
        System.out.println();
    }

    /**
     * Demonstrates collect() - gather results.
     */
    public static void demoCollect() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);
        List<String> names = SampleData.getNames();

        System.out.println("\n   🔹 collect() - To List:");
        List<Integer> doubled = numbers.stream()
                .map(n -> n * 2)
                .collect(Collectors.toList());
        System.out.println("      Doubled: " + doubled);

        System.out.println("\n   🔹 collect() - To Map:");
        Map<String, Integer> nameLengths = names.stream()
                .collect(Collectors.toMap(
                        name -> name,
                        name -> name.length()));
        System.out.println("      Name→Length: " + nameLengths);

        System.out.println("\n   🔹 collect() - Joining:");
        String joined = names.stream()
                .collect(Collectors.joining(", "));
        System.out.println("      Joined: " + joined);
    }

    /**
     * Demonstrates reduce() - combine all elements to one.
     */
    public static void demoReduce() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);

        System.out.println("\n   🔹 reduce() - Sum:");
        int sum = numbers.stream().reduce(0, (a, b) -> a + b);
        System.out.println("      Sum of " + numbers + " = " + sum);

        System.out.println("   🔹 reduce() - Product:");
        int product = numbers.stream().reduce(1, (a, b) -> a * b);
        System.out.println("      Product of " + numbers + " = " + product);
    }

    /**
     * Demonstrates findFirst() and findAny().
     */
    public static void demoFind() {
        List<String> names = SampleData.getNames();

        System.out.println("\n   🔹 findFirst():");
        Optional<String> first = names.stream()
                .filter(n -> n.startsWith("A"))
                .findFirst();
        System.out.println("      First name with 'A': " + first.orElse("None"));
    }

    /**
     * Demonstrates match operations.
     */
    public static void demoMatch() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);

        System.out.println("\n   🔹 Match operations:");
        boolean anyNegative = numbers.stream().anyMatch(n -> n < 0);
        boolean allPositive = numbers.stream().allMatch(n -> n > 0);
        boolean noneZero = numbers.stream().noneMatch(n -> n == 0);
        System.out.println("      Any negative? " + anyNegative);
        System.out.println("      All positive? " + allPositive);
        System.out.println("      None zero? " + noneZero);
    }

    /**
     * Demonstrates aggregation operations.
     */
    public static void demoAggregates() {
        List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);

        System.out.println("\n   🔹 Aggregate operations:");
        IntSummaryStatistics stats = numbers.stream()
                .mapToInt(Integer::intValue)
                .summaryStatistics();
        System.out.println("      Count: " + stats.getCount());
        System.out.println("      Sum: " + stats.getSum());
        System.out.println("      Min: " + stats.getMin());
        System.out.println("      Max: " + stats.getMax());
        System.out.println("      Avg: " + stats.getAverage());
    }

    /**
     * Runs all demos.
     */
    public static void runAll() {
        System.out.println("\n📌 TERMINAL OPERATIONS\n");
        demoForEach();
        demoCollect();
        demoReduce();
        demoFind();
        demoMatch();
        demoAggregates();
    }
}
