"""
Mentor class demonstrating inheritance in Python.
Extends User and adds mentor-specific functionality.
"""
import sys
from pathlib import Path

sys.path.insert(0, str(Path(__file__).parent.parent))

from base.user import User


class Mentor(User):
    """
    Mentor class extending User.
    Demonstrates inheritance and method overriding.
    """
    
    def __init__(self, name: str, email: str, specialization: str):
        """
        Initialize a Mentor.
        
        Args:
            name: Mentor's name
            email: Mentor's email
            specialization: Mentor's area of expertise
        """
        super().__init__(name, email)
        self.specialization = specialization
        print(f"  [Mentor.__init__] Added specialization: {self.specialization}")
    
    def solve_problem(self) -> None:
        """
        Override parent method.
        Mentors solve problems in their specialization area.
        """
        print(f"{self._name} (Mentor) is solving a {self.specialization} problem.")
    
    def teach(self) -> None:
        """Mentor-specific method."""
        print(f"{self._name} is teaching {self.specialization}.")
    
    def __str__(self) -> str:
        return f"Mentor(name='{self._name}', email='{self._email}', spec='{self.specialization}')"
