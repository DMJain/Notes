"""
Engine class for demonstrating Composition relationship.
"""


class Engine:
    """
    Engine class that is owned by a Car.
    Demonstrates the "part" in Composition relationship.
    """
    
    def __init__(self, engine_type: str, horsepower: int = 150):
        self.engine_type = engine_type
        self.horsepower = horsepower
        self._is_running = False
        print(f"  [Created] Engine ({self.engine_type}, {self.horsepower}HP)")
    
    def start(self) -> None:
        if not self._is_running:
            self._is_running = True
            print(f"    Engine ({self.engine_type}) started. Vroom!")
        else:
            print(f"    Engine is already running.")
    
    def stop(self) -> None:
        if self._is_running:
            self._is_running = False
            print(f"    Engine ({self.engine_type}) stopped.")
        else:
            print(f"    Engine is already stopped.")
    
    @property
    def is_running(self) -> bool:
        return self._is_running
    
    def __str__(self) -> str:
        status = "running" if self._is_running else "stopped"
        return f"Engine(type='{self.engine_type}', hp={self.horsepower}, {status})"
    
    def __del__(self):
        """Destructor - called when engine is garbage collected with its car."""
        print(f"  [GC] Engine ({self.engine_type}) is being garbage collected.")
