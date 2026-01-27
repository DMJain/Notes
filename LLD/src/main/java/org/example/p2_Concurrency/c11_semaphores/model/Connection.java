package org.example.p2_Concurrency.c11_semaphores.model;

/**
 * Represents a database connection in the connection pool.
 * 
 * <p>
 * Simulates a database connection with unique ID for
 * demonstration purposes.
 * </p>
 */
public class Connection {

    private final int connectionId;
    private final long createdAt;
    private boolean inUse;

    public Connection(int connectionId) {
        this.connectionId = connectionId;
        this.createdAt = System.currentTimeMillis();
        this.inUse = false;
    }

    /**
     * Simulates executing a query on this connection.
     * 
     * @param query   the SQL query to execute
     * @param delayMs simulated execution time in milliseconds
     */
    public void executeQuery(String query, long delayMs) {
        System.out.printf("    [Connection-%d] Executing: %s%n", connectionId, query);
        try {
            Thread.sleep(delayMs);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        System.out.printf("    [Connection-%d] Completed: %s%n", connectionId, query);
    }

    public int getConnectionId() {
        return connectionId;
    }

    public boolean isInUse() {
        return inUse;
    }

    public void setInUse(boolean inUse) {
        this.inUse = inUse;
    }

    @Override
    public String toString() {
        return String.format("Connection[id=%d, age=%dms, inUse=%s]",
                connectionId, System.currentTimeMillis() - createdAt, inUse);
    }
}
