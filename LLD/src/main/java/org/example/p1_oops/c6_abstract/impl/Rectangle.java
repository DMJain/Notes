package org.example.p1_oops.c6_abstract.impl;

import org.example.p1_oops.c6_abstract.base.Shape;

public class Rectangle extends Shape {
    private double length;
    private double width;

    public Rectangle(double length, double width) {
        super("Rectangle");
        this.length = length;
        this.width = width;
    }

    @Override
    public double getArea() {
        return length * width;
    }
}
