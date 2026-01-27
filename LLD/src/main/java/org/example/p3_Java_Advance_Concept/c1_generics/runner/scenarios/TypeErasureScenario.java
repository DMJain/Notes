package org.example.p3_Java_Advance_Concept.c1_generics.runner.scenarios;

import org.example.p3_Java_Advance_Concept.c1_generics.impl.Box;

import java.util.ArrayList;
import java.util.List;

/**
 * Demonstrates type erasure and its implications.
 */
public class TypeErasureScenario {

    public void execute() {
        System.out.println("┌────────────────────────────────────────┐");
        System.out.println("│      SCENARIO 6: TYPE ERASURE          │");
        System.out.println("└────────────────────────────────────────┘\n");

        demonstrateBasicErasure();
        demonstrateRuntimeTypeLoss();
        demonstrateLimitations();
    }

    private void demonstrateBasicErasure() {
        System.out.println("▸ What is Type Erasure?");
        System.out.println("  ---------------------");
        System.out.println();
        System.out.println("  At COMPILE time:          At RUNTIME:");
        System.out.println("  ┌─────────────────┐       ┌─────────────────┐");
        System.out.println("  │ Box<String>     │  ──▸  │ Box             │");
        System.out.println("  │   T content     │       │   Object content│");
        System.out.println("  │   T get()       │       │   Object get()  │");
        System.out.println("  └─────────────────┘       └─────────────────┘");
        System.out.println();
        System.out.println("  Compiler erases type parameters and inserts casts!");
        System.out.println();
    }

    private void demonstrateRuntimeTypeLoss() {
        System.out.println("▸ Runtime Type Loss Demonstration");
        System.out.println("  --------------------------------");

        Box<String> stringBox = new Box<>();
        Box<Integer> intBox = new Box<>();

        // At runtime, both are just "Box" - no type parameter info!
        System.out.println("  Box<String>.getClass(): " + stringBox.getClass().getName());
        System.out.println("  Box<Integer>.getClass(): " + intBox.getClass().getName());

        boolean sameClass = stringBox.getClass() == intBox.getClass();
        System.out.println("\n  Are they the same class? " + sameClass);
        System.out.println("  ⚠️ YES! At runtime, generics are ERASED!\n");

        // Lists behave the same
        List<String> stringList = new ArrayList<>();
        List<Integer> intList = new ArrayList<>();

        System.out.println("  ArrayList<String>.getClass(): " + stringList.getClass().getName());
        System.out.println("  ArrayList<Integer>.getClass(): " + intList.getClass().getName());
        System.out.println("  Same class? " + (stringList.getClass() == intList.getClass()));
        System.out.println();
    }

    private void demonstrateLimitations() {
        System.out.println("▸ Type Erasure Limitations");
        System.out.println("  ------------------------");
        System.out.println();

        System.out.println("  ❌ CANNOT create generic array:");
        System.out.println("     // T[] array = new T[10];  // COMPILE ERROR!");
        System.out.println("     // List<String>[] = new ArrayList<String>[10];  // ERROR!");
        System.out.println();

        System.out.println("  ❌ CANNOT use instanceof with parameterized type:");
        System.out.println("     // if (obj instanceof List<String>) { }  // ERROR!");
        System.out.println("     if (obj instanceof List<?>) { }  // ✅ OK with wildcard");
        System.out.println();

        System.out.println("  ❌ CANNOT create instance of type parameter:");
        System.out.println("     // return new T();  // COMPILE ERROR!");
        System.out.println();

        System.out.println("  ❌ CANNOT use primitives:");
        System.out.println("     // List<int> nums;  // COMPILE ERROR!");
        System.out.println("     List<Integer> nums;  // ✅ Use wrapper");
        System.out.println();

        System.out.println("  ✅ WORKAROUNDS:");
        printWorkaround();
        System.out.println();
    }

    private void printWorkaround() {
        System.out.println("     • For arrays: @SuppressWarnings(\"unchecked\")");
        System.out.println("       T[] array = (T[]) new Object[10];");
        System.out.println();
        System.out.println("     • For instanceof: Use raw type or wildcard");
        System.out.println("       if (obj instanceof List) { ... }");
        System.out.println();
        System.out.println("     • For new T(): Pass Class<T> object");
        System.out.println("       T create(Class<T> clazz) { return clazz.newInstance(); }");
    }
}
