package org.example.p1_oops.c6_abstract.base;

public abstract class Shape {
    protected String name;

    public Shape(String name) {
        this.name = name;
    }

    // Abstract method - MUST be implemented by child classes
    public abstract double getArea();

    // Concrete method - CAN be used directly by child classes
    public void display() {
        System.out.println("Shape: " + name + ", Area: " + getArea());
    }
}
