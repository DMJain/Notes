# HashSet in Java

> **Prerequisites:** [Collection Framework](../c4_collection_framework/CollectionFrameworkNotes.md), [List Interface](../c5_list_interface/ArrayListNotes.md)

---

## What You'll Learn

| Question | Answer |
|----------|--------|
| **WHAT** | Hash table implementation of Set, no duplicates, no ordering |
| **WHY** | O(1) add/remove/contains - fastest for uniqueness checks |
| **WHEN** | Need unique elements, don't care about order |
| **HOW** | Backed by HashMap (elements stored as keys) |

---

## 1. Hierarchy

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                           HASHSET HIERARCHY                                  │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│   java.lang.Iterable<E>                                                      │
│          │                                                                   │
│          ▼                                                                   │
│   java.util.Collection<E>                                                    │
│          │                                                                   │
│          ▼                                                                   │
│   java.util.Set<E>  ←─── No duplicates, no guaranteed order                 │
│          │                                                                   │
│          ▼                                                                   │
│   java.util.AbstractSet<E>                                                   │
│          │                                                                   │
│          ▼                                                                   │
│   ┌─────────────────────────────────────────────────────────────────┐       │
│   │                     java.util.HashSet<E>                         │       │
│   │                                                                  │       │
│   │   • Backed by HashMap internally                                │       │
│   │   • No ordering guarantee                                       │       │
│   │   • Allows one null element                                     │       │
│   │   • Not synchronized                                            │       │
│   └─────────────────────────────────────────────────────────────────┘       │
│          │                                                                   │
│          ▼                                                                   │
│   ┌─────────────────────────────────────────────────────────────────┐       │
│   │                  java.util.LinkedHashSet<E>                      │       │
│   │         (maintains insertion order)                              │       │
│   └─────────────────────────────────────────────────────────────────┘       │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 2. Internal Structure

### HashSet is Backed by HashMap

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                     HASHSET INTERNAL STRUCTURE                               │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│   HashSet<String> set = new HashSet<>();                                     │
│   set.add("Apple");                                                          │
│   set.add("Banana");                                                         │
│   set.add("Cherry");                                                         │
│                                                                              │
│   INTERNALLY: Uses HashMap where elements are KEYS, value is DUMMY          │
│   ┌─────────────────────────────────────────────────────────────────┐       │
│   │                     HashMap<E, Object>                          │       │
│   ├─────────────────────────────────────────────────────────────────┤       │
│   │                                                                  │       │
│   │   Key: "Apple"   → Value: PRESENT (dummy Object)                │       │
│   │   Key: "Banana"  → Value: PRESENT (dummy Object)                │       │
│   │   Key: "Cherry"  → Value: PRESENT (dummy Object)                │       │
│   │                                                                  │       │
│   └─────────────────────────────────────────────────────────────────┘       │
│                                                                              │
│   Source code:                                                               │
│   private static final Object PRESENT = new Object();                        │
│   private transient HashMap<E, Object> map;                                  │
│                                                                              │
│   public boolean add(E e) {                                                  │
│       return map.put(e, PRESENT) == null;  // true if new, false if exists  │
│   }                                                                          │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

### Hash Bucket Structure

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                     HASH BUCKET DIAGRAM                                      │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│   When you call set.add("Apple"):                                           │
│                                                                              │
│   1. Calculate hash: "Apple".hashCode() = 63476538                          │
│   2. Find bucket: hash % bucketCount = 63476538 % 16 = 10                   │
│   3. Store in bucket 10                                                      │
│                                                                              │
│   Bucket Array (default 16 buckets):                                        │
│   ┌──────┬──────┬──────┬──────┬──────┬──────┬──────┬──────┐                │
│   │  [0] │  [1] │  [2] │ ...  │ [10] │ [11] │ ...  │ [15] │                │
│   └──────┴──────┴──────┴──────┴──────┴──────┴──────┴──────┘                │
│                                  │                                           │
│                                  ▼                                           │
│                            ┌──────────┐                                     │
│                            │ "Apple"  │                                     │
│                            └──────────┘                                     │
│                                  │  (if collision)                          │
│                                  ▼                                           │
│                            ┌──────────┐                                     │
│                            │ "Apricot"│                                     │
│                            └──────────┘                                     │
│                                                                              │
│   Collision handling:                                                        │
│   • < 8 elements: Linked list                                               │
│   • ≥ 8 elements: Tree (Red-Black) for O(log n) lookup                     │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 3. Time Complexity

| Operation | Average | Worst (all collisions) |
|-----------|---------|------------------------|
| `add(element)` | **O(1)** | O(n) |
| `remove(element)` | **O(1)** | O(n) |
| `contains(element)` | **O(1)** | O(n) |
| `size()` | **O(1)** | O(1) |
| `isEmpty()` | **O(1)** | O(1) |
| `iterator()` | O(n) | O(n) |

---

## 4. hashCode/equals Contract

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                     HASHCODE/EQUALS CONTRACT                                 │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│   For HashSet to work correctly:                                            │
│                                                                              │
│   RULE 1: If a.equals(b) → a.hashCode() == b.hashCode()                     │
│           Equal objects MUST have the same hash code                        │
│                                                                              │
│   RULE 2: If a.hashCode() != b.hashCode() → !a.equals(b)                    │
│           Different hash codes means objects are NOT equal                  │
│                                                                              │
│   RULE 3: a.hashCode() == b.hashCode() does NOT mean a.equals(b)            │
│           Same hash code doesn't guarantee equality (collisions happen)     │
│                                                                              │
│   WHY THIS MATTERS:                                                          │
│   ──────────────────                                                        │
│   set.add(new Person("Alice"));                                              │
│   set.add(new Person("Alice"));  // Should this be duplicate?               │
│                                                                              │
│   If Person doesn't override equals/hashCode:                               │
│   • Both objects have different hashCode (based on memory address)         │
│   • HashSet thinks they're different → BOTH get added! 💥                   │
│                                                                              │
│   If Person overrides equals/hashCode:                                       │
│   • Both objects have same hashCode (based on name)                         │
│   • HashSet finds first, calls equals(), returns true                       │
│   • Second add() is rejected as duplicate ✅                                │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 5. Before vs After

```java
// ❌ BEFORE: Manual duplicate checking with List
List<String> list = new ArrayList<>();
if (!list.contains("Apple")) {  // O(n) check!
    list.add("Apple");
}
if (!list.contains("Apple")) {  // O(n) check!
    list.add("Apple");  // Won't add, but expensive check
}

// ✅ AFTER: HashSet handles it automatically
Set<String> set = new HashSet<>();
set.add("Apple");  // Added, returns true
set.add("Apple");  // Ignored, returns false - O(1)!
```

---

## 6. When to Use HashSet

### ✅ Good Use Cases

| Scenario | Why HashSet |
|----------|-------------|
| Remove duplicates from list | `new HashSet<>(list)` |
| Fast contains() check | O(1) vs O(n) for List |
| Membership testing | Is element in set? |
| Unique collection needed | Automatic deduplication |

### ❌ When NOT to Use

| Scenario | Use Instead | Why |
|----------|-------------|-----|
| Need insertion order | `LinkedHashSet` | HashSet has no order |
| Need sorted elements | `TreeSet` | HashSet doesn't sort |
| Need duplicates | `ArrayList` | Set rejects duplicates |
| Need index access | `ArrayList` | Sets have no indices |

---

## 7. Common Gotchas

### ❌ Gotcha 1: Mutable objects as elements

```java
Set<List<String>> set = new HashSet<>();
List<String> list = new ArrayList<>();
list.add("Hello");
set.add(list);

// Mutate the list after adding
list.add("World");

// Now contains() fails!
set.contains(list);  // false! hashCode changed! 💥
```

### ❌ Gotcha 2: Custom objects without hashCode/equals

```java
class Person { String name; }

Set<Person> set = new HashSet<>();
set.add(new Person("Alice"));
set.add(new Person("Alice"));  // BOTH get added! 💥
System.out.println(set.size());  // 2, not 1!
```

---

## 8. Summary

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                           HASHSET SUMMARY                                    │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│   WHAT:     Hash table Set, backed by HashMap                               │
│   ORDER:    No ordering guarantee                                           │
│   NULLS:    One null allowed                                                │
│                                                                              │
│   FAST:     add(), remove(), contains() - all O(1) average                  │
│   KEY:      Objects must override hashCode() and equals()                   │
│                                                                              │
│   USE WHEN:                                                                  │
│   • Need fast uniqueness checks                                              │
│   • Don't care about element order                                           │
│   • Need fast contains() operation                                           │
│                                                                              │
│   AVOID WHEN:                                                                │
│   • Need ordered elements (use LinkedHashSet or TreeSet)                    │
│   • Objects don't have proper hashCode/equals                               │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```
