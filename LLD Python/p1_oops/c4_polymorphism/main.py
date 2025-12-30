"""
Polymorphism Demonstration (Python)

This module demonstrates:
1. "Overloading" patterns (Python doesn't support true overloading)
2. Method overriding
3. Duck typing
4. super() usage
5. Operator overloading
"""
import sys
from pathlib import Path

sys.path.insert(0, str(Path(__file__).parent))

from overloading.calculator import Calculator, process
from overriding.animal import Animal
from overriding.dog import Dog
from overriding.cat import Cat


def demonstrate_overloading_patterns():
    """Show Python's alternatives to method overloading."""
    print("=" * 60)
    print("1. 'OVERLOADING' PATTERNS (Python Workarounds)")
    print("=" * 60)
    
    calc = Calculator()
    
    print("\n--- Default Parameters ---")
    print(f"add_with_defaults(1, 2):       {calc.add_with_defaults(1, 2)}")
    print(f"add_with_defaults(1, 2, 3):    {calc.add_with_defaults(1, 2, 3)}")
    print(f"add_with_defaults(1, 2, 3, 4): {calc.add_with_defaults(1, 2, 3, 4)}")
    
    print("\n--- Variable Arguments (*args) ---")
    print(f"add_with_args(1, 2):          {calc.add_with_args(1, 2)}")
    print(f"add_with_args(1, 2, 3, 4, 5): {calc.add_with_args(1, 2, 3, 4, 5)}")
    
    print("\n--- Type Checking ---")
    print(f"add_smart(1, 2):              {calc.add_smart(1, 2)}")
    print(f"add_smart('Hello', 'World'): {calc.add_smart('Hello', 'World')}")
    
    print("\n--- @singledispatch ---")
    process(42)
    process(3.14159)
    process("hello")
    process([1, 2, 3])


def demonstrate_method_overriding():
    """Show method overriding with Animal hierarchy."""
    print("\n" + "=" * 60)
    print("2. METHOD OVERRIDING (Runtime Polymorphism)")
    print("=" * 60)
    
    animal = Animal("Generic")
    dog = Dog("Buddy", "Golden Retriever")
    cat = Cat("Whiskers", is_indoor=True)
    
    print(f"\n{animal}:")
    animal.speak()
    animal.move()
    
    print(f"\n{dog}:")
    dog.speak()
    dog.move()
    dog.fetch()
    
    print(f"\n{cat}:")
    cat.speak()
    cat.move()
    cat.climb()


def demonstrate_duck_typing():
    """Show Python's duck typing in action."""
    print("\n" + "=" * 60)
    print("3. DUCK TYPING")
    print("=" * 60)
    
    print("\n'If it walks like a duck and quacks like a duck...'")
    
    class Robot:
        """Not an Animal, but has speak() method."""
        def __init__(self, name):
            self.name = name
        
        def speak(self):
            print(f"{self.name} says: BEEP BOOP!")
    
    class Person:
        """Not an Animal, but has speak() method."""
        def __init__(self, name):
            self.name = name
        
        def speak(self):
            print(f"{self.name} says: Hello there!")
    
    def make_speak(obj):
        """Works with ANY object that has a speak() method!"""
        obj.speak()
    
    print("\nCalling make_speak() on different types:")
    make_speak(Dog("Rex"))
    make_speak(Cat("Mittens"))
    make_speak(Robot("R2D2"))
    make_speak(Person("Alice"))


def demonstrate_polymorphic_list():
    """Show polymorphism with a list of different types."""
    print("\n" + "=" * 60)
    print("4. POLYMORPHISM WITH COLLECTIONS")
    print("=" * 60)
    
    animals = [
        Animal("Generic"),
        Dog("Max", "Labrador"),
        Cat("Luna"),
        Dog("Charlie", "Poodle"),
        Cat("Simba", is_indoor=False),
    ]
    
    print("\nProcessing a list of animals:")
    for animal in animals:
        print(f"{type(animal).__name__:10} -> ", end="")
        animal.speak()


def demonstrate_super_usage():
    """Show super() for calling parent methods."""
    print("\n" + "=" * 60)
    print("5. USING super() TO CALL PARENT METHODS")
    print("=" * 60)
    
    class VerboseDog(Dog):
        def speak(self):
            print(f"[VerboseDog] Before parent speak...")
            super().speak()  # Call Dog's speak
            print(f"[VerboseDog] After parent speak...")
    
    print("\nVerboseDog calling super().speak():")
    verbose = VerboseDog("Talky", "Husky")
    verbose.speak()


def demonstrate_operator_overloading():
    """Show operator overloading with special methods."""
    print("\n" + "=" * 60)
    print("6. OPERATOR OVERLOADING")
    print("=" * 60)
    
    class Vector:
        def __init__(self, x, y):
            self.x = x
            self.y = y
        
        def __add__(self, other):
            return Vector(self.x + other.x, self.y + other.y)
        
        def __sub__(self, other):
            return Vector(self.x - other.x, self.y - other.y)
        
        def __mul__(self, scalar):
            return Vector(self.x * scalar, self.y * scalar)
        
        def __eq__(self, other):
            return self.x == other.x and self.y == other.y
        
        def __str__(self):
            return f"Vector({self.x}, {self.y})"
        
        def __repr__(self):
            return self.__str__()
    
    v1 = Vector(1, 2)
    v2 = Vector(3, 4)
    
    print(f"\nv1 = {v1}")
    print(f"v2 = {v2}")
    print(f"v1 + v2 = {v1 + v2}")
    print(f"v2 - v1 = {v2 - v1}")
    print(f"v1 * 3 = {v1 * 3}")
    print(f"v1 == Vector(1, 2): {v1 == Vector(1, 2)}")


def main():
    print("\n" + "=" * 60)
    print(" POLYMORPHISM DEMONSTRATION (Python)")
    print("=" * 60)
    
    demonstrate_overloading_patterns()
    demonstrate_method_overriding()
    demonstrate_duck_typing()
    demonstrate_polymorphic_list()
    demonstrate_super_usage()
    demonstrate_operator_overloading()
    
    print("\n" + "=" * 60)
    print("Summary:")
    print("- Python doesn't support true overloading (use *args, defaults)")
    print("- Method overriding works like Java")
    print("- Duck typing: 'If it has the method, use it!'")
    print("- Use super() to call parent methods")
    print("- Use __add__, __eq__, etc. for operator overloading")
    print("=" * 60)


if __name__ == "__main__":
    main()
