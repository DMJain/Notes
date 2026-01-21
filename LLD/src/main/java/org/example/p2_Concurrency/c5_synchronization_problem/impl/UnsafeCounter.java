package org.example.p2_Concurrency.c5_synchronization_problem.impl;

import org.example.p2_Concurrency.c5_synchronization_problem.contracts.ICounter;

/**
 * A deliberately UNSAFE counter implementation to demonstrate race conditions.
 * 
 * <p>
 * <b>WARNING:</b> This class is NOT thread-safe! It is designed to show
 * what happens when multiple threads access shared mutable state without
 * synchronization.
 * </p>
 * 
 * <h2>Why is this unsafe?</h2>
 * <p>
 * The operation {@code count++} is NOT atomic. It actually consists of:
 * </p>
 * <ol>
 * <li>READ: Load current value of count</li>
 * <li>MODIFY: Add 1 to the loaded value</li>
 * <li>WRITE: Store the result back</li>
 * </ol>
 * 
 * <p>
 * A thread can be interrupted between any of these steps, causing
 * race conditions and lost updates.
 * </p>
 * 
 * @see org.example.p2_Concurrency.c6_mutex_locks.impl.ReentrantLockCounter for
 *      the safe version
 */
public class UnsafeCounter implements ICounter {

    /**
     * The shared count variable.
     * 
     * <p>
     * This is the source of the race condition - multiple threads
     * read and write this without any protection.
     * </p>
     */
    private int count = 0;

    /**
     * Increments the counter by 1.
     * 
     * <p>
     * <b>NOT THREAD-SAFE!</b> The {@code count++} operation is:
     * 
     * <pre>
     * temp = count; // READ
     * temp = temp + 1; // MODIFY
     * count = temp; // WRITE
     * </pre>
     * 
     * Thread can be interrupted between any of these steps.
     * </p>
     */
    @Override
    public void increment() {
        count++; // NOT atomic! This is the problem.
    }

    /**
     * Decrements the counter by 1.
     * 
     * <p>
     * <b>NOT THREAD-SAFE!</b> Same issue as increment().
     * </p>
     */
    @Override
    public void decrement() {
        count--; // NOT atomic! This is the problem.
    }

    /**
     * Returns the current value of the counter.
     * 
     * @return current count value (may be stale due to race conditions)
     */
    @Override
    public int getCount() {
        return count;
    }
}
