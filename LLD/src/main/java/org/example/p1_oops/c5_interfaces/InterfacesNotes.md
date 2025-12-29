# Interfaces Notes

An interface is a **contract**. It defines WHAT a class must do, not HOW.

## Syntax
```java
public interface Flyable {
    void fly(); // Implicitly public and abstract
}
```

## Implementing an Interface
```java
public class Bird implements Flyable {
    @Override
    public void fly() {
        System.out.println("Bird is flying.");
    }
}
```

## Multiple Interface Implementation
Java does NOT allow multiple class inheritance, but a class CAN implement **multiple interfaces**.

```java
public class Duck implements Flyable, Swimmable {
    // Must implement fly() and swim()
}
```

## Interface as a Type
```java
Flyable f = new Bird(); // Polymorphism
f.fly();
```

## Key Differences: Interface vs Abstract Class

| Feature | Interface | Abstract Class |
| :--- | :--- | :--- |
| Methods | All abstract (before Java 8) | Can have concrete methods |
| Fields | `public static final` only | Any fields |
| Inheritance | `implements` (multiple allowed) | `extends` (single only) |
| Constructor | None | Can have |
