package org.example.p1_oops.c3_inheritance;

import org.example.p1_oops.c3_inheritance.base.User;
import org.example.p1_oops.c3_inheritance.child.Student;
import org.example.p1_oops.c3_inheritance.child.Mentor;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Inheritance & Polymorphism Demonstration ===\n");

        // --- 1. Inheritance: Creating a Child Object ---
        System.out.println("--- 1. Inheritance (Creating Student) ---");
        Student s1 = new Student("Alice", "alice@scaler.com", "Sept23");
        s1.login();        // Inherited from User
        s1.solveProblem(); // Overridden in Student
        System.out.println("Student batchName: " + s1.batchName);

        // --- 2. Constructor Chaining (super()) ---
        System.out.println("\n--- 2. Constructor Chaining (Creating Mentor) ---");
        Mentor m1 = new Mentor("Bob", "bob@google.com", "Google");
        m1.login();
        m1.solveProblem();

        // --- 3. Polymorphism (Upcasting) ---
        System.out.println("\n--- 3. Polymorphism: Parent ref = Child object ---");
        User u1 = new Student("Charlie", "charlie@scaler.com", "Oct23");
        User u2 = new Mentor("Dave", "dave@amazon.com", "Amazon");

        // At compile time, u1 is a User. At runtime, it's a Student.
        // The method called is decided at RUNTIME (Dynamic Method Dispatch).
        u1.solveProblem(); // Calls Student's version
        u2.solveProblem(); // Calls Mentor's version

        // --- 4. Negative Cases (Uncomment to see errors) ---
        System.out.println("\n--- 4. Negative Cases (See comments in code) ---");

        // CASE A: Child Reference = Parent Object
        // Student s2 = new User("Test", "test@email.com");
        System.out.println("// Student s2 = new User(...);");
        System.out.println("// COMPILE ERROR: Incompatible types.");
        System.out.println("// Reason: A User is NOT necessarily a Student. Parent cannot fit into Child reference.");

        // CASE B: Parent Reference cannot access Child-specific fields
        // User u3 = new Student("Test", "test@email.com", "Batch");
        // System.out.println(u3.batchName); // COMPILE ERROR
        System.out.println("\n// User u = new Student(...); u.batchName;");
        System.out.println("// COMPILE ERROR: Cannot find symbol 'batchName'.");
        System.out.println("// Reason: 'u' is a User ref. Compiler only knows User fields (name, email).");
    }
}
