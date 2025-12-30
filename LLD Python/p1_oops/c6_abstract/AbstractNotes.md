# Abstract Classes Notes (Python)

An abstract class is a **partially complete** class that cannot be instantiated.

**Python uses the `abc` module** (same as interfaces) but abstract classes typically have:
- Some abstract methods (must be implemented)
- Some concrete methods (can be used directly)

## Creating an Abstract Class

```python
from abc import ABC, abstractmethod

class Shape(ABC):
    def __init__(self, name: str):
        self.name = name  # Can have state!
    
    @abstractmethod
    def get_area(self) -> float:
        """Abstract method - must be implemented."""
        pass
    
    def display(self) -> None:
        """Concrete method - can be used directly."""
        print(f"Shape: {self.name}, Area: {self.get_area()}")
```

## Key Points

- Use `ABC` as base class and `@abstractmethod` decorator
- Abstract methods have no implementation (just `pass`)
- Can have constructors, fields, and concrete methods
- Child class MUST implement all abstract methods (or be abstract itself)
- Cannot instantiate a class with unimplemented abstract methods

## Abstract Class vs Interface

In Python, the distinction is less formal than Java:

| Feature | Abstract Class | Interface (Pure ABC) |
| :--- | :--- | :--- |
| State (attributes) | Yes | Usually no |
| Constructor | Yes | Usually no |
| Concrete methods | Yes | No (all abstract) |
| Multiple inheritance | Yes | Yes |
| Purpose | Partial implementation | Pure contract |

## Template Method Pattern

Abstract classes are perfect for the Template Method pattern:

```python
from abc import ABC, abstractmethod

class DataProcessor(ABC):
    def process(self) -> None:
        """Template method - defines the algorithm skeleton."""
        data = self.fetch_data()
        result = self.transform(data)
        self.save(result)
        print("Processing complete!")
    
    @abstractmethod
    def fetch_data(self) -> str:
        """Abstract - subclass defines HOW to fetch."""
        pass
    
    @abstractmethod
    def transform(self, data: str) -> str:
        """Abstract - subclass defines HOW to transform."""
        pass
    
    @abstractmethod
    def save(self, result: str) -> None:
        """Abstract - subclass defines HOW to save."""
        pass
```

## Abstract Properties

You can also have abstract properties:

```python
from abc import ABC, abstractmethod

class Shape(ABC):
    @property
    @abstractmethod
    def area(self) -> float:
        """Abstract property - must be implemented."""
        pass
```

## Partial Abstract Classes

A class can be partially abstract (some methods implemented):

```python
class Animal(ABC):
    def __init__(self, name: str):
        self.name = name  # Concrete: all animals have names
    
    @abstractmethod
    def speak(self) -> None:
        pass  # Abstract: each animal speaks differently
    
    def breathe(self) -> None:
        print(f"{self.name} is breathing.")  # Concrete: all animals breathe
```

## When to Use Abstract Classes?

| Use Abstract Class When... | Use Interface (Pure ABC) When... |
| :--- | :--- |
| Subclasses share common code | Only defining a contract |
| You need state/attributes | No shared implementation |
| Template Method pattern | Multiple unrelated classes conform |
| IS-A relationship with shared behavior | CAN-DO capability |

## Differences: Java vs Python

| Feature | Java | Python |
| :--- | :--- | :--- |
| Keyword | `abstract class` | `class ...(ABC)` |
| Abstract method | `abstract void method()` | `@abstractmethod` |
| Concrete method | Regular method | Regular method |
| Constructor | Yes | Yes |
| Fields | Any type | Any type |
| Multiple inheritance | No (single only) | Yes |
