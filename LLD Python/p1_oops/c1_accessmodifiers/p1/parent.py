"""
Parent class demonstrating access modifiers in Python

Note: Python doesn't have traditional access modifiers like Java.
- No prefix = public (default)
- _ prefix = convention for "protected" (not enforced)
- __ prefix = "private" (name mangling)
- Modules can simulate package-level access via __all__
"""


class Parent:
    # Class attribute (shared by all instances)
    class_variable = "Class Level"
    
    def __init__(self):
        # Public (accessible from anywhere)
        self.public_var = "Public"
        
        # Protected (by convention using _ prefix - not enforced)
        self._protected_var = "Protected"
        
        # Default/Package (simulated via module - we export this class)
        # In Python, this is effectively the same as public
        self.default_var = "Default"
        
        # Private (name mangling using __)
        # Internally stored as _Parent__private_var
        self.__private_var = "Private"

    def print_access(self):
        print("\n--- Inside Parent (Same Class) ---")
        print(f"public_var: {self.public_var} (Accessible)")
        print(f"_protected_var: {self._protected_var} (Accessible)")
        print(f"default_var: {self.default_var} (Accessible)")
        print(f"__private_var: {self.__private_var} (Accessible)")

    def get_private_var(self):
        """Getter for private var (for controlled access)"""
        return self.__private_var
    
    @property
    def private_var(self):
        """Property-based access to private variable"""
        return self.__private_var
