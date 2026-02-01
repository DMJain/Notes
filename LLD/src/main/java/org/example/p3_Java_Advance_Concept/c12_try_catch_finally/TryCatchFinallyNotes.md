# Try-Catch-Finally: Exception Handling Mechanics

> **Prerequisites:** [c11_exception_basics](../c11_exception_basics/ExceptionBasicsNotes.md) - Understanding what exceptions are

---

## What You'll Learn

By the end of this chapter, you'll understand:
- **WHAT** try-catch-finally blocks do (exception handling structure)
- **WHY** they're designed this way (separate normal code from error handling)
- **WHEN** to use each block (try, catch, finally, multi-catch)
- **HOW** control flows through these blocks

---

## 1. Why This Structure? (The Problem)

### Without Structured Exception Handling

```java
// 😰 Messy error handling everywhere
public void processFile(String filename) {
    File file = null;
    FileReader reader = null;
    
    file = new File(filename);
    if (!file.exists()) {
        System.out.println("File not found!");
        return;
    }
    
    reader = new FileReader(file);  // Could fail!
    if (reader == null) {
        System.out.println("Couldn't open file!");
        return;
    }
    
    // Process file...
    
    // Don't forget to close!
    if (reader != null) {
        reader.close();  // Could also fail!
    }
}
```

**Problems:**
- Error handling code mixed with business logic
- Easy to forget cleanup
- Hard to read and maintain

### With Try-Catch-Finally

```java
// 😎 Clean separation of concerns
public void processFile(String filename) {
    FileReader reader = null;
    try {
        reader = new FileReader(filename);  // May throw
        // Process file - focus on happy path!
        
    } catch (FileNotFoundException e) {
        System.out.println("File not found: " + e.getMessage());
        
    } finally {
        // Cleanup - ALWAYS runs!
        if (reader != null) {
            reader.close();
        }
    }
}
```

---

## 2. The Try Block

### What It Does

The `try` block contains code that **might throw an exception**. It defines the scope of exception monitoring.

```
┌────────────────────────────────────────────────────────────────────┐
│                         THE TRY BLOCK                               │
├────────────────────────────────────────────────────────────────────┤
│                                                                     │
│   try {                                                             │
│       // Code that might fail goes here                            │
│       // If exception occurs → Jump to matching catch              │
│       // If no exception → Skip all catch blocks                   │
│   }                                                                 │
│                                                                     │
│   RULES:                                                            │
│   ✅ Must have at least one catch OR finally                       │
│   ✅ Can have multiple statements                                   │
│   ✅ Code after exception point is SKIPPED                          │
│   ❌ Cannot stand alone (needs catch or finally)                   │
│                                                                     │
└────────────────────────────────────────────────────────────────────┘
```

### Visual: Execution Flow

```
┌────────────────────────────────────────────────────────────────────┐
│                 TRY BLOCK EXECUTION FLOW                            │
├────────────────────────────────────────────────────────────────────┤
│                                                                     │
│   try {                                                             │
│       statement1;  ──────────────►  Executes                       │
│       statement2;  ──────────────►  Executes                       │
│       riskyCode(); ──────┐          May throw exception            │
│       statement4;        │          ← SKIPPED if exception!        │
│       statement5;        │          ← SKIPPED if exception!        │
│   }                      │                                          │
│                          │ Exception thrown!                        │
│                          ▼                                          │
│   catch (Exception e) {  ◄──────────────────────────────────        │
│       handleError();     // Control jumps here                      │
│   }                                                                 │
│                                                                     │
└────────────────────────────────────────────────────────────────────┘
```

---

## 3. The Catch Block

### What It Does

The `catch` block handles a specific type of exception. It's like saying: "If THIS type of problem happens, do THIS."

```java
try {
    // risky code
} catch (ExceptionType variableName) {
    // handle this specific exception
    // variableName contains the exception object
}
```

### Catching Specific Exceptions

```java
try {
    int result = Integer.parseInt(userInput);
    int[] arr = new int[result];
} catch (NumberFormatException e) {
    System.out.println("Not a valid number: " + e.getMessage());
} catch (NegativeArraySizeException e) {
    System.out.println("Size can't be negative!");
}
```

### Multiple Catch Blocks

You can have multiple catch blocks to handle different exceptions differently.

```
┌────────────────────────────────────────────────────────────────────┐
│                    MULTIPLE CATCH BLOCKS                            │
├────────────────────────────────────────────────────────────────────┤
│                                                                     │
│   try {                                                             │
│       riskyOperation();                                             │
│   }                                                                 │
│   catch (FileNotFoundException e) {  ← Most specific first!       │
│       // Handle file not found                                     │
│   }                                                                 │
│   catch (IOException e) {            ← Less specific               │
│       // Handle other IO problems                                  │
│   }                                                                 │
│   catch (Exception e) {              ← Most general (catch-all)    │
│       // Handle everything else                                    │
│   }                                                                 │
│                                                                     │
│   ⚠️ ORDER MATTERS!                                                │
│   • Specific exceptions MUST come before general ones              │
│   • Only ONE catch block executes (first matching one)             │
│   • Compiler error if specific comes after general                 │
│                                                                     │
└────────────────────────────────────────────────────────────────────┘
```

### ⚠️ The Ordering Rule (CRITICAL!)

```java
// ❌ WRONG - Won't compile!
try {
    readFile();
} catch (Exception e) {           // Too general first!
    // ...
} catch (IOException e) {         // ❌ UNREACHABLE - Exception already caught above!
    // ...
}

// ✅ CORRECT - Specific first
try {
    readFile();
} catch (IOException e) {         // Specific first
    // Handle IO problems
} catch (Exception e) {           // General last
    // Handle everything else
}
```

**Why?** Because `IOException` IS-A `Exception` (inheritance). If you catch `Exception` first, it catches ALL exceptions including `IOException`!

### Multi-Catch (Java 7+)

Handle multiple exception types the same way using `|`:

```java
// Instead of this:
try {
    riskyOperation();
} catch (IOException e) {
    logError(e);
    showMessage("Operation failed");
} catch (SQLException e) {
    logError(e);              // Same code duplicated!
    showMessage("Operation failed");
}

// Do this:
try {
    riskyOperation();
} catch (IOException | SQLException e) {  // Multi-catch!
    logError(e);
    showMessage("Operation failed");
}
```

---

## 4. The Finally Block

### What It Does

The `finally` block **ALWAYS executes**, whether an exception occurred or not. It's perfect for cleanup code.

```
┌────────────────────────────────────────────────────────────────────┐
│                       THE FINALLY BLOCK                             │
├────────────────────────────────────────────────────────────────────┤
│                                                                     │
│   FINALLY EXECUTES WHEN:                                            │
│                                                                     │
│   ✅ No exception occurs (normal execution)                         │
│   ✅ Exception occurs and is caught                                 │
│   ✅ Exception occurs and is NOT caught                             │
│   ✅ Return statement in try or catch                               │
│   ✅ Break/continue in a loop                                       │
│                                                                     │
│   FINALLY DOES NOT EXECUTE WHEN:                                    │
│                                                                     │
│   ❌ System.exit() is called                                        │
│   ❌ JVM crashes                                                    │
│   ❌ Infinite loop before finally                                   │
│                                                                     │
└────────────────────────────────────────────────────────────────────┘
```

### Visual: Finally Execution Guarantee

```
┌──────────────────────────────────────────────────────────────────────┐
│               FINALLY ALWAYS RUNS (almost)                            │
├──────────────────────────────────────────────────────────────────────┤
│                                                                       │
│   SCENARIO 1: No Exception                                            │
│   ┌────────────────┐    ┌────────────────┐    ┌────────────────┐     │
│   │     TRY        │───▶│  (skip catch)  │───▶│   FINALLY      │     │
│   │   executes     │    │                │    │   executes     │     │
│   └────────────────┘    └────────────────┘    └────────────────┘     │
│                                                                       │
│   SCENARIO 2: Exception Caught                                        │
│   ┌────────────────┐    ┌────────────────┐    ┌────────────────┐     │
│   │     TRY        │───▶│    CATCH       │───▶│   FINALLY      │     │
│   │   throws!      │    │   executes     │    │   executes     │     │
│   └────────────────┘    └────────────────┘    └────────────────┘     │
│                                                                       │
│   SCENARIO 3: Exception NOT Caught                                    │
│   ┌────────────────┐    ┌────────────────┐    ┌────────────────┐     │
│   │     TRY        │───▶│  (no match)    │───▶│   FINALLY      │──▶  │
│   │   throws!      │    │                │    │   executes     │ ↑   │
│   └────────────────┘    └────────────────┘    └────────────────┘ │   │
│                                                                   │   │
│                                          Exception propagates up ─┘   │
│                                                                       │
│   SCENARIO 4: Return in try/catch                                     │
│   ┌────────────────┐    ┌────────────────┐    ┌────────────────┐     │
│   │ TRY: return;   │───▶│                │───▶│   FINALLY      │──▶  │
│   │ or CATCH:return│    │                │    │ RUNS FIRST!    │    │
│   └────────────────┘    └────────────────┘    └────────────────┘    │
│                                                        │              │
│                                          Then return ──┘              │
│                                                                       │
└──────────────────────────────────────────────────────────────────────┘
```

### Common Use Cases for Finally

```java
// RESOURCE CLEANUP (Pre-Java 7 pattern)
FileReader reader = null;
try {
    reader = new FileReader("file.txt");
    // read file...
} catch (IOException e) {
    // handle error
} finally {
    // CLEANUP: Close the resource
    if (reader != null) {
        try {
            reader.close();
        } catch (IOException e) {
            // log but don't throw
        }
    }
}
```

---

## 5. Return Gotcha: Finally Overrides Return!

### ⚠️ Critical Gotcha

```java
public int getNumber() {
    try {
        return 1;           // Return 1?
    } finally {
        return 2;           // FINALLY WINS! Returns 2!
    }
}
// Result: Returns 2, NOT 1!
```

**Why?** Finally executes AFTER the return value is computed but BEFORE the method actually returns. If finally has a return, it overrides!

```
┌────────────────────────────────────────────────────────────────────┐
│                    ⚠️ RETURN IN FINALLY GOTCHA                      │
├────────────────────────────────────────────────────────────────────┤
│                                                                     │
│   try {                                                             │
│       return 1;  ←───────── Prepares to return 1                   │
│   } finally {                                                       │
│       return 2;  ←───────── OVERRIDES! Returns 2 instead           │
│   }                                                                 │
│                                                                     │
│   ❌ NEVER put return statements in finally blocks!                │
│   It makes code confusing and can hide exceptions.                 │
│                                                                     │
│   ALSO BAD:                                                         │
│                                                                     │
│   try {                                                             │
│       throw new Exception();  ← Exception thrown                   │
│   } finally {                                                       │
│       return 1;               ← Exception is LOST! Silently!       │
│   }                                                                 │
│   // Method returns 1, exception disappears! 😱                    │
│                                                                     │
└────────────────────────────────────────────────────────────────────┘
```

---

## 6. Try-Catch Combinations

```
┌────────────────────────────────────────────────────────────────────┐
│                   VALID TRY-CATCH COMBINATIONS                      │
├────────────────────────────────────────────────────────────────────┤
│                                                                     │
│   ✅ try + catch                                                    │
│   ┌───────────────┐                                                │
│   │ try {         │                                                │
│   │   ...         │                                                │
│   │ } catch (...) │                                                │
│   │   ...         │                                                │
│   │ }             │                                                │
│   └───────────────┘                                                │
│                                                                     │
│   ✅ try + finally                                                  │
│   ┌───────────────┐                                                │
│   │ try {         │  (exception propagates up)                     │
│   │   ...         │                                                │
│   │ } finally {   │                                                │
│   │   ...         │                                                │
│   │ }             │                                                │
│   └───────────────┘                                                │
│                                                                     │
│   ✅ try + catch + finally                                          │
│   ┌───────────────┐                                                │
│   │ try {         │                                                │
│   │   ...         │                                                │
│   │ } catch (...) │                                                │
│   │   ...         │                                                │
│   │ } finally {   │                                                │
│   │   ...         │                                                │
│   │ }             │                                                │
│   └───────────────┘                                                │
│                                                                     │
│   ✅ try + multiple catches + finally                               │
│                                                                     │
│   ❌ try alone - INVALID!                                          │
│   ❌ catch alone - INVALID!                                        │
│   ❌ finally alone - INVALID!                                      │
│                                                                     │
└────────────────────────────────────────────────────────────────────┘
```

---

## 7. When To Use / When NOT To Use

### ✅ When To Use Each Block

| Block | Use When |
|-------|----------|
| `try` | Wrapping code that may throw exceptions |
| `catch` | You can meaningfully handle the exception |
| `finally` | You need cleanup regardless of success/failure |
| Multi-catch | Same handling for different exception types |

### ❌ When NOT To Use

| Anti-Pattern | Why It's Bad |
|--------------|--------------|
| Empty catch block | Silently swallows exceptions - debugging nightmare! |
| Catch Exception (too broad) | Hides specific problems, catches bugs you should fix |
| Return in finally | Confusing behavior, hides exceptions |
| Exception for flow control | Slow and unclear, use if/else instead |

---

## 8. Common Gotchas

### Gotcha 1: Empty Catch Block

```java
// ❌ TERRIBLE! Silent failure
try {
    criticalOperation();
} catch (Exception e) {
    // do nothing  🤦
}

// ✅ At minimum, log it
try {
    criticalOperation();
} catch (Exception e) {
    logger.error("Critical operation failed", e);
    throw e;  // Re-throw if you can't handle
}
```

### Gotcha 2: Catching Exception (Too Broad)

```java
// ❌ Catches programming bugs too!
try {
    processData(data);
} catch (Exception e) {  // Catches NPE, ArrayIndexOutOfBounds, etc!
    showUserError();     // Wrong! Those are bugs to fix!
}

// ✅ Catch only what you expect
try {
    processData(data);
} catch (IOException e) {
    showUserError("Could not read data");
}
```

### Gotcha 3: Exception in Finally

```java
// ❌ Exception in finally can hide original exception!
try {
    throw new Exception("Original");
} finally {
    throw new Exception("Finally");  // Original exception is LOST!
}
// Only "Finally" exception is thrown
```

---

## 9. Summary

```
┌────────────────────────────────────────────────────────────────────┐
│                     TRY-CATCH-FINALLY SUMMARY                       │
├────────────────────────────────────────────────────────────────────┤
│                                                                     │
│   STRUCTURE:                                                        │
│     try { risky code }                                              │
│     catch (SpecificException e) { handle }                         │
│     finally { cleanup - ALWAYS runs }                              │
│                                                                     │
│   EXECUTION ORDER:                                                  │
│     1. try block executes                                          │
│     2. If exception → matching catch executes                       │
│     3. finally ALWAYS executes                                      │
│     4. Method returns OR exception propagates                       │
│                                                                     │
│   KEY RULES:                                                        │
│     ✅ Catch specific exceptions before general                     │
│     ✅ Only ONE catch block runs                                    │
│     ✅ finally runs even with return/throw                          │
│     ❌ Never return in finally                                      │
│     ❌ Never use empty catch blocks                                 │
│                                                                     │
└────────────────────────────────────────────────────────────────────┘
```

---

## What's Next?

Now that you know HOW to catch exceptions, let's learn about THROWING them:

→ [c13_throw_throws](../c13_throw_throws/ThrowThrowsNotes.md) - throw vs throws, propagating exceptions
