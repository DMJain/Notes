# Inheritance & Polymorphism Notes

## Inheritance
Inheritance allows a Child class to acquire fields and methods from a Parent class.
- **Keyword**: `extends`
- **Constructor Chaining**: Child must call Parent constructor using `super()`. If not explicit, Java auto-inserts `super()`.

## Polymorphism
"Many forms". An object can behave differently based on its actual type.

### Runtime Polymorphism (Method Overriding)
- Child provides its own implementation of a Parent method.
- **Annotation**: `@Override` (Good practice).
- **Upcasting**: `Parent p = new Child();`
- **Dynamic Method Dispatch**: The actual method called is decided at **Runtime** based on the object type, not the reference type.

```java
User u = new Student(); // Upcasting
u.solveProblem(); // Calls Student's solveProblem()
```

## Negative Cases (What is NOT allowed)

| Scenario | Code Example | Error |
| :--- | :--- | :--- |
| Child Ref = Parent Object | `Student s = new User();` | COMPILE ERROR |
| Parent Ref accessing Child Field | `User u = new Student(); u.batchName;` | COMPILE ERROR |
