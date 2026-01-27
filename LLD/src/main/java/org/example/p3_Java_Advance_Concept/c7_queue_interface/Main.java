package org.example.p3_Java_Advance_Concept.c7_queue_interface;

import org.example.p3_Java_Advance_Concept.c7_queue_interface.runner.QueueRunner;

/**
 * Entry point for the Queue Interface module.
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("╔═══════════════════════════════════════════════════════════╗");
        System.out.println("║              c7_queue_interface Module                     ║");
        System.out.println("║           Queue, Deque, PriorityQueue                      ║");
        System.out.println("╚═══════════════════════════════════════════════════════════╝");
        System.out.println();

        QueueRunner.runAllScenarios();
    }
}
