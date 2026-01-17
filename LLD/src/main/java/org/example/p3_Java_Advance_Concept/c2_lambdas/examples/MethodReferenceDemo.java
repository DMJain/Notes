package org.example.p3_Java_Advance_Concept.c2_lambdas.examples;

import java.util.*;
import java.util.function.*;

/**
 * Demonstrates Method References - shorthand syntax for lambdas.
 * 
 * Types of method references:
 * 1. Static method: ClassName::staticMethod
 * 2. Instance (object): instance::instanceMethod
 * 3. Instance (param): ClassName::instanceMethod
 * 4. Constructor: ClassName::new
 */
public class MethodReferenceDemo {

    /**
     * Demonstrates static method references.
     */
    public static void demoStaticReference() {
        System.out.println("   🔹 Static method reference (Integer::parseInt):");

        // Lambda
        Function<String, Integer> parseOld = s -> Integer.parseInt(s);

        // Method reference
        Function<String, Integer> parseNew = Integer::parseInt;

        System.out.println("      Lambda: parse(\"42\") = " + parseOld.apply("42"));
        System.out.println("      Method ref: parse(\"42\") = " + parseNew.apply("42"));
    }

    /**
     * Demonstrates instance method reference on specific object.
     */
    public static void demoInstanceOnObject() {
        System.out.println("\n   🔹 Instance method on object (System.out::println):");

        List<String> words = Arrays.asList("Hello", "World", "Lambda");

        System.out.println("      Printing list items:");
        words.forEach(System.out::println);
    }

    /**
     * Demonstrates instance method reference on parameter.
     */
    public static void demoInstanceOnParameter() {
        System.out.println("\n   🔹 Instance method on parameter (String::toUpperCase):");

        List<String> words = Arrays.asList("Hello", "World", "Lambda");

        // Lambda way
        List<String> upper1 = new ArrayList<>();
        words.forEach(s -> upper1.add(s.toUpperCase()));
        System.out.println("      Lambda way: " + upper1);

        // Method reference way
        List<String> upper2 = new ArrayList<>();
        words.stream().map(String::toUpperCase).forEach(upper2::add);
        System.out.println("      Method ref: " + upper2);
    }

    /**
     * Demonstrates constructor reference.
     */
    public static void demoConstructorReference() {
        System.out.println("\n   🔹 Constructor reference (ArrayList::new):");

        Supplier<List<String>> listMaker = ArrayList::new;
        List<String> newList = listMaker.get();
        newList.add("Created with ::new!");
        System.out.println("      " + newList);
    }

    /**
     * Runs all demos.
     */
    public static void runAll() {
        System.out.println("\n📌 METHOD REFERENCES (::)\n");
        demoStaticReference();
        demoInstanceOnObject();
        demoInstanceOnParameter();
        demoConstructorReference();
    }
}
