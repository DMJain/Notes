package org.example.p3_Java_Advance_Concept.c1_generics.impl;

import org.example.p3_Java_Advance_Concept.c1_generics.contracts.IContainer;

/**
 * A simple generic container that holds a single item.
 * <p>
 * This class demonstrates the basic generic class pattern where
 * the type parameter T is used for the stored content.
 * </p>
 *
 * <h3>Example Usage:</h3>
 * 
 * <pre>{@code
 * Box<String> stringBox = new Box<>();
 * stringBox.add("Hello");
 * String value = stringBox.get(); // No casting needed!
 *
 * Box<Integer> intBox = new Box<>();
 * intBox.add(42);
 * int number = intBox.get(); // Autoboxing works!
 * }</pre>
 *
 * <h3>Thread Safety:</h3>
 * <p>
 * This class is NOT thread-safe. For concurrent access,
 * external synchronization is required.
 * </p>
 *
 * @param <T> the type of element stored in this box
 */
public class Box<T> implements IContainer<T> {

    private T content;

    /**
     * Creates an empty box.
     */
    public Box() {
        this.content = null;
    }

    /**
     * Creates a box with initial content.
     *
     * @param content the initial content
     */
    public Box(T content) {
        this.content = content;
    }

    @Override
    public void add(T item) {
        this.content = item;
    }

    @Override
    public T get() {
        return content;
    }

    @Override
    public boolean isEmpty() {
        return content == null;
    }

    /**
     * Returns a string representation of this box.
     *
     * @return string in format "Box[content]" or "Box[empty]"
     */
    @Override
    public String toString() {
        return isEmpty() ? "Box[empty]" : "Box[" + content + "]";
    }
}
