package org.example.p3_Java_Advance_Concept.c1_generics.runner.scenarios;

import org.example.p3_Java_Advance_Concept.c1_generics.impl.NumberBox;
import org.example.p3_Java_Advance_Concept.c1_generics.impl.ComparableContainer;
import org.example.p3_Java_Advance_Concept.c1_generics.model.User;

/**
 * Demonstrates bounded generics: &lt;T extends X&gt; and multiple bounds.
 */
public class BoundedGenericScenario {

    public void execute() {
        System.out.println("┌────────────────────────────────────────┐");
        System.out.println("│     SCENARIO 4: BOUNDED GENERICS       │");
        System.out.println("└────────────────────────────────────────┘\n");

        demonstrateNumberBox();
        demonstrateComparableContainer();
        demonstrateWhy();
    }

    private void demonstrateNumberBox() {
        System.out.println("▸ NumberBox<T extends Number>");
        System.out.println("  The bound gives us access to Number methods");
        System.out.println("  --------------------------------------------");

        NumberBox<Integer> intBox = new NumberBox<>(42);
        NumberBox<Double> doubleBox = new NumberBox<>(3.14159);
        NumberBox<Long> longBox = new NumberBox<>(9999999999L);

        System.out.println("  Integer box:");
        System.out.println("    getContent():     " + intBox.getContent());
        System.out.println("    getDoubleValue(): " + intBox.getDoubleValue());
        System.out.println("    isPositive():     " + intBox.isPositive());

        System.out.println("\n  Double box:");
        System.out.println("    getContent():     " + doubleBox.getContent());
        System.out.println("    getIntValue():    " + doubleBox.getIntValue());

        System.out.println("\n  Long box:");
        System.out.println("    getContent():     " + longBox.getContent());
        System.out.println("    getDoubleValue(): " + longBox.getDoubleValue());

        System.out.println("\n  ⚠️ NumberBox<String> would be a COMPILE ERROR!");
        System.out.println("     String does not extend Number.");
        System.out.println();
    }

    private void demonstrateComparableContainer() {
        System.out.println("▸ ComparableContainer<T extends Comparable<T>>");
        System.out.println("  The bound allows sorting and finding min/max");
        System.out.println("  -----------------------------------------------");

        // With Integers
        ComparableContainer<Integer> numbers = new ComparableContainer<>();
        numbers.add(30);
        numbers.add(10);
        numbers.add(50);
        numbers.add(20);

        System.out.println("  Integer container:");
        System.out.println("    items: " + numbers.getItems());
        System.out.println("    min:   " + numbers.getMin());
        System.out.println("    max:   " + numbers.getMax());

        numbers.sort();
        System.out.println("    after sort(): " + numbers.getItems());

        // With Strings
        ComparableContainer<String> words = new ComparableContainer<>();
        words.add("delta");
        words.add("alpha");
        words.add("gamma");

        System.out.println("\n  String container:");
        System.out.println("    items: " + words.getItems());
        System.out.println("    min:   " + words.getMin());
        System.out.println("    max:   " + words.getMax());

        // With our User class (implements Comparable<User> by age)
        ComparableContainer<User> users = new ComparableContainer<>();
        users.add(new User(1L, "Alice", 30));
        users.add(new User(2L, "Bob", 25));
        users.add(new User(3L, "Charlie", 35));

        System.out.println("\n  User container (sorted by age):");
        System.out.println("    items: " + users.getItems());
        System.out.println("    youngest (min): " + users.getMin());
        System.out.println("    oldest (max):   " + users.getMax());
        System.out.println();
    }

    private void demonstrateWhy() {
        System.out.println("▸ WHY Use Bounded Generics?");
        System.out.println("  -------------------------");
        System.out.println();
        System.out.println("  WITHOUT bounds: Box<T>");
        System.out.println("    - T is erased to Object");
        System.out.println("    - Can only call Object methods (toString, hashCode, etc.)");
        System.out.println();
        System.out.println("  WITH bounds: Box<T extends Number>");
        System.out.println("    - T is erased to Number");
        System.out.println("    - Can call Number methods (doubleValue, intValue, etc.)");
        System.out.println("    - Type checking ensures only Number subtypes are used");
        System.out.println();
        System.out.println("  MULTIPLE bounds: <T extends Number & Comparable<T>>");
        System.out.println("    - T must satisfy ALL bounds");
        System.out.println("    - Can call methods from both Number AND Comparable");
        System.out.println();
    }
}
