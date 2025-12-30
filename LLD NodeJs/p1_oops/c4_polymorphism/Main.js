const Calculator = require('./overloading/Calculator');
const Animal = require('./overriding/Animal');
const Dog = require('./overriding/Dog');
const Cat = require('./overriding/Cat');

function main() {
    console.log("=== Polymorphism Demonstration (JavaScript) ===\n");

    // --- 1. Method Overloading (Simulated in JavaScript) ---
    console.log("--- 1. Method Overloading (Simulated) ---");
    const calc = new Calculator();
    console.log("Result: " + calc.add(5, 10)); // add(int, int)
    console.log("Result: " + calc.add(5, 10, 15)); // add(int, int, int)
    console.log("Result: " + calc.add(5.5, 10.5)); // add(double, double)

    // Negative Case (Overloading)
    console.log("\n// Negative: JavaScript doesn't support true method overloading.");
    console.log("// If you define the same method twice, the second overwrites the first.");
    console.log("// Workaround: Use rest parameters (...args) or type checking.");

    // --- 2. Method Overriding (Runtime Polymorphism) ---
    console.log("\n--- 2. Method Overriding ---");
    const animal = new Animal();
    const dog = new Dog();
    const cat = new Cat();

    animal.speak(); // Animal's version
    dog.speak(); // Dog's version
    cat.speak(); // Cat's version

    // --- 3. Dynamic Method Dispatch ---
    console.log("\n--- 3. Dynamic Method Dispatch ---");
    // In JS, all typing is dynamic, but we can still demonstrate polymorphism
    const a1 = new Dog(); // Could be assigned to Animal type in TypeScript
    const a2 = new Cat();

    // JavaScript calls the ACTUAL object's method (Runtime decision)
    a1.speak(); // Dog barks
    a2.speak(); // Cat meows

    console.log("\n// Reason: JavaScript is dynamically typed.");
    console.log("// The actual object type determines WHICH implementation runs.");
    console.log("// There's no compile-time type checking like Java.");

    // Demonstrate polymorphism with an array
    console.log("\n--- Using Array for Polymorphism ---");
    const animals = [new Animal(), new Dog(), new Cat()];
    animals.forEach((animal, index) => {
        process.stdout.write(`animals[${index}].speak(): `);
        animal.speak();
    });
}

main();
