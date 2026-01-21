# Mutex Locks in Java (ReentrantLock)

## Prerequisites
- [c5: Synchronization Problem](../c5_synchronization_problem/SynchronizationProblemNotes.md) — Understanding race conditions

---

## What You'll Learn

| Question | Answer |
|----------|--------|
| **WHAT** | Mutex = "Mutual Exclusion" lock. Only ONE thread can hold it at a time. |
| **WHY** | To prevent race conditions by guarding critical sections |
| **WHEN** | When you need explicit control over locking (tryLock, fairness, etc.) |
| **HOW** | Using `java.util.concurrent.locks.ReentrantLock` |

---

## The Solution to Race Condition

In c5, we saw the problem. Now we fix it:

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                         THE MUTEX SOLUTION                                   │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│   BEFORE (Race Condition):            AFTER (With Mutex Lock):               │
│   ────────────────────────            ─────────────────────────             │
│                                                                              │
│   Thread A: READ                      Thread A: LOCK → READ → MODIFY         │
│   Thread B: READ  (same old value!)                    → WRITE → UNLOCK     │
│   Thread A: MODIFY → WRITE                                                   │
│   Thread B: MODIFY → WRITE (LOST!)    Thread B: WAIT... LOCK → READ          │
│                                                → MODIFY → WRITE → UNLOCK    │
│   Result: Unpredictable ❌             Result: Always correct ✅             │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## WHY Mutex? The Room with a Key Analogy

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                    🔑 THE ROOM WITH A KEY ANALOGY                           │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│   Imagine a ROOM (Critical Section) with only ONE KEY (Lock):                │
│                                                                              │
│   ┌──────────────────────────────────────────────────────────────────────┐  │
│   │                                                                       │  │
│   │    🚪 ┌─────────────────────────────────┐                            │  │
│   │       │                                 │   🔑 (Single Key)          │  │
│   │       │    CRITICAL SECTION             │                            │  │
│   │       │    (Shared Counter)             │   Rules:                   │  │
│   │       │                                 │   1. Take key to enter     │  │
│   │       │    count = count + 1;           │   2. Only 1 key exists     │  │
│   │       │                                 │   3. Return key when done  │  │
│   │       └─────────────────────────────────┘                            │  │
│   │                                                                       │  │
│   │   Thread A: Has the 🔑, inside the room, doing work                  │  │
│   │   Thread B: "I need the key!" → MUST WAIT outside                    │  │
│   │   Thread C: "I need the key!" → MUST WAIT outside                    │  │
│   │                                                                       │  │
│   └──────────────────────────────────────────────────────────────────────┘  │
│                                                                              │
│   When Thread A finishes:                                                    │
│   1. Thread A puts key back                                                  │
│   2. Thread B grabs key, enters room                                         │
│   3. Thread C still waits                                                    │
│                                                                              │
│   ✅ MUTUAL EXCLUSION: Only ONE person in room at a time!                   │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## ReentrantLock: Java's Mutex

### Basic Usage

```java
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

Lock lock = new ReentrantLock();

// Enter critical section
lock.lock();
try {
    // CRITICAL SECTION - only one thread here at a time
    count++;
} finally {
    // Exit critical section
    lock.unlock();
}
```

### Why "Reentrant"?

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                         WHY "REENTRANT"?                                     │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│   "Reentrant" means the SAME THREAD can acquire the lock MULTIPLE TIMES.    │
│                                                                              │
│   ┌────────────────────────────────────────────────────────────────────┐    │
│   │  public void outerMethod() {                                        │    │
│   │      lock.lock();           // Thread A acquires lock (count = 1)   │    │
│   │      try {                                                          │    │
│   │          innerMethod();     // Calls another locked method          │    │
│   │      } finally {                                                    │    │
│   │          lock.unlock();     // count = 0, lock released            │    │
│   │      }                                                              │    │
│   │  }                                                                  │    │
│   │                                                                     │    │
│   │  public void innerMethod() {                                        │    │
│   │      lock.lock();           // Same thread, count = 2 (allowed!)   │    │
│   │      try {                                                          │    │
│   │          // do work                                                 │    │
│   │      } finally {                                                    │    │
│   │          lock.unlock();     // count = 1                           │    │
│   │      }                                                              │    │
│   │  }                                                                  │    │
│   └────────────────────────────────────────────────────────────────────┘    │
│                                                                              │
│   WITHOUT reentrant capability → DEADLOCK (thread waits for itself!)        │
│   WITH reentrant capability   → Works perfectly!                            │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## ReentrantLock Methods

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                       REENTRANTLOCK API                                      │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│   lock()                                                                     │
│   ──────                                                                    │
│   • Acquires the lock                                                        │
│   • BLOCKS if another thread holds it (waits forever)                        │
│   • Use in try-finally to ensure unlock                                      │
│                                                                              │
│   unlock()                                                                   │
│   ────────                                                                  │
│   • Releases the lock                                                        │
│   • ⚠️ MUST be called same number of times as lock()                        │
│   • Put in finally block to guarantee execution                              │
│                                                                              │
│   tryLock()                                                                  │
│   ─────────                                                                 │
│   • Attempts to acquire lock WITHOUT BLOCKING                                │
│   • Returns true if successful, false otherwise                              │
│   • Useful for avoiding deadlocks                                            │
│                                                                              │
│   tryLock(long timeout, TimeUnit unit)                                       │
│   ────────────────────────────────────                                      │
│   • Waits up to the specified time for the lock                              │
│   • Returns true if acquired, false if timeout expires                       │
│                                                                              │
│   isLocked()                                                                 │
│   ──────────                                                                │
│   • Returns true if lock is held by any thread                               │
│                                                                              │
│   isHeldByCurrentThread()                                                    │
│   ───────────────────────                                                   │
│   • Returns true if current thread holds the lock                            │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## Fixing Adder-Subtractor with ReentrantLock

### Before (Problem from c5)

```java
// ❌ RACE CONDITION - count++ is not atomic!
public void increment() {
    count++;  // READ → MODIFY → WRITE can be interrupted!
}
```

### After (Fixed with Lock)

```java
private Lock lock = new ReentrantLock();

// ✅ Thread-safe with lock
public void increment() {
    lock.lock();
    try {
        count++;  // Protected! Only one thread can execute this at a time.
    } finally {
        lock.unlock();  // ALWAYS unlock in finally!
    }
}
```

---

## tryLock() Pattern - Avoiding Deadlocks

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                      tryLock() vs lock()                                     │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│   lock():                              tryLock():                            │
│   ───────                              ──────────                           │
│   • Will WAIT FOREVER                  • Returns IMMEDIATELY                 │
│   • Can cause deadlock                 • Returns true/false                  │
│   • Simpler code                       • More control                        │
│                                                                              │
│   ┌────────────────────────────────────────────────────────────────────┐    │
│   │  // Using tryLock() for timeout-based locking                       │    │
│   │                                                                     │    │
│   │  if (lock.tryLock(5, TimeUnit.SECONDS)) {                           │    │
│   │      try {                                                          │    │
│   │          // Got the lock! Do work.                                  │    │
│   │          updateCounter();                                           │    │
│   │      } finally {                                                    │    │
│   │          lock.unlock();                                             │    │
│   │      }                                                              │    │
│   │  } else {                                                           │    │
│   │      // Couldn't get lock in 5 seconds                              │    │
│   │      System.out.println("Timed out waiting for lock!");            │    │
│   │      // Handle gracefully - maybe retry or give up                  │    │
│   │  }                                                                  │    │
│   └────────────────────────────────────────────────────────────────────┘    │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## Properties of ReentrantLock

| Property | Explanation |
|----------|-------------|
| **Mutual Exclusion** | ✅ Only one thread can hold the lock |
| **Progress** | ✅ When lock is free, waiting threads can acquire it |
| **Bounded Waiting** | ✅ With fairness=true, threads enter in FIFO order |
| **No Deadlock** | ⚠️ Possible if you forget to unlock (use finally!) |
| **No Busy Waiting** | ✅ Blocked threads don't spin, they sleep |

### Fairness Option

```java
// Non-fair lock (default) - faster but threads might starve
Lock lock = new ReentrantLock();

// Fair lock - threads acquire in FIFO order
Lock fairLock = new ReentrantLock(true);
```

---

## When to Use ReentrantLock vs synchronized

```
┌─────────────────────────────────────────────────────────────────────────────┐
│            ReentrantLock vs synchronized                                     │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│   Use ReentrantLock when you need:        Use synchronized when:             │
│   ─────────────────────────────────       ────────────────────────          │
│   • tryLock() with timeout                • Simple locking is enough         │
│   • Fairness guarantee                    • Less code/boilerplate           │
│   • Multiple conditions waiting           • Auto-release on exception        │
│   • Interruptible lock acquisition        • No special features needed       │
│                                                                              │
│   ┌───────────────────────────────────────────────────────────────────┐     │
│   │                  PREFER synchronized when:                         │     │
│   │                                                                    │     │
│   │  • You just need basic mutual exclusion                           │     │
│   │  • You want simpler, less error-prone code                        │     │
│   │  • Lock is always released (even on exceptions)                   │     │
│   │                                                                    │     │
│   │                  PREFER ReentrantLock when:                        │     │
│   │                                                                    │     │
│   │  • You need tryLock() to avoid blocking                           │     │
│   │  • You need fairness (FIFO ordering)                              │     │
│   │  • You need to lock/unlock in different scopes                    │     │
│   │  • You need to create multiple Condition objects                  │     │
│   └───────────────────────────────────────────────────────────────────┘     │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## Common Gotchas

### ❌ Forgetting to Unlock

```java
// ❌ DANGEROUS - if exception occurs, lock is NEVER released!
lock.lock();
doSomething();  // What if this throws an exception?
lock.unlock();  // Never reached! Lock held forever → DEADLOCK!

// ✅ CORRECT - ALWAYS use try-finally
lock.lock();
try {
    doSomething();
} finally {
    lock.unlock();  // Always executes, even on exception
}
```

### ❌ Unlocking Without Locking

```java
// ❌ RUNTIME ERROR - IllegalMonitorStateException!
lock.unlock();  // You don't hold the lock!

// ✅ CORRECT - only unlock if you hold the lock
if (lock.tryLock()) {
    try {
        doWork();
    } finally {
        lock.unlock();  // Safe - we definitely have the lock
    }
}
```

### ❌ Different Lock Instances

```java
// ❌ RACE CONDITION - each counter has ITS OWN lock!
class Counter {
    private Lock lock = new ReentrantLock();  // Each instance gets new lock
    // ...
}

// One lock per counter = NO synchronization between threads!

// ✅ CORRECT - share the SAME lock instance
Lock sharedLock = new ReentrantLock();
Counter counter1 = new Counter(sharedLock);
Counter counter2 = new Counter(sharedLock);  // Same lock!
```

---

## When NOT to Use Mutex

### ❌ Anti-Patterns

| Scenario | Why It's Wrong |
|----------|----------------|
| **Read-only data** | No writes = no race condition |
| **Single-threaded code** | No concurrency = no need for locks |
| **Large critical sections** | Kills parallelism, becomes sequential |
| **Locking on primitive operations** | Use Atomic types instead (c8) |

---

## Summary

| Concept | Key Point |
|---------|-----------|
| **Mutex** | Mutual Exclusion lock - only ONE thread at a time |
| **ReentrantLock** | Java's explicit lock, same thread can acquire multiple times |
| **lock()** | Acquire lock, blocks if unavailable |
| **unlock()** | Release lock, MUST be in finally block |
| **tryLock()** | Non-blocking attempt to acquire lock |
| **Fair lock** | `new ReentrantLock(true)` for FIFO ordering |

### The Big Picture

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                    SYNCHRONIZATION JOURNEY                                   │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│   c5: The Problem                                                            │
│   ───────────────                                                           │
│   count++ is not atomic → Race conditions                                    │
│                                                                              │
│   c6: Solution #1 - ReentrantLock  ← YOU ARE HERE                           │
│   ──────────────────────────────────                                        │
│   Explicit lock() / unlock() with fine-grained control                       │
│                                                                              │
│   c7: Solution #2 - synchronized keyword                                     │
│   ──────────────────────────────────────                                    │
│   Simpler syntax, automatic unlock                                           │
│                                                                              │
│   c8: Solution #3 - Atomic types                                             │
│   ──────────────────────────────                                            │
│   Lock-free thread safety using hardware CAS                                 │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## Project Demo Structure

```
c6_mutex_locks/
├── MutexLocksNotes.md          ← You are here
├── Main.java                   ← Entry point
├── contracts/
│   └── ILockableCounter.java   ← Counter interface with lock
├── impl/
│   └── ReentrantLockCounter.java  ← Thread-safe implementation
└── runner/
    ├── MutexRunner.java        ← Orchestrates demos
    └── scenarios/
        ├── LockSolutionScenario.java   ← Shows fix
        └── TryLockScenario.java        ← tryLock() demo
```

---

## Next Chapter
→ [c7: synchronized keyword](../c7_synchronized_keyword/SynchronizedNotes.md) — Java's built-in lock mechanism
