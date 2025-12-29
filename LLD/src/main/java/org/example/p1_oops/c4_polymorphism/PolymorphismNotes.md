# Polymorphism Notes

"Poly" = Many, "Morph" = Forms. An object can take many forms.

## 1. Compile-Time Polymorphism (Method Overloading)
- **Same method name, different parameters** (type or number).
- Decision made at **Compile time**.
- Example: `add(int, int)`, `add(int, int, int)`, `add(double, double)`.

> **Note**: Return type ALONE cannot differentiate overloaded methods.

## 2. Runtime Polymorphism (Method Overriding)
- **Child provides its own implementation** of a Parent method.
- Decision made at **Runtime** (Dynamic Method Dispatch).
- Example: `Animal.speak()` vs `Dog.speak()`.

## Key Concept: Dynamic Method Dispatch
```java
Animal a = new Dog(); // Upcasting
a.speak();            // Calls Dog's speak() at RUNTIME
```

| Aspect | Overloading | Overriding |
| :--- | :--- | :--- |
| **When** | Compile-time | Runtime |
| **Where** | Same class | Parent-Child |
| **Signature** | Different params | Same signature |
