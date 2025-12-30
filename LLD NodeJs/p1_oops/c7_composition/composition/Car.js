const Engine = require('./Engine');

/**
 * Car class demonstrating Composition (strong HAS-A relationship)
 * 
 * Composition: Car HAS-A Engine, Engine CANNOT exist without Car.
 * The Engine is created INSIDE the Car, not passed from outside.
 */
class Car {
    #model;
    #engine; // Engine is created INSIDE Car (strong ownership)

    constructor(model, engineType) {
        this.#model = model;
        console.log("Creating Car: " + model);
        this.#engine = new Engine(engineType); // Engine is created HERE
    }

    drive() {
        console.log("Car " + this.#model + " is driving.");
        this.#engine.start();
    }

    park() {
        console.log("Car " + this.#model + " is parked.");
        this.#engine.stop();
    }

    // When Car is destroyed (garbage collected), Engine is also destroyed
    // Engine cannot exist independently
    // This is COMPOSITION (strong relationship)
}

module.exports = Car;
