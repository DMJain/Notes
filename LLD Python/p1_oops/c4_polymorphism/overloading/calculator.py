"""
Calculator class demonstrating "overloading" patterns in Python.

Python doesn't support true method overloading, so we use:
1. Default parameters
2. *args and **kwargs
3. Type checking with isinstance
4. @singledispatch from functools
"""
from functools import singledispatch
from typing import Union


class Calculator:
    """Demonstrates various approaches to simulate method overloading."""
    
    # Approach 1: Default Parameters
    def add_with_defaults(self, a: float, b: float, c: float = 0, d: float = 0) -> float:
        """
        Add numbers using default parameters.
        Can be called with 2, 3, or 4 arguments.
        """
        return a + b + c + d
    
    # Approach 2: Variable Arguments (*args)
    def add_with_args(self, *args: float) -> float:
        """
        Add any number of arguments.
        
        Example:
            calc.add_with_args(1, 2)
            calc.add_with_args(1, 2, 3, 4, 5)
        """
        return sum(args)
    
    # Approach 3: Type Checking
    def add_smart(self, a: Union[int, float, str], b: Union[int, float, str]) -> Union[float, str]:
        """
        Add based on type - numbers are added, strings are concatenated.
        """
        if isinstance(a, str) and isinstance(b, str):
            return f"{a} {b}"
        elif isinstance(a, (int, float)) and isinstance(b, (int, float)):
            return a + b
        else:
            raise TypeError(f"Cannot add {type(a).__name__} and {type(b).__name__}")
    
    # Approach 4: Separate Methods (Most Pythonic)
    def add_two(self, a: float, b: float) -> float:
        """Add two numbers."""
        return a + b
    
    def add_three(self, a: float, b: float, c: float) -> float:
        """Add three numbers."""
        return a + b + c


# Approach 5: @singledispatch (for functions)
@singledispatch
def process(arg):
    """Default handler for unknown types."""
    print(f"Processing generic: {arg}")
    return arg


@process.register(int)
def _(arg: int) -> int:
    """Handler for integers."""
    print(f"Processing integer: {arg} -> {arg * 2}")
    return arg * 2


@process.register(float)
def _(arg: float) -> float:
    """Handler for floats."""
    print(f"Processing float: {arg} -> {arg:.2f}")
    return round(arg, 2)


@process.register(str)
def _(arg: str) -> str:
    """Handler for strings."""
    print(f"Processing string: {arg} -> {arg.upper()}")
    return arg.upper()


@process.register(list)
def _(arg: list) -> int:
    """Handler for lists."""
    print(f"Processing list: {arg} -> length {len(arg)}")
    return len(arg)


if __name__ == "__main__":
    calc = Calculator()
    
    print("=== Default Parameters ===")
    print(f"add_with_defaults(1, 2): {calc.add_with_defaults(1, 2)}")
    print(f"add_with_defaults(1, 2, 3): {calc.add_with_defaults(1, 2, 3)}")
    print(f"add_with_defaults(1, 2, 3, 4): {calc.add_with_defaults(1, 2, 3, 4)}")
    
    print("\n=== Variable Arguments ===")
    print(f"add_with_args(1, 2): {calc.add_with_args(1, 2)}")
    print(f"add_with_args(1, 2, 3, 4, 5): {calc.add_with_args(1, 2, 3, 4, 5)}")
    
    print("\n=== Type Checking ===")
    print(f"add_smart(1, 2): {calc.add_smart(1, 2)}")
    print(f"add_smart('Hello', 'World'): {calc.add_smart('Hello', 'World')}")
    
    print("\n=== @singledispatch ===")
    process(42)
    process(3.14159)
    process("hello")
    process([1, 2, 3])
