"""
Composition & Association Demonstration (Python)

This module demonstrates:
1. Association (weak HAS-A): Student-Teacher relationship
2. Composition (strong HAS-A): Car-Engine relationship
3. Aggregation: Team-Player relationship
4. Dependency Injection pattern
"""
import sys
from pathlib import Path
import gc

sys.path.insert(0, str(Path(__file__).parent))

from association.teacher import Teacher
from association.student import Student
from composition.car import Car
from composition.engine import Engine


def demonstrate_association():
    """Show Association relationship (weak HAS-A)."""
    print("=" * 60)
    print("1. ASSOCIATION (Weak HAS-A): Student-Teacher")
    print("=" * 60)
    
    print("\nCreate a teacher (exists independently):")
    teacher = Teacher("Mr. Smith", "Mathematics")
    print(f"  {teacher}")
    
    print("\nCreate students with the SAME teacher (passed in):")
    student1 = Student("Alice", teacher)
    student2 = Student("Bob", teacher)
    print(f"  {student1}")
    print(f"  {student2}")
    
    print("\nStudents interact with shared teacher:")
    student1.study()
    student2.ask_question()
    
    print("\nKey point: Both students share the SAME teacher object!")
    print(f"  student1.teacher is student2.teacher: {student1.teacher is student2.teacher}")


def demonstrate_composition():
    """Show Composition relationship (strong HAS-A)."""
    print("\n" + "=" * 60)
    print("2. COMPOSITION (Strong HAS-A): Car-Engine")
    print("=" * 60)
    
    print("\nCreate a car (engine created INSIDE):")
    car = Car("Tesla Model 3", "Electric", 283)
    print(f"  {car}")
    
    print("\nOperate the car:")
    car.drive()  # Can't drive - engine not started
    car.start()
    car.drive()  # Now it works!
    car.stop()
    
    print("\nKey point: Engine is owned by Car, created inside!")


def demonstrate_lifetime_difference():
    """Show the lifetime difference between Association and Composition."""
    print("\n" + "=" * 60)
    print("3. LIFETIME DIFFERENCE")
    print("=" * 60)
    
    # Force garbage collection to clean up any previous objects
    gc.collect()
    
    print("\n--- Association: Object survives ---")
    print("Creating teacher and student...")
    teacher = Teacher("Dr. Jones", "Physics")
    student = Student("Charlie", teacher)
    
    print("\nDeleting student...")
    del student
    gc.collect()  # Force garbage collection
    
    print(f"\nTeacher still exists: {teacher}")
    teacher.teach()
    
    print("\n--- Composition: Object dies with owner ---")
    print("Creating car (with engine inside)...")
    car = Car("BMW M3", "V6", 450)
    
    print("\nDeleting car...")
    del car
    gc.collect()  # Force garbage collection
    print("Car and Engine are both gone!")
    
    # Clean up teacher
    del teacher
    gc.collect()


def demonstrate_aggregation():
    """Show Aggregation (middle ground)."""
    print("\n" + "=" * 60)
    print("4. AGGREGATION (Collection of Independent Objects)")
    print("=" * 60)
    
    class Player:
        def __init__(self, name: str):
            self.name = name
        
        def __str__(self):
            return f"Player('{self.name}')"
    
    class Team:
        def __init__(self, name: str):
            self.name = name
            self._members = []  # Aggregation: collection of players
        
        def add_member(self, player: Player):
            self._members.append(player)
            print(f"  {player.name} joined {self.name}")
        
        def list_members(self):
            print(f"  {self.name} roster: {[p.name for p in self._members]}")
    
    print("\nCreate players (exist independently):")
    player1 = Player("LeBron")
    player2 = Player("Curry")
    player3 = Player("Durant")
    
    print("\nCreate team and add players:")
    team = Team("All-Stars")
    team.add_member(player1)
    team.add_member(player2)
    team.add_member(player3)
    team.list_members()
    
    print("\nPlayers can belong to multiple teams:")
    team2 = Team("Olympic Team")
    team2.add_member(player1)
    team2.add_member(player3)
    team2.list_members()


def demonstrate_dependency_injection():
    """Show Dependency Injection using Association."""
    print("\n" + "=" * 60)
    print("5. DEPENDENCY INJECTION PATTERN")
    print("=" * 60)
    
    class Database:
        def query(self, sql: str): pass
    
    class MySQLDatabase(Database):
        def query(self, sql: str):
            return f"[MySQL] Result of: {sql}"
    
    class PostgreSQLDatabase(Database):
        def query(self, sql: str):
            return f"[PostgreSQL] Result of: {sql}"
    
    class MockDatabase(Database):
        def query(self, sql: str):
            return f"[MOCK] Fake result for testing"
    
    class UserService:
        def __init__(self, database: Database):
            self._db = database  # Injected, not created
        
        def get_user(self, user_id: int):
            return self._db.query(f"SELECT * FROM users WHERE id = {user_id}")
    
    print("\nSame UserService with different database implementations:")
    
    print("\n  Using MySQL:")
    service_mysql = UserService(MySQLDatabase())
    print(f"    {service_mysql.get_user(1)}")
    
    print("\n  Using PostgreSQL:")
    service_postgres = UserService(PostgreSQLDatabase())
    print(f"    {service_postgres.get_user(1)}")
    
    print("\n  Using Mock (for testing):")
    service_mock = UserService(MockDatabase())
    print(f"    {service_mock.get_user(1)}")


def main():
    print("\n" + "=" * 60)
    print(" COMPOSITION & ASSOCIATION DEMONSTRATION (Python)")
    print("=" * 60)
    
    demonstrate_association()
    demonstrate_composition()
    demonstrate_lifetime_difference()
    demonstrate_aggregation()
    demonstrate_dependency_injection()
    
    print("\n" + "=" * 60)
    print("Summary:")
    print("- Association: Part exists independently (passed in)")
    print("- Composition: Part owned by whole (created inside)")
    print("- Aggregation: Collection of independent parts")
    print("- Use Association for flexibility and dependency injection")
    print("- Use Composition when lifetime must be tied together")
    print("=" * 60)


if __name__ == "__main__":
    main()
