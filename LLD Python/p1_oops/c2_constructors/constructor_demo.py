"""
Constructor Demonstration (Python)

This module demonstrates:
1. Default vs Parameterized constructors
2. Copy constructor pattern
3. Shallow vs Deep copy
4. Python's memory model and pass-by-object-reference
"""
import copy
from student import Student


def demonstrate_constructors():
    """Show different constructor patterns."""
    print("=" * 60)
    print("1. CONSTRUCTOR PATTERNS")
    print("=" * 60)
    
    # Default constructor (using default values)
    default_student = Student()
    print(f"\nDefault Constructor:")
    print(f"  {default_student}")
    
    # Parameterized constructor
    alice = Student("Alice", 20, [90, 85, 88])
    print(f"\nParameterized Constructor:")
    print(f"  {alice}")
    
    # Copy constructor (using class method)
    alice_copy = Student.from_student(alice)
    print(f"\nCopy Constructor (classmethod):")
    print(f"  Original: {alice}")
    print(f"  Copy:     {alice_copy}")


def demonstrate_shallow_vs_deep_copy():
    """Show the difference between shallow and deep copy."""
    print("\n" + "=" * 60)
    print("2. SHALLOW COPY vs DEEP COPY")
    print("=" * 60)
    
    original = Student("Bob", 22, [70, 75, 80])
    print(f"\nOriginal: {original}")
    print(f"Original scores id: {id(original.exam_scores)}")
    
    # Reference assignment
    print("\n--- Reference Assignment (s2 = s1) ---")
    ref = original
    print(f"Reference: {ref}")
    print(f"Same object? {ref is original}")  # True
    
    # Shallow copy
    print("\n--- Shallow Copy (copy.copy) ---")
    shallow = copy.copy(original)
    print(f"Shallow copy: {shallow}")
    print(f"Same object? {shallow is original}")  # False
    print(f"Same scores list? {shallow.exam_scores is original.exam_scores}")  # True!
    
    # Deep copy
    print("\n--- Deep Copy (copy.deepcopy) ---")
    deep = copy.deepcopy(original)
    print(f"Deep copy: {deep}")
    print(f"Same object? {deep is original}")  # False
    print(f"Same scores list? {deep.exam_scores is original.exam_scores}")  # False!
    
    # Demonstrate the difference
    print("\n--- Modifying copies ---")
    shallow.exam_scores[0] = 999
    deep.exam_scores[0] = 111
    
    print(f"After shallow.exam_scores[0] = 999:")
    print(f"  Original: {original}")  # Changed!
    print(f"  Shallow:  {shallow}")
    
    print(f"\nAfter deep.exam_scores[0] = 111:")
    print(f"  Deep:     {deep}")  # Only deep copy changed
    print(f"  Original still affected by shallow: {original}")


def demonstrate_pass_by_reference():
    """Show Python's pass-by-object-reference behavior."""
    print("\n" + "=" * 60)
    print("3. PASS-BY-OBJECT-REFERENCE")
    print("=" * 60)
    
    student = Student("Charlie", 25, [60, 65, 70])
    print(f"\nOriginal: {student}")
    
    def reassign_reference(s: Student):
        """Reassigning the reference doesn't affect the caller."""
        s = Student("NewPerson", 99, [100, 100, 100])
        print(f"  Inside function (after reassign): {s}")
    
    def modify_object(s: Student):
        """Modifying the object DOES affect the caller."""
        s.name = "Modified"
        s.exam_scores[0] = 1000
        print(f"  Inside function (after modify): {s}")
    
    print("\n--- Reassigning reference inside function ---")
    reassign_reference(student)
    print(f"After function call: {student}")  # Unchanged!
    
    print("\n--- Modifying object inside function ---")
    modify_object(student)
    print(f"After function call: {student}")  # Changed!


def demonstrate_mutable_default_pitfall():
    """Show the mutable default argument pitfall."""
    print("\n" + "=" * 60)
    print("4. MUTABLE DEFAULT ARGUMENT PITFALL")
    print("=" * 60)
    
    class BadStudent:
        """DON'T DO THIS!"""
        def __init__(self, name, scores=[]):  # BAD: mutable default
            self.name = name
            self.scores = scores
    
    class GoodStudent:
        """Do this instead."""
        def __init__(self, name, scores=None):  # GOOD: None default
            self.name = name
            self.scores = scores if scores is not None else []
    
    print("\n--- Bad: Mutable default argument ---")
    bad1 = BadStudent("A")
    bad1.scores.append(100)
    bad2 = BadStudent("B")
    print(f"bad1.scores: {bad1.scores}")
    print(f"bad2.scores: {bad2.scores}")  # Oops! Also has 100!
    print("Both share the SAME list!")
    
    print("\n--- Good: None default with conditional ---")
    good1 = GoodStudent("A")
    good1.scores.append(100)
    good2 = GoodStudent("B")
    print(f"good1.scores: {good1.scores}")
    print(f"good2.scores: {good2.scores}")  # Empty as expected!


def main():
    print("\n" + "=" * 60)
    print(" CONSTRUCTOR DEMONSTRATION (Python)")
    print("=" * 60)
    
    demonstrate_constructors()
    demonstrate_shallow_vs_deep_copy()
    demonstrate_pass_by_reference()
    demonstrate_mutable_default_pitfall()
    
    print("\n" + "=" * 60)
    print("Summary:")
    print("- Use None as default for mutable arguments")
    print("- Use copy.copy() for shallow copy")
    print("- Use copy.deepcopy() for deep copy")
    print("- Python passes object references, not copies")
    print("=" * 60)


if __name__ == "__main__":
    main()
