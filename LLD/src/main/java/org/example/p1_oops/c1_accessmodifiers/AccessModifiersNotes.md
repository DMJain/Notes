# Access Modifiers Notes

Access modifiers determine the visibility of classes, methods, and variables.

## Visibility Table

| Modifier | Class | Package | Subclass (Same Pkg) | Subclass (Diff Pkg) | World (Diff Pkg) |
| :--- | :---: | :---: | :---: | :---: | :---: |
| **public** | ✅ | ✅ | ✅ | ✅ | ✅ |
| **protected** | ✅ | ✅ | ✅ | ✅ | ❌ |
| **default** (no keyword) | ✅ | ✅ | ✅ | ❌ | ❌ |
| **private** | ✅ | ❌ | ❌ | ❌ | ❌ |

## Explanations

1.  **Public**: The most permissive modifier. Accessible from anywhere in the application.
2.  **Protected**: Accessible within the same package and by subclasses (even if they are in a different package).
3.  **Default (Package-Private)**: The default access level if no modifier is specified. Accessible only within the same package.
4.  **Private**: The most restrictive modifier. Accessible only within the same class.

## Project Structure for Demonstration

- **p1.Parent**: The class containing fields with all 4 modifiers.
- **p1.SamePackageNeighbor**: Demonstrates access within the same package.
- **p2.ChildInDiffPackage**: Demonstrates access from a subclass in a different package.
- **p2.StrangerInDiffPackage**: Demonstrates access from an unrelated class in a different package.
