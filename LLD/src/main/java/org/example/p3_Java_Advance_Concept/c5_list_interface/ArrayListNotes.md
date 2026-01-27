# ArrayList in Java

> **Prerequisites:** Understanding of [Collection Framework](../c4_collection_framework/CollectionFrameworkNotes.md), [Generics](../c1_generics/GenericsNotes.md)

---

## What You'll Learn

| Question | Answer |
|----------|--------|
| **WHAT** | A resizable array implementation of the List interface |
| **WHY** | Fast random access (O(1)), dynamic sizing, most commonly used List |
| **WHEN** | Need ordered collection with fast index-based access |
| **HOW** | Backed by an internal array that grows automatically |

---

## 1. Hierarchy

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                         ARRAYLIST HIERARCHY                                  │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│   java.lang.Iterable<E>                                                      │
│          │                                                                   │
│          ▼                                                                   │
│   java.util.Collection<E>                                                    │
│          │                                                                   │
│          ▼                                                                   │
│   java.util.List<E>  ←─── Ordered, allows duplicates, index-based access    │
│          │                                                                   │
│          ▼                                                                   │
│   java.util.AbstractList<E>  ←─── Skeletal implementation                   │
│          │                                                                   │
│          ▼                                                                   │
│   ┌─────────────────────────────────────────────────────────────────┐       │
│   │                     java.util.ArrayList<E>                       │       │
│   │                                                                  │       │
│   │   Also implements:                                               │       │
│   │   • RandomAccess ←── Marker interface for fast random access    │       │
│   │   • Cloneable                                                   │       │
│   │   • Serializable                                                │       │
│   └─────────────────────────────────────────────────────────────────┘       │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 2. Internal Structure

### How ArrayList Works Internally

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                     ARRAYLIST INTERNAL STRUCTURE                             │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│   ArrayList<String> list = new ArrayList<>();                                │
│                                                                              │
│   INITIAL STATE (capacity = 10, size = 0):                                   │
│   ┌─────┬─────┬─────┬─────┬─────┬─────┬─────┬─────┬─────┬─────┐            │
│   │null │null │null │null │null │null │null │null │null │null │            │
│   └─────┴─────┴─────┴─────┴─────┴─────┴─────┴─────┴─────┴─────┘            │
│     [0]   [1]   [2]   [3]   [4]   [5]   [6]   [7]   [8]   [9]               │
│                                                                              │
│   Internal array: Object[] elementData                                       │
│   size = 0 (actual elements)                                                 │
│   capacity = 10 (array length)                                               │
│                                                                              │
│   AFTER list.add("A"), list.add("B"), list.add("C"):                        │
│   ┌─────┬─────┬─────┬─────┬─────┬─────┬─────┬─────┬─────┬─────┐            │
│   │ "A" │ "B" │ "C" │null │null │null │null │null │null │null │            │
│   └─────┴─────┴─────┴─────┴─────┴─────┴─────┴─────┴─────┴─────┘            │
│     [0]   [1]   [2]   [3]   [4]   [5]   [6]   [7]   [8]   [9]               │
│                                                                              │
│   size = 3                                                                   │
│   capacity = 10 (unchanged)                                                  │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

### Resize Operation (Growing)

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                     ARRAYLIST RESIZE OPERATION                               │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│   When array is FULL and you add another element:                           │
│                                                                              │
│   STEP 1: Current array is full (capacity = 10, size = 10)                  │
│   ┌───┬───┬───┬───┬───┬───┬───┬───┬───┬───┐                                │
│   │ A │ B │ C │ D │ E │ F │ G │ H │ I │ J │  ← FULL!                       │
│   └───┴───┴───┴───┴───┴───┴───┴───┴───┴───┘                                │
│                                                                              │
│   STEP 2: Create NEW array with capacity = oldCapacity + (oldCapacity >> 1) │
│           New capacity = 10 + 5 = 15 (grows by ~50%)                        │
│   ┌───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┐            │
│   │   │   │   │   │   │   │   │   │   │   │   │   │   │   │   │            │
│   └───┴───┴───┴───┴───┴───┴───┴───┴───┴───┴───┴───┴───┴───┴───┘            │
│                                                                              │
│   STEP 3: Copy all elements to new array (System.arraycopy)                 │
│   ┌───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┐            │
│   │ A │ B │ C │ D │ E │ F │ G │ H │ I │ J │   │   │   │   │   │            │
│   └───┴───┴───┴───┴───┴───┴───┴───┴───┴───┴───┴───┴───┴───┴───┘            │
│                                                                              │
│   STEP 4: Add new element                                                    │
│   ┌───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┐            │
│   │ A │ B │ C │ D │ E │ F │ G │ H │ I │ J │ K │   │   │   │   │            │
│   └───┴───┴───┴───┴───┴───┴───┴───┴───┴───┴───┴───┴───┴───┴───┘            │
│                                                                              │
│   Growth Pattern: 10 → 15 → 22 → 33 → 49 → ...                              │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

### Insert at Middle Operation

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                     INSERT AT MIDDLE: O(n)                                   │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│   list.add(2, "X");  // Insert "X" at index 2                               │
│                                                                              │
│   BEFORE:                                                                    │
│   ┌───┬───┬───┬───┬───┬───────────┐                                         │
│   │ A │ B │ C │ D │ E │  (empty)  │                                         │
│   └───┴───┴───┴───┴───┴───────────┘                                         │
│     0   1   2   3   4                                                        │
│             ↑                                                                │
│          Insert here                                                         │
│                                                                              │
│   STEP 1: Shift elements right (expensive!)                                 │
│   ┌───┬───┬───┬───┬───┬───┬───────┐                                         │
│   │ A │ B │   │ C │ D │ E │(empty)│                                         │
│   └───┴───┴───┴───┴───┴───┴───────┘                                         │
│             ↑                                                                │
│          Gap created                                                         │
│                                                                              │
│   STEP 2: Insert new element                                                 │
│   ┌───┬───┬───┬───┬───┬───┬───────┐                                         │
│   │ A │ B │ X │ C │ D │ E │(empty)│                                         │
│   └───┴───┴───┴───┴───┴───┴───────┘                                         │
│             ↑                                                                │
│          "X" inserted                                                        │
│                                                                              │
│   ⚠️ Shifting = O(n) - gets slower with more elements after index           │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 3. Time Complexity

| Operation | Time Complexity | Notes |
|-----------|-----------------|-------|
| `get(index)` | **O(1)** | Direct array access |
| `set(index, element)` | **O(1)** | Direct array access |
| `add(element)` | **O(1)** amortized | O(n) when resize needed |
| `add(index, element)` | **O(n)** | Shift elements right |
| `remove(index)` | **O(n)** | Shift elements left |
| `remove(object)` | **O(n)** | Search + shift |
| `contains(object)` | **O(n)** | Linear search |
| `indexOf(object)` | **O(n)** | Linear search |
| `size()` | **O(1)** | Stored as field |
| `isEmpty()` | **O(1)** | Check size == 0 |

---

## 4. Initial Capacity and Load Factor

```java
// Default capacity = 10
List<String> list1 = new ArrayList<>();

// Custom initial capacity (use when you know approximate size)
List<String> list2 = new ArrayList<>(1000);  // Avoids multiple resizes!

// From another collection
List<String> list3 = new ArrayList<>(existingList);
```

### Why Set Initial Capacity?

```
Scenario: Adding 1,000,000 elements

❌ Without initial capacity:
   10 → 15 → 22 → 33 → ... → ~30 resize operations!
   Each resize = copy ALL elements to new array
   
✅ With initial capacity of 1,000,000:
   0 resize operations
   Much faster!
```

---

## 5. Before vs After

```java
// ❌ BEFORE: Using arrays (tedious)
String[] arr = new String[10];
int size = 0;

void add(String s) {
    if (size == arr.length) {
        String[] newArr = new String[arr.length * 2];
        System.arraycopy(arr, 0, newArr, 0, size);
        arr = newArr;
    }
    arr[size++] = s;
}

String get(int index) {
    if (index < 0 || index >= size) throw new IndexOutOfBoundsException();
    return arr[index];
}

// ✅ AFTER: Using ArrayList (simple)
List<String> list = new ArrayList<>();
list.add("Hello");           // Auto-resizes!
String s = list.get(0);      // Bounds checking included!
list.remove("Hello");        // Easy removal!
```

---

## 6. When to Use ArrayList

### ✅ Good Use Cases

| Scenario | Why ArrayList |
|----------|---------------|
| Frequent random access by index | O(1) get/set |
| Add elements mostly at end | O(1) amortized add |
| Known size upfront | Set initial capacity |
| Read-heavy, write-light | Fast reads |
| Need to sort/search | Works with Collections utilities |

### ❌ When NOT to Use

| Scenario | Better Alternative | Why |
|----------|-------------------|-----|
| Frequent insert/remove in middle | `LinkedList` | O(n) shift in ArrayList |
| Thread-safe needed | `CopyOnWriteArrayList` or `synchronized` | ArrayList is not thread-safe |
| Unique elements only | `HashSet` | ArrayList allows duplicates |
| FIFO queue operations | `ArrayDeque` | Designed for queue ops |

---

## 7. Common Gotchas

### ❌ Gotcha 1: ConcurrentModificationException

```java
List<String> list = new ArrayList<>(Arrays.asList("a", "b", "c"));

// 💥 WRONG: Modifying while iterating with for-each
for (String s : list) {
    if (s.equals("b")) {
        list.remove(s);  // ConcurrentModificationException!
    }
}

// ✅ CORRECT: Use Iterator.remove()
Iterator<String> it = list.iterator();
while (it.hasNext()) {
    if (it.next().equals("b")) {
        it.remove();  // Safe!
    }
}

// ✅ ALSO CORRECT: Use removeIf() (Java 8+)
list.removeIf(s -> s.equals("b"));
```

### ❌ Gotcha 2: Autoboxing with primitives

```java
List<Integer> list = new ArrayList<>();
list.add(1);
list.add(2);
list.add(3);

// 💥 CONFUSING: remove(1) removes at INDEX 1, not value 1!
list.remove(1);  // Removes element at index 1 → removes "2"

// ✅ To remove VALUE 1:
list.remove(Integer.valueOf(1));
```

### ❌ Gotcha 3: Fixed-size list from Arrays.asList()

```java
// 💥 This list is FIXED-SIZE!
List<String> list = Arrays.asList("a", "b", "c");
list.add("d");  // UnsupportedOperationException!

// ✅ Wrap in ArrayList for modifiable list
List<String> modifiable = new ArrayList<>(Arrays.asList("a", "b", "c"));
modifiable.add("d");  // Works!
```

---

## 8. Summary

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                           ARRAYLIST SUMMARY                                  │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│   WHAT:     Resizable array implementation of List                          │
│   BACKING:  Object[] elementData                                            │
│   GROWTH:   ~50% when full (oldCap + oldCap >> 1)                           │
│                                                                              │
│   FAST:     get(i), set(i), add() at end                                    │
│   SLOW:     add(i)/remove(i) in middle, contains(), indexOf()               │
│                                                                              │
│   USE WHEN:                                                                  │
│   • Need fast random access                                                  │
│   • Mostly add at end                                                        │
│   • Read-heavy workload                                                      │
│                                                                              │
│   AVOID WHEN:                                                                │
│   • Frequent insert/remove in middle                                         │
│   • Need thread-safety                                                       │
│   • Need unique elements                                                     │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```
