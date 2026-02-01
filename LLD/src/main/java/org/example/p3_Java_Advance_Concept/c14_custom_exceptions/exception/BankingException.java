package org.example.p3_Java_Advance_Concept.c14_custom_exceptions.exception;

/**
 * Base exception for all banking domain errors.
 * <p>
 * Demonstrates creating an exception hierarchy.
 * All banking-related exceptions should extend this.
 * </p>
 */
public class BankingException extends Exception {

    public BankingException(String message) {
        super(message);
    }

    public BankingException(String message, Throwable cause) {
        super(message, cause);
    }
}
