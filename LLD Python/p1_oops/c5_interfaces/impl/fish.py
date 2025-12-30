"""
Fish class implementing the Swimmable interface.
"""
import sys
from pathlib import Path

sys.path.insert(0, str(Path(__file__).parent.parent))

from contract.swimmable import Swimmable


class Fish(Swimmable):
    """
    Fish class that implements Swimmable.
    Must provide implementation of swim() method.
    """
    
    def __init__(self, name: str, species: str = "Unknown"):
        self.name = name
        self.species = species
    
    def swim(self) -> None:
        """Implementation of Swimmable.swim()"""
        print(f"{self.name} the {self.species} is swimming with fins!")
    
    def get_depth(self) -> str:
        """Override the concrete method from Swimmable."""
        return "Deep underwater"
    
    def blow_bubbles(self) -> None:
        """Fish-specific method."""
        print(f"{self.name} is blowing bubbles!")
    
    def __str__(self) -> str:
        return f"Fish(name='{self.name}', species='{self.species}')"
