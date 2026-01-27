# Queue and Deque in Java

> **Prerequisites:** [Collection Framework](../c4_collection_framework/CollectionFrameworkNotes.md), [List Interface](../c5_list_interface/LinkedListNotes.md)

---

## What You'll Learn

| Question | Answer |
|----------|--------|
| **WHAT** | FIFO (Queue) and double-ended (Deque) data structures |
| **WHY** | Task scheduling, BFS, undo/redo, sliding window |
| **WHEN** | Need ordered processing, stack/queue behavior |
| **HOW** | ArrayDeque (circular array) or LinkedList |

---

## 1. Hierarchy

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                     QUEUE/DEQUE HIERARCHY                                    │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│   java.lang.Iterable<E>                                                      │
│          │                                                                   │
│          ▼                                                                   │
│   java.util.Collection<E>                                                    │
│          │                                                                   │
│          ▼                                                                   │
│   java.util.Queue<E>  ←─── FIFO: add at tail, remove from head              │
│          │                                                                   │
│          ├─────────────────────────────────────────┐                        │
│          ▼                                         ▼                        │
│   java.util.Deque<E>                      java.util.PriorityQueue<E>        │
│   (Double-Ended Queue)                    (See PriorityQueueNotes.md)       │
│          │                                                                   │
│          ├─────────────────┬───────────────────┐                            │
│          ▼                 ▼                   ▼                            │
│   ┌─────────────┐   ┌─────────────┐   ┌─────────────────┐                   │
│   │ ArrayDeque  │   │ LinkedList  │   │ ConcurrentLinked│                   │
│   │ (preferred) │   │ (also List) │   │     Deque       │                   │
│   └─────────────┘   └─────────────┘   └─────────────────┘                   │
│                                                                              │
│   ⚠️ Use ArrayDeque, NOT Stack or LinkedList for stack/queue!              │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 2. Queue Operations

```java
Queue<String> queue = new LinkedList<>();  // or new ArrayDeque<>();

// Three pairs of methods:
// ┌─────────────────┬─────────────┬────────────────┐
// │   Operation     │ Throws exc  │ Returns null   │
// ├─────────────────┼─────────────┼────────────────┤
// │ Insert at tail  │ add(e)      │ offer(e)       │
// │ Remove from head│ remove()    │ poll()         │
// │ Examine head    │ element()   │ peek()         │
// └─────────────────┴─────────────┴────────────────┘

queue.offer("First");   // Add to tail
queue.offer("Second");
queue.offer("Third");
// Queue: [First, Second, Third]
//         ↑ head        ↑ tail

queue.poll();   // Removes "First"
queue.peek();   // Returns "Second" without removing
```

---

## 3. ArrayDeque Internal Structure

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                     ARRAYDEQUE: CIRCULAR BUFFER                              │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│   ArrayDeque uses a resizable circular array:                               │
│                                                                              │
│   Initial state (capacity 16):                                               │
│   ┌───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┐        │
│   │   │   │   │   │   │   │   │   │ A │ B │ C │ D │   │   │   │   │        │
│   └───┴───┴───┴───┴───┴───┴───┴───┴───┴───┴───┴───┴───┴───┴───┴───┘        │
│                                   ↑ head      ↑ tail                         │
│                                                                              │
│   After addFirst("X"):                                                       │
│   ┌───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┐        │
│   │   │   │   │   │   │   │   │ X │ A │ B │ C │ D │   │   │   │   │        │
│   └───┴───┴───┴───┴───┴───┴───┴───┴───┴───┴───┴───┴───┴───┴───┴───┘        │
│                               ↑ head          ↑ tail                         │
│                                                                              │
│   Wraps around when reaching edges:                                          │
│   ┌───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┐        │
│   │ Y │ Z │   │   │   │   │   │ X │ A │ B │ C │ D │ E │ F │ G │ H │        │
│   └───┴───┴───┴───┴───┴───┴───┴───┴───┴───┴───┴───┴───┴───┴───┴───┘        │
│       ↑ tail              ↑ head                                             │
│                                                                              │
│   Benefits:                                                                  │
│   • O(1) insertions/removals at both ends                                   │
│   • No node allocation overhead (unlike LinkedList)                         │
│   • Good cache locality                                                      │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 4. Deque as Stack and Queue

```java
Deque<String> deque = new ArrayDeque<>();

// ═══════════════════════════════════════════════════════
// AS A STACK (LIFO) - Use these methods:
// ═══════════════════════════════════════════════════════
deque.push("First");    // Adds to head
deque.push("Second");
deque.push("Third");
// Stack: [Third, Second, First]
//         ↑ top

deque.pop();    // Returns "Third" (removes from head)
deque.peek();   // Returns "Second" (examines head)

// ═══════════════════════════════════════════════════════
// AS A QUEUE (FIFO) - Use these methods:
// ═══════════════════════════════════════════════════════
deque.offer("First");   // Adds to tail
deque.offer("Second");
deque.offer("Third");
// Queue: [First, Second, Third]
//         ↑ head       ↑ tail

deque.poll();   // Returns "First" (removes from head)
deque.peek();   // Returns "Second" (examines head)
```

---

## 5. Time Complexity

| Operation | ArrayDeque | LinkedList |
|-----------|------------|------------|
| `addFirst()` / `addLast()` | **O(1)** | O(1) |
| `removeFirst()` / `removeLast()` | **O(1)** | O(1) |
| `peekFirst()` / `peekLast()` | **O(1)** | O(1) |
| `get(index)` | N/A | O(n) |
| Memory per element | ~8 bytes | ~24 bytes |
| Cache locality | Good | Poor |

---

## 6. When to Use

### ArrayDeque (Preferred)

| Scenario | Why |
|----------|-----|
| Stack implementation | Faster than Stack class, no sync overhead |
| Queue implementation | Better than LinkedList (memory, cache) |
| Sliding window problems | O(1) both ends |
| BFS traversal | Fast queue operations |

### LinkedList (Rarely)

| Scenario | Why |
|----------|-----|
| Need List AND Queue/Deque | Only LinkedList implements both |
| Frequent removal from middle | But that's O(n) anyway |

---

## 7. Summary

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                     QUEUE/DEQUE SUMMARY                                      │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│   Queue: FIFO - offer()/poll()/peek()                                       │
│   Deque: Double-ended - works as both Stack and Queue                       │
│                                                                              │
│   ✅ USE: ArrayDeque                                                        │
│      • Best for Stack (push/pop/peek)                                       │
│      • Best for Queue (offer/poll/peek)                                     │
│      • Circular array, good performance                                     │
│                                                                              │
│   ❌ AVOID: Stack class (legacy, synchronized)                              │
│   ❌ AVOID: LinkedList for pure queue/stack (more memory)                   │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```
