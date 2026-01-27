package org.example.p2_Concurrency.c11_semaphores.contracts;

/**
 * Contract for semaphore-controlled resource access.
 * 
 * <p>
 * Defines operations for acquiring and releasing permits
 * from a bounded resource pool.
 * </p>
 * 
 * @see java.util.concurrent.Semaphore
 */
public interface ISemaphoreController {

    /**
     * Acquires a permit, blocking until one is available.
     * 
     * @throws InterruptedException if the thread is interrupted while waiting
     */
    void acquirePermit() throws InterruptedException;

    /**
     * Attempts to acquire a permit without blocking.
     * 
     * @return true if permit was acquired, false otherwise
     */
    boolean tryAcquirePermit();

    /**
     * Releases a permit back to the pool.
     */
    void releasePermit();

    /**
     * Returns the current number of available permits.
     * 
     * @return available permit count (snapshot)
     */
    int getAvailablePermits();
}
