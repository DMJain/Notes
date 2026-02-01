package org.example.p3_Java_Advance_Concept.c14_custom_exceptions.exception;

/**
 * Thrown when an invalid amount is provided.
 * <p>
 * This is an UNCHECKED exception because:
 * <ul>
 * <li>Caller CAN prevent it (validate before calling)</li>
 * <li>It represents a programming error (invalid input)</li>
 * </ul>
 * </p>
 */
public class InvalidAmountException extends RuntimeException {

    private final double amount;

    public InvalidAmountException(double amount) {
        super("Invalid amount: " + amount + ". Amount must be positive.");
        this.amount = amount;
    }

    public InvalidAmountException(double amount, String customMessage) {
        super(customMessage);
        this.amount = amount;
    }

    public double getAmount() {
        return amount;
    }
}
