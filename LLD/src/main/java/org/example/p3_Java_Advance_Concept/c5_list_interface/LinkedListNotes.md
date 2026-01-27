# LinkedList in Java

> **Prerequisites:** Understanding of [Collection Framework](../c4_collection_framework/CollectionFrameworkNotes.md), [ArrayList](./ArrayListNotes.md)

---

## What You'll Learn

| Question | Answer |
|----------|--------|
| **WHAT** | A doubly-linked list implementation of List AND Deque |
| **WHY** | Fast insertion/deletion at ends, implements both List and Queue |
| **WHEN** | Frequent add/remove at head/tail, need queue/stack behavior |
| **HOW** | Each element stored in a Node with prev/next pointers |

---

## 1. Hierarchy

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                         LINKEDLIST HIERARCHY                                 │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│   java.lang.Iterable<E>                                                      │
│          │                                                                   │
│          ▼                                                                   │
│   java.util.Collection<E>                                                    │
│          │                                                                   │
│          ├─────────────────────────────────────┐                            │
│          ▼                                     ▼                            │
│   java.util.List<E>                  java.util.Queue<E>                     │
│          │                                     │                            │
│          │                                     ▼                            │
│          │                           java.util.Deque<E>                     │
│          │                                     │                            │
│          └────────────┬────────────────────────┘                            │
│                       │                                                      │
│                       ▼                                                      │
│   ┌─────────────────────────────────────────────────────────────────┐       │
│   │                   java.util.LinkedList<E>                        │       │
│   │                                                                  │       │
│   │   Implements BOTH:                                               │       │
│   │   • List<E>  ←── Index-based access (though slow)               │       │
│   │   • Deque<E> ←── Double-ended queue operations                  │       │
│   │   • Cloneable, Serializable                                     │       │
│   └─────────────────────────────────────────────────────────────────┘       │
│                                                                              │
│   ⚠️ Does NOT implement RandomAccess (because random access is slow!)       │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 2. Internal Structure

### Node-Based Structure

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                     LINKEDLIST INTERNAL STRUCTURE                            │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│   LinkedList<String> list = new LinkedList<>();                              │
│   list.add("A"); list.add("B"); list.add("C");                              │
│                                                                              │
│   ┌─────────────────────────────────────────────────────────────────────┐   │
│   │                         DOUBLY LINKED LIST                          │   │
│   ├─────────────────────────────────────────────────────────────────────┤   │
│   │                                                                     │   │
│   │   first                                                    last     │   │
│   │     │                                                        │      │   │
│   │     ▼                                                        ▼      │   │
│   │  ┌──────────┐    ┌──────────┐    ┌──────────┐                      │   │
│   │  │   Node   │    │   Node   │    │   Node   │                      │   │
│   │  ├──────────┤    ├──────────┤    ├──────────┤                      │   │
│   │  │prev: null│◄───│prev      │◄───│prev      │                      │   │
│   │  │item: "A" │    │item: "B" │    │item: "C" │                      │   │
│   │  │next      │───►│next      │───►│next: null│                      │   │
│   │  └──────────┘    └──────────┘    └──────────┘                      │   │
│   │                                                                     │   │
│   │   Each Node contains:                                               │   │
│   │   • E item       ← The actual data                                  │   │
│   │   • Node<E> prev ← Pointer to previous node                         │   │
│   │   • Node<E> next ← Pointer to next node                             │   │
│   │                                                                     │   │
│   └─────────────────────────────────────────────────────────────────────┘   │
│                                                                              │
│   size = 3                                                                   │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

### Insertion at Head

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                     INSERT AT HEAD: O(1)                                     │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│   list.addFirst("X");  // or list.add(0, "X")                               │
│                                                                              │
│   BEFORE:                                                                    │
│   first                                                    last              │
│     │                                                        │               │
│     ▼                                                        ▼               │
│   ┌───────┐       ┌───────┐       ┌───────┐                                 │
│   │  "A"  │──────►│  "B"  │──────►│  "C"  │                                 │
│   └───────┘◄──────└───────┘◄──────└───────┘                                 │
│                                                                              │
│   STEP 1: Create new node                                                    │
│   ┌───────┐                                                                  │
│   │  "X"  │  (prev: null, next: null)                                       │
│   └───────┘                                                                  │
│                                                                              │
│   STEP 2: Point new node's next to old first                                │
│   ┌───────┐       ┌───────┐       ┌───────┐       ┌───────┐                 │
│   │  "X"  │──────►│  "A"  │──────►│  "B"  │──────►│  "C"  │                 │
│   └───────┘       └───────┘       └───────┘       └───────┘                 │
│                                                                              │
│   STEP 3: Point old first's prev to new node, update first reference        │
│   first                                                    last              │
│     │                                                        │               │
│     ▼                                                        ▼               │
│   ┌───────┐       ┌───────┐       ┌───────┐       ┌───────┐                 │
│   │  "X"  │◄─────►│  "A"  │◄─────►│  "B"  │◄─────►│  "C"  │                 │
│   └───────┘       └───────┘       └───────┘       └───────┘                 │
│                                                                              │
│   ✅ Only pointer changes, no shifting! O(1)                                │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

### Deletion at Middle

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                     DELETE IN MIDDLE                                         │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│   list.remove("B");  // O(n) to find, O(1) to remove                        │
│                                                                              │
│   BEFORE:                                                                    │
│   ┌───────┐       ┌───────┐       ┌───────┐                                 │
│   │  "A"  │◄─────►│  "B"  │◄─────►│  "C"  │                                 │
│   └───────┘       └───────┘       └───────┘                                 │
│                       ↑                                                      │
│                    Remove this                                               │
│                                                                              │
│   STEP 1: Find node to remove (O(n) traversal)                              │
│                                                                              │
│   STEP 2: Unlink by changing pointers                                        │
│   ┌───────┐       ┌ ─ ─ ─ ┐       ┌───────┐                                 │
│   │  "A"  │─ ─ ─ ─│  "B"  │─ ─ ─ ─│  "C"  │                                 │
│   └───────┘       └ ─ ─ ─ ┘       └───────┘                                 │
│       │                               ▲                                      │
│       └───────────────────────────────┘                                     │
│                                                                              │
│   AFTER:                                                                     │
│   ┌───────┐                       ┌───────┐                                 │
│   │  "A"  │◄─────────────────────►│  "C"  │                                 │
│   └───────┘                       └───────┘                                 │
│                                                                              │
│   ✅ No shifting elements! Just pointer changes                             │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 3. Time Complexity

| Operation | Time Complexity | Notes |
|-----------|-----------------|-------|
| `get(index)` | **O(n)** | Must traverse from head or tail |
| `set(index, element)` | **O(n)** | Find + set |
| `add(element)` | **O(1)** | Add at tail |
| `addFirst(element)` | **O(1)** | Add at head |
| `addLast(element)` | **O(1)** | Add at tail |
| `add(index, element)` | **O(n)** | Find position, then O(1) link |
| `remove(index)` | **O(n)** | Find position, then O(1) unlink |
| `removeFirst()` | **O(1)** | Direct access to first |
| `removeLast()` | **O(1)** | Direct access to last |
| `contains(object)` | **O(n)** | Linear search |
| `size()` | **O(1)** | Stored as field |

---

## 4. LinkedList as Deque

LinkedList implements `Deque` (double-ended queue), providing stack and queue operations:

```java
LinkedList<String> deque = new LinkedList<>();

// Queue operations (FIFO)
deque.offer("First");      // Add to tail
deque.offer("Second");
String first = deque.poll();  // Remove from head → "First"

// Stack operations (LIFO)
deque.push("Top");         // Add to head
String top = deque.pop();  // Remove from head → "Top"

// Deque operations
deque.addFirst("Head");
deque.addLast("Tail");
deque.peekFirst();         // View head without removing
deque.peekLast();          // View tail without removing
```

---

## 5. ArrayList vs LinkedList

| Feature | ArrayList | LinkedList |
|---------|-----------|------------|
| **Internal structure** | Dynamic array | Doubly-linked nodes |
| **Random access** | O(1) ✅ | O(n) ❌ |
| **Add at end** | O(1) amortized | O(1) |
| **Add at beginning** | O(n) ❌ | O(1) ✅ |
| **Add in middle** | O(n) | O(n) |
| **Remove from middle** | O(n) + shift | O(n) find + O(1) unlink |
| **Memory** | Compact | More overhead (prev/next pointers) |
| **Cache locality** | Good | Poor |
| **Implements** | List, RandomAccess | List, Deque |

### When to Choose Which?

```
ArrayList wins when:
├── Frequent random access (get/set by index)
├── Mostly adding at end
├── Memory is a concern
└── You want cache-friendly iteration

LinkedList wins when:
├── Frequent add/remove at head
├── Need Queue/Deque functionality
├── Never need random access
└── Size changes dramatically and frequently
```

---

## 6. Memory Overhead

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                     MEMORY COMPARISON                                        │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│   ArrayList storing 3 Strings:                                               │
│   ┌─────────────────────────────────────────────────┐                       │
│   │ Object[] array                                   │                       │
│   │ ┌─────────┬─────────┬─────────┬─────────────┐  │                       │
│   │ │ ref "A" │ ref "B" │ ref "C" │ unused      │  │                       │
│   │ └─────────┴─────────┴─────────┴─────────────┘  │                       │
│   │ + int size                                       │                       │
│   └─────────────────────────────────────────────────┘                       │
│   Overhead: ~8 bytes per element (reference only)                           │
│                                                                              │
│   LinkedList storing 3 Strings:                                              │
│   ┌──────────────┐  ┌──────────────┐  ┌──────────────┐                      │
│   │ Node object  │  │ Node object  │  │ Node object  │                      │
│   │ ├─ prev ref  │  │ ├─ prev ref  │  │ ├─ prev ref  │                      │
│   │ ├─ item ref  │  │ ├─ item ref  │  │ ├─ item ref  │                      │
│   │ └─ next ref  │  │ └─ next ref  │  │ └─ next ref  │                      │
│   └──────────────┘  └──────────────┘  └──────────────┘                      │
│   Overhead: ~24 bytes per element (Node object + 3 references)              │
│                                                                              │
│   ⚠️ LinkedList uses ~3x more memory per element!                           │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 7. When to Use LinkedList

### ✅ Good Use Cases

| Scenario | Why LinkedList |
|----------|----------------|
| Frequent add/remove at head | O(1) vs O(n) for ArrayList |
| Implementing a Queue | Deque interface built-in |
| Implementing a Stack | push/pop are O(1) |
| No random access needed | Traversal is fine |

### ❌ When NOT to Use

| Scenario | Better Alternative | Why |
|----------|-------------------|-----|
| Random access by index | `ArrayList` | O(n) in LinkedList |
| Memory-constrained | `ArrayList` | Less overhead |
| Cache-friendly iteration | `ArrayList` | Better locality |
| General purpose list | `ArrayList` | Usually faster |

---

## 8. Summary

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                          LINKEDLIST SUMMARY                                  │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│   WHAT:     Doubly-linked list implementing List AND Deque                  │
│   BACKING:  Node objects with prev/next pointers                            │
│                                                                              │
│   FAST:     addFirst(), addLast(), removeFirst(), removeLast()              │
│   SLOW:     get(i), set(i), any operation requiring traversal               │
│                                                                              │
│   UNIQUE:   Only List that also implements Deque                            │
│             Can be used as Queue, Stack, or Deque                           │
│                                                                              │
│   USE WHEN:                                                                  │
│   • Need queue/stack behavior                                                │
│   • Frequent add/remove at ends                                              │
│   • Don't need random access                                                 │
│                                                                              │
│   AVOID WHEN:                                                                │
│   • Need random access (use ArrayList)                                       │
│   • Memory is limited (ArrayList is more compact)                           │
│   • General purpose (ArrayList is usually better)                           │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```
