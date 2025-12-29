package org.example.p1_oops.c6_abstract;

import org.example.p1_oops.c6_abstract.base.Shape;
import org.example.p1_oops.c6_abstract.impl.Circle;
import org.example.p1_oops.c6_abstract.impl.Rectangle;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Abstract Classes Demonstration ===\n");

        // --- 1. Creating Concrete Implementations ---
        System.out.println("--- 1. Creating Shapes ---");
        Circle circle = new Circle(5.0);
        Rectangle rectangle = new Rectangle(4.0, 6.0);

        circle.display(); // Uses concrete display() from Shape
        rectangle.display(); // Uses concrete display() from Shape

        // --- 2. Abstract Class as a Type (Polymorphism) ---
        System.out.println("\n--- 2. Abstract Class as Type ---");
        Shape s1 = new Circle(3.0);
        Shape s2 = new Rectangle(2.0, 5.0);

        s1.display();
        s2.display();

        // --- 3. Negative Cases ---
        System.out.println("\n--- 3. Negative Cases (See comments in code) ---");

        // CASE A: Cannot instantiate abstract class
        // Shape shape = new Shape("Test");
        System.out.println("// Shape s = new Shape(\"Test\");");
        System.out.println("// COMPILE ERROR: 'Shape' is abstract; cannot be instantiated.");
        System.out.println("// Reason: Abstract classes are incomplete. They require a concrete subclass.");
    }
}
