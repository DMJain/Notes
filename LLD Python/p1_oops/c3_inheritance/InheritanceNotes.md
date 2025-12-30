# Inheritance & Polymorphism Notes (Python)

## Inheritance

Inheritance allows a Child class to acquire fields and methods from a Parent class.

### Python Syntax
```python
class Child(Parent):
    def __init__(self, parent_args, child_args):
        super().__init__(parent_args)  # Call parent constructor
        self.child_property = child_args
```

### Key Features
- **Keyword**: Class name in parentheses `class Child(Parent)`
- **Constructor Chaining**: Must call `super().__init__()` to initialize parent
- **Multiple Inheritance**: Python supports it natively!

## Multiple Inheritance

Unlike Java, Python supports inheriting from multiple classes:

```python
class Flyable:
    def fly(self):
        print("Flying")

class Swimmable:
    def swim(self):
        print("Swimming")

class Duck(Flyable, Swimmable):
    pass

duck = Duck()
duck.fly()   # Works!
duck.swim()  # Works!
```

## Method Resolution Order (MRO)

Python uses the C3 linearization algorithm to determine method order:

```python
class A:
    def greet(self):
        print("A")

class B(A):
    def greet(self):
        print("B")

class C(A):
    def greet(self):
        print("C")

class D(B, C):  # Order matters!
    pass

d = D()
d.greet()  # Prints "B" (follows MRO: D -> B -> C -> A)

# Check MRO:
print(D.__mro__)  # (<class 'D'>, <class 'B'>, <class 'C'>, <class 'A'>, <class 'object'>)
```

## Polymorphism

"Many forms". An object can behave differently based on its actual type.

### Runtime Polymorphism (Method Overriding)
- Child provides its own implementation of a Parent method.
- Python determines the method at **runtime** based on the actual object.

```python
class User:
    def solve_problem(self):
        print("Generic problem solving")

class Student(User):
    def solve_problem(self):  # Overrides parent
        print("Solving DSA problems")

u = Student()
u.solve_problem()  # Calls Student's solve_problem()
```

## Type Checking in Python

Since Python is dynamically typed, use `isinstance()` for type checking:

```python
student = Student("Alice", "alice@email.com", "Batch1")

print(isinstance(student, Student))  # True
print(isinstance(student, User))     # True
print(isinstance(student, Mentor))   # False

# Check class hierarchy
print(issubclass(Student, User))     # True
```

## The `super()` Function

`super()` returns a proxy object that delegates method calls to the parent:

```python
class Parent:
    def __init__(self, name):
        self.name = name
    
    def greet(self):
        print(f"Hello, I'm {self.name}")

class Child(Parent):
    def __init__(self, name, age):
        super().__init__(name)  # Call parent's __init__
        self.age = age
    
    def greet(self):
        super().greet()  # Call parent's greet
        print(f"I'm {self.age} years old")
```

## Differences: Java vs Python

| Aspect | Java | Python |
| :--- | :--- | :--- |
| **Multiple Inheritance** | No (interfaces only) | Yes |
| **@Override** | Yes (annotation) | No (just override) |
| **Method Resolution** | Compile + Runtime | Runtime only (MRO) |
| **Type Errors** | Compile-time | Runtime |
| **super() call** | `super(args)` | `super().__init__(args)` |

## Negative Cases

| Scenario | Java | Python |
| :--- | :--- | :--- |
| Parent accessing Child field | COMPILE ERROR | Works (returns `AttributeError` if missing) |
| Calling non-existent method | COMPILE ERROR | `AttributeError` at runtime |

### Python Behavior
```python
user = User("Test", "test@email.com")
# user.batch_name  # AttributeError: 'User' has no attribute 'batch_name'

# Use hasattr() to check before accessing
if hasattr(user, 'batch_name'):
    print(user.batch_name)

# Or use getattr() with default
batch = getattr(user, 'batch_name', 'N/A')
```
