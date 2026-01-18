# Inheritance in Java

## Prerequisites
- [c0: OOP Introduction](../c0_introduction/OOPIntroduction.md) — Understanding of classes, encapsulation
- [c1: Access Modifiers](../c1_accessmodifiers/AccessModifiersNotes.md) — Visibility rules (important for inheritance!)
- [c2: Constructors](../c2_constructors/ConstructorsNotes.md) — How objects are created

---

## What You'll Learn
| Question | Answer |
|----------|--------|
| **WHAT** | A child class acquires properties and behaviors from a parent class |
| **WHY** | To reuse code and establish IS-A relationships |
| **WHEN** | When you have a clear "X is a type of Y" relationship |
| **HOW** | Using the `extends` keyword |

---

## Why Does Inheritance Exist? (The History)

### The Problem: Code Duplication

Imagine building a system with Students and Teachers:

```java
// Without inheritance - massive duplication!
class Student {
    String name;
    String email;
    
    void login() { System.out.println(name + " logged in"); }
    void logout() { System.out.println(name + " logged out"); }
    
    void study() { /* Student-specific */ }
}

class Teacher {
    String name;      // 💥 Duplicated!
    String email;     // 💥 Duplicated!
    
    void login() { System.out.println(name + " logged in"); }  // 💥 Same!
    void logout() { System.out.println(name + " logged out"); } // 💥 Same!
    
    void teach() { /* Teacher-specific */ }
}
```

**Problems:**
1. Same code in multiple places (DRY violation)
2. Bug fix in login? Must update EVERY class!
3. 10 new user types = 10x the maintenance

### The OOP Solution

```java
class User {
    String name;
    String email;
    
    void login() { System.out.println(name + " logged in"); }
    void logout() { System.out.println(name + " logged out"); }
}

class Student extends User {  // ✅ Inherits name, email, login, logout
    void study() { /* Student-specific */ }
}

class Teacher extends User {  // ✅ Inherits name, email, login, logout
    void teach() { /* Teacher-specific */ }
}
```

> **Key Insight:** Write once in the parent, use everywhere in children.

---

## Inheritance Hierarchy

```
┌─────────────────────────────────────────────────────────────┐
│                    INHERITANCE HIERARCHY                    │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│                      ┌─────────┐                            │
│                      │  User   │  ← Parent / Superclass     │
│                      │─────────│                            │
│                      │ name    │                            │
│                      │ email   │                            │
│                      │─────────│                            │
│                      │ login() │                            │
│                      │ logout()│                            │
│                      └────┬────┘                            │
│                           │                                 │
│              ┌────────────┼────────────┐                    │
│              │            │            │                    │
│              ▼            ▼            ▼                    │
│        ┌─────────┐  ┌─────────┐  ┌─────────┐               │
│        │ Student │  │ Teacher │  │  Admin  │  ← Children   │
│        │─────────│  │─────────│  │─────────│    (Subclass) │
│        │batchName│  │ subject │  │ level   │               │
│        │─────────│  │─────────│  │─────────│               │
│        │ study() │  │ teach() │  │manage() │               │
│        └─────────┘  └─────────┘  └─────────┘               │
│                                                             │
│  All children INHERIT: name, email, login(), logout()       │
│  Each child ADDS its own specific fields and methods        │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

---

## The `extends` Keyword

```java
public class Child extends Parent {
    // Child inherits ALL non-private members from Parent
}
```

### What is Inherited?

| Member Type | Inherited? |
|-------------|:----------:|
| public fields/methods | ✅ Yes |
| protected fields/methods | ✅ Yes |
| default (package-private) | ✅ (if same package) |
| private fields/methods | ❌ No |
| Constructors | ❌ No (but can be called via super) |

---

## Constructor Chaining with `super()`

**Rule:** Parent must be constructed before child.

```
┌─────────────────────────────────────────────────────────────┐
│               CONSTRUCTOR CHAINING                          │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│   new Student("Alice", "a@x.com", "Sept23")                 │
│         │                                                   │
│         ▼                                                   │
│   ┌─────────────────────────────┐                          │
│   │ Student Constructor        │                          │
│   │   super(name, email);  ────┼──┐                       │
│   │   this.batchName = batch;  │  │                       │
│   └─────────────────────────────┘  │                       │
│                                    ▼                       │
│                    ┌─────────────────────────────┐         │
│                    │ User Constructor            │         │
│                    │   this.name = name;         │         │
│                    │   this.email = email;       │         │
│                    └─────────────────────────────┘         │
│                                                             │
│   Order: User constructor runs FIRST, then Student          │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

```java
public class User {
    protected String name;
    protected String email;
    
    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }
}

public class Student extends User {
    public String batchName;
    
    public Student(String name, String email, String batchName) {
        super(name, email);  // MUST be first line!
        this.batchName = batchName;
    }
}
```

---

## Upcasting and Polymorphism (Preview)

**Upcasting:** Treating a child object as a parent type.

```java
User u = new Student("Alice", "a@x.com", "Sept23");  // ✅ Upcasting

// u is a User reference, but holds a Student object
u.login();   // ✅ Works (User method)
// u.study();   // ❌ COMPILE ERROR (User doesn't have study())
```

```
┌─────────────────────────────────────────────────────────────┐
│                       UPCASTING                             │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│   Reference Type: User ───┐                                 │
│                           │     What YOU see                │
│                           ▼                                 │
│                    ┌─────────────┐                          │
│                    │    User     │   login() ✅             │
│                    │   (view)    │   logout() ✅            │
│                    └─────────────┘                          │
│                                                             │
│   Actual Object: Student ─┐                                 │
│                           │     What's REALLY there         │
│                           ▼                                 │
│                    ┌─────────────┐                          │
│                    │  Student    │   login() ✅             │
│                    │  (actual)   │   logout() ✅            │
│                    │             │   study() (hidden)       │
│                    └─────────────┘                          │
│                                                             │
│   The reference TYPE limits what you can SEE.               │
│   The actual OBJECT determines what RUNS.                   │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

---

## When to Use Inheritance

### ✅ Good Use Cases

| Relationship | Example |
|--------------|---------|
| IS-A relationship | Dog IS-A Animal |
| Behavioral extension | PremiumUser extends User |
| Framework hooks | MyController extends BaseController |

### Decision Flowchart

```
Is there a clear IS-A relationship?
        │
        ├─── YES ───► Does child need most parent behaviors?
        │                    │
        │                    ├─── YES ───► ✅ Use Inheritance
        │                    │
        │                    └─── NO ────► Consider Composition
        │
        └─── NO ────► ❌ Don't use Inheritance
                      Consider Composition instead
```

---

## When NOT to Use Inheritance (Anti-Patterns)

### ❌ Just to Reuse a Few Methods

```java
// BAD: Stack IS-NOT-A Vector!
public class Stack extends Vector {  // JDK design mistake
    // Stack can now do vector.add(index, item)
    // which violates stack semantics 💥
}

// GOOD: Composition
public class Stack {
    private ArrayList<Object> items = new ArrayList<>();
    
    public void push(Object item) { items.add(item); }
    public Object pop() { return items.remove(items.size() - 1); }
}
```

### ❌ Deep Inheritance Hierarchies

```java
// BAD: Too many levels
Animal → Mammal → Canine → Pet → Dog → GermanShepherd → MyDog

// Each level adds complexity and rigidity
```

### ❌ HAS-A Disguised as IS-A

```java
// BAD: Car IS-NOT-A Engine!
public class Car extends Engine { }

// GOOD: Car HAS-A Engine
public class Car {
    private Engine engine;  // Composition
}
```

---

## Common Gotchas

### 1. Forgetting to Call `super()` with Arguments

```java
public class Parent {
    public Parent(String name) { }  // No default constructor!
}

public class Child extends Parent {
    public Child() {
        // super();  ← Implicit super() won't work!
        // COMPILE ERROR: no suitable constructor found
    }
}
```

### 2. Accessing Private Parent Members

```java
public class Parent {
    private int secret = 42;
}

public class Child extends Parent {
    public void reveal() {
        // System.out.println(secret);  // ❌ COMPILE ERROR
        // Private is NEVER inherited, even to children
    }
}
```

### 3. Overriding vs Hiding (Static Methods)

```java
public class Parent {
    public static void greet() { System.out.println("Parent"); }
}

public class Child extends Parent {
    public static void greet() { System.out.println("Child"); }  // HIDING, not overriding!
}

Parent p = new Child();
p.greet();  // Prints "Parent"! Static uses reference type, not object type.
```

---

## Negative Cases (What is NOT Allowed)

| Scenario | Code | Error |
|----------|------|-------|
| Child ref = Parent object | `Student s = new User();` | COMPILE ERROR |
| Parent ref accessing child field | `User u = new Student(); u.batchName;` | COMPILE ERROR |
| Java does NOT support multiple class inheritance | `class A extends B, C` | COMPILE ERROR |

---

## Java's Single Inheritance Limitation

```
┌─────────────────────────────────────────────────────────────┐
│              WHY NO MULTIPLE INHERITANCE?                   │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│   THE DIAMOND PROBLEM:                                      │
│                                                             │
│              ┌───────┐                                      │
│              │   A   │     class A { void foo(); }          │
│              └───┬───┘                                      │
│                  │                                          │
│          ┌───────┴───────┐                                  │
│          │               │                                  │
│          ▼               ▼                                  │
│      ┌───────┐       ┌───────┐                              │
│      │   B   │       │   C   │   Both override foo()        │
│      └───┬───┘       └───┬───┘                              │
│          │               │                                  │
│          └───────┬───────┘                                  │
│                  │                                          │
│                  ▼                                          │
│              ┌───────┐                                      │
│              │   D   │   Which foo() does D inherit? 🤔     │
│              └───────┘                                      │
│                                                             │
│   Java avoids this by allowing only ONE parent class.       │
│   (But you CAN implement multiple interfaces!)              │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

---

## Project Demo Structure

```
c3_inheritance/
├── InheritanceNotes.md  ← You are here
├── Main.java            ← Entry point with demos
├── base/
│   └── User.java        ← Parent class
└── child/
    ├── Student.java     ← Child class
    └── Mentor.java      ← Another child class
```

---

## Summary

| Concept | Key Point |
|---------|-----------|
| **Inheritance** | Child acquires parent's non-private members |
| **extends** | Keyword to establish parent-child relationship |
| **super()** | Call parent constructor (must be first line) |
| **Upcasting** | Parent ref = Child object (polymorphism) |
| **Single Inheritance** | Java allows only one parent class |

> **Remember:** Favor composition over inheritance when there's no clear IS-A relationship.

---

## Next Chapter
→ [c4: Polymorphism](../c4_polymorphism/PolymorphismNotes.md) — Same interface, different behaviors
