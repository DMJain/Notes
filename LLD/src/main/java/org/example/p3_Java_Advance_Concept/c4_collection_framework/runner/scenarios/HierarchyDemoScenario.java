package org.example.p3_Java_Advance_Concept.c4_collection_framework.runner.scenarios;

import org.example.p3_Java_Advance_Concept.c4_collection_framework.contracts.ICollectionDemo;

import java.util.*;

/**
 * Demonstrates the Collection hierarchy and interface relationships.
 * Shows how different collection types relate to each other.
 */
public class HierarchyDemoScenario implements ICollectionDemo {

    @Override
    public String getDemoName() {
        return "Collection Hierarchy Demo";
    }

    @Override
    public void execute() {
        System.out.println("┌─────────────────────────────────────────────────────────┐");
        System.out.println("│           COLLECTION HIERARCHY DEMONSTRATION             │");
        System.out.println("└─────────────────────────────────────────────────────────┘");
        System.out.println();

        demonstrateIterableHierarchy();
        demonstrateCollectionMethods();
        demonstrateInterfaceVsImplementation();
    }

    private void demonstrateIterableHierarchy() {
        System.out.println("🔗 ITERABLE HIERARCHY");
        System.out.println("─────────────────────────────────────────────");
        System.out.println();
        System.out.println("   Iterable<E>");
        System.out.println("       │");
        System.out.println("       └── Collection<E>");
        System.out.println("               │");
        System.out.println("               ├── List<E> ──── ArrayList, LinkedList");
        System.out.println("               ├── Set<E>  ──── HashSet, TreeSet");
        System.out.println("               └── Queue<E> ─── ArrayDeque, PriorityQueue");
        System.out.println();
        System.out.println("   ⚠️  Map<K,V> is SEPARATE (not under Collection!)");
        System.out.println();
    }

    private void demonstrateCollectionMethods() {
        System.out.println("📝 COMMON COLLECTION METHODS");
        System.out.println("─────────────────────────────────────────────");

        // All these methods work on ANY Collection type!
        Collection<String> collection = new ArrayList<>();

        // Add elements
        collection.add("Alpha");
        collection.add("Beta");
        collection.add("Gamma");
        System.out.println("   add(): " + collection);

        // Size
        System.out.println("   size(): " + collection.size());

        // Contains
        System.out.println("   contains(\"Beta\"): " + collection.contains("Beta"));

        // isEmpty
        System.out.println("   isEmpty(): " + collection.isEmpty());

        // Remove
        collection.remove("Beta");
        System.out.println("   remove(\"Beta\"): " + collection);

        // Clear
        Collection<String> copy = new ArrayList<>(collection);
        copy.clear();
        System.out.println("   clear(): " + copy);

        // AddAll
        Collection<String> more = Arrays.asList("Delta", "Epsilon");
        collection.addAll(more);
        System.out.println("   addAll(): " + collection);

        System.out.println();
        System.out.println("   ✓ These methods work on List, Set, and Queue!");
        System.out.println();
    }

    private void demonstrateInterfaceVsImplementation() {
        System.out.println("🎭 PROGRAM TO INTERFACE, NOT IMPLEMENTATION");
        System.out.println("─────────────────────────────────────────────");
        System.out.println();

        System.out.println("   ❌ Bad: Coupled to implementation");
        System.out.println("      ArrayList<String> list = new ArrayList<>();");
        System.out.println();

        System.out.println("   ✅ Good: Program to interface");
        System.out.println("      List<String> list = new ArrayList<>();");
        System.out.println();

        System.out.println("   WHY? Easy to switch implementations:");
        System.out.println("      List<String> list = new LinkedList<>();  // Just change this!");
        System.out.println();

        // Demonstrate polymorphism
        List<String> asList = new ArrayList<>();
        List<String> linkedList = new LinkedList<>();

        // Same interface, different implementations
        processAnyList(asList);
        processAnyList(linkedList);

        System.out.println("   Both ArrayList and LinkedList work with List<> reference!");
        System.out.println();
    }

    private void processAnyList(List<String> list) {
        list.add("Item");
        // Works with ANY List implementation!
    }
}
