package org.example.p1_oops.c5_interfaces.impl;

import org.example.p1_oops.c5_interfaces.contract.Flyable;

public class Bird implements Flyable {
    @Override
    public void fly() {
        System.out.println("Bird is flying in the sky.");
    }
}
