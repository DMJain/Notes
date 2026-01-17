package org.example.p3_Java_Advance_Concept.c2_lambdas.interfaces;

/**
 * Custom functional interface for processing strings.
 * Demonstrates transformation-style functional interfaces.
 */
@FunctionalInterface
public interface StringProcessor {

    /**
     * Processes a string and returns the result.
     * 
     * @param input the input string
     * @return processed string
     */
    String process(String input);
}
