"""
Teacher class for demonstrating Association relationship.
"""


class Teacher:
    """
    Teacher class that exists independently of students.
    Demonstrates independence in Association relationship.
    """
    
    def __init__(self, name: str, subject: str = "General"):
        self.name = name
        self.subject = subject
    
    def teach(self) -> None:
        print(f"{self.name} is teaching {self.subject}.")
    
    def grade(self, student_name: str, score: int) -> None:
        print(f"{self.name} is grading {student_name}: {score}/100")
    
    def __str__(self) -> str:
        return f"Teacher(name='{self.name}', subject='{self.subject}')"
    
    def __del__(self):
        """Destructor - called when object is garbage collected."""
        print(f"  [GC] Teacher '{self.name}' is being garbage collected.")
