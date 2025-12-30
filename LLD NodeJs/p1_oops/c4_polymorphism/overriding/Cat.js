const Animal = require('./Animal');

/**
 * Cat class extending Animal - demonstrates method overriding
 */
class Cat extends Animal {
    // Method override
    speak() {
        console.log("Cat meows: Meow!");
    }
}

module.exports = Cat;
