package org.example.constructors;

import java.util.Arrays;

public class Student {
    public String name;
    public int age;
    public int[] examScores; // Array to demonstrate Deep vs Shallow copy

    // 1. Default Constructor
    public Student() {
        this.name = "Unknown";
        this.age = 18;
        this.examScores = new int[] { 0, 0, 0 };
    }

    // 2. Parameterized Constructor
    public Student(String name, int age, int[] scores) {
        this.name = name;
        this.age = age;
        this.examScores = scores;
    }

    // 3. Copy Constructor (Deep Copy)
    public Student(Student other) {
        this.name = other.name;
        this.age = other.age;

        // Deep Copy: Create a NEW array and copy values
        // If we did this.examScores = other.examScores; it would be a Shallow Copy
        this.examScores = new int[other.examScores.length];
        for (int i = 0; i < other.examScores.length; i++) {
            this.examScores[i] = other.examScores[i];
        }
    }

    // Method to demonstrate Shallow Copy assignment
    public void shallowCopyFrom(Student other) {
        this.name = other.name;
        this.age = other.age;
        this.examScores = other.examScores; // Copying REFERENCE, not creating new array
    }

    @Override
    public String toString() {
        return "Student{name='" + name + "', age=" + age + ", scores=" + Arrays.toString(examScores) + "}";
    }
}
