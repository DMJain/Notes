# Access Modifiers Notes (JavaScript)

JavaScript handles access control differently than Java. Here's a comparison:

## Visibility in JavaScript

| Approach | Syntax | Example | Enforced? |
| :--- | :--- | :--- | :---: |
| **Public** | No prefix | `this.name` | ✅ |
| **Private** | `#` prefix (ES2022) | `#privateVar` | ✅ |
| **Protected** | `_` prefix (convention) | `_protectedVar` | ❌ |
| **Module/Package** | `module.exports` | Export what you want | ✅ |

## Key Differences from Java

### 1. True Private Fields (ES2022+)
```javascript
class Parent {
    #privateVar = "Private";  // Truly private - SyntaxError if accessed outside
    
    getPrivate() {
        return this.#privateVar;  // Only accessible within the class
    }
}
```

### 2. Protected (Convention Only)
```javascript
class Parent {
    _protectedVar = "Protected";  // Just a naming convention
}

// NOT enforced - can still be accessed:
const p = new Parent();
console.log(p._protectedVar);  // Works! Convention only.
```

### 3. Module-based Access Control
```javascript
// internal.js (not exported)
function internalHelper() { ... }

// public.js
function publicAPI() { 
    internalHelper(); 
}
module.exports = { publicAPI };  // Only publicAPI is accessible
```

## Comparison Table

| Modifier | Java | JavaScript Equivalent |
| :--- | :--- | :--- |
| **public** | `public` keyword | Default (no prefix) |
| **protected** | `protected` keyword | `_` prefix (convention) |
| **default** (package-private) | No keyword | Module scope / not exported |
| **private** | `private` keyword | `#` prefix (ES2022) |

## Project Structure for Demonstration

- **p1/Parent.js**: Class containing fields with all 4 modifiers.
- **p1/SamePackageNeighbor.js**: Demonstrates access within the same directory/module.
- **p2/ChildInDiffPackage.js**: Demonstrates access from a subclass in a different directory.
- **p2/StrangerInDiffPackage.js**: Demonstrates access from an unrelated class in a different directory.
