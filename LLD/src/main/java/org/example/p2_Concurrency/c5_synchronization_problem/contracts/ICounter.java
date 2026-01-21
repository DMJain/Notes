package org.example.p2_Concurrency.c5_synchronization_problem.contracts;

/**
 * Contract for a Counter that can be incremented and decremented.
 * 
 * <p>
 * This interface defines the basic operations for a counter.
 * Implementations may or may NOT be thread-safe - that's the point
 * of this module: to demonstrate what happens without synchronization.
 * </p>
 * 
 * @see org.example.p2_Concurrency.c5_synchronization_problem.impl.UnsafeCounter
 */
public interface ICounter {

    /**
     * Increments the counter by 1.
     */
    void increment();

    /**
     * Decrements the counter by 1.
     */
    void decrement();

    /**
     * Returns the current value of the counter.
     * 
     * @return current count value
     */
    int getCount();
}
