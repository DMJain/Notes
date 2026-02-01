# Java Exception Basics

> **Prerequisites:** Understanding of [Classes & Objects](../../p1_oops/c1_classes/ClassesNotes.md), [Inheritance](../../p1_oops/c3_inheritance/InheritanceNotes.md), and basic Java syntax

---

## What You'll Learn

By the end of this chapter, you'll understand:
- **WHAT** exceptions are (disruptions in normal program flow)
- **WHY** they exist (graceful error handling instead of crashes)
- **WHEN** to use checked vs unchecked exceptions
- **HOW** Java's exception hierarchy works

---

## 1. Why Do Exceptions Exist? (The Problem)

### The Dark Ages: No Exception Handling

Imagine you're building a banking app. What happens when a user tries to withdraw more money than they have?

```java
// 😰 WITHOUT Exception Handling
public void withdraw(double amount) {
    balance = balance - amount;  // What if balance < amount?
    // Program continues silently with negative balance! 💀
}
```

**What could go wrong?**

```
┌───────────────────────────────────────────────────────────────────┐
│                  Without Exception Handling                        │
├───────────────────────────────────────────────────────────────────┤
│                                                                    │
│   User Action          Program Response          Result            │
│   ───────────          ────────────────          ──────            │
│                                                                    │
│   Withdraw $1000       balance = 500 - 1000      balance = -$500  │
│   from $500 account    (no check!)               😱 CORRUPTED!     │
│                                                                    │
│   Open file            file = open("x.txt")      file = null       │
│   that doesn't exist   (no check!)               💥 NullPointer!   │
│                                                                    │
│   Divide by zero       result = 10 / 0           Program CRASHES!  │
│                        (no check!)               💀 USER ANGRY!    │
│                                                                    │
│   ❌ Silent failures = Data corruption                             │
│   ❌ Crashes = Terrible user experience                            │
│   ❌ No way to know WHAT went wrong or WHERE                       │
│                                                                    │
└───────────────────────────────────────────────────────────────────┘
```

### The Solution: Exceptions!

**Exceptions are Java's way of saying: "Something went wrong, and here's exactly what and where!"**

```java
// 😎 WITH Exception Handling
public void withdraw(double amount) throws InsufficientFundsException {
    if (amount > balance) {
        throw new InsufficientFundsException("Cannot withdraw $" + amount + 
            ". Balance is only $" + balance);
    }
    balance = balance - amount;
}

// Caller can now HANDLE the problem gracefully
try {
    account.withdraw(1000);
} catch (InsufficientFundsException e) {
    showMessage("Sorry, you don't have enough funds!");
    suggestLowerAmount();
}
```

### Real-World Analogy

```
┌───────────────────────────────────────────────────────────────────┐
│                     Real-World Analogy                             │
├───────────────────────────────────────────────────────────────────┤
│                                                                    │
│   🏥 A HOSPITAL EMERGENCY SYSTEM                                   │
│                                                                    │
│   WITHOUT Exceptions (old way):                                    │
│   ┌─────────────────────────────────────────┐                     │
│   │ Patient has heart attack                 │                     │
│   │ ↓                                        │                     │
│   │ System continues normally                │                     │
│   │ ↓                                        │                     │
│   │ Patient dies, no one noticed! 💀        │                     │
│   └─────────────────────────────────────────┘                     │
│                                                                    │
│   WITH Exceptions (proper way):                                    │
│   ┌─────────────────────────────────────────┐                     │
│   │ Patient has heart attack                 │                     │
│   │ ↓                                        │                     │
│   │ 🚨 EXCEPTION THROWN!                     │                     │
│   │ ↓                                        │                     │
│   │ Emergency alert → Doctors rush in → ✅   │                     │
│   └─────────────────────────────────────────┘                     │
│                                                                    │
│   Exception = "STOP! Something's wrong! Here's what happened!"     │
│                                                                    │
└───────────────────────────────────────────────────────────────────┘
```

---

## 2. What Is An Exception?

### Definition

An **exception** is an object that represents an error or unexpected event that disrupts the normal flow of program execution.

When something goes wrong:
1. Java creates an **Exception object** containing:
   - What went wrong (exception type + message)
   - Where it happened (stack trace)
2. The exception is **thrown** (like raising an alarm)
3. Your code can **catch** it and handle it gracefully

### The Exception Hierarchy

```
┌───────────────────────────────────────────────────────────────────────────┐
│                        JAVA THROWABLE HIERARCHY                            │
├───────────────────────────────────────────────────────────────────────────┤
│                                                                            │
│                              java.lang.Object                              │
│                                    │                                       │
│                                    ▼                                       │
│                           ┌─────────────────┐                              │
│                           │   Throwable     │ ← Root of all errors        │
│                           │                 │   (can be thrown/caught)     │
│                           └────────┬────────┘                              │
│                                    │                                       │
│                    ┌───────────────┴───────────────┐                       │
│                    │                               │                       │
│                    ▼                               ▼                       │
│           ┌─────────────────┐             ┌─────────────────┐              │
│           │     Error       │             │   Exception     │              │
│           │                 │             │                 │              │
│           │  "JVM is dying" │             │ "Something wrong│              │
│           │   DON'T CATCH!  │             │  but recoverable"│             │
│           └────────┬────────┘             └────────┬────────┘              │
│                    │                               │                       │
│          ┌─────────┼─────────┐           ┌─────────┴─────────┐             │
│          ▼         ▼         ▼           │                   │             │
│    OutOfMemory  StackOver  VirtualMa     ▼                   ▼             │
│    Error       flowError  chineError  ┌──────────┐    ┌─────────────┐      │
│                                       │ Checked  │    │RuntimeExcep │      │
│    ⚠️ JVM-level                       │Exceptions│    │   tion      │      │
│    Cannot recover!                    │          │    │ (Unchecked) │      │
│                                       └────┬─────┘    └──────┬──────┘      │
│                                            │                 │             │
│                               ┌────────────┼──────┐   ┌──────┼──────┐      │
│                               ▼            ▼      ▼   ▼      ▼      ▼      │
│                          IOException  SQLException  NullPoi  Arithm Illegal│
│                          FileNotFound ClassNotFound nterExc  eticEx Argum  │
│                          Exception    Exception     eption   ception entEx │
│                                                                            │
│  ┌────────────────────────────────────────────────────────────────────┐   │
│  │ KEY INSIGHT:                                                        │   │
│  │ • Error → JVM problems → DON'T catch (you can't fix it anyway)     │   │
│  │ • Checked Exception → MUST handle (compiler forces you)            │   │
│  │ • Unchecked Exception → OPTIONAL to handle (programming bugs)      │   │
│  └────────────────────────────────────────────────────────────────────┘   │
│                                                                            │
└───────────────────────────────────────────────────────────────────────────┘
```

---

## 3. Checked vs Unchecked Exceptions

This is the **most important distinction** in Java exception handling!

### Checked Exceptions

**What:** Exceptions that the compiler FORCES you to handle.

**Why they exist:** For situations where:
- The error is **external** (file system, network, database)
- The error is **recoverable** (try again, use backup)
- The **caller should know** this operation might fail

```java
// Checked Exception Example
public void readFile(String filename) throws IOException {  // MUST declare!
    FileReader file = new FileReader(filename);  // May throw FileNotFoundException
    // ...
}

// Caller MUST handle it!
try {
    readFile("data.txt");
} catch (IOException e) {
    // Handle the error - maybe show "File not found" to user
}
```

### Unchecked Exceptions (RuntimeException)

**What:** Exceptions that the compiler does NOT force you to handle.

**Why they exist:** For situations where:
- The error is a **programming bug** (null pointer, array out of bounds)
- The error is **preventable** (check for null before using)
- The caller **shouldn't need** to handle this (fix the bug instead!)

```java
// Unchecked Exception Example
public void processUser(User user) {
    String name = user.getName();  // NPE if user is null!
    // No throws clause needed - it's unchecked
}

// Better approach: FIX THE BUG, don't catch it!
public void processUser(User user) {
    if (user == null) {
        throw new IllegalArgumentException("User cannot be null");
    }
    String name = user.getName();  // Safe now!
}
```

### Visual Comparison

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                    CHECKED vs UNCHECKED EXCEPTIONS                           │
├────────────────────────────────┬────────────────────────────────────────────┤
│       CHECKED EXCEPTIONS       │         UNCHECKED EXCEPTIONS               │
│      (Compile-time check)      │        (Runtime exceptions)                │
├────────────────────────────────┼────────────────────────────────────────────┤
│                                │                                             │
│  extends Exception             │  extends RuntimeException                   │
│  (but NOT RuntimeException)    │                                             │
│                                │                                             │
├────────────────────────────────┼────────────────────────────────────────────┤
│  MUST handle or declare:       │  Optional to handle:                        │
│                                │                                             │
│  ❌ Won't compile:             │  ✅ Compiles fine:                          │
│  void read() {                 │  void process() {                           │
│    new FileReader("x.txt");    │    int x = 10 / 0;  // ArithmeticException │
│  }                             │  }                                          │
│                                │                                             │
│  ✅ Must do one of:            │                                             │
│  void read() throws IOExc {..} │                                             │
│  OR                            │                                             │
│  try { .. } catch(..) { .. }   │                                             │
│                                │                                             │
├────────────────────────────────┼────────────────────────────────────────────┤
│  EXAMPLES:                     │  EXAMPLES:                                  │
│  • IOException                 │  • NullPointerException                     │
│  • SQLException                │  • ArrayIndexOutOfBoundsException           │
│  • ClassNotFoundException      │  • ArithmeticException                      │
│  • InterruptedException        │  • IllegalArgumentException                 │
│  • FileNotFoundException       │  • ClassCastException                       │
│                                │                                             │
├────────────────────────────────┼────────────────────────────────────────────┤
│  WHEN TO USE:                  │  WHEN TO USE:                               │
│  ✅ External resources         │  ✅ Programming bugs                        │
│  ✅ Network operations         │  ✅ Validation failures                     │
│  ✅ File I/O                   │  ✅ Precondition violations                 │
│  ✅ Database operations        │  ✅ API misuse                              │
│                                │                                             │
├────────────────────────────────┼────────────────────────────────────────────┤
│  PHILOSOPHY:                   │  PHILOSOPHY:                                │
│  "This CAN fail, be prepared!" │  "This SHOULDN'T fail, fix your code!"     │
│                                │                                             │
└────────────────────────────────┴────────────────────────────────────────────┘
```

### Decision Flowchart: Which Exception Type?

```
┌─────────────────────────────────────────────────────────────────────────┐
│               WHICH EXCEPTION TYPE SHOULD I USE?                         │
├─────────────────────────────────────────────────────────────────────────┤
│                                                                          │
│                    Is this error PREVENTABLE                             │
│                    by the programmer?                                    │
│                            │                                             │
│              ┌─────────────┴─────────────┐                               │
│              │                           │                               │
│             YES                         NO                               │
│              │                           │                               │
│              ▼                           ▼                               │
│    ┌─────────────────┐        ┌─────────────────┐                        │
│    │   UNCHECKED     │        │ Is the caller   │                        │
│    │  RuntimeExcep   │        │ able to recover?│                        │
│    │                 │        └────────┬────────┘                        │
│    │ Examples:       │                 │                                 │
│    │ • null check    │       ┌─────────┴─────────┐                       │
│    │ • bounds check  │       │                   │                       │
│    │ • format check  │      YES                 NO                       │
│    └─────────────────┘       │                   │                       │
│                              ▼                   ▼                       │
│                   ┌─────────────────┐  ┌─────────────────┐               │
│                   │    CHECKED      │  │ UNCHECKED or    │               │
│                   │   Exception     │  │ don't throw     │               │
│                   │                 │  │ (log and fail)  │               │
│                   │ Examples:       │  │                 │               │
│                   │ • file not found│  │ Example:        │               │
│                   │ • network down  │  │ • corrupted file│               │
│                   │ • DB unavailable│  │   (can't fix)   │               │
│                   └─────────────────┘  └─────────────────┘               │
│                                                                          │
└─────────────────────────────────────────────────────────────────────────┘
```

---

## 4. Common Built-in Exceptions

### Checked Exceptions (Must Handle)

| Exception | When It Happens | Example |
|-----------|-----------------|---------|
| `IOException` | File/network I/O fails | Reading a file that doesn't exist |
| `FileNotFoundException` | File not found | `new FileReader("missing.txt")` |
| `SQLException` | Database error | Query fails, connection lost |
| `ClassNotFoundException` | Class not found at runtime | Using `Class.forName("Unknown")` |
| `InterruptedException` | Thread is interrupted | `Thread.sleep()` interrupted |

### Unchecked Exceptions (Programming Bugs)

| Exception | When It Happens | How to Prevent |
|-----------|-----------------|----------------|
| `NullPointerException` | Using a null reference | Check for null first |
| `ArrayIndexOutOfBoundsException` | Invalid array index | Check array length |
| `ArithmeticException` | Math error (div by 0) | Check divisor ≠ 0 |
| `NumberFormatException` | Invalid number string | Validate input format |
| `ClassCastException` | Invalid type cast | Use `instanceof` first |
| `IllegalArgumentException` | Invalid argument | Validate parameters |
| `IllegalStateException` | Object in wrong state | Check state before operation |

---

## 5. When To Use / When NOT To Use

### ✅ When To Use Exceptions

| Situation | Why | Example |
|-----------|-----|---------|
| External resource failure | Can't control external systems | File not found, network down |
| Invalid user input | User made a mistake | Invalid email format |
| Precondition violation | API contract broken | `withdraw(-100)` |
| Unrecoverable state | Something really went wrong | Corrupted configuration |

### ❌ When NOT To Use Exceptions

| Anti-Pattern | Why It's Bad | Better Approach |
|--------------|--------------|-----------------|
| Flow control | Slow, confusing | Use if/else |
| Expected outcomes | Exceptions are for UNEXPECTED | Return Optional or boolean |
| Catching everything | Hides bugs | Catch specific exceptions |
| Ignoring exceptions | Silent failures | At minimum, log them |

```java
// ❌ BAD: Using exception for flow control
try {
    int index = list.indexOf(item);
    if (index == -1) throw new Exception("Not found");
} catch (Exception e) {
    // Handle not found
}

// ✅ GOOD: Simple conditional
int index = list.indexOf(item);
if (index == -1) {
    // Handle not found
} else {
    // Process item
}
```

---

## 6. Common Gotchas

### Gotcha 1: Catching Too Broadly

```java
// ❌ BAD: Catches EVERYTHING including bugs!
try {
    processOrder(order);
} catch (Exception e) {
    log.error("Order failed");  // What failed? NPE? Logic error? Network?
}

// ✅ GOOD: Catch specific exceptions
try {
    processOrder(order);
} catch (NetworkException e) {
    retryLater();
} catch (ValidationException e) {
    showUserError(e.getMessage());
}
```

### Gotcha 2: Swallowing Exceptions

```java
// ❌ BAD: Silent failure - nightmare to debug!
try {
    importantOperation();
} catch (Exception e) {
    // Do nothing 🤦
}

// ✅ GOOD: At minimum, log it
try {
    importantOperation();
} catch (Exception e) {
    log.error("Operation failed", e);
    throw e;  // Or re-throw if you can't handle it
}
```

### Gotcha 3: Wrong Exception Type

```java
// ❌ BAD: Using checked for programming errors
public void setAge(int age) throws InvalidAgeException {  // Checked
    if (age < 0) throw new InvalidAgeException("Age can't be negative");
}

// ✅ GOOD: Use unchecked for programming errors
public void setAge(int age) {
    if (age < 0) throw new IllegalArgumentException("Age can't be negative");
}
```

---

## 7. Summary

```
┌─────────────────────────────────────────────────────────────────────────┐
│                         EXCEPTION BASICS SUMMARY                         │
├─────────────────────────────────────────────────────────────────────────┤
│                                                                          │
│  WHAT: Objects representing errors that disrupt normal program flow     │
│                                                                          │
│  WHY: Graceful error handling instead of crashes                        │
│                                                                          │
│  HIERARCHY:                                                              │
│    Throwable → Error (JVM dying, don't catch)                           │
│             → Exception → Checked (must handle)                          │
│                        → RuntimeException (unchecked, optional)         │
│                                                                          │
│  KEY DECISION:                                                           │
│    • External failure + recoverable → CHECKED                           │
│    • Programming bug + preventable → UNCHECKED                          │
│                                                                          │
│  BEST PRACTICES:                                                         │
│    ✅ Catch specific exceptions                                          │
│    ✅ Always log or handle                                               │
│    ✅ Use unchecked for validation                                       │
│    ❌ Don't use exceptions for flow control                              │
│    ❌ Don't swallow exceptions                                           │
│                                                                          │
└─────────────────────────────────────────────────────────────────────────┘
```

---

## What's Next?

Now that you understand what exceptions are and when to use each type, let's learn HOW to handle them:

→ [c12_try_catch_finally](../c12_try_catch_finally/TryCatchFinallyNotes.md) - The mechanics of catching and handling exceptions
