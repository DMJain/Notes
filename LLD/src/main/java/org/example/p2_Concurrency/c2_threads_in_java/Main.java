package org.example.p2_Concurrency.c2_threads_in_java;

import org.example.p2_Concurrency.c2_threads_in_java.examples.FactorialThread;
import org.example.p2_Concurrency.c2_threads_in_java.examples.NumberPrinter;
import org.example.p2_Concurrency.c2_threads_in_java.examples.ThreadLifecycleDemo;

/**
 * Threads in Java - Entry Point
 * 
 * This chapter covers practical thread creation and management:
 * - Thread class vs Runnable interface
 * - start() vs run()
 * - Thread lifecycle and states
 * - Essential thread methods (sleep, join, interrupt)
 * 
 * Run this class to see all demos in action!
 * 
 * @see ThreadsNotes.md for detailed explanations
 */
public class Main {

    public static void main(String[] args) {
        printHeader();

        // Demo 1: NumberPrinter (Runnable implementation)
        NumberPrinter.runDemo();

        // Demo 2: FactorialThread (Thread subclass with join timeout)
        FactorialThread.runDemo();

        // Demo 3: Thread Lifecycle States
        ThreadLifecycleDemo.runDemo();

        // Demo 4: Lambda-based threads (modern approach)
        demonstrateLambdaThreads();

        // Demo 5: start() vs run() difference
        demonstrateStartVsRun();

        printFooter();
    }

    private static void printHeader() {
        System.out.println("╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║         CHAPTER 2: THREADS IN JAVA                            ║");
        System.out.println("╠═══════════════════════════════════════════════════════════════╣");
        System.out.println("║                                                               ║");
        System.out.println("║  This chapter demonstrates:                                   ║");
        System.out.println("║    ✓ Runnable interface (preferred way)                       ║");
        System.out.println("║    ✓ Extending Thread class                                   ║");
        System.out.println("║    ✓ Thread lifecycle states                                  ║");
        System.out.println("║    ✓ Lambda-based threads (Java 8+)                           ║");
        System.out.println("║    ✓ start() vs run() difference                              ║");
        System.out.println("║                                                               ║");
        System.out.println("║  📖 Read: ThreadsNotes.md for detailed explanations           ║");
        System.out.println("║                                                               ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");
    }

    /**
     * Demonstrates creating threads with lambda expressions.
     * This is the most concise way in modern Java.
     */
    private static void demonstrateLambdaThreads() {
        System.out.println("\n╔═══════════════════════════════════════════════╗");
        System.out.println("║    DEMO 4: Lambda-Based Threads (Java 8+)     ║");
        System.out.println("╚═══════════════════════════════════════════════╝\n");

        System.out.println("Creating threads with lambda expressions:\n");

        // Most concise form
        Thread t1 = new Thread(() -> {
            System.out.println("   Hello from lambda thread: " +
                    Thread.currentThread().getName());
        });
        t1.setName("Lambda-Thread-1");

        // With multiple statements
        Thread t2 = new Thread(() -> {
            String name = Thread.currentThread().getName();
            for (int i = 1; i <= 3; i++) {
                System.out.println("   " + name + " counting: " + i);
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
            }
        });
        t2.setName("Lambda-Thread-2");

        t1.start();
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("\n💡 Lambda syntax: new Thread(() -> { /* code */ }).start();");
        System.out.println("   Most concise way to create threads!\n");
    }

    /**
     * Demonstrates the critical difference between start() and run().
     */
    private static void demonstrateStartVsRun() {
        System.out.println("\n╔═══════════════════════════════════════════════╗");
        System.out.println("║      DEMO 5: start() vs run() Difference      ║");
        System.out.println("╚═══════════════════════════════════════════════╝\n");

        Runnable task = () -> {
            System.out.println("   Executing on: " + Thread.currentThread().getName());
        };

        Thread thread = new Thread(task);
        thread.setName("NewThread");

        System.out.println("Calling run() directly (WRONG):");
        thread.run(); // This runs on main thread!

        System.out.println("\nCalling start() (CORRECT):");
        Thread thread2 = new Thread(task);
        thread2.setName("ActualNewThread");
        thread2.start(); // This creates a new thread!

        try {
            thread2.join();
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("\n⚠️  GOTCHA: run() executes on CURRENT thread!");
        System.out.println("   Only start() creates a NEW thread.\n");
    }

    private static void printFooter() {
        System.out.println("╔═══════════════════════════════════════════════════════════════╗");
        System.out.println("║                     DEMOS COMPLETE                            ║");
        System.out.println("╠═══════════════════════════════════════════════════════════════╣");
        System.out.println("║  Key Takeaways:                                               ║");
        System.out.println("║  • Use Runnable (not extends Thread)                          ║");
        System.out.println("║  • Always call start(), never run()                           ║");
        System.out.println("║  • Use join() to wait for thread completion                   ║");
        System.out.println("║  • Lambda syntax is cleanest for simple tasks                 ║");
        System.out.println("║                                                               ║");
        System.out.println("║  → Next: c3_synchronization for thread safety!                ║");
        System.out.println("╚═══════════════════════════════════════════════════════════════╝");
    }
}
