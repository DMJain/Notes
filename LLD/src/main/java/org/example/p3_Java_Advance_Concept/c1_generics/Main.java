package org.example.p3_Java_Advance_Concept.c1_generics;

import org.example.p3_Java_Advance_Concept.c1_generics.runner.GenericsRunner;

/**
 * Entry point for the Java Generics module.
 * <p>
 * Run this class to see all generics concepts demonstrated:
 * <ol>
 * <li>Generic Classes (Box, Pair)</li>
 * <li>Generic Methods</li>
 * <li>Wildcards (?, extends, super)</li>
 * <li>Bounded Generics</li>
 * <li>Generic Interfaces</li>
 * <li>Type Erasure</li>
 * </ol>
 * </p>
 */
public class Main {

    public static void main(String[] args) {
        System.out.println();
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("                    JAVA GENERICS MODULE                       ");
        System.out.println("                p3_Java_Advance_Concept/c1_generics            ");
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println();

        GenericsRunner.runAllScenarios();
    }
}
