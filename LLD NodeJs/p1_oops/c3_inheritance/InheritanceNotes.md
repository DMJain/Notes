# Inheritance & Polymorphism Notes (JavaScript)

## Inheritance

Inheritance allows a Child class to acquire fields and methods from a Parent class.

### JavaScript Syntax
```javascript
class Child extends Parent {
    constructor(args) {
        super(parentArgs); // Must call parent constructor first
        this.childProperty = value;
    }
}
```

### Key Differences from Java
- **Keyword**: `extends` (same as Java)
- **Constructor Chaining**: Must call `super()` before accessing `this` in constructor
- **No @Override**: JavaScript doesn't have an `@Override` annotation

## Polymorphism

"Many forms". An object can behave differently based on its actual type.

### Runtime Polymorphism (Method Overriding)
- Child provides its own implementation of a Parent method.
- JavaScript determines the method at **runtime** based on the actual object.

```javascript
class User {
    solveProblem() { console.log("Generic"); }
}

class Student extends User {
    solveProblem() { console.log("DSA"); } // Overrides parent
}

const u = new Student();
u.solveProblem(); // Calls Student's solveProblem()
```

## Type Checking in JavaScript

Since JavaScript is dynamically typed, use `instanceof` for type checking:

```javascript
const student = new Student("Alice", "alice@email.com", "Batch1");

console.log(student instanceof Student); // true
console.log(student instanceof User);    // true
console.log(student instanceof Mentor);  // false
```

## Differences: Java vs JavaScript

| Aspect | Java | JavaScript |
| :--- | :--- | :--- |
| **Static Typing** | Yes | No |
| **@Override** | Yes (annotation) | No |
| **Method Resolution** | Compile + Runtime | Runtime only |
| **Type Errors** | Compile-time | Runtime |
| **instanceof** | Works | Works |

## Negative Cases (What is NOT allowed)

| Scenario | Java | JavaScript |
| :--- | :--- | :--- |
| Child Ref = Parent Object | COMPILE ERROR | Works (but missing props) |
| Parent Ref accessing Child Field | COMPILE ERROR | Works (returns undefined) |

### JavaScript Behavior
```javascript
const user = new User("Test", "test@email.com");
console.log(user.batchName); // undefined (no error, just no property)

// Use instanceof to safely check type before accessing child properties
if (user instanceof Student) {
    console.log(user.batchName);
}
```
