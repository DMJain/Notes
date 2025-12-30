package org.example.p1_oops.c7_composition.composition;

public class Engine {
    private String type;

    public Engine(String type) {
        this.type = type;
        System.out.println("  Engine created: " + type);
    }

    public void start() {
        System.out.println("  Engine (" + type + ") started.");
    }

    public void stop() {
        System.out.println("  Engine (" + type + ") stopped.");
    }
}
