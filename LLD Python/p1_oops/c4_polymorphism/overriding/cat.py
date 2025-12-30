"""
Cat class demonstrating method overriding.
"""
from .animal import Animal


class Cat(Animal):
    """Cat class that overrides Animal methods."""
    
    def __init__(self, name: str, is_indoor: bool = True):
        super().__init__(name)
        self.is_indoor = is_indoor
    
    def speak(self) -> None:
        """Override: Cats meow!"""
        print(f"{self.name} says: Meow!")
    
    def move(self) -> None:
        """Override: Cats prowl."""
        print(f"{self.name} is prowling silently.")
    
    def climb(self) -> None:
        """Cat-specific method."""
        print(f"{self.name} is climbing a tree!")
    
    def __str__(self) -> str:
        location = "indoor" if self.is_indoor else "outdoor"
        return f"Cat(name='{self.name}', {location})"
