# Lambdas & Functional Interfaces

> **Prerequisites:** Understanding of [Interfaces](../../p1_oops/c5_interfaces/InterfacesNotes.md) and [Polymorphism](../../p1_oops/c4_polymorphism/PolymorphismNotes.md)

---

## What You'll Learn
By the end of this chapter, you'll understand:
- **WHAT** lambdas are (anonymous functions)
- **WHY** they were introduced (cleaner code)
- **WHEN** to use them (and when NOT to)
- **HOW** they work under the hood

---

## 1. Why Do Lambdas Exist? (The Problem)

### The Verbose Past: Anonymous Inner Classes

Before Java 8 (2014), if you wanted to pass behavior (like "how to compare two objects"), you had to write a **lot** of boilerplate code.

**The Old Way - Creating a Thread:**

```java
// 😰 BEFORE Java 8: Anonymous Inner Class
Runnable task = new Runnable() {
    @Override
    public void run() {
        System.out.println("Hello, World!");
    }
};
new Thread(task).start();
```

**What's the actual logic?** Just `System.out.println("Hello, World!");`

**What's boilerplate?** EVERYTHING else! The class declaration, method override, braces...

```
┌──────────────────────────────────────────────────────────┐
│            Anonymous Inner Class Breakdown               │
├──────────────────────────────────────────────────────────┤
│                                                          │
│   new Runnable() {              ← Boilerplate (5 words)  │
│       @Override                 ← Boilerplate            │
│       public void run() {       ← Boilerplate            │
│           System.out.println    ← 🎯 ACTUAL LOGIC!       │
│             ("Hello, World!");                           │
│       }                         ← Boilerplate            │
│   };                            ← Boilerplate            │
│                                                          │
│   📊 Result: 1 line of logic, 6 lines of ceremony!      │
│                                                          │
└──────────────────────────────────────────────────────────┘
```

### The Solution: Lambda Expressions

**The Lambda Way (Java 8+):**

```java
// 😎 AFTER Java 8: Lambda Expression
Runnable task = () -> System.out.println("Hello, World!");
new Thread(task).start();
```

**Same logic. One line.** No boilerplate!

```
┌──────────────────────────────────────────────────────────┐
│               Before vs After Comparison                 │
├───────────────────────────┬──────────────────────────────┤
│      BEFORE (Java 7)      │       AFTER (Java 8+)        │
├───────────────────────────┼──────────────────────────────┤
│ new Runnable() {          │                              │
│     @Override             │                              │
│     public void run() {   │  () -> System.out.println    │
│         System.out        │       ("Hello, World!")      │
│           .println(...)   │                              │
│     }                     │                              │
│ };                        │                              │
├───────────────────────────┼──────────────────────────────┤
│        7 lines            │          1 line              │
│     86% boilerplate       │       100% logic             │
└───────────────────────────┴──────────────────────────────┘
```

### Real-World Analogy

Think of ordering food:

| Approach | Analogy |
|----------|---------|
| **Anonymous Class** | "I would like to formally request the preparation of a meal. The meal shall consist of one burger. The burger should be cooked. Please deliver upon completion." |
| **Lambda** | "One burger, please." |

Same result. Way less ceremony!

---

## 2. What is a Functional Interface?

### Definition

A **Functional Interface** is an interface with **exactly ONE abstract method**.

```java
@FunctionalInterface
interface Greeting {
    void sayHello(String name);  // Only ONE abstract method
}
```

### Why Does This Matter?

Lambdas can ONLY implement functional interfaces because:
- Lambda = One piece of behavior
- Functional Interface = One method to implement
- **Perfect match!**

```
┌───────────────────────────────────────────────────────────┐
│                   The Magic Connection                    │
├───────────────────────────────────────────────────────────┤
│                                                           │
│   Lambda Expression:     (name) -> "Hello, " + name       │
│                              │                            │
│                              │ implements                 │
│                              ▼                            │
│   Functional Interface:  String greet(String name)        │
│                                                           │
│   ✅ One lambda → One method → Perfect fit!               │
│                                                           │
└───────────────────────────────────────────────────────────┘
```

### The @FunctionalInterface Annotation

This is **optional** but recommended:

```java
@FunctionalInterface  // ← Compiler will verify single abstract method
interface Calculator {
    int calculate(int a, int b);
}

// ❌ This would cause a compile error!
@FunctionalInterface
interface InvalidFunctional {
    void method1();
    void method2();  // ERROR: Too many abstract methods!
}
```

### Why NOT Multiple Methods?

If an interface has 2+ abstract methods, the lambda wouldn't know which one to implement!

```
┌───────────────────────────────────────────────────────────┐
│              Why Lambdas Need ONE Method                  │
├───────────────────────────────────────────────────────────┤
│                                                           │
│   Interface with 2 methods:                               │
│   ┌─────────────────────┐                                 │
│   │ void methodA();     │                                 │
│   │ void methodB();     │                                 │
│   └─────────────────────┘                                 │
│                                                           │
│   Lambda: () -> System.out.println("?")                   │
│                                                           │
│   ❓ Is this implementing methodA() or methodB()?         │
│   ❌ AMBIGUOUS! Compiler cannot decide.                   │
│                                                           │
└───────────────────────────────────────────────────────────┘
```

---

## 3. Lambda Syntax Breakdown

### The Anatomy of a Lambda

```
┌──────────────────────────────────────────────────────────┐
│                  Lambda Anatomy                          │
├──────────────────────────────────────────────────────────┤
│                                                          │
│       (parameters)    ->     expression/body             │
│           │           │            │                     │
│           │           │            └── What to DO        │
│           │           │                                  │
│           │           └── Arrow operator ("goes to")     │
│           │                                              │
│           └── Input values (can be empty)                │
│                                                          │
└──────────────────────────────────────────────────────────┘
```

### Syntax Variations

| Parameters | Lambda Syntax | Example |
|------------|---------------|---------|
| Zero | `() -> expression` | `() -> 42` |
| One | `x -> expression` | `x -> x * 2` |
| One (explicit type) | `(int x) -> expression` | `(int x) -> x * 2` |
| Multiple | `(x, y) -> expression` | `(x, y) -> x + y` |
| Multiple (explicit) | `(int x, int y) -> expression` | `(int x, int y) -> x + y` |

### Single Expression vs Block Body

**Single Expression (implicit return):**
```java
// The result is automatically returned
(a, b) -> a + b
```

**Block Body (explicit return needed):**
```java
// Use {} for multiple statements
(a, b) -> {
    int sum = a + b;
    System.out.println("Sum: " + sum);
    return sum;  // Must explicitly return!
}
```

```
┌──────────────────────────────────────────────────────────┐
│           Expression vs Block Comparison                 │
├───────────────────────────┬──────────────────────────────┤
│   Expression (1 line)     │      Block (multiple)        │
├───────────────────────────┼──────────────────────────────┤
│                           │                              │
│   (a, b) -> a + b         │   (a, b) -> {               │
│                           │       int sum = a + b;       │
│   ✓ No braces             │       log(sum);              │
│   ✓ No return             │       return sum;            │
│   ✓ Cleaner               │   }                          │
│                           │                              │
│                           │   ✓ Multiple statements      │
│                           │   ✓ Must use return          │
│                           │                              │
└───────────────────────────┴──────────────────────────────┘
```

### Type Inference: Let Java Figure It Out

Java can **infer** parameter types from context:

```java
// Explicit types (verbose)
Comparator<String> comp1 = (String a, String b) -> a.compareTo(b);

// Inferred types (cleaner) ✅
Comparator<String> comp2 = (a, b) -> a.compareTo(b);
```

**How does Java know?** From the `Comparator<String>` type!

---

## 4. Before/After Examples

### Example 1: Runnable (No Parameters)

```java
// ❌ BEFORE: Anonymous Inner Class
Runnable oldWay = new Runnable() {
    @Override
    public void run() {
        System.out.println("Running task...");
    }
};

// ✅ AFTER: Lambda
Runnable newWay = () -> System.out.println("Running task...");
```

### Example 2: Comparator (Two Parameters)

```java
List<String> names = Arrays.asList("Charlie", "Alice", "Bob");

// ❌ BEFORE: Anonymous Inner Class
Collections.sort(names, new Comparator<String>() {
    @Override
    public int compare(String a, String b) {
        return a.compareTo(b);
    }
});

// ✅ AFTER: Lambda
Collections.sort(names, (a, b) -> a.compareTo(b));

// ✅✅ EVEN BETTER: Method Reference
Collections.sort(names, String::compareTo);
```

### Example 3: Custom Interface

```java
// Define a functional interface
@FunctionalInterface
interface MathOperation {
    int operate(int a, int b);
}

// ❌ BEFORE
MathOperation addOld = new MathOperation() {
    @Override
    public int operate(int a, int b) {
        return a + b;
    }
};

// ✅ AFTER
MathOperation addNew = (a, b) -> a + b;
MathOperation multiply = (a, b) -> a * b;
MathOperation power = (a, b) -> (int) Math.pow(a, b);

// Using them
System.out.println(addNew.operate(5, 3));     // 8
System.out.println(multiply.operate(5, 3));   // 15
System.out.println(power.operate(2, 10));     // 1024
```

### Summary Table

| Interface | Method Signature | Lambda Example |
|-----------|------------------|----------------|
| `Runnable` | `void run()` | `() -> doSomething()` |
| `Comparator<T>` | `int compare(T a, T b)` | `(a, b) -> a.compareTo(b)` |
| `ActionListener` | `void actionPerformed(e)` | `e -> handleClick(e)` |
| Custom | `R method(T arg)` | `arg -> process(arg)` |

---

## 5. Built-in Functional Interfaces

Java 8 added many functional interfaces in `java.util.function` package. Here are the **Big Four**:

### Overview

```
┌───────────────────────────────────────────────────────────────────┐
│                    The Big Four Interfaces                        │
├─────────────────┬────────────────────────────────────────────────┤
│                 │                                                 │
│   Predicate<T>  │   T ──▶ [ test ] ──▶ boolean                   │
│                 │   "Does this pass the test?"                    │
│                 │                                                 │
├─────────────────┼────────────────────────────────────────────────┤
│                 │                                                 │
│   Function<T,R> │   T ──▶ [ apply ] ──▶ R                        │
│                 │   "Transform T into R"                          │
│                 │                                                 │
├─────────────────┼────────────────────────────────────────────────┤
│                 │                                                 │
│   Consumer<T>   │   T ──▶ [ accept ] ──▶ void                    │
│                 │   "Do something with T (no return)"            │
│                 │                                                 │
├─────────────────┼────────────────────────────────────────────────┤
│                 │                                                 │
│   Supplier<T>   │   () ──▶ [ get ] ──▶ T                         │
│                 │   "Give me a T (no input)"                      │
│                 │                                                 │
└─────────────────┴────────────────────────────────────────────────┘
```

### 5.1 Predicate<T> - The Tester

**Purpose:** Test a condition, return true/false

```java
import java.util.function.Predicate;

// Check if a number is even
Predicate<Integer> isEven = n -> n % 2 == 0;

System.out.println(isEven.test(4));  // true
System.out.println(isEven.test(7));  // false

// Chain predicates!
Predicate<Integer> isPositive = n -> n > 0;
Predicate<Integer> isPositiveEven = isEven.and(isPositive);

System.out.println(isPositiveEven.test(4));   // true
System.out.println(isPositiveEven.test(-4));  // false
```

**Common Use:** Filtering collections with `.filter()`

### 5.2 Function<T, R> - The Transformer

**Purpose:** Transform input T to output R

```java
import java.util.function.Function;

// Convert String to its length
Function<String, Integer> stringLength = s -> s.length();

System.out.println(stringLength.apply("Hello"));  // 5
System.out.println(stringLength.apply("Lambda")); // 6

// Chain functions!
Function<String, String> toUpperCase = String::toUpperCase;
Function<String, String> addExclamation = s -> s + "!";
Function<String, String> shout = toUpperCase.andThen(addExclamation);

System.out.println(shout.apply("hello")); // "HELLO!"
```

**Common Use:** Mapping values with `.map()`

### 5.3 Consumer<T> - The Action Taker

**Purpose:** Do something with input (no return value)

```java
import java.util.function.Consumer;

// Print a greeting
Consumer<String> greet = name -> System.out.println("Hello, " + name);

greet.accept("Alice");  // Hello, Alice
greet.accept("Bob");    // Hello, Bob

// Chain consumers!
Consumer<String> shout = name -> System.out.println(name.toUpperCase());
Consumer<String> greetAndShout = greet.andThen(shout);

greetAndShout.accept("Charlie");
// Hello, Charlie
// CHARLIE
```

**Common Use:** Performing actions with `.forEach()`

### 5.4 Supplier<T> - The Provider

**Purpose:** Provide/generate a value (no input)

```java
import java.util.function.Supplier;

// Generate random numbers
Supplier<Double> randomValue = () -> Math.random();

System.out.println(randomValue.get());  // 0.7234...
System.out.println(randomValue.get());  // 0.1567...

// Lazy initialization
Supplier<ExpensiveObject> lazy = () -> new ExpensiveObject();
// Object not created yet!

// Later, when needed:
ExpensiveObject obj = lazy.get();  // NOW it's created
```

**Common Use:** Lazy initialization, default values with `.orElseGet()`

### Quick Reference Table

| Interface | Method | Input | Output | Use Case |
|-----------|--------|-------|--------|----------|
| `Predicate<T>` | `test(T)` | T | boolean | Filtering |
| `Function<T,R>` | `apply(T)` | T | R | Transforming |
| `Consumer<T>` | `accept(T)` | T | void | Side effects |
| `Supplier<T>` | `get()` | none | T | Generating |

### Specialized Variants (For Primitives)

To avoid boxing overhead, Java provides specialized versions:

| Base | int | long | double |
|------|-----|------|--------|
| `Predicate<T>` | `IntPredicate` | `LongPredicate` | `DoublePredicate` |
| `Function<T,R>` | `IntFunction<R>` | `LongFunction<R>` | `DoubleFunction<R>` |
| `Consumer<T>` | `IntConsumer` | `LongConsumer` | `DoubleConsumer` |
| `Supplier<T>` | `IntSupplier` | `LongSupplier` | `DoubleSupplier` |

---

## 6. Method References (::)

Method references are a **shorthand** for lambdas when you're just calling an existing method.

### The Pattern

```
┌──────────────────────────────────────────────────────────┐
│              Lambda vs Method Reference                  │
├──────────────────────────────────────────────────────────┤
│                                                          │
│   Lambda:           x -> System.out.println(x)           │
│                          │                               │
│                          ▼                               │
│   Method Reference: System.out::println                  │
│                                                          │
│   Rule: If lambda just CALLS a method, use ::            │
│                                                          │
└──────────────────────────────────────────────────────────┘
```

### Types of Method References

#### 1. Static Method Reference

```java
// Lambda
Function<String, Integer> parse1 = s -> Integer.parseInt(s);

// Method Reference
Function<String, Integer> parse2 = Integer::parseInt;
```

**Pattern:** `ClassName::staticMethod`

#### 2. Instance Method Reference (on a specific object)

```java
String prefix = "Hello, ";

// Lambda
Function<String, String> greet1 = name -> prefix.concat(name);

// Method Reference
Function<String, String> greet2 = prefix::concat;
```

**Pattern:** `instance::instanceMethod`

#### 3. Instance Method Reference (on the parameter)

```java
// Lambda
Function<String, String> upper1 = s -> s.toUpperCase();

// Method Reference (called ON the parameter)
Function<String, String> upper2 = String::toUpperCase;
```

**Pattern:** `ClassName::instanceMethod` (first param becomes `this`)

#### 4. Constructor Reference

```java
// Lambda
Supplier<ArrayList<String>> list1 = () -> new ArrayList<>();

// Method Reference
Supplier<ArrayList<String>> list2 = ArrayList::new;

// With parameter
Function<Integer, ArrayList<String>> sizedList = ArrayList::new;
```

**Pattern:** `ClassName::new`

### Reference Table

| Type | Lambda | Method Reference |
|------|--------|------------------|
| Static | `x -> ClassName.method(x)` | `ClassName::method` |
| Instance (specific) | `x -> object.method(x)` | `object::method` |
| Instance (on param) | `x -> x.method()` | `ClassName::method` |
| Constructor | `() -> new ClassName()` | `ClassName::new` |

### When to Use Method References?

| Use Method Reference | Use Lambda |
|---------------------|------------|
| Just calling one method | Multiple operations |
| Cleaner, more readable | Need to transform arguments |
| The method matches exactly | Custom logic required |

```java
// ✅ Method Reference: clean and simple
names.forEach(System.out::println);

// ✅ Lambda: need custom logic
names.forEach(name -> System.out.println("Name: " + name));
```

---

## 7. When to Use Lambdas (and When NOT To)

### ✅ USE Lambdas When:

| Scenario | Example |
|----------|---------|
| **Short, simple logic** | `x -> x * 2` |
| **Stream operations** | `.filter(x -> x > 0).map(x -> x * 2)` |
| **Event handlers** | `button.onClick(e -> handleClick(e))` |
| **Callbacks** | `service.fetch(result -> processResult(result))` |
| **Strategy pattern** | `sort(list, (a, b) -> a.age - b.age)` |

### ❌ DON'T Use Lambdas When:

| Scenario | Why Not | Alternative |
|----------|---------|-------------|
| **Complex logic (5+ lines)** | Hard to read | Extract to a method |
| **Reused in many places** | Code duplication | Create a named method |
| **Need debugging** | Can't set breakpoints inside | Use a method |
| **Need documentation** | Lambdas can't have Javadoc | Use a method |
| **Throw checked exceptions** | Messy exception handling | Use a method |

### Example: When Lambda Gets Too Long

```java
// ❌ BAD: Too complex for a lambda
users.stream()
    .filter(user -> {
        if (user.getAge() < 18) return false;
        if (user.getSubscription() == null) return false;
        if (user.getSubscription().isExpired()) return false;
        if (!user.hasVerifiedEmail()) return false;
        return user.getCountry().equals("US");
    })
    .collect(toList());

// ✅ GOOD: Extract to a method
users.stream()
    .filter(this::isEligibleUser)  // Method reference, readable!
    .collect(toList());

private boolean isEligibleUser(User user) {
    if (user.getAge() < 18) return false;
    if (user.getSubscription() == null) return false;
    if (user.getSubscription().isExpired()) return false;
    if (!user.hasVerifiedEmail()) return false;
    return user.getCountry().equals("US");
}
```

---

## 8. Common Mistakes & Gotchas

### Gotcha 1: Variable Capture (Effectively Final)

Lambdas can access variables from the enclosing scope, but those variables must be **effectively final** (never reassigned).

```java
// ✅ WORKS: count is never reassigned
int count = 10;
Runnable r = () -> System.out.println(count);

// ❌ COMPILE ERROR: count is reassigned!
int count = 10;
Runnable r = () -> System.out.println(count);
count = 20;  // Assignment makes lambda invalid
```

**Why?** Lambdas might run later (in another thread). If the variable changed, which value should the lambda see?

```
┌───────────────────────────────────────────────────────────┐
│               Why "Effectively Final" Matters             │
├───────────────────────────────────────────────────────────┤
│                                                           │
│   Thread 1                    Thread 2 (Lambda)           │
│   ─────────                   ─────────────────           │
│   int x = 10;                                             │
│   lambda = () -> print(x)     (waiting...)                │
│   x = 20;                                                 │
│                               print(x) → 10 or 20???      │
│                                                           │
│   ❓ Ambiguous! Java prevents this confusion.             │
│                                                           │
└───────────────────────────────────────────────────────────┘
```

**Workaround:** Use a container (array or AtomicInteger)

```java
// Workaround: Use a single-element array
int[] counter = {0};
Runnable r = () -> counter[0]++;  // ✅ Array reference is final
```

### Gotcha 2: `this` Refers to Enclosing Class

Unlike anonymous inner classes, `this` in a lambda refers to the **enclosing class**, not the lambda itself.

```java
public class Example {
    String name = "Outer";
    
    void demo() {
        // Anonymous inner class: this = the anonymous class
        Runnable r1 = new Runnable() {
            String name = "Inner";
            public void run() {
                System.out.println(this.name);  // "Inner"
            }
        };
        
        // Lambda: this = Example instance
        Runnable r2 = () -> {
            System.out.println(this.name);  // "Outer"
        };
    }
}
```

### Gotcha 3: Checked Exceptions

Functional interfaces don't declare checked exceptions, so lambdas can't throw them directly.

```java
// ❌ COMPILE ERROR: IOException is checked
Function<String, String> readFile = path -> {
    return Files.readString(Path.of(path));  // Throws IOException!
};

// ✅ WORKAROUND 1: Wrap in try-catch
Function<String, String> readFile = path -> {
    try {
        return Files.readString(Path.of(path));
    } catch (IOException e) {
        throw new RuntimeException(e);  // Convert to unchecked
    }
};

// ✅ WORKAROUND 2: Create a custom functional interface
@FunctionalInterface
interface CheckedFunction<T, R> {
    R apply(T t) throws Exception;
}
```

---

## 9. Connection to Streams

Lambdas are the **building blocks** for Java Streams.

```java
List<String> names = Arrays.asList("Alice", "Bob", "Charlie", "David");

// Lambdas power every stream operation!
List<String> result = names.stream()
    .filter(name -> name.length() > 3)        // Predicate
    .map(name -> name.toUpperCase())          // Function
    .sorted((a, b) -> a.compareTo(b))         // Comparator
    .collect(Collectors.toList());

// With method references (even cleaner)
List<String> result = names.stream()
    .filter(name -> name.length() > 3)
    .map(String::toUpperCase)
    .sorted(String::compareTo)
    .collect(Collectors.toList());
```

**Next Chapter:** Learn how Streams leverage lambdas to create powerful data pipelines in [c3_streams](../c3_streams/StreamsNotes.md)!

---

## 10. Summary

### Key Concepts

| Concept | Definition |
|---------|------------|
| **Lambda** | Anonymous function: `(params) -> expression` |
| **Functional Interface** | Interface with exactly ONE abstract method |
| **Method Reference** | Shorthand for lambdas that just call a method: `::` |
| **Effectively Final** | Variables captured by lambdas can't be reassigned |

### The Big Picture

```
┌─────────────────────────────────────────────────────────────┐
│                    Lambda Ecosystem                         │
├─────────────────────────────────────────────────────────────┤
│                                                             │
│   @FunctionalInterface ──────► Lambda Expression            │
│          │                           │                      │
│          │                           │                      │
│          ▼                           ▼                      │
│   Predicate<T>               (x) -> x > 0                   │
│   Function<T,R>              (x) -> x.toString()            │
│   Consumer<T>                (x) -> System.out.println(x)   │
│   Supplier<T>                () -> new Object()             │
│          │                           │                      │
│          │                           │                      │
│          ▼                           ▼                      │
│   Built-in Interfaces ◀──────Method References              │
│          │                      System.out::println         │
│          │                      String::toUpperCase         │
│          │                      ArrayList::new              │
│          │                                                  │
│          └──────────► STREAMS API (next chapter!)           │
│                                                             │
└─────────────────────────────────────────────────────────────┘
```

### Quick Reference

```java
// Zero parameters
() -> 42

// One parameter (type inferred)
x -> x * 2

// Multiple parameters
(a, b) -> a + b

// Block body with return
(a, b) -> {
    int result = a + b;
    return result;
}

// Method references
System.out::println      // Static/instance method
String::toUpperCase      // Instance method on parameter
ArrayList::new           // Constructor
```

---

> **Remember:** Lambdas are about **passing behavior, not just data**.
> Before lambdas: "Here's some data, figure out what to do with it."
> With lambdas: "Here's WHAT to do, apply it to the data."

Happy coding! 🚀
