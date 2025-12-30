package org.example.p1_oops.c7_composition.composition;

// Composition: Car HAS-A Engine, Engine CANNOT exist without Car
public class Car {
    private String model;
    private Engine engine; // Engine is created INSIDE Car (strong ownership)

    public Car(String model, String engineType) {
        this.model = model;
        System.out.println("Creating Car: " + model);
        this.engine = new Engine(engineType); // Engine is created HERE
    }

    public void drive() {
        System.out.println("Car " + model + " is driving.");
        engine.start();
    }

    public void park() {
        System.out.println("Car " + model + " is parked.");
        engine.stop();
    }

    // When Car is destroyed, Engine is also destroyed
    // Engine cannot exist independently
    // This is COMPOSITION (strong relationship)
}
