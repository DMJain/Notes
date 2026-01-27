# Iterators in Java

> **Prerequisites:** [Collection Framework](../c4_collection_framework/CollectionFrameworkNotes.md)

---

## What You'll Learn

| Question | Answer |
|----------|--------|
| **WHAT** | Objects that allow traversal over collections |
| **WHY** | Safe removal during iteration, uniform traversal API |
| **WHEN** | Need to remove while iterating, need explicit control |
| **HOW** | Iterator pattern - hasNext(), next(), remove() |

---

## 1. Iterator vs Enhanced For-Loop

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                    ITERATOR vs FOR-EACH                                      │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│   FOR-EACH (syntactic sugar):                                                │
│   ─────────────────────────────                                             │
│   for (String s : list) {                                                    │
│       System.out.println(s);                                                 │
│       // list.remove(s);  ← 💥 ConcurrentModificationException!             │
│   }                                                                          │
│                                                                              │
│   ITERATOR (explicit):                                                       │
│   ─────────────────────                                                     │
│   Iterator<String> it = list.iterator();                                     │
│   while (it.hasNext()) {                                                     │
│       String s = it.next();                                                  │
│       if (shouldRemove(s)) {                                                 │
│           it.remove();  ← ✅ Safe removal!                                  │
│       }                                                                      │
│   }                                                                          │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 2. ConcurrentModificationException

```java
// ❌ WRONG: Modifying during for-each
List<String> list = new ArrayList<>(Arrays.asList("a", "b", "c"));
for (String s : list) {
    if (s.equals("b")) {
        list.remove(s);  // 💥 ConcurrentModificationException!
    }
}

// ✅ CORRECT: Using Iterator.remove()
Iterator<String> it = list.iterator();
while (it.hasNext()) {
    if (it.next().equals("b")) {
        it.remove();  // Safe!
    }
}

// ✅ ALSO CORRECT: removeIf() (Java 8+)
list.removeIf(s -> s.equals("b"));
```

---

## 3. ListIterator

ListIterator extends Iterator with bidirectional traversal and modification:

```java
ListIterator<String> lit = list.listIterator();

// Forward traversal
while (lit.hasNext()) {
    int index = lit.nextIndex();
    String element = lit.next();
}

// Backward traversal
while (lit.hasPrevious()) {
    int index = lit.previousIndex();
    String element = lit.previous();
}

// Modification
lit.set("newValue");    // Replace current
lit.add("inserted");    // Insert before next
lit.remove();           // Remove current
```

---

## 4. Summary

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                       ITERATORS SUMMARY                                      │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│   Iterator:                                                                  │
│   • hasNext(), next(), remove()                                             │
│   • ONLY safe way to remove during iteration                                │
│   • Works on any Collection                                                  │
│                                                                              │
│   ListIterator (for List only):                                              │
│   • Bidirectional: next(), previous()                                       │
│   • Index access: nextIndex(), previousIndex()                              │
│   • More modifications: add(), set()                                        │
│                                                                              │
│   Modern alternatives:                                                       │
│   • removeIf() - for conditional removal                                    │
│   • forEach() - for read-only iteration                                     │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```
