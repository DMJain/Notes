# Access Modifiers in Java

## Prerequisites
- [c0: OOP Introduction](../c0_introduction/OOPIntroduction.md) — Understanding of classes, objects, encapsulation

---

## What You'll Learn
| Question | Answer |
|----------|--------|
| **WHAT** | Keywords that control visibility of classes, methods, and fields |
| **WHY** | To protect data and enforce encapsulation |
| **WHEN** | Every time you declare a class member |
| **HOW** | Using `public`, `private`, `protected`, or default (no keyword) |

---

## Why Do Access Modifiers Exist? (The History)

### The Problem in Procedural Programming

In C (procedural), **all data was accessible everywhere**:

```c
// C - No data protection
struct BankAccount {
    double balance;
};

// Anyone can do this:
account.balance = -1000000;  // 💥 Invalid state!
```

**Problems:**
1. No way to enforce valid states
2. Any code can break your data
3. Debugging nightmares — "Who changed this?"

### The OOP Solution

OOP introduced **access modifiers** to control who can see and modify data:

```java
public class BankAccount {
    private double balance;  // 🔒 Hidden!
    
    public void deposit(double amount) {
        if (amount > 0) {
            balance += amount;  // ✅ Controlled access
        }
    }
}
```

> **Key Insight:** Access modifiers are the **gatekeepers** of encapsulation.

---

## The Four Access Modifiers

```
┌─────────────────────────────────────────────────────────────────┐
│                     VISIBILITY SPECTRUM                         │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│   PRIVATE ◄────────────────────────────────────► PUBLIC        │
│   🔒 Most                                         🌐 Most       │
│   Restrictive                                   Permissive      │
│                                                                 │
│   private    default    protected    public                    │
│      │          │            │          │                       │
│      ▼          ▼            ▼          ▼                       │
│   Same       Same        Same Pkg    Anywhere                  │
│   Class      Package     + Subclass                            │
│   Only       Only        (any pkg)                             │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

## Visibility Table

| Modifier | Same Class | Same Package | Subclass (Diff Pkg) | World (Diff Pkg) |
|:---------|:----------:|:------------:|:-------------------:|:----------------:|
| **`public`** | ✅ | ✅ | ✅ | ✅ |
| **`protected`** | ✅ | ✅ | ✅ | ❌ |
| **default** (no keyword) | ✅ | ✅ | ❌ | ❌ |
| **`private`** | ✅ | ❌ | ❌ | ❌ |

---

## ASCII Diagram: Package Visibility

```
┌──────────────────────────────────────────────────────────────────┐
│                          YOUR PROJECT                            │
├──────────────────────────────────────────────────────────────────┤
│                                                                  │
│   ┌─── Package p1 ───────────────────────────────────────────┐   │
│   │                                                          │   │
│   │   ┌─────────────────┐      ┌─────────────────┐          │   │
│   │   │  class Parent   │      │  class Neighbor │          │   │
│   │   │  ─────────────  │      │                 │          │   │
│   │   │  public    ✅   │◄────►│  Can access:    │          │   │
│   │   │  protected ✅   │      │  public    ✅   │          │   │
│   │   │  default   ✅   │      │  protected ✅   │          │   │
│   │   │  private   ✅   │      │  default   ✅   │          │   │
│   │   └─────────────────┘      │  private   ❌   │          │   │
│   │                            └─────────────────┘          │   │
│   └──────────────────────────────────────────────────────────┘   │
│                              │                                   │
│                              │ extends (inheritance)             │
│                              ▼                                   │
│   ┌─── Package p2 ───────────────────────────────────────────┐   │
│   │                                                          │   │
│   │   ┌─────────────────┐      ┌─────────────────┐          │   │
│   │   │  class Child    │      │  class Stranger │          │   │
│   │   │  extends Parent │      │                 │          │   │
│   │   │  ─────────────  │      │  Can access:    │          │   │
│   │   │  Can access:    │      │  public    ✅   │          │   │
│   │   │  public    ✅   │      │  protected ❌   │          │   │
│   │   │  protected ✅   │      │  default   ❌   │          │   │
│   │   │  default   ❌   │      │  private   ❌   │          │   │
│   │   │  private   ❌   │      └─────────────────┘          │   │
│   │   └─────────────────┘                                   │   │
│   │                                                          │   │
│   └──────────────────────────────────────────────────────────┘   │
│                                                                  │
└──────────────────────────────────────────────────────────────────┘
```

---

## Each Modifier Explained

### 1. `private` — 🔒 Class Only

```java
public class BankAccount {
    private double balance;  // Only this class can access
    
    private void auditLog() {  // Internal helper
        // ...
    }
}
```

**Use for:** Internal state, helper methods, implementation details

### 2. `default` (Package-Private) — 📦 Same Package Only

```java
class DatabaseConnection {  // No modifier = package-private
    String connectionString;  // Package-private field
    
    void connect() { }  // Package-private method
}
```

**Use for:** Internal APIs within a package, test access

### 3. `protected` — 👨‍👩‍👧 Family Access

```java
public class Animal {
    protected String species;  // Subclasses can access
    
    protected void makeSound() {
        System.out.println("Some sound");
    }
}

// In a DIFFERENT package
public class Dog extends Animal {
    public void bark() {
        species = "Canine";  // ✅ Works! Dog inherits from Animal
        makeSound();          // ✅ Works!
    }
}
```

**Use for:** Fields/methods meant for extension by subclasses

### 4. `public` — 🌐 Everywhere

```java
public class Calculator {
    public int add(int a, int b) {
        return a + b;
    }
}
```

**Use for:** Public API, constants, utility methods

---

## When to Use Each Modifier

| Modifier | ✅ Use When | 
|----------|-------------|
| `private` | Data that should never leak; internal helpers |
| default | Classes working together in a package |
| `protected` | Subclasses need access to extend behavior |
| `public` | Part of your public API |

### The Golden Rule

> **Start with `private`, loosen only when necessary.**

```
Design Flow:
private → default → protected → public
   │                                │
   └────── Only move right ────────►│
           when absolutely needed
```

---

## When NOT to Use (Anti-Patterns)

### ❌ Over-Publicizing

```java
// BAD: Everything is public
public class User {
    public String password;    // 💥 Security disaster!
    public String internalId;  // 💥 Implementation leak!
}

// GOOD: Proper encapsulation
public class User {
    private String password;
    private String internalId;
    
    public boolean checkPassword(String input) {
        return this.password.equals(input);
    }
}
```

### ❌ Protected Field Abuse

```java
// BAD: Using protected for everything
public class Parent {
    protected int x, y, z, a, b, c;  // 💥 Too much exposed!
}

// GOOD: Minimize protected exposure
public class Parent {
    private int x, y, z, a, b, c;
    protected int getX() { return x; }  // Controlled access
}
```

### ❌ Forgetting Default Scope

```java
// Mistake: Thinking this is private
class Config {
    String apiKey;  // 💥 Actually package-private, not private!
}
```

---

## Common Gotchas

### 1. Default vs Private Confusion

```java
class Example {
    String name;     // ❌ Package-private (NOT private!)
    private int id;  // ✅ Actually private
}
```

### 2. Protected Through Inheritance Only

```java
// In package p1
public class Parent {
    protected int value = 10;
}

// In package p2
public class Child extends Parent {
    public void test() {
        System.out.println(value);         // ✅ Inherited access
        
        Parent p = new Parent();
        // System.out.println(p.value);    // ❌ COMPILE ERROR!
        // Can't access protected via parent reference in different package
    }
}
```

### 3. Inner Classes See Private

```java
public class Outer {
    private int secret = 42;
    
    class Inner {
        void reveal() {
            System.out.println(secret);  // ✅ Works! Inner sees Outer's private
        }
    }
}
```

---

## Real-World Analogy: House Security

```
┌─────────────────────────────────────────────────────────────┐
│                        YOUR HOUSE                           │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│   🔒 private    = Safe in your bedroom                     │
│                   Only YOU can access                       │
│                                                             │
│   📦 default    = Stuff in shared family areas             │
│                   Family members (same package) can use     │
│                                                             │
│   👨‍👩‍👧 protected = Family heirloom you pass to children     │
│                   Your kids (subclasses) inherit it         │
│                                                             │
│   🌐 public     = Your front porch                         │
│                   Anyone walking by can see it              │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

---

## Project Demo Structure

```
c1_accessmodifiers/
├── AccessModifiersNotes.md  ← You are here
├── Main.java                ← Entry point
├── p1/
│   ├── Parent.java          ← Class with all 4 modifiers
│   └── SamePackageNeighbor.java   ← Tests same-package access
└── p2/
    ├── ChildInDiffPackage.java    ← Tests protected via inheritance
    └── StrangerInDiffPackage.java ← Tests access from unrelated class
```

---

## Summary

| Modifier | Scope | Mnemonic |
|----------|-------|----------|
| `private` | Same class only | 🔒 "Lock it up" |
| default | Same package | 📦 "Keep it in the box" |
| `protected` | Same package + subclasses | 👨‍👩‍👧 "Family inheritance" |
| `public` | Everywhere | 🌐 "Open to the world" |

**Remember:** Encapsulation = private by default, expose only what's necessary.

---

## Next Chapter
→ [c2: Constructors & Memory Model](../c2_constructors/ConstructorsNotes.md) — How objects are created and initialized
