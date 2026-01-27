# TreeSet in Java

> **Prerequisites:** [HashSet](./HashSetNotes.md), [Collection Framework](../c4_collection_framework/CollectionFrameworkNotes.md)

---

## What You'll Learn

| Question | Answer |
|----------|--------|
| **WHAT** | Red-Black tree implementation of NavigableSet |
| **WHY** | Sorted elements + range operations |
| **WHEN** | Need sorted unique elements, floor/ceiling/range queries |
| **HOW** | Self-balancing Red-Black tree |

---

## 1. Hierarchy

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                           TREESET HIERARCHY                                  │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│   java.lang.Iterable<E>                                                      │
│          │                                                                   │
│          ▼                                                                   │
│   java.util.Collection<E>                                                    │
│          │                                                                   │
│          ▼                                                                   │
│   java.util.Set<E>                                                           │
│          │                                                                   │
│          ▼                                                                   │
│   java.util.SortedSet<E>  ←─── Sorted elements                              │
│          │                                                                   │
│          ▼                                                                   │
│   java.util.NavigableSet<E>  ←─── floor(), ceiling(), higher(), lower()     │
│          │                                                                   │
│          ▼                                                                   │
│   ┌─────────────────────────────────────────────────────────────────┐       │
│   │                     java.util.TreeSet<E>                         │       │
│   │                                                                  │       │
│   │   • Backed by TreeMap (Red-Black tree)                          │       │
│   │   • Elements sorted (natural or Comparator)                     │       │
│   │   • O(log n) operations                                         │       │
│   │   • Does NOT allow null                                         │       │
│   └─────────────────────────────────────────────────────────────────┘       │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 2. Internal Structure

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                   TREESET: RED-BLACK TREE                                    │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│   TreeSet<Integer> set = new TreeSet<>();                                    │
│   set.addAll(Arrays.asList(50, 30, 70, 20, 40, 60, 80));                    │
│                                                                              │
│   RED-BLACK TREE STRUCTURE:                                                  │
│                                                                              │
│                            ┌─────┐                                          │
│                            │ 50  │ (root)                                   │
│                            └─────┘                                          │
│                        ┌─────┴─────┐                                        │
│                        ▼           ▼                                        │
│                   ┌─────┐     ┌─────┐                                       │
│                   │ 30  │     │ 70  │                                       │
│                   └─────┘     └─────┘                                       │
│                  ┌──┴──┐     ┌──┴──┐                                        │
│                  ▼     ▼     ▼     ▼                                        │
│              ┌─────┐┌─────┐┌─────┐┌─────┐                                   │
│              │ 20  ││ 40  ││ 60  ││ 80  │                                   │
│              └─────┘└─────┘└─────┘└─────┘                                   │
│                                                                              │
│   IN-ORDER TRAVERSAL gives sorted: [20, 30, 40, 50, 60, 70, 80]            │
│                                                                              │
│   Tree Properties:                                                           │
│   • Self-balancing (height ≈ log n)                                         │
│   • Left child < Parent < Right child                                       │
│   • All operations O(log n)                                                  │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 3. Time Complexity

| Operation | HashSet | TreeSet |
|-----------|---------|---------|
| `add(element)` | O(1) | **O(log n)** |
| `remove(element)` | O(1) | **O(log n)** |
| `contains(element)` | O(1) | **O(log n)** |
| `first()` | N/A | **O(log n)** |
| `last()` | N/A | **O(log n)** |
| `floor(e)` | N/A | **O(log n)** |
| `ceiling(e)` | N/A | **O(log n)** |

---

## 4. NavigableSet Methods

```java
TreeSet<Integer> set = new TreeSet<>();
set.addAll(Arrays.asList(10, 20, 30, 40, 50));

// Navigation methods
set.first();        // 10 - smallest
set.last();         // 50 - largest
set.floor(25);      // 20 - largest ≤ 25
set.ceiling(25);    // 30 - smallest ≥ 25
set.lower(30);      // 20 - largest < 30
set.higher(30);     // 40 - smallest > 30

// Range views
set.headSet(30);           // [10, 20] - elements < 30
set.tailSet(30);           // [30, 40, 50] - elements ≥ 30
set.subSet(20, 40);        // [20, 30] - between 20 and 40

// Descending
set.descendingSet();       // [50, 40, 30, 20, 10]
set.pollFirst();           // Removes and returns 10
set.pollLast();            // Removes and returns 50
```

---

## 5. Comparable vs Comparator

```java
// Option 1: Elements implement Comparable (natural ordering)
TreeSet<String> names = new TreeSet<>();
names.add("Charlie");
names.add("Alice");
names.add("Bob");
// Result: [Alice, Bob, Charlie] - alphabetical

// Option 2: Provide Comparator (custom ordering)
TreeSet<String> byLength = new TreeSet<>(Comparator.comparingInt(String::length));
byLength.add("Charlie");
byLength.add("Alice");
byLength.add("Bob");
// Result: [Bob, Alice, Charlie] - by length

// Reverse order
TreeSet<Integer> descending = new TreeSet<>(Comparator.reverseOrder());
descending.addAll(Arrays.asList(1, 3, 2));
// Result: [3, 2, 1]
```

---

## 6. HashSet vs TreeSet

| Feature | HashSet | TreeSet |
|---------|---------|---------|
| **Order** | Unordered | Sorted |
| **Performance** | O(1) | O(log n) |
| **Null** | One null | ❌ No null |
| **Navigation** | ❌ | ✅ floor/ceiling |
| **Range ops** | ❌ | ✅ subSet/headSet |
| **Use case** | Fast lookup | Sorted + queries |

---

## 7. When to Use TreeSet

### ✅ Good Use Cases

| Scenario | Why TreeSet |
|----------|-------------|
| Need sorted elements | Auto-sorted |
| Range queries | subSet, headSet, tailSet |
| Find nearest element | floor(), ceiling() |
| Ordered iteration | Always sorted |

### ❌ When NOT to Use

| Scenario | Use Instead | Why |
|----------|-------------|-----|
| Just need uniqueness | `HashSet` | O(1) faster |
| Need null element | `HashSet` | TreeSet doesn't allow null |
| No natural ordering | `HashSet` + sort | If Comparable impossible |

---

## 8. Summary

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                           TREESET SUMMARY                                    │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│   WHAT:     Red-Black tree Set, sorted elements                             │
│   ORDER:    Natural ordering or Comparator                                  │
│   BACKING:  TreeMap (Red-Black tree)                                        │
│                                                                              │
│   PERFORMANCE: O(log n) for all operations                                  │
│   NULL:     NOT allowed                                                      │
│                                                                              │
│   UNIQUE:   NavigableSet methods (floor, ceiling, higher, lower)            │
│             Range views (subSet, headSet, tailSet)                          │
│                                                                              │
│   USE WHEN:                                                                  │
│   • Need sorted unique elements                                              │
│   • Need range queries or nearest-element lookups                           │
│                                                                              │
│   AVOID WHEN:                                                                │
│   • Just need fast uniqueness test (use HashSet)                            │
│   • Need null elements (use HashSet)                                        │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```
