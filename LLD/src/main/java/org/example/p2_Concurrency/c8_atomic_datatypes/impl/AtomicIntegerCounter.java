package org.example.p2_Concurrency.c8_atomic_datatypes.impl;

import org.example.p2_Concurrency.c8_atomic_datatypes.contracts.IAtomicCounter;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Thread-safe counter using AtomicInteger (lock-free).
 * 
 * <p>
 * Uses hardware-level CAS (Compare-And-Swap) operations
 * instead of locks for thread safety.
 * </p>
 */
public class AtomicIntegerCounter implements IAtomicCounter {

    private AtomicInteger count = new AtomicInteger(0);

    @Override
    public void increment() {
        count.incrementAndGet(); // Atomic CAS operation
    }

    @Override
    public void decrement() {
        count.decrementAndGet();
    }

    @Override
    public int getCount() {
        return count.get();
    }
}
