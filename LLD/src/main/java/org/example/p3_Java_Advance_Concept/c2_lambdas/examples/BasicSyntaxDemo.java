package org.example.p3_Java_Advance_Concept.c2_lambdas.examples;

import java.util.*;
import java.util.function.*;

/**
 * Demonstrates basic lambda syntax variations.
 */
public class BasicSyntaxDemo {

    /**
     * Shows different lambda syntax patterns.
     */
    public static void demoSyntaxVariations() {
        System.out.println("   🔹 Syntax Variations:\n");

        // Zero parameters
        Runnable noParams = () -> System.out.println("      No parameters!");
        noParams.run();

        // One parameter (parentheses optional)
        Consumer<String> oneParam = message -> System.out.println("      One param: " + message);
        oneParam.accept("Hello!");

        // Multiple parameters
        BinaryOperator<Integer> add = (a, b) -> a + b;
        System.out.println("      Two params: 5 + 3 = " + add.apply(5, 3));

        // Block body with multiple statements
        BinaryOperator<Integer> addWithLog = (a, b) -> {
            System.out.println("      Block body: Adding " + a + " and " + b);
            int result = a + b;
            return result;
        };
        System.out.println("      Result: " + addWithLog.apply(10, 20));
    }

    /**
     * Shows before/after comparison with Runnable.
     */
    public static void demoRunnableComparison() {
        System.out.println("\n   🔹 Before/After - Runnable:\n");

        // OLD WAY: Anonymous Inner Class
        Runnable oldWay = new Runnable() {
            @Override
            public void run() {
                System.out.println("      [Anonymous Class] Hello!");
            }
        };

        // NEW WAY: Lambda
        Runnable newWay = () -> System.out.println("      [Lambda] Hello!");

        oldWay.run();
        newWay.run();
        System.out.println("\n      Notice: Same result, 6 lines vs 1 line!");
    }

    /**
     * Shows before/after comparison with Comparator.
     */
    public static void demoComparatorComparison() {
        System.out.println("\n   🔹 Before/After - Comparator:\n");

        List<String> names = new ArrayList<>(Arrays.asList("Charlie", "Alice", "Bob"));
        System.out.println("      Original: " + names);

        // OLD WAY: Anonymous Inner Class
        Collections.sort(names, new Comparator<String>() {
            @Override
            public int compare(String a, String b) {
                return a.compareTo(b);
            }
        });
        System.out.println("      Sorted (old way): " + names);

        // Shuffle for next demo
        Collections.shuffle(names);

        // NEW WAY: Lambda
        Collections.sort(names, (a, b) -> a.compareTo(b));
        System.out.println("      Sorted (lambda): " + names);

        // EVEN BETTER: Method Reference
        Collections.shuffle(names);
        names.sort(String::compareTo);
        System.out.println("      Sorted (method ref): " + names);
    }

    /**
     * Runs all demos.
     */
    public static void runAll() {
        System.out.println("\n📌 BASIC LAMBDA SYNTAX\n");
        demoSyntaxVariations();
        demoRunnableComparison();
        demoComparatorComparison();
    }
}
