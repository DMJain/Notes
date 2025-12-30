/**
 * Calculator class demonstrating method overloading in JavaScript
 * 
 * Note: JavaScript doesn't have true method overloading like Java.
 * We simulate it using:
 * 1. Default parameters
 * 2. Rest parameters
 * 3. Type checking with typeof
 */
class Calculator {

    // Simulated overloading using rest parameters and type checking
    add(...args) {
        if (args.length === 2) {
            const [a, b] = args;
            if (typeof a === 'number' && typeof b === 'number') {
                // Check if they're floats or integers for demonstration
                if (Number.isInteger(a) && Number.isInteger(b)) {
                    console.log("add(int, int) called");
                } else {
                    console.log("add(double, double) called");
                }
                return a + b;
            }
        } else if (args.length === 3) {
            const [a, b, c] = args;
            console.log("add(int, int, int) called");
            return a + b + c;
        }

        throw new Error("Invalid arguments for add()");
    }

    // Alternative: Using separate method names (more idiomatic JS)
    addTwo(a, b) {
        console.log("addTwo(a, b) called");
        return a + b;
    }

    addThree(a, b, c) {
        console.log("addThree(a, b, c) called");
        return a + b + c;
    }

    // Negative Case: Return type alone cannot differentiate methods
    // In JavaScript, you simply can't have two functions with the same name
    // The second one would overwrite the first one:
    // add(a, b) { return a + b; }
    // add(a, b) { return a + b; }  // This replaces the previous add!
}

module.exports = Calculator;
