# Abstract Classes Notes (JavaScript)

An abstract class is a **partially complete** class that cannot be instantiated.

**JavaScript does NOT have the `abstract` keyword** like Java. We simulate it using:
1. Constructor checks with `new.target`
2. Methods that throw errors if not overridden

## Simulating Abstract Classes

```javascript
class Shape {
    constructor(name) {
        // Prevent direct instantiation
        if (new.target === Shape) {
            throw new Error("Cannot instantiate abstract class");
        }
        this.name = name;
    }

    // Abstract method (must be overridden)
    getArea() {
        throw new Error("Method 'getArea()' must be implemented");
    }

    // Concrete method (can be used directly)
    display() {
        console.log(`Shape: ${this.name}, Area: ${this.getArea()}`);
    }
}
```

## The `new.target` Property

`new.target` refers to the constructor that was directly invoked with `new`:

```javascript
class Shape {
    constructor() {
        console.log(new.target.name);
    }
}

class Circle extends Shape {}

new Circle(); // Logs: "Circle" (new.target is Circle, not Shape)
new Shape();  // Logs: "Shape" (new.target is Shape)
```

## Key Points

- Use `new.target` to prevent direct instantiation
- Abstract methods should throw errors
- Concrete methods work exactly like regular methods
- Child class MUST override abstract methods (or get runtime error)

## Abstract Class vs Interface (in JavaScript)

| Feature | Abstract Class Pattern | Interface Pattern |
| :--- | :--- | :--- |
| Can have constructor | Yes | No |
| Can have state | Yes | No (interfaces are stateless) |
| Can have concrete methods | Yes | No (just contracts) |
| Prevention check | `new.target` | N/A |
| Multiple inheritance | No | Yes (via duck typing/mixins) |

## When to Use Abstract Classes?

1. **Shared Code**: When subclasses share common functionality
2. **Template Method Pattern**: Define algorithm skeleton, let subclasses fill details
3. **Enforced Contract**: When subclasses MUST provide certain implementations

```javascript
class DataProcessor {
    constructor() {
        if (new.target === DataProcessor) {
            throw new Error("Abstract class");
        }
    }

    // Template method (concrete)
    process() {
        const data = this.fetchData();    // Abstract
        const result = this.transform(data); // Abstract
        this.save(result);                   // Abstract
        console.log("Processing complete!");
    }

    // Abstract methods
    fetchData() { throw new Error("Implement fetchData"); }
    transform(data) { throw new Error("Implement transform"); }
    save(result) { throw new Error("Implement save"); }
}
```

## TypeScript Alternative

For true abstract classes, use TypeScript:

```typescript
abstract class Shape {
    protected name: string;

    constructor(name: string) {
        this.name = name;
    }

    abstract getArea(): number; // Must be implemented

    display(): void {
        console.log(`Shape: ${this.name}, Area: ${this.getArea()}`);
    }
}
```
