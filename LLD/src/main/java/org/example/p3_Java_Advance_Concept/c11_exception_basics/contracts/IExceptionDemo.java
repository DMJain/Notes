package org.example.p3_Java_Advance_Concept.c11_exception_basics.contracts;

/**
 * Contract defining how to demonstrate exception concepts.
 * <p>
 * Each implementation demonstrates a specific type of exception
 * (checked, unchecked) with clear examples.
 * </p>
 */
public interface IExceptionDemo {

    /**
     * Executes the demonstration.
     * <p>
     * Shows the exception scenario and how it manifests.
     * </p>
     */
    void demonstrate();

    /**
     * Gets the name of this demonstration.
     *
     * @return A human-readable name for the demo
     */
    String getDemoName();

    /**
     * Gets a description of what this demo teaches.
     *
     * @return Description of the learning objective
     */
    String getDescription();
}
