package org.example.p3_Java_Advance_Concept.c5_list_interface.contracts;

/**
 * Contract for List demonstration classes.
 * 
 * <p>
 * All list demo implementations must provide:
 * <ul>
 * <li>A descriptive name for the demo</li>
 * <li>An execute method that runs the demonstration</li>
 * </ul>
 * </p>
 * 
 * @author LLD Notes
 */
public interface IListDemo {

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
