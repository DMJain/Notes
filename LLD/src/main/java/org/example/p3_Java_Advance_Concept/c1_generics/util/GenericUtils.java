package org.example.p3_Java_Advance_Concept.c1_generics.util;

import java.util.List;

/**
 * Utility class with generic methods for array and collection operations.
 * <p>
 * This class demonstrates <b>generic methods</b> - methods that declare
 * their own type parameters independent of any class-level parameters.
 * </p>
 *
 * <h3>Key Concept:</h3>
 * <p>
 * Type parameter {@code <T>} is declared BEFORE the return type.
 * </p>
 *
 * <h3>Example:</h3>
 * 
 * <pre>{@code
 * Integer[] nums = { 1, 2, 3 };
 * GenericUtils.printArray(nums); // Type inferred as Integer
 *
 * String first = GenericUtils.getFirst(Arrays.asList("a", "b"));
 * }</pre>
 */
public class GenericUtils {

    // Private constructor to prevent instantiation
    private GenericUtils() {
    }

    /**
     * Prints all elements of an array.
     * <p>
     * This is a generic method - note {@code <T>} before void.
     * </p>
     *
     * @param array the array to print
     * @param <T>   the type of array elements
     */
    public static <T> void printArray(T[] array) {
        System.out.print("[");
        for (int i = 0; i < array.length; i++) {
            System.out.print(array[i]);
            if (i < array.length - 1) {
                System.out.print(", ");
            }
        }
        System.out.println("]");
    }

    /**
     * Returns the first element of a list.
     *
     * @param list the list
     * @param <T>  the type of list elements
     * @return the first element, or null if empty
     */
    public static <T> T getFirst(List<T> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }

    /**
     * Returns the last element of a list.
     *
     * @param list the list
     * @param <T>  the type of list elements
     * @return the last element, or null if empty
     */
    public static <T> T getLast(List<T> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(list.size() - 1);
    }

    /**
     * Swaps two elements in an array.
     *
     * @param array the array
     * @param i     first index
     * @param j     second index
     * @param <T>   the type of array elements
     */
    public static <T> void swap(T[] array, int i, int j) {
        if (array == null || i < 0 || j < 0 || i >= array.length || j >= array.length) {
            return;
        }
        T temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }

    /**
     * Finds the middle element of an array.
     *
     * @param array the array
     * @param <T>   the type of array elements
     * @return the middle element, or null if empty
     */
    public static <T> T getMiddle(T[] array) {
        if (array == null || array.length == 0) {
            return null;
        }
        return array[array.length / 2];
    }

    /**
     * Counts occurrences of an element in an array.
     *
     * @param array   the array to search
     * @param element the element to count
     * @param <T>     the type of elements
     * @return the count of occurrences
     */
    public static <T> int countOccurrences(T[] array, T element) {
        int count = 0;
        for (T item : array) {
            if (item != null && item.equals(element)) {
                count++;
            } else if (item == null && element == null) {
                count++;
            }
        }
        return count;
    }

    /**
     * Generic method with bounded type for finding maximum.
     * <p>
     * Demonstrates: {@code <T extends Comparable<T>>}
     * The type must implement Comparable to use compareTo().
     * </p>
     *
     * @param array the array
     * @param <T>   the type, must be Comparable
     * @return the maximum element, or null if empty
     */
    public static <T extends Comparable<T>> T findMax(T[] array) {
        if (array == null || array.length == 0) {
            return null;
        }

        T max = array[0];
        for (T item : array) {
            if (item.compareTo(max) > 0) {
                max = item;
            }
        }
        return max;
    }

    /**
     * Generic method with bounded type for finding minimum.
     *
     * @param array the array
     * @param <T>   the type, must be Comparable
     * @return the minimum element, or null if empty
     */
    public static <T extends Comparable<T>> T findMin(T[] array) {
        if (array == null || array.length == 0) {
            return null;
        }

        T min = array[0];
        for (T item : array) {
            if (item.compareTo(min) < 0) {
                min = item;
            }
        }
        return min;
    }
}
