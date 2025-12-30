"""
Demonstrates access from a class in the same package (directory).
In Python, "package" roughly corresponds to a directory with __init__.py
"""
from .parent import Parent


class SamePackageNeighbor:
    def check_access(self):
        print("\n--- SamePackageNeighbor (Same Package, No Relationship) ---")
        
        parent = Parent()
        
        # Public - accessible
        print(f"public_var: {parent.public_var} (Accessible)")
        
        # Protected - accessible but conventionally shouldn't be used
        print(f"_protected_var: {parent._protected_var} (Accessible - but convention says don't!)")
        
        # Default - accessible (same as public in Python)
        print(f"default_var: {parent.default_var} (Accessible)")
        
        # Private - NOT directly accessible due to name mangling
        # print(parent.__private_var)  # AttributeError!
        print("__private_var: AttributeError (Not Accessible - name mangling)")
        
        # However, we can access via the getter
        print(f"Via get_private_var(): {parent.get_private_var()} (Accessible via method)")
        
        # Or technically via the mangled name (DON'T DO THIS!)
        # print(f"_Parent__private_var: {parent._Parent__private_var}")
