package org.example.p1_oops.c5_interfaces;

import org.example.p1_oops.c5_interfaces.contract.Flyable;
import org.example.p1_oops.c5_interfaces.contract.Swimmable;
import org.example.p1_oops.c5_interfaces.impl.Bird;
import org.example.p1_oops.c5_interfaces.impl.Fish;
import org.example.p1_oops.c5_interfaces.impl.Duck;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Interfaces Demonstration ===\n");

        // --- 1. Basic Interface Implementation ---
        System.out.println("--- 1. Basic Interface Implementation ---");
        Bird bird = new Bird();
        bird.fly();

        Fish fish = new Fish();
        fish.swim();

        // --- 2. Multiple Interface Implementation ---
        System.out.println("\n--- 2. Multiple Interface Implementation ---");
        Duck duck = new Duck();
        duck.fly();
        duck.swim();
        System.out.println("// Duck implements BOTH Flyable and Swimmable.");

        // --- 3. Interface as a Type (Polymorphism) ---
        System.out.println("\n--- 3. Interface as a Type ---");
        Flyable f = new Bird(); // Bird is treated as Flyable
        Swimmable s = new Fish(); // Fish is treated as Swimmable

        f.fly();
        s.swim();

        // Duck can be assigned to EITHER interface type
        Flyable flyingDuck = new Duck();
        Swimmable swimmingDuck = new Duck();
        flyingDuck.fly();
        swimmingDuck.swim();

        // --- 4. Negative Cases ---
        System.out.println("\n--- 4. Negative Cases (See comments in code) ---");

        // CASE A: Cannot instantiate an interface
        // Flyable f2 = new Flyable();
        System.out.println("// Flyable f = new Flyable();");
        System.out.println("// COMPILE ERROR: 'Flyable' is abstract; cannot be instantiated.");
        System.out.println("// Reason: Interfaces have no implementation. You need a class that implements it.");
    }
}
