package org.example.p3_Java_Advance_Concept.c2_lambdas.interfaces;

/**
 * Custom functional interface for math operations.
 * Demonstrates how to create a functional interface.
 * 
 * A functional interface has exactly ONE abstract method,
 * making it suitable for lambda expressions.
 */
@FunctionalInterface
public interface MathOperation {

    /**
     * Performs a mathematical operation on two integers.
     * 
     * @param a first operand
     * @param b second operand
     * @return result of the operation
     */
    int operate(int a, int b);
}
