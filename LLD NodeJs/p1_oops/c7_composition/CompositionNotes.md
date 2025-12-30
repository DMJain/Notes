# Composition & Association Notes (JavaScript)

Object relationships describe how objects are connected.

## Association (Weak HAS-A)

- Objects are related but **exist independently**.
- One object uses another but doesn't own it.
- Example: `Student` has a `Teacher`, but Teacher exists on its own.

```javascript
class Student {
    #teacher;
    
    constructor(teacher) {
        this.#teacher = teacher; // Passed from outside
    }
}

// Usage
const teacher = new Teacher("Mr. Smith");
const student = new Student(teacher);  // Teacher passed in
// If student is deleted, teacher still exists
```

## Composition (Strong HAS-A)

- Part **cannot exist** without the whole.
- Whole object **creates and owns** the part.
- Example: `Car` has an `Engine`, Engine dies when Car is destroyed.

```javascript
class Car {
    #engine;
    
    constructor(engineType) {
        this.#engine = new Engine(engineType); // Created inside
    }
}

// Usage
const car = new Car("V8");
// Engine is created when Car is created
// If car is garbage collected, engine is also gone
```

## Quick Comparison

| Aspect | Association | Composition |
| :--- | :--- | :--- |
| Ownership | Weak | Strong |
| Lifetime | Independent | Dependent |
| Creation | Outside (passed in) | Inside (created) |
| Example | Student-Teacher | Car-Engine |

## How to Identify?

**"Can the part exist without the whole?"**
- Yes → **Association**
- No → **Composition**

## JavaScript/Node.js Specifics

### Garbage Collection
In JavaScript, when an object has no references, it's garbage collected:

```javascript
// Association
let teacher = new Teacher("Smith");
let student = new Student(teacher);
student = null;  // student is garbage collected, teacher still exists

// Composition  
let car = new Car("Model S", "Electric");
car = null;  // Both car AND its engine are garbage collected
```

### Reference Handling
JavaScript doesn't have explicit memory management, but the concept still applies:

```javascript
// Association: Multiple references to same object
const teacher = new Teacher("Smith");
const s1 = new Student(teacher);
const s2 = new Student(teacher);
// Both students share the SAME teacher instance

// Composition: Each instance owns its own part
const car1 = new Car("Tesla", "Electric");
const car2 = new Car("BMW", "V8");
// car1 and car2 each have their OWN engine instance
```

## Related Patterns

### Aggregation
A middle ground between Association and Composition:
- Whole contains parts
- Parts can exist independently
- Often implemented with arrays/collections

```javascript
class Team {
    #members = [];
    
    addMember(player) {
        this.#members.push(player);  // Players can exist outside team
    }
}
```

### Dependency Injection (DI)
Using Association pattern for flexibility:

```javascript
class UserService {
    #database;
    
    constructor(database) {  // Injected, not created
        this.#database = database;
    }
}

// Easy to swap implementations
const service1 = new UserService(new MySQLDatabase());
const service2 = new UserService(new MongoDatabase());
```
