package org.example.p3_Java_Advance_Concept.c15_exception_chaining.impl;

import java.sql.SQLException;

/**
 * Demonstrates exception chaining.
 */
public class ExceptionChainingDemo {

    public static void runDemo() {
        System.out.println();
        System.out.println("╔════════════════════════════════════════════════════════════════╗");
        System.out.println("║             EXCEPTION CHAINING DEMONSTRATION                   ║");
        System.out.println("╚════════════════════════════════════════════════════════════════╝");
        System.out.println();

        demonstrateProblem();
        demonstrateSolution();
        demonstrateWalkingChain();
    }

    private static void demonstrateProblem() {
        System.out.println("📕 PROBLEM: Lost Context Without Chaining");
        System.out.println("─".repeat(60));
        System.out.println();
        System.out.println("  When you catch and throw a new exception without chaining:");
        System.out.println();
        System.out.println("  ┌────────────────────────────────────────────────────┐");
        System.out.println("  │ catch (SQLException e) {                           │");
        System.out.println("  │     throw new DataException(\"Failed\");  // ❌ BAD  │");
        System.out.println("  │ }                                                  │");
        System.out.println("  └────────────────────────────────────────────────────┘");
        System.out.println();
        System.out.println("  The original SQLException is LOST!");
        System.out.println("  We don't know if it was timeout, constraint, network...");
        System.out.println();
    }

    private static void demonstrateSolution() {
        System.out.println("📗 SOLUTION: Chain with Cause");
        System.out.println("─".repeat(60));
        System.out.println();
        System.out.println("  Pass the original exception as the 'cause':");
        System.out.println();
        System.out.println("  ┌────────────────────────────────────────────────────┐");
        System.out.println("  │ catch (SQLException e) {                           │");
        System.out.println("  │     throw new DataException(\"Failed\", e); // ✅   │");
        System.out.println("  │ }                           ↑ CHAIN IT!            │");
        System.out.println("  └────────────────────────────────────────────────────┘");
        System.out.println();

        // Live demo
        System.out.println("  LIVE DEMO:");
        try {
            loadUserProfile("user-123");
        } catch (ServiceException e) {
            System.out.println();
            System.out.println("  Caught: " + e.getClass().getSimpleName());
            System.out.println("  Message: " + e.getMessage());
            System.out.println();
            System.out.println("  Full stack trace shows the chain:");
            System.out.println("  ─".repeat(30));
            e.printStackTrace(System.out);
        }
        System.out.println();
    }

    private static void demonstrateWalkingChain() {
        System.out.println("📘 WALKING THE CHAIN: getCause()");
        System.out.println("─".repeat(60));
        System.out.println();

        try {
            loadUserProfile("user-456");
        } catch (ServiceException e) {
            System.out.println("  Walking the exception chain:");
            System.out.println();

            int level = 0;
            Throwable current = e;
            while (current != null) {
                System.out.printf("  Level %d: %s%n", level, current.getClass().getSimpleName());
                System.out.printf("           Message: %s%n", current.getMessage());
                current = current.getCause();
                level++;
            }
            System.out.println();
            System.out.println("  Root cause found at level " + (level - 1));
        }
        System.out.println();
    }

    // Simulated layer chain
    private static void loadUserProfile(String userId) throws ServiceException {
        try {
            fetchFromDatabase(userId);
        } catch (RepositoryException e) {
            // Chain: Repository → Service level
            throw new ServiceException("Failed to load profile for " + userId, e);
        }
    }

    private static void fetchFromDatabase(String id) throws RepositoryException {
        try {
            executeQuery("SELECT * FROM users WHERE id = " + id);
        } catch (SQLException e) {
            // Chain: SQL → Repository level
            throw new RepositoryException("Database query failed for id: " + id, e);
        }
    }

    private static void executeQuery(String sql) throws SQLException {
        // Simulate database error
        throw new SQLException("Connection timeout: host not reachable");
    }

    // Custom exceptions for demo
    private static class ServiceException extends Exception {
        ServiceException(String message, Throwable cause) {
            super(message, cause);
        }
    }

    private static class RepositoryException extends Exception {
        RepositoryException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
