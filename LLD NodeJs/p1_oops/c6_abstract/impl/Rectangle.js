const Shape = require('../base/Shape');

/**
 * Rectangle class extending abstract Shape
 */
class Rectangle extends Shape {
    #length; // Private field
    #width;

    constructor(length, width) {
        super("Rectangle");
        this.#length = length;
        this.#width = width;
    }

    // Implementing abstract method
    getArea() {
        return this.#length * this.#width;
    }
}

module.exports = Rectangle;
