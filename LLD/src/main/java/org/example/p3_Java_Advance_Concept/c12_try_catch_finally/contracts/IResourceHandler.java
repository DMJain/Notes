package org.example.p3_Java_Advance_Concept.c12_try_catch_finally.contracts;

/**
 * Contract for demonstrating resource handling patterns.
 * <p>
 * Implementations show different approaches to managing resources
 * that need cleanup (files, connections, etc.).
 * </p>
 */
public interface IResourceHandler {

    /**
     * Opens/acquires the resource.
     *
     * @throws Exception if resource cannot be opened
     */
    void open() throws Exception;

    /**
     * Performs operations on the resource.
     *
     * @throws Exception if operation fails
     */
    void process() throws Exception;

    /**
     * Closes/releases the resource.
     * Should be called in finally block!
     */
    void close();

    /**
     * Checks if the resource is currently open.
     *
     * @return true if open, false otherwise
     */
    boolean isOpen();

    /**
     * Gets the resource name for display purposes.
     *
     * @return Human-readable resource name
     */
    String getResourceName();
}
