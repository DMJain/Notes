# TreeMap in Java

> **Prerequisites:** [HashMap](./HashMapNotes.md), [TreeSet](../c6_set_interface/TreeSetNotes.md)

---

## What You'll Learn

| Question | Answer |
|----------|--------|
| **WHAT** | Red-Black tree implementation of NavigableMap |
| **WHY** | Sorted keys + range operations |
| **WHEN** | Need sorted key-value pairs, range queries |
| **HOW** | Self-balancing Red-Black tree |

---

## 1. Hierarchy

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                     TREEMAP HIERARCHY                                        │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│   java.util.Map<K,V>                                                         │
│          │                                                                   │
│          ▼                                                                   │
│   java.util.SortedMap<K,V>  ←─── firstKey(), lastKey()                      │
│          │                                                                   │
│          ▼                                                                   │
│   java.util.NavigableMap<K,V>  ←─── floorKey(), ceilingKey(), subMap()     │
│          │                                                                   │
│          ▼                                                                   │
│   ┌─────────────────────────────────────────────────────────────────┐       │
│   │                    java.util.TreeMap<K,V>                        │       │
│   │                                                                  │       │
│   │   • Red-Black tree (self-balancing)                             │       │
│   │   • Keys sorted (natural or Comparator)                         │       │
│   │   • O(log n) operations                                         │       │
│   │   • Does NOT allow null keys                                    │       │
│   └─────────────────────────────────────────────────────────────────┘       │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 2. HashMap vs TreeMap

| Feature | HashMap | TreeMap |
|---------|---------|---------|
| **Order** | No order | Sorted by key |
| **get/put** | O(1) | O(log n) |
| **Null key** | One allowed | ❌ Not allowed |
| **Navigation** | ❌ | ✅ floor/ceiling/subMap |
| **Use case** | Fast lookup | Sorted + range queries |

---

## 3. NavigableMap Methods

```java
TreeMap<Integer, String> map = new TreeMap<>();
map.put(10, "Ten");
map.put(20, "Twenty");
map.put(30, "Thirty");
map.put(40, "Forty");

// Navigation
map.firstKey();       // 10
map.lastKey();        // 40
map.floorKey(25);     // 20 (largest ≤ 25)
map.ceilingKey(25);   // 30 (smallest ≥ 25)
map.lowerKey(30);     // 20 (largest < 30)
map.higherKey(30);    // 40 (smallest > 30)

// Range views
map.headMap(30);      // {10=Ten, 20=Twenty} (keys < 30)
map.tailMap(30);      // {30=Thirty, 40=Forty} (keys ≥ 30)
map.subMap(15, 35);   // {20=Twenty, 30=Thirty}

// Descending
map.descendingMap();  // {40=Forty, 30=Thirty, 20=Twenty, 10=Ten}
```

---

## 4. Summary

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                       TREEMAP SUMMARY                                        │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│   WHAT:      Red-Black tree sorted by keys                                  │
│   ORDER:     Natural ordering or Comparator                                 │
│                                                                              │
│   TIME:      O(log n) for all operations                                    │
│   NULL:      Keys NOT allowed                                               │
│                                                                              │
│   UNIQUE:    NavigableMap methods (floor, ceiling, subMap)                  │
│                                                                              │
│   USE WHEN:                                                                  │
│   • Need sorted keys                                                         │
│   • Need range queries                                                       │
│   • Need floor/ceiling operations                                           │
│                                                                              │
│   AVOID WHEN:                                                                │
│   • Just need fast lookup (use HashMap)                                     │
│   • Need null keys (use HashMap)                                            │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```
