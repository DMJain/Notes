package org.example.p2_Concurrency.c6_mutex_locks.impl;

import org.example.p2_Concurrency.c6_mutex_locks.contracts.ILockableCounter;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Thread-safe counter implementation using {@link ReentrantLock}.
 * 
 * <p>
 * This class demonstrates the SOLUTION to the race condition problem
 * shown in c5. By using a lock, we ensure that only one thread can
 * modify the counter at a time.
 * </p>
 * 
 * <h2>How It Works</h2>
 * 
 * <pre>
 *   Thread A: lock() ──► READ ──► MODIFY ──► WRITE ──► unlock()
 *   Thread B: lock() [BLOCKED] ─────────────────────► lock() ──► ...
 * </pre>
 * 
 * <h2>Key Points</h2>
 * <ul>
 * <li>Always use try-finally to ensure unlock() is called</li>
 * <li>The lock is reentrant - same thread can acquire it multiple times</li>
 * <li>Non-fair by default (can pass true for fairness)</li>
 * </ul>
 * 
 * @see org.example.p2_Concurrency.c5_synchronization_problem.impl.UnsafeCounter
 */
public class ReentrantLockCounter implements ILockableCounter {

    /**
     * The shared count variable.
     */
    private int count = 0;

    /**
     * The ReentrantLock that protects access to count.
     * This is the "key" to the "room" (critical section).
     */
    private final Lock lock = new ReentrantLock();

    /**
     * Increments the counter by 1 in a thread-safe manner.
     * 
     * <p>
     * The lock ensures that READ → MODIFY → WRITE happens
     * as an atomic unit, preventing race conditions.
     * </p>
     */
    @Override
    public void increment() {
        lock.lock(); // Acquire the key
        try {
            count++; // Critical section - protected!
        } finally {
            lock.unlock(); // ALWAYS release the key, even on exception
        }
    }

    /**
     * Decrements the counter by 1 in a thread-safe manner.
     */
    @Override
    public void decrement() {
        lock.lock();
        try {
            count--;
        } finally {
            lock.unlock();
        }
    }

    /**
     * Returns the current value of the counter.
     * 
     * @return current count value
     */
    @Override
    public int getCount() {
        return count;
    }
}
