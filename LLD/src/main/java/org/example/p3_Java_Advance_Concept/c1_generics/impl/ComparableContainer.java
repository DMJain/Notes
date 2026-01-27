package org.example.p3_Java_Advance_Concept.c1_generics.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * A container for comparable elements with sorting capabilities.
 * <p>
 * This class demonstrates <b>multiple bounds</b> in generics:
 * {@code <T extends Comparable<T>>} ensures that stored elements
 * can be compared to each other.
 * </p>
 *
 * <h3>Why This Bound?</h3>
 * <p>
 * Without {@code Comparable} bound, we couldn't sort or find min/max.
 * The bound guarantees {@code compareTo()} method exists.
 * </p>
 *
 * <h3>Example Usage:</h3>
 * 
 * <pre>{@code
 * ComparableContainer<Integer> container = new ComparableContainer<>();
 * container.add(5);
 * container.add(2);
 * container.add(8);
 *
 * Integer min = container.getMin(); // 2
 * Integer max = container.getMax(); // 8
 * container.sort(); // [2, 5, 8]
 * }</pre>
 *
 * @param <T> the type of elements, must implement Comparable
 */
public class ComparableContainer<T extends Comparable<T>> {

    private final List<T> items;

    /**
     * Creates an empty container.
     */
    public ComparableContainer() {
        this.items = new ArrayList<>();
    }

    /**
     * Adds an item to the container.
     *
     * @param item the item to add
     */
    public void add(T item) {
        if (item != null) {
            items.add(item);
        }
    }

    /**
     * Returns all items in the container.
     *
     * @return unmodifiable list of items
     */
    public List<T> getItems() {
        return Collections.unmodifiableList(items);
    }

    /**
     * Returns the minimum element.
     * <p>
     * This is only possible because T implements Comparable,
     * allowing us to compare elements.
     * </p>
     *
     * @return the minimum element, or null if empty
     */
    public T getMin() {
        if (items.isEmpty()) {
            return null;
        }

        T min = items.get(0);
        for (T item : items) {
            if (item.compareTo(min) < 0) {
                min = item;
            }
        }
        return min;
    }

    /**
     * Returns the maximum element.
     *
     * @return the maximum element, or null if empty
     */
    public T getMax() {
        if (items.isEmpty()) {
            return null;
        }

        T max = items.get(0);
        for (T item : items) {
            if (item.compareTo(max) > 0) {
                max = item;
            }
        }
        return max;
    }

    /**
     * Sorts the items in natural order.
     * <p>
     * Uses the Comparable interface's compareTo method for ordering.
     * </p>
     */
    public void sort() {
        Collections.sort(items);
    }

    /**
     * Checks if an item exists in the container.
     *
     * @param item the item to search for
     * @return true if found
     */
    public boolean contains(T item) {
        return items.contains(item);
    }

    /**
     * Returns the number of items.
     *
     * @return the count of items
     */
    public int size() {
        return items.size();
    }

    /**
     * Checks if container is empty.
     *
     * @return true if no items
     */
    public boolean isEmpty() {
        return items.isEmpty();
    }

    @Override
    public String toString() {
        return "ComparableContainer" + items;
    }
}
