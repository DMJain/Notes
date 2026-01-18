# Polymorphism in Java

## Prerequisites
- [c0: OOP Introduction](../c0_introduction/OOPIntroduction.md) — Understanding that polymorphism is one of the 3 pillars
- [c3: Inheritance](../c3_inheritance/InheritanceNotes.md) — Required for method overriding

---

## What You'll Learn
| Question | Answer |
|----------|--------|
| **WHAT** | "Many forms" — same interface, different behaviors |
| **WHY** | Flexibility, extensibility, and clean code |
| **WHEN** | When you want uniform treatment of different types |
| **HOW** | Method Overloading and Method Overriding |

---

## Why Does Polymorphism Exist? (The History)

### The Problem: Rigid Code

Without polymorphism, you need explicit type checks everywhere:

```java
// BAD: Without polymorphism
void makeSound(Object obj) {
    if (obj instanceof Dog) {
        ((Dog) obj).bark();
    } else if (obj instanceof Cat) {
        ((Cat) obj).meow();
    } else if (obj instanceof Bird) {
        ((Bird) obj).chirp();
    }
    // Adding a new animal? Change this EVERY time! 💥
}
```

### The OOP Solution

```java
// GOOD: With polymorphism
void makeSound(Animal animal) {
    animal.speak();  // ✅ One line handles ALL animals
}

// Adding new animal? Just create a new class, no changes to makeSound!
```

> **Key Insight:** Polymorphism lets you write "future-proof" code that works with types that don't exist yet.

---

## The Two Types of Polymorphism

```
┌─────────────────────────────────────────────────────────────────┐
│                    POLYMORPHISM TYPES                           │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│   ┌─────────────────────────┐    ┌─────────────────────────┐   │
│   │   COMPILE-TIME          │    │   RUNTIME               │   │
│   │   (Static)              │    │   (Dynamic)             │   │
│   ├─────────────────────────┤    ├─────────────────────────┤   │
│   │                         │    │                         │   │
│   │   Method OVERLOADING    │    │   Method OVERRIDING     │   │
│   │                         │    │                         │   │
│   │   Same name,            │    │   Same signature,       │   │
│   │   DIFFERENT params      │    │   DIFFERENT class       │   │
│   │                         │    │                         │   │
│   │   Decided by COMPILER   │    │   Decided at RUNTIME    │   │
│   │                         │    │                         │   │
│   │   Example:              │    │   Example:              │   │
│   │   add(int, int)         │    │   Animal.speak()        │   │
│   │   add(double, double)   │    │   Dog.speak()           │   │
│   │                         │    │   Cat.speak()           │   │
│   └─────────────────────────┘    └─────────────────────────┘   │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

## 1. Method Overloading (Compile-Time)

**Definition:** Same method name, different parameter lists (type, number, or order).

```java
public class Calculator {
    public int add(int a, int b) {
        return a + b;
    }
    
    public int add(int a, int b, int c) {  // Different number of params
        return a + b + c;
    }
    
    public double add(double a, double b) {  // Different param types
        return a + b;
    }
}
```

### Overloading Resolution

```
┌─────────────────────────────────────────────────────────────┐
│            COMPILE-TIME METHOD SELECTION                    │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│   calc.add(5, 10);                                         │
│         │                                                   │
│         ▼                                                   │
│   ┌─────────────────────────────────────────────────┐      │
│   │            COMPILER CHECKS                       │      │
│   │                                                  │      │
│   │   add(int, int)    ✅ MATCH! Calls this one     │      │
│   │   add(int,int,int) ❌ Wrong count               │      │
│   │   add(double,double) ❌ Wrong type              │      │
│   │                                                  │      │
│   │   Decision made at COMPILE time                  │      │
│   │   (Before program runs)                          │      │
│   └─────────────────────────────────────────────────┘      │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

### What CAN and CAN'T Differentiate Methods

| Criterion | Valid for Overloading? |
|-----------|:----------------------:|
| Parameter count | ✅ Yes |
| Parameter types | ✅ Yes |
| Parameter order | ✅ Yes |
| Return type ONLY | ❌ No! |
| Access modifier | ❌ No |

```java
// ❌ COMPILE ERROR: Same signature, different return type
public int add(int a, int b) { return a + b; }
public double add(int a, int b) { return a + b; }  // NOT valid!
```

---

## 2. Method Overriding (Runtime)

**Definition:** Child class provides its own implementation of a parent method.

```java
public class Animal {
    public void speak() {
        System.out.println("Some generic sound");
    }
}

public class Dog extends Animal {
    @Override  // Good practice - compiler checks this IS an override
    public void speak() {
        System.out.println("Woof!");
    }
}

public class Cat extends Animal {
    @Override
    public void speak() {
        System.out.println("Meow!");
    }
}
```

### The Magic: Dynamic Method Dispatch

```
┌─────────────────────────────────────────────────────────────────┐
│                DYNAMIC METHOD DISPATCH                          │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│   Animal a = new Dog();  // Compile-time type: Animal          │
│                          // Runtime type: Dog                   │
│   a.speak();                                                    │
│                                                                 │
│   ┌─────────────────────────────────────────────────────────┐  │
│   │                    AT RUNTIME                            │  │
│   │                                                          │  │
│   │   JVM asks: "What's the ACTUAL object?"                  │  │
│   │             ────────────────────────────                 │  │
│   │                        │                                 │  │
│   │                        ▼                                 │  │
│   │                ┌─────────────┐                           │  │
│   │                │    Dog      │ ← This is the real object │  │
│   │                │  speak()    │                           │  │
│   │                └─────────────┘                           │  │
│   │                        │                                 │  │
│   │                        ▼                                 │  │
│   │              Prints: "Woof!"                             │  │
│   │                                                          │  │
│   └─────────────────────────────────────────────────────────┘  │
│                                                                 │
│   Reference type (Animal) → Controls WHAT you can call         │
│   Object type (Dog) → Controls WHICH version runs              │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

### Rules for Valid Overriding

| Rule | Explanation |
|------|-------------|
| Same method name | Must match exactly |
| Same parameter list | Types and order must match |
| Return type | Same or covariant (subtype) |
| Access level | Same or MORE permissive (private → protected ✅, public → protected ❌) |
| No `final` methods | Cannot override final methods |
| No `static` methods | Static methods are HIDDEN, not overridden |

---

## Polymorphism in Action

```java
// Beautiful polymorphic code
Animal[] zoo = { new Dog(), new Cat(), new Bird() };

for (Animal animal : zoo) {
    animal.speak();  // Each animal speaks in its own way!
}

// Output:
// Woof!
// Meow!
// Chirp!
```

### Why This is Powerful

```
┌─────────────────────────────────────────────────────────────┐
│           EXTENSIBILITY WITHOUT MODIFICATION                │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│   BEFORE: You have Dog, Cat, Bird                          │
│                                                             │
│   // This code ALREADY works with any Animal               │
│   for (Animal a : animals) {                               │
│       a.speak();                                           │
│   }                                                         │
│                                                             │
│   AFTER: You add Snake, Fish, Elephant                     │
│                                                             │
│   // SAME code still works! Zero changes!                  │
│   for (Animal a : animals) {                               │
│       a.speak();  // ✅ Works with new animals too         │
│   }                                                         │
│                                                             │
│   This is the OPEN/CLOSED principle in action:             │
│   Open for extension, Closed for modification              │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

---

## Overloading vs Overriding Comparison

| Aspect | Overloading | Overriding |
|--------|-------------|------------|
| **When decided** | Compile-time | Runtime |
| **Where** | Same class | Parent-Child |
| **Method signature** | DIFFERENT params | SAME signature |
| **Return type** | Can be different | Same or covariant |
| **Keyword** | None | `@Override` (recommended) |
| **Also called** | Static polymorphism | Dynamic polymorphism |

---

## When to Use Polymorphism

### ✅ Good Use Cases

| Scenario | Example |
|----------|---------|
| Collection of related types | `List<Shape>` containing Circle, Rectangle |
| Plugin/extension systems | `Processor.process()` with different implementations |
| Strategy pattern | `PaymentMethod.pay()` for CreditCard, PayPal |
| Uniform APIs | `save(Object)` that handles any type |

### Decision: Overloading vs Overriding

```
Same class with similar operations but different inputs?
   → Use OVERLOADING (add(int,int) vs add(double,double))

Different classes with same behavior signature?
   → Use OVERRIDING (Dog.speak() vs Cat.speak())
```

---

## When NOT to Use (Anti-Patterns)

### ❌ Overloading with Confusing Signatures

```java
// BAD: Too similar, easy to call wrong one
public void process(String s) { }
public void process(Object o) { }  // Both accept String!

// What gets called?
process("hello");  // String version (more specific wins)
process((Object) "hello");  // Object version 💥 Confusing!
```

### ❌ Override That Changes Behavior Radically

```java
// BAD: Violates Liskov Substitution Principle
class Rectangle {
    void setDimensions(int w, int h) {
        this.width = w;
        this.height = h;
    }
}

class Square extends Rectangle {
    @Override
    void setDimensions(int w, int h) {
        // Forces w == h, breaking expected behavior! 💥
        this.width = w;
        this.height = w;  // Ignores h!
    }
}
```

---

## Common Gotchas

### 1. `@Override` Catches Mistakes

```java
class Child extends Parent {
    // Typo! This creates a NEW method, doesn't override
    public void precess() { }  // Should be process()
    
    @Override
    public void precess() { }  // ❌ COMPILE ERROR: No method to override
}
```

### 2. Static Methods Don't Override

```java
class Parent {
    static void greet() { System.out.println("Parent"); }
}

class Child extends Parent {
    static void greet() { System.out.println("Child"); }  // HIDING!
}

Parent p = new Child();
p.greet();  // Prints "Parent"! Reference type wins for static.
```

### 3. Private Methods Can't Be Overridden

```java
class Parent {
    private void secret() { }
}

class Child extends Parent {
    // This is a NEW method, not an override (private not inherited)
    private void secret() { }
}
```

---

## The Power of Polymorphism: Real Example

```java
// Without polymorphism: Nightmare
public void printReceipt(Object payment) {
    if (payment instanceof CreditCard) {
        CreditCard cc = (CreditCard) payment;
        System.out.println("Paid $" + cc.getAmount() + " via Credit Card");
    } else if (payment instanceof PayPal) {
        PayPal pp = (PayPal) payment;
        System.out.println("Paid $" + pp.getAmount() + " via PayPal");
    }
    // Add more payment types? More if-else! 💥
}

// With polymorphism: Beautiful
public void printReceipt(PaymentMethod payment) {
    System.out.println("Paid $" + payment.getAmount() + 
                       " via " + payment.getName());
}
// Works with ANY PaymentMethod, forever! ✅
```

---

## Project Demo Structure

```
c4_polymorphism/
├── PolymorphismNotes.md  ← You are here
├── Main.java             ← Entry point with all demos
├── overloading/
│   └── Calculator.java   ← Overloading examples
└── overriding/
    ├── Animal.java       ← Parent class
    ├── Dog.java          ← Child with override
    └── Cat.java          ← Another child
```

---

## Summary

| Concept | Key Point |
|---------|-----------|
| **Polymorphism** | "Many forms" — same interface, different behaviors |
| **Overloading** | Same name, different params (compile-time) |
| **Overriding** | Same signature, different class (runtime) |
| **Dynamic Dispatch** | JVM picks method based on ACTUAL object type |
| **@Override** | Annotation for safety (catches typos) |

> **Remember:** 
> - Reference type → What you CAN call
> - Object type → Which VERSION runs

---

## Next Chapter
→ [c5: Interfaces](../c5_interfaces/InterfacesNotes.md) — Contracts for behavior
