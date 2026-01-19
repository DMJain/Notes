package org.example.p2_Concurrency.c4_callables.examples;

import java.util.concurrent.Callable;

/**
 * A simple Callable that calculates the sum of numbers from 1 to n.
 * 
 * Unlike Runnable, Callable CAN return a value!
 * 
 * This demonstrates the core benefit of Callable over Runnable.
 */
public class SumCalculator implements Callable<Long> {
    private final int n;

    public SumCalculator(int n) {
        this.n = n;
    }

    @Override
    public Long call() throws Exception {
        System.out.println("Calculating sum(1.." + n + ") on " +
                Thread.currentThread().getName());

        long sum = 0;
        for (int i = 1; i <= n; i++) {
            sum += i;
            // Simulate some work
            if (i % 1000000 == 0) {
                Thread.sleep(10);
            }
        }

        System.out.println("Calculation complete! Sum = " + sum);
        return sum; // This is the magic - we RETURN the result!
    }
}
