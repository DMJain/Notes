# Constructors & Memory Model in Java

## Prerequisites
- [c0: OOP Introduction](../c0_introduction/OOPIntroduction.md) — Understanding of classes and objects
- [c1: Access Modifiers](../c1_accessmodifiers/AccessModifiersNotes.md) — Understanding of visibility

---

## What You'll Learn
| Question | Answer |
|----------|--------|
| **WHAT** | Special methods that initialize objects when created |
| **WHY** | To ensure objects start in a valid state |
| **WHEN** | Every time you use `new ClassName()` |
| **HOW** | Constructor types, copy constructors, pass-by-value |

---

## Why Do Constructors Exist? (The History)

### The Problem Without Constructors

In C, you had to manually initialize structs:

```c
// C - Manual initialization
struct Student {
    char name[50];
    int age;
};

struct Student s;
// Oops! Forgot to initialize age
strcpy(s.name, "Alice");
// s.age is garbage! 💥
```

**Problems:**
1. Easy to forget initialization
2. Objects can be in invalid states
3. No guarantee of proper setup

### The OOP Solution

Constructors **guarantee** initialization:

```java
public class Student {
    private String name;
    private int age;
    
    public Student(String name, int age) {
        this.name = name;
        this.age = age;  // Always initialized!
    }
}

Student s = new Student("Alice", 20);  // ✅ Valid from birth
```

> **Key Insight:** Constructors ensure objects are "born valid."

---

## Constructor Rules

```
┌────────────────────────────────────────────────────────┐
│                   CONSTRUCTOR RULES                    │
├────────────────────────────────────────────────────────┤
│                                                        │
│   1. Same name as the class                           │
│   2. NO return type (not even void)                   │
│   3. Called automatically with 'new'                  │
│   4. Can be overloaded (multiple constructors)        │
│   5. Default constructor provided if none declared    │
│                                                        │
└────────────────────────────────────────────────────────┘
```

---

## Types of Constructors

### 1. Default Constructor (No-Args)

```java
public class Student {
    String name;
    
    // Default constructor
    public Student() {
        name = "Unknown";
    }
}

Student s = new Student();  // name = "Unknown"
```

> **Note:** If you don't write ANY constructor, Java provides one automatically.

### 2. Parameterized Constructor

```java
public class Student {
    private String name;
    private int age;
    
    public Student(String name, int age) {
        this.name = name;  // 'this' distinguishes field from parameter
        this.age = age;
    }
}

Student s = new Student("Alice", 20);
```

### 3. Copy Constructor

```java
public class Student {
    private String name;
    private int[] scores;
    
    // Copy constructor
    public Student(Student other) {
        this.name = other.name;        // Copy primitive
        this.scores = other.scores.clone();  // Deep copy array!
    }
}

Student original = new Student("Alice", new int[]{90, 85});
Student copy = new Student(original);  // Independent copy
```

---

## Java Memory Model

Understanding how Java manages memory is crucial for understanding copy behavior.

```
┌──────────────────┐         ┌──────────────────────────────┐
│     STACK        │         │           HEAP               │
│  (Method Calls)  │         │      (Object Storage)        │
├──────────────────┤         ├──────────────────────────────┤
│                  │         │                              │
│  main()          │         │  ┌─────────────────────┐     │
│  ├─ s1 ──────────┼────────►│  │  Student Object     │     │
│  │  (reference)  │         │  │  name: "Alice"      │     │
│  │               │         │  │  scores: ─────────┐ │     │
│  └─ int x = 5    │         │  └─────────────────────┘     │
│     (primitive)  │         │           │                  │
│                  │         │           ▼                  │
│                  │         │  ┌─────────────┐             │
│                  │         │  │  int[] {90} │             │
│                  │         │  └─────────────┘             │
│                  │         │                              │
└──────────────────┘         └──────────────────────────────┘

KEY:
- Primitives (int, double, boolean) → Stored directly on Stack
- Objects and Arrays → Stored on Heap, Stack holds reference (address)
```

---

## Pass-by-Value in Java

> **Important:** Java is **ALWAYS** pass-by-value. For objects, the "value" is the reference (memory address).

### Visualizing Pass-by-Value

```
┌─────────────────────────────────────────────────────────────┐
│  BEFORE: main() calls modifyStudent(s1)                     │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│  STACK                        HEAP                          │
│  ┌─────────────┐              ┌─────────────────┐          │
│  │ main()      │              │ Student Object  │          │
│  │ s1: 0x100 ──┼─────────────►│ name: "Alice"   │          │
│  └─────────────┘              └─────────────────┘          │
│                                      ▲                      │
│  ┌─────────────┐                     │                      │
│  │ modify()    │                     │                      │
│  │ s: 0x100 ───┼─────────────────────┘                      │
│  └─────────────┘                                            │
│                                                             │
│  Both s1 and s point to the SAME object                     │
└─────────────────────────────────────────────────────────────┘
```

### Case A: Reassigning Reference (No Effect on Original)

```java
public static void modifyReference(Student s) {
    s = new Student("New", 99);  // s now points to NEW object
    System.out.println(s.name);   // "New"
}

Student s1 = new Student("Alice", 20);
modifyReference(s1);
System.out.println(s1.name);  // Still "Alice"! ✅
```

**Why?** The method got a COPY of the reference. Reassigning the copy doesn't affect the original.

```
After s = new Student():
STACK                           HEAP
┌─────────────┐                 ┌─────────────────┐
│ main()      │                 │ Student Object  │
│ s1: 0x100 ──┼────────────────►│ name: "Alice"   │ ← Original unchanged!
└─────────────┘                 └─────────────────┘

┌─────────────┐                 ┌─────────────────┐
│ modify()    │                 │ Student Object  │
│ s: 0x200 ───┼────────────────►│ name: "New"     │ ← New object
└─────────────┘                 └─────────────────┘
```

### Case B: Modifying Object (Affects Original!)

```java
public static void modifyObject(Student s) {
    s.name = "Changed";  // Modifies the actual object
}

Student s1 = new Student("Alice", 20);
modifyObject(s1);
System.out.println(s1.name);  // "Changed"! 💥
```

**Why?** Both references point to the SAME object. Following the reference modifies that object.

---

## Shallow Copy vs Deep Copy

### The Problem

```java
Student s1 = new Student("Alice", new int[]{90, 85});
Student s2 = s1;  // ❌ Not a copy! Same object!

s2.name = "Bob";
System.out.println(s1.name);  // "Bob" 💥 s1 changed too!
```

### Shallow Copy

Copies field **values**. For references, copies the **address** (points to same object).

```
┌─────────────────────────────────────────────────────────────┐
│                     SHALLOW COPY                            │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│   s1 ──────────────────────┐                                │
│                            ▼                                │
│                     ┌─────────────┐     ┌─────────────┐     │
│                     │   Student   │     │   int[]     │     │
│                     │ name:"Alice"│────►│  [90, 85]   │     │
│                     │ scores ─────┤     └─────────────┘     │
│                     └─────────────┘            ▲            │
│                            ▲                   │            │
│   s2 ──────────────────────┘                   │            │
│                                                │            │
│          SHALLOW COPY                          │            │
│          (name copied, scores points to SAME array)         │
│                                                             │
│   s2.scores[0] = 100;  → s1.scores[0] is now 100 too! 💥   │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

### Deep Copy

Creates **new instances** for all nested objects.

```
┌─────────────────────────────────────────────────────────────┐
│                       DEEP COPY                             │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│   s1 ─────────────────────┐                                 │
│                           ▼                                 │
│                    ┌─────────────┐     ┌─────────────┐      │
│                    │   Student   │     │   int[]     │      │
│                    │ name:"Alice"│────►│  [90, 85]   │      │
│                    │ scores ─────┤     └─────────────┘      │
│                    └─────────────┘                          │
│                                                             │
│   s2 ─────────────────────┐                                 │
│                           ▼                                 │
│                    ┌─────────────┐     ┌─────────────┐      │
│                    │   Student   │     │   int[]     │      │
│                    │ name:"Alice"│────►│  [90, 85]   │      │
│                    │ scores ─────┤     └─────────────┘      │
│                    └─────────────┘         (SEPARATE!)      │
│                                                             │
│   s2.scores[0] = 100;  → s1.scores still [90, 85] ✅       │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

### How to Deep Copy

```java
public class Student {
    private String name;
    private int[] scores;
    
    // Deep Copy Constructor
    public Student(Student other) {
        this.name = other.name;  // String is immutable, copy is safe
        this.scores = other.scores.clone();  // Clone creates new array
    }
}
```

---

## When to Use Each Copy Type

| Scenario | Shallow Copy | Deep Copy |
|----------|:------------:|:---------:|
| Primitive fields only | ✅ | ✅ |
| Immutable objects (String) | ✅ | ✅ |
| Mutable nested objects | ❌ | ✅ |
| Need independent copies | ❌ | ✅ |
| Performance critical | ✅ (faster) | ❌ (slower) |

---

## When NOT to Use (Anti-Patterns)

### ❌ Constructor Does Too Much

```java
// BAD: Constructor does heavy work
public class DataProcessor {
    public DataProcessor(String filePath) {
        // Reading 1GB file in constructor 💥
        byte[] data = Files.readAllBytes(Paths.get(filePath));
        process(data);
    }
}

// GOOD: Constructor just initializes
public class DataProcessor {
    private String filePath;
    
    public DataProcessor(String filePath) {
        this.filePath = filePath;  // Quick!
    }
    
    public void process() {  // Heavy work done explicitly
        // ...
    }
}
```

### ❌ Forgetting to Deep Copy

```java
// BAD: Shallow copy of array
public Student(Student other) {
    this.name = other.name;
    this.scores = other.scores;  // 💥 Same array reference!
}

// GOOD: Deep copy
public Student(Student other) {
    this.name = other.name;
    this.scores = other.scores.clone();  // ✅ New array
}
```

---

## Common Gotchas

### 1. No Default Constructor After Defining Any Constructor

```java
public class Student {
    public Student(String name) { }  // Parameterized constructor
}

Student s = new Student();  // ❌ COMPILE ERROR!
// Default constructor is NOT provided if ANY constructor is defined
```

### 2. Constructor Chaining with `this()`

```java
public class Student {
    private String name;
    private int age;
    
    public Student() {
        this("Unknown", 0);  // Calls other constructor
    }
    
    public Student(String name, int age) {
        this.name = name;
        this.age = age;
    }
}
```

### 3. Super Constructor Call

```java
public class Child extends Parent {
    public Child() {
        super();  // Must be FIRST line! (or implicit)
    }
}
```

---

## Project Demo Structure

```
c2_constructors/
├── ConstructorsNotes.md  ← You are here
├── ConstructorDemo.java  ← Entry point with all demos
└── Student.java          ← Student class with all constructor types
```

---

## Summary

| Concept | Key Point |
|---------|-----------|
| **Constructor** | Special method, same name as class, no return type |
| **Default** | No-args, auto-provided if none defined |
| **Parameterized** | Takes arguments to initialize fields |
| **Copy** | Creates new object from existing one |
| **Shallow Copy** | Copies references (share nested objects) |
| **Deep Copy** | Creates new instances of nested objects |
| **Pass-by-Value** | Java copies the reference value, not the object |

---

## Next Chapter
→ [c3: Inheritance](../c3_inheritance/InheritanceNotes.md) — Code reuse through parent-child relationships
