package org.example.p2_Concurrency.c12_producer_consumer.contracts;

/**
 * Contract for a bounded buffer in the Producer-Consumer pattern.
 * 
 * <p>
 * Defines blocking put/take operations for thread-safe
 * data exchange between producers and consumers.
 * </p>
 * 
 * @param <T> the type of items in the buffer
 */
public interface IBuffer<T> {

    /**
     * Adds an item to the buffer.
     * Blocks if the buffer is full until space becomes available.
     * 
     * @param item the item to add
     * @throws InterruptedException if interrupted while waiting
     */
    void put(T item) throws InterruptedException;

    /**
     * Removes and returns an item from the buffer.
     * Blocks if the buffer is empty until an item becomes available.
     * 
     * @return the item removed from the buffer
     * @throws InterruptedException if interrupted while waiting
     */
    T take() throws InterruptedException;

    /**
     * Returns the current number of items in the buffer.
     * 
     * @return current item count
     */
    int size();

    /**
     * Returns the maximum capacity of the buffer.
     * 
     * @return buffer capacity
     */
    int capacity();

    /**
     * Checks if the buffer is empty.
     * 
     * @return true if empty
     */
    boolean isEmpty();

    /**
     * Checks if the buffer is full.
     * 
     * @return true if full
     */
    boolean isFull();
}
