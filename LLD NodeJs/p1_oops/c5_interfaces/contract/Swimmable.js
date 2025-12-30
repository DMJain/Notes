/**
 * Swimmable interface in JavaScript
 * Simulated using a class that throws if methods aren't implemented.
 */
class Swimmable {
    swim() {
        throw new Error("Method 'swim()' must be implemented by subclass");
    }
}

// Duck typing interface check
Swimmable.checkInterface = function (obj) {
    if (typeof obj.swim !== 'function') {
        throw new TypeError("Object must implement swim() method");
    }
    return true;
};

module.exports = Swimmable;
