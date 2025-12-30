# Introduction to Object-Oriented Programming (Python)

Welcome to the OOP module! This document introduces the foundational concepts you'll explore in depth throughout chapters c1-c7.

---

## What is a Programming Paradigm?

A **programming paradigm** is a way of thinking about and structuring code. Think of it as a **philosophy** for solving problems with code.

| Paradigm | Focus | Example |
|----------|-------|---------|
| **Imperative** | Step-by-step instructions | "Do this, then do that" |
| **Procedural** | Organize steps into functions | "Call mix_ingredients(), then bake()" |
| **Object-Oriented** | Model real-world entities | "The Chef object bakes the Cake object" |
| **Functional** | Transform data with pure functions | "Filter → Map → Reduce" |

> **Python** is a **multi-paradigm** language! It elegantly supports Procedural, Object-Oriented, AND Functional programming. This flexibility makes Python incredibly versatile.

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
| **Hard to Extend** | Adding features means modifying existing code | Adding new shape breaks `play_sound()` |

### The Birth of OOP

**Simula** (1967) and later **Smalltalk** (1972) introduced OOP concepts. **C++** (1979) brought OOP to mainstream by extending C. **Python** (1991) was designed from the ground up with OOP support.

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

Imagine building a shapes app with rotate and play_sound features:

```python
# Procedural approach
def rotate(shape_num):
    # rotate based on shape_num
    pass

def play_sound(shape_num):
    if shape_num == 1:
        # play square sound
        pass
    elif shape_num == 2:
        # play circle sound
        pass
    elif shape_num == 3:
        # play triangle sound
        pass
    # What happens when we add a new shape? 😰
```

**Problems:**
1. Adding new shapes requires modifying existing, tested code
2. Data (shape properties) and behavior (rotate, play_sound) are separate
3. Code becomes a tangled mess as the project grows

### The OOP Solution

```python
class Square:
    def rotate(self):
        """Square rotation logic"""
        pass
    
    def play_sound(self):
        """Play square.mp3"""
        pass

class Circle:
    def rotate(self):
        """Circle rotation logic"""
        pass
    
    def play_sound(self):
        """Play circle.mp3"""
        pass

# Adding a new shape? Just add a new class!
class Amoeba:
    def rotate(self):
        """Amoeba rotation logic"""
        pass
    
    def play_sound(self):
        """Play amoeba.hif"""
        pass
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
| Color, speed, fuel level | **Attributes** (state) |
| Drive, brake, honk | **Methods** (behavior) |

---

## Classes and Objects in Python

### Class = Blueprint

```python
class Player:
    """A player in our game."""
    
    def __init__(self, name: str):
        """Constructor - called when creating new object."""
        self.name = name      # State (Attributes)
        self.score = 0
    
    def play(self):
        """Behavior (Methods)"""
        print(f"{self.name} is playing!")
    
    def score_point(self):
        self.score += 1
        print(f"{self.name} scored! Total: {self.score}")
```

### Object = Instance

Objects are created **from** classes by calling the class like a function.

```python
# Create two different Player objects from the same class
player1 = Player("Alice")
player2 = Player("Bob")

player1.play()        # Alice is playing!
player2.play()        # Bob is playing!
player1.score_point() # Alice scored! Total: 1
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
```python
# User of the Player class doesn't need to know HOW score is calculated
player1.score_point()  # Just call the method!

# The implementation details are hidden inside the class
```

**How do we achieve abstraction?** Through the 3 pillars: Encapsulation, Inheritance, and Polymorphism.

**Covered in:** c5 (Interfaces), c6 (Abstract Classes)

**Python Note:** Python uses the `abc` module (Abstract Base Classes) to implement interfaces and abstract classes.

---

## Pillar 1: Encapsulation

**"Bundle data with methods that operate on it, and restrict direct access."**

Encapsulation helps achieve abstraction by **hiding the internal state** and only exposing controlled methods to interact with it.

```python
class BankAccount:
    def __init__(self):
        self.__balance = 0  # Private attribute (name mangling with __)
    
    def deposit(self, amount: float):
        if amount > 0:
            self.__balance += amount  # Controlled access
    
    def get_balance(self) -> float:
        return self.__balance  # Read-only access


account = BankAccount()
account.deposit(100)
print(account.get_balance())  # 100
# print(account.__balance)    # AttributeError! Private attribute
```

**Why?** 
- Prevents invalid states (e.g., negative balance without a withdrawal method)
- Users don't need to know balance is stored as a float — that's abstracted away

**Covered in:** c1 (Access Modifiers)

---

## Pillar 2: Inheritance

**"Child classes inherit properties and methods from parent classes."**

Inheritance helps achieve abstraction by allowing you to **define common behavior once** and reuse it, hiding the details in the parent class.

```python
class User:
    def __init__(self, name: str, email: str):
        self.name = name
        self.email = email
    
    def login(self):
        print(f"{self.name} logged in")


class Student(User):
    def __init__(self, name: str, email: str, batch_name: str):
        super().__init__(name, email)  # Call parent constructor
        self.batch_name = batch_name   # Additional property
    
    def study(self):
        print(f"{self.name} is studying")


# Student inherits name, email, and login() from User
s = Student("Alice", "alice@email.com", "Sept23")
s.login()  # Works! Inherited from User
s.study()  # Student-specific
```

**Why?**
- Common functionality is abstracted into the parent class
- Child classes focus only on their specific behavior

**Covered in:** c3 (Inheritance)

---

## Pillar 3: Polymorphism

**"Same interface, different implementations."**

Polymorphism helps achieve abstraction by allowing you to work with objects through a **common interface** without knowing their specific types.

```python
class Animal:
    def speak(self):
        print("Some sound")


class Dog(Animal):
    def speak(self):  # Override parent method
        print("Woof!")


class Cat(Animal):
    def speak(self):  # Override parent method
        print("Meow!")


# Polymorphism in action
animals = [Dog(), Cat(), Animal()]
for animal in animals:
    animal.speak()
# Output:
# Woof!
# Meow!
# Some sound
```

**Why?**
- You can add new animal types without changing the loop code
- The interface (`speak()`) stays the same, implementation varies

**Covered in:** c3 (Inheritance), c4 (Polymorphism)

**Python Note:** Python uses **duck typing**: "If it walks like a duck and quacks like a duck, treat it as a duck." You don't need formal interfaces — just implement the required methods.

---

## Summary: Principle & Pillars

| Concept | Type | Role | How It Helps | Chapter |
|---------|------|------|--------------|---------|
| **Abstraction** | Principle | The Goal | Hide complexity, show essentials | c5, c6 |
| **Encapsulation** | Pillar 1 | Achieve Abstraction | Protect data, control access | c1 |
| **Inheritance** | Pillar 2 | Achieve Abstraction | Reuse code, define once | c3 |
| **Polymorphism** | Pillar 3 | Achieve Abstraction | Same interface, many forms | c4 |

---

## Python vs Java: Key OOP Differences

| Feature | Java | Python |
|---------|------|--------|
| **Class support** | Native since beginning | Native since beginning |
| **Typing** | Static (compile-time) | Dynamic (runtime), optional type hints |
| **Access modifiers** | `public`, `private`, `protected`, package | `_protected` (convention), `__private` (name mangling) |
| **Interfaces** | Native `interface` keyword | `abc.ABC` + `@abstractmethod` |
| **Abstract classes** | Native `abstract` keyword | `abc.ABC` + `@abstractmethod` |
| **Method overloading** | Supported (different params) | NOT supported (use `*args`, `**kwargs`, `@singledispatch`) |
| **Multiple inheritance** | Via interfaces only | Fully supported (with MRO) |
| **Constructor** | Constructor overloading | Single `__init__` (use defaults/factory methods) |

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
| **c1** | Access Modifiers | `_protected`, `__private`, name mangling |
| **c2** | Constructors | `__init__`, `copy` module, deep vs shallow copy |
| **c3** | Inheritance | `super()`, MRO, `isinstance()` |
| **c4** | Polymorphism | Override, duck typing, `@singledispatch` |
| **c5** | Interfaces | `abc.ABC`, `@abstractmethod`, duck typing |
| **c6** | Abstract Classes | `abc.ABC`, template method pattern |
| **c7** | Composition | HAS-A relationships, dependency injection |

---

## Quick Reference: Python OOP Syntax

```python
from abc import ABC, abstractmethod

# Class definition
class ClassName:
    # Class attribute (shared by all instances)
    class_var = "shared"
    
    def __init__(self, name: str):
        """Constructor"""
        self.name = name                # Public attribute
        self._protected = "protected"   # Protected (convention)
        self.__private = "private"      # Private (name mangling)
    
    def do_something(self):
        """Instance method"""
        print("Doing something")
    
    @property
    def private(self):
        """Getter for private attribute"""
        return self.__private
    
    @private.setter
    def private(self, value):
        """Setter for private attribute"""
        self.__private = value
    
    @staticmethod
    def static_method():
        """Called on class, not instance"""
        print("Static!")
    
    @classmethod
    def class_method(cls):
        """Can access class state"""
        print(f"Class: {cls}")


# Inheritance
class Child(Parent):
    def __init__(self, name: str, extra: str):
        super().__init__(name)  # Call parent constructor
        self.extra = extra


# Abstract class
class Shape(ABC):
    @abstractmethod
    def get_area(self) -> float:
        """Must be implemented by subclasses"""
        pass


# Object creation
obj = ClassName("value")
```

---

## The `self` Parameter

In Python, `self` is a reference to the current instance. It's passed automatically but must be declared explicitly:

```python
class Player:
    def __init__(self, name):  # self is first parameter
        self.name = name       # Assigns to instance attribute
    
    def play(self):            # self is first parameter
        print(f"{self.name} is playing!")  # Access instance attribute


# When calling, don't pass self
player = Player("Alice")
player.play()  # Python passes 'player' as 'self' automatically
```

> **Note:** `self` is just a convention. You could use any name, but ALWAYS use `self` for consistency.

---

## Type Hints (Modern Python)

Python 3.5+ supports type hints for better code documentation:

```python
from typing import List, Optional

class Student:
    def __init__(self, name: str, age: int) -> None:
        self.name: str = name
        self.age: int = age
        self.scores: List[int] = []
    
    def add_score(self, score: int) -> None:
        self.scores.append(score)
    
    def get_average(self) -> Optional[float]:
        if not self.scores:
            return None
        return sum(self.scores) / len(self.scores)
```

Type hints are **optional** and don't affect runtime — they're for documentation and static analysis tools like `mypy`.

---

> **Remember:** OOP in Python is flexible and "batteries included". Use:
> - `_underscore` prefix for protected attributes (convention)
> - `__double_underscore` for private attributes (name mangling)
> - `abc` module for interfaces and abstract classes
> - Duck typing: "If it has the right methods, it's the right type"
> - `isinstance()` for type checking when needed

Happy coding! 🐍🚀
