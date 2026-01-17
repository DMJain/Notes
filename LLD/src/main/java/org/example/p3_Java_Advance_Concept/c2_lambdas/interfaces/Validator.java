package org.example.p3_Java_Advance_Concept.c2_lambdas.interfaces;

/**
 * Custom functional interface for validation with default method chaining.
 * Demonstrates how functional interfaces can have default methods.
 * 
 * @param <T> the type of value to validate
 */
@FunctionalInterface
public interface Validator<T> {

    /**
     * Validates the given value.
     * 
     * @param value the value to validate
     * @return true if valid, false otherwise
     */
    boolean validate(T value);

    /**
     * Chains this validator with another using AND logic.
     * Both validators must pass for the result to be true.
     * 
     * @param other another validator to chain
     * @return a new validator that combines both
     */
    default Validator<T> and(Validator<T> other) {
        return value -> this.validate(value) && other.validate(value);
    }

    /**
     * Chains this validator with another using OR logic.
     * At least one validator must pass for the result to be true.
     * 
     * @param other another validator to chain
     * @return a new validator that combines both
     */
    default Validator<T> or(Validator<T> other) {
        return value -> this.validate(value) || other.validate(value);
    }

    /**
     * Negates this validator.
     * 
     * @return a new validator that returns the opposite result
     */
    default Validator<T> negate() {
        return value -> !this.validate(value);
    }
}
