package org.example.p1_oops.c4_polymorphism.overriding;

public class Cat extends Animal {
    @Override
    public void speak() {
        System.out.println("Cat meows: Meow!");
    }
}
