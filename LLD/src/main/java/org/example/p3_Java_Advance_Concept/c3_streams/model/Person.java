package org.example.p3_Java_Advance_Concept.c3_streams.model;

/**
 * Simple Person model for demonstrating stream operations.
 */
public class Person {
    private String name;
    private int age;
    private String department;
    private double salary;

    public Person(String name, int age, String department, double salary) {
        this.name = name;
        this.age = age;
        this.department = department;
        this.salary = salary;
    }

    // Getters
    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public String getDepartment() {
        return department;
    }

    public double getSalary() {
        return salary;
    }

    @Override
    public String toString() {
        return String.format("%s(%d, %s, $%.0f)", name, age, department, salary);
    }
}
