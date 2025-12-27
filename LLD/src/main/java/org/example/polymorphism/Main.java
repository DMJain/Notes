package org.example.polymorphism;

import org.example.polymorphism.overloading.Calculator;
import org.example.polymorphism.overriding.Animal;
import org.example.polymorphism.overriding.Dog;
import org.example.polymorphism.overriding.Cat;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Polymorphism Demonstration ===\n");

        // --- 1. Method Overloading (Compile-Time Polymorphism) ---
        System.out.println("--- 1. Method Overloading ---");
        Calculator calc = new Calculator();
        System.out.println("Result: " + calc.add(5, 10)); // add(int, int)
        System.out.println("Result: " + calc.add(5, 10, 15)); // add(int, int, int)
        System.out.println("Result: " + calc.add(5.5, 10.5)); // add(double, double)

        // Negative Case (Overloading)
        System.out.println("\n// Negative: Return type alone cannot differentiate methods.");
        System.out.println("// public double add(int a, int b) { ... }");
        System.out.println("// COMPILE ERROR: method add(int,int) is already defined.");

        // --- 2. Method Overriding (Runtime Polymorphism) ---
        System.out.println("\n--- 2. Method Overriding ---");
        Animal animal = new Animal();
        Dog dog = new Dog();
        Cat cat = new Cat();

        animal.speak(); // Animal's version
        dog.speak(); // Dog's version
        cat.speak(); // Cat's version

        // --- 3. Dynamic Method Dispatch ---
        System.out.println("\n--- 3. Dynamic Method Dispatch ---");
        Animal a1 = new Dog(); // Upcasting
        Animal a2 = new Cat();

        // At compile time, a1 is Animal. At runtime, it's Dog.
        // Java calls the ACTUAL object's method (Runtime decision).
        a1.speak(); // Dog barks
        a2.speak(); // Cat meows

        System.out.println("\n// Reason: The reference type (Animal) determines WHAT methods you can call.");
        System.out.println("// The actual object type determines WHICH implementation runs.");
    }
}
