package org.example.p3_Java_Advance_Concept.c10_custom_objects.impl;

import org.example.p3_Java_Advance_Concept.c10_custom_objects.contracts.ICustomObjectDemo;
import java.util.*;

/**
 * Demonstrates proper hashCode/equals implementation.
 */
public class HashCodeEqualsDemo implements ICustomObjectDemo {

    @Override
    public String getDemoName() {
        return "hashCode/equals Demo";
    }

    @Override
    public void execute() {
        System.out.println("┌─────────────────────────────────────────────────────────┐");
        System.out.println("│               HASHCODE/EQUALS DEMO                       │");
        System.out.println("└─────────────────────────────────────────────────────────┘");
        System.out.println();

        demonstrateProblem();
        demonstrateSolution();
    }

    private void demonstrateProblem() {
        System.out.println("❌ THE PROBLEM (without hashCode/equals)");
        System.out.println("─────────────────────────────────────────────");

        // BadPerson doesn't override hashCode/equals
        Set<BadPerson> badSet = new HashSet<>();
        badSet.add(new BadPerson("Alice", 25));
        badSet.add(new BadPerson("Alice", 25)); // Should be duplicate!

        System.out.println("   Added two BadPerson(\"Alice\", 25) to HashSet");
        System.out.println("   Set size: " + badSet.size() + " ← Should be 1!");
        System.out.println("   💥 Both added because default equals() uses ==");

        System.out.println();
    }

    private void demonstrateSolution() {
        System.out.println("✅ THE SOLUTION (with hashCode/equals)");
        System.out.println("─────────────────────────────────────────────");

        // GoodPerson properly overrides hashCode/equals
        Set<GoodPerson> goodSet = new HashSet<>();
        goodSet.add(new GoodPerson("Alice", 25));
        goodSet.add(new GoodPerson("Alice", 25)); // Correctly rejected!

        System.out.println("   Added two GoodPerson(\"Alice\", 25) to HashSet");
        System.out.println("   Set size: " + goodSet.size() + " ← Correct!");
        System.out.println("   ✅ Duplicate rejected because equals() checks content");

        System.out.println();
    }

    // Class WITHOUT hashCode/equals - problematic
    static class BadPerson {
        private final String name;
        private final int age;

        BadPerson(String name, int age) {
            this.name = name;
            this.age = age;
        }
    }

    // Class WITH hashCode/equals - correct
    static class GoodPerson {
        private final String name;
        private final int age;

        GoodPerson(String name, int age) {
            this.name = name;
            this.age = age;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o)
                return true;
            if (o == null || getClass() != o.getClass())
                return false;
            GoodPerson person = (GoodPerson) o;
            return age == person.age && Objects.equals(name, person.name);
        }

        @Override
        public int hashCode() {
            return Objects.hash(name, age);
        }
    }
}
