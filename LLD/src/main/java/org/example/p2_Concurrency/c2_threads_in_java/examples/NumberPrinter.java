package org.example.p2_Concurrency.c2_threads_in_java.examples;

/**
 * Demonstrates implementing Runnable interface for thread tasks.
 * 
 * This is the PREFERRED way to create threads in Java because:
 * 1. Allows extending other classes (doesn't waste single inheritance)
 * 2. Represents a TASK, not a thread type (correct IS-A relationship)
 * 3. Supports lambda expressions (Runnable is @FunctionalInterface)
 * 4. Follows composition over inheritance principle
 * 
 * Problem: Print numbers from 1 to 100 using 100 different threads.
 * 
 * @see java.lang.Runnable
 */
public class NumberPrinter implements Runnable {

    private final int number;

    /**
     * Creates a NumberPrinter task for the given number.
     * 
     * @param number the number this task will print
     */
    public NumberPrinter(int number) {
        this.number = number;
    }

    /**
     * The run method contains the code that executes on the thread.
     * When this task is passed to a Thread and start() is called,
     * this method runs on the NEW thread.
     */
    @Override
    public void run() {
        // Get the name of the thread executing this code
        String threadName = Thread.currentThread().getName();

        // Simulate some work with a small delay
        try {
            Thread.sleep(10); // 10ms delay
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("Printing " + number + " from " + threadName);
    }

    /**
     * Demonstrates creating and starting threads with NumberPrinter.
     */
    public static void runDemo() {
        System.out.println("\n╔═══════════════════════════════════════════════╗");
        System.out.println("║     DEMO 1: NumberPrinter (Runnable)          ║");
        System.out.println("╚═══════════════════════════════════════════════╝\n");

        System.out.println("Creating 10 threads to print numbers 1-10...");
        System.out.println("Notice: Numbers may print OUT OF ORDER!\n");

        // Create and start 10 threads
        for (int i = 1; i <= 10; i++) {
            // Create a Runnable task
            Runnable task = new NumberPrinter(i);

            // Wrap it in a Thread
            Thread thread = new Thread(task);

            // Optionally set a custom name
            thread.setName("NumberThread-" + i);

            // Start the thread (calls run() on a NEW thread)
            thread.start();
        }

        // Wait a bit for all threads to complete
        try {
            Thread.sleep(500);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        System.out.println("\n💡 Notice how numbers printed in random order!");
        System.out.println("   This demonstrates concurrent execution.\n");
    }
}
