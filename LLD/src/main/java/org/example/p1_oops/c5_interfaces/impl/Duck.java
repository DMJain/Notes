package org.example.p1_oops.c5_interfaces.impl;

import org.example.p1_oops.c5_interfaces.contract.Flyable;
import org.example.p1_oops.c5_interfaces.contract.Swimmable;

// Duck implements BOTH interfaces - Multiple Inheritance via Interfaces
public class Duck implements Flyable, Swimmable {
    @Override
    public void fly() {
        System.out.println("Duck is flying low.");
    }

    @Override
    public void swim() {
        System.out.println("Duck is swimming in the pond.");
    }
}
