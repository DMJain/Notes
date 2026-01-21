package org.example.p2_Concurrency.c7_synchronized_keyword.contracts;

/**
 * Contract for a counter using synchronized for thread-safety.
 */
public interface ISynchronizedCounter {
    void increment();

    void decrement();

    int getCount();
}
