# Abstract Classes Notes

An abstract class is a **partially complete** class that cannot be instantiated.

## Syntax
```java
public abstract class Shape {
    public abstract double getArea(); // Abstract method
    public void display() { ... }     // Concrete method
}
```

## Key Points
- Use `abstract` keyword on class and methods.
- Abstract methods have NO body.
- Can have constructors, fields, and concrete methods.
- Child class MUST implement all abstract methods (or be abstract itself).

## Abstract Class vs Interface

| Feature | Abstract Class | Interface |
| :--- | :--- | :--- |
| Methods | Abstract + Concrete | Abstract only (pre-Java 8) |
| Fields | Any type | `public static final` only |
| Inheritance | `extends` (single) | `implements` (multiple) |
| Constructor | Yes | No |
| Use Case | **IS-A** with shared code | **CAN-DO** (capability) |

## When to Use?
- **Abstract Class**: When you have shared code between related classes.
- **Interface**: When unrelated classes need to share a capability.
