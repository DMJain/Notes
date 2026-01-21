# synchronized Keyword in Java

## Prerequisites
- [c5: Synchronization Problem](../c5_synchronization_problem/SynchronizationProblemNotes.md) — Race conditions
- [c6: Mutex Locks](../c6_mutex_locks/MutexLocksNotes.md) — ReentrantLock basics

---

## What You'll Learn

| Question | Answer |
|----------|--------|
| **WHAT** | Java's built-in locking mechanism using the `synchronized` keyword |
| **WHY** | Simpler than ReentrantLock, automatic unlock, less boilerplate |
| **WHEN** | Basic mutual exclusion without advanced features |
| **HOW** | Two forms: synchronized method and synchronized block |

---

## WHY synchronized When We Have ReentrantLock?

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                   ReentrantLock vs synchronized                              │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│   ReentrantLock (c6):              synchronized (c7):                        │
│   ───────────────────              ──────────────────                       │
│                                                                              │
│   lock.lock();                     synchronized (obj) {                      │
│   try {                                // critical section                  │
│       // critical section          }                                        │
│   } finally {                                                                │
│       lock.unlock();               // Automatic unlock! ✅                   │
│   }                                // No try-finally needed! ✅              │
│                                    // Less code! ✅                          │
│   // More code 😕                                                            │
│   // Must remember finally 😕                                                │
│   // Can forget unlock 😕                                                    │
│                                                                              │
│   ┌────────────────────────────────────────────────────────────────────┐    │
│   │  synchronized is SIMPLER but has FEWER FEATURES:                   │    │
│   │                                                                     │    │
│   │  ❌ No tryLock() with timeout                                       │    │
│   │  ❌ No fairness option                                              │    │
│   │  ❌ Cannot interrupt waiting thread                                 │    │
│   │                                                                     │    │
│   │  ✅ Automatic unlock (even on exception)                            │    │
│   │  ✅ Simpler, less error-prone                                       │    │
│   │  ✅ JVM-optimized                                                   │    │
│   └────────────────────────────────────────────────────────────────────┘    │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## Two Forms of synchronized

### Form 1: Synchronized Method

```java
public synchronized void increment() {
    count++;  // Entire method is the critical section
}
```

### Form 2: Synchronized Block

```java
public void increment() {
    synchronized (this) {
        count++;  // Only this part is the critical section
    }
    // Other code can be outside the lock
}
```

---

## How synchronized Works: The Lock Object

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                    THE LOCK OBJECT (MONITOR)                                 │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│   Every Java object has an INTRINSIC LOCK (also called "monitor").          │
│   synchronized uses this built-in lock.                                      │
│                                                                              │
│   ┌────────────────────────────────────────────────────────────────────┐    │
│   │                                                                     │    │
│   │   class Counter {                                                   │    │
│   │       private int count = 0;                                        │    │
│   │                                                                     │    │
│   │       ┌─────────────────────────────────────────────────────────┐  │    │
│   │       │  INTRINSIC LOCK (Monitor)                               │  │    │
│   │       │  ─────────────────────────                              │  │    │
│   │       │  • Every object has one                                 │  │    │
│   │       │  • Used by synchronized                                 │  │    │
│   │       │  • One thread holds it at a time                        │  │    │
│   │       └─────────────────────────────────────────────────────────┘  │    │
│   │   }                                                                │    │
│   │                                                                     │    │
│   └────────────────────────────────────────────────────────────────────┘    │
│                                                                              │
│   synchronized METHOD:                                                       │
│   ─────────────────────                                                     │
│   public synchronized void increment() {                                    │
│       // Locks on 'this' (the Counter object)                               │
│   }                                                                          │
│                                                                              │
│   synchronized BLOCK:                                                        │
│   ───────────────────                                                       │
│   synchronized (this) {          // Locks on 'this'                         │
│       count++;                                                               │
│   }                                                                          │
│                                                                              │
│   synchronized (someOtherObject) { // Locks on a DIFFERENT object           │
│       // Useful for more control                                            │
│   }                                                                          │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## Synchronized Method vs Block

```
┌─────────────────────────────────────────────────────────────────────────────┐
│              SYNCHRONIZED METHOD vs SYNCHRONIZED BLOCK                       │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│   SYNCHRONIZED METHOD                    SYNCHRONIZED BLOCK                  │
│   ───────────────────                    ──────────────────                 │
│                                                                              │
│   public synchronized                    public void method() {              │
│       void method() {                        // Not locked                  │
│       // ENTIRE method locked                doPreprocessing();              │
│       doPreprocessing();                                                    │
│       criticalSection();                     synchronized (this) {          │
│       doPostprocessing();                        criticalSection();         │
│   }                                          }                               │
│                                                                              │
│                                              // Not locked                  │
│                                              doPostprocessing();            │
│                                          }                                   │
│                                                                              │
│   ┌─────────────────────────────────────────────────────────────────────┐   │
│   │  WHICH IS BETTER?                                                    │   │
│   │                                                                      │   │
│   │  Synchronized Method:                                                │   │
│   │  ✅ Simpler, cleaner code                                            │   │
│   │  ❌ Locks more than necessary (entire method)                        │   │
│   │  ❌ Reduces concurrency                                              │   │
│   │                                                                      │   │
│   │  Synchronized Block:                                                 │   │
│   │  ✅ Lock only what you need (finer granularity)                      │   │
│   │  ✅ Better concurrency                                               │   │
│   │  ✅ Can use any lock object                                          │   │
│   │  ❌ Slightly more verbose                                            │   │
│   │                                                                      │   │
│   │  💡 BEST PRACTICE: Use synchronized block with minimal scope         │   │
│   └─────────────────────────────────────────────────────────────────────┘   │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## Object-Level vs Class-Level Lock

```
┌─────────────────────────────────────────────────────────────────────────────┐
│              OBJECT LOCK vs CLASS LOCK                                       │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│   OBJECT-LEVEL LOCK (Instance Lock):                                         │
│   ──────────────────────────────────                                        │
│   • Each INSTANCE has its own lock                                           │
│   • Different instances → Different locks → Can run in parallel             │
│                                                                              │
│   public synchronized void instanceMethod() {                                │
│       // Locks on 'this'                                                     │
│   }                                                                          │
│                                                                              │
│   Counter c1 = new Counter();                                                │
│   Counter c2 = new Counter();                                                │
│   c1.increment();  // Uses c1's lock                                         │
│   c2.increment();  // Uses c2's lock → PARALLEL! (different locks)           │
│                                                                              │
│   ─────────────────────────────────────────────────────────────────────────  │
│                                                                              │
│   CLASS-LEVEL LOCK (Static Lock):                                            │
│   ────────────────────────────────                                          │
│   • ONE lock shared by ALL instances                                         │
│   • Used for static synchronized methods                                     │
│                                                                              │
│   public static synchronized void staticMethod() {                           │
│       // Locks on Counter.class (not 'this')                                 │
│   }                                                                          │
│                                                                              │
│   Counter.staticMethod();  // Uses Counter.class lock                        │
│   Counter.staticMethod();  // Same lock → BLOCKED!                           │
│                                                                              │
│   Equivalent block form:                                                     │
│   synchronized (Counter.class) {                                             │
│       // Uses class-level lock                                               │
│   }                                                                          │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## Fixing Adder-Subtractor with synchronized

### Option 1: Synchronized Method

```java
public class SynchronizedMethodCounter implements ICounter {
    private int count = 0;
    
    @Override
    public synchronized void increment() {
        count++;  // Protected by 'this' lock
    }
    
    @Override
    public synchronized void decrement() {
        count--;  // Same lock, so mutual exclusion
    }
    
    @Override
    public int getCount() {
        return count;
    }
}
```

### Option 2: Synchronized Block

```java
public class SynchronizedBlockCounter implements ICounter {
    private int count = 0;
    private final Object lock = new Object();  // Dedicated lock object
    
    @Override
    public void increment() {
        synchronized (lock) {
            count++;
        }
    }
    
    @Override
    public void decrement() {
        synchronized (lock) {
            count--;
        }
    }
    
    @Override
    public int getCount() {
        return count;
    }
}
```

---

## When to Use Each

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                    DECISION GUIDE                                            │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│                  Need tryLock() or timeout?                                  │
│                           │                                                  │
│              ┌────────────┴────────────┐                                    │
│              YES                       NO                                   │
│              │                         │                                    │
│              ▼                         ▼                                    │
│      ReentrantLock              Need fairness?                              │
│         (c6)                           │                                    │
│                          ┌─────────────┴─────────────┐                      │
│                          YES                         NO                     │
│                          │                           │                      │
│                          ▼                           ▼                      │
│                   ReentrantLock           Need fine-grained control?        │
│                   (fair=true)                        │                      │
│                                        ┌─────────────┴─────────────┐        │
│                                        YES                         NO       │
│                                        │                           │        │
│                                        ▼                           ▼        │
│                                 synchronized           synchronized         │
│                                    block                  method            │
│                                                                              │
│   ┌─────────────────────────────────────────────────────────────────────┐   │
│   │  SUMMARY:                                                            │   │
│   │                                                                      │   │
│   │  • Simple mutual exclusion → synchronized method                    │   │
│   │  • Need smaller critical section → synchronized block               │   │
│   │  • Need timeout/fairness/interruptibility → ReentrantLock           │   │
│   └─────────────────────────────────────────────────────────────────────┘   │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## Common Gotchas

### ❌ Locking on 'this' in Public API

```java
// ❌ DANGEROUS - external code can lock on YOUR object!
public class Counter {
    public synchronized void increment() {  // Uses 'this'
        count++;
    }
}

// External code:
Counter c = new Counter();
synchronized (c) {
    // This blocks c.increment() since it uses same lock!
    Thread.sleep(1000000);  // Denial of service!
}

// ✅ BETTER - use private lock object
public class Counter {
    private final Object lock = new Object();
    
    public void increment() {
        synchronized (lock) {  // External code can't access
            count++;
        }
    }
}
```

### ❌ Different Lock Objects

```java
// ❌ RACE CONDITION - different locks!
public void increment() {
    synchronized (new Object()) {  // NEW object each time!
        count++;
    }
}

// ✅ CORRECT - same lock object
private final Object lock = new Object();
public void increment() {
    synchronized (lock) {  // Same lock
        count++;
    }
}
```

---

## Summary

| Concept | Key Point |
|---------|-----------|
| **synchronized method** | Locks on `this`, entire method is critical section |
| **synchronized block** | Locks on specified object, finer control |
| **Intrinsic lock** | Every object has one, used by synchronized |
| **Object lock** | Each instance has separate lock |
| **Class lock** | Static synchronized uses `ClassName.class` |
| **Auto-unlock** | synchronized always unlocks, even on exception |

### Comparison Table

| Feature | synchronized | ReentrantLock |
|---------|-------------|---------------|
| Syntax | Simple | Verbose |
| Auto-unlock | ✅ Yes | ❌ Need try-finally |
| tryLock | ❌ No | ✅ Yes |
| Fairness | ❌ No | ✅ Yes |
| Timeout | ❌ No | ✅ Yes |
| Interruptible | ❌ No | ✅ Yes |

---

## Project Demo Structure

```
c7_synchronized_keyword/
├── SynchronizedNotes.md          ← You are here
├── Main.java
├── contracts/
│   └── ISynchronizedCounter.java
├── impl/
│   ├── SynchronizedMethodCounter.java
│   └── SynchronizedBlockCounter.java
└── runner/
    ├── SyncRunner.java
    ├── scenarios/
    │   └── MethodVsBlockScenario.java
    └── comparison/
        └── LockVsSyncDemo.java
```

---

## Next Chapter
→ [c8: Atomic Datatypes](../c8_atomic_datatypes/AtomicDatatypesNotes.md) — Lock-free thread safety
