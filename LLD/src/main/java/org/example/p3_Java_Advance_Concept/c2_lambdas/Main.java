package org.example.p3_Java_Advance_Concept.c2_lambdas;

import org.example.p3_Java_Advance_Concept.c2_lambdas.examples.*;

/**
 * Main entry point for Lambda & Functional Interface demonstrations.
 * 
 * This chapter covers:
 * 1. Basic Lambda Syntax
 * 2. Built-in Functional Interfaces (Predicate, Function, Consumer, Supplier)
 * 3. Custom Functional Interfaces
 * 4. Method References
 * 
 * @see LambdasNotes.md for detailed explanations
 */
public class Main {

    public static void main(String[] args) {
        printHeader();

        // Run each demo in sequence
        BasicSyntaxDemo.runAll();
        BuiltInInterfacesDemo.runAll();
        CustomInterfaceDemo.runAll();
        MethodReferenceDemo.runAll();

        printFooter();
    }

    private static void printHeader() {
        System.out.println("=".repeat(60));
        System.out.println("       LAMBDAS & FUNCTIONAL INTERFACES");
        System.out.println("=".repeat(60));
        System.out.println("\nThis module demonstrates lambda expressions in Java 8+.");
        System.out.println("See LambdasNotes.md for detailed explanations.\n");
    }

    private static void printFooter() {
        System.out.println("\n" + "=".repeat(60));
        System.out.println("       END OF LAMBDA DEMONSTRATIONS");
        System.out.println("=".repeat(60));
        System.out.println("\nNext: Learn how lambdas power the Streams API!");
        System.out.println("See: ../c3_streams/Main.java");
    }
}
