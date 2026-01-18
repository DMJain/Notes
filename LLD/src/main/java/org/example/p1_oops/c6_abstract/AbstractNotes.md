# Abstract Classes in Java

## Prerequisites
- [c0: OOP Introduction](../c0_introduction/OOPIntroduction.md) — Abstraction as the core principle
- [c3: Inheritance](../c3_inheritance/InheritanceNotes.md) — Parent-child relationships
- [c5: Interfaces](../c5_interfaces/InterfacesNotes.md) — Compare with interfaces

---

## What You'll Learn
| Question | Answer |
|----------|--------|
| **WHAT** | A partially complete class that cannot be instantiated |
| **WHY** | To provide a template with both shared code and forced customization |
| **WHEN** | When related classes share code AND need to implement specific behaviors |
| **HOW** | Using the `abstract` keyword |

---

## Why Do Abstract Classes Exist? (The History)

### The Problem: Two Extremes

```
┌─────────────────────────────────────────────────────────────┐
│                   THE MIDDLE GROUND PROBLEM                 │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│   INTERFACE                          CONCRETE CLASS         │
│   ─────────                          ──────────────         │
│                                                             │
│   Only abstract methods              All methods complete   │
│   (Pre-Java 8)                                              │
│                                                             │
│   "Define WHAT, not HOW"            "Define everything"     │
│                                                             │
│   No shared implementation          Can share code BUT      │
│                                     no forced overrides     │
│                                                             │
│                     NEED: BOTH!                             │
│             Some shared + Some forced                       │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

### The OOP Solution: Abstract Classes

```java
public abstract class Shape {
    protected String name;
    
    // Concrete method - shared by all shapes
    public void display() {
        System.out.println("Shape: " + name + ", Area: " + getArea());
    }
    
    // Abstract method - MUST be implemented by each shape
    public abstract double getArea();  // No body!
}

public class Circle extends Shape {
    private double radius;
    
    @Override
    public double getArea() {
        return Math.PI * radius * radius;  // Circle's specific calculation
    }
}
```

> **Key Insight:** Abstract classes are templates: some assembly required.

---

## Abstract Class = Template

```
┌─────────────────────────────────────────────────────────────┐
│              ABSTRACT CLASS AS TEMPLATE                     │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│   ┌─────────────────────────────────────────┐              │
│   │      abstract class Shape               │              │
│   │     ─────────────────────────           │              │
│   │                                         │              │
│   │   String name ─────────────── SHARED    │              │
│   │   void display() ──────────── SHARED    │              │
│   │                                         │              │
│   │   abstract double getArea() ── FORCED   │              │
│   │         (No implementation)             │              │
│   │                                         │              │
│   │   "I provide the template,              │              │
│   │    you fill in the blanks"              │              │
│   └─────────────────────────────────────────┘              │
│                        │                                    │
│            ┌───────────┼───────────┐                       │
│            │           │           │                       │
│            ▼           ▼           ▼                       │
│      ┌─────────┐ ┌─────────┐ ┌─────────┐                  │
│      │ Circle  │ │Rectangle│ │Triangle │                  │
│      │─────────│ │─────────│ │─────────│                  │
│      │getArea()│ │getArea()│ │getArea()│                  │
│      │ πr²     │ │  w × h  │ │ ½bh     │                  │
│      └─────────┘ └─────────┘ └─────────┘                  │
│                                                             │
│   Each MUST implement getArea() — it's forced!             │
│   All INHERIT display() — it's shared!                     │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

---

## Abstract Class Rules

```
┌────────────────────────────────────────────────────────────┐
│                   ABSTRACT CLASS RULES                     │
├────────────────────────────────────────────────────────────┤
│                                                            │
│   1. Declared with 'abstract' keyword                     │
│   2. CANNOT be instantiated (new Shape() ❌)              │
│   3. CAN have:                                            │
│      • Abstract methods (no body)                         │
│      • Concrete methods (with body)                       │
│      • Constructors (called by children via super())      │
│      • Fields (any access modifier)                       │
│   4. Child class MUST implement all abstract methods      │
│      (UNLESS child is also abstract)                      │
│                                                            │
└────────────────────────────────────────────────────────────┘
```

---

## Complete Example

```java
public abstract class Shape {
    protected String name;
    
    // Constructor - called by children
    public Shape(String name) {
        this.name = name;
    }
    
    // Concrete method - shared implementation
    public void display() {
        System.out.println(name + " has area: " + getArea());
    }
    
    // Abstract method - must be implemented
    public abstract double getArea();
}

public class Circle extends Shape {
    private double radius;
    
    public Circle(double radius) {
        super("Circle");  // Call parent constructor
        this.radius = radius;
    }
    
    @Override
    public double getArea() {
        return Math.PI * radius * radius;
    }
}

public class Rectangle extends Shape {
    private double width, height;
    
    public Rectangle(double w, double h) {
        super("Rectangle");
        this.width = w;
        this.height = h;
    }
    
    @Override
    public double getArea() {
        return width * height;
    }
}
```

---

## Abstract Class vs Interface

```
┌──────────────────────────────────────────────────────────────┐
│            ABSTRACT CLASS vs INTERFACE                       │
├──────────────────────────────────────────────────────────────┤
│                                                              │
│   ABSTRACT CLASS                   INTERFACE                 │
│   ──────────────                   ─────────                 │
│                                                              │
│   IS-A (Identity)                  CAN-DO (Capability)       │
│   "Circle IS-A Shape"              "Duck CAN fly"            │
│                                                              │
│   Single inheritance               Multiple implementation   │
│   (one parent only)                (as many as you want)     │
│                                                              │
│   Has state (fields)               Constants only*           │
│   Has constructors                 No constructors           │
│                                                              │
│   Abstract + Concrete methods      Abstract (+ default)*     │
│                                                              │
│   *Since Java 8, interfaces can have default methods         │
│                                                              │
└──────────────────────────────────────────────────────────────┘
```

### When to Use Which?

| Use Case | Abstract Class | Interface |
|----------|:--------------:|:---------:|
| Related classes sharing code | ✅ | ❌ |
| Multiple capabilities on one class | ❌ | ✅ |
| Template with common + forced parts | ✅ | ⚠️ (default methods) |
| State/fields needed | ✅ | ❌ |
| Constructor logic needed | ✅ | ❌ |
| Unrelated classes, same capability | ❌ | ✅ |

---

## Template Method Pattern

Abstract classes naturally support the **Template Method** design pattern:

```java
public abstract class DataProcessor {
    // Template method - defines the algorithm skeleton
    public final void process() {
        readData();      // Concrete
        transformData(); // Abstract - child decides
        writeData();     // Concrete
    }
    
    private void readData() {
        System.out.println("Reading from source...");
    }
    
    protected abstract void transformData();  // Child implements!
    
    private void writeData() {
        System.out.println("Writing to destination...");
    }
}

public class CSVProcessor extends DataProcessor {
    @Override
    protected void transformData() {
        System.out.println("Transforming CSV format");
    }
}

public class JSONProcessor extends DataProcessor {
    @Override
    protected void transformData() {
        System.out.println("Transforming JSON format");
    }
}
```

```
┌─────────────────────────────────────────────────────────────┐
│              TEMPLATE METHOD PATTERN                        │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│   process() {         ← Defined in abstract class          │
│       readData();     ← Step 1: Fixed                      │
│       transformData();← Step 2: VARIES by child            │
│       writeData();    ← Step 3: Fixed                      │
│   }                                                         │
│                                                             │
│   Children only implement the varying step!                 │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

---

## When to Use Abstract Classes

### ✅ Good Use Cases

| Scenario | Example |
|----------|---------|
| Shared code + forced methods | `Shape` with `display()` shared, `getArea()` forced |
| Template algorithms | `DataProcessor` template method pattern |
| Base class with state | `Animal` with `name`, `age` fields |
| Factory method pattern | Abstract factory with common logic |

### Decision Flowchart

```
Do classes share a clear IS-A relationship?
       │
       ├── NO ───► Consider Interface
       │
       └── YES
            │
            └── Need shared implementation code?
                    │
                    ├── NO ───► Interface might work
                    │
                    └── YES ───► ✅ Use Abstract Class
```

---

## When NOT to Use Abstract Classes (Anti-Patterns)

### ❌ Abstract Class with Only Abstract Methods

```java
// BAD: All abstract = should be an interface
public abstract class Printable {
    public abstract void print();
    public abstract void scan();
}

// GOOD: Use interface instead
public interface Printable {
    void print();
    void scan();
}
```

### ❌ Deep Abstract Hierarchies

```java
// BAD: Too many levels of abstraction
abstract class Vehicle { }
abstract class MotorVehicle extends Vehicle { }
abstract class FourWheeler extends MotorVehicle { }
abstract class Car extends FourWheeler { }
class Sedan extends Car { }  // Finally concrete!

// Hard to understand, rigid, hard to change
```

### ❌ Using Abstract Class to Force Interface Contract

```java
// BAD: Using abstract class just for contract
public abstract class Repository {
    public abstract void save(Object o);
    public abstract Object find(int id);
}

// GOOD: Interface is cleaner for pure contracts
public interface Repository {
    void save(Object o);
    Object find(int id);
}
```

---

## Common Gotchas

### 1. Can't Instantiate Abstract Classes

```java
Shape shape = new Shape("Test");  // ❌ COMPILE ERROR!
Shape shape = new Circle(5.0);    // ✅ Create concrete subclass
```

### 2. Must Implement ALL Abstract Methods

```java
public abstract class Shape {
    public abstract double getArea();
    public abstract double getPerimeter();
}

public class Circle extends Shape {
    public double getArea() { return ...; }
    // ❌ COMPILE ERROR: Must also implement getPerimeter()
}

// Unless Circle is also abstract:
public abstract class Circle extends Shape {
    public double getArea() { return ...; }
    // OK - Circle is abstract, so getPerimeter() can stay abstract
}
```

### 3. Abstract Classes Can Have Constructors

```java
public abstract class Shape {
    protected String name;
    
    public Shape(String name) {  // ✅ Valid!
        this.name = name;
    }
}

// Called by children:
public class Circle extends Shape {
    public Circle(double radius) {
        super("Circle");  // Calls abstract class constructor
    }
}
```

---

## Project Demo Structure

```
c6_abstract/
├── AbstractNotes.md  ← You are here
├── Main.java         ← Entry point with demos
├── base/
│   └── Shape.java    ← Abstract class
└── impl/
    ├── Circle.java   ← Concrete implementation
    └── Rectangle.java ← Another implementation
```

---

## Summary

| Concept | Key Point |
|---------|-----------|
| **Abstract Class** | Partial template, cannot instantiate |
| **abstract method** | No body, must be implemented by children |
| **Concrete method** | Has body, shared by all children |
| **Constructor** | Can exist, called via super() |
| **vs Interface** | Abstract = IS-A + shared code; Interface = CAN-DO |

> **Remember:** Abstract classes are templates — they provide some pieces, and force children to provide the rest.

---

## Next Chapter
→ [c7: Composition](../c7_composition/CompositionNotes.md) — HAS-A relationships and alternatives to inheritance
