package org.example.p2_Concurrency.c6_mutex_locks.contracts;

/**
 * Contract for a thread-safe lockable counter.
 * 
 * <p>
 * This interface defines a counter that uses explicit locking
 * to ensure thread safety. Implementations should use
 * {@link java.util.concurrent.locks.ReentrantLock}.
 * </p>
 * 
 * @see org.example.p2_Concurrency.c6_mutex_locks.impl.ReentrantLockCounter
 */
public interface ILockableCounter {

    /**
     * Increments the counter by 1 in a thread-safe manner.
     * The implementation must acquire a lock before modifying.
     */
    void increment();

    /**
     * Decrements the counter by 1 in a thread-safe manner.
     * The implementation must acquire a lock before modifying.
     */
    void decrement();

    /**
     * Returns the current value of the counter.
     * 
     * @return current count value
     */
    int getCount();
}
