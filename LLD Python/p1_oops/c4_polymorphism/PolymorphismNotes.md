# Polymorphism Notes (Python)

"Poly" = Many, "Morph" = Forms. An object can take many forms.

## 1. Compile-Time Polymorphism (Method Overloading)

**Python does NOT support true method overloading.**

In Java, you can have multiple methods with the same name but different parameters. In Python, if you define the same method name twice, the second one **overwrites** the first.

### Workarounds in Python

1. **Default Parameters**:
```python
def add(a, b, c=0):
    return a + b + c

add(1, 2)      # Returns 3
add(1, 2, 3)   # Returns 6
```

2. **Variable Arguments (*args, **kwargs)**:
```python
def add(*args):
    return sum(args)

add(1, 2)         # Returns 3
add(1, 2, 3, 4)   # Returns 10
```

3. **Type Checking (isinstance)**:
```python
def add(a, b):
    if isinstance(a, str) and isinstance(b, str):
        return a + " " + b
    return a + b

add(1, 2)           # Returns 3
add("Hello", "World")  # Returns "Hello World"
```

4. **@singledispatch (functools)**:
```python
from functools import singledispatch

@singledispatch
def process(arg):
    print(f"Default: {arg}")

@process.register(int)
def _(arg):
    print(f"Integer: {arg * 2}")

@process.register(str)
def _(arg):
    print(f"String: {arg.upper()}")

process(5)       # Integer: 10
process("hello") # String: HELLO
process([1, 2])  # Default: [1, 2]
```

5. **Separate Method Names** (most idiomatic):
```python
def add_two(a, b):
    return a + b

def add_three(a, b, c):
    return a + b + c
```

## 2. Runtime Polymorphism (Method Overriding)

- **Child provides its own implementation** of a Parent method.
- Decision made at **Runtime**.
- Example: `Animal.speak()` vs `Dog.speak()`.

```python
class Animal:
    def speak(self):
        print("Generic sound")

class Dog(Animal):
    def speak(self):  # Overrides parent
        print("Woof!")
```

## Key Concept: Duck Typing

**"If it walks like a duck and quacks like a duck, it's a duck."**

Python doesn't care about the type — only that the object has the required method:

```python
class Duck:
    def speak(self):
        print("Quack!")

class Person:
    def speak(self):
        print("Hello!")

def make_speak(obj):
    obj.speak()  # Works for anything with a speak() method!

make_speak(Duck())    # Quack!
make_speak(Person())  # Hello!
```

## Comparison: Overloading in Java vs Python

| Aspect | Java | Python |
| :--- | :--- | :--- |
| **Overloading** | Supported (different params) | NOT supported |
| **When** | Compile-time | N/A |
| **Workaround** | N/A | *args, defaults, @singledispatch |

## Comparison: Overriding

| Aspect | Java | Python |
| :--- | :--- | :--- |
| **Overriding** | Supported | Supported |
| **@Override** | Annotation available | No annotation (but typing has `@override`) |
| **Resolution** | Runtime | Runtime |
| **super keyword** | Yes | Yes (super()) |

## Calling Parent Method

```python
class Dog(Animal):
    def speak(self):
        super().speak()  # Calls Animal's speak()
        print("Woof!")
```

## Python 3.12+ @override decorator

```python
from typing import override

class Dog(Animal):
    @override  # Will error if Animal doesn't have speak()
    def speak(self):
        print("Woof!")
```

## Operator Overloading

Python allows overloading operators via special methods:

```python
class Vector:
    def __init__(self, x, y):
        self.x = x
        self.y = y
    
    def __add__(self, other):
        return Vector(self.x + other.x, self.y + other.y)
    
    def __str__(self):
        return f"Vector({self.x}, {self.y})"

v1 = Vector(1, 2)
v2 = Vector(3, 4)
v3 = v1 + v2  # Calls __add__
print(v3)     # Vector(4, 6)
```

| Operator | Method |
| :--- | :--- |
| `+` | `__add__` |
| `-` | `__sub__` |
| `*` | `__mul__` |
| `==` | `__eq__` |
| `<` | `__lt__` |
| `[]` | `__getitem__` |
| `len()` | `__len__` |
