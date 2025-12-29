package org.example.p1_oops.c4_polymorphism.overloading;

public class Calculator {

    // Method Overloading: Same name, different parameters

    public int add(int a, int b) {
        System.out.println("add(int, int) called");
        return a + b;
    }

    public int add(int a, int b, int c) {
        System.out.println("add(int, int, int) called");
        return a + b + c;
    }

    public double add(double a, double b) {
        System.out.println("add(double, double) called");
        return a + b;
    }

    // Negative Case: Return type alone cannot differentiate
    // public double add(int a, int b) { return a + b; }
    // COMPILE ERROR: method add(int,int) is already defined
    // Reason: Overloading is based on parameter list, NOT return type.
}
