package org.example.p3_Java_Advance_Concept.c1_generics.model;

import java.util.Objects;

/**
 * A simple user entity for demonstrating generics with domain objects.
 * <p>
 * This class implements Comparable for use with ComparableContainer
 * demonstrations.
 * </p>
 */
public class User implements Comparable<User> {

    private final Long id;
    private final String name;
    private final int age;

    /**
     * Creates a new user.
     *
     * @param id   unique user identifier
     * @param name user's name
     * @param age  user's age
     */
    public User(Long id, String name, int age) {
        this.id = id;
        this.name = name;
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    /**
     * Compares users by age (natural ordering).
     *
     * @param other the user to compare to
     * @return negative if younger, positive if older, zero if same age
     */
    @Override
    public int compareTo(User other) {
        return Integer.compare(this.age, other.age);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "User{id=" + id + ", name='" + name + "', age=" + age + "}";
    }
}
