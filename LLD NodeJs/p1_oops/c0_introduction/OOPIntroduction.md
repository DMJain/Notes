# Introduction to Object-Oriented Programming (JavaScript)

Welcome to the OOP module! This document introduces the foundational concepts you'll explore in depth throughout chapters c1-c7.

---

## What is a Programming Paradigm?

A **programming paradigm** is a way of thinking about and structuring code. Think of it as a **philosophy** for solving problems with code.

| Paradigm | Focus | Example |
|----------|-------|---------|
| **Imperative** | Step-by-step instructions | "Do this, then do that" |
| **Procedural** | Organize steps into functions | "Call mixIngredients(), then bake()" |
| **Object-Oriented** | Model real-world entities | "The Chef object bakes the Cake object" |
| **Functional** | Transform data with pure functions | "Filter → Map → Reduce" |

> **JavaScript** is a **multi-paradigm** language! It supports Procedural, Object-Oriented, AND Functional programming. This flexibility is both powerful and can be confusing for beginners.

---

## Why Was OOP Developed? (The History)

### The C Era: Procedural Programming

In the 1970s, **C** was the king. It was powerful, fast, and revolutionized programming. C uses **procedural programming** where:
- Code is organized into **functions**
- Data and functions are **separate**
- You pass data to functions, functions process it, return results

```c
// C style - Data and Functions are SEPARATE
struct Rectangle {
    int width;
    int height;
};

int calculateArea(struct Rectangle r) {
    return r.width * r.height;
}

// Anyone can modify the data directly
struct Rectangle rect;
rect.width = -5;  // Invalid! But C allows it 😱
```

### Problems with Procedural Code (Why C Wasn't Enough)

As software grew larger (1980s-90s), procedural programming showed its limits:

| Problem | Description | Example |
|---------|-------------|---------|
| **Spaghetti Code** | Functions calling functions in a tangled mess | 100+ functions, unclear flow |
| **Global State** | Data accessible everywhere = bugs everywhere | One function changes data, another relies on old value |
| **No Data Protection** | Anyone can modify any data | Setting `balance = -1000` |
| **Poor Reusability** | Copy-paste to reuse code | Same validation logic in 10 places |
| **Hard to Extend** | Adding features means modifying existing code | Adding new shape breaks `playSound()` |

### The Birth of OOP

**Simula** (1967) and later **Smalltalk** (1972) introduced OOP concepts. **C++** (1979) brought OOP to mainstream by extending C. **JavaScript** (1995) adopted OOP through prototypes, later adding class syntax in ES6 (2015).

The core idea: **Model software like the real world**
- Real world has **objects** (car, person, bank account)
- Objects have **properties** (color, name, balance)
- Objects have **behaviors** (drive, walk, withdraw)
- Objects **hide complexity** (you drive without knowing engine internals)

> **Key Insight:** OOP wasn't invented because C was bad. It was invented because **software was getting too complex** for procedural thinking to handle.

---

## Why Do We Need Abstraction? (The Core Problem)

### The Complexity Problem

As systems grow, complexity explodes:

```
Small System:    🔲 → Simple, manageable
Medium System:   🔲🔲🔲🔲🔲 → Getting complex
Large System:    🔲🔲🔲🔲🔲🔲🔲🔲🔲🔲🔲🔲🔲🔲🔲 → Chaos!
```

**Without abstraction:**
- Every developer must understand EVERYTHING
- One change can break unrelated parts
- Debugging becomes a nightmare
- New team members take months to onboard

### What Abstraction Solves

**Abstraction = Managing Complexity by Hiding Details**

| Without Abstraction | With Abstraction |
|---------------------|------------------|
| Must know how engine works to drive | Just press pedal to go |
| Must understand TCP/IP to send email | Just click "Send" |
| Must know database internals to save data | Just call `user.save()` |

### Real Example: Driving a Car

```
┌─────────────────────────────────────────────────────┐
│                    DRIVER'S VIEW                    │
│            (Abstracted Interface)                   │
│                                                     │
│    [Steering Wheel]  [Pedals]  [Gear Shift]        │
│                                                     │
│         Simple! Anyone can learn to drive.         │
└─────────────────────────────────────────────────────┘
                         │
                         │ Abstraction Layer
                         ▼
┌─────────────────────────────────────────────────────┐
│                  HIDDEN COMPLEXITY                  │
│                                                     │
│   Engine, Transmission, Fuel Injection, ECU,       │
│   Brake Hydraulics, Power Steering, Suspension...  │
│                                                     │
│      You don't need to know any of this!           │
└─────────────────────────────────────────────────────┘
```

---

## The Problem with Procedural Code (Detailed Example)

Imagine building a shapes app with rotate and playSound features:

```javascript
// Procedural approach
function rotate(shapeNum) {
    // rotate based on shapeNum
}

function playSound(shapeNum) {
    if (shapeNum === 1) {
        // play square sound
    } else if (shapeNum === 2) {
        // play circle sound
    } else if (shapeNum === 3) {
        // play triangle sound
    }
    // What happens when we add a new shape? 😰
}
```

**Problems:**
1. Adding new shapes requires modifying existing, tested code
2. Data (shape properties) and behavior (rotate, playSound) are separate
3. Code becomes a tangled mess as the project grows

### The OOP Solution

```javascript
class Square {
    rotate() { /* square rotation logic */ }
    playSound() { /* play square.mp3 */ }
}

class Circle {
    rotate() { /* circle rotation logic */ }
    playSound() { /* play circle.mp3 */ }
}

// Adding a new shape? Just add a new class!
class Amoeba {
    rotate() { /* amoeba rotation logic */ }
    playSound() { /* play amoeba.hif */ }
}
```

**Benefits:**
- ✅ Adding new shapes doesn't touch existing code
- ✅ Each shape manages its own data and behavior
- ✅ Easy to understand, test, and maintain

---

## What is Object-Oriented Programming?

**OOP is a paradigm that models code around real-world entities (objects) that combine:**
- **State** (data/properties) — What the object *has*
- **Behavior** (methods/functions) — What the object *does*

### Real-World Analogy

| Real World | OOP Concept |
|------------|-------------|
| A car's blueprint | **Class** |
| Your specific car | **Object** (instance) |
| Color, speed, fuel level | **Properties** (state) |
| Drive, brake, honk | **Methods** (behavior) |

---

## A Brief History of OOP in JavaScript

JavaScript has evolved significantly:

| Era | OOP Approach | Syntax |
|-----|--------------|--------|
| **ES5 and before** | Prototype-based | `function Person() {}` + `Person.prototype.method` |
| **ES6 (2015)** | Class syntax (syntactic sugar) | `class Person { }` |
| **ES2022** | True private fields | `#privateField` |

> **Note:** JavaScript's `class` keyword is syntactic sugar over prototypes. Under the hood, it's still prototype-based inheritance!

---

## Classes and Objects in JavaScript

### Class = Blueprint

```javascript
class Player {
    // Constructor - called when creating new object
    constructor(name) {
        this.name = name;    // State (Properties)
        this.score = 0;
    }
    
    // Behavior (Methods)
    play() {
        console.log(`${this.name} is playing!`);
    }
    
    scorePoint() {
        this.score++;
        console.log(`${this.name} scored! Total: ${this.score}`);
    }
}
```

### Object = Instance

Objects are created **from** classes using the `new` keyword.

```javascript
// Create two different Player objects from the same class
const player1 = new Player("Alice");
const player2 = new Player("Bob");

player1.play();       // Alice is playing!
player2.play();       // Bob is playing!
player1.scorePoint(); // Alice scored! Total: 1
```

**Key Insight:** One class, many objects. Each object has its own state but shares the same behavior.

---

## The Principle and Pillars of OOP

OOP has **1 core principle** and **3 pillars** to achieve it:

```
                    ┌─────────────────────────┐
                    │      ABSTRACTION        │
                    │    (The Principle)      │
                    │  "Hide complexity,      │
                    │   show only essentials" │
                    └───────────┬─────────────┘
                                │
            ┌───────────────────┼───────────────────┐
            │                   │                   │
            ▼                   ▼                   ▼
    ┌───────────────┐   ┌───────────────┐   ┌───────────────┐
    │ ENCAPSULATION │   │  INHERITANCE  │   │ POLYMORPHISM  │
    │   (Pillar 1)  │   │   (Pillar 2)  │   │   (Pillar 3)  │
    │               │   │               │   │               │
    │ Protect data  │   │  Reuse code   │   │  Many forms   │
    └───────────────┘   └───────────────┘   └───────────────┘
```

### How the 3 Pillars Support Abstraction

| Pillar | How It Achieves Abstraction |
|--------|----------------------------|
| **Encapsulation** | Hides **data** — Users interact via methods, not raw data. They don't know how data is stored or validated internally. |
| **Inheritance** | Hides **implementation** — Child classes reuse parent code without knowing its internals. Common logic is written once and abstracted away. |
| **Polymorphism** | Hides **type details** — You work with a general interface (`animal.speak()`) without knowing the specific type (`Dog` vs `Cat`). |

### Analogy: Building a House

Think of building software like building a house:

| OOP Concept | House Building Analogy |
|-------------|------------------------|
| **Abstraction** (Principle) | The homeowner just wants a house that works — doesn't need to know construction details |
| **Encapsulation** (Pillar 1) | Electrical wiring is hidden in walls — you just use switches |
| **Inheritance** (Pillar 2) | All rooms share basic features (floor, ceiling) — defined once in "Room" blueprint |
| **Polymorphism** (Pillar 3) | "Turn on" works for lights, fans, and AC — same interface, different behavior |

---

## The Principle: Abstraction

**"Hide complexity and show only the essential features."**

Abstraction is the **goal** of OOP. It means users of your code don't need to know HOW things work internally — they only need to know WHAT they can do.

### Real-World Example
Think of a **car**:
- You use the steering wheel, pedals, and gear shift (simple interface)
- You don't need to know how the engine, transmission, or fuel injection works
- The complexity is **abstracted** away

### Code Example
```javascript
// User of the Player class doesn't need to know HOW score is calculated
player1.scorePoint(); // Just call the method!

// The implementation details are hidden inside the class
```

**How do we achieve abstraction?** Through the 3 pillars: Encapsulation, Inheritance, and Polymorphism.

**Covered in:** c5 (Interfaces), c6 (Abstract Classes)

**JavaScript Note:** No native abstract classes or interfaces, but we simulate them using conventions and runtime checks.

---

## Pillar 1: Encapsulation

**"Bundle data with methods that operate on it, and restrict direct access."**

Encapsulation helps achieve abstraction by **hiding the internal state** and only exposing controlled methods to interact with it.

```javascript
class BankAccount {
    #balance = 0;  // Private field (ES2022) - truly private!
    
    deposit(amount) {
        if (amount > 0) {
            this.#balance += amount; // Controlled access
        }
    }
    
    getBalance() {
        return this.#balance; // Read-only access
    }
}

const account = new BankAccount();
account.deposit(100);
console.log(account.getBalance()); // 100
// console.log(account.#balance);  // SyntaxError! Private field
```

**Why?** 
- Prevents invalid states (e.g., negative balance without a withdrawal method)
- Users don't need to know balance is stored as a number — that's abstracted away

**Covered in:** c1 (Access Modifiers)

---

## Pillar 2: Inheritance

**"Child classes inherit properties and methods from parent classes."**

Inheritance helps achieve abstraction by allowing you to **define common behavior once** and reuse it, hiding the details in the parent class.

```javascript
class User {
    constructor(name, email) {
        this.name = name;
        this.email = email;
    }
    
    login() {
        console.log(`${this.name} logged in`);
    }
}

class Student extends User {
    constructor(name, email, batchName) {
        super(name, email);  // Call parent constructor
        this.batchName = batchName;  // Additional property
    }
    
    study() {
        console.log(`${this.name} is studying`);
    }
}

// Student inherits name, email, and login() from User
const s = new Student("Alice", "alice@email.com", "Sept23");
s.login();  // Works! Inherited from User
s.study();  // Student-specific
```

**Why?**
- Common functionality is abstracted into the parent class
- Child classes focus only on their specific behavior

**Covered in:** c3 (Inheritance)

---

## Pillar 3: Polymorphism

**"Same interface, different implementations."**

Polymorphism helps achieve abstraction by allowing you to work with objects through a **common interface** without knowing their specific types.

```javascript
class Animal {
    speak() {
        console.log("Some sound");
    }
}

class Dog extends Animal {
    speak() {  // Override parent method
        console.log("Woof!");
    }
}

class Cat extends Animal {
    speak() {  // Override parent method
        console.log("Meow!");
    }
}

// Polymorphism in action
const animals = [new Dog(), new Cat(), new Animal()];
animals.forEach(animal => animal.speak());
// Output:
// Woof!
// Meow!
// Some sound
```

**Why?**
- You can add new animal types without changing the loop code
- The interface (`speak()`) stays the same, implementation varies

**Covered in:** c3 (Inheritance), c4 (Polymorphism)

**JavaScript Note:** JS doesn't support method overloading (same method name, different parameters). We simulate it using default parameters or rest arguments.

---

## Summary: Principle & Pillars

| Concept | Type | Role | How It Helps | Chapter |
|---------|------|------|--------------|---------|
| **Abstraction** | Principle | The Goal | Hide complexity, show essentials | c5, c6 |
| **Encapsulation** | Pillar 1 | Achieve Abstraction | Protect data, control access | c1 |
| **Inheritance** | Pillar 2 | Achieve Abstraction | Reuse code, define once | c3 |
| **Polymorphism** | Pillar 3 | Achieve Abstraction | Same interface, many forms | c4 |

---

## JavaScript vs Java: Key OOP Differences

| Feature | Java | JavaScript |
|---------|------|------------|
| **Class support** | Native since beginning | ES6+ (syntactic sugar) |
| **Typing** | Static (compile-time) | Dynamic (runtime) |
| **Access modifiers** | public, private, protected, package | # for private, _ convention for protected |
| **Interfaces** | Native `interface` keyword | Duck typing or abstract class pattern |
| **Abstract classes** | Native `abstract` keyword | Simulate with `new.target` check |
| **Method overloading** | Supported | NOT supported (use rest params) |
| **Multiple inheritance** | Via interfaces only | Via mixins or duck typing |

---

## Advantages of OOP

| Advantage | Explanation |
|-----------|-------------|
| **Reusability** | Write once (class), use many times (objects) |
| **Maintainability** | Changes in one class don't break others |
| **Security** | Encapsulation hides sensitive data |
| **Scalability** | Easy to add new features via inheritance |
| **Modularity** | Each class is a self-contained unit |

## Disadvantages of OOP

| Disadvantage | Explanation |
|--------------|-------------|
| **Requires planning** | Must design classes before coding |
| **Larger codebase** | More boilerplate than procedural code |
| **Over-engineering risk** | Can create unnecessary complexity |
| **Learning curve** | Concepts like polymorphism take time to master |

---

## What's Next?

This module will take you through each pillar in depth:

| Chapter | Topic | Key Concepts |
|---------|-------|--------------|
| **c1** | Access Modifiers | #private, _protected (convention), public |
| **c2** | Constructors | Object creation, spread operator, deep copy |
| **c3** | Inheritance | extends, super, instanceof |
| **c4** | Polymorphism | Override, Duck typing, No true overloading |
| **c5** | Interfaces | Duck typing, Abstract class pattern |
| **c6** | Abstract Classes | new.target, throw in methods |
| **c7** | Composition | HAS-A relationships, Aggregation |

---

## Quick Reference: JavaScript OOP Syntax

```javascript
// Class definition
class ClassName {
    // Private field (ES2022)
    #privateField;
    
    // Constructor
    constructor(name) {
        this.name = name;  // Public field
        this.#privateField = 'secret';
    }
    
    // Method
    doSomething() {
        console.log("Doing something");
    }
    
    // Getter
    get privateField() {
        return this.#privateField;
    }
    
    // Setter
    set privateField(value) {
        this.#privateField = value;
    }
    
    // Static method
    static staticMethod() {
        console.log("Called on class, not instance");
    }
}

// Inheritance
class Child extends Parent {
    constructor(name, extra) {
        super(name);  // MUST call before using 'this'
        this.extra = extra;
    }
}

// Object creation
const obj = new ClassName("value");

// Module export (Node.js)
module.exports = ClassName;
// or ES6 modules
export default ClassName;
```

---

## Prototype-Based Inheritance (Legacy)

Before ES6 classes, JavaScript used prototype-based inheritance:

```javascript
// ES5 style (still works, still used internally)
function Player(name) {
    this.name = name;
    this.score = 0;
}

Player.prototype.play = function() {
    console.log(this.name + " is playing!");
};

const p = new Player("Alice");
p.play(); // Alice is playing!
```

> **Important:** ES6 `class` is just syntactic sugar over this prototype system. Understanding prototypes helps debugging!

---

> **Remember:** OOP in JavaScript is flexible but less strict than Java. Use:
> - `#` for truly private fields
> - `_` prefix as a convention for "protected"
> - Duck typing: "If it walks like a duck and quacks like a duck, treat it as a duck"
> - `instanceof` for type checking when needed

Happy coding! 🚀
