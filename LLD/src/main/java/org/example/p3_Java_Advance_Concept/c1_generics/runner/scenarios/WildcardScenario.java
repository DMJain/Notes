package org.example.p3_Java_Advance_Concept.c1_generics.runner.scenarios;

import org.example.p3_Java_Advance_Concept.c1_generics.util.WildcardUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Demonstrates wildcard usage: ?, ? extends T, ? super T.
 */
public class WildcardScenario {

    public void execute() {
        System.out.println("┌────────────────────────────────────────┐");
        System.out.println("│        SCENARIO 3: WILDCARDS           │");
        System.out.println("└────────────────────────────────────────┘\n");

        demonstrateUnboundedWildcard();
        demonstrateUpperBoundedWildcard();
        demonstrateLowerBoundedWildcard();
        demonstratePECS();
    }

    private void demonstrateUnboundedWildcard() {
        System.out.println("▸ Unbounded Wildcard: <?>");
        System.out.println("  Use when: You only need Object methods or don't care about type");
        System.out.println("  -------------------------------------------------------------------");

        List<String> strings = Arrays.asList("Hello", "World");
        List<Integer> integers = Arrays.asList(1, 2, 3);
        List<Double> doubles = Arrays.asList(1.1, 2.2, 3.3);

        System.out.println("  printList() accepts ANY List:");
        System.out.print("    ");
        WildcardUtils.printList(strings);
        System.out.print("    ");
        WildcardUtils.printList(integers);
        System.out.print("    ");
        WildcardUtils.printList(doubles);

        System.out.println("\n  getSize() works on any list:");
        System.out.println("    getSize(strings): " + WildcardUtils.getSize(strings));
        System.out.println("    getSize(integers): " + WildcardUtils.getSize(integers));
        System.out.println();
    }

    private void demonstrateUpperBoundedWildcard() {
        System.out.println("▸ Upper Bounded Wildcard: <? extends Number>");
        System.out.println("  Use when: READING from a collection (PRODUCER)");
        System.out.println("  -------------------------------------------------------------------");

        List<Integer> integers = Arrays.asList(1, 2, 3, 4, 5);
        List<Double> doubles = Arrays.asList(1.5, 2.5, 3.5);
        List<Long> longs = Arrays.asList(100L, 200L, 300L);

        System.out.println("  sumNumbers() accepts List<? extends Number>:");
        System.out.println("    sum(integers): " + WildcardUtils.sumNumbers(integers));
        System.out.println("    sum(doubles):  " + WildcardUtils.sumNumbers(doubles));
        System.out.println("    sum(longs):    " + WildcardUtils.sumNumbers(longs));

        System.out.println("\n  📖 WHY extends? We READ from the list (it PRODUCES values)");
        System.out.println("  ❌ Cannot WRITE to List<? extends Number> (except null)");
        System.out.println();
    }

    private void demonstrateLowerBoundedWildcard() {
        System.out.println("▸ Lower Bounded Wildcard: <? super Integer>");
        System.out.println("  Use when: WRITING to a collection (CONSUMER)");
        System.out.println("  -------------------------------------------------------------------");

        List<Integer> integerList = new ArrayList<>();
        List<Number> numberList = new ArrayList<>();
        List<Object> objectList = new ArrayList<>();

        System.out.println("  addNumbers() accepts List<? super Integer>:");

        WildcardUtils.addNumbers(integerList);
        System.out.println("    List<Integer> after addNumbers(): " + integerList);

        WildcardUtils.addNumbers(numberList);
        System.out.println("    List<Number> after addNumbers():  " + numberList);

        WildcardUtils.addNumbers(objectList);
        System.out.println("    List<Object> after addNumbers():  " + objectList);

        System.out.println("\n  📝 WHY super? We WRITE to the list (it CONSUMES values)");
        System.out.println("  ❌ Cannot READ specific type from List<? super Integer> (only Object)");
        System.out.println();
    }

    private void demonstratePECS() {
        System.out.println("▸ PECS in Action: copy(dest, src)");
        System.out.println("  Producer Extends, Consumer Super");
        System.out.println("  -------------------------------------------------------------------");

        System.out.println("\n  Method signature:");
        System.out.println("    copy(List<? super T> dest, List<? extends T> src)");
        System.out.println("           ↑ CONSUMER (super)     ↑ PRODUCER (extends)");

        List<Number> destination = new ArrayList<>();
        List<Integer> source = Arrays.asList(10, 20, 30);

        System.out.println("\n  Before copy:");
        System.out.println("    destination (List<Number>): " + destination);
        System.out.println("    source (List<Integer>):     " + source);

        WildcardUtils.copy(destination, source);

        System.out.println("\n  After copy(destination, source):");
        System.out.println("    destination: " + destination);

        System.out.println("\n  ✅ Integer is-a Number, so copy works!");
        System.out.println("  ✅ src PRODUCES Integers (extends)");
        System.out.println("  ✅ dest CONSUMES Numbers (super)");
        System.out.println();
    }
}
