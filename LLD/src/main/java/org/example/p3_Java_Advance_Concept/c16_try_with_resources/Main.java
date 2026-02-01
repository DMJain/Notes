package org.example.p3_Java_Advance_Concept.c16_try_with_resources;

import org.example.p3_Java_Advance_Concept.c16_try_with_resources.impl.TryWithResourcesDemo;

public class Main {
    public static void main(String[] args) {
        System.out.println();
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("                TRY-WITH-RESOURCES MODULE                      ");
        System.out.println("       p3_Java_Advance_Concept/c16_try_with_resources          ");
        System.out.println("═══════════════════════════════════════════════════════════════");

        TryWithResourcesDemo.runDemo();

        System.out.println("╔══════════════════════════════════════════════════════════════════╗");
        System.out.println("║  SUMMARY: Use try(...) for AutoCloseable - auto cleanup!         ║");
        System.out.println("║  NEXT: c17_exception_best_practices - Production guidelines      ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════╝");
    }
}
