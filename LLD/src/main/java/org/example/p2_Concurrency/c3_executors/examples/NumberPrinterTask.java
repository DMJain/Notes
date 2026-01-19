package org.example.p2_Concurrency.c3_executors.examples;

/**
 * A simple Runnable task that prints a number.
 * Demonstrates the basic unit of work submitted to an ExecutorService.
 */
public class NumberPrinterTask implements Runnable {
    private final int number;

    public NumberPrinterTask(int number) {
        this.number = number;
    }

    @Override
    public void run() {
        System.out.println("Task " + number + " executed by: " +
                Thread.currentThread().getName());

        // Simulate some work
        try {
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
