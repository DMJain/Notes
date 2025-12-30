"""
Access Modifier Demonstration (Python)

This module demonstrates Python's approach to access control:
- No prefix = public
- _ prefix = protected (convention)
- __ prefix = private (name mangling)
"""
import sys
from pathlib import Path

# Add current directory to path for imports
sys.path.insert(0, str(Path(__file__).parent))

from p1.parent import Parent
from p1.same_package_neighbor import SamePackageNeighbor
from p2.child_in_diff_package import ChildInDiffPackage
from p2.stranger_in_diff_package import StrangerInDiffPackage


def main():
    print("=== Access Modifier Demonstration (Python) ===\n")

    # 1. Same Class
    Parent().print_access()

    # 2. Same Package (Neighbor)
    SamePackageNeighbor().check_access()

    # 3. Different Package (Child)
    ChildInDiffPackage().check_access()

    # 4. Different Package (Stranger)
    StrangerInDiffPackage().check_access()
    
    print("\n" + "=" * 50)
    print("Summary:")
    print("- Public: Accessible everywhere")
    print("- Protected (_): Accessible everywhere, but convention says 'internal'")
    print("- Private (__): Name mangling prevents direct access")
    print("=" * 50)


if __name__ == "__main__":
    main()
