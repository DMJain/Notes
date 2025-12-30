"""
Car class demonstrating Composition relationship with Engine.
"""
from .engine import Engine


class Car:
    """
    Car class that HAS-A Engine (Composition).
    
    Composition characteristics:
    - Engine is created INSIDE the Car (not passed in)
    - Engine cannot exist without the Car
    - When Car is destroyed, Engine is destroyed too
    """
    
    def __init__(self, model: str, engine_type: str, horsepower: int = 150):
        """
        Initialize a Car with its own Engine.
        
        Args:
            model: Car model name
            engine_type: Type of engine to create
            horsepower: Engine horsepower
        """
        self.model = model
        # Composition: Engine created INSIDE Car
        self._engine = Engine(engine_type, horsepower)
        print(f"  [Created] Car ({self.model}) with {engine_type} engine")
    
    def start(self) -> None:
        print(f"{self.model} starting...")
        self._engine.start()
    
    def stop(self) -> None:
        print(f"{self.model} stopping...")
        self._engine.stop()
    
    def drive(self) -> None:
        if self._engine.is_running:
            print(f"{self.model} is driving! 🚗")
        else:
            print(f"{self.model} can't drive - engine is off!")
    
    @property
    def engine(self) -> Engine:
        """Get the car's engine (read-only access)."""
        return self._engine
    
    def __str__(self) -> str:
        return f"Car(model='{self.model}', engine={self._engine})"
    
    def __del__(self):
        """Destructor - Engine is also destroyed with Car."""
        print(f"  [GC] Car '{self.model}' is being garbage collected.")
        print(f"       (Engine will also be garbage collected)")
