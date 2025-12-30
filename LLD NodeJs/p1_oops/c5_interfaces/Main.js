const Flyable = require('./contract/Flyable');
const Swimmable = require('./contract/Swimmable');
const Bird = require('./impl/Bird');
const Fish = require('./impl/Fish');
const Duck = require('./impl/Duck');

function main() {
    console.log("=== Interfaces Demonstration (JavaScript) ===\n");

    // --- 1. Basic Interface Implementation ---
    console.log("--- 1. Basic Interface Implementation ---");
    const bird = new Bird();
    bird.fly();

    const fish = new Fish();
    fish.swim();

    // --- 2. Multiple Interface Implementation ---
    console.log("\n--- 2. Multiple Interface Implementation ---");
    const duck = new Duck();
    duck.fly();
    duck.swim();
    console.log("// Duck implements BOTH fly() and swim().");
    console.log("// In JS, we use duck typing or mixins instead of multiple extends.");

    // --- 3. Interface as a Type (Polymorphism) ---
    console.log("\n--- 3. Interface as a Type ---");
    // In JS, we don't have interface types, but we can use duck typing
    const flyables = [new Bird(), new Duck()];
    const swimmables = [new Fish(), new Duck()];

    console.log("Flying objects:");
    flyables.forEach(f => {
        Flyable.checkInterface(f); // Runtime check
        f.fly();
    });

    console.log("\nSwimming objects:");
    swimmables.forEach(s => {
        Swimmable.checkInterface(s); // Runtime check
        s.swim();
    });

    // --- 4. Negative Cases ---
    console.log("\n--- 4. Negative Cases (See comments in code) ---");

    // CASE A: Cannot instantiate an "interface"
    // const f2 = new Flyable();
    // f2.fly(); // Throws Error: "Method 'fly()' must be implemented by subclass"
    console.log("// const f = new Flyable(); f.fly();");
    console.log("// RUNTIME ERROR: 'Method fly() must be implemented by subclass'");
    console.log("// Reason: Our interface simulation throws an error for unimplemented methods.");

    // CASE B: Object without required method
    console.log("\n// Checking interface compliance at runtime:");
    try {
        const invalidObject = { name: "Not a flyer" };
        Flyable.checkInterface(invalidObject);
    } catch (e) {
        console.log("// Error: " + e.message);
    }
}

main();
