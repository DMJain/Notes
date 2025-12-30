"""
Inheritance Demonstration (Python)

This module demonstrates:
1. Basic inheritance with super()
2. Method overriding
3. Runtime polymorphism
4. Type checking with isinstance()
5. Method Resolution Order (MRO)
"""
import sys
from pathlib import Path

sys.path.insert(0, str(Path(__file__).parent))

from base.user import User
from child.student import Student
from child.mentor import Mentor


def demonstrate_basic_inheritance():
    """Show basic inheritance and constructor chaining."""
    print("=" * 60)
    print("1. BASIC INHERITANCE & CONSTRUCTOR CHAINING")
    print("=" * 60)
    
    print("\nCreating a User:")
    user = User("John", "john@email.com")
    print(f"  Result: {user}")
    
    print("\nCreating a Student (notice constructor chain):")
    student = Student("Alice", "alice@email.com", "Sept2023")
    print(f"  Result: {student}")
    
    print("\nCreating a Mentor:")
    mentor = Mentor("Bob", "bob@email.com", "System Design")
    print(f"  Result: {mentor}")


def demonstrate_method_overriding():
    """Show method overriding in action."""
    print("\n" + "=" * 60)
    print("2. METHOD OVERRIDING (Runtime Polymorphism)")
    print("=" * 60)
    
    user = User("GenericUser", "user@email.com")
    student = Student("StudentUser", "student@email.com", "Batch1")
    mentor = Mentor("MentorUser", "mentor@email.com", "DSA")
    
    print("\nCalling solve_problem() on each:")
    print("  User ->", end=" ")
    user.solve_problem()
    
    print("  Student ->", end=" ")
    student.solve_problem()
    
    print("  Mentor ->", end=" ")
    mentor.solve_problem()


def demonstrate_polymorphism():
    """Show polymorphism with a list of different types."""
    print("\n" + "=" * 60)
    print("3. POLYMORPHISM (Same Interface, Different Behavior)")
    print("=" * 60)
    
    print("\nCreating objects silently...")
    
    # Suppress constructor print statements for cleaner output
    class QuietUser(User):
        def __init__(self, *args):
            object.__setattr__(self, '_name', args[0])
            object.__setattr__(self, '_email', args[1])
    
    class QuietStudent(QuietUser):
        def __init__(self, name, email, batch):
            super().__init__(name, email)
            self.batch_name = batch
        
        def solve_problem(self):
            print(f"{self._name} (Student) is solving a DSA problem.")
    
    class QuietMentor(QuietUser):
        def __init__(self, name, email, spec):
            super().__init__(name, email)
            self.specialization = spec
        
        def solve_problem(self):
            print(f"{self._name} (Mentor) is solving a {self.specialization} problem.")
    
    # List of different types (polymorphism!)
    users = [
        QuietUser("User1", "u1@email.com"),
        QuietStudent("Student1", "s1@email.com", "Batch1"),
        QuietMentor("Mentor1", "m1@email.com", "Databases"),
    ]
    
    print("\nProcessing a list of different user types:")
    for user in users:
        print(f"  Type: {type(user).__name__:15} -> ", end="")
        user.solve_problem()


def demonstrate_type_checking():
    """Show isinstance() and issubclass() usage."""
    print("\n" + "=" * 60)
    print("4. TYPE CHECKING (isinstance & issubclass)")
    print("=" * 60)
    
    student = Student("TypeCheckStudent", "tc@email.com", "Batch99")
    
    print(f"\nChecking: {student}")
    print(f"  isinstance(student, Student): {isinstance(student, Student)}")
    print(f"  isinstance(student, User):    {isinstance(student, User)}")
    print(f"  isinstance(student, Mentor):  {isinstance(student, Mentor)}")
    
    print("\nClass hierarchy checks:")
    print(f"  issubclass(Student, User):   {issubclass(Student, User)}")
    print(f"  issubclass(User, Student):   {issubclass(User, Student)}")
    print(f"  issubclass(Student, object): {issubclass(Student, object)}")


def demonstrate_mro():
    """Show Method Resolution Order with multiple inheritance."""
    print("\n" + "=" * 60)
    print("5. METHOD RESOLUTION ORDER (MRO)")
    print("=" * 60)
    
    class A:
        def greet(self):
            print("Hello from A")
    
    class B(A):
        def greet(self):
            print("Hello from B")
    
    class C(A):
        def greet(self):
            print("Hello from C")
    
    class D(B, C):  # Multiple inheritance!
        pass
    
    print("\nClass D inherits from B and C (both inherit from A)")
    print(f"\nMRO: {[cls.__name__ for cls in D.__mro__]}")
    
    d = D()
    print("\nCalling d.greet():")
    d.greet()  # Will call B's greet because B comes before C in MRO


def demonstrate_negative_cases():
    """Show common errors and how Python handles them."""
    print("\n" + "=" * 60)
    print("6. NEGATIVE CASES (Access Missing Attributes)")
    print("=" * 60)
    
    user = User("BaseUser", "base@email.com")
    
    print("\nTrying to access 'batch_name' on a User object:")
    
    # Method 1: Try/except
    try:
        print(f"  user.batch_name: {user.batch_name}")
    except AttributeError as e:
        print(f"  AttributeError: {e}")
    
    # Method 2: hasattr
    print(f"\n  hasattr(user, 'batch_name'): {hasattr(user, 'batch_name')}")
    
    # Method 3: getattr with default
    batch = getattr(user, 'batch_name', 'N/A')
    print(f"  getattr(user, 'batch_name', 'N/A'): {batch}")


def main():
    print("\n" + "=" * 60)
    print(" INHERITANCE DEMONSTRATION (Python)")
    print("=" * 60)
    
    demonstrate_basic_inheritance()
    demonstrate_method_overriding()
    demonstrate_polymorphism()
    demonstrate_type_checking()
    demonstrate_mro()
    demonstrate_negative_cases()
    
    print("\n" + "=" * 60)
    print("Summary:")
    print("- Use super().__init__() to call parent constructor")
    print("- Python supports multiple inheritance (use carefully!)")
    print("- Method overriding works the same as Java")
    print("- Use isinstance() for runtime type checking")
    print("- Check MRO with ClassName.__mro__")
    print("=" * 60)


if __name__ == "__main__":
    main()
