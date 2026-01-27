# Vector and Stack in Java

> **Prerequisites:** Understanding of [ArrayList](./ArrayListNotes.md), [Collection Framework](../c4_collection_framework/CollectionFrameworkNotes.md)

---

## What You'll Learn

| Question | Answer |
|----------|--------|
| **WHAT** | Vector: synchronized ArrayList; Stack: LIFO data structure |
| **WHY** | Legacy classes from Java 1.0 (before Collections Framework) |
| **WHEN** | Almost never in new code - use ArrayList or ArrayDeque |
| **HOW** | Vector uses synchronized methods; Stack extends Vector |

---

## 1. Hierarchy

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                         VECTOR & STACK HIERARCHY                             │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│   java.lang.Iterable<E>                                                      │
│          │                                                                   │
│          ▼                                                                   │
│   java.util.Collection<E>                                                    │
│          │                                                                   │
│          ▼                                                                   │
│   java.util.List<E>                                                          │
│          │                                                                   │
│          ▼                                                                   │
│   java.util.AbstractList<E>                                                  │
│          │                                                                   │
│          ▼                                                                   │
│   ┌─────────────────────────────────────────────────────────────────┐       │
│   │                     java.util.Vector<E>                          │       │
│   │                                                                  │       │
│   │   ⚠️ LEGACY CLASS (since Java 1.0)                              │       │
│   │   • Synchronized (thread-safe, but slow)                        │       │
│   │   • Implements: List, RandomAccess, Cloneable, Serializable    │       │
│   └─────────────────────────────────────────────────────────────────┘       │
│          │                                                                   │
│          ▼                                                                   │
│   ┌─────────────────────────────────────────────────────────────────┐       │
│   │                     java.util.Stack<E>                           │       │
│   │                                                                  │       │
│   │   ⚠️ DEPRECATED (use ArrayDeque instead!)                       │       │
│   │   • Extends Vector (inherits synchronization)                   │       │
│   │   • Adds push(), pop(), peek(), empty(), search()               │       │
│   └─────────────────────────────────────────────────────────────────┘       │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 2. Vector: Synchronized ArrayList

### Why Vector is Legacy

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                     WHY IS VECTOR LEGACY?                                    │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│   Vector was created in Java 1.0 (1996), BEFORE Collections Framework       │
│                                                                              │
│   PROBLEM: Every method is synchronized                                      │
│   ─────────────────────────────────────────                                 │
│                                                                              │
│   public synchronized boolean add(E e) { ... }                              │
│   public synchronized E get(int index) { ... }                              │
│   public synchronized E remove(int index) { ... }                           │
│                                                                              │
│   Even when used in single-threaded code:                                   │
│   ┌────────────────────────────────────────────────────────────────┐        │
│   │  Thread 1:  acquire lock → add() → release lock                │        │
│   │             acquire lock → get() → release lock                 │        │
│   │             acquire lock → add() → release lock                 │        │
│   │                                                                 │        │
│   │  ❌ Lock overhead on EVERY operation!                          │        │
│   │  ❌ No benefit when single-threaded                            │        │
│   │  ❌ Still not truly thread-safe for compound operations        │        │
│   └────────────────────────────────────────────────────────────────┘        │
│                                                                              │
│   MODERN ALTERNATIVES:                                                       │
│   • Single-threaded: ArrayList (no sync overhead)                           │
│   • Multi-threaded: Collections.synchronizedList(new ArrayList<>())        │
│   • Concurrent: CopyOnWriteArrayList                                        │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

### Vector vs ArrayList

| Feature | Vector | ArrayList |
|---------|--------|-----------|
| **Synchronization** | Every method synchronized | Not synchronized |
| **Performance** | Slower (lock overhead) | Faster |
| **Growth** | Doubles (100%) | Grows by 50% |
| **Since** | Java 1.0 (legacy) | Java 1.2 (Collections) |
| **Use in new code** | ❌ Avoid | ✅ Preferred |

---

## 3. Stack: LIFO Data Structure

### Stack Operations

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                     STACK: LIFO (Last-In-First-Out)                          │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│   Stack<String> stack = new Stack<>();                                       │
│                                                                              │
│   push("A")          push("B")          push("C")          pop()            │
│   ─────────          ─────────          ─────────          ─────            │
│                                                                              │
│   ┌───────┐         ┌───────┐         ┌───────┐         ┌───────┐          │
│   │       │         │       │         │  "C"  │ ← top   │       │ → "C"    │
│   │       │         │  "B"  │ ← top   ├───────┤         │  "B"  │ ← top    │
│   │  "A"  │ ← top   ├───────┤         │  "B"  │         ├───────┤          │
│   └───────┘         │  "A"  │         ├───────┤         │  "A"  │          │
│                     └───────┘         │  "A"  │         └───────┘          │
│                                       └───────┘                             │
│                                                                              │
│   OPERATIONS:                                                                │
│   • push(E item)  → Add to top                                              │
│   • pop()         → Remove and return top                                   │
│   • peek()        → View top without removing                               │
│   • empty()       → Check if stack is empty                                 │
│   • search(Object) → Find position from top (1-based)                       │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

### Why Stack is Deprecated

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                     WHY IS STACK DEPRECATED?                                 │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│   PROBLEM 1: Stack extends Vector                                            │
│   ───────────────────────────────                                           │
│   Stack inherits ALL Vector methods, breaking encapsulation:                │
│                                                                              │
│   Stack<String> stack = new Stack<>();                                       │
│   stack.push("A");                                                           │
│   stack.push("B");                                                           │
│   stack.add(0, "X");  // 💥 Can insert anywhere! Breaks LIFO!               │
│   stack.remove(1);    // 💥 Can remove from middle!                          │
│                                                                              │
│   A "proper" Stack should ONLY allow push/pop/peek!                         │
│                                                                              │
│   PROBLEM 2: Synchronized overhead                                           │
│   ────────────────────────────────                                          │
│   Same as Vector - every operation has lock overhead.                       │
│                                                                              │
│   MODERN ALTERNATIVE: ArrayDeque                                             │
│   ────────────────────────────────                                          │
│   Deque<String> stack = new ArrayDeque<>();                                 │
│   stack.push("A");   // ✅ Add to top                                       │
│   stack.pop();       // ✅ Remove from top                                  │
│   stack.peek();      // ✅ View top                                         │
│   // No add(index), remove(index) - proper stack behavior!                  │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 4. Modern Alternatives

### For Synchronized List

```java
// ❌ LEGACY: Vector
Vector<String> vector = new Vector<>();

// ✅ MODERN: Synchronized wrapper around ArrayList
List<String> syncList = Collections.synchronizedList(new ArrayList<>());

// ✅ MODERN: CopyOnWriteArrayList (for read-heavy workloads)
List<String> cowList = new CopyOnWriteArrayList<>();
```

### For Stack Behavior

```java
// ❌ DEPRECATED: Stack
Stack<String> stack = new Stack<>();
stack.push("A");
stack.pop();

// ✅ MODERN: ArrayDeque as stack
Deque<String> stack = new ArrayDeque<>();
stack.push("A");
stack.pop();

// ✅ Why ArrayDeque is better:
// • Faster (no synchronization)
// • No inherited methods that break stack semantics
// • Implements Deque interface (more flexible)
```

---

## 5. Time Complexity

### Vector

| Operation | Vector | ArrayList |
|-----------|--------|-----------|
| `get(index)` | O(1) + sync overhead | O(1) |
| `add(element)` | O(1) + sync overhead | O(1) amortized |
| `add(index, element)` | O(n) + sync overhead | O(n) |
| `remove(index)` | O(n) + sync overhead | O(n) |

### Stack

| Operation | Time | Notes |
|-----------|------|-------|
| `push(element)` | O(1) | Add at end, synchronized |
| `pop()` | O(1) | Remove from end, synchronized |
| `peek()` | O(1) | View end, synchronized |
| `search(object)` | O(n) | Linear search from top |
| `empty()` | O(1) | Check size |

---

## 6. When to Use (Almost Never!)

### ✅ Only Use When

| Scenario | Why |
|----------|-----|
| Maintaining legacy code | Existing codebase uses them |
| API requires it | Third-party library expects Vector/Stack |

### ❌ Don't Use For

| Scenario | Use Instead |
|----------|-------------|
| New single-threaded code | `ArrayList` |
| Need thread-safe list | `Collections.synchronizedList()` or `CopyOnWriteArrayList` |
| Need stack behavior | `ArrayDeque` |
| Need queue behavior | `ArrayDeque` or `LinkedList` |

---

## 7. Summary

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                      VECTOR & STACK SUMMARY                                  │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│   VECTOR:                                                                    │
│   • Legacy synchronized ArrayList (Java 1.0)                                │
│   • Every method has lock overhead                                           │
│   • Use ArrayList + Collections.synchronizedList() instead                  │
│                                                                              │
│   STACK:                                                                     │
│   • LIFO data structure (push/pop/peek)                                     │
│   • Extends Vector (bad design - inherits non-stack methods)               │
│   • Use ArrayDeque instead                                                   │
│                                                                              │
│   ┌────────────────────────────────────────────────────────────────┐        │
│   │   ⚠️ BOTH ARE LEGACY - AVOID IN NEW CODE!                     │        │
│   │                                                                 │        │
│   │   Vector → ArrayList (or Collections.synchronizedList())       │        │
│   │   Stack  → ArrayDeque                                          │        │
│   └────────────────────────────────────────────────────────────────┘        │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```
