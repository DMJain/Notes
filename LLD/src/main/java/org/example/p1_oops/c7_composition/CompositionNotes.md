# Composition & Association Notes

Object relationships describe how objects are connected.

## Association (Weak HAS-A)
- Objects are related but **exist independently**.
- One object uses another but doesn't own it.
- Example: `Student` has a `Teacher`, but Teacher exists on its own.

```java
class Student {
    private Teacher teacher; // Passed from outside
    public Student(Teacher t) { this.teacher = t; }
}
```

## Composition (Strong HAS-A)
- Part **cannot exist** without the whole.
- Whole object **creates and owns** the part.
- Example: `Car` has an `Engine`, Engine dies when Car is destroyed.

```java
class Car {
    private Engine engine;
    public Car() { this.engine = new Engine(); } // Created inside
}
```

## Quick Comparison

| Aspect | Association | Composition |
| :--- | :--- | :--- |
| Ownership | Weak | Strong |
| Lifetime | Independent | Dependent |
| Creation | Outside (passed in) | Inside (created) |
| Example | Student-Teacher | Car-Engine |

## How to Identify?
- **"Can the part exist without the whole?"**
  - Yes → Association
  - No → Composition
