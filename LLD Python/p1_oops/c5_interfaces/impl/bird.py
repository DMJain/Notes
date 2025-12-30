"""
Bird class implementing the Flyable interface.
"""
import sys
from pathlib import Path

sys.path.insert(0, str(Path(__file__).parent.parent))

from contract.flyable import Flyable


class Bird(Flyable):
    """
    Bird class that implements Flyable.
    Must provide implementation of fly() method.
    """
    
    def __init__(self, name: str, species: str = "Unknown"):
        self.name = name
        self.species = species
    
    def fly(self) -> None:
        """Implementation of Flyable.fly()"""
        print(f"{self.name} the {self.species} is flying with wings!")
    
    def get_altitude(self) -> str:
        """Override the concrete method from Flyable."""
        return "High in the sky"
    
    def chirp(self) -> None:
        """Bird-specific method."""
        print(f"{self.name} is chirping!")
    
    def __str__(self) -> str:
        return f"Bird(name='{self.name}', species='{self.species}')"
