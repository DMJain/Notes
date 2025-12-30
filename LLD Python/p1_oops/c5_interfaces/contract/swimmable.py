"""
Swimmable interface using Python's ABC (Abstract Base Class).
"""
from abc import ABC, abstractmethod


class Swimmable(ABC):
    """
    Interface for objects that can swim.
    Any class implementing this must provide a swim() method.
    """
    
    @abstractmethod
    def swim(self) -> None:
        """
        Make the object swim.
        Must be implemented by all subclasses.
        """
        pass
    
    def get_depth(self) -> str:
        """
        Concrete method - can be used by all implementors.
        """
        return "Unknown depth"
