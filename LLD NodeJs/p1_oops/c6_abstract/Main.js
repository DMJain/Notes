const Shape = require('./base/Shape');
const Circle = require('./impl/Circle');
const Rectangle = require('./impl/Rectangle');

function main() {
    console.log("=== Abstract Classes Demonstration (JavaScript) ===\n");

    // --- 1. Creating Concrete Implementations ---
    console.log("--- 1. Creating Shapes ---");
    const circle = new Circle(5.0);
    const rectangle = new Rectangle(4.0, 6.0);

    circle.display(); // Uses concrete display() from Shape
    rectangle.display(); // Uses concrete display() from Shape

    // --- 2. Abstract Class as a Type (Polymorphism) ---
    console.log("\n--- 2. Abstract Class as Type ---");
    const s1 = new Circle(3.0);
    const s2 = new Rectangle(2.0, 5.0);

    s1.display();
    s2.display();

    // Using shapes polymorphically
    const shapes = [new Circle(2), new Rectangle(3, 4), new Circle(5)];
    console.log("\nCalculating areas for multiple shapes:");
    shapes.forEach((shape, i) => {
        console.log(`Shape ${i + 1}: Area = ${shape.getArea().toFixed(2)}`);
    });

    // --- 3. Negative Cases ---
    console.log("\n--- 3. Negative Cases (See comments in code) ---");

    // CASE A: Cannot instantiate abstract class
    // const shape = new Shape("Test");
    console.log("// const s = new Shape('Test');");
    try {
        const badShape = new Shape("Test");
    } catch (e) {
        console.log("// RUNTIME ERROR: " + e.message);
    }
    console.log("// Reason: Our abstract class simulation prevents direct instantiation.");

    // CASE B: Forgetting to implement abstract method
    console.log("\n// If a subclass doesn't implement getArea():");
    console.log("// Calling display() would throw: 'Method getArea() must be implemented'");
}

main();
