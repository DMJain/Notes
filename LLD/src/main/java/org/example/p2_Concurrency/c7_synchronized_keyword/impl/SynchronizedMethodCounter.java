package org.example.p2_Concurrency.c7_synchronized_keyword.impl;

import org.example.p2_Concurrency.c7_synchronized_keyword.contracts.ISynchronizedCounter;

/**
 * Thread-safe counter using synchronized METHOD.
 * 
 * <p>
 * The entire method is the critical section. The lock used is 'this'.
 * </p>
 */
public class SynchronizedMethodCounter implements ISynchronizedCounter {

    private int count = 0;

    /**
     * Increments counter. Uses 'this' as lock object.
     */
    @Override
    public synchronized void increment() {
        count++; // Entire method is protected
    }

    /**
     * Decrements counter. Uses same 'this' lock.
     */
    @Override
    public synchronized void decrement() {
        count--;
    }

    @Override
    public int getCount() {
        return count;
    }
}
