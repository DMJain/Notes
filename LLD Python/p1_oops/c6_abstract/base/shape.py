"""
Abstract Shape class demonstrating abstract classes in Python.
"""
from abc import ABC, abstractmethod
import math


class Shape(ABC):
    """
    Abstract base class for all shapes.
    Demonstrates:
    - Abstract methods that must be implemented
    - Concrete methods that can be used directly
    - State (attributes) in an abstract class
    """
    
    def __init__(self, name: str):
        """
        Constructor - abstract classes can have constructors!
        
        Args:
            name: Name of the shape
        """
        self.name = name
    
    @abstractmethod
    def get_area(self) -> float:
        """
        Calculate the area of the shape.
        Must be implemented by all subclasses.
        """
        pass
    
    @abstractmethod
    def get_perimeter(self) -> float:
        """
        Calculate the perimeter of the shape.
        Must be implemented by all subclasses.
        """
        pass
    
    def display(self) -> None:
        """
        Concrete method - can be used by all shapes.
        Uses abstract method get_area() internally.
        """
        print(f"Shape: {self.name}")
        print(f"  Area: {self.get_area():.2f}")
        print(f"  Perimeter: {self.get_perimeter():.2f}")
    
    def __str__(self) -> str:
        return f"{self.name}(area={self.get_area():.2f})"
