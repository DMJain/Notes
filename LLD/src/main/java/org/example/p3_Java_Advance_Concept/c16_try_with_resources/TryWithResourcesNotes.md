# Try-with-Resources: Automatic Resource Management

> **Prerequisites:** [c12_try_catch_finally](../c12_try_catch_finally/TryCatchFinallyNotes.md) - Understanding finally for cleanup

---

## What You'll Learn

By the end of this chapter, you'll understand:
- **WHAT** try-with-resources does (auto-close)
- **WHY** it's better than finally (less boilerplate, fewer bugs)
- **HOW** AutoCloseable works
- **WHAT** suppressed exceptions are

---

## 1. The Problem with Finally

### Manual Resource Management is Error-Prone

```java
// ❌ Pre-Java 7: Verbose and error-prone
FileReader reader = null;
BufferedReader buffered = null;
try {
    reader = new FileReader("file.txt");
    buffered = new BufferedReader(reader);
    return buffered.readLine();
} catch (IOException e) {
    // handle
} finally {
    // Cleanup is UGLY and error-prone!
    if (buffered != null) {
        try {
            buffered.close();
        } catch (IOException e) {
            // Now what? Log and continue? Original exception lost!
        }
    }
    if (reader != null) {
        try {
            reader.close();
        } catch (IOException e) {
            // Same problem!
        }
    }
}
```

**Problems:**
- Lots of boilerplate code
- Easy to forget to close
- Exception in close() can hide original exception
- Nested try-catch is ugly

---

## 2. The Solution: Try-with-Resources

### Java 7+ Syntax

```java
// ✅ Clean and safe!
try (BufferedReader reader = new BufferedReader(new FileReader("file.txt"))) {
    return reader.readLine();
} catch (IOException e) {
    // handle
}
// reader is AUTOMATICALLY closed!
```

```
┌────────────────────────────────────────────────────────────────────────────┐
│                     TRY-WITH-RESOURCES SYNTAX                               │
├────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│   try (Resource resource = new Resource()) {                                │
│        ↑                                                                    │
│   Resource declared in parentheses                                          │
│   Must implement AutoCloseable                                              │
│                                                                             │
│       // Use the resource                                                   │
│                                                                             │
│   }  ← resource.close() called automatically here!                         │
│                                                                             │
│   catch (Exception e) {                                                    │
│       // Optional: handle exceptions                                       │
│   }                                                                         │
│                                                                             │
│   // No finally needed for cleanup!                                         │
│                                                                             │
└────────────────────────────────────────────────────────────────────────────┘
```

### Multiple Resources

```java
// Multiple resources - all auto-closed in REVERSE order!
try (
    FileReader fileReader = new FileReader("file.txt");
    BufferedReader bufferedReader = new BufferedReader(fileReader)
) {
    return bufferedReader.readLine();
}
// bufferedReader.close() called first (last opened = first closed)
// fileReader.close() called second
```

---

## 3. AutoCloseable Interface

### What Must Resources Implement?

```java
public interface AutoCloseable {
    void close() throws Exception;
}

// Closeable extends AutoCloseable (more specific for I/O)
public interface Closeable extends AutoCloseable {
    void close() throws IOException;
}
```

### Creating Your Own Resource

```java
public class DatabaseConnection implements AutoCloseable {
    
    public DatabaseConnection() {
        System.out.println("Connection opened");
    }
    
    public void query(String sql) {
        System.out.println("Executing: " + sql);
    }
    
    @Override
    public void close() {
        System.out.println("Connection closed");
    }
}

// Usage:
try (DatabaseConnection conn = new DatabaseConnection()) {
    conn.query("SELECT * FROM users");
}  // close() called automatically!
```

---

## 4. Suppressed Exceptions

### The Problem

What if BOTH the try block AND close() throw exceptions?

```java
try (ProblematicResource r = new ProblematicResource()) {
    throw new Exception("Error in try block!");
}  // close() also throws: "Error closing!"

// Which exception is thrown to the caller?
```

### The Solution: Suppressed Exceptions

```
┌────────────────────────────────────────────────────────────────────────────┐
│                      SUPPRESSED EXCEPTIONS                                  │
├────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│   When multiple exceptions occur:                                           │
│                                                                             │
│   1. Try block throws Exception A                                           │
│   2. close() throws Exception B                                             │
│                                                                             │
│   Result:                                                                   │
│   - Exception A is thrown (the "main" exception)                           │
│   - Exception B is SUPPRESSED (attached to A)                              │
│                                                                             │
│   Access suppressed exceptions:                                             │
│                                                                             │
│   catch (Exception e) {                                                    │
│       System.out.println("Main: " + e.getMessage());                       │
│       for (Throwable t : e.getSuppressed()) {                              │
│           System.out.println("Suppressed: " + t.getMessage());             │
│       }                                                                     │
│   }                                                                         │
│                                                                             │
└────────────────────────────────────────────────────────────────────────────┘
```

---

## 5. Summary

```
┌────────────────────────────────────────────────────────────────────────────┐
│                  TRY-WITH-RESOURCES SUMMARY                                 │
├────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│   SYNTAX:                                                                   │
│     try (Resource r = new Resource()) { ... }                              │
│                                                                             │
│   REQUIREMENT:                                                              │
│     Resource must implement AutoCloseable                                   │
│                                                                             │
│   BEHAVIOR:                                                                 │
│     close() called automatically when leaving try block                    │
│     Multiple resources closed in REVERSE order                             │
│                                                                             │
│   SUPPRESSED:                                                               │
│     If close() throws after try throws, it's suppressed                    │
│     Access via exception.getSuppressed()                                   │
│                                                                             │
│   RULE: Always use try-with-resources for AutoCloseable resources!         │
│                                                                             │
└────────────────────────────────────────────────────────────────────────────┘
```

---

## What's Next?

→ [c17_exception_best_practices](../c17_exception_best_practices/BestPracticesNotes.md) - Production-ready exception handling
