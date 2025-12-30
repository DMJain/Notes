package org.example.p1_oops.c7_composition;

import org.example.p1_oops.c7_composition.association.Teacher;
import org.example.p1_oops.c7_composition.association.Student;
import org.example.p1_oops.c7_composition.composition.Car;

public class Main {
    public static void main(String[] args) {
        System.out.println("=== Composition & Association Demonstration ===\n");

        // --- 1. Association (Weak HAS-A) ---
        System.out.println("--- 1. Association (Student - Teacher) ---");

        // Teacher exists independently
        Teacher mathTeacher = new Teacher("Mr. Smith", "Math");

        // Student is associated with Teacher (reference passed in)
        Student student1 = new Student("Alice", mathTeacher);
        Student student2 = new Student("Bob", mathTeacher);

        student1.study();
        student2.study();

        // Both students share the same teacher
        // Teacher can exist without any student
        // This is ASSOCIATION
        System.out.println("// Key: Teacher created OUTSIDE Student, passed as reference.");
        System.out.println("// Both can exist independently.\n");

        // --- 2. Composition (Strong HAS-A) ---
        System.out.println("--- 2. Composition (Car - Engine) ---");

        // Engine is created INSIDE Car (not passed in)
        Car car = new Car("Tesla Model S", "Electric Motor");
        car.drive();
        car.park();

        System.out.println("\n// Key: Engine created INSIDE Car.");
        System.out.println("// If Car is destroyed, Engine is also gone.");
        System.out.println("// Engine cannot exist without Car.\n");

        // --- 3. Summary ---
        System.out.println("--- 3. Summary ---");
        System.out.println("| Type        | Ownership | Example       |");
        System.out.println("|-------------|-----------|---------------|");
        System.out.println("| Association | Weak      | Student-Teacher|");
        System.out.println("| Composition | Strong    | Car-Engine    |");
    }
}
