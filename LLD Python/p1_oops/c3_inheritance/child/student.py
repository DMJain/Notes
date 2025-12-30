"""
Student class demonstrating inheritance in Python.
Extends User and adds student-specific functionality.
"""
import sys
from pathlib import Path

# Add parent directory for imports
sys.path.insert(0, str(Path(__file__).parent.parent))

from base.user import User


class Student(User):
    """
    Student class extending User.
    Demonstrates inheritance and method overriding.
    """
    
    def __init__(self, name: str, email: str, batch_name: str):
        """
        Initialize a Student.
        
        Args:
            name: Student's name
            email: Student's email
            batch_name: Student's batch/cohort name
        """
        # Call parent constructor FIRST
        super().__init__(name, email)
        
        # Student-specific attribute
        self.batch_name = batch_name
        print(f"  [Student.__init__] Added batch: {self.batch_name}")
    
    def solve_problem(self) -> None:
        """
        Override parent method.
        Students solve Data Structures & Algorithms problems.
        """
        print(f"{self._name} (Student) is solving a DSA problem.")
    
    def study(self) -> None:
        """Student-specific method."""
        print(f"{self._name} is studying in batch {self.batch_name}.")
    
    def __str__(self) -> str:
        return f"Student(name='{self._name}', email='{self._email}', batch='{self.batch_name}')"
