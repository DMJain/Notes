"""
Flyable interface using Python's ABC (Abstract Base Class).
"""
from abc import ABC, abstractmethod


class Flyable(ABC):
    """
    Interface for objects that can fly.
    Any class implementing this must provide a fly() method.
    """
    
    @abstractmethod
    def fly(self) -> None:
        """
        Make the object fly.
        Must be implemented by all subclasses.
        """
        pass
    
    def get_altitude(self) -> str:
        """
        Concrete method - can be used by all implementors.
        Shows that ABCs can have regular methods too.
        """
        return "Unknown altitude"
