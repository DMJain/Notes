package org.example.p3_Java_Advance_Concept.c14_custom_exceptions.exception;

/**
 * Thrown when a withdrawal or transfer exceeds available balance.
 * <p>
 * This is a CHECKED exception because:
 * <ul>
 * <li>Caller cannot prevent it (balance changes externally)</li>
 * <li>Caller CAN recover (show balance, suggest deposit)</li>
 * <li>Caller SHOULD be forced to handle this case</li>
 * </ul>
 * </p>
 */
public class InsufficientFundsException extends BankingException {

    private final double currentBalance;
    private final double requestedAmount;

    public InsufficientFundsException(double currentBalance, double requestedAmount) {
        super(String.format(
                "Insufficient funds: current balance=%.2f, requested=%.2f, shortfall=%.2f",
                currentBalance, requestedAmount, requestedAmount - currentBalance));
        this.currentBalance = currentBalance;
        this.requestedAmount = requestedAmount;
    }

    /**
     * Gets the current account balance.
     * Useful for displaying to user.
     */
    public double getCurrentBalance() {
        return currentBalance;
    }

    /**
     * Gets the amount that was requested.
     */
    public double getRequestedAmount() {
        return requestedAmount;
    }

    /**
     * Gets the shortfall (how much more is needed).
     */
    public double getShortfall() {
        return requestedAmount - currentBalance;
    }
}
