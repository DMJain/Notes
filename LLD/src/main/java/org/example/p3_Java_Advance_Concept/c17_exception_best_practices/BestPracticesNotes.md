# Exception Handling Best Practices

> **Prerequisites:** All previous exception modules (c11-c16)

---

## What You'll Learn

Production-ready exception handling guidelines covering:
- DO's and DON'Ts
- Common anti-patterns
- Logging best practices
- Recovery strategies

---

## 1. The DO's ✅

### 1.1 Be Specific with Exceptions

```java
// ✅ GOOD: Specific exceptions
try {
    file.read();
} catch (FileNotFoundException e) {
    createFile();
} catch (IOException e) {
    logError(e);
}

// ❌ BAD: Catching too broad
try {
    file.read();
} catch (Exception e) {  // Catches NPE, array bounds, etc!
    // ...
}
```

### 1.2 Include Context in Messages

```java
// ✅ GOOD: Rich context
throw new OrderNotFoundException(
    String.format("Order not found: orderId=%s, userId=%s", orderId, userId)
);

// ❌ BAD: No context
throw new OrderNotFoundException("Not found");
```

### 1.3 Preserve the Cause (Chain Exceptions)

```java
// ✅ GOOD: Preserve original exception
catch (SQLException e) {
    throw new DataAccessException("Query failed for user: " + userId, e);
}

// ❌ BAD: Original exception lost!
catch (SQLException e) {
    throw new DataAccessException("Query failed");
}
```

### 1.4 Use Try-with-Resources

```java
// ✅ GOOD: Auto-cleanup
try (Connection conn = getConnection()) {
    conn.execute(query);
}

// ❌ BAD: Manual cleanup (error-prone)
Connection conn = getConnection();
try { ... } finally { conn.close(); }
```

### 1.5 Fail Fast

```java
// ✅ GOOD: Validate early
public void process(Order order) {
    Objects.requireNonNull(order, "order cannot be null");
    if (order.getItems().isEmpty()) {
        throw new IllegalArgumentException("order must have items");
    }
    // Now process...
}
```

---

## 2. The DON'Ts ❌

### 2.1 Never Swallow Exceptions

```java
// ❌ TERRIBLE: Silent failure
try {
    criticalOperation();
} catch (Exception e) {
    // do nothing - DEBUGGING NIGHTMARE!
}

// ✅ At minimum, log it
catch (Exception e) {
    logger.error("Critical operation failed", e);
    throw e;
}
```

### 2.2 Don't Use Exceptions for Flow Control

```java
// ❌ BAD: Using exceptions as control flow
try {
    int value = Integer.parseInt(input);
    return true;
} catch (NumberFormatException e) {
    return false;  // Expected case handled by exception!
}

// ✅ GOOD: Use proper validation
if (input.matches("-?\\d+")) {
    return true;
}
return false;
```

### 2.3 Don't Catch Exception (Too Broad)

```java
// ❌ BAD: Catches programming bugs too!
catch (Exception e) { ... }

// ✅ GOOD: Specific types
catch (IOException | SQLException e) { ... }
```

### 2.4 Don't Ignore InterruptedException

```java
// ❌ BAD: Ignoring interrupt
catch (InterruptedException e) {
    // do nothing
}

// ✅ GOOD: Preserve interrupt status
catch (InterruptedException e) {
    Thread.currentThread().interrupt();
    throw new RuntimeException("Operation interrupted", e);
}
```

### 2.5 Don't Return in Finally

```java
// ❌ BAD: Overrides try's return/exception
try {
    throw new Exception();
} finally {
    return;  // Exception is LOST!
}
```

---

## 3. Logging Best Practices

```java
// ✅ Log with full stack trace
logger.error("Failed to process order: {}", orderId, exception);

// ❌ Don't log just the message
logger.error("Error: " + exception.getMessage());  // Stack trace lost!

// ✅ Log once at the handling point
// Don't log at every catch and rethrow!
```

---

## 4. Anti-Patterns Summary

| Anti-Pattern | Problem | Solution |
|--------------|---------|----------|
| Empty catch | Silent failures | Log or rethrow |
| catch(Exception) | Catches bugs | Be specific |
| Exception for flow | Slow, unclear | Use if/else |
| Lost cause | No root cause | Chain with cause |
| Log and throw | Duplicate logs | Do one, not both |
| Return in finally | Hides exceptions | Never do this |

---

## 5. Golden Rules

```
┌────────────────────────────────────────────────────────────────────────────┐
│                     EXCEPTION HANDLING GOLDEN RULES                         │
├────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│   1. CATCH what you can HANDLE meaningfully                                │
│   2. PROPAGATE what you can't handle                                        │
│   3. FAIL FAST - validate early                                             │
│   4. PRESERVE CONTEXT - include cause and details                           │
│   5. BE SPECIFIC - use appropriate exception types                          │
│   6. NEVER SWALLOW - always log or handle                                   │
│   7. USE TRY-WITH-RESOURCES for cleanup                                     │
│                                                                             │
└────────────────────────────────────────────────────────────────────────────┘
```

---

## Congratulations! 🎉

You've completed the Exception Handling module series!

**Module Summary:**
- c11: Exception basics and hierarchy
- c12: Try-catch-finally mechanics  
- c13: Throw vs throws
- c14: Custom exceptions
- c15: Exception chaining
- c16: Try-with-resources
- c17: Best practices (this module)
