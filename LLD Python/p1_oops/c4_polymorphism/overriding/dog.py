"""
Dog class demonstrating method overriding.
"""
from .animal import Animal


class Dog(Animal):
    """Dog class that overrides Animal methods."""
    
    def __init__(self, name: str, breed: str = "Unknown"):
        super().__init__(name)
        self.breed = breed
    
    def speak(self) -> None:
        """Override: Dogs bark!"""
        print(f"{self.name} says: Woof! Woof!")
    
    def move(self) -> None:
        """Override: Dogs run."""
        print(f"{self.name} is running on four legs.")
    
    def fetch(self) -> None:
        """Dog-specific method."""
        print(f"{self.name} is fetching the ball!")
    
    def __str__(self) -> str:
        return f"Dog(name='{self.name}', breed='{self.breed}')"
