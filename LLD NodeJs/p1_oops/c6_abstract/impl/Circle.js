const Shape = require('../base/Shape');

/**
 * Circle class extending abstract Shape
 */
class Circle extends Shape {
    #radius; // Private field

    constructor(radius) {
        super("Circle");
        this.#radius = radius;
    }

    // Implementing abstract method
    getArea() {
        return Math.PI * this.#radius * this.#radius;
    }
}

module.exports = Circle;
