package org.example.p3_Java_Advance_Concept.c11_exception_basics.contracts;

/**
 * Contract defining how to handle exceptions in different scenarios.
 * <p>
 * Implementations show various strategies for dealing with exceptions:
 * logging, rethrowing, wrapping, etc.
 * </p>
 */
public interface IExceptionHandler {

    /**
     * Handles an exception according to the specific strategy.
     *
     * @param exception The exception to handle
     * @return true if handled successfully, false otherwise
     */
    boolean handle(Exception exception);

    /**
     * Gets the strategy name for this handler.
     *
     * @return Name describing the handling strategy
     */
    String getStrategyName();
}
