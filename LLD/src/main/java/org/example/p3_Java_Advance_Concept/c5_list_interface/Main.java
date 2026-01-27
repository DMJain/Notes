package org.example.p3_Java_Advance_Concept.c5_list_interface;

import org.example.p3_Java_Advance_Concept.c5_list_interface.runner.ListRunner;

/**
 * Entry point for the List Interface module.
 * 
 * <p>
 * This module covers List implementations: ArrayList, LinkedList, Vector, and
 * Stack.
 * </p>
 * 
 * @see ListRunner
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║               c5_list_interface Module                     ║");
        System.out.println("║         ArrayList, LinkedList, Vector, Stack               ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝");
        System.out.println();

        ListRunner.runAllScenarios();
    }
}
