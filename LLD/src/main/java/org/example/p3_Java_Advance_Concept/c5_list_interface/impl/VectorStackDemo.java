package org.example.p3_Java_Advance_Concept.c5_list_interface.impl;

import org.example.p3_Java_Advance_Concept.c5_list_interface.contracts.IListDemo;
import java.util.*;

/**
 * Demonstrates Vector and Stack (legacy classes) and modern alternatives.
 */
public class VectorStackDemo implements IListDemo {

    @Override
    public String getDemoName() {
        return "Vector & Stack Demo";
    }

    @Override
    public void execute() {
        System.out.println("┌─────────────────────────────────────────────────────────┐");
        System.out.println("│               VECTOR & STACK DEMO (Legacy)               │");
        System.out.println("└─────────────────────────────────────────────────────────┘");
        System.out.println();

        demonstrateVector();
        demonstrateStack();
        demonstrateModernAlternatives();
    }

    private void demonstrateVector() {
        System.out.println("📦 VECTOR (Legacy - synchronized ArrayList)");
        System.out.println("─────────────────────────────────────────────");

        Vector<String> vector = new Vector<>();
        vector.add("A");
        vector.add("B");
        vector.add("C");

        System.out.println("   Vector: " + vector);
        System.out.println("   ⚠️ Every method is synchronized (slow in single-threaded code)");
        System.out.println("   ⚠️ Growth: doubles capacity (100%), ArrayList grows by 50%");
        System.out.println("   ❌ Avoid in new code - use ArrayList instead");

        System.out.println();
    }

    private void demonstrateStack() {
        System.out.println("📚 STACK (Deprecated - extends Vector)");
        System.out.println("─────────────────────────────────────────────");

        Stack<String> stack = new Stack<>();

        // Push elements
        stack.push("First");
        stack.push("Second");
        stack.push("Third");
        System.out.println("   After push(): " + stack);

        // Peek
        System.out.println("   peek(): " + stack.peek());

        // Pop
        System.out.println("   pop(): " + stack.pop());
        System.out.println("   After pop(): " + stack);

        // Search (1-based from top)
        System.out.println("   search(\"First\"): position " + stack.search("First") + " from top");

        // BAD: Can break stack semantics!
        stack.add(0, "WRONG!"); // Inserting at bottom!
        System.out.println("   💥 add(0, \"WRONG!\"): " + stack + " ← Breaks LIFO!");

        System.out.println("   ❌ Stack extends Vector - inherits non-stack methods!");

        System.out.println();
    }

    private void demonstrateModernAlternatives() {
        System.out.println("✅ MODERN ALTERNATIVES");
        System.out.println("─────────────────────────────────────────────");

        // For synchronized list
        System.out.println("   Instead of Vector:");
        System.out.println("   └─ List<String> list = Collections.synchronizedList(new ArrayList<>());");
        List<String> syncList = Collections.synchronizedList(new ArrayList<>());
        syncList.add("Safe");
        System.out.println("      Result: " + syncList);

        // For stack behavior
        System.out.println();
        System.out.println("   Instead of Stack:");
        System.out.println("   └─ Deque<String> stack = new ArrayDeque<>();");
        Deque<String> dequeStack = new ArrayDeque<>();
        dequeStack.push("First");
        dequeStack.push("Second");
        System.out.println("      push/pop work: " + dequeStack);
        System.out.println("      ✅ No add(index) method - can't break stack semantics!");

        System.out.println();
    }
}
