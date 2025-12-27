package org.example.polymorphism.overriding;

public class Dog extends Animal {
    @Override
    public void speak() {
        System.out.println("Dog barks: Woof!");
    }
}
