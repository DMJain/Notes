package org.example.polymorphism.overriding;

public class Cat extends Animal {
    @Override
    public void speak() {
        System.out.println("Cat meows: Meow!");
    }
}
