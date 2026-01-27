package org.example.p2_Concurrency.c13_deadlocks.contracts;

/**
 * Contract for a lockable resource.
 */
public interface IResource {

    /**
     * Gets the unique identifier of this resource.
     * 
     * @return resource ID
     */
    int getId();

    /**
     * Gets the name of this resource.
     * 
     * @return resource name
     */
    String getName();

    /**
     * Acquires exclusive access to this resource.
     * Blocks if resource is held by another thread.
     */
    void acquire();

    /**
     * Releases this resource.
     */
    void release();

    /**
     * Attempts to acquire this resource without blocking.
     * 
     * @return true if acquired, false if not available
     */
    boolean tryAcquire();
}
