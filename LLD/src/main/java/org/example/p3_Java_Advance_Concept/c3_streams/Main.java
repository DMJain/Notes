package org.example.p3_Java_Advance_Concept.c3_streams;

import org.example.p3_Java_Advance_Concept.c3_streams.examples.*;

/**
 * Main entry point for Java Streams API demonstrations.
 * 
 * This chapter covers:
 * 1. Stream Creation
 * 2. Intermediate Operations (filter, map, flatMap, sorted, etc.)
 * 3. Terminal Operations (collect, reduce, forEach, etc.)
 * 4. Collectors (groupingBy, partitioningBy, etc.)
 * 5. Advanced Topics (lazy evaluation, parallel streams)
 * 
 * @see StreamsNotes.md for detailed explanations
 */
public class Main {

    public static void main(String[] args) {
        printHeader();

        // Run each demo in sequence
        StreamCreationDemo.runAll();
        IntermediateOpsDemo.runAll();
        TerminalOpsDemo.runAll();
        CollectorsDemo.runAll();
        AdvancedStreamDemo.runAll();

        printFooter();
    }

    private static void printHeader() {
        System.out.println("=".repeat(60));
        System.out.println("             JAVA STREAMS API");
        System.out.println("=".repeat(60));
        System.out.println("\nThis module demonstrates the Streams API in Java 8+.");
        System.out.println("Prerequisite: ../c2_lambdas (Lambdas & Functional Interfaces)");
        System.out.println("See StreamsNotes.md for detailed explanations.\n");
    }

    private static void printFooter() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("             END OF STREAMS DEMONSTRATIONS");
        System.out.println("=".repeat(60));
        System.out.println("\nKey Takeaways:");
        System.out.println("- Streams are LAZY (process on-demand)");
        System.out.println("- Intermediate ops return Stream, Terminal ops return result");
        System.out.println("- Parallel isn't always faster - use wisely!");
    }
}
