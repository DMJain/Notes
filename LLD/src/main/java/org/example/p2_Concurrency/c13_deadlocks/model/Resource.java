package org.example.p2_Concurrency.c13_deadlocks.model;

import org.example.p2_Concurrency.c13_deadlocks.contracts.IResource;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * A lockable resource that can be acquired by threads.
 * 
 * <p>
 * Used for demonstrating deadlock scenarios.
 * </p>
 */
public class Resource implements IResource {

    private final int id;
    private final String name;
    private final Lock lock;

    public Resource(int id, String name) {
        this.id = id;
        this.name = name;
        this.lock = new ReentrantLock();
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public void acquire() {
        String threadName = Thread.currentThread().getName();
        System.out.printf("   [%s] Acquiring %s...%n", threadName, name);
        lock.lock();
        System.out.printf("   [%s] ✅ Acquired %s%n", threadName, name);
    }

    @Override
    public void release() {
        String threadName = Thread.currentThread().getName();
        lock.unlock();
        System.out.printf("   [%s] 🔓 Released %s%n", threadName, name);
    }

    @Override
    public boolean tryAcquire() {
        return lock.tryLock();
    }

    @Override
    public String toString() {
        return String.format("Resource[%d: %s]", id, name);
    }
}
