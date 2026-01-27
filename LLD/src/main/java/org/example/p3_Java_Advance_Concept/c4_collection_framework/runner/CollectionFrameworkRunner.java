package org.example.p3_Java_Advance_Concept.c4_collection_framework.runner;

import org.example.p3_Java_Advance_Concept.c4_collection_framework.impl.CollectionOverviewDemo;
import org.example.p3_Java_Advance_Concept.c4_collection_framework.runner.scenarios.HierarchyDemoScenario;

/**
 * Runner class that orchestrates all Collection Framework demonstrations.
 */
public class CollectionFrameworkRunner {

    /**
     * Runs all demonstration scenarios for the Collection Framework module.
     */
    public static void runAllScenarios() {
        System.out.println("🚀 Starting Collection Framework Demonstrations...");
        System.out.println("═".repeat(60));
        System.out.println();

        // Run overview demo
        CollectionOverviewDemo overviewDemo = new CollectionOverviewDemo();
        overviewDemo.execute();

        System.out.println("═".repeat(60));
        System.out.println();

        // Run hierarchy scenario
        HierarchyDemoScenario hierarchyScenario = new HierarchyDemoScenario();
        hierarchyScenario.execute();

        System.out.println("═".repeat(60));
        System.out.println("✅ All demonstrations completed!");
        System.out.println();
        System.out.println("📚 Next modules to explore:");
        System.out.println("   → c5_list_interface: ArrayList, LinkedList, Vector, Stack");
        System.out.println("   → c6_set_interface: HashSet, LinkedHashSet, TreeSet");
        System.out.println("   → c7_queue_interface: Queue, Deque, PriorityQueue");
        System.out.println("   → c8_map_interface: HashMap, LinkedHashMap, TreeMap");
    }
}
