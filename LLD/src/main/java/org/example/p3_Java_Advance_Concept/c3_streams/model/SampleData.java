package org.example.p3_Java_Advance_Concept.c3_streams.model;

import java.util.Arrays;
import java.util.List;

/**
 * Provides sample data for stream demonstrations.
 */
public class SampleData {

    /**
     * Returns a list of sample people for demonstrations.
     */
    public static List<Person> getPeople() {
        return Arrays.asList(
                new Person("Alice", 30, "Engineering", 95000),
                new Person("Bob", 25, "Marketing", 65000),
                new Person("Charlie", 35, "Engineering", 105000),
                new Person("Diana", 28, "Marketing", 70000),
                new Person("Eve", 32, "Engineering", 100000),
                new Person("Frank", 29, "HR", 60000),
                new Person("Grace", 27, "HR", 55000));
    }

    /**
     * Returns a sample list of numbers.
     */
    public static List<Integer> getNumbers() {
        return Arrays.asList(5, 2, 8, 1, 9, 3, 7, 4, 6);
    }

    /**
     * Returns a sample list of names.
     */
    public static List<String> getNames() {
        return Arrays.asList("Alice", "Bob", "Anna", "Charlie", "Alex");
    }
}
