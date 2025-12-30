const Flyable = require('../contract/Flyable');
const Swimmable = require('../contract/Swimmable');

/**
 * Duck implements BOTH Flyable and Swimmable
 * 
 * Note: JavaScript doesn't support multiple inheritance,
 * so we can't extend both Flyable and Swimmable.
 * 
 * Approaches for multiple interface implementation:
 * 1. Mixins (shown here)
 * 2. Composition
 * 3. Duck typing (just implement the methods)
 */
class Duck {
    // Implement both interfaces' methods directly
    fly() {
        console.log("Duck is flying low.");
    }

    swim() {
        console.log("Duck is swimming in the pond.");
    }
}

// Optional: Verify interface compliance
Flyable.checkInterface(new Duck());
Swimmable.checkInterface(new Duck());

module.exports = Duck;
