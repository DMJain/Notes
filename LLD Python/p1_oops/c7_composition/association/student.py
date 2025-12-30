"""
Student class demonstrating Association relationship with Teacher.
"""
from .teacher import Teacher


class Student:
    """
    Student class that HAS-A Teacher (Association).
    
    Association characteristics:
    - Teacher is passed from OUTSIDE (not created inside)
    - Teacher can exist independently of Student
    - Multiple students can share the same teacher
    """
    
    def __init__(self, name: str, teacher: Teacher):
        """
        Initialize a Student with an associated Teacher.
        
        Args:
            name: Student's name
            teacher: The teacher associated with this student (passed in)
        """
        self.name = name
        self._teacher = teacher  # Association: passed from outside
    
    @property
    def teacher(self) -> Teacher:
        """Get the associated teacher."""
        return self._teacher
    
    @teacher.setter
    def teacher(self, new_teacher: Teacher) -> None:
        """Change the associated teacher."""
        print(f"{self.name}'s teacher changed from {self._teacher.name} to {new_teacher.name}")
        self._teacher = new_teacher
    
    def study(self) -> None:
        print(f"{self.name} is studying {self._teacher.subject} with {self._teacher.name}.")
    
    def ask_question(self) -> None:
        print(f"{self.name} asks {self._teacher.name} a question about {self._teacher.subject}.")
    
    def __str__(self) -> str:
        return f"Student(name='{self.name}', teacher='{self._teacher.name}')"
    
    def __del__(self):
        """Destructor - notice teacher is NOT deleted with student."""
        print(f"  [GC] Student '{self.name}' is being garbage collected.")
        print(f"       (Teacher '{self._teacher.name}' continues to exist)")
