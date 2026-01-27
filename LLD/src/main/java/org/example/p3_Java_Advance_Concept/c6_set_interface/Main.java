package org.example.p3_Java_Advance_Concept.c6_set_interface;

import org.example.p3_Java_Advance_Concept.c6_set_interface.runner.SetRunner;

/**
 * Entry point for the Set Interface module.
 * 
 * <p>
 * This module covers Set implementations: HashSet, LinkedHashSet, TreeSet.
 * </p>
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║                c6_set_interface Module                     ║");
        System.out.println("║           HashSet, LinkedHashSet, TreeSet                  ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝");
        System.out.println();

        SetRunner.runAllScenarios();
    }
}
