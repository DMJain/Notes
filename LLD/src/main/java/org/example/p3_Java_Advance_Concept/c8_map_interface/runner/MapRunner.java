package org.example.p3_Java_Advance_Concept.c8_map_interface.runner;

import org.example.p3_Java_Advance_Concept.c8_map_interface.impl.*;

/**
 * Runner class that orchestrates all Map interface demonstrations.
 */
public class MapRunner {

    public static void runAllScenarios() {
        System.out.println("🚀 Starting Map Interface Demonstrations...");
        System.out.println("═".repeat(60));
        System.out.println();

        new HashMapDemo().execute();
        new TreeMapDemo().execute();

        System.out.println("═".repeat(60));
        System.out.println("✅ All Map demonstrations completed!");
        System.out.println();
        System.out.println("📚 Key takeaways:");
        System.out.println("   • HashMap: O(1) operations, no order");
        System.out.println("   • LinkedHashMap: O(1) + insertion order");
        System.out.println("   • TreeMap: O(log n), sorted keys + NavigableMap");
        System.out.println();
        System.out.println("📖 Next: c9_iterators, c10_custom_objects");
    }
}
