"""
Interfaces Demonstration (Python)

This module demonstrates:
1. Creating interfaces with ABC and @abstractmethod
2. Implementing interfaces
3. Multiple interface implementation
4. Interface as type (polymorphism)
5. Duck typing alternative
6. Protocol (structural subtyping)
"""
import sys
from pathlib import Path
from typing import Protocol

sys.path.insert(0, str(Path(__file__).parent))

from contract.flyable import Flyable
from contract.swimmable import Swimmable
from impl.bird import Bird
from impl.fish import Fish
from impl.duck import Duck


def demonstrate_interface_basics():
    """Show basic interface implementation."""
    print("=" * 60)
    print("1. INTERFACE BASICS (ABC + @abstractmethod)")
    print("=" * 60)
    
    print("\nCreating objects that implement interfaces:")
    
    bird = Bird("Tweety", "Canary")
    print(f"  Created: {bird}")
    bird.fly()
    
    fish = Fish("Nemo", "Clownfish")
    print(f"\n  Created: {fish}")
    fish.swim()


def demonstrate_cannot_instantiate():
    """Show that you cannot instantiate an abstract class."""
    print("\n" + "=" * 60)
    print("2. CANNOT INSTANTIATE ABSTRACT CLASS")
    print("=" * 60)
    
    print("\nTrying to instantiate Flyable directly:")
    try:
        flyable = Flyable()  # This will fail!
    except TypeError as e:
        print(f"  TypeError: {e}")


def demonstrate_multiple_interfaces():
    """Show multiple interface implementation."""
    print("\n" + "=" * 60)
    print("3. MULTIPLE INTERFACE IMPLEMENTATION")
    print("=" * 60)
    
    duck = Duck("Donald")
    print(f"\n{duck} implements both Flyable and Swimmable:")
    duck.fly()
    duck.swim()
    duck.quack()
    
    print("\nType checks:")
    print(f"  isinstance(duck, Flyable):   {isinstance(duck, Flyable)}")
    print(f"  isinstance(duck, Swimmable): {isinstance(duck, Swimmable)}")


def demonstrate_interface_as_type():
    """Show using interface as parameter type."""
    print("\n" + "=" * 60)
    print("4. INTERFACE AS TYPE (Polymorphism)")
    print("=" * 60)
    
    def make_fly(flyer: Flyable) -> None:
        """Accepts any Flyable object."""
        print(f"  Making {type(flyer).__name__} fly...")
        flyer.fly()
    
    def make_swim(swimmer: Swimmable) -> None:
        """Accepts any Swimmable object."""
        print(f"  Making {type(swimmer).__name__} swim...")
        swimmer.swim()
    
    bird = Bird("Eagle", "Bald Eagle")
    fish = Fish("Dory", "Blue Tang")
    duck = Duck("Daffy")
    
    print("\nPassing different objects to make_fly():")
    make_fly(bird)
    make_fly(duck)
    
    print("\nPassing different objects to make_swim():")
    make_swim(fish)
    make_swim(duck)


def demonstrate_duck_typing():
    """Show Python's duck typing alternative."""
    print("\n" + "=" * 60)
    print("5. DUCK TYPING ALTERNATIVE")
    print("=" * 60)
    
    print("\n'If it has fly(), it can fly!'")
    
    class Airplane:
        """Not a Flyable, but has fly() method."""
        def fly(self):
            print("Airplane is flying at 30,000 feet!")
    
    class Superman:
        """Not a Flyable, but has fly() method."""
        def fly(self):
            print("Superman is flying faster than a speeding bullet!")
    
    def make_it_fly(obj):
        """Duck typing: doesn't care about type, just method."""
        if hasattr(obj, 'fly') and callable(obj.fly):
            obj.fly()
        else:
            print(f"  {type(obj).__name__} cannot fly!")
    
    print("\nUsing duck typing:")
    make_it_fly(Bird("Sparrow", "House Sparrow"))
    make_it_fly(Airplane())
    make_it_fly(Superman())
    make_it_fly("just a string")  # Will fail gracefully


def demonstrate_protocol():
    """Show Protocol for structural subtyping."""
    print("\n" + "=" * 60)
    print("6. PROTOCOL (Structural Subtyping)")
    print("=" * 60)
    
    class Speakable(Protocol):
        """Protocol: any class with speak() method matches."""
        def speak(self) -> None: ...
    
    class Dog:
        def speak(self) -> None:
            print("Woof!")
    
    class Robot:
        def speak(self) -> None:
            print("Beep boop!")
    
    def make_speak(obj: Speakable) -> None:
        obj.speak()
    
    print("\nProtocol allows structural typing (no explicit inheritance):")
    make_speak(Dog())
    make_speak(Robot())


def demonstrate_incomplete_implementation():
    """Show what happens with incomplete implementation."""
    print("\n" + "=" * 60)
    print("7. INCOMPLETE IMPLEMENTATION ERROR")
    print("=" * 60)
    
    print("\nDefining a class that doesn't implement all abstract methods:")
    print("  class BadBird(Flyable):")
    print("      pass  # Forgot to implement fly()!")
    
    try:
        # This class definition is fine...
        class BadBird(Flyable):
            pass
        
        # ... but instantiation will fail!
        bad_bird = BadBird()
    except TypeError as e:
        print(f"\n  Result: TypeError when instantiating!")
        print(f"  {e}")


def main():
    print("\n" + "=" * 60)
    print(" INTERFACES DEMONSTRATION (Python)")
    print("=" * 60)
    
    demonstrate_interface_basics()
    demonstrate_cannot_instantiate()
    demonstrate_multiple_interfaces()
    demonstrate_interface_as_type()
    demonstrate_duck_typing()
    demonstrate_protocol()
    demonstrate_incomplete_implementation()
    
    print("\n" + "=" * 60)
    print("Summary:")
    print("- Use ABC + @abstractmethod for formal interfaces")
    print("- Use Protocol for structural subtyping (type hints)")
    print("- Use duck typing for flexibility")
    print("- Multiple inheritance is supported natively")
    print("- Cannot instantiate class with unimplemented abstract methods")
    print("=" * 60)


if __name__ == "__main__":
    main()
