# Interfaces Notes (Python)

An interface is a **contract**. It defines WHAT a class must do, not HOW.

**Python uses the `abc` module (Abstract Base Classes)** to implement interfaces. Unlike Java, Python doesn't have a separate `interface` keyword.

## Using the abc Module

```python
from abc import ABC, abstractmethod

class Flyable(ABC):
    @abstractmethod
    def fly(self) -> None:
        """Must be implemented by subclasses."""
        pass
```

## Implementing an Interface

```python
class Bird(Flyable):
    def fly(self) -> None:
        print("Bird is flying with wings.")
```

## Key Points

1. **Cannot Instantiate ABC**: You cannot create an instance of a class with abstract methods
2. **Must Implement All**: Subclass must implement ALL abstract methods
3. **Can Have Concrete Methods**: ABCs can have regular methods too

```python
# This will raise TypeError!
flyable = Flyable()  # TypeError: Can't instantiate abstract class
```

## Multiple Interface Implementation

Python supports multiple inheritance, so implementing multiple interfaces is straightforward:

```python
from abc import ABC, abstractmethod

class Flyable(ABC):
    @abstractmethod
    def fly(self) -> None:
        pass

class Swimmable(ABC):
    @abstractmethod
    def swim(self) -> None:
        pass

class Duck(Flyable, Swimmable):
    def fly(self) -> None:
        print("Duck is flying low.")
    
    def swim(self) -> None:
        print("Duck is swimming.")
```

## Interface as a Type

```python
def make_fly(obj: Flyable) -> None:
    obj.fly()

bird = Bird()
make_fly(bird)  # Works! Bird implements Flyable
```

## Duck Typing Alternative

In Python, you often don't need formal interfaces. Just use duck typing:

```python
def make_fly(obj):
    if hasattr(obj, 'fly') and callable(obj.fly):
        obj.fly()
```

## Protocol (Python 3.8+)

For structural subtyping (like TypeScript interfaces):

```python
from typing import Protocol

class Flyable(Protocol):
    def fly(self) -> None: ...

class Bird:
    def fly(self) -> None:
        print("Flying!")

def make_fly(obj: Flyable) -> None:
    obj.fly()

make_fly(Bird())  # Works! Bird has fly() method
```

## Comparison: Java vs Python

| Feature | Java Interface | Python ABC |
| :--- | :--- | :--- |
| Keyword | `interface` | `class ... (ABC)` |
| Abstract methods | `void fly();` | `@abstractmethod def fly()` |
| Default methods | `default void ..` | Regular methods in ABC |
| Multiple impl | `implements A, B` | `class X(A, B)` |
| Instantiation | Not allowed | Not allowed |

## When to Use What?

| Scenario | Approach |
| :--- | :--- |
| Enforce contract at instantiation | `ABC` + `@abstractmethod` |
| Type hints only (no runtime check) | `Protocol` |
| Simple cases | Duck typing |
| Runtime behavior checking | `ABC` + `@abstractmethod` |

## Checking Interface Implementation

```python
from abc import ABC

# Check if class is a subclass of ABC
print(issubclass(Bird, Flyable))  # True

# Check if instance implements the interface
print(isinstance(bird, Flyable))  # True
```
