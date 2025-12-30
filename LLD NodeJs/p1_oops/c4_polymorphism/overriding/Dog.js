const Animal = require('./Animal');

/**
 * Dog class extending Animal - demonstrates method overriding
 */
class Dog extends Animal {
    // Method override (no @Override annotation in JavaScript)
    speak() {
        console.log("Dog barks: Woof!");
    }
}

module.exports = Dog;
