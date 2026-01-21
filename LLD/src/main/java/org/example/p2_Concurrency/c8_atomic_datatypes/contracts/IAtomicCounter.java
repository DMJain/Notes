package org.example.p2_Concurrency.c8_atomic_datatypes.contracts;

/**
 * Contract for an atomic counter using lock-free operations.
 */
public interface IAtomicCounter {
    void increment();

    void decrement();

    int getCount();
}
