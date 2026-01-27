# HashMap in Java

> **Prerequisites:** [HashSet](../c6_set_interface/HashSetNotes.md), [Collection Framework](../c4_collection_framework/CollectionFrameworkNotes.md)

---

## What You'll Learn

| Question | Answer |
|----------|--------|
| **WHAT** | Hash table implementation of Map interface |
| **WHY** | O(1) get/put operations - fastest key-value lookup |
| **WHEN** | Need key-value pairs, don't care about order |
| **HOW** | Array of buckets + linked list/tree for collisions |

---

## 1. Hierarchy

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                     MAP HIERARCHY (NOT part of Collection!)                  │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│   ⚠️ Map does NOT extend Collection!                                        │
│   Maps store KEY-VALUE pairs, not single elements.                          │
│                                                                              │
│   java.util.Map<K,V>  ←─── Root interface for maps                          │
│          │                                                                   │
│          ▼                                                                   │
│   java.util.AbstractMap<K,V>                                                 │
│          │                                                                   │
│          ▼                                                                   │
│   ┌─────────────────────────────────────────────────────────────────┐       │
│   │                     java.util.HashMap<K,V>                       │       │
│   │                                                                  │       │
│   │   • Hash table with array of buckets                            │       │
│   │   • O(1) get/put average case                                   │       │
│   │   • Allows one null key, multiple null values                   │       │
│   │   • Not synchronized                                            │       │
│   └─────────────────────────────────────────────────────────────────┘       │
│          │                                                                   │
│          ▼                                                                   │
│   ┌─────────────────────────────────────────────────────────────────┐       │
│   │                  java.util.LinkedHashMap<K,V>                    │       │
│   │              (maintains insertion order)                         │       │
│   └─────────────────────────────────────────────────────────────────┘       │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 2. Internal Structure

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                     HASHMAP BUCKET ARRAY                                     │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│   HashMap<String, Integer> map = new HashMap<>();                            │
│   map.put("Alice", 25);                                                      │
│   map.put("Bob", 30);                                                        │
│   map.put("Charlie", 35);                                                    │
│                                                                              │
│   Process:                                                                   │
│   1. hash("Alice") = 63476538                                               │
│   2. bucket = hash % 16 = 10                                                │
│   3. Store Entry(key="Alice", value=25) in bucket 10                        │
│                                                                              │
│   Bucket Array (default 16 buckets):                                        │
│   ┌───────┬───────┬───────┬───────┬───────┬───────┬───────┬───────┐        │
│   │  [0]  │  [1]  │  [2]  │ ...   │ [10]  │ [11]  │ ...   │ [15]  │        │
│   └───────┴───────┴───────┴───────┴───────┴───────┴───────┴───────┘        │
│                    │               │       │                                 │
│                    ▼               ▼       ▼                                 │
│                 ┌───────┐      ┌───────┐ ┌───────┐                          │
│                 │"Bob"  │      │"Alice"│ │"Carol"│                          │
│                 │  30   │      │  25   │ │  28   │                          │
│                 └───────┘      └───────┘ └───────┘                          │
│                                    │                                         │
│                                    ▼  (collision - linked list)             │
│                              ┌───────┐                                      │
│                              │"Amy"  │  ← Same bucket as Alice              │
│                              │  22   │                                      │
│                              └───────┘                                      │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 3. Collision Handling (Treeification)

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                     COLLISION HANDLING                                       │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│   Fewer than 8 collisions → Linked list (O(n) lookup)                       │
│   ──────────────────────────────────────────────────                        │
│   Bucket [10]:                                                               │
│   ┌───────┐  ┌───────┐  ┌───────┐  ┌───────┐                               │
│   │Entry 1│─►│Entry 2│─►│Entry 3│─►│Entry 4│                               │
│   └───────┘  └───────┘  └───────┘  └───────┘                               │
│                                                                              │
│   8 or more collisions → Red-Black Tree (O(log n) lookup)                   │
│   ──────────────────────────────────────────────────────                    │
│   Bucket [10]:                                                               │
│                    ┌───────┐                                                │
│                    │Entry 4│                                                │
│                    └───────┘                                                │
│                   ┌───┴───┐                                                 │
│               ┌───────┐ ┌───────┐                                           │
│               │Entry 2│ │Entry 6│                                           │
│               └───────┘ └───────┘                                           │
│              ┌─┴─┐     ┌─┴─┐                                                │
│            ┌───┐┌───┐┌───┐┌───┐                                            │
│            │E1 ││E3 ││E5 ││E7 │                                            │
│            └───┘└───┘└───┘└───┘                                            │
│                                                                              │
│   This prevents worst-case O(n) when many keys hash to same bucket          │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 4. Load Factor and Rehashing

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                     LOAD FACTOR & REHASHING                                  │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│   LOAD FACTOR = size / capacity                                             │
│   Default: 0.75 (rehash when 75% full)                                      │
│                                                                              │
│   Example: capacity=16, threshold = 16 * 0.75 = 12                          │
│   When 13th element is added → REHASH:                                       │
│   1. Create new array with capacity = 16 * 2 = 32                           │
│   2. Recalculate bucket for EVERY entry (new hash % 32)                     │
│   3. Move all entries to new array                                          │
│                                                                              │
│   BEFORE (capacity=16):                                                      │
│   ┌───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┐        │
│   │ A │ B │ C │ D │ E │ F │ G │ H │ I │ J │ K │ L │   │   │   │   │        │
│   └───┴───┴───┴───┴───┴───┴───┴───┴───┴───┴───┴───┴───┴───┴───┴───┘        │
│   12/16 = 75% → REHASH!                                                      │
│                                                                              │
│   AFTER (capacity=32):                                                       │
│   ┌───┬───┬───┬───┬───┬───┬...┬───┬───┬───┬───┬───┬───┬───┬───┬───┐        │
│   │ A │   │ B │   │ C │ D │   │ E │   │ F │ G │   │ H │ I │ J │...│        │
│   └───┴───┴───┴───┴───┴───┴...┴───┴───┴───┴───┴───┴───┴───┴───┴───┘        │
│   Entries redistributed across 32 buckets                                   │
│                                                                              │
│   ⚠️ Rehashing is O(n) - set initial capacity if you know size!            │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 5. Time Complexity

| Operation | Average | Worst (all collisions) |
|-----------|---------|------------------------|
| `put(key, value)` | **O(1)** | O(n) |
| `get(key)` | **O(1)** | O(n) |
| `remove(key)` | **O(1)** | O(n) |
| `containsKey(key)` | **O(1)** | O(n) |
| `containsValue(value)` | O(n) | O(n) |

---

## 6. When to Use HashMap

### ✅ Good Use Cases

| Scenario | Why HashMap |
|----------|-------------|
| Fast key lookup | O(1) get |
| Counting occurrences | `map.merge(key, 1, Integer::sum)` |
| Caching | Quick retrieval |
| Two-sum pattern | Store complement |

### ❌ When NOT to Use

| Scenario | Use Instead | Why |
|----------|-------------|-----|
| Need ordering | `LinkedHashMap` | HashMap has no order |
| Need sorted keys | `TreeMap` | HashMap doesn't sort |
| Thread-safe needed | `ConcurrentHashMap` | HashMap not thread-safe |

---

## 7. Summary

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                       HASHMAP SUMMARY                                        │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│   WHAT:      Hash table key-value store                                     │
│   BACKING:   Bucket array + linked list/tree                                │
│   NULL:      One null key allowed, multiple null values                     │
│                                                                              │
│   FAST:      get(), put(), remove(), containsKey() - O(1)                   │
│   SLOW:      containsValue() - O(n)                                         │
│                                                                              │
│   REMEMBER:                                                                  │
│   • Keys must have proper hashCode/equals                                   │
│   • Not synchronized (use ConcurrentHashMap)                                │
│   • Set initial capacity if size is known                                   │
│   • Collisions turn to tree at 8 elements (Java 8+)                        │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```
