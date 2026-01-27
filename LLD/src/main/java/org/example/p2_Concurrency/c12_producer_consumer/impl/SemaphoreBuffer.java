package org.example.p2_Concurrency.c12_producer_consumer.impl;

import org.example.p2_Concurrency.c12_producer_consumer.contracts.IBuffer;

import java.util.LinkedList;
import java.util.Queue;
import java.util.concurrent.Semaphore;

/**
 * Semaphore-based bounded buffer implementation.
 * 
 * <p>
 * Uses three semaphores for Producer-Consumer coordination:
 * </p>
 * <ul>
 * <li><b>mutex</b>: Protects buffer access (1 permit)</li>
 * <li><b>empty</b>: Tracks empty slots (capacity permits)</li>
 * <li><b>full</b>: Tracks filled slots (0 permits initially)</li>
 * </ul>
 * 
 * <h3>How It Works:</h3>
 * 
 * <pre>
 *   Producer: empty.acquire() → mutex.acquire() → add → mutex.release() → full.release()
 *   Consumer: full.acquire() → mutex.acquire() → take → mutex.release() → empty.release()
 * </pre>
 * 
 * @param <T> the type of items in the buffer
 * @see java.util.concurrent.Semaphore
 */
public class SemaphoreBuffer<T> implements IBuffer<T> {

    private final Queue<T> buffer;
    private final int capacity;

    private final Semaphore mutex; // Binary semaphore for mutual exclusion
    private final Semaphore empty; // Counting semaphore for empty slots
    private final Semaphore full; // Counting semaphore for filled slots

    /**
     * Creates a semaphore-based buffer with specified capacity.
     * 
     * @param capacity maximum items the buffer can hold
     */
    public SemaphoreBuffer(int capacity) {
        this.capacity = capacity;
        this.buffer = new LinkedList<>();

        // Initialize semaphores
        this.mutex = new Semaphore(1); // Binary: only 1 thread in critical section
        this.empty = new Semaphore(capacity); // All slots empty initially
        this.full = new Semaphore(0); // No items initially

        System.out.printf("📦 SemaphoreBuffer created (capacity: %d)%n", capacity);
    }

    /**
     * Adds an item to the buffer.
     * Blocks if buffer is full until a consumer takes an item.
     * 
     * @param item the item to add
     * @throws InterruptedException if interrupted while waiting
     */
    @Override
    public void put(T item) throws InterruptedException {
        String threadName = Thread.currentThread().getName();

        // Step 1: Wait for an empty slot
        System.out.printf("   [%s] Waiting for empty slot...%n", threadName);
        empty.acquire();

        // Step 2: Enter critical section
        mutex.acquire();
        try {
            // Step 3: Add item to buffer
            buffer.add(item);
            System.out.printf("   [%s] ✅ PUT: %s (buffer size: %d/%d)%n",
                    threadName, item, buffer.size(), capacity);
        } finally {
            // Step 4: Exit critical section
            mutex.release();
        }

        // Step 5: Signal that an item is available
        full.release();
    }

    /**
     * Takes an item from the buffer.
     * Blocks if buffer is empty until a producer adds an item.
     * 
     * @return the item taken from buffer
     * @throws InterruptedException if interrupted while waiting
     */
    @Override
    public T take() throws InterruptedException {
        String threadName = Thread.currentThread().getName();

        // Step 1: Wait for an available item
        System.out.printf("   [%s] Waiting for item...%n", threadName);
        full.acquire();

        // Step 2: Enter critical section
        mutex.acquire();
        T item;
        try {
            // Step 3: Remove item from buffer
            item = buffer.poll();
            System.out.printf("   [%s] ✅ TAKE: %s (buffer size: %d/%d)%n",
                    threadName, item, buffer.size(), capacity);
        } finally {
            // Step 4: Exit critical section
            mutex.release();
        }

        // Step 5: Signal that a slot is available
        empty.release();

        return item;
    }

    @Override
    public int size() {
        return buffer.size();
    }

    @Override
    public int capacity() {
        return capacity;
    }

    @Override
    public boolean isEmpty() {
        return buffer.isEmpty();
    }

    @Override
    public boolean isFull() {
        return buffer.size() >= capacity;
    }

    /**
     * Displays current buffer state.
     */
    public void displayStatus() {
        System.out.printf("📊 Buffer: %d/%d | Empty permits: %d | Full permits: %d%n",
                buffer.size(), capacity, empty.availablePermits(), full.availablePermits());
    }
}
