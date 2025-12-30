"""
Demonstrates access from a Child class in a DIFFERENT package (directory).
This is equivalent to a subclass in a different Java package.
"""
import sys
from pathlib import Path

# Add parent directory to path for imports
sys.path.insert(0, str(Path(__file__).parent.parent))

from p1.parent import Parent


class ChildInDiffPackage(Parent):
    """Child class extending Parent from a different package."""
    
    def __init__(self):
        super().__init__()
        self.child_var = "Child's own variable"
    
    def check_access(self):
        print("\n--- ChildInDiffPackage (Different Package, IS-A Relationship) ---")
        
        # Public - accessible
        print(f"public_var: {self.public_var} (Accessible)")
        
        # Protected - accessible (intended for subclasses)
        print(f"_protected_var: {self._protected_var} (Accessible - intended for subclasses)")
        
        # Default - accessible
        print(f"default_var: {self.default_var} (Accessible)")
        
        # Private - NOT directly accessible due to name mangling
        # print(self.__private_var)  # AttributeError!
        print("__private_var: AttributeError (Not Accessible - name mangling)")
        
        # Access via parent's method
        print(f"Via get_private_var(): {self.get_private_var()} (Accessible via inherited method)")
        
        # Technically can access via mangled name (BAD PRACTICE!)
        # print(f"_Parent__private_var: {self._Parent__private_var}")
