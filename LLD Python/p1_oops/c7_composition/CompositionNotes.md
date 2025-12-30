# Composition & Association Notes (Python)

Object relationships describe how objects are connected.

## Association (Weak HAS-A)

- Objects are related but **exist independently**.
- One object uses another but doesn't own it.
- Example: `Student` has a `Teacher`, but Teacher exists on its own.

```python
class Student:
    def __init__(self, name: str, teacher: 'Teacher'):
        self.name = name
        self._teacher = teacher  # Passed from outside
    
# Usage
teacher = Teacher("Mr. Smith")
student = Student("Alice", teacher)  # Teacher passed in
# If student is deleted, teacher still exists
```

## Composition (Strong HAS-A)

- Part **cannot exist** without the whole.
- Whole object **creates and owns** the part.
- Example: `Car` has an `Engine`, Engine dies when Car is destroyed.

```python
class Car:
    def __init__(self, model: str, engine_type: str):
        self.model = model
        self._engine = Engine(engine_type)  # Created inside
    
# Usage
car = Car("Tesla", "Electric")
# Engine is created when Car is created
# If car is garbage collected, engine is also gone
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

## Aggregation (Middle Ground)

Aggregation is a middle ground:
- Whole contains parts (like composition)
- Parts can exist independently (like association)
- Often implemented with collections

```python
class Team:
    def __init__(self):
        self._members = []  # List of players
    
    def add_member(self, player: 'Player'):
        self._members.append(player)  # Players can exist outside team
```

## Garbage Collection in Python

Python uses automatic garbage collection. When an object has no references, it's cleaned up:

```python
# Association
teacher = Teacher("Smith")
student = Student("Alice", teacher)
del student  # student gone, teacher still exists

# Composition
car = Car("Model S", "Electric")
del car  # Both car AND its engine are garbage collected
```

## Dependency Injection Pattern

Association enables Dependency Injection for flexibility:

```python
class UserService:
    def __init__(self, database: 'Database'):
        self._database = database  # Injected, not created
    
    def get_user(self, user_id: int):
        return self._database.query(f"SELECT * FROM users WHERE id = {user_id}")

# Easy to swap implementations
service1 = UserService(MySQLDatabase())
service2 = UserService(PostgreSQLDatabase())
service_test = UserService(MockDatabase())  # For testing!
```

## When to Use What?

| Scenario | Use |
| :--- | :--- |
| Lifetime tied to parent | Composition |
| Shared resource | Association |
| Collections of items | Aggregation |
| Testing flexibility | Association + DI |
| Strong ownership | Composition |

## Python-Specific: Context Managers

For resource management in composition, use context managers:

```python
class DatabaseConnection:
    def __init__(self, connection_string: str):
        self.connection_string = connection_string
        self._connection = None
    
    def __enter__(self):
        self._connection = create_connection(self.connection_string)
        return self
    
    def __exit__(self, exc_type, exc_val, exc_tb):
        if self._connection:
            self._connection.close()

# Usage - connection is properly cleaned up
with DatabaseConnection("localhost:5432") as db:
    db.query("SELECT * FROM users")
```
