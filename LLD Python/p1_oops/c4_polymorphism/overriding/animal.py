"""
Base Animal class demonstrating method overriding.
"""


class Animal:
    """Base class for all animals."""
    
    def __init__(self, name: str):
        self.name = name
    
    def speak(self) -> None:
        """Make a generic animal sound - to be overridden by subclasses."""
        print(f"{self.name} makes a generic sound.")
    
    def move(self) -> None:
        """Move - can be overridden by subclasses."""
        print(f"{self.name} is moving.")
    
    def __str__(self) -> str:
        return f"Animal(name='{self.name}')"
