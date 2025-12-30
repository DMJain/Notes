"""
Rectangle class implementing the abstract Shape class.
"""
import sys
from pathlib import Path

sys.path.insert(0, str(Path(__file__).parent.parent))

from base.shape import Shape


class Rectangle(Shape):
    """
    Rectangle implementation of Shape.
    Demonstrates implementing all abstract methods.
    """
    
    def __init__(self, width: float, height: float):
        """
        Initialize a Rectangle.
        
        Args:
            width: Width of the rectangle
            height: Height of the rectangle
        """
        super().__init__("Rectangle")
        self.width = width
        self.height = height
    
    def get_area(self) -> float:
        """Implementation: Area = width * height"""
        return self.width * self.height
    
    def get_perimeter(self) -> float:
        """Implementation: Perimeter = 2 * (width + height)"""
        return 2 * (self.width + self.height)
    
    def is_square(self) -> bool:
        """Rectangle-specific method."""
        return self.width == self.height
    
    def __str__(self) -> str:
        return f"Rectangle(width={self.width}, height={self.height}, area={self.get_area():.2f})"
