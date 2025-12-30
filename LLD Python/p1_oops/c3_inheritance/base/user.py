"""
Base User class demonstrating inheritance in Python.
"""


class User:
    """Base class for all users in the system."""
    
    def __init__(self, name: str, email: str):
        """
        Initialize a User.
        
        Args:
            name: User's name
            email: User's email address
        """
        # Protected attributes (by convention)
        self._name = name
        self._email = email
        print(f"  [User.__init__] Called for: {self._name}")
    
    def login(self) -> None:
        """Simulate user login."""
        print(f"{self._name} has logged in.")
    
    def solve_problem(self) -> None:
        """Generic problem solving - to be overridden by subclasses."""
        print(f"{self._name} (User) is solving a generic problem.")
    
    @property
    def name(self) -> str:
        """Getter for name."""
        return self._name
    
    @property
    def email(self) -> str:
        """Getter for email."""
        return self._email
    
    def __str__(self) -> str:
        return f"User(name='{self._name}', email='{self._email}')"
