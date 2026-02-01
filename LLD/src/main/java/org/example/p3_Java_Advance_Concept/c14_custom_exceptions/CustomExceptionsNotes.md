# Custom Exceptions: Domain-Specific Error Types

> **Prerequisites:** [c13_throw_throws](../c13_throw_throws/ThrowThrowsNotes.md) - Understanding how to throw exceptions

---

## What You'll Learn

By the end of this chapter, you'll understand:
- **WHY** create custom exceptions (domain-specific errors)
- **WHEN** to create new exception types vs use existing ones
- **HOW** to design a good exception class
- **WHAT** patterns to follow (checked vs unchecked, hierarchy)

---

## 1. Why Create Custom Exceptions?

### The Problem with Generic Exceptions

```java
// ❌ Using generic exceptions
public void withdraw(double amount) throws Exception {
    if (amount < 0) throw new Exception("Bad amount");
    if (balance < amount) throw new Exception("Not enough money");
    balance -= amount;
}

// Caller doesn't know what went wrong!
try {
    account.withdraw(100);
} catch (Exception e) {
    // Is it insufficient funds? Invalid amount? Network error?
    // We don't know! Poor error handling.
}
```

### The Solution: Custom Exceptions

```java
// ✅ Using custom exceptions
public void withdraw(double amount) throws InvalidAmountException, 
                                           InsufficientFundsException {
    if (amount < 0) throw new InvalidAmountException(amount);
    if (balance < amount) throw new InsufficientFundsException(balance, amount);
    balance -= amount;
}

// Caller can handle each case specifically!
try {
    account.withdraw(100);
} catch (InvalidAmountException e) {
    showError("Please enter a valid amount");
} catch (InsufficientFundsException e) {
    showError("Not enough funds. Balance: " + e.getCurrentBalance());
    suggestDeposit();
}
```

```
┌────────────────────────────────────────────────────────────────────────────┐
│                    WHY CUSTOM EXCEPTIONS?                                   │
├────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│   1. CLARITY                                                                │
│      Exception name describes WHAT went wrong                              │
│      InsufficientFundsException > Exception("not enough money")           │
│                                                                             │
│   2. TYPE SAFETY                                                            │
│      Compiler helps catch unhandled cases                                   │
│      Different catch blocks for different errors                           │
│                                                                             │
│   3. CONTEXT                                                                │
│      Can include domain-specific data                                       │
│      e.getCurrentBalance(), e.getOrderId(), etc.                          │
│                                                                             │
│   4. ABSTRACTION                                                            │
│      Hide implementation details                                            │
│      DataAccessException instead of SQLException                           │
│                                                                             │
└────────────────────────────────────────────────────────────────────────────┘
```

---

## 2. How to Create Custom Exceptions

### Basic Template: Checked Exception

```java
// Checked exception (caller MUST handle)
public class InsufficientFundsException extends Exception {
    
    private final double currentBalance;
    private final double requestedAmount;
    
    public InsufficientFundsException(double balance, double requested) {
        super(String.format("Insufficient funds: balance=%.2f, requested=%.2f", 
              balance, requested));
        this.currentBalance = balance;
        this.requestedAmount = requested;
    }
    
    // Getters for context
    public double getCurrentBalance() {
        return currentBalance;
    }
    
    public double getRequestedAmount() {
        return requestedAmount;
    }
}
```

### Basic Template: Unchecked Exception

```java
// Unchecked exception (caller CAN handle, but not forced)
public class InvalidEmailException extends RuntimeException {
    
    private final String email;
    
    public InvalidEmailException(String email) {
        super("Invalid email format: " + email);
        this.email = email;
    }
    
    public String getEmail() {
        return email;
    }
}
```

### The Exception Constructor Pattern

```
┌────────────────────────────────────────────────────────────────────────────┐
│                STANDARD EXCEPTION CONSTRUCTORS                              │
├────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│  public class MyException extends Exception {                               │
│                                                                             │
│      // 1. No-arg constructor (if message is always the same)              │
│      public MyException() {                                                 │
│          super("Default message");                                         │
│      }                                                                      │
│                                                                             │
│      // 2. Message constructor (most common)                                │
│      public MyException(String message) {                                   │
│          super(message);                                                    │
│      }                                                                      │
│                                                                             │
│      // 3. Cause constructor (for exception chaining)                       │
│      public MyException(Throwable cause) {                                  │
│          super(cause);                                                      │
│      }                                                                      │
│                                                                             │
│      // 4. Message + Cause constructor (MOST IMPORTANT!)                    │
│      public MyException(String message, Throwable cause) {                  │
│          super(message, cause);                                            │
│      }                                                                      │
│  }                                                                          │
│                                                                             │
└────────────────────────────────────────────────────────────────────────────┘
```

---

## 3. Checked vs Unchecked: Which to Choose?

```
┌────────────────────────────────────────────────────────────────────────────┐
│                 CHECKED vs UNCHECKED DECISION                               │
├────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│   ASK YOURSELF:                                                             │
│                                                                             │
│   Q1: Can the caller PREVENT this from happening?                          │
│       ↓                                                                     │
│       YES → UNCHECKED (RuntimeException)                                    │
│             "It's a bug - fix the code!"                                    │
│             Example: InvalidEmailException (validate first)                 │
│       ↓                                                                     │
│       NO → Continue to Q2...                                                │
│                                                                             │
│   Q2: SHOULD the caller be FORCED to handle this?                          │
│       ↓                                                                     │
│       YES → CHECKED (Exception)                                             │
│             "You MUST think about this case"                                │
│             Example: InsufficientFundsException (recovery possible)         │
│       ↓                                                                     │
│       NO → UNCHECKED (RuntimeException)                                     │
│            "Fatal error - can't recover anyway"                             │
│            Example: ConfigurationException (app can't start)                │
│                                                                             │
└────────────────────────────────────────────────────────────────────────────┘
```

### Decision Examples

| Scenario | Type | Why |
|----------|------|-----|
| Invalid email format | Unchecked | Can prevent by validating first |
| User not found | Checked | Caller should handle (show "register" option) |
| Database connection failed | Checked | Caller should retry or use cache |
| Null argument passed | Unchecked | Programming bug - use Objects.requireNonNull |
| Missing configuration | Unchecked | Fatal - app can't run without config |
| Insufficient funds | Checked | Caller can recover (show balance, suggest deposit) |

---

## 4. Exception Hierarchy Design

### Creating an Exception Family

```
┌────────────────────────────────────────────────────────────────────────────┐
│                     EXCEPTION HIERARCHY DESIGN                              │
├────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│   For a banking domain:                                                     │
│                                                                             │
│               BankingException (base)                                       │
│                       │                                                     │
│       ┌───────────────┼───────────────┐                                    │
│       │               │               │                                     │
│       ▼               ▼               ▼                                     │
│   AccountException  TransactionException  AuthException                    │
│       │                   │                                                 │
│   ┌───┴───┐          ┌────┼────┐                                           │
│   │       │          │    │    │                                           │
│   ▼       ▼          ▼    ▼    ▼                                           │
│ Closed  NotFound  Insuff  Daily  Trans                                     │
│ Account Account   Funds   Limit  Failed                                    │
│                                                                             │
│   BENEFITS:                                                                 │
│   • catch (BankingException e) → catches ALL banking errors                │
│   • catch (TransactionException e) → catches transaction errors only       │
│   • catch (InsufficientFundsException e) → catches specific error          │
│                                                                             │
└────────────────────────────────────────────────────────────────────────────┘
```

### Code Example

```java
// Base exception for all banking errors
public class BankingException extends Exception {
    public BankingException(String message) { super(message); }
    public BankingException(String message, Throwable cause) { super(message, cause); }
}

// Transaction-related exceptions
public class TransactionException extends BankingException {
    public TransactionException(String message) { super(message); }
}

public class InsufficientFundsException extends TransactionException {
    private final double balance;
    private final double amount;
    
    public InsufficientFundsException(double balance, double amount) {
        super(String.format("Balance: %.2f, Requested: %.2f", balance, amount));
        this.balance = balance;
        this.amount = amount;
    }
    // Getters...
}

public class DailyLimitExceededException extends TransactionException {
    public DailyLimitExceededException(double limit) {
        super("Daily limit exceeded: " + limit);
    }
}
```

---

## 5. Best Practices

### ✅ DO

```java
// 1. Include context (helpful data)
public class OrderNotFoundException extends Exception {
    private final String orderId;
    
    public OrderNotFoundException(String orderId) {
        super("Order not found: " + orderId);
        this.orderId = orderId;  // Context for handling!
    }
    
    public String getOrderId() { return orderId; }
}

// 2. Use specific naming
throw new InvalidEmailFormatException(email);   // ✅ Specific
throw new ValidationException("bad email");     // ❌ Vague

// 3. Provide message + cause constructor for chaining
public MyException(String message, Throwable cause) {
    super(message, cause);
}
```

### ❌ DON'T

```java
// 1. Don't catch and throw generic Exception
throw new Exception("Something went wrong");  // ❌

// 2. Don't create exceptions without context
throw new UserException();  // ❌ Which user? What happened?

// 3. Don't inherit from Error
public class MyError extends Error { }  // ❌ Error is for JVM!

// 4. Don't create exceptions for flow control
if (user == null) {
    throw new UserNotFoundException();  // ❌ If this is expected!
}
// Instead:
Optional<User> user = findUser(id);  // ✅ Handle absence explicitly
```

---

## 6. Summary

```
┌────────────────────────────────────────────────────────────────────────────┐
│                    CUSTOM EXCEPTIONS SUMMARY                                │
├────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│   WHY:     Clarity, type safety, context, abstraction                       │
│                                                                             │
│   WHEN:    Domain-specific errors that callers need to handle differently  │
│                                                                             │
│   HOW:     Extend Exception (checked) or RuntimeException (unchecked)       │
│            Include context fields and standard constructors                 │
│                                                                             │
│   CHOICE:  Checked = Must handle, recoverable                               │
│            Unchecked = Bug or fatal                                         │
│                                                                             │
│   PATTERN: Create hierarchy for related exceptions                          │
│            BankingException → TransactionException → InsufficientFunds     │
│                                                                             │
└────────────────────────────────────────────────────────────────────────────┘
```

---

## What's Next?

When you catch an exception and need to throw a different one, how do you preserve the original cause? That's **exception chaining**:

→ [c15_exception_chaining](../c15_exception_chaining/ExceptionChainingNotes.md) - Preserving root cause information
