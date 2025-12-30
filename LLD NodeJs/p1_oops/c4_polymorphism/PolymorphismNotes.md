# Polymorphism Notes (JavaScript)

"Poly" = Many, "Morph" = Forms. An object can take many forms.

## 1. Compile-Time Polymorphism (Method Overloading)

**JavaScript does NOT support true method overloading.**

In Java, you can have multiple methods with the same name but different parameters. In JavaScript, if you define the same method name twice, the second one **overwrites** the first.

### Workarounds in JavaScript

1. **Rest Parameters with Type Checking**:
```javascript
add(...args) {
    if (args.length === 2) {
        return args[0] + args[1];
    } else if (args.length === 3) {
        return args[0] + args[1] + args[2];
    }
}
```

2. **Default Parameters**:
```javascript
add(a, b, c = 0) {
    return a + b + c;
}
```

3. **Object Destructuring**:
```javascript
add({ a, b, c = 0 }) {
    return a + b + c;
}
```

4. **Separate Method Names** (most idiomatic):
```javascript
addTwo(a, b) { return a + b; }
addThree(a, b, c) { return a + b + c; }
```

## 2. Runtime Polymorphism (Method Overriding)

- **Child provides its own implementation** of a Parent method.
- Decision made at **Runtime** (Dynamic Method Dispatch).
- Example: `Animal.speak()` vs `Dog.speak()`.

```javascript
class Animal {
    speak() { console.log("Generic sound"); }
}

class Dog extends Animal {
    speak() { console.log("Woof!"); }  // Overrides parent
}
```

## Key Concept: Dynamic Method Dispatch

```javascript
const a = new Dog(); // Variable points to Dog instance
a.speak();           // Calls Dog's speak() at RUNTIME
```

## Comparison: Overloading in Java vs JavaScript

| Aspect | Java | JavaScript |
| :--- | :--- | :--- |
| **Overloading** | Supported (different params) | NOT supported |
| **When** | Compile-time | N/A |
| **Workaround** | N/A | Rest params, type checking, different names |

## Comparison: Overriding

| Aspect | Java | JavaScript |
| :--- | :--- | :--- |
| **Overriding** | Supported | Supported |
| **@Override** | Annotation available | No annotation |
| **Resolution** | Runtime | Runtime |
| **super keyword** | Yes | Yes |

## Calling Parent Method

```javascript
class Dog extends Animal {
    speak() {
        super.speak(); // Calls Animal's speak()
        console.log("Woof!");
    }
}
```
