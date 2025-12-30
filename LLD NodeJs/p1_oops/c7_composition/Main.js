const Teacher = require('./association/Teacher');
const Student = require('./association/Student');
const Car = require('./composition/Car');

function main() {
    console.log("=== Composition & Association Demonstration (JavaScript) ===\n");

    // --- 1. Association (Weak HAS-A) ---
    console.log("--- 1. Association (Student - Teacher) ---");

    // Teacher exists independently
    const mathTeacher = new Teacher("Mr. Smith", "Math");

    // Student is associated with Teacher (reference passed in)
    const student1 = new Student("Alice", mathTeacher);
    const student2 = new Student("Bob", mathTeacher);

    student1.study();
    student2.study();

    // Both students share the same teacher
    // Teacher can exist without any student
    // This is ASSOCIATION
    console.log("// Key: Teacher created OUTSIDE Student, passed as reference.");
    console.log("// Both can exist independently.\n");

    // --- 2. Composition (Strong HAS-A) ---
    console.log("--- 2. Composition (Car - Engine) ---");

    // Engine is created INSIDE Car (not passed in)
    const car = new Car("Tesla Model S", "Electric Motor");
    car.drive();
    car.park();

    console.log("\n// Key: Engine created INSIDE Car.");
    console.log("// If Car is garbage collected, Engine is also gone.");
    console.log("// Engine cannot exist without Car.\n");

    // --- 3. Summary ---
    console.log("--- 3. Summary ---");
    console.log("| Type        | Ownership | Example        |");
    console.log("|-------------|-----------|----------------|");
    console.log("| Association | Weak      | Student-Teacher|");
    console.log("| Composition | Strong    | Car-Engine     |");
}

main();
