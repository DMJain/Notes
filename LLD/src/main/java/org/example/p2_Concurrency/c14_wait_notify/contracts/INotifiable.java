package org.example.p2_Concurrency.c14_wait_notify.contracts;

/**
 * Contract for objects that can signal waiting threads.
 */
public interface INotifiable {

    /**
     * Waits until condition is met.
     * 
     * @throws InterruptedException if interrupted while waiting
     */
    void awaitCondition() throws InterruptedException;

    /**
     * Signals that condition may have changed.
     */
    void signalCondition();
}
