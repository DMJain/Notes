package org.example.p1_oops.c4_polymorphism.overriding;

public class Dog extends Animal {
    @Override
    public void speak() {
        System.out.println("Dog barks: Woof!");
    }
}
