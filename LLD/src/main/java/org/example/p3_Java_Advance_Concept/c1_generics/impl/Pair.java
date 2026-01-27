package org.example.p3_Java_Advance_Concept.c1_generics.impl;

import org.example.p3_Java_Advance_Concept.c1_generics.contracts.IPair;

import java.util.Objects;

/**
 * An immutable key-value pair implementation.
 * <p>
 * This class demonstrates multiple type parameters (K and V) and
 * follows best practices for value objects with immutability.
 * </p>
 *
 * <h3>Example Usage:</h3>
 * 
 * <pre>{@code
 * Pair<String, Integer> age = new Pair<>("Alice", 25);
 * String key = age.getKey(); // "Alice"
 * Integer value = age.getValue(); // 25
 *
 * Pair<Integer, List<String>> config = new Pair<>(1, Arrays.asList("a", "b"));
 * }</pre>
 *
 * @param <K> the type of the key
 * @param <V> the type of the value
 */
public class Pair<K, V> implements IPair<K, V> {

    private final K key;
    private final V value;

    /**
     * Creates a new pair with the given key and value.
     *
     * @param key   the key (may be null)
     * @param value the value (may be null)
     */
    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    /**
     * Factory method for creating a pair.
     * <p>
     * This demonstrates a generic static factory method pattern.
     * </p>
     *
     * @param key   the key
     * @param value the value
     * @param <K>   type of key
     * @param <V>   type of value
     * @return a new Pair instance
     */
    public static <K, V> Pair<K, V> of(K key, V value) {
        return new Pair<>(key, value);
    }

    @Override
    public K getKey() {
        return key;
    }

    @Override
    public V getValue() {
        return value;
    }

    /**
     * Returns a new pair with the same key but different value.
     *
     * @param newValue the new value
     * @return a new Pair with updated value
     */
    public Pair<K, V> withValue(V newValue) {
        return new Pair<>(this.key, newValue);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        Pair<?, ?> pair = (Pair<?, ?>) o;
        return Objects.equals(key, pair.key) && Objects.equals(value, pair.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(key, value);
    }

    @Override
    public String toString() {
        return "Pair{" + key + " -> " + value + "}";
    }
}
