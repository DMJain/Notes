package org.example.p3_Java_Advance_Concept.c1_generics.util;

import java.util.List;

/**
 * Utility class demonstrating wildcard usage in generics.
 * <p>
 * This class shows when and how to use:
 * <ul>
 * <li>{@code <?>} - Unbounded wildcard</li>
 * <li>{@code <? extends T>} - Upper bounded wildcard (PRODUCER)</li>
 * <li>{@code <? super T>} - Lower bounded wildcard (CONSUMER)</li>
 * </ul>
 * </p>
 *
 * <h3>PECS Principle:</h3>
 * <ul>
 * <li><b>P</b>roducer <b>E</b>xtends - use extends when you READ from a
 * structure</li>
 * <li><b>C</b>onsumer <b>S</b>uper - use super when you WRITE to a
 * structure</li>
 * </ul>
 */
public class WildcardUtils {

    // Private constructor
    private WildcardUtils() {
    }

    // ==================== UNBOUNDED WILDCARD <?> ====================

    /**
     * Prints any list (unbounded wildcard).
     * <p>
     * Use {@code <?>} when you only need to read as Object
     * or don't care about the type at all.
     * </p>
     *
     * @param list any list
     */
    public static void printList(List<?> list) {
        System.out.print("List: [");
        for (int i = 0; i < list.size(); i++) {
            System.out.print(list.get(i)); // Returns Object
            if (i < list.size() - 1)
                System.out.print(", ");
        }
        System.out.println("]");
    }

    /**
     * Returns the size of any list.
     * <p>
     * Perfect use case for unbounded wildcard - we don't need the element type.
     * </p>
     *
     * @param list any list
     * @return the size
     */
    public static int getSize(List<?> list) {
        return list == null ? 0 : list.size();
    }

    // ==================== UPPER BOUNDED <? extends T> ====================

    /**
     * Sums all numbers in a list (upper bounded wildcard).
     * <p>
     * {@code <? extends Number>} means this list PRODUCES Number values.
     * We can READ from it safely, but cannot WRITE (except null).
     * </p>
     *
     * @param numbers list of any Number subtype (Integer, Double, etc.)
     * @return the sum as double
     */
    public static double sumNumbers(List<? extends Number> numbers) {
        double sum = 0;
        for (Number n : numbers) {
            sum += n.doubleValue(); // Can read as Number
        }
        // numbers.add(1); // ❌ COMPILE ERROR! Cannot write
        return sum;
    }

    /**
     * Finds the maximum in a list of comparable elements.
     *
     * @param list list of comparable elements
     * @param <T>  the element type
     * @return the maximum element, or null if empty
     */
    public static <T extends Comparable<T>> T findMax(List<? extends T> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }

        T max = list.get(0);
        for (T item : list) {
            if (item.compareTo(max) > 0) {
                max = item;
            }
        }
        return max;
    }

    // ==================== LOWER BOUNDED <? super T> ====================

    /**
     * Adds numbers to a list (lower bounded wildcard).
     * <p>
     * {@code <? super Integer>} means this list CONSUMES Integer values.
     * We can WRITE Integer to it, but can only READ as Object.
     * </p>
     *
     * @param dest list that accepts Integer (or its supertypes)
     */
    public static void addNumbers(List<? super Integer> dest) {
        dest.add(1);
        dest.add(2);
        dest.add(3);
        // Integer num = dest.get(0); // ❌ Cannot read as Integer, only Object
    }

    /**
     * Copies elements from source to destination.
     * <p>
     * This method demonstrates the PECS principle:
     * <ul>
     * <li>src is a PRODUCER (we read from it) → use extends</li>
     * <li>dest is a CONSUMER (we write to it) → use super</li>
     * </ul>
     * </p>
     *
     * @param dest destination list (consumer)
     * @param src  source list (producer)
     * @param <T>  the element type
     */
    public static <T> void copy(List<? super T> dest, List<? extends T> src) {
        for (T item : src) {
            dest.add(item);
        }
    }

    /**
     * Fills a list with a single value.
     * <p>
     * The destination must be able to accept T values (? super T).
     * </p>
     *
     * @param list  the list to fill
     * @param value the value to add
     * @param count how many times to add
     * @param <T>   the element type
     */
    public static <T> void fill(List<? super T> list, T value, int count) {
        for (int i = 0; i < count; i++) {
            list.add(value);
        }
    }
}
