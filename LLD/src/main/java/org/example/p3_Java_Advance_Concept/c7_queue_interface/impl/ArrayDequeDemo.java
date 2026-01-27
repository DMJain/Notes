package org.example.p3_Java_Advance_Concept.c7_queue_interface.impl;

import org.example.p3_Java_Advance_Concept.c7_queue_interface.contracts.IQueueDemo;
import java.util.*;

/**
 * Demonstrates ArrayDeque as Stack and Queue.
 */
public class ArrayDequeDemo implements IQueueDemo {

    @Override
    public String getDemoName() {
        return "ArrayDeque Demo";
    }

    @Override
    public void execute() {
        System.out.println("┌─────────────────────────────────────────────────────────┐");
        System.out.println("│                   ARRAYDEQUE DEMO                        │");
        System.out.println("└─────────────────────────────────────────────────────────┘");
        System.out.println();

        demonstrateAsQueue();
        demonstrateAsStack();
    }

    private void demonstrateAsQueue() {
        System.out.println("📥 AS A QUEUE (FIFO)");
        System.out.println("─────────────────────────────────────────────");

        Deque<String> queue = new ArrayDeque<>();

        // Add to tail
        queue.offer("First");
        queue.offer("Second");
        queue.offer("Third");
        System.out.println("   After offer(): " + queue);

        // Remove from head
        System.out.println("   poll(): " + queue.poll() + " ← First in, first out");
        System.out.println("   After poll(): " + queue);

        // Peek at head
        System.out.println("   peek(): " + queue.peek() + " ← View without removing");

        System.out.println();
    }

    private void demonstrateAsStack() {
        System.out.println("📚 AS A STACK (LIFO)");
        System.out.println("─────────────────────────────────────────────");

        Deque<String> stack = new ArrayDeque<>();

        // Push to head
        stack.push("Bottom");
        stack.push("Middle");
        stack.push("Top");
        System.out.println("   After push(): " + stack);

        // Pop from head
        System.out.println("   pop(): " + stack.pop() + " ← Last in, first out");
        System.out.println("   After pop(): " + stack);

        // Peek at head
        System.out.println("   peek(): " + stack.peek());

        System.out.println("   ✅ Better than Stack class (no sync overhead)");
        System.out.println();
    }
}
