package org.example.p3_Java_Advance_Concept.c13_throw_throws.contracts;

/**
 * Contract for validation operations that may throw on invalid input.
 */
public interface IValidator {

    /**
     * Validates the given input.
     *
     * @param input The input to validate
     * @throws IllegalArgumentException if validation fails
     */
    void validate(Object input) throws IllegalArgumentException;

    /**
     * Gets the validator name for display.
     *
     * @return Human-readable validator name
     */
    String getValidatorName();
}
