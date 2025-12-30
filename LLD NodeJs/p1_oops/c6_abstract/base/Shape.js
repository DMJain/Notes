/**
 * Abstract Shape class in JavaScript
 * 
 * JavaScript doesn't have the 'abstract' keyword.
 * We simulate abstract classes by:
 * 1. Throwing errors in methods that must be overridden
 * 2. Checking the constructor to prevent direct instantiation (optional)
 */
class Shape {
    _name;

    constructor(name) {
        // Prevent direct instantiation of abstract class
        if (new.target === Shape) {
            throw new Error("Cannot instantiate abstract class Shape directly");
        }
        this._name = name;
    }

    // Abstract method - MUST be implemented by child classes
    getArea() {
        throw new Error("Method 'getArea()' must be implemented by subclass");
    }

    // Concrete method - CAN be used directly by child classes
    display() {
        console.log("Shape: " + this._name + ", Area: " + this.getArea());
    }
}

module.exports = Shape;
