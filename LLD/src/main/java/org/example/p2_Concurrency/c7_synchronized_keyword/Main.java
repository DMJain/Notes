package org.example.p2_Concurrency.c7_synchronized_keyword;

import org.example.p2_Concurrency.c7_synchronized_keyword.runner.SyncRunner;

/**
 * Entry point for the synchronized keyword module.
 */
public class Main {
    public static void main(String[] args) {
        System.out.println("=== synchronized Keyword Module ===");
        SyncRunner.runAllScenarios();
    }
}
