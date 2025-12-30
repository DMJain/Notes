# Interfaces Notes (JavaScript)

An interface is a **contract**. It defines WHAT a class must do, not HOW.

**JavaScript does NOT have native interfaces** like Java. Instead, we use:
1. **Duck Typing**: "If it walks like a duck and quacks like a duck, it's a duck"
2. **Abstract Classes**: Classes that throw errors for unimplemented methods
3. **TypeScript**: For compile-time interface checking

## Simulating Interfaces

### Approach 1: Duck Typing (Most Common)
```javascript
// Just implement the methods you need
class Bird {
    fly() { console.log("Flying!"); }
}

// No formal interface, just check if method exists
function makeFly(obj) {
    if (typeof obj.fly === 'function') {
        obj.fly();
    }
}
```

### Approach 2: Abstract Class Pattern
```javascript
class Flyable {
    fly() {
        throw new Error("Method 'fly()' must be implemented");
    }
}

class Bird extends Flyable {
    fly() { console.log("Flying!"); } // Must override
}
```

### Approach 3: Interface Checking Function
```javascript
Flyable.checkInterface = function(obj) {
    if (typeof obj.fly !== 'function') {
        throw new TypeError("Object must implement fly()");
    }
};
```

## Multiple Interface Implementation

Java allows `implements InterfaceA, InterfaceB`. JavaScript doesn't support multiple inheritance.

### Workarounds:

1. **Duck Typing**: Just implement all required methods
```javascript
class Duck {
    fly() { console.log("Flying low"); }
    swim() { console.log("Swimming"); }
}
```

2. **Mixins**: Copy methods from multiple sources
```javascript
const FlyMixin = {
    fly() { console.log("Flying"); }
};

const SwimMixin = {
    swim() { console.log("Swimming"); }
};

class Duck {}
Object.assign(Duck.prototype, FlyMixin, SwimMixin);
```

3. **Composition**: Use composition over inheritance
```javascript
class Duck {
    constructor() {
        this.flyBehavior = new SimpleFly();
        this.swimBehavior = new SimpleSwim();
    }
    fly() { this.flyBehavior.execute(); }
    swim() { this.swimBehavior.execute(); }
}
```

## Key Differences: Java vs JavaScript

| Feature | Java Interface | JavaScript Equivalent |
| :--- | :--- | :--- |
| Syntax | `interface Flyable {}` | Class with throw statements |
| Implementation | `implements` keyword | `extends` or duck typing |
| Multiple | `implements A, B` | Mixins or duck typing |
| Compile-time check | Yes | No (use TypeScript) |
| Runtime check | instanceof | typeof + manual checks |

## TypeScript Alternative

For true interfaces, use TypeScript:
```typescript
interface Flyable {
    fly(): void;
}

class Bird implements Flyable {
    fly() { console.log("Flying"); }
}
```

## When to Use What?

| Scenario | Approach |
| :--- | :--- |
| Simple contracts | Duck typing |
| Enforcing implementation | Abstract class pattern |
| Multiple "interfaces" | Mixins or composition |
| Large codebases | TypeScript |
