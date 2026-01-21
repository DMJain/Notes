package org.example.p2_Concurrency.c6_mutex_locks;

import org.example.p2_Concurrency.c6_mutex_locks.runner.MutexRunner;

/**
 * Entry point for the Mutex Locks module.
 * 
 * <p>
 * This module demonstrates how to use
 * {@link java.util.concurrent.locks.ReentrantLock}
 * to solve the race condition problem from c5.
 * </p>
 * 
 * <h2>What This Module Covers</h2>
 * <ul>
 * <li>ReentrantLock basics: lock(), unlock()</li>
 * <li>The try-finally pattern for safe unlocking</li>
 * <li>tryLock() for timeout-based locking</li>
 * <li>When to use ReentrantLock vs synchronized</li>
 * </ul>
 * 
 * @see org.example.p2_Concurrency.c5_synchronization_problem for the problem
 * @see org.example.p2_Concurrency.c7_synchronized_keyword for alternative
 *      solution
 */
public class Main {

    public static void main(String[] args) {
        System.out.println("=== Mutex Locks Module ===");
        MutexRunner.runAllScenarios();
    }
}
