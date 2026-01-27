# Semaphores in Java

## Prerequisites
- [c6: Mutex Locks](../c6_mutex_locks/MutexLocksNotes.md) — ReentrantLock basics
- [c7: Synchronized Keyword](../c7_synchronized_keyword/SynchronizedNotes.md) — Intrinsic locks
- [c8: Atomic Datatypes](../c8_atomic_datatypes/AtomicDatatypesNotes.md) — Lock-free operations
- Understanding of thread blocking and scheduling

---

## What You'll Learn

| Question | Answer |
|----------|--------|
| **WHAT** | Semaphore = a counter of available "permits" that threads can acquire/release |
| **WHY** | To control access when **more than one** thread can use a resource simultaneously |
| **WHEN** | Connection pools, rate limiting, bounded resource access |
| **HOW** | `acquire()` decrements permits, `release()` increments permits |

---

## Why Do Semaphores Exist? (The History)

### The Problem with Mutex

```
┌─────────────────────────────────────────────────────────────────┐
│                    THE MUTEX LIMITATION                          │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│   SCENARIO: Database Connection Pool (10 connections available)  │
│                                                                  │
│   WITH MUTEX (only 1 thread at a time):                          │
│   ┌──────────────────────────────────────────────────────────┐  │
│   │                                                           │  │
│   │   Thread 1: ████████████  Using connection               │  │
│   │   Thread 2: ░░░░░░░░░░░░  WAITING (9 connections idle!)  │  │
│   │   Thread 3: ░░░░░░░░░░░░  WAITING (9 connections idle!)  │  │
│   │   Thread 4: ░░░░░░░░░░░░  WAITING (9 connections idle!)  │  │
│   │                                                           │  │
│   │   💀 PROBLEM: Only 1 thread uses resource at a time!     │  │
│   │              9 perfectly good connections sit idle!       │  │
│   │                                                           │  │
│   └──────────────────────────────────────────────────────────┘  │
│                                                                  │
│   WE NEED: Allow UP TO 10 threads simultaneously!                │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

### The Semaphore Solution

```
┌─────────────────────────────────────────────────────────────────┐
│                    THE SEMAPHORE SOLUTION                        │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│   SEMAPHORE(10) = "10 permits available"                         │
│                                                                  │
│   ┌──────────────────────────────────────────────────────────┐  │
│   │                                                           │  │
│   │   Thread 1: ████████  Connection 1   ─┐                  │  │
│   │   Thread 2: ████████  Connection 2    │                  │  │
│   │   Thread 3: ██████    Connection 3    ├─► ALL RUNNING!   │  │
│   │          ...                          │                  │  │
│   │   Thread 10: ████     Connection 10  ─┘                  │  │
│   │                                                           │  │
│   │   Thread 11: ░░░░░░░░  WAITING (no permits left)         │  │
│   │                                                           │  │
│   │   ✅ SOLUTION: Up to 10 threads can proceed!              │  │
│   │                                                           │  │
│   └──────────────────────────────────────────────────────────┘  │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

> **Key Insight**: A semaphore is NOT a lock—it's a **permit counter**. Mutex = 1 permit. Semaphore = N permits.

---

## What is a Semaphore?

A **Semaphore** is a synchronization primitive that maintains a set of permits (like tokens or tickets).

```
┌─────────────────────────────────────────────────────────────────┐
│              SEMAPHORE = PERMIT BUCKET                           │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│   Semaphore(3) created:                                          │
│                                                                  │
│   ┌─────────────────────┐                                       │
│   │   PERMIT BUCKET     │                                       │
│   │   ┌───┬───┬───┐     │     Capacity: 3 permits               │
│   │   │ 🎫│ 🎫│ 🎫│     │     Available: 3 permits              │
│   │   └───┴───┴───┘     │                                       │
│   └─────────────────────┘                                       │
│                                                                  │
│   Thread A calls acquire():                                      │
│   ┌─────────────────────┐                                       │
│   │   ┌───┬───┬───┐     │     Thread A takes 1 permit           │
│   │   │ 🎫│ 🎫│   │ ──► │     Available: 2 permits              │
│   │   └───┴───┴───┘     │                                       │
│   └─────────────────────┘     Thread A: 🎫 (has permit)         │
│                                                                  │
│   Thread B, C call acquire():                                    │
│   ┌─────────────────────┐                                       │
│   │   ┌───┬───┬───┐     │     Available: 0 permits              │
│   │   │   │   │   │     │                                       │
│   │   └───┴───┴───┘     │     A: 🎫  B: 🎫  C: 🎫               │
│   └─────────────────────┘                                       │
│                                                                  │
│   Thread D calls acquire():                                      │
│   ┌─────────────────────┐                                       │
│   │   ┌───┬───┬───┐     │     Thread D: ⏳ BLOCKED!             │
│   │   │   │   │   │     │     (waits until someone releases)    │
│   │   └───┴───┴───┘     │                                       │
│   └─────────────────────┘                                       │
│                                                                  │
│   Thread A calls release():                                      │
│   ┌─────────────────────┐                                       │
│   │   ┌───┬───┬───┐     │     Thread A returns permit           │
│   │   │ 🎫│   │   │     │     Thread D: Wakes up, takes permit  │
│   │   └───┴───┴───┘     │                                       │
│   └─────────────────────┘                                       │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

### Types of Semaphores

| Type | Permits | Use Case |
|------|---------|----------|
| **Binary Semaphore** | 0 or 1 | Acts like a mutex (mutual exclusion) |
| **Counting Semaphore** | 0 to N | Limits concurrent access to N threads |

---

## Java Semaphore API

### Creating a Semaphore

```java
import java.util.concurrent.Semaphore;

// Counting semaphore with 5 permits
Semaphore semaphore = new Semaphore(5);

// With fairness (FIFO order for waiting threads)
Semaphore fairSemaphore = new Semaphore(5, true);
```

### Core Methods

```
┌─────────────────────────────────────────────────────────────────┐
│                    SEMAPHORE METHODS                             │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│   acquire()                                                      │
│   ─────────                                                     │
│   • Blocks until a permit is available                          │
│   • Decrements permit count by 1                                │
│   • Throws InterruptedException if thread is interrupted        │
│                                                                  │
│   release()                                                      │
│   ─────────                                                     │
│   • Increments permit count by 1                                │
│   • Wakes up a waiting thread (if any)                          │
│   • Can be called by ANY thread (not just the one who acquired) │
│                                                                  │
│   tryAcquire()                                                   │
│   ────────────                                                  │
│   • Non-blocking: returns immediately                           │
│   • Returns true if permit acquired, false otherwise            │
│                                                                  │
│   tryAcquire(timeout, unit)                                      │
│   ─────────────────────────                                     │
│   • Blocks up to specified timeout                              │
│   • Returns true if acquired within timeout, false otherwise    │
│                                                                  │
│   availablePermits()                                             │
│   ──────────────────                                            │
│   • Returns current number of available permits                 │
│   • Snapshot value (may change immediately after)               │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

### Method Comparison

| Method | Blocks? | Returns | Best For |
|--------|---------|---------|----------|
| `acquire()` | Yes | void | Must have permit |
| `tryAcquire()` | No | boolean | Non-blocking check |
| `tryAcquire(timeout, unit)` | Partial | boolean | Timeout-based access |

---

## Before/After: Mutex vs Semaphore

### ❌ Before: Using Mutex for Connection Pool

```java
// PROBLEM: Only 1 connection used at a time!
public class BrokenConnectionPool {
    private final Lock lock = new ReentrantLock();
    private final List<Connection> connections = new ArrayList<>();
    
    public Connection getConnection() {
        lock.lock();  // ❌ Only 1 thread can enter
        try {
            return connections.remove(0);
        } finally {
            lock.unlock();
        }
    }
    
    // Even with 10 connections, only 1 thread works at a time!
}
```

### ✅ After: Using Semaphore for Connection Pool

```java
// SOLUTION: Up to N connections used simultaneously!
public class ConnectionPool {
    private static final int MAX_CONNECTIONS = 10;
    private final Semaphore semaphore = new Semaphore(MAX_CONNECTIONS);
    private final Queue<Connection> pool = new ConcurrentLinkedQueue<>();
    
    public ConnectionPool() {
        for (int i = 0; i < MAX_CONNECTIONS; i++) {
            pool.add(createConnection());
        }
    }
    
    public Connection getConnection() throws InterruptedException {
        semaphore.acquire();  // ✅ Up to 10 threads can pass
        return pool.poll();
    }
    
    public void releaseConnection(Connection conn) {
        pool.offer(conn);
        semaphore.release();  // ✅ Signal that a connection is available
    }
}
```

---

## Semaphore Execution Flow

```
┌─────────────────────────────────────────────────────────────────┐
│              SEMAPHORE(3) EXECUTION TIMELINE                     │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│   Permits: [🎫][🎫][🎫]  (3 available)                           │
│                                                                  │
│   TIME ─────────────────────────────────────────────────────►   │
│                                                                  │
│   T1: ──acquire()──[🎫 GOT]────────────────release()──►         │
│   T2: ──acquire()──[🎫 GOT]────────────────release()──►         │
│   T3: ──acquire()──[🎫 GOT]──────release()──────────────►       │
│   T4: ──acquire()──[BLOCKED ⏳]──[🎫 GOT]──release()───►        │
│                                  ↑                               │
│                                  │                               │
│                          T3 released, T4 wakes up                │
│                                                                  │
│   Permits over time:                                             │
│   ┌───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┐            │
│   │ 3 │ 2 │ 1 │ 0 │ 0 │ 0 │ 1 │ 0 │ 1 │ 2 │ 3 │   │            │
│   └───┴───┴───┴───┴───┴───┴───┴───┴───┴───┴───┴───┘            │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

---

## Real-World Use Cases

### 1. Rate Limiting (API Throttling)

```
┌─────────────────────────────────────────────────────────────────┐
│                    RATE LIMITER                                  │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│   Limit: 5 requests per second                                   │
│                                                                  │
│   Semaphore permits = 5                                          │
│                                                                  │
│   Incoming Requests:                                             │
│   ─────────────────                                             │
│   Req 1 ──► acquire() ──► ✅ Process                             │
│   Req 2 ──► acquire() ──► ✅ Process                             │
│   Req 3 ──► acquire() ──► ✅ Process                             │
│   Req 4 ──► acquire() ──► ✅ Process                             │
│   Req 5 ──► acquire() ──► ✅ Process                             │
│   Req 6 ──► acquire() ──► ⏳ WAIT (rate limited!)               │
│                                                                  │
│   After 1 second, permits refill → Req 6 proceeds                │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

### 2. Database Connection Pooling

```
┌─────────────────────────────────────────────────────────────────┐
│                 CONNECTION POOL (10 connections)                 │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│   ┌─────────────────────────────────────────────────────────┐   │
│   │                   APPLICATION                            │   │
│   │                                                          │   │
│   │   Web Request 1 ─┐                                       │   │
│   │   Web Request 2 ─┤                                       │   │
│   │   Web Request 3 ─┼── Semaphore(10) ──► Connection Pool  │   │
│   │        ...       │                                       │   │
│   │   Web Request N ─┘                                       │   │
│   │                                                          │   │
│   │   ✅ Up to 10 requests get connections instantly         │   │
│   │   ⏳ Request 11+ waits until connection freed            │   │
│   │                                                          │   │
│   └─────────────────────────────────────────────────────────┘   │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

### 3. Thread Pool / Worker Limit

```java
// Limit concurrent file processing to 3 threads
Semaphore fileSemaphore = new Semaphore(3);

public void processFile(File file) throws InterruptedException {
    fileSemaphore.acquire();  // Only 3 files processed concurrently
    try {
        // Heavy file processing...
    } finally {
        fileSemaphore.release();
    }
}
```

---

## When to Use Semaphores

### ✅ Good Use Cases

| Scenario | Why Semaphore Works |
|----------|---------------------|
| **Connection pooling** | Limit concurrent DB connections |
| **Rate limiting** | Control requests per second |
| **Resource pooling** | Limited printers, licenses, threads |
| **Producer-Consumer** | Bounded buffer coordination |
| **Parking lot** | Limit cars that can enter |

### ❌ When NOT to Use

| Scenario | Why NOT | Use Instead |
|----------|---------|-------------|
| **Simple mutual exclusion** | Semaphore(1) works but overkill | `synchronized` or `ReentrantLock` |
| **Read-write scenarios** | Need separate read/write control | `ReadWriteLock` |
| **Condition-based waiting** | Semaphore doesn't support conditions | `Condition` with Lock |
| **Single resource ownership** | Semaphore doesn't track owner | `ReentrantLock` |

---

## Semaphore vs Mutex: Key Differences

```
┌─────────────────────────────────────────────────────────────────┐
│            SEMAPHORE vs MUTEX COMPARISON                         │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│   MUTEX (Lock)                     SEMAPHORE                     │
│   ────────────                     ─────────                    │
│                                                                  │
│   • 1 permit only                  • N permits (configurable)   │
│   • Owner must release             • ANY thread can release     │
│   • Reentrant possible             • Not reentrant              │
│   • For mutual exclusion           • For limiting concurrency   │
│                                                                  │
│   Use when: "Only one at a time"   Use when: "Up to N at a time"│
│                                                                  │
│   Example:                         Example:                      │
│   Writing to file                  Connection pool               │
│   Modifying shared variable        Rate limiter                  │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

---

## Common Gotchas

### 1. Forgetting to Release in Finally Block

```java
// ❌ BAD: If exception occurs, permit is never released
public void doWork() throws InterruptedException {
    semaphore.acquire();
    riskyOperation();  // If this throws, acquire is never released!
    semaphore.release();
}

// ✅ GOOD: Always release in finally
public void doWork() throws InterruptedException {
    semaphore.acquire();
    try {
        riskyOperation();
    } finally {
        semaphore.release();  // Always releases, even on exception
    }
}
```

### 2. Releasing Without Acquiring

```java
// ❌ DANGER: Semaphore doesn't track who acquired!
Semaphore sem = new Semaphore(3);

// Thread A
sem.release();  // Permits = 4! (More than initial!)
sem.release();  // Permits = 5! 💀 Permit count grows unbounded!
```

### 3. Deadlock with Multiple Semaphores

```java
// ❌ POTENTIAL DEADLOCK
Semaphore semA = new Semaphore(1);
Semaphore semB = new Semaphore(1);

// Thread 1              // Thread 2
semA.acquire();          semB.acquire();
semB.acquire(); // WAIT  semA.acquire(); // WAIT
// DEADLOCK!
```

### 4. Not Using Fair Semaphore When Needed

```java
// Unfair semaphore (default): Threads may starve
Semaphore unfair = new Semaphore(1);  // ❌ Newer threads might get permit first

// Fair semaphore: FIFO order guaranteed
Semaphore fair = new Semaphore(1, true);  // ✅ Threads served in order
```

---

## Binary Semaphore vs Mutex

```
┌─────────────────────────────────────────────────────────────────┐
│           BINARY SEMAPHORE(1) vs REENTRANTLOCK                   │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│   Binary Semaphore(1)              ReentrantLock                 │
│   ───────────────────              ──────────────               │
│                                                                  │
│   • NOT reentrant                  • IS reentrant                │
│     (same thread acquires twice     (same thread can lock       │
│      = DEADLOCK!)                   multiple times)             │
│                                                                  │
│   • No owner tracking              • Tracks owner thread        │
│     (Thread B can release           (Only owner can unlock)     │
│      what Thread A acquired)                                    │
│                                                                  │
│   • For signaling                  • For mutual exclusion       │
│                                                                  │
│   Semaphore sem = new Semaphore(1);                              │
│   sem.acquire();                                                 │
│   sem.acquire();  // ❌ DEADLOCK - waits forever!               │
│                                                                  │
│   ReentrantLock lock = new ReentrantLock();                      │
│   lock.lock();                                                   │
│   lock.lock();    // ✅ OK - same thread, increments count       │
│   lock.unlock();  // Hold count: 1                               │
│   lock.unlock();  // Hold count: 0, released                     │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

---

## Summary

| Concept | Key Point |
|---------|-----------|
| **Semaphore** | Counter-based permit system for N concurrent threads |
| **acquire()** | Decrements permit, blocks if zero |
| **release()** | Increments permit, wakes waiting thread |
| **tryAcquire()** | Non-blocking permit check |
| **Fair Semaphore** | FIFO ordering prevents starvation |
| **Use Case** | Connection pools, rate limiters, resource bounds |

### Decision Flowchart

```
                       ┌────────────────────────────────┐
                       │ How many threads can access    │
                       │ the resource simultaneously?   │
                       └───────────────┬────────────────┘
                                       │
               ┌───────────────────────┴───────────────────────┐
               │                                               │
               ▼                                               ▼
         EXACTLY 1                                        MORE THAN 1
               │                                               │
               ▼                                               ▼
    ┌──────────────────┐                            ┌─────────────────┐
    │ Use synchronized │                            │ Use Semaphore(N)│
    │ or ReentrantLock │                            │                 │
    └──────────────────┘                            └─────────────────┘
```

---

## Next Chapter
→ [c12: Producer-Consumer Problem](../c12_producer_consumer/ProducerConsumerNotes.md) — Applying semaphores to the classic bounded buffer problem
