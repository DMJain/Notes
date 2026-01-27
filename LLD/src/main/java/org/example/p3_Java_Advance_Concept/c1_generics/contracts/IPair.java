package org.example.p3_Java_Advance_Concept.c1_generics.contracts;

/**
 * Generic pair interface for storing key-value associations.
 * <p>
 * This interface demonstrates multiple type parameters (K for Key, V for Value)
 * following Java naming conventions for generic types.
 * </p>
 *
 * <h3>Example Usage:</h3>
 * 
 * <pre>{@code
 * IPair<String, Integer> entry = new OrderedPair<>("age", 25);
 * String key = entry.getKey(); // "age"
 * Integer value = entry.getValue(); // 25
 * }</pre>
 *
 * @param <K> the type of key in this pair
 * @param <V> the type of value in this pair
 *
 * @see org.example.p3_Java_Advance_Concept.c1_generics.impl.Pair
 */
public interface IPair<K, V> {

    /**
     * Returns the key of this pair.
     *
     * @return the key
     */
    K getKey();

    /**
     * Returns the value of this pair.
     *
     * @return the value
     */
    V getValue();
}
