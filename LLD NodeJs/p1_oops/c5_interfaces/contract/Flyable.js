/**
 * Flyable interface in JavaScript
 * 
 * Note: JavaScript doesn't have interfaces like Java.
 * We use either:
 * 1. Documentation/conventions (duck typing)
 * 2. Abstract class with throw statements
 * 3. TypeScript (for compile-time checking)
 * 
 * This is a simulation using a class that throws if methods aren't implemented.
 */
class Flyable {
    fly() {
        throw new Error("Method 'fly()' must be implemented by subclass");
    }
}

// For duck typing approach, you can also just document the expected interface
// and check at runtime:
Flyable.checkInterface = function (obj) {
    if (typeof obj.fly !== 'function') {
        throw new TypeError("Object must implement fly() method");
    }
    return true;
};

module.exports = Flyable;
