package org.example.p3_Java_Advance_Concept.c2_lambdas.examples;

import java.util.function.*;

/**
 * Demonstrates the built-in functional interfaces from java.util.function.
 * 
 * The Big Four:
 * - Predicate<T> : T -> boolean (test a condition)
 * - Function<T, R> : T -> R (transform data)
 * - Consumer<T> : T -> void (consume/use data)
 * - Supplier<T> : () -> T (provide/generate data)
 */
public class BuiltInInterfacesDemo {

    /**
     * Demonstrates Predicate - tests a condition.
     */
    public static void demoPredicate() {
        System.out.println("   🔹 Predicate (test condition):");

        Predicate<Integer> isEven = n -> n % 2 == 0;
        Predicate<Integer> isPositive = n -> n > 0;

        System.out.println("      isEven.test(4) = " + isEven.test(4));
        System.out.println("      isEven.test(7) = " + isEven.test(7));

        // Chaining predicates
        Predicate<Integer> isPositiveEven = isEven.and(isPositive);
        System.out.println("      isPositiveEven.test(-4) = " + isPositiveEven.test(-4));
    }

    /**
     * Demonstrates Function - transforms data from T to R.
     */
    public static void demoFunction() {
        System.out.println("\n   🔹 Function (transform):");

        Function<String, Integer> stringLength = String::length;
        Function<String, String> toUpper = String::toUpperCase;

        System.out.println("      stringLength.apply(\"Hello\") = " + stringLength.apply("Hello"));
        System.out.println("      toUpper.apply(\"lambda\") = " + toUpper.apply("lambda"));

        // Chaining functions
        Function<String, String> addExclaim = s -> s + "!";
        Function<String, String> shout = toUpper.andThen(addExclaim);
        System.out.println("      shout.apply(\"wow\") = " + shout.apply("wow"));
    }

    /**
     * Demonstrates Consumer - consumes data without returning.
     */
    public static void demoConsumer() {
        System.out.println("\n   🔹 Consumer (consume/action):");

        Consumer<String> printer = s -> System.out.println("      Consumed: " + s);
        printer.accept("Apple");
        printer.accept("Banana");
    }

    /**
     * Demonstrates Supplier - provides data without input.
     */
    public static void demoSupplier() {
        System.out.println("\n   🔹 Supplier (provide/generate):");

        Supplier<Double> randomValue = Math::random;
        System.out.println("      Random 1: " + randomValue.get());
        System.out.println("      Random 2: " + randomValue.get());
    }

    /**
     * Runs all demos.
     */
    public static void runAll() {
        System.out.println("\n📌 BUILT-IN FUNCTIONAL INTERFACES\n");
        demoPredicate();
        demoFunction();
        demoConsumer();
        demoSupplier();
    }
}
