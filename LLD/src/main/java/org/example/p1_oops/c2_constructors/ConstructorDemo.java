package org.example.p1_oops.c2_constructors;

public class ConstructorDemo {
    public static void main(String[] args) {
        System.out.println("=== Constructor & Memory Model Demo ===\n");

        // 1. Constructor Demo
        System.out.println("--- 1. Constructors ---");
        Student s1 = new Student("Alice", 20, new int[] { 90, 85, 88 });
        System.out.println("Student 1 (Parameterized): " + s1);

        // 2. Reference Copy (Student x = y)
        System.out.println("\n--- 2. Reference Copy (Student x = y) ---");
        Student s2 = s1; // s2 points to the SAME object as s1
        System.out.println("s2 created by assignment (s2 = s1).");

        s2.name = "Bob"; // Changing s2 changes s1 because they are the SAME object
        System.out.println("Changed s2.name to 'Bob'.");
        System.out.println("s1.name: " + s1.name); // Bob
        System.out.println("s2.name: " + s2.name); // Bob
        System.out.println(
                "Reason: 's2 = s1' copies the REFERENCE (address), not the object. Both point to the same memory location.");

        // Reset s1
        s1.name = "Alice";

        // 3. Pass-by-Value Demo
        System.out.println("\n--- 3. Pass-by-Value (Java Memory Model) ---");

        // Case A: Reassigning Reference inside function
        System.out.println("Case A: Reassigning Reference inside function");
        System.out.println("Before modifyReference: " + s1.name);
        modifyReference(s1);
        System.out.println("After modifyReference: " + s1.name); // Alice (Unchanged)
        System.out.println("Reason: Java passes the VALUE of the reference. The function gets a COPY of the address. \n"
                +
                "Reassigning 's = new Student()' inside the function changes the COPY to point to a new object. \n" +
                "The original reference 's1' in main still points to the old object.");

        // Case B: Modifying Object inside function
        System.out.println("\nCase B: Modifying Object inside function");
        System.out.println("Before modifyObject: " + s1.name);
        modifyObject(s1);
        System.out.println("After modifyObject: " + s1.name); // ChangedName (Changed)
        System.out
                .println("Reason: The function gets a COPY of the address, but it still points to the SAME object. \n" +
                        "Using 's.name = ...' follows the reference to the actual object in Heap and modifies it.");

        // 4. Deep vs Shallow Copy
        System.out.println("\n--- 4. Deep vs Shallow Copy ---");

        // Deep Copy using Copy Constructor
        Student s3 = new Student(s1);
        System.out.println("s3 created using Copy Constructor (Deep Copy).");

        s3.examScores[0] = 100; // Modify s3's array
        System.out.println("Changed s3.examScores[0] to 100.");
        System.out.println("s1 scores: " + java.util.Arrays.toString(s1.examScores)); // Unchanged
        System.out.println("s3 scores: " + java.util.Arrays.toString(s3.examScores)); // Changed
        System.out.println("Reason: Deep copy created a NEW array in memory for s3. They are independent.");

        // Shallow Copy Simulation
        Student s4 = new Student();
        s4.shallowCopyFrom(s1);
        System.out.println("\ns4 created using Shallow Copy.");

        s4.examScores[0] = 50; // Modify s4's array
        System.out.println("Changed s4.examScores[0] to 50.");
        System.out.println("s1 scores: " + java.util.Arrays.toString(s1.examScores)); // Changed!
        System.out.println("s4 scores: " + java.util.Arrays.toString(s4.examScores)); // Changed!
        System.out.println(
                "Reason: Shallow copy copied the array REFERENCE. Both s1 and s4 share the same array in memory.");
    }

    // Helper method for Case A
    public static void modifyReference(Student s) {
        s = new Student("New Student", 22, new int[] { 0, 0, 0 });
        System.out.println("  Inside modifyReference: s.name = " + s.name);
    }

    // Helper method for Case B
    public static void modifyObject(Student s) {
        s.name = "ChangedName";
        System.out.println("  Inside modifyObject: s.name = " + s.name);
    }
}
