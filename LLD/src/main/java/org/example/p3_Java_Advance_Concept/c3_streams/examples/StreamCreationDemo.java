package org.example.p3_Java_Advance_Concept.c3_streams.examples;

import java.util.*;
import java.util.stream.*;

/**
 * Demonstrates various ways to create streams.
 */
public class StreamCreationDemo {

    /**
     * Creates stream from a Collection.
     */
    public static void demoFromCollection() {
        System.out.println("   🔹 From Collection:");
        List<String> list = Arrays.asList("a", "b", "c");
        list.stream().forEach(s -> System.out.print("   " + s));
        System.out.println();
    }

    /**
     * Creates stream from an Array.
     */
    public static void demoFromArray() {
        System.out.println("\n   🔹 From Array:");
        String[] array = { "x", "y", "z" };
        Arrays.stream(array).forEach(s -> System.out.print("   " + s));
        System.out.println();
    }

    /**
     * Creates stream using Stream.of().
     */
    public static void demoFromValues() {
        System.out.println("\n   🔹 From Stream.of():");
        Stream.of("one", "two", "three").forEach(s -> System.out.print("   " + s));
        System.out.println();
    }

    /**
     * Creates stream from range of integers.
     */
    public static void demoFromRange() {
        System.out.println("\n   🔹 From IntStream.range(1, 6):");
        IntStream.range(1, 6).forEach(n -> System.out.print("   " + n));
        System.out.println();
    }

    /**
     * Creates infinite stream with limit.
     */
    public static void demoInfinite() {
        System.out.println("\n   🔹 Infinite stream with limit:");
        Stream.iterate(1, n -> n * 2)
                .limit(5)
                .forEach(n -> System.out.print("   " + n));
        System.out.println(" (powers of 2)");
    }

    /**
     * Runs all demos.
     */
    public static void runAll() {
        System.out.println("\n📌 STREAM CREATION\n");
        demoFromCollection();
        demoFromArray();
        demoFromValues();
        demoFromRange();
        demoInfinite();
    }
}
