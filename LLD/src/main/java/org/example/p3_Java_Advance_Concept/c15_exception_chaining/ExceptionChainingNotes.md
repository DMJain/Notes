# Exception Chaining: Preserving Root Cause

> **Prerequisites:** [c14_custom_exceptions](../c14_custom_exceptions/CustomExceptionsNotes.md) - Creating custom exception types

---

## What You'll Learn

By the end of this chapter, you'll understand:
- **WHAT** exception chaining is (wrapping exceptions)
- **WHY** it's important (preserve debugging context)
- **WHEN** to chain exceptions
- **HOW** to use the `cause` parameter

---

## 1. The Problem: Lost Context

### Without Chaining

```java
public void saveUser(User user) throws DataAccessException {
    try {
        database.insert(user);
    } catch (SQLException e) {
        // ❌ BAD: Original exception is LOST!
        throw new DataAccessException("Failed to save user");
    }
}
```

**The problem:** When debugging, we only see "Failed to save user" but don't know:
- Was it a connection timeout?
- Was it a constraint violation?
- Was it a network error?

The original `SQLException` with all its details is **GONE**!

### With Chaining

```java
public void saveUser(User user) throws DataAccessException {
    try {
        database.insert(user);
    } catch (SQLException e) {
        // ✅ GOOD: Original exception is PRESERVED!
        throw new DataAccessException("Failed to save user: " + user.getId(), e);
        //                                                                    ↑
        //                                                      pass original as 'cause'
    }
}
```

Now the stack trace shows:
```
DataAccessException: Failed to save user: 123
    at UserService.saveUser(UserService.java:25)
Caused by: SQLException: Connection timeout
    at Database.executeQuery(Database.java:102)
```

---

## 2. How Exception Chaining Works

```
┌────────────────────────────────────────────────────────────────────────────┐
│                      EXCEPTION CHAINING                                     │
├────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│   Original Exception (root cause)                                           │
│   ┌─────────────────────────────────────────────────────────────────┐      │
│   │ SQLException: "Connection refused to host 192.168.1.10"        │      │
│   │ Stack trace:                                                    │      │
│   │     at Database.connect()                                       │      │
│   │     at Database.executeQuery()                                  │      │
│   └───────────────────────────────────┬─────────────────────────────┘      │
│                                       │                                     │
│                                       │ Wrapped as 'cause'                  │
│                                       ▼                                     │
│   Higher-level Exception                                                    │
│   ┌─────────────────────────────────────────────────────────────────┐      │
│   │ DataAccessException: "Failed to load user profile"             │      │
│   │ Stack trace:                                                    │      │
│   │     at UserService.getProfile()                                 │      │
│   │ Caused by: SQLException ← Points to original!                  │      │
│   └───────────────────────────────────┬─────────────────────────────┘      │
│                                       │                                     │
│                                       │ Wrapped again as 'cause'            │
│                                       ▼                                     │
│   Application-level Exception                                               │
│   ┌─────────────────────────────────────────────────────────────────┐      │
│   │ ProfileLoadException: "Unable to display profile"              │      │
│   │ Stack trace:                                                    │      │
│   │     at ProfileController.showProfile()                          │      │
│   │ Caused by: DataAccessException                                  │      │
│   │ Caused by: SQLException ← Still preserved!                     │      │
│   └─────────────────────────────────────────────────────────────────┘      │
│                                                                             │
└────────────────────────────────────────────────────────────────────────────┘
```

---

## 3. The Cause API

### Setting the Cause

```java
// Method 1: Constructor (PREFERRED)
throw new MyException("message", originalException);

// Method 2: initCause() method (for legacy code)
MyException e = new MyException("message");
e.initCause(originalException);
throw e;
```

### Getting the Cause

```java
try {
    service.doSomething();
} catch (ServiceException e) {
    System.out.println("Main error: " + e.getMessage());
    
    // Get the cause
    Throwable cause = e.getCause();
    if (cause != null) {
        System.out.println("Caused by: " + cause.getMessage());
        
        // Get root cause (keep going until no more causes)
        Throwable rootCause = cause;
        while (rootCause.getCause() != null) {
            rootCause = rootCause.getCause();
        }
        System.out.println("Root cause: " + rootCause.getMessage());
    }
}
```

---

## 4. When to Chain

```
┌────────────────────────────────────────────────────────────────────────────┐
│                    WHEN TO CHAIN EXCEPTIONS                                 │
├────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│   ✅ CHAIN when:                                                            │
│                                                                             │
│   1. Converting from low-level to high-level exception                     │
│      SQLException → DataAccessException                                     │
│      IOException → FileProcessingException                                  │
│                                                                             │
│   2. Adding context while preserving original                               │
│      "Connection failed" → "Failed to save order #123"                     │
│                                                                             │
│   3. Hiding implementation details                                          │
│      Caller shouldn't know you use MySQL (SQLException)                     │
│      They should see DataAccessException                                    │
│                                                                             │
│   ❌ DON'T CHAIN when:                                                      │
│                                                                             │
│   1. Just re-throwing (use throw e; instead)                               │
│   2. The original exception is irrelevant                                   │
│   3. You're actually handling and recovering                                │
│                                                                             │
└────────────────────────────────────────────────────────────────────────────┘
```

---

## 5. Practical Example

```java
// Layer 1: Database access
public class UserRepository {
    public User findById(String id) throws RepositoryException {
        try {
            return database.query("SELECT * FROM users WHERE id = ?", id);
        } catch (SQLException e) {
            // Chain: Low-level → Repository-level
            throw new RepositoryException("Failed to find user: " + id, e);
        }
    }
}

// Layer 2: Service
public class UserService {
    public UserProfile getProfile(String userId) throws ServiceException {
        try {
            User user = repository.findById(userId);
            return buildProfile(user);
        } catch (RepositoryException e) {
            // Chain: Repository-level → Service-level
            throw new ServiceException("Failed to load profile", e);
        }
    }
}

// Layer 3: Controller
public class ProfileController {
    public void showProfile(String userId) {
        try {
            UserProfile profile = userService.getProfile(userId);
            display(profile);
        } catch (ServiceException e) {
            // Log with full chain for debugging
            logger.error("Profile display failed", e);
            
            // Walk the chain for specific handling
            if (getRootCause(e) instanceof SQLException) {
                showError("Database is currently unavailable");
            } else {
                showError("Unable to load profile");
            }
        }
    }
}
```

---

## 6. Summary

```
┌────────────────────────────────────────────────────────────────────────────┐
│                    EXCEPTION CHAINING SUMMARY                               │
├────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│   WHAT: Wrapping one exception inside another                               │
│                                                                             │
│   WHY:  Preserve debugging info while using appropriate exception types    │
│                                                                             │
│   HOW:  throw new HighLevelException("message", lowLevelException);         │
│                                                                             │
│   API:  getCause() - get wrapped exception                                  │
│         initCause() - set cause (legacy)                                    │
│                                                                             │
│   RULE: Always pass the cause when wrapping exceptions!                    │
│                                                                             │
└────────────────────────────────────────────────────────────────────────────┘
```

---

## What's Next?

→ [c16_try_with_resources](../c16_try_with_resources/TryWithResourcesNotes.md) - Automatic resource management
