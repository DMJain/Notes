package org.example.p2_Concurrency.c2_threads_in_java.interfaces;

/**
 * Functional interface for number-based tasks.
 * 
 * This demonstrates how to create custom functional interfaces
 * for thread tasks. Since Runnable is already a functional interface,
 * this is primarily for demonstration purposes.
 * 
 * @see java.lang.Runnable
 */
@FunctionalInterface
public interface NumberTask {

    /**
     * Process a number in some way.
     * The implementation defines what "processing" means.
     * 
     * @param number the number to process
     */
    void process(int number);
}
