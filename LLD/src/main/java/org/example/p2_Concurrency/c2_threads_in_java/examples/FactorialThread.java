package org.example.p2_Concurrency.c2_threads_in_java.examples;

import java.math.BigInteger;

/**
 * Demonstrates extending the Thread class for thread creation.
 * 
 * NOTE: This approach is shown for completeness, but implementing
 * Runnable is PREFERRED. See NumberPrinter.java for the better approach.
 * 
 * Why this approach is less ideal:
 * 1. Wastes single inheritance - can't extend other classes
 * 2. Wrong IS-A relationship - FactorialThread is NOT a type of Thread
 * 3. Cannot use with lambdas
 * 
 * Problem: Compute factorials of large numbers on separate threads,
 * with a timeout of 2 seconds per computation.
 * 
 * @see NumberPrinter for the preferred Runnable approach
 */
public class FactorialThread extends Thread {

    private final long number;
    private BigInteger result;
    private boolean isFinished;

    /**
     * Creates a FactorialThread for computing factorial of given number.
     * 
     * @param number the number to compute factorial of
     */
    public FactorialThread(long number) {
        this.number = number;
        this.result = BigInteger.ZERO;
        this.isFinished = false;
    }

    /**
     * Computes the factorial and stores the result.
     * This method runs on a separate thread when start() is called.
     */
    @Override
    public void run() {
        result = computeFactorial(number);
        isFinished = true;
    }

    /**
     * Computes factorial using iterative approach.
     * Uses BigInteger to handle very large numbers.
     * 
     * @param n the number to compute factorial of
     * @return n! as BigInteger
     */
    private BigInteger computeFactorial(long n) {
        BigInteger factorial = BigInteger.ONE;
        for (long i = 2; i <= n; i++) {
            factorial = factorial.multiply(BigInteger.valueOf(i));
        }
        return factorial;
    }

    /**
     * @return the computed factorial result
     */
    public BigInteger getResult() {
        return result;
    }

    /**
     * @return true if computation has finished
     */
    public boolean isFinished() {
        return isFinished;
    }

    /**
     * @return the input number
     */
    public long getNumber() {
        return number;
    }

    /**
     * Demonstrates factorial computation with timeout using join().
     */
    public static void runDemo() {
        System.out.println("\n╔═══════════════════════════════════════════════╗");
        System.out.println("║   DEMO 2: FactorialThread (extends Thread)    ║");
        System.out.println("╚═══════════════════════════════════════════════╝\n");

        // Mix of fast and slow computations
        // 100000000L will be too slow (timeout)
        long[] numbers = { 10L, 20L, 100L, 1000L, 100000000L };

        System.out.println("Computing factorials with 2-second timeout each...\n");

        // Create threads
        FactorialThread[] threads = new FactorialThread[numbers.length];
        for (int i = 0; i < numbers.length; i++) {
            threads[i] = new FactorialThread(numbers[i]);
        }

        // Start all threads
        for (FactorialThread t : threads) {
            t.start();
        }

        // Wait for each thread with timeout
        for (FactorialThread t : threads) {
            try {
                t.join(2000); // Wait max 2 seconds
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // Print results
        for (FactorialThread t : threads) {
            if (t.isFinished()) {
                String resultStr = t.getResult().toString();
                if (resultStr.length() > 50) {
                    resultStr = resultStr.substring(0, 25) + "..." +
                            resultStr.substring(resultStr.length() - 25) +
                            " (" + resultStr.length() + " digits)";
                }
                System.out.println("✅ " + t.getNumber() + "! = " + resultStr);
            } else {
                System.out.println("❌ " + t.getNumber() + "! = Couldn't complete in 2 seconds!");
            }
        }

        System.out.println("\n💡 join(2000) waits max 2 seconds for thread to complete.");
        System.out.println("   If thread isn't done, we continue anyway (timeout).\n");
    }
}
