const Flyable = require('../contract/Flyable');

/**
 * Bird class implementing Flyable
 * In JS, we extend the interface and implement the method
 */
class Bird extends Flyable {
    fly() {
        console.log("Bird is flying in the sky.");
    }
}

module.exports = Bird;
