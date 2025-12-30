"""
Abstract Classes Demonstration (Python)

This module demonstrates:
1. Creating abstract classes with ABC
2. Abstract vs concrete methods
3. Cannot instantiate abstract classes
4. Template Method pattern
5. Abstract properties
"""
import sys
from pathlib import Path
from abc import ABC, abstractmethod

sys.path.insert(0, str(Path(__file__).parent))

from base.shape import Shape
from impl.circle import Circle
from impl.rectangle import Rectangle


def demonstrate_abstract_basics():
    """Show basic abstract class usage."""
    print("=" * 60)
    print("1. ABSTRACT CLASS BASICS")
    print("=" * 60)
    
    print("\nCreating concrete classes that extend abstract Shape:")
    
    circle = Circle(5.0)
    print(f"\n{circle}")
    circle.display()
    
    rectangle = Rectangle(4.0, 6.0)
    print(f"\n{rectangle}")
    rectangle.display()


def demonstrate_cannot_instantiate():
    """Show that abstract classes cannot be instantiated."""
    print("\n" + "=" * 60)
    print("2. CANNOT INSTANTIATE ABSTRACT CLASS")
    print("=" * 60)
    
    print("\nTrying to instantiate Shape directly:")
    try:
        shape = Shape("Generic")  # This will fail!
    except TypeError as e:
        print(f"  TypeError: {e}")


def demonstrate_polymorphism_with_abstract():
    """Show polymorphism with abstract base class."""
    print("\n" + "=" * 60)
    print("3. POLYMORPHISM WITH ABSTRACT BASE")
    print("=" * 60)
    
    shapes = [
        Circle(3.0),
        Rectangle(4.0, 5.0),
        Circle(2.5),
        Rectangle(6.0, 6.0),  # A square!
    ]
    
    print("\nProcessing a list of shapes:")
    total_area = 0
    for shape in shapes:
        print(f"  {type(shape).__name__:12} -> Area: {shape.get_area():.2f}")
        total_area += shape.get_area()
    
    print(f"\nTotal area: {total_area:.2f}")


def demonstrate_template_method():
    """Show the Template Method pattern."""
    print("\n" + "=" * 60)
    print("4. TEMPLATE METHOD PATTERN")
    print("=" * 60)
    
    class DataProcessor(ABC):
        """Abstract class using Template Method pattern."""
        
        def process(self) -> None:
            """Template method - defines algorithm skeleton."""
            print("  Starting processing...")
            data = self.fetch_data()
            print(f"    Fetched: {data}")
            result = self.transform(data)
            print(f"    Transformed: {result}")
            self.save(result)
            print("  Processing complete!")
        
        @abstractmethod
        def fetch_data(self) -> str:
            pass
        
        @abstractmethod
        def transform(self, data: str) -> str:
            pass
        
        @abstractmethod
        def save(self, result: str) -> None:
            pass
    
    class UpperCaseProcessor(DataProcessor):
        """Concrete processor that converts to uppercase."""
        
        def fetch_data(self) -> str:
            return "hello world"
        
        def transform(self, data: str) -> str:
            return data.upper()
        
        def save(self, result: str) -> None:
            print(f"    Saved: '{result}' to memory")
    
    class ReverseProcessor(DataProcessor):
        """Concrete processor that reverses the string."""
        
        def fetch_data(self) -> str:
            return "python"
        
        def transform(self, data: str) -> str:
            return data[::-1]
        
        def save(self, result: str) -> None:
            print(f"    Saved: '{result}' to file")
    
    print("\nUpperCaseProcessor:")
    UpperCaseProcessor().process()
    
    print("\nReverseProcessor:")
    ReverseProcessor().process()


def demonstrate_abstract_property():
    """Show abstract properties."""
    print("\n" + "=" * 60)
    print("5. ABSTRACT PROPERTIES")
    print("=" * 60)
    
    class Vehicle(ABC):
        @property
        @abstractmethod
        def max_speed(self) -> int:
            """Abstract property - must be implemented."""
            pass
        
        def describe(self) -> None:
            print(f"This vehicle has max speed: {self.max_speed} km/h")
    
    class Car(Vehicle):
        @property
        def max_speed(self) -> int:
            return 200
    
    class Bicycle(Vehicle):
        @property
        def max_speed(self) -> int:
            return 40
    
    print("\nVehicles with abstract max_speed property:")
    Car().describe()
    Bicycle().describe()


def demonstrate_incomplete_implementation():
    """Show what happens with incomplete implementation."""
    print("\n" + "=" * 60)
    print("6. INCOMPLETE IMPLEMENTATION ERROR")
    print("=" * 60)
    
    print("\nDefining a class that doesn't implement all abstract methods:")
    print("  class PartialShape(Shape):")
    print("      def get_area(self):  # Only implements one method!")
    print("          return 0")
    
    try:
        class PartialShape(Shape):
            def get_area(self) -> float:
                return 0
            # Missing get_perimeter()!
        
        partial = PartialShape("Partial")
    except TypeError as e:
        print(f"\n  Result: TypeError when instantiating!")
        print(f"  {e}")


def main():
    print("\n" + "=" * 60)
    print(" ABSTRACT CLASSES DEMONSTRATION (Python)")
    print("=" * 60)
    
    demonstrate_abstract_basics()
    demonstrate_cannot_instantiate()
    demonstrate_polymorphism_with_abstract()
    demonstrate_template_method()
    demonstrate_abstract_property()
    demonstrate_incomplete_implementation()
    
    print("\n" + "=" * 60)
    print("Summary:")
    print("- Use ABC + @abstractmethod for abstract classes")
    print("- Abstract classes can have state and concrete methods")
    print("- Cannot instantiate class with unimplemented abstract methods")
    print("- Use Template Method pattern for algorithm skeletons")
    print("- Abstract properties use @property + @abstractmethod")
    print("=" * 60)


if __name__ == "__main__":
    main()
