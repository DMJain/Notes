"""
Circle class implementing the abstract Shape class.
"""
import sys
from pathlib import Path
import math

sys.path.insert(0, str(Path(__file__).parent.parent))

from base.shape import Shape


class Circle(Shape):
    """
    Circle implementation of Shape.
    Demonstrates implementing all abstract methods.
    """
    
    def __init__(self, radius: float):
        """
        Initialize a Circle.
        
        Args:
            radius: Radius of the circle
        """
        super().__init__("Circle")  # Call parent constructor
        self.radius = radius
    
    def get_area(self) -> float:
        """Implementation: Area = π * r²"""
        return math.pi * self.radius ** 2
    
    def get_perimeter(self) -> float:
        """Implementation: Perimeter = 2 * π * r"""
        return 2 * math.pi * self.radius
    
    def get_diameter(self) -> float:
        """Circle-specific method."""
        return 2 * self.radius
    
    def __str__(self) -> str:
        return f"Circle(radius={self.radius}, area={self.get_area():.2f})"
