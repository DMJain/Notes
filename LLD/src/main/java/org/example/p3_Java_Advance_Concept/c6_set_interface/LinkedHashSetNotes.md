# LinkedHashSet in Java

> **Prerequisites:** [HashSet](./HashSetNotes.md), [Collection Framework](../c4_collection_framework/CollectionFrameworkNotes.md)

---

## What You'll Learn

| Question | Answer |
|----------|--------|
| **WHAT** | HashSet that maintains insertion order |
| **WHY** | O(1) operations + predictable iteration order |
| **WHEN** | Need uniqueness AND insertion order |
| **HOW** | Hash table + doubly-linked list |

---

## 1. Hierarchy

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                       LINKEDHASHSET HIERARCHY                                │
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
│   java.util.HashSet<E>                                                       │
│          │                                                                   │
│          ▼                                                                   │
│   ┌─────────────────────────────────────────────────────────────────┐       │
│   │                  java.util.LinkedHashSet<E>                      │       │
│   │                                                                  │       │
│   │   • Extends HashSet                                             │       │
│   │   • Uses LinkedHashMap internally                              │       │
│   │   • Maintains insertion order                                   │       │
│   │   • Slightly more memory than HashSet                          │       │
│   └─────────────────────────────────────────────────────────────────┘       │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 2. Internal Structure

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                   LINKEDHASHSET INTERNAL STRUCTURE                           │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│   LinkedHashSet<String> set = new LinkedHashSet<>();                         │
│   set.add("Banana");                                                         │
│   set.add("Apple");                                                          │
│   set.add("Cherry");                                                         │
│                                                                              │
│   HASH TABLE (like HashSet):                                                 │
│   ┌──────┬──────┬──────┬──────┬──────┬──────┐                              │
│   │  [0] │  [1] │  [2] │  [3] │  [4] │  [5] │                              │
│   └──────┴──────┴──────┴──────┴──────┴──────┘                              │
│      │             │      │                                                  │
│      ▼             ▼      ▼                                                  │
│   ┌────────┐  ┌────────┐ ┌────────┐                                        │
│   │"Banana"│  │"Cherry"│ │"Apple" │                                        │
│   └────────┘  └────────┘ └────────┘                                        │
│                                                                              │
│   PLUS: Doubly-linked list maintaining insertion order:                     │
│                                                                              │
│   head ───► "Banana" ◄──► "Apple" ◄──► "Cherry" ◄─── tail                  │
│              (1st)         (2nd)        (3rd)                               │
│                                                                              │
│   Iteration follows the linked list: Banana → Apple → Cherry                │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 3. HashSet vs LinkedHashSet

| Feature | HashSet | LinkedHashSet |
|---------|---------|---------------|
| **Order** | No order guarantee | Insertion order |
| **Time** | O(1) operations | O(1) operations |
| **Memory** | Less overhead | Slightly more (links) |
| **Iteration** | Undefined order | Predictable order |
| **Use case** | Just uniqueness | Uniqueness + order |

```java
Set<String> hashSet = new HashSet<>();
hashSet.add("Banana");
hashSet.add("Apple");
hashSet.add("Cherry");
System.out.println(hashSet);  
// Output: Could be [Apple, Cherry, Banana] or any order!

Set<String> linkedHashSet = new LinkedHashSet<>();
linkedHashSet.add("Banana");
linkedHashSet.add("Apple");
linkedHashSet.add("Cherry");
System.out.println(linkedHashSet);
// Output: ALWAYS [Banana, Apple, Cherry] - insertion order!
```

---

## 4. When to Use LinkedHashSet

### ✅ Good Use Cases

| Scenario | Why LinkedHashSet |
|----------|-------------------|
| Remove duplicates, keep order | First occurrence is kept |
| Cache with insertion order | Know when added |
| Ordered unique collection | Best of both worlds |

### ❌ When NOT to Use

| Scenario | Use Instead | Why |
|----------|-------------|-----|
| Don't care about order | `HashSet` | Less memory |
| Need sorted order | `TreeSet` | LinkedHashSet isn't sorted |
| Memory constrained | `HashSet` | Less overhead |

---

## 5. Summary

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                       LINKEDHASHSET SUMMARY                                  │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│   WHAT:     HashSet + insertion order                                        │
│   ORDER:    Maintains insertion order                                        │
│   BACKING:  LinkedHashMap (hash table + doubly-linked list)                 │
│                                                                              │
│   USE WHEN:                                                                  │
│   • Need O(1) operations AND predictable iteration order                    │
│   • Removing duplicates while preserving first occurrence order             │
│                                                                              │
│   REMEMBER:                                                                  │
│   • Slightly more memory than HashSet                                        │
│   • Still NOT sorted - just insertion ordered                               │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```
