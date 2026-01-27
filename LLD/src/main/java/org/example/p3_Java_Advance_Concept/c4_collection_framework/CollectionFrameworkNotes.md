# Java Collections Framework

> **Prerequisites:** Understanding of [Generics](../c1_generics/GenericsNotes.md), [Lambdas](../c2_lambdas/LambdasNotes.md), and [Streams](../c3_streams/StreamsNotes.md)

---

## What You'll Learn

| Question | Answer |
|----------|--------|
| **WHAT** | A unified architecture for storing and manipulating groups of objects |
| **WHY** | Type-safe, resizable, feature-rich alternatives to raw arrays |
| **WHEN** | Whenever you need dynamic data structures (almost always) |
| **HOW** | Through interfaces (List, Set, Queue, Map) and their implementations |

---

## 1. Why Do Collections Exist?

### The Problem with Arrays

```java
// 😰 Arrays are FIXED size
String[] names = new String[3];
names[0] = "Alice";
names[1] = "Bob";
names[2] = "Charlie";
// names[3] = "David";  // 💥 ArrayIndexOutOfBoundsException!

// Need more space? Create new array and copy everything!
String[] bigger = new String[6];
System.arraycopy(names, 0, bigger, 0, names.length);
```

```
┌───────────────────────────────────────────────────────────────────────┐
│                        Arrays: The Limitations                         │
├───────────────────────────────────────────────────────────────────────┤
│                                                                        │
│   String[] arr = new String[3];                                        │
│   ┌─────────┬─────────┬─────────┐                                     │
│   │ "Alice" │  "Bob"  │"Charlie"│  ← Fixed size! Can't grow!          │
│   └─────────┴─────────┴─────────┘                                     │
│       [0]       [1]       [2]                                          │
│                                                                        │
│   ❌ Fixed size at creation                                           │
│   ❌ No built-in search, sort, filter                                 │
│   ❌ Manual resizing = tedious code                                   │
│   ❌ Can't easily remove elements                                     │
│   ❌ No duplicate prevention                                          │
│                                                                        │
└───────────────────────────────────────────────────────────────────────┘
```

### The Solution: Collections

```java
// 😎 Collections are DYNAMIC
List<String> names = new ArrayList<>();
names.add("Alice");
names.add("Bob");
names.add("Charlie");
names.add("David");    // ✅ Just works! Auto-resizes!

// Built-in operations
names.remove("Bob");
names.contains("Alice");  // true
Collections.sort(names);
```

---

## 2. The Complete Hierarchy

### Collection Interface Hierarchy

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                     JAVA COLLECTIONS HIERARCHY                               │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│   java.lang.Iterable<E>  ←─── Enables for-each loop                         │
│          │                                                                   │
│          ▼                                                                   │
│   java.util.Collection<E>  ←─── Root interface for collections              │
│          │                                                                   │
│          ├──────────────────┬─────────────────┐                             │
│          ▼                  ▼                 ▼                             │
│   ┌─────────────┐    ┌─────────────┐   ┌─────────────┐                      │
│   │    List     │    │    Set      │   │   Queue     │                      │
│   │  (ordered,  │    │  (unique,   │   │   (FIFO,    │                      │
│   │  duplicates)│    │ no dupes)   │   │  priority)  │                      │
│   └─────────────┘    └─────────────┘   └─────────────┘                      │
│          │                  │                 │                             │
│          ▼                  ▼                 ▼                             │
│   ┌───────────┐      ┌───────────┐     ┌───────────┐                        │
│   │ ArrayList │      │  HashSet  │     │ArrayDeque │                        │
│   │LinkedList │      │ TreeSet   │     │PriorityQ  │                        │
│   │  Vector   │      │LinkedHash │     │LinkedList │                        │
│   └───────────┘      └───────────┘     └───────────┘                        │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

### Map Interface Hierarchy (SEPARATE from Collection!)

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                     MAP HIERARCHY (NOT a Collection!)                        │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│   ⚠️ Map does NOT extend Collection!                                        │
│   Maps store KEY-VALUE pairs, not single elements.                          │
│                                                                              │
│   java.util.Map<K,V>                                                         │
│          │                                                                   │
│          ├──────────────────┬─────────────────┐                             │
│          ▼                  ▼                 ▼                             │
│   ┌─────────────┐    ┌─────────────┐   ┌─────────────┐                      │
│   │   HashMap   │    │  TreeMap    │   │  Hashtable  │                      │
│   │ (O(1) ops)  │    │ (sorted)    │   │  (legacy)   │                      │
│   └─────────────┘    └─────────────┘   └─────────────┘                      │
│          │                                                                   │
│          ▼                                                                   │
│   ┌─────────────┐                                                           │
│   │LinkedHashMap│                                                           │
│   │  (ordered)  │                                                           │
│   └─────────────┘                                                           │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

### Complete Detailed Hierarchy

```
┌─────────────────────────────────────────────────────────────────────────────────────┐
│                         COMPLETE COLLECTIONS HIERARCHY                               │
├─────────────────────────────────────────────────────────────────────────────────────┤
│                                                                                      │
│   Iterable<E>                                                                        │
│       │                                                                              │
│       └── Collection<E>                                                              │
│               │                                                                      │
│               ├── List<E> ─────────────────┬── ArrayList                            │
│               │                             ├── LinkedList (also Deque)             │
│               │                             ├── Vector (legacy)                     │
│               │                             │     └── Stack (deprecated)            │
│               │                             └── CopyOnWriteArrayList (concurrent)   │
│               │                                                                      │
│               ├── Set<E> ──────────────────┬── HashSet                              │
│               │       │                     │     └── LinkedHashSet                 │
│               │       │                     │                                        │
│               │       └── SortedSet<E>      └── EnumSet                             │
│               │              │                                                       │
│               │              └── NavigableSet<E>                                    │
│               │                      │                                               │
│               │                      └── TreeSet                                    │
│               │                                                                      │
│               └── Queue<E> ────────────────┬── PriorityQueue                        │
│                       │                     │                                        │
│                       └── Deque<E>          └── ConcurrentLinkedQueue              │
│                              │                                                       │
│                              ├── ArrayDeque                                         │
│                              ├── LinkedList                                         │
│                              └── ConcurrentLinkedDeque                              │
│                                                                                      │
│   Map<K,V> (SEPARATE) ─────────────────────┬── HashMap                              │
│       │                                     │     └── LinkedHashMap                 │
│       │                                     ├── Hashtable (legacy)                  │
│       │                                     ├── WeakHashMap                         │
│       │                                     ├── IdentityHashMap                     │
│       │                                     └── EnumMap                             │
│       │                                                                              │
│       └── SortedMap<K,V>                                                            │
│               │                                                                      │
│               └── NavigableMap<K,V>                                                 │
│                       │                                                              │
│                       └── TreeMap                                                   │
│                                                                                      │
└─────────────────────────────────────────────────────────────────────────────────────┘
```

---

## 3. Choosing the Right Collection

### Decision Flowchart

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                    WHICH COLLECTION SHOULD I USE?                            │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│   Need key-value pairs?                                                      │
│       │                                                                      │
│       ├── YES ──► MAP                                                        │
│       │           ├── Need sorting? ──► TreeMap                             │
│       │           ├── Need insertion order? ──► LinkedHashMap               │
│       │           └── Just fast access? ──► HashMap                         │
│       │                                                                      │
│       └── NO ──► Single elements                                            │
│                   │                                                          │
│                   ├── Need duplicates?                                       │
│                   │       │                                                  │
│                   │       ├── YES ──► LIST                                  │
│                   │       │           ├── Random access? ──► ArrayList      │
│                   │       │           └── Frequent insert/delete? ──► LinkedList │
│                   │       │                                                  │
│                   │       └── NO ──► SET                                    │
│                   │                   ├── Need sorting? ──► TreeSet         │
│                   │                   ├── Need insertion order? ──► LinkedHashSet │
│                   │                   └── Just uniqueness? ──► HashSet      │
│                   │                                                          │
│                   └── Need FIFO/LIFO/Priority?                              │
│                           │                                                  │
│                           └── QUEUE                                          │
│                               ├── Priority ordering? ──► PriorityQueue      │
│                               ├── Stack (LIFO)? ──► ArrayDeque              │
│                               └── Queue (FIFO)? ──► ArrayDeque/LinkedList   │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

### Quick Reference Table

| Need | Interface | Best Implementation |
|------|-----------|---------------------|
| Ordered list with duplicates | `List` | `ArrayList` |
| Fast insert/remove at ends | `List` | `LinkedList` |
| Unique elements, no order | `Set` | `HashSet` |
| Unique elements, insertion order | `Set` | `LinkedHashSet` |
| Unique elements, sorted | `Set` | `TreeSet` |
| FIFO queue | `Queue` | `ArrayDeque` |
| Priority queue | `Queue` | `PriorityQueue` |
| Stack (LIFO) | `Deque` | `ArrayDeque` |
| Key-value, fast lookup | `Map` | `HashMap` |
| Key-value, sorted keys | `Map` | `TreeMap` |
| Key-value, insertion order | `Map` | `LinkedHashMap` |

---

## 4. Array vs Collection Comparison

| Feature | Array | Collection |
|---------|-------|------------|
| **Size** | Fixed at creation | Dynamic, grows automatically |
| **Type Safety** | Can hold primitives | Generics (wrapper types only) |
| **Performance** | Slightly faster for primitives | Slight overhead (autoboxing) |
| **Search** | Manual loop | `contains()`, `indexOf()` |
| **Sort** | `Arrays.sort()` | `Collections.sort()` or streams |
| **Remove** | Manual shift | `remove()` method |
| **Duplicate Check** | Manual | Sets prevent automatically |
| **Null Handling** | Allowed | Depends on implementation |

### Before vs After Example

```java
// ❌ BEFORE: Using arrays (tedious)
String[] colors = new String[100];
int size = 0;

void addColor(String color) {
    if (size >= colors.length) {
        // Manual resize
        String[] bigger = new String[colors.length * 2];
        System.arraycopy(colors, 0, bigger, 0, size);
        colors = bigger;
    }
    colors[size++] = color;
}

boolean containsColor(String color) {
    for (int i = 0; i < size; i++) {
        if (colors[i].equals(color)) return true;
    }
    return false;
}

// ✅ AFTER: Using ArrayList (simple)
List<String> colors = new ArrayList<>();

colors.add("Red");         // Auto-resizes!
colors.contains("Red");    // Built-in!
colors.remove("Red");      // Easy!
```

---

## 5. When NOT to Use Collections

| Scenario | Use Instead | Why |
|----------|-------------|-----|
| Primitive arrays for performance | `int[]`, `double[]` | No autoboxing overhead |
| Known fixed size, never changes | Array | Slightly less memory |
| Need to pass to legacy API | Array | API compatibility |
| Extremely memory-constrained | Array | Collections have overhead |

---

## 6. Common Gotchas

### ❌ Gotcha 1: Modifying while iterating

```java
List<String> list = new ArrayList<>(Arrays.asList("a", "b", "c"));
for (String s : list) {
    if (s.equals("b")) {
        list.remove(s);  // 💥 ConcurrentModificationException!
    }
}

// ✅ Use Iterator.remove() instead
Iterator<String> it = list.iterator();
while (it.hasNext()) {
    if (it.next().equals("b")) {
        it.remove();  // Safe!
    }
}
```

### ❌ Gotcha 2: Forgetting hashCode/equals for custom objects

```java
class Person {
    String name;
    // No hashCode/equals!
}

Set<Person> people = new HashSet<>();
people.add(new Person("Alice"));
people.add(new Person("Alice"));  // Adds duplicate! 💥
// Set now has 2 "Alice" objects because they're not .equals()
```

### ❌ Gotcha 3: Using wrong collection for the job

```java
// Using ArrayList when you need fast lookup
List<String> users = new ArrayList<>();
// ... add 1 million users
users.contains("john");  // O(n) - scans entire list!

// ✅ Use HashSet for O(1) lookup
Set<String> users = new HashSet<>();
users.contains("john");  // O(1) - instant!
```

---

## 7. Summary

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                         COLLECTIONS SUMMARY                                  │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│   INTERFACES:                                                                │
│   • List → Ordered, allows duplicates (ArrayList, LinkedList)               │
│   • Set  → Unique elements (HashSet, TreeSet, LinkedHashSet)                │
│   • Queue → FIFO/Priority (ArrayDeque, PriorityQueue)                       │
│   • Map  → Key-value pairs (HashMap, TreeMap, LinkedHashMap)                │
│                                                                              │
│   KEY POINTS:                                                                │
│   • Collection extends Iterable (for-each works)                            │
│   • Map is SEPARATE (not a Collection)                                      │
│   • Choose based on: ordering, duplicates, performance needs                │
│   • Override hashCode/equals for custom objects in Sets/Maps                │
│                                                                              │
│   NEXT: Deep dive into each interface and implementation                    │
│   • c5_list_interface: ArrayList, LinkedList, Vector, Stack                │
│   • c6_set_interface: HashSet, LinkedHashSet, TreeSet                       │
│   • c7_queue_interface: Queue, Deque, PriorityQueue                        │
│   • c8_map_interface: HashMap, LinkedHashMap, TreeMap                       │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```
