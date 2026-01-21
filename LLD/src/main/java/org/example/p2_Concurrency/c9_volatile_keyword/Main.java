package org.example.p2_Concurrency.c9_volatile_keyword;

import org.example.p2_Concurrency.c9_volatile_keyword.runner.VolatileRunner;

/**
 * Entry point for the volatile keyword module.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("=== volatile Keyword Module ===");
        VolatileRunner.runAllScenarios();
    }
}
