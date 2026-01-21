package org.example.p2_Concurrency.c7_synchronized_keyword.impl;

import org.example.p2_Concurrency.c7_synchronized_keyword.contracts.ISynchronizedCounter;

/**
 * Thread-safe counter using synchronized BLOCK with private lock object.
 * 
 * <p>
 * Uses a dedicated lock object instead of 'this'. This is more secure
 * as external code cannot accidentally or maliciously lock on our object.
 * </p>
 */
public class SynchronizedBlockCounter implements ISynchronizedCounter {

    private int count = 0;

    /**
     * Private lock object - external code cannot access this.
     */
    private final Object lock = new Object();

    @Override
    public void increment() {
        synchronized (lock) {
            count++; // Only this line is protected
        }
    }

    @Override
    public void decrement() {
        synchronized (lock) {
            count--;
        }
    }

    @Override
    public int getCount() {
        return count;
    }
}
