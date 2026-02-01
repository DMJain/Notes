# Throw vs Throws: Exception Propagation

> **Prerequisites:** [c12_try_catch_finally](../c12_try_catch_finally/TryCatchFinallyNotes.md) - Understanding try-catch mechanics

---

## What You'll Learn

By the end of this chapter, you'll understand:
- **WHAT** `throw` and `throws` do (and the DIFFERENCE!)
- **WHY** propagate exceptions (let caller decide)
- **WHEN** to throw vs when to handle
- **HOW** exceptions flow up the call stack

---

## 1. The Key Difference: throw vs throws

```
┌────────────────────────────────────────────────────────────────────────────┐
│                    throw vs throws - THE DIFFERENCE                         │
├──────────────────────────────────┬─────────────────────────────────────────┤
│            throw                 │              throws                      │
├──────────────────────────────────┼─────────────────────────────────────────┤
│                                  │                                          │
│  WHAT: An ACTION                 │  WHAT: A DECLARATION                    │
│  "I'm throwing this exception    │  "This method might throw these         │
│   right now!"                    │   exceptions - be prepared!"            │
│                                  │                                          │
│  WHERE: Inside method body       │  WHERE: In method signature             │
│                                  │                                          │
│  void foo() {                    │  void foo() throws IOException {        │
│      throw new Exception();  ←   │              ↑                          │
│  }                    ACTION     │              DECLARATION                 │
│                                  │                                          │
│  WHAT IT DOES:                   │  WHAT IT DOES:                          │
│  Creates & throws an exception   │  Tells compiler & callers "this method  │
│  object at this exact point      │  might throw these exceptions"          │
│                                  │                                          │
├──────────────────────────────────┼─────────────────────────────────────────┤
│  ANALOGY:                        │  ANALOGY:                               │
│  Pulling the fire alarm          │  A sign saying "Fire alarm in building" │
│  (doing it NOW)                  │  (warning about possibility)            │
│                                  │                                          │
└──────────────────────────────────┴─────────────────────────────────────────┘
```

---

## 2. The `throw` Keyword

### What It Does

`throw` creates and throws an exception object. Execution immediately jumps to the nearest matching catch block.

```java
public void validateAge(int age) {
    if (age < 0) {
        throw new IllegalArgumentException("Age cannot be negative: " + age);
        //     ↑
        //     Creates and throws the exception RIGHT HERE
    }
    // Code here never runs if exception was thrown
}
```

### Anatomy of throw

```
┌────────────────────────────────────────────────────────────────────┐
│                     ANATOMY OF throw                                │
├────────────────────────────────────────────────────────────────────┤
│                                                                     │
│   throw new IllegalArgumentException("Invalid value");             │
│   ──┬── ─┬─ ─────────────┬────────── ───────┬───────               │
│     │    │               │                  │                      │
│     │    │               │                  └── Message (context)  │
│     │    │               └── Exception type to throw               │
│     │    └── Create NEW instance of exception                      │
│     └── KEYWORD: "throw this exception now!"                       │
│                                                                     │
│   NOTES:                                                            │
│   • Must throw a Throwable (usually Exception or subclass)         │
│   • Can throw existing exception: throw e;                         │
│   • After throw, no more code in this method runs                  │
│     (unless caught in same method)                                 │
│                                                                     │
└────────────────────────────────────────────────────────────────────┘
```

### Example: Validation with throw

```java
public class UserService {
    
    public void createUser(String email, int age) {
        // Validate email
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        
        if (!email.contains("@")) {
            throw new IllegalArgumentException("Invalid email format: " + email);
        }
        
        // Validate age
        if (age < 0 || age > 150) {
            throw new IllegalArgumentException("Age must be between 0 and 150");
        }
        
        // If we get here, all validations passed
        saveUser(email, age);
    }
}
```

---

## 3. The `throws` Keyword

### What It Does

`throws` declares that a method might throw certain exceptions. It's a **contract** that warns callers.

```java
public void readFile(String filename) throws IOException {
    //                                ↑
    //                         Declaration: "I might throw IOException"
    //                         Caller must handle or propagate!
    
    FileReader reader = new FileReader(filename);  // May throw!
    // ...
}
```

### Why Use throws?

```
┌────────────────────────────────────────────────────────────────────┐
│                      WHY USE throws?                                │
├────────────────────────────────────────────────────────────────────┤
│                                                                     │
│   SCENARIO: You can't meaningfully handle the exception here       │
│                                                                     │
│   readConfigFile()                                                  │
│        │                                                            │
│        ▼                                                            │
│   ┌─────────────────────────────────────────────┐                  │
│   │ What should I do if the file doesn't exist? │                  │
│   │                                             │                  │
│   │ • Create a new file? (What content?)        │                  │
│   │ • Use defaults? (What defaults?)            │                  │
│   │ • Ask user? (I don't have UI access!)       │                  │
│   │ • Fail? (Let the caller decide!)     ✓     │                  │
│   └─────────────────────────────────────────────┘                  │
│                                                                     │
│   SOLUTION: Declare "throws" and let CALLER decide!                │
│                                                                     │
│   public Config readConfigFile() throws FileNotFoundException {    │
│       return parseFile("config.txt");                              │
│   }                                                                 │
│                                                                     │
│   // Caller (e.g., Application) can now decide:                    │
│   try {                                                            │
│       Config c = readConfigFile();                                 │
│   } catch (FileNotFoundException e) {                              │
│       createDefaultConfig();  // CALLER knows what defaults to use │
│   }                                                                 │
│                                                                     │
└────────────────────────────────────────────────────────────────────┘
```

### Multiple throws

```java
// Can declare multiple exception types
public void processData() throws IOException, SQLException, ParseException {
    readFromFile();     // throws IOException
    saveToDatabase();   // throws SQLException
    parseData();        // throws ParseException
}
```

---

## 4. Exception Propagation

### How Exceptions Flow Up the Call Stack

```
┌────────────────────────────────────────────────────────────────────────────┐
│                     EXCEPTION PROPAGATION                                   │
├────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│   main() ─────────────┐                                                     │
│                       │ calls                                               │
│                       ▼                                                     │
│   processOrder() ─────┐                                                     │
│                       │ calls                                               │
│                       ▼                                                     │
│   validatePayment() ──┐                                                     │
│                       │ calls                                               │
│                       ▼                                                     │
│   chargeCard() ◀──── throws PaymentException!                              │
│                                                                             │
│   ═══════════════════════════════════════════════════════════              │
│   WHAT HAPPENS NEXT?                                                        │
│   ═══════════════════════════════════════════════════════════              │
│                                                                             │
│   chargeCard() throws PaymentException                                      │
│         │                                                                   │
│         ▼                                                                   │
│   validatePayment() - has catch block?                                      │
│         │                                                                   │
│         │ NO → propagates up                                                │
│         ▼                                                                   │
│   processOrder() - has catch block?                                         │
│         │                                                                   │
│         │ YES! → catches it here                                            │
│         ▼                                                                   │
│   ┌─────────────────────────────────────────┐                              │
│   │ catch (PaymentException e) {            │                              │
│   │     notifyUser("Payment failed");       │                              │
│   │     offerAlternativePayment();          │                              │
│   │ }                                       │                              │
│   └─────────────────────────────────────────┘                              │
│                                                                             │
└────────────────────────────────────────────────────────────────────────────┘
```

### Code Example

```java
public class PaymentFlow {
    
    public static void main(String[] args) {
        try {
            processOrder("ORD-123");
        } catch (PaymentException e) {
            System.out.println("Order failed: " + e.getMessage());
        }
    }
    
    static void processOrder(String orderId) throws PaymentException {
        // Doesn't catch - propagates up
        validatePayment(orderId);
    }
    
    static void validatePayment(String orderId) throws PaymentException {
        // Doesn't catch - propagates up
        chargeCard("4111111111111111", 99.99);
    }
    
    static void chargeCard(String cardNumber, double amount) throws PaymentException {
        if (/* card declined */) {
            throw new PaymentException("Card declined");  // Exception born here!
        }
    }
}
```

---

## 5. When to Handle vs Propagate

```
┌────────────────────────────────────────────────────────────────────────────┐
│                   HANDLE HERE vs PROPAGATE UP?                              │
├────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│   ✅ HANDLE HERE (catch) when:                                              │
│   ────────────────────────────                                              │
│   • You can MEANINGFULLY recover                                            │
│   • You have enough CONTEXT to fix the problem                             │
│   • The caller shouldn't know about this implementation detail              │
│                                                                             │
│   Example:                                                                  │
│   public List<User> getUsers() {                                            │
│       try {                                                                 │
│           return database.query("SELECT * FROM users");                    │
│       } catch (SQLException e) {                                           │
│           log.error("DB query failed, using cache", e);                    │
│           return cache.getUsers();  // Meaningful recovery!                │
│       }                                                                     │
│   }                                                                         │
│                                                                             │
│   ❌ PROPAGATE (throws) when:                                               │
│   ───────────────────────────                                               │
│   • You CAN'T recover meaningfully                                          │
│   • The CALLER needs to know about the failure                              │
│   • The caller has more CONTEXT to decide what to do                       │
│                                                                             │
│   Example:                                                                  │
│   public Config loadConfig(String path) throws IOException {               │
│       return parser.parse(new FileReader(path));                           │
│       // Can't decide what defaults to use - that's caller's job           │
│   }                                                                         │
│                                                                             │
└────────────────────────────────────────────────────────────────────────────┘
```

---

## 6. Rethrowing Exceptions

### Same Exception

```java
try {
    riskyOperation();
} catch (Exception e) {
    log.error("Operation failed", e);
    throw e;  // Rethrow the SAME exception
}
```

### Wrapped Exception (Exception Chaining - covered in c15)

```java
try {
    database.query(sql);
} catch (SQLException e) {
    // Wrap in a higher-level exception
    throw new DataAccessException("Query failed: " + sql, e);
}
```

---

## 7. Common Patterns

### Pattern 1: Validate-and-Throw (Guard Clause)

```java
public void transfer(Account from, Account to, double amount) {
    // Guard clauses - throw immediately if invalid
    if (from == null) throw new IllegalArgumentException("from cannot be null");
    if (to == null) throw new IllegalArgumentException("to cannot be null");
    if (amount <= 0) throw new IllegalArgumentException("amount must be positive");
    if (amount > from.getBalance()) {
        throw new InsufficientFundsException("Not enough balance");
    }
    
    // Happy path - all validations passed
    from.withdraw(amount);
    to.deposit(amount);
}
```

### Pattern 2: Catch-Log-Rethrow

```java
public void processOrder(Order order) throws OrderException {
    try {
        validateOrder(order);
        chargePayment(order);
        shipOrder(order);
    } catch (PaymentException | ShippingException e) {
        log.error("Order processing failed", e);  // Log for debugging
        throw new OrderException("Failed to process order", e);  // Rethrow wrapped
    }
}
```

---

## 8. Summary

```
┌────────────────────────────────────────────────────────────────────────────┐
│                         THROW vs THROWS SUMMARY                             │
├────────────────────────────────────────────────────────────────────────────┤
│                                                                             │
│   throw = ACTION                    throws = DECLARATION                    │
│   "Do it NOW"                       "I might do it"                         │
│                                                                             │
│   throw new Exception();            void foo() throws Exception {...}       │
│   ↑                                             ↑                           │
│   Inside method body                In method signature                     │
│                                                                             │
│   ───────────────────────────────────────────────────────────────────       │
│                                                                             │
│   PROPAGATION RULES:                                                        │
│   • Exception propagates up call stack until caught                         │
│   • Checked exceptions MUST be caught OR declared with throws               │
│   • Unchecked exceptions propagate automatically (no throws needed)         │
│                                                                             │
│   DECISION: Handle or Propagate?                                            │
│   • Can YOU recover? → catch and handle                                     │
│   • Need CALLER to decide? → throws and propagate                           │
│                                                                             │
└────────────────────────────────────────────────────────────────────────────┘
```

---

## What's Next?

Now that you understand throwing and propagating, let's create your OWN exception types:

→ [c14_custom_exceptions](../c14_custom_exceptions/CustomExceptionsNotes.md) - Designing domain-specific exceptions
