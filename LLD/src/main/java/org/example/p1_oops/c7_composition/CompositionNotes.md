# Composition & Association in Java

## Prerequisites
- [c0: OOP Introduction](../c0_introduction/OOPIntroduction.md) — Understanding objects and relationships
- [c3: Inheritance](../c3_inheritance/InheritanceNotes.md) — IS-A relationships (contrast with HAS-A)

---

## What You'll Learn
| Question | Answer |
|----------|--------|
| **WHAT** | Object relationships: HAS-A (owning or using other objects) |
| **WHY** | Flexible, modular design without inheritance rigidity |
| **WHEN** | When you need to build complex objects from simpler ones |
| **HOW** | One object contains/references another |

---

## Why Does Composition Exist? (The History)

### The Problem: Inheritance Overuse

Early OOP developers overused inheritance for code reuse:

```java
// BAD: Stack inheriting from Vector (actual JDK mistake!)
public class Stack extends Vector {
    // Stack "IS-A" Vector? Not really...
    // Now Stack has Vector.add(index, item) which breaks LIFO!
}
```

**Problems with inheritance:**
1. Tight coupling — child depends heavily on parent
2. Fragile base class — parent changes break children
3. Wrong abstraction — not everything IS-A something else

### The OOP Solution: Composition

```java
// GOOD: Stack HAS-A List (uses it internally)
public class Stack {
    private List<Object> items = new ArrayList<>();
    
    public void push(Object o) { items.add(o); }
    public Object pop() { return items.remove(items.size() - 1); }
    // No unwanted Vector methods exposed!
}
```

> **Key Insight:** "Favor composition over inheritance" — Gang of Four

---

## IS-A vs HAS-A

```
┌─────────────────────────────────────────────────────────────────┐
│                    RELATIONSHIP TYPES                           │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│   INHERITANCE (IS-A)              COMPOSITION (HAS-A)           │
│   ──────────────────              ───────────────────           │
│                                                                 │
│   Dog IS-A Animal                 Car HAS-A Engine              │
│   Student IS-A Person             House HAS-A Room              │
│   Circle IS-A Shape               Computer HAS-A CPU            │
│                                                                 │
│   ┌───────────┐                   ┌───────────┐                │
│   │  Animal   │                   │    Car    │                │
│   └─────┬─────┘                   │───────────│                │
│         │                         │ ┌───────┐ │                │
│         │ extends                 │ │Engine │ │                │
│         ▼                         │ └───────┘ │                │
│   ┌───────────┐                   └───────────┘                │
│   │    Dog    │                                                 │
│   └───────────┘                                                 │
│                                                                 │
│   Child inherits behavior         Object contains another      │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

## Two Types of HAS-A

### Association vs Composition

```
┌─────────────────────────────────────────────────────────────────┐
│              ASSOCIATION vs COMPOSITION                         │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│   ASSOCIATION (Weak HAS-A)         COMPOSITION (Strong HAS-A)   │
│   ────────────────────────         ─────────────────────────    │
│                                                                 │
│   Objects exist INDEPENDENTLY      Part CANNOT exist without    │
│                                    the whole                    │
│                                                                 │
│   Student ◇──── Teacher            Car ◆──── Engine             │
│       (open diamond)                   (filled diamond)         │
│                                                                 │
│   Teacher exists without Student   Engine created BY Car        │
│   Student just USES a Teacher      Engine dies WITH Car         │
│                                                                 │
│   Lifetime: INDEPENDENT            Lifetime: DEPENDENT          │
│   Ownership: WEAK                  Ownership: STRONG            │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

## Association (Weak HAS-A)

**Definition:** Objects are related but exist independently.

```java
public class Student {
    private Teacher mentor;  // Teacher exists independently
    
    // Teacher passed in from OUTSIDE (not created here)
    public Student(Teacher mentor) {
        this.mentor = mentor;
    }
    
    public void study() {
        System.out.println("Learning from " + mentor.getName());
    }
}

public class Teacher {
    private String name;
    // ... Teacher can exist without any Student
}
```

### Usage

```java
Teacher mathTeacher = new Teacher("Mr. Smith");  // Teacher created first
Student alice = new Student(mathTeacher);         // Student uses the teacher
Student bob = new Student(mathTeacher);           // Same teacher, multiple students

// If alice is garbage collected, mathTeacher still exists!
```

### Visual

```
┌─────────────────────────────────────────────────────────────┐
│                    ASSOCIATION EXAMPLE                      │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│   ┌──────────────┐       ┌──────────────┐                  │
│   │   Student    │       │   Teacher    │                  │
│   │   (Alice)    │──────►│  (Mr. Smith) │                  │
│   └──────────────┘       └──────────────┘                  │
│                                ▲                           │
│   ┌──────────────┐            │                            │
│   │   Student    │────────────┘                            │
│   │    (Bob)     │                                         │
│   └──────────────┘                                         │
│                                                             │
│   Teacher is SHARED and EXISTS INDEPENDENTLY               │
│   Created OUTSIDE of Student                               │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

---

## Composition (Strong HAS-A)

**Definition:** Part cannot exist without the whole. Whole creates and owns the part.

```java
public class Car {
    private Engine engine;  // Engine is part of Car
    
    // Engine created INSIDE Car (not passed in)
    public Car(String model, String engineType) {
        this.engine = new Engine(engineType);  // Created here!
    }
    
    public void start() {
        engine.ignite();
    }
}

public class Engine {
    private String type;
    // Engine is meaningless without a Car
}
```

### Usage

```java
Car tesla = new Car("Model S", "Electric Motor");
tesla.start();

// When tesla is garbage collected, the engine goes with it!
// No way to access engine independently.
```

### Visual

```
┌─────────────────────────────────────────────────────────────┐
│                    COMPOSITION EXAMPLE                      │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│   ┌─────────────────────────────────────────┐              │
│   │                 Car                     │              │
│   │   ┌───────────────────────────────┐     │              │
│   │   │           Engine              │     │              │
│   │   │   (Lives INSIDE the Car)      │     │              │
│   │   └───────────────────────────────┘     │              │
│   │                                         │              │
│   │   Engine created BY Car                 │              │
│   │   Engine cannot be accessed directly    │              │
│   │   Engine dies WHEN Car dies             │              │
│   └─────────────────────────────────────────┘              │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

---

## How to Identify the Relationship

```
"Can the PART exist without the WHOLE?"

   ├── YES ───► ASSOCIATION (Weak)
   │            Example: Student-Teacher
   │            Teacher can exist without Student
   │
   └── NO ────► COMPOSITION (Strong)
                Example: Car-Engine
                This Engine belongs to this Car
```

### Quick Decision Table

| Relationship | Part Created | Part's Lifetime | Example |
|--------------|:------------:|:---------------:|---------|
| **Association** | Outside | Independent | Student→Teacher |
| **Composition** | Inside | Dependent | Car→Engine |

---

## Composition Over Inheritance

### The Principle

> **"Favor composition over inheritance."**
> — Gang of Four (Design Patterns book)

### Why?

| Aspect | Inheritance | Composition |
|--------|-------------|-------------|
| Coupling | Tight (child depends on parent) | Loose (can swap parts) |
| Flexibility | Fixed at compile time | Can change at runtime |
| Encapsulation | Breaks it (child sees parent internals) | Maintains it |
| Reusability | Limited to class hierarchy | Any compatible object |

### Example: Duck Problem

```java
// BAD: Inheritance approach
class Bird {
    void fly() { System.out.println("Flying"); }
}

class Duck extends Bird { }  // Ducks can fly ✅
class Penguin extends Bird { }  // Penguins... fly? 💥 NO!
```

```java
// GOOD: Composition approach
interface FlyBehavior {
    void fly();
}

class CanFly implements FlyBehavior {
    public void fly() { System.out.println("Flying!"); }
}

class CannotFly implements FlyBehavior {
    public void fly() { System.out.println("Cannot fly"); }
}

class Bird {
    private FlyBehavior flyBehavior;  // HAS-A behavior
    
    public Bird(FlyBehavior fb) {
        this.flyBehavior = fb;
    }
    
    public void fly() {
        flyBehavior.fly();  // Delegate to composed object
    }
}

// Usage
Bird duck = new Bird(new CanFly());        // Duck can fly
Bird penguin = new Bird(new CannotFly());  // Penguin cannot
```

---

## When to Use Composition

### ✅ Good Use Cases

| Scenario | Example |
|----------|---------|
| Complex objects from simpler parts | `Computer` has `CPU`, `Memory`, `Storage` |
| Runtime flexibility | Swap `FlyBehavior` at runtime |
| Avoiding inheritance hierarchy | `Stack` using `List` internally |
| Hiding implementation details | `OrderService` uses `PaymentProcessor` |

### Decision Flowchart

```
Is there a clear IS-A relationship?
       │
       ├── YES ───► Is it a stable abstraction?
       │                  │
       │                  ├── YES ───► Inheritance OK
       │                  │
       │                  └── NO ────► Consider Composition
       │
       └── NO ────► ✅ Use Composition
```

---

## When NOT to Use (Anti-Patterns)

### ❌ Over-Composition (Too Many Small Objects)

```java
// BAD: Everything is composed, even trivial things
class Person {
    private Name name;        // Just use String!
    private Age age;          // Just use int!
    private Height height;    // Just use double!
}

// GOOD: Compose meaningful objects only
class Person {
    private String name;
    private int age;
    private Address address;  // ✅ Address is complex, worth composing
}
```

### ❌ Circular Dependencies

```java
// BAD: A owns B, B owns A
class A {
    private B b = new B();
}

class B {
    private A a = new A();  // 💥 Infinite loop!
}
```

---

## Common Gotchas

### 1. Association vs Composition Detection

```java
// This is COMPOSITION (created inside)
class Car {
    private Engine engine = new Engine();  // ← Created here
}

// This is ASSOCIATION (passed from outside)
class Car {
    private Engine engine;
    
    public Car(Engine e) {
        this.engine = e;  // ← Passed in
    }
}
```

### 2. Null References in Association

```java
class Student {
    private Teacher mentor;  // Could be null!
    
    public void study() {
        if (mentor != null) {  // Must check!
            System.out.println("Learning from " + mentor.getName());
        }
    }
}
```

### 3. Immutable Composition

```java
class Car {
    private final Engine engine;  // Can't change after creation
    
    public Car(String type) {
        this.engine = new Engine(type);
    }
    
    // No setEngine() method - engine is fixed!
}
```

---

## Dependency Injection (Preview)

Composition enables **Dependency Injection** — a key design pattern:

```java
// Without DI: Tight coupling
class OrderService {
    private PaymentProcessor pp = new StripeProcessor();  // Hard-coded!
}

// With DI: Loose coupling via composition
class OrderService {
    private PaymentProcessor pp;
    
    public OrderService(PaymentProcessor pp) {
        this.pp = pp;  // Injected from outside
    }
}

// Usage - swap implementations easily
OrderService stripeOrder = new OrderService(new StripeProcessor());
OrderService paypalOrder = new OrderService(new PayPalProcessor());
```

---

## Project Demo Structure

```
c7_composition/
├── CompositionNotes.md  ← You are here
├── Main.java            ← Entry point with demos
├── association/
│   ├── Teacher.java     ← Independent object
│   └── Student.java     ← Uses Teacher
└── composition/
    ├── Car.java         ← Whole (creates Engine)
    └── Engine.java      ← Part (owned by Car)
```

---

## Summary

| Concept | Key Point |
|---------|-----------|
| **HAS-A** | Object contains/references another object |
| **Association** | Weak ownership, independent lifetimes |
| **Composition** | Strong ownership, dependent lifetimes |
| **vs Inheritance** | More flexible, less coupled |
| **Creation** | Association: outside, Composition: inside |

> **Remember:** "Favor composition over inheritance" — but use inheritance when IS-A is truly appropriate.

---

## What's Next?

Congratulations! 🎉 You've completed the OOP fundamentals module.

**Summary of p1_oops:**

| Chapter | Topic | Key Takeaway |
|---------|-------|--------------|
| c0 | Introduction | OOP = Abstraction via 3 pillars |
| c1 | Access Modifiers | Control visibility (encapsulation) |
| c2 | Constructors | Object initialization & memory |
| c3 | Inheritance | IS-A relationships, code reuse |
| c4 | Polymorphism | Many forms, dynamic dispatch |
| c5 | Interfaces | CAN-DO contracts |
| c6 | Abstract Classes | Templates with forced implementation |
| c7 | Composition | HAS-A relationships, flexibility |

**Next Module:** → [p2: Concurrency](../../p2_concurrency/) — Multithreading and parallel programming
