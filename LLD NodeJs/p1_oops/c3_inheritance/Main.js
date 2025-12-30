const User = require('./base/User');
const Student = require('./child/Student');
const Mentor = require('./child/Mentor');

function main() {
    console.log("=== Inheritance & Polymorphism Demonstration (JavaScript) ===\n");

    // --- 1. Inheritance: Creating a Child Object ---
    console.log("--- 1. Inheritance (Creating Student) ---");
    const s1 = new Student("Alice", "alice@scaler.com", "Sept23");
    s1.login();        // Inherited from User
    s1.solveProblem(); // Overridden in Student
    console.log("Student batchName: " + s1.batchName);

    // --- 2. Constructor Chaining (super()) ---
    console.log("\n--- 2. Constructor Chaining (Creating Mentor) ---");
    const m1 = new Mentor("Bob", "bob@google.com", "Google");
    m1.login();
    m1.solveProblem();

    // --- 3. Polymorphism (Upcasting) ---
    console.log("\n--- 3. Polymorphism: Parent ref = Child object ---");
    // In JS, we don't have static typing, but we can still demonstrate polymorphism
    const u1 = new Student("Charlie", "charlie@scaler.com", "Oct23");
    const u2 = new Mentor("Dave", "dave@amazon.com", "Amazon");

    // At runtime, JavaScript calls the actual object's method
    u1.solveProblem(); // Calls Student's version
    u2.solveProblem(); // Calls Mentor's version

    // --- 4. Negative Cases (See comments) ---
    console.log("\n--- 4. Negative Cases (See comments in code) ---");

    // CASE A: Child Reference = Parent Object
    // In JS, this would just work, but the object won't have child properties
    const u3 = new User("Test", "test@email.com");
    console.log("// const s2 = new User(...); s2.batchName;");
    console.log("// RESULT: undefined (User doesn't have batchName property)");
    console.log("// In Java, this would be a compile error.");

    // CASE B: Parent instance cannot access Child-specific fields
    console.log("\n// const u = new Student(...); u.batchName;");
    console.log("// WORKS in JS because JS is dynamically typed.");
    console.log("// In Java, if u was declared as User type, compiler wouldn't allow u.batchName.");

    // Demonstrate instanceof for type checking
    console.log("\n--- Using instanceof for type checking ---");
    console.log("u1 instanceof Student: " + (u1 instanceof Student)); // true
    console.log("u1 instanceof User: " + (u1 instanceof User)); // true
    console.log("u3 instanceof Student: " + (u3 instanceof Student)); // false
}

main();
