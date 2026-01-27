package org.example.p3_Java_Advance_Concept.c8_map_interface.impl;

import org.example.p3_Java_Advance_Concept.c8_map_interface.contracts.IMapDemo;
import java.util.*;

/**
 * Demonstrates HashMap operations and O(1) performance.
 */
public class HashMapDemo implements IMapDemo {

    @Override
    public String getDemoName() {
        return "HashMap Demo";
    }

    @Override
    public void execute() {
        System.out.println("┌─────────────────────────────────────────────────────────┐");
        System.out.println("│                     HASHMAP DEMO                         │");
        System.out.println("└─────────────────────────────────────────────────────────┘");
        System.out.println();

        demonstrateBasics();
        demonstrateMethods();
    }

    private void demonstrateBasics() {
        System.out.println("📋 BASIC OPERATIONS");
        System.out.println("─────────────────────────────────────────────");

        Map<String, Integer> map = new HashMap<>();

        // Put
        map.put("Alice", 25);
        map.put("Bob", 30);
        map.put("Charlie", 35);
        System.out.println("   After put(): " + map);

        // Get - O(1)
        System.out.println("   get(\"Bob\"): " + map.get("Bob") + " ← O(1)!");

        // Contains - O(1)
        System.out.println("   containsKey(\"Alice\"): " + map.containsKey("Alice"));

        // Remove
        map.remove("Bob");
        System.out.println("   After remove(\"Bob\"): " + map);

        System.out.println();
    }

    private void demonstrateMethods() {
        System.out.println("🔧 USEFUL METHODS");
        System.out.println("─────────────────────────────────────────────");

        Map<String, Integer> map = new HashMap<>();
        map.put("A", 1);
        map.put("B", 2);

        // getOrDefault
        int val = map.getOrDefault("C", 0);
        System.out.println("   getOrDefault(\"C\", 0): " + val);

        // putIfAbsent
        map.putIfAbsent("B", 100); // Won't change, B exists
        map.putIfAbsent("C", 3); // Will add, C doesn't exist
        System.out.println("   After putIfAbsent: " + map);

        // merge (counting pattern)
        map.merge("A", 1, Integer::sum);
        System.out.println("   After merge(\"A\", 1, sum): A = " + map.get("A"));

        // compute
        map.compute("B", (k, v) -> v * 10);
        System.out.println("   After compute(\"B\", v*10): B = " + map.get("B"));

        System.out.println();
    }
}
