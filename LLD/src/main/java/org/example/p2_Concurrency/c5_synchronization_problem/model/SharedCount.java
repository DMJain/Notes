package org.example.p2_Concurrency.c5_synchronization_problem.model;

import org.example.p2_Concurrency.c5_synchronization_problem.contracts.ICounter;

/**
 * A wrapper that holds a shared counter for demonstration purposes.
 * 
 * <p>
 * This class encapsulates a counter instance that will be shared
 * across multiple threads to demonstrate the synchronization problem.
 * </p>
 */
public class SharedCount {

    private final ICounter counter;

    /**
     * Creates a SharedCount with the specified counter implementation.
     * 
     * @param counter the counter implementation to use
     */
    public SharedCount(ICounter counter) {
        this.counter = counter;
    }

    /**
     * Gets the underlying counter.
     * 
     * @return the counter instance
     */
    public ICounter getCounter() {
        return counter;
    }

    /**
     * Convenience method to get the current count value.
     * 
     * @return current count
     */
    public int getValue() {
        return counter.getCount();
    }
}
