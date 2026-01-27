package org.example.p2_Concurrency.c11_semaphores.contracts;

/**
 * Contract for poolable resources that can be borrowed and returned.
 * 
 * @param <T> the type of resource being pooled
 */
public interface IResourcePool<T> {

    /**
     * Borrows a resource from the pool, blocking if none available.
     * 
     * @return the borrowed resource
     * @throws InterruptedException if interrupted while waiting
     */
    T borrowResource() throws InterruptedException;

    /**
     * Returns a borrowed resource to the pool.
     * 
     * @param resource the resource to return
     */
    void returnResource(T resource);

    /**
     * Gets the current number of available resources.
     * 
     * @return available resource count
     */
    int getAvailableCount();

    /**
     * Gets the total pool capacity.
     * 
     * @return maximum resources in pool
     */
    int getTotalCapacity();
}
