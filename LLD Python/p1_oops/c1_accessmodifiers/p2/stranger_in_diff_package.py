"""
Demonstrates access from an unrelated class in a DIFFERENT package.
This is equivalent to a random class in a different Java package (no inheritance).
"""
import sys
from pathlib import Path

# Add parent directory to path for imports
sys.path.insert(0, str(Path(__file__).parent.parent))

from p1.parent import Parent


class StrangerInDiffPackage:
    """Unrelated class trying to access Parent from a different package."""
    
    def check_access(self):
        print("\n--- StrangerInDiffPackage (Different Package, No Relationship) ---")
        
        parent = Parent()
        
        # Public - accessible
        print(f"public_var: {parent.public_var} (Accessible)")
        
        # Protected - technically accessible but SHOULD NOT be used
        print(f"_protected_var: {parent._protected_var} (Accessible - but BAD PRACTICE!)")
        
        # Default - accessible
        print(f"default_var: {parent.default_var} (Accessible)")
        
        # Private - NOT directly accessible
        # print(parent.__private_var)  # AttributeError!
        print("__private_var: AttributeError (Not Accessible)")
        
        # Can still use public getter
        print(f"Via get_private_var(): {parent.get_private_var()} (Accessible via method)")
