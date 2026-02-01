package org.example.p3_Java_Advance_Concept.c15_exception_chaining;

import org.example.p3_Java_Advance_Concept.c15_exception_chaining.impl.ExceptionChainingDemo;

public class Main {
    public static void main(String[] args) {
        System.out.println();
        System.out.println("═══════════════════════════════════════════════════════════════");
        System.out.println("                 EXCEPTION CHAINING MODULE                     ");
        System.out.println("        p3_Java_Advance_Concept/c15_exception_chaining         ");
        System.out.println("═══════════════════════════════════════════════════════════════");

        ExceptionChainingDemo.runDemo();

        System.out.println("╔══════════════════════════════════════════════════════════════════╗");
        System.out.println("║  SUMMARY: Always pass cause when wrapping exceptions!            ║");
        System.out.println("║  NEXT: c16_try_with_resources - Automatic resource cleanup       ║");
        System.out.println("╚══════════════════════════════════════════════════════════════════╝");
    }
}
