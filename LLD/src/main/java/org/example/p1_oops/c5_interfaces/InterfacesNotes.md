# Interfaces in Java

## Prerequisites
- [c0: OOP Introduction](../c0_introduction/OOPIntroduction.md) — Understanding that abstraction is the goal
- [c3: Inheritance](../c3_inheritance/InheritanceNotes.md) — Single inheritance limitation
- [c4: Polymorphism](../c4_polymorphism/PolymorphismNotes.md) — Runtime polymorphism concepts

---

## What You'll Learn
| Question | Answer |
|----------|--------|
| **WHAT** | A contract that defines WHAT a class must do, not HOW |
| **WHY** | Multiple inheritance of behavior, pluggable components |
| **WHEN** | When unrelated classes share a capability |
| **HOW** | Using `interface` and `implements` keywords |

---

## Why Do Interfaces Exist? (The History)

### The Problem: Java's Single Inheritance

Java allows only ONE parent class. But real-world objects have multiple capabilities:

```
┌─────────────────────────────────────────────────────────────┐
│              THE MULTIPLE CAPABILITY PROBLEM                │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│   A Duck can:                                               │
│     • Fly (like a Bird)                                     │
│     • Swim (like a Fish)                                    │
│     • Walk (like a Mammal)                                  │
│                                                             │
│   class Duck extends Bird, Fish, Mammal { }  // ❌ ILLEGAL! │
│                                                             │
│   Java says: "Pick ONE parent only!"                        │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

### The OOP Solution: Interfaces

```java
interface Flyable {
    void fly();
}

interface Swimmable {
    void swim();
}

interface Walkable {
    void walk();
}

class Duck implements Flyable, Swimmable, Walkable {  // ✅ Multiple!
    public void fly() { System.out.println("Duck flapping wings"); }
    public void swim() { System.out.println("Duck paddling"); }
    public void walk() { System.out.println("Duck waddling"); }
}
```

> **Key Insight:** Interfaces define capabilities (CAN-DO), not identity (IS-A).

---

## Interface = Contract

```
┌─────────────────────────────────────────────────────────────┐
│                  INTERFACE AS CONTRACT                      │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│   ┌─────────────────────────────────────────┐              │
│   │          interface Flyable              │              │
│   │         ─────────────────────           │              │
│   │          void fly();                    │              │
│   │                                         │              │
│   │   "I promise I will implement fly()"   │              │
│   └─────────────────────────────────────────┘              │
│                        │                                    │
│            ┌───────────┼───────────┐                       │
│            │           │           │                       │
│            ▼           ▼           ▼                       │
│      ┌─────────┐ ┌─────────┐ ┌─────────┐                  │
│      │  Bird   │ │  Plane  │ │Superman │                  │
│      │ fly() { │ │ fly() { │ │ fly() { │                  │
│      │ flap    │ │ engines │ │ cape    │                  │
│      │ wings } │ │       } │ │       } │                  │
│      └─────────┘ └─────────┘ └─────────┘                  │
│                                                             │
│   Contract met! All know HOW to fly, each in their own way │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

---

## Interface Syntax

### Declaring an Interface

```java
public interface Flyable {
    // Constants (implicitly public static final)
    int MAX_ALTITUDE = 10000;
    
    // Abstract method (implicitly public abstract)
    void fly();
    
    // Default method (Java 8+)
    default void land() {
        System.out.println("Landing safely");
    }
    
    // Static method (Java 8+)
    static void checkWeather() {
        System.out.println("Weather is good for flying");
    }
}
```

### Implementing an Interface

```java
public class Bird implements Flyable {
    @Override
    public void fly() {  // MUST be public!
        System.out.println("Bird flapping wings");
    }
    // land() uses default implementation from Flyable
}
```

---

## Interface vs Abstract Class

```
┌──────────────────────────────────────────────────────────────┐
│            INTERFACE vs ABSTRACT CLASS                       │
├──────────────────────────────────────────────────────────────┤
│                                                              │
│   INTERFACE                    ABSTRACT CLASS                │
│   ─────────                    ──────────────                │
│                                                              │
│   CAN-DO (Capability)          IS-A (Identity)               │
│   "Bird CAN fly"               "Eagle IS-A Bird"             │
│                                                              │
│   Multiple implementation      Single inheritance            │
│   (implements A, B, C)         (extends ONE only)            │
│                                                              │
│   No state (before Java 8)     Can have state                │
│                                                              │
│   All methods were abstract    Mix of abstract + concrete    │
│   (until Java 8 defaults)                                    │
│                                                              │
│   No constructors              Can have constructors         │
│                                                              │
└──────────────────────────────────────────────────────────────┘
```

### Quick Comparison Table

| Feature | Interface | Abstract Class |
|---------|:---------:|:--------------:|
| Multiple inheritance | ✅ Yes | ❌ No |
| State (instance fields) | ❌ No* | ✅ Yes |
| Constructor | ❌ No | ✅ Yes |
| Abstract methods | ✅ Yes | ✅ Yes |
| Concrete methods | ✅ (default) | ✅ Yes |
| Access modifiers on methods | public only | Any |

*Interfaces can only have `public static final` fields (constants)

---

## Multiple Interface Implementation

```java
interface Flyable {
    void fly();
}

interface Swimmable {
    void swim();
}

// Duck implements BOTH!
public class Duck implements Flyable, Swimmable {
    @Override
    public void fly() {
        System.out.println("Duck flying");
    }
    
    @Override
    public void swim() {
        System.out.println("Duck swimming");
    }
}
```

---

## Interface as a Type (Polymorphism)

```java
// Duck can be treated as Flyable OR Swimmable
Flyable f = new Duck();
Swimmable s = new Duck();

f.fly();   // ✅ Works
s.swim();  // ✅ Works

// f.swim();  // ❌ COMPILE ERROR: Flyable doesn't know about swim()
```

```
┌─────────────────────────────────────────────────────────────┐
│              INTERFACE POLYMORPHISM                         │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│   Flyable flying = ???                                      │
│                                                             │
│   flying = new Bird();     // ✅ Bird can fly               │
│   flying = new Plane();    // ✅ Plane can fly              │
│   flying = new Superman(); // ✅ Superman can fly           │
│   flying = new Fish();     // ❌ Fish cannot fly!           │
│                                                             │
│   // Uniform treatment                                      │
│   void performShow(Flyable[] performers) {                  │
│       for (Flyable f : performers) {                        │
│           f.fly();  // Works for any Flyable!               │
│       }                                                     │
│   }                                                         │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

---

## Java 8+ Interface Features

### Default Methods

Interfaces can now have method implementations:

```java
public interface Printable {
    void print();  // Abstract
    
    default void printTwice() {  // Has implementation!
        print();
        print();
    }
}
```

**Why?** To add new methods to interfaces without breaking existing implementations.

### Static Methods

```java
public interface MathOperations {
    static int add(int a, int b) {
        return a + b;
    }
}

// Called on interface directly
int sum = MathOperations.add(5, 3);
```

### Diamond Problem Resolution

```java
interface A {
    default void greet() { System.out.println("A"); }
}

interface B {
    default void greet() { System.out.println("B"); }
}

class C implements A, B {
    @Override
    public void greet() {
        A.super.greet();  // Explicitly choose A's version
        // OR provide own implementation
    }
}
```

---

## When to Use Interfaces

### ✅ Good Use Cases

| Scenario | Example |
|----------|---------|
| Defining capabilities | `Comparable`, `Serializable`, `Cloneable` |
| Multiple inheritance of behavior | `class Duck implements Flyable, Swimmable` |
| API contracts | `List`, `Map`, `Set` interfaces |
| Dependency injection | `PaymentProcessor` interface with multiple implementations |
| Strategy pattern | `SortingStrategy` with different algorithms |

### Decision Flowchart

```
Need to share BEHAVIOR (methods)?
       │
       ├── Among UNRELATED classes? ───► ✅ Use INTERFACE
       │
       └── Among RELATED classes with shared STATE? ───► Use ABSTRACT CLASS
```

---

## When NOT to Use Interfaces (Anti-Patterns)

### ❌ Interface Pollution (Too Many Small Interfaces)

```java
// BAD: Over-segregation
interface Walkable { void walk(); }
interface Talkable { void talk(); }
interface Breathable { void breathe(); }
interface Thinkable { void think(); }

class Human implements Walkable, Talkable, Breathable, Thinkable { }
// Every class now needs 4 implements clauses!

// GOOD: Reasonable grouping
interface Living { void breathe(); }
interface Ambulatory { void walk(); }
interface Communicative { void talk(); }
```

### ❌ Marker Interfaces (Empty Interfaces)

```java
// Mostly replaced by annotations now
interface Serializable { }  // No methods!

// Modern approach: Use annotations
@Serializable
class MyClass { }
```

### ❌ Fat Interfaces (Too Many Methods)

```java
// BAD: Interface Segregation Principle violation
interface Vehicle {
    void drive();
    void fly();
    void sail();
    void submerse();
}

class Car implements Vehicle {
    void drive() { }
    void fly() { throw new UnsupportedOperationException(); }  // 💥
    void sail() { throw new UnsupportedOperationException(); } // 💥
    void submerse() { throw new UnsupportedOperationException(); } // 💥
}

// GOOD: Separate interfaces
interface Drivable { void drive(); }
interface Flyable { void fly(); }
```

---

## Common Gotchas

### 1. Implementing Methods Must Be `public`

```java
interface Flyable {
    void fly();  // Implicitly public
}

class Bird implements Flyable {
    void fly() { }  // ❌ COMPILE ERROR! Cannot reduce visibility
    public void fly() { }  // ✅ Correct
}
```

### 2. Interface Fields Are Constants

```java
interface Config {
    int MAX_SIZE = 100;  // Implicitly: public static final
    // MAX_SIZE = 200;   // ❌ COMPILE ERROR: Cannot reassign
}
```

### 3. Cannot Instantiate Interfaces

```java
Flyable f = new Flyable();  // ❌ COMPILE ERROR!
Flyable f = new Bird();     // ✅ Create implementing class
```

---

## Real-World Example: JDBC

```
┌─────────────────────────────────────────────────────────────┐
│              JDBC: INTERFACE IN ACTION                      │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│   Java defines INTERFACES:                                  │
│   ┌────────────────────────────────────────┐               │
│   │  interface Connection { ... }          │               │
│   │  interface Statement { ... }           │               │
│   │  interface ResultSet { ... }           │               │
│   └────────────────────────────────────────┘               │
│                        │                                    │
│           ┌────────────┼────────────┐                      │
│           │            │            │                      │
│           ▼            ▼            ▼                      │
│   ┌───────────┐  ┌───────────┐  ┌───────────┐             │
│   │  MySQL    │  │  Oracle   │  │ PostgreSQL│             │
│   │  Driver   │  │  Driver   │  │  Driver   │             │
│   │           │  │           │  │           │             │
│   │implements │  │implements │  │implements │             │
│   │Connection │  │Connection │  │Connection │             │
│   └───────────┘  └───────────┘  └───────────┘             │
│                                                             │
│   Your code works with ANY database! Just swap drivers.     │
│                                                             │
│   Connection conn = DriverManager.getConnection(url);       │
│   // Works with MySQL, Oracle, PostgreSQL... any DB!        │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

---

## Project Demo Structure

```
c5_interfaces/
├── InterfacesNotes.md   ← You are here
├── Main.java            ← Entry point with demos
├── contract/
│   ├── Flyable.java     ← Interface
│   └── Swimmable.java   ← Another interface
└── impl/
    ├── Bird.java        ← Implements Flyable
    ├── Fish.java        ← Implements Swimmable
    └── Duck.java        ← Implements both!
```

---

## Summary

| Concept | Key Point |
|---------|-----------|
| **Interface** | Contract defining behaviors (CAN-DO) |
| **implements** | Keyword to fulfill a contract |
| **Multiple interfaces** | One class can implement many |
| **Default methods** | Java 8+ interface implementations |
| **vs Abstract Class** | Interface = capability, Abstract = identity |

> **Remember:** Use interfaces when you need pluggable, interchangeable components.

---

## Next Chapter
→ [c6: Abstract Classes](../c6_abstract/AbstractNotes.md) — Partial implementations and templates
