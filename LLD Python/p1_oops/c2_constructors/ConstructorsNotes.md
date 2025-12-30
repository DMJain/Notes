# Constructors & Memory Model Notes (Python)

## Constructors

In Python, classes have an `__init__` method that serves as the constructor. Unlike Java, Python does not support constructor overloading (multiple constructors with different signatures).

### Patterns

1. **Default Constructor Pattern**: Use default parameters
2. **Parameterized Constructor**: Pass arguments to `__init__`
3. **Copy Constructor Pattern**: Use class methods or check argument types

```python
import copy

class Student:
    def __init__(self, name: str = "Unknown", age: int = 18, scores: list = None):
        self.name = name
        self.age = age
        # Avoid mutable default arguments!
        self.exam_scores = scores if scores is not None else [0, 0, 0]
    
    @classmethod
    def from_student(cls, other: 'Student') -> 'Student':
        """Copy constructor pattern using class method."""
        return cls(
            name=other.name,
            age=other.age,
            scores=copy.deepcopy(other.exam_scores)  # Deep copy
        )
```

## Shallow Copy vs Deep Copy

### Shallow Copy
- Copies **references** for nested objects/lists.
- **Result**: Both objects share the same nested objects. Modifying one affects the other.

```python
import copy

s1 = Student("Alice", 20, [90, 85, 88])
s2 = copy.copy(s1)  # Shallow copy

s2.exam_scores[0] = 100
print(s1.exam_scores)  # [100, 85, 88] - s1 also changed!
```

### Deep Copy
- Creates **new instances** for all nested objects.
- **Result**: Objects are completely independent.

```python
import copy

s1 = Student("Alice", 20, [90, 85, 88])
s2 = copy.deepcopy(s1)  # Deep copy

s2.exam_scores[0] = 100
print(s1.exam_scores)  # [90, 85, 88] - s1 unchanged!
```

## Deep Copy Methods in Python

| Method | Depth | Preserves Methods? | Use Case |
| :--- | :--- | :--- | :--- |
| `=` assignment | Reference only | N/A | Same object |
| `copy.copy()` | 1 level | Yes | Simple objects |
| `copy.deepcopy()` | Full | Yes | Nested objects |
| `__copy__` / `__deepcopy__` | Custom | Yes | Control behavior |

## Python Memory Model

- **Immutable Types** (int, str, tuple, frozenset): Cannot be changed, share references
- **Mutable Types** (list, dict, set, custom objects): Can be changed in place

### Pass-by-Object-Reference

Python passes the **reference** (not the object, not a copy of the object):

```python
def modify_reference(s):
    s = Student("New")  # Reassigns LOCAL reference
# Original unchanged!

def modify_object(s):
    s.name = "Changed"  # Follows reference to actual object
# Original changed!
```

### Important: Mutable Default Arguments

**NEVER USE MUTABLE DEFAULT ARGUMENTS!**

```python
# WRONG! All instances share the SAME list
def __init__(self, scores=[]):
    self.scores = scores

# CORRECT! Create new list each time
def __init__(self, scores=None):
    self.scores = scores if scores is not None else []
```

## Reference Assignment vs Cloning

| Operation | Code | Result |
| :--- | :--- | :--- |
| Reference Copy | `s2 = s1` | Same object |
| Shallow Copy | `copy.copy(s1)` | New object, shared nested refs |
| Deep Copy | `copy.deepcopy(s1)` | Completely independent |
| List Slice | `list2 = list1[:]` | Shallow copy for lists |
| Dict Copy | `dict2 = {**dict1}` | Shallow copy for dicts |

## Custom Copy Behavior

Implement `__copy__` and `__deepcopy__` for custom behavior:

```python
import copy

class Student:
    def __init__(self, name, scores):
        self.name = name
        self.scores = scores
    
    def __copy__(self):
        # Shallow copy: same list reference
        return Student(self.name, self.scores)
    
    def __deepcopy__(self, memo):
        # Deep copy: new list
        return Student(
            copy.deepcopy(self.name, memo),
            copy.deepcopy(self.scores, memo)
        )
```
