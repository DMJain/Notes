# PriorityQueue in Java

> **Prerequisites:** [Queue/Deque](./QueueDequeNotes.md), [Collection Framework](../c4_collection_framework/CollectionFrameworkNotes.md)

---

## What You'll Learn

| Question | Answer |
|----------|--------|
| **WHAT** | Heap-based queue where elements are ordered by priority |
| **WHY** | Always get the smallest (or largest) element efficiently |
| **WHEN** | Task scheduling, Dijkstra's, K-th largest, merge K lists |
| **HOW** | Binary min-heap stored in array |

---

## 1. Hierarchy

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                     PRIORITYQUEUE HIERARCHY                                  │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│   java.lang.Iterable<E>                                                      │
│          │                                                                   │
│          ▼                                                                   │
│   java.util.Collection<E>                                                    │
│          │                                                                   │
│          ▼                                                                   │
│   java.util.Queue<E>  ←─── NOT FIFO! Priority-based ordering                │
│          │                                                                   │
│          ▼                                                                   │
│   java.util.AbstractQueue<E>                                                 │
│          │                                                                   │
│          ▼                                                                   │
│   ┌─────────────────────────────────────────────────────────────────┐       │
│   │                    java.util.PriorityQueue<E>                    │       │
│   │                                                                  │       │
│   │   • Min-heap by default (smallest first)                        │       │
│   │   • Use Comparator.reverseOrder() for max-heap                 │       │
│   │   • NOT thread-safe (use PriorityBlockingQueue)                │       │
│   │   • Does NOT allow null                                        │       │
│   └─────────────────────────────────────────────────────────────────┘       │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 2. Binary Heap Structure

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                     MIN-HEAP AS BINARY TREE                                  │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│   PriorityQueue<Integer> pq = new PriorityQueue<>();                        │
│   pq.addAll(Arrays.asList(40, 20, 60, 10, 30));                             │
│                                                                              │
│   HEAP PROPERTY: Parent ≤ Children (min-heap)                               │
│                                                                              │
│                         ┌────┐                                              │
│                         │ 10 │  ← Minimum always at root                   │
│                         └────┘                                              │
│                       ┌───┴───┐                                             │
│                   ┌────┐   ┌────┐                                           │
│                   │ 20 │   │ 60 │                                           │
│                   └────┘   └────┘                                           │
│                  ┌──┴──┐                                                    │
│              ┌────┐  ┌────┐                                                 │
│              │ 40 │  │ 30 │                                                 │
│              └────┘  └────┘                                                 │
│                                                                              │
│   STORED AS ARRAY:                                                           │
│   ┌────┬────┬────┬────┬────┐                                               │
│   │ 10 │ 20 │ 60 │ 40 │ 30 │                                               │
│   └────┴────┴────┴────┴────┘                                               │
│     [0]  [1]  [2]  [3]  [4]                                                 │
│                                                                              │
│   Index formulas:                                                            │
│   • Parent of i: (i-1) / 2                                                  │
│   • Left child: 2*i + 1                                                     │
│   • Right child: 2*i + 2                                                    │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 3. Heapify Operations

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                     ADD (HEAPIFY UP)                                         │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│   pq.add(5);  // Add 5 to heap containing [10, 20, 60, 40, 30]              │
│                                                                              │
│   Step 1: Add at end                 Step 2: Bubble up                      │
│                                                                              │
│           ┌────┐                            ┌────┐                          │
│           │ 10 │                            │  5 │ ← New min!              │
│           └────┘                            └────┘                          │
│         ┌───┴───┐                         ┌───┴───┐                         │
│     ┌────┐   ┌────┐                   ┌────┐   ┌────┐                       │
│     │ 20 │   │ 60 │                   │ 10 │   │ 60 │                       │
│     └────┘   └────┘                   └────┘   └────┘                       │
│    ┌──┴──┐   ┌──┘                    ┌──┴──┐   ┌──┘                         │
│  ┌────┐┌────┐┌────┐              ┌────┐┌────┐┌────┐                         │
│  │ 40 ││ 30 ││ 5  │              │ 40 ││ 30 ││ 20 │                         │
│  └────┘└────┘└────┘              └────┘└────┘└────┘                         │
│              ↑ new                                                           │
│                                                                              │
│   Time: O(log n) - at most height swaps                                     │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 4. Time Complexity

| Operation | Time |
|-----------|------|
| `offer(e)` / `add(e)` | O(log n) |
| `poll()` / `remove()` | O(log n) |
| `peek()` | **O(1)** |
| `remove(Object)` | O(n) |
| `contains(Object)` | O(n) |

---

## 5. Min-Heap vs Max-Heap

```java
// MIN-HEAP (default): Smallest first
PriorityQueue<Integer> minHeap = new PriorityQueue<>();
minHeap.addAll(Arrays.asList(3, 1, 4, 1, 5));
minHeap.poll();  // Returns 1 (smallest)

// MAX-HEAP: Largest first
PriorityQueue<Integer> maxHeap = new PriorityQueue<>(Comparator.reverseOrder());
maxHeap.addAll(Arrays.asList(3, 1, 4, 1, 5));
maxHeap.poll();  // Returns 5 (largest)

// Custom priority (e.g., by string length)
PriorityQueue<String> byLength = new PriorityQueue<>(
    Comparator.comparingInt(String::length)
);
```

---

## 6. ⚠️ Common Gotcha: Iterator Order

```java
PriorityQueue<Integer> pq = new PriorityQueue<>(Arrays.asList(3, 1, 4, 1, 5));

// ❌ WRONG: Iterator does NOT return sorted order!
for (int n : pq) {
    System.out.print(n + " ");  // Might print: 1 1 4 3 5 or other!
}

// ✅ CORRECT: Use poll() repeatedly
while (!pq.isEmpty()) {
    System.out.print(pq.poll() + " ");  // Prints: 1 1 3 4 5
}
```

---

## 7. Common Use Cases

| Algorithm | How PriorityQueue Helps |
|-----------|-------------------------|
| Dijkstra's | Get next nearest unvisited node |
| K-th largest | Max-heap of size K |
| Merge K lists | Track smallest head of all lists |
| Task scheduling | Execute highest priority first |
| Median stream | Two heaps (max for lower, min for upper) |

---

## 8. Summary

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                     PRIORITYQUEUE SUMMARY                                    │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│   WHAT:     Heap-based priority queue                                        │
│   DEFAULT:  Min-heap (smallest element at head)                             │
│                                                                              │
│   FAST:     offer(), poll() - O(log n), peek() - O(1)                       │
│   SLOW:     remove(Object), contains() - O(n)                               │
│                                                                              │
│   ⚠️ GOTCHA: Iterator does NOT return sorted order!                        │
│   ✅ TIP: Use Comparator.reverseOrder() for max-heap                        │
│                                                                              │
│   USE WHEN:                                                                  │
│   • Need to repeatedly access min/max element                               │
│   • Dijkstra's, K-th element, task scheduling                               │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```
