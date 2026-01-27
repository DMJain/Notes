package org.example.p2_Concurrency.c14_wait_notify.impl;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Bounded buffer using wait/notify for Producer-Consumer.
 * 
 * <p>
 * Demonstrates the classic wait/notify pattern with:
 * </p>
 * <ul>
 * <li>While loop for condition checking (not if!)</li>
 * <li>notifyAll() to wake all waiting threads</li>
 * <li>Synchronized methods for thread safety</li>
 * </ul>
 * 
 * @param <T> the type of items in the buffer
 */
public class WaitNotifyBuffer<T> {

    private final Queue<T> buffer;
    private final int capacity;

    public WaitNotifyBuffer(int capacity) {
        this.capacity = capacity;
        this.buffer = new LinkedList<>();
        System.out.printf("📦 WaitNotifyBuffer created (capacity: %d)%n", capacity);
    }

    /**
     * Adds an item to the buffer.
     * Blocks if buffer is full until space is available.
     * 
     * @param item the item to add
     * @throws InterruptedException if interrupted while waiting
     */
    public synchronized void put(T item) throws InterruptedException {
        String threadName = Thread.currentThread().getName();

        // WHILE loop - essential for spurious wakeups!
        while (buffer.size() >= capacity) {
            System.out.printf("   [%s] Buffer full, waiting...%n", threadName);
            wait(); // Releases lock, goes to sleep
        }

        buffer.add(item);
        System.out.printf("   [%s] ✅ PUT: %s (size: %d/%d)%n",
                threadName, item, buffer.size(), capacity);

        // Wake up ALL waiting consumers (and producers for safety)
        notifyAll();
    }

    /**
     * Takes an item from the buffer.
     * Blocks if buffer is empty until an item is available.
     * 
     * @return the item taken
     * @throws InterruptedException if interrupted while waiting
     */
    public synchronized T take() throws InterruptedException {
        String threadName = Thread.currentThread().getName();

        // WHILE loop - essential for spurious wakeups and race conditions!
        while (buffer.isEmpty()) {
            System.out.printf("   [%s] Buffer empty, waiting...%n", threadName);
            wait(); // Releases lock, goes to sleep
        }

        T item = buffer.poll();
        System.out.printf("   [%s] ✅ TAKE: %s (size: %d/%d)%n",
                threadName, item, buffer.size(), capacity);

        // Wake up ALL waiting producers (and consumers for safety)
        notifyAll();

        return item;
    }

    /**
     * Returns current buffer size.
     */
    public synchronized int size() {
        return buffer.size();
    }

    /**
     * Checks if buffer is empty.
     */
    public synchronized boolean isEmpty() {
        return buffer.isEmpty();
    }

    /**
     * Checks if buffer is full.
     */
    public synchronized boolean isFull() {
        return buffer.size() >= capacity;
    }

    /**
     * Displays buffer status.
     */
    public synchronized void displayStatus() {
        System.out.printf("📊 Buffer: %d/%d%n", buffer.size(), capacity);
    }
}
