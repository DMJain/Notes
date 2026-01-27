package org.example.p2_Concurrency.c11_semaphores.runner.scenarios;

import org.example.p2_Concurrency.c11_semaphores.impl.ConnectionPoolController;
import org.example.p2_Concurrency.c11_semaphores.model.Connection;

/**
 * Demonstrates connection pool scenario using semaphores.
 * 
 * <p>
 * Shows how semaphores limit concurrent database connections.
 * 10 worker threads compete for 3 connections.
 * </p>
 */
public class ConnectionPoolScenario {

    private static final int POOL_SIZE = 3;
    private static final int WORKER_COUNT = 10;

    public void execute() {
        System.out.println("\n" + "═".repeat(60));
        System.out.println("           CONNECTION POOL SCENARIO");
        System.out.println("═".repeat(60));
        System.out.printf("Pool Size: %d | Workers: %d%n%n", POOL_SIZE, WORKER_COUNT);

        ConnectionPoolController pool = new ConnectionPoolController(POOL_SIZE);

        // Create worker threads that need database connections
        Thread[] workers = new Thread[WORKER_COUNT];

        for (int i = 0; i < WORKER_COUNT; i++) {
            final int workerId = i + 1;
            workers[i] = new Thread(() -> performDatabaseWork(pool, workerId),
                    "Worker-" + workerId);
        }

        // Start all workers
        System.out.println("\n🚀 Starting all workers...\n");
        for (Thread worker : workers) {
            worker.start();
        }

        // Wait for all workers to complete
        for (Thread worker : workers) {
            try {
                worker.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        System.out.println("\n✅ All workers completed!");
        pool.displayPoolStatus();
    }

    private void performDatabaseWork(ConnectionPoolController pool, int workerId) {
        try {
            // Get a connection from the pool (blocks if none available)
            Connection connection = pool.borrowResource();

            try {
                // Simulate database work
                connection.executeQuery("SELECT * FROM users WHERE id=" + workerId, 500);
            } finally {
                // Always return connection to pool
                pool.returnResource(connection);
            }

        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            System.out.printf("❌ Worker-%d was interrupted%n", workerId);
        }
    }
}
