package org.example.p2_Concurrency.c11_semaphores.impl;

import org.example.p2_Concurrency.c11_semaphores.contracts.IResourcePool;
import org.example.p2_Concurrency.c11_semaphores.model.Connection;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Thread-safe connection pool implementation using Semaphores.
 * 
 * <p>
 * Demonstrates how semaphores control access to a bounded
 * pool of database connections. Up to {@code maxConnections}
 * threads can hold connections simultaneously.
 * </p>
 * 
 * <h3>How It Works:</h3>
 * 
 * <pre>
 *   Semaphore(maxConnections)
 *        │
 *        ▼
 *   acquire() ──► Decrement permits ──► Get connection from pool
 *        │
 *   release() ◄── Increment permits ◄── Return connection to pool
 * </pre>
 * 
 * @see java.util.concurrent.Semaphore
 */
public class ConnectionPoolController implements IResourcePool<Connection> {

    private final int maxConnections;
    private final Semaphore semaphore;
    private final ConcurrentLinkedQueue<Connection> connectionPool;
    private final AtomicInteger connectionIdGenerator;

    /**
     * Creates a connection pool with specified capacity.
     * 
     * @param maxConnections maximum simultaneous connections allowed
     */
    public ConnectionPoolController(int maxConnections) {
        this.maxConnections = maxConnections;
        // Fair semaphore ensures FIFO ordering for waiting threads
        this.semaphore = new Semaphore(maxConnections, true);
        this.connectionPool = new ConcurrentLinkedQueue<>();
        this.connectionIdGenerator = new AtomicInteger(1);

        // Pre-populate the pool with connections
        for (int i = 0; i < maxConnections; i++) {
            connectionPool.add(createConnection());
        }

        System.out.printf("✅ ConnectionPool created with %d connections%n", maxConnections);
    }

    /**
     * Borrows a connection from the pool.
     * 
     * <p>
     * Blocks if all connections are in use. The semaphore ensures
     * that at most {@code maxConnections} threads can hold connections.
     * </p>
     * 
     * @return a connection from the pool
     * @throws InterruptedException if interrupted while waiting
     */
    @Override
    public Connection borrowResource() throws InterruptedException {
        String threadName = Thread.currentThread().getName();
        System.out.printf("⏳ [%s] Waiting for connection... (available: %d)%n",
                threadName, semaphore.availablePermits());

        // Block until a permit is available
        semaphore.acquire();

        // Get connection from pool
        Connection connection = connectionPool.poll();
        if (connection != null) {
            connection.setInUse(true);
            System.out.printf("✅ [%s] Acquired Connection-%d (available: %d)%n",
                    threadName, connection.getConnectionId(), semaphore.availablePermits());
        }

        return connection;
    }

    /**
     * Returns a connection to the pool.
     * 
     * @param connection the connection to return
     */
    @Override
    public void returnResource(Connection connection) {
        String threadName = Thread.currentThread().getName();

        if (connection != null) {
            connection.setInUse(false);
            connectionPool.offer(connection);
            System.out.printf("🔄 [%s] Released Connection-%d%n",
                    threadName, connection.getConnectionId());
        }

        // Release permit to wake up waiting threads
        semaphore.release();
        System.out.printf("   [%s] Permits now available: %d%n",
                threadName, semaphore.availablePermits());
    }

    @Override
    public int getAvailableCount() {
        return semaphore.availablePermits();
    }

    @Override
    public int getTotalCapacity() {
        return maxConnections;
    }

    /**
     * Creates a new simulated connection.
     */
    private Connection createConnection() {
        return new Connection(connectionIdGenerator.getAndIncrement());
    }

    /**
     * Displays the current state of the pool.
     */
    public void displayPoolStatus() {
        System.out.printf("%n📊 Pool Status: %d/%d connections available%n",
                semaphore.availablePermits(), maxConnections);
    }
}
