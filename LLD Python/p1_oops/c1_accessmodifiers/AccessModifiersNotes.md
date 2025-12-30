# Access Modifiers Notes (Python)

Python handles access control through **naming conventions** rather than keywords. Here's a comparison:

## Visibility in Python

| Approach | Syntax | Example | Enforced? |
| :--- | :--- | :--- | :---: |
| **Public** | No prefix | `self.name` | ✅ |
| **Protected** | `_` prefix (convention) | `self._protected_var` | ❌ |
| **Private** | `__` prefix (name mangling) | `self.__private_var` | ⚠️ Partially |
| **Module/Package** | `__all__` list | `__all__ = ['PublicClass']` | ✅ |

## Key Differences from Java

### 1. No True Private (But Name Mangling Helps)

Python doesn't have true private members. The `__` prefix triggers **name mangling**:

```python
class Parent:
    def __init__(self):
        self.__private_var = "Private"  # Becomes _Parent__private_var
    
    def get_private(self):
        return self.__private_var  # Works inside the class

p = Parent()
# print(p.__private_var)    # AttributeError!
print(p._Parent__private_var)  # Works! (but don't do this)
```

### 2. Protected (Convention Only)

```python
class Parent:
    def __init__(self):
        self._protected_var = "Protected"  # Convention: "don't touch this"

# NOT enforced - can still be accessed:
p = Parent()
print(p._protected_var)  # Works! Just a convention.
```

### 3. Module-based Access Control

```python
# mymodule.py
__all__ = ['public_function']  # Only public_function is exported

def public_function():
    pass

def _internal_helper():  # Convention: internal
    pass

# When someone does: from mymodule import *
# Only public_function is imported
```

## Comparison Table

| Modifier | Java | Python Equivalent |
| :--- | :--- | :--- |
| **public** | `public` keyword | Default (no prefix) |
| **protected** | `protected` keyword | `_` prefix (convention) |
| **default** (package-private) | No keyword | Module scope / `__all__` |
| **private** | `private` keyword | `__` prefix (name mangling) |

## Name Mangling Explained

When you use `__` prefix, Python internally renames the attribute:

```python
class MyClass:
    def __init__(self):
        self.__secret = 42  # Internally stored as _MyClass__secret

obj = MyClass()
print(dir(obj))  # Shows '_MyClass__secret' in the list
```

**Why?** To prevent accidental overriding in subclasses:

```python
class Parent:
    def __init__(self):
        self.__value = "parent"

class Child(Parent):
    def __init__(self):
        super().__init__()
        self.__value = "child"  # Stored as _Child__value

c = Child()
print(c._Parent__value)  # "parent" - Parent's version preserved!
print(c._Child__value)   # "child" - Child's version
```

## Project Structure for Demonstration

- **p1/parent.py**: Class containing attributes with all access patterns.
- **p1/same_package_neighbor.py**: Demonstrates access within the same package.
- **p2/child_in_diff_package.py**: Demonstrates access from a subclass in a different package.
- **p2/stranger_in_diff_package.py**: Demonstrates access from an unrelated class in a different package.

## Best Practices

| Scenario | Use |
| :--- | :--- |
| Public API | No prefix: `self.name` |
| Internal implementation | `_` prefix: `self._internal` |
| Avoid name collisions in subclasses | `__` prefix: `self.__private` |
| Constants | ALL_CAPS: `MAX_SIZE = 100` |
