"""
Duck class implementing BOTH Flyable and Swimmable interfaces.
Demonstrates multiple interface implementation in Python.
"""
import sys
from pathlib import Path

sys.path.insert(0, str(Path(__file__).parent.parent))

from contract.flyable import Flyable
from contract.swimmable import Swimmable


class Duck(Flyable, Swimmable):
    """
    Duck class that implements BOTH Flyable and Swimmable.
    Must provide implementation of both fly() and swim() methods.
    """
    
    def __init__(self, name: str):
        self.name = name
    
    def fly(self) -> None:
        """Implementation of Flyable.fly()"""
        print(f"{self.name} the duck is flying low over the pond!")
    
    def swim(self) -> None:
        """Implementation of Swimmable.swim()"""
        print(f"{self.name} the duck is swimming gracefully!")
    
    def get_altitude(self) -> str:
        """Duck-specific altitude."""
        return "Low altitude"
    
    def get_depth(self) -> str:
        """Duck-specific depth."""
        return "Surface level"
    
    def quack(self) -> None:
        """Duck-specific method."""
        print(f"{self.name} says: Quack quack!")
    
    def __str__(self) -> str:
        return f"Duck(name='{self.name}')"
