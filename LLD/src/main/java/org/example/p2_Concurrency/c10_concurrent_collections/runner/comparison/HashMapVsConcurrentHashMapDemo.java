package org.example.p2_Concurrency.c10_concurrent_collections.runner.comparison;

import java.util.*;
import java.util.concurrent.*;

/**
 * Compares HashMap (unsafe) vs ConcurrentHashMap (safe).
 */
public class HashMapVsConcurrentHashMapDemo {

    private static final int ITERATIONS = 10_000;
    private static final int THREADS = 10;

    public void execute() {
        System.out.println("╔══════════════════════════════════════════════════════════╗");
        System.out.println("║       HashMap vs ConcurrentHashMap                       ║");
        System.out.println("╚══════════════════════════════════════════════════════════╝");
        System.out.println();

        // Test ConcurrentHashMap (should work correctly)
        System.out.println("  Testing ConcurrentHashMap (thread-safe):");
        testMap(new ConcurrentHashMap<>(), "ConcurrentHashMap");

        // Test synchronizedMap
        System.out.println("  Testing Collections.synchronizedMap:");
        testMap(Collections.synchronizedMap(new HashMap<>()), "synchronizedMap");

        System.out.println();
        System.out.println("  ┌─────────────────────────────────────────────────────────┐");
        System.out.println("  │  ConcurrentHashMap advantages:                          │");
        System.out.println("  │  • Bucket-level locking (better concurrency)            │");
        System.out.println("  │  • Lock-free reads in Java 8+                           │");
        System.out.println("  │  • Atomic putIfAbsent(), compute() methods              │");
        System.out.println("  └─────────────────────────────────────────────────────────┘");
        System.out.println();
    }

    private void testMap(Map<String, Integer> map, String name) {
        ExecutorService executor = Executors.newFixedThreadPool(THREADS);

        // All threads increment the same key's value
        for (int i = 0; i < THREADS; i++) {
            executor.submit(() -> {
                for (int j = 0; j < ITERATIONS; j++) {
                    // Atomic increment using compute()
                    if (map instanceof ConcurrentHashMap) {
                        ((ConcurrentHashMap<String, Integer>) map).compute("counter",
                                (k, v) -> v == null ? 1 : v + 1);
                    } else {
                        // For synchronizedMap, we need external sync for compound ops
                        synchronized (map) {
                            Integer v = map.get("counter");
                            map.put("counter", v == null ? 1 : v + 1);
                        }
                    }
                }
            });
        }

        executor.shutdown();
        try {
            executor.awaitTermination(30, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        int expected = THREADS * ITERATIONS;
        Integer actual = map.get("counter");
        String status = (actual != null && actual == expected) ? "✅" : "❌";

        System.out.printf("    %s: count = %d (expected %d) %s%n",
                name, actual, expected, status);
    }
}
