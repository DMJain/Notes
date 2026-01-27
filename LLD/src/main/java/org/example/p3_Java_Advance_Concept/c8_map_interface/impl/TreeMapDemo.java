package org.example.p3_Java_Advance_Concept.c8_map_interface.impl;

import org.example.p3_Java_Advance_Concept.c8_map_interface.contracts.IMapDemo;
import java.util.*;

/**
 * Demonstrates TreeMap with sorted keys and NavigableMap methods.
 */
public class TreeMapDemo implements IMapDemo {

    @Override
    public String getDemoName() {
        return "TreeMap Demo";
    }

    @Override
    public void execute() {
        System.out.println("┌─────────────────────────────────────────────────────────┐");
        System.out.println("│                     TREEMAP DEMO                         │");
        System.out.println("└─────────────────────────────────────────────────────────┘");
        System.out.println();

        demonstrateSortedKeys();
        demonstrateNavigableMethods();
    }

    private void demonstrateSortedKeys() {
        System.out.println("📊 SORTED KEYS");
        System.out.println("─────────────────────────────────────────────");

        TreeMap<Integer, String> map = new TreeMap<>();
        map.put(30, "Thirty");
        map.put(10, "Ten");
        map.put(50, "Fifty");
        map.put(20, "Twenty");

        System.out.println("   Added: 30, 10, 50, 20");
        System.out.println("   Iteration: " + map + " ← Keys sorted!");
        System.out.println("   firstKey(): " + map.firstKey());
        System.out.println("   lastKey(): " + map.lastKey());

        System.out.println();
    }

    private void demonstrateNavigableMethods() {
        System.out.println("🧭 NAVIGABLEMAP METHODS");
        System.out.println("─────────────────────────────────────────────");

        TreeMap<Integer, String> map = new TreeMap<>();
        map.put(10, "A");
        map.put(20, "B");
        map.put(30, "C");
        map.put(40, "D");

        System.out.println("   Map: " + map);
        System.out.println("   floorKey(25): " + map.floorKey(25) + " ← largest ≤ 25");
        System.out.println("   ceilingKey(25): " + map.ceilingKey(25) + " ← smallest ≥ 25");
        System.out.println("   headMap(30): " + map.headMap(30) + " ← keys < 30");
        System.out.println("   tailMap(30): " + map.tailMap(30) + " ← keys ≥ 30");

        System.out.println();
    }
}
