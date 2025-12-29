package org.example.p1_oops.c5_interfaces.impl;

import org.example.p1_oops.c5_interfaces.contract.Swimmable;

public class Fish implements Swimmable {
    @Override
    public void swim() {
        System.out.println("Fish is swimming in the water.");
    }
}
