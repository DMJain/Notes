package org.example.p3_Java_Advance_Concept.c4_collection_framework.contracts;

/**
 * Contract for Collection Framework demonstration classes.
 * 
 * <p>
 * All collection demo implementations must provide:
 * <ul>
 * <li>A descriptive name for the demo</li>
 * <li>An execute method that runs the demonstration</li>
 * </ul>
 * </p>
 * 
 * @author LLD Notes
 * @since Java Collections Module
 */
public interface ICollectionDemo {

    /**
     * Returns the name of this demonstration.
     * 
     * @return A descriptive name for the demo
     */
    String getDemoName();

    /**
     * Executes the demonstration, printing educational output to console.
     */
    void execute();
}
