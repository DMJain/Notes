package org.example.p3_Java_Advance_Concept.c1_generics.contracts;

/**
 * Generic container interface for storing a single element.
 * <p>
 * This interface demonstrates the basic contract for any container
 * that holds one item of a parameterized type.
 * </p>
 *
 * <h3>Example Usage:</h3>
 * 
 * <pre>{@code
 * IContainer<String> box = new Box<>();
 * box.add("Hello");
 * String value = box.get();
 * }</pre>
 *
 * @param <T> the type of element stored in this container
 *
 * @see org.example.p3_Java_Advance_Concept.c1_generics.impl.Box
 */
public interface IContainer<T> {

    /**
     * Adds an item to this container, replacing any existing content.
     *
     * @param item the item to add (may be null)
     */
    void add(T item);

    /**
     * Retrieves the current item from this container.
     *
     * @return the stored item, or null if empty
     */
    T get();

    /**
     * Checks whether this container is empty.
     *
     * @return {@code true} if no item is stored, {@code false} otherwise
     */
    boolean isEmpty();
}
