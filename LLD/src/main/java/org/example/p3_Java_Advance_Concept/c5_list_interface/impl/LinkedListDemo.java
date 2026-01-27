package org.example.p3_Java_Advance_Concept.c5_list_interface.impl;

import org.example.p3_Java_Advance_Concept.c5_list_interface.contracts.IListDemo;
import java.util.*;

/**
 * Demonstrates LinkedList as both List and Deque.
 */
public class LinkedListDemo implements IListDemo {

    @Override
    public String getDemoName() {
        return "LinkedList Demo";
    }

    @Override
    public void execute() {
        System.out.println("┌─────────────────────────────────────────────────────────┐");
        System.out.println("│                   LINKEDLIST DEMO                        │");
        System.out.println("└─────────────────────────────────────────────────────────┘");
        System.out.println();

        demonstrateListOperations();
        demonstrateDequeOperations();
        demonstrateStackBehavior();
    }

    private void demonstrateListOperations() {
        System.out.println("📋 AS A LIST");
        System.out.println("─────────────────────────────────────────────");

        LinkedList<String> list = new LinkedList<>();

        // Add elements
        list.add("A");
        list.add("B");
        list.add("C");
        System.out.println("   After add(): " + list);

        // Add at specific position
        list.add(1, "X");
        System.out.println("   add(1, \"X\"): " + list + " ← O(n) to find, O(1) to insert");

        // Get by index (slow!)
        System.out.println("   get(2): " + list.get(2) + " ← O(n) - must traverse!");

        // Does NOT implement RandomAccess
        boolean isRandomAccess = list instanceof RandomAccess;
        System.out.println("   Implements RandomAccess? " + isRandomAccess + " ← get(i) is O(n)!");

        System.out.println();
    }

    private void demonstrateDequeOperations() {
        System.out.println("📥 AS A DEQUE (Double-Ended Queue)");
        System.out.println("─────────────────────────────────────────────");

        LinkedList<String> deque = new LinkedList<>();

        // Add at both ends - O(1)
        deque.addFirst("Head");
        deque.addLast("Tail");
        System.out.println("   addFirst(\"Head\"), addLast(\"Tail\"): " + deque);

        // View without removing
        System.out.println("   peekFirst(): " + deque.peekFirst() + " ← O(1)");
        System.out.println("   peekLast(): " + deque.peekLast() + " ← O(1)");

        // Remove from both ends - O(1)
        String first = deque.removeFirst();
        String last = deque.removeLast();
        System.out.println("   removeFirst(): " + first + ", removeLast(): " + last);

        System.out.println();
    }

    private void demonstrateStackBehavior() {
        System.out.println("📚 AS A STACK (via Deque methods)");
        System.out.println("─────────────────────────────────────────────");

        LinkedList<String> stack = new LinkedList<>();

        // Push - adds to head
        stack.push("First");
        stack.push("Second");
        stack.push("Third");
        System.out.println("   After push(): " + stack);
        System.out.println("   Top (peek): " + stack.peek());

        // Pop - removes from head
        String popped = stack.pop();
        System.out.println("   pop(): " + popped);
        System.out.println("   After pop(): " + stack);

        System.out.println("   ⚠️ Note: ArrayDeque is preferred over LinkedList for stack!");

        System.out.println();
    }
}
