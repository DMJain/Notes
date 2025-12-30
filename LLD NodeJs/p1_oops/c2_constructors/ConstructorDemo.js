const Student = require('./Student');

function main() {
    console.log("=== Constructor & Memory Model Demo (JavaScript) ===\n");

    // 1. Constructor Demo
    console.log("--- 1. Constructors ---");
    const s1 = new Student("Alice", 20, [90, 85, 88]);
    console.log("Student 1 (Parameterized): " + s1.toString());

    // 2. Reference Copy (let x = y)
    console.log("\n--- 2. Reference Copy (let s2 = s1) ---");
    let s2 = s1; // s2 points to the SAME object as s1
    console.log("s2 created by assignment (s2 = s1).");

    s2.name = "Bob"; // Changing s2 changes s1 because they are the SAME object
    console.log("Changed s2.name to 'Bob'.");
    console.log("s1.name: " + s1.name); // Bob
    console.log("s2.name: " + s2.name); // Bob
    console.log(
        "Reason: 's2 = s1' copies the REFERENCE (address), not the object. Both point to the same memory location.");

    // Reset s1
    s1.name = "Alice";

    // 3. Pass-by-Reference Demo (JavaScript)
    console.log("\n--- 3. Pass-by-Value of Reference (JavaScript Memory Model) ---");

    // Case A: Reassigning Reference inside function
    console.log("Case A: Reassigning Reference inside function");
    console.log("Before modifyReference: " + s1.name);
    modifyReference(s1);
    console.log("After modifyReference: " + s1.name); // Alice (Unchanged)
    console.log("Reason: JS passes the VALUE of the reference. The function gets a COPY of the address.\n" +
        "Reassigning 's = new Student()' inside the function changes the COPY to point to a new object.\n" +
        "The original reference 's1' in main still points to the old object.");

    // Case B: Modifying Object inside function
    console.log("\nCase B: Modifying Object inside function");
    console.log("Before modifyObject: " + s1.name);
    modifyObject(s1);
    console.log("After modifyObject: " + s1.name); // ChangedName (Changed)
    console.log("Reason: The function gets a COPY of the address, but it still points to the SAME object.\n" +
        "Using 's.name = ...' follows the reference to the actual object in Heap and modifies it.");

    // 4. Deep vs Shallow Copy
    console.log("\n--- 4. Deep vs Shallow Copy ---");

    // Deep Copy using Copy Constructor
    const s3 = new Student(s1); // Pass s1 to constructor for deep copy
    console.log("s3 created using Copy Constructor (Deep Copy).");

    s3.examScores[0] = 100; // Modify s3's array
    console.log("Changed s3.examScores[0] to 100.");
    console.log("s1 scores: [" + s1.examScores.join(", ") + "]"); // Unchanged
    console.log("s3 scores: [" + s3.examScores.join(", ") + "]"); // Changed
    console.log("Reason: Deep copy created a NEW array in memory for s3. They are independent.");

    // Shallow Copy Simulation
    const s4 = new Student();
    s4.shallowCopyFrom(s1);
    console.log("\ns4 created using Shallow Copy.");

    s4.examScores[0] = 50; // Modify s4's array
    console.log("Changed s4.examScores[0] to 50.");
    console.log("s1 scores: [" + s1.examScores.join(", ") + "]"); // Changed!
    console.log("s4 scores: [" + s4.examScores.join(", ") + "]"); // Changed!
    console.log(
        "Reason: Shallow copy copied the array REFERENCE. Both s1 and s4 share the same array in memory.");
}

// Helper function for Case A
function modifyReference(s) {
    s = new Student("New Student", 22, [0, 0, 0]);
    console.log("  Inside modifyReference: s.name = " + s.name);
}

// Helper function for Case B
function modifyObject(s) {
    s.name = "ChangedName";
    console.log("  Inside modifyObject: s.name = " + s.name);
}

main();
