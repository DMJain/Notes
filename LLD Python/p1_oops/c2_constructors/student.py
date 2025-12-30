"""
Student class demonstrating constructors, deep copy, and shallow copy in Python.
"""
import copy
from typing import List, Optional


class Student:
    """Student class with constructor patterns and copy examples."""
    
    def __init__(
        self, 
        name: str = "Unknown", 
        age: int = 18, 
        exam_scores: Optional[List[int]] = None
    ):
        """
        Constructor - handles both default and parameterized cases.
        
        Args:
            name: Student's name (default: "Unknown")
            age: Student's age (default: 18)
            exam_scores: List of exam scores (default: [0, 0, 0])
        
        Note: We use None as default for mutable arguments to avoid
        the mutable default argument pitfall!
        """
        self.name = name
        self.age = age
        # Create new list for each instance
        self.exam_scores = exam_scores if exam_scores is not None else [0, 0, 0]
    
    @classmethod
    def from_student(cls, other: 'Student') -> 'Student':
        """
        Copy constructor pattern using class method.
        Creates a DEEP COPY of another student.
        
        Args:
            other: The student to copy from
            
        Returns:
            A new Student instance with copied values
        """
        return cls(
            name=other.name,
            age=other.age,
            exam_scores=copy.deepcopy(other.exam_scores)
        )
    
    def shallow_copy_from(self, other: 'Student') -> None:
        """
        Demonstrates SHALLOW COPY - copies references, not values.
        
        After this, self.exam_scores points to the SAME list as other.exam_scores.
        Modifying one affects the other!
        """
        self.name = other.name
        self.age = other.age
        self.exam_scores = other.exam_scores  # Same reference!
    
    def __copy__(self) -> 'Student':
        """
        Called by copy.copy() - implements shallow copy.
        """
        return Student(
            name=self.name,
            age=self.age,
            exam_scores=self.exam_scores  # Same list reference
        )
    
    def __deepcopy__(self, memo: dict) -> 'Student':
        """
        Called by copy.deepcopy() - implements deep copy.
        
        Args:
            memo: Dictionary to handle circular references
        """
        return Student(
            name=copy.deepcopy(self.name, memo),
            age=self.age,  # int is immutable, no need for deepcopy
            exam_scores=copy.deepcopy(self.exam_scores, memo)
        )
    
    def __str__(self) -> str:
        """String representation of the student."""
        return f"Student(name='{self.name}', age={self.age}, scores={self.exam_scores})"
    
    def __repr__(self) -> str:
        """Developer-friendly representation."""
        return self.__str__()


if __name__ == "__main__":
    # Quick test
    s1 = Student("Alice", 20, [90, 85, 88])
    print(f"Original: {s1}")
    
    s2 = Student.from_student(s1)
    print(f"Deep Copy: {s2}")
    
    s2.exam_scores[0] = 100
    print(f"After modifying copy's scores:")
    print(f"  Original: {s1}")
    print(f"  Copy: {s2}")
