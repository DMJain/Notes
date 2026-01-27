# Producer-Consumer Problem in Java

## Prerequisites
- [c11: Semaphores](../c11_semaphores/SemaphoresNotes.md) — Permit-based synchronization
- [c7: Synchronized Keyword](../c7_synchronized_keyword/SynchronizedNotes.md) — Intrinsic locks
- [c10: Concurrent Collections](../c10_concurrent_collections/ConcurrentCollectionsNotes.md) — Thread-safe data structures

---

## What You'll Learn

| Question | Answer |
|----------|--------|
| **WHAT** | Producer-Consumer = coordination pattern between data producers and consumers |
| **WHY** | To safely share data between threads without corruption or deadlock |
| **WHEN** | Queues, message passing, work distribution, streaming data |
| **HOW** | Using semaphores, wait/notify, or blocking queues |

---

## Why Does This Problem Exist?

### The Classic Scenario: T-Shirt Store

```
┌─────────────────────────────────────────────────────────────────┐
│                    THE T-SHIRT STORE PROBLEM                     │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│   ┌─────────────────────────────────────────────────────────┐   │
│   │                    T-SHIRT STORE                         │   │
│   │                                                          │   │
│   │   PRODUCER                  BUFFER                       │   │
│   │   (Factory)                 (Store Shelf)     CONSUMER   │   │
│   │                                               (Customer)  │   │
│   │     👕 ──────►         ┌───┬───┬───┬───┬───┐              │   │
│   │     Make               │ 👕│ 👕│ 👕│   │   │  ──────► 🛒 │   │
│   │     T-Shirt            └───┴───┴───┴───┴───┘     Buy     │   │
│   │                         Capacity: 5              T-Shirt │   │
│   │                                                          │   │
│   └─────────────────────────────────────────────────────────┘   │
│                                                                  │
│   PROBLEMS:                                                      │
│   ─────────                                                     │
│   1. What if producer makes too fast? (OVERFLOW 💥)             │
│   2. What if consumer buys too fast? (UNDERFLOW 💥)             │
│   3. What if both access buffer at same time? (CORRUPTION 💥)   │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

### The Three Core Challenges

```
┌─────────────────────────────────────────────────────────────────┐
│              THREE SYNCHRONIZATION CHALLENGES                    │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│   CHALLENGE 1: BUFFER OVERFLOW                                   │
│   ──────────────────────────────                                │
│   Producer makes T-shirts faster than consumers buy             │
│   ┌───┬───┬───┬───┬───┐                                        │
│   │ 👕│ 👕│ 👕│ 👕│ 👕│ ← FULL! Producer tries to add more 💥   │
│   └───┴───┴───┴───┴───┘                                        │
│   SOLUTION: Producer must WAIT when buffer is full              │
│                                                                  │
│   CHALLENGE 2: BUFFER UNDERFLOW                                  │
│   ───────────────────────────                                   │
│   Consumer wants T-shirt but none available                     │
│   ┌───┬───┬───┬───┬───┐                                        │
│   │   │   │   │   │   │ ← EMPTY! Consumer tries to take 💥     │
│   └───┴───┴───┴───┴───┘                                        │
│   SOLUTION: Consumer must WAIT when buffer is empty             │
│                                                                  │
│   CHALLENGE 3: RACE CONDITION                                    │
│   ───────────────────────                                       │
│   Producer and Consumer access buffer simultaneously            │
│                                                                  │
│   Producer: "I'll add at index 3"    ─┐                         │
│   Consumer: "I'll take from index 3"  ├─► DATA CORRUPTION! 💥   │
│   (Both happen at same time)         ─┘                         │
│   SOLUTION: Mutual exclusion (only one at a time)               │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

---

## The Semaphore Solution

### Three Semaphores Strategy

```
┌─────────────────────────────────────────────────────────────────┐
│              THREE SEMAPHORES SOLUTION                           │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│   Semaphore #1: MUTEX (1 permit)                                 │
│   ──────────────────────────────                                │
│   Purpose: Protects buffer access (only 1 thread at a time)     │
│                                                                  │
│   Semaphore #2: EMPTY (N permits, where N = buffer capacity)     │
│   ────────────────────────────────────────────────────────      │
│   Purpose: Tracks available SLOTS (for producer)                │
│   Initial: N (all slots empty)                                   │
│   Producer: acquire() before adding (decrements empty slots)    │
│   Consumer: release() after taking (increments empty slots)     │
│                                                                  │
│   Semaphore #3: FULL (0 permits initially)                       │
│   ────────────────────────────────────                          │
│   Purpose: Tracks available ITEMS (for consumer)                │
│   Initial: 0 (no items)                                          │
│   Producer: release() after adding (increments available items) │
│   Consumer: acquire() before taking (decrements available items)│
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

### Visual Execution Flow

```
┌─────────────────────────────────────────────────────────────────┐
│          PRODUCER-CONSUMER EXECUTION TIMELINE                    │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│   Buffer Capacity: 3                                             │
│   Initial:  empty=3, full=0, mutex=1                             │
│                                                                  │
│   PRODUCER                             CONSUMER                  │
│   ────────                             ────────                 │
│                                                                  │
│   1. empty.acquire()                                             │
│      (empty: 3→2)                                                │
│   2. mutex.acquire()                                             │
│   3. add(item)                         4. full.acquire() BLOCK!  │
│      [👕│  │  ]                           (full=0, must wait)    │
│   4. mutex.release()                                             │
│   5. full.release()                    5. full.acquire() UNBLOCK!│
│      (full: 0→1)                          (full: 1→0)            │
│                                        6. mutex.acquire()        │
│   6. empty.acquire()                   7. take(item)             │
│      (empty: 2→1)                         [  │  │  ]             │
│   7. mutex.acquire()                   8. mutex.release()        │
│                                        9. empty.release()        │
│      ... continues ...                    (empty: 1→2)           │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

### The Algorithm

```
┌─────────────────────────────────────────────────────────────────┐
│                    PRODUCER ALGORITHM                            │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│   while (true) {                                                 │
│       item = produce();                                          │
│                                                                  │
│       empty.acquire();     // Wait for empty slot                │
│       mutex.acquire();     // Enter critical section             │
│                                                                  │
│       buffer.add(item);    // ← CRITICAL SECTION                 │
│                                                                  │
│       mutex.release();     // Exit critical section              │
│       full.release();      // Signal item is available           │
│   }                                                              │
│                                                                  │
├─────────────────────────────────────────────────────────────────┤
│                    CONSUMER ALGORITHM                            │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│   while (true) {                                                 │
│       full.acquire();      // Wait for available item            │
│       mutex.acquire();     // Enter critical section             │
│                                                                  │
│       item = buffer.take(); // ← CRITICAL SECTION                │
│                                                                  │
│       mutex.release();     // Exit critical section              │
│       empty.release();     // Signal slot is available           │
│                                                                  │
│       consume(item);                                             │
│   }                                                              │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

---

## Three Implementation Approaches

### Comparison Table

| Approach | Complexity | Thread-Safety | When to Use |
|----------|------------|---------------|-------------|
| **Semaphores** | Medium | Manual | Learning, fine control |
| **Concurrent Queue + synchronized** | Low | Built-in | Quick implementation |
| **BlockingQueue** | Lowest | Built-in | Production code |

---

## Approach 1: Semaphore-Based Implementation

```java
public class SemaphoreBuffer<T> implements IBuffer<T> {
    private final Queue<T> buffer;
    private final int capacity;
    
    private final Semaphore mutex;  // Mutual exclusion
    private final Semaphore empty;  // Counts empty slots
    private final Semaphore full;   // Counts filled slots
    
    public SemaphoreBuffer(int capacity) {
        this.capacity = capacity;
        this.buffer = new LinkedList<>();
        this.mutex = new Semaphore(1);       // Binary semaphore
        this.empty = new Semaphore(capacity); // Empty slots
        this.full = new Semaphore(0);         // Full slots (none initially)
    }
    
    @Override
    public void put(T item) throws InterruptedException {
        empty.acquire();     // Wait for empty slot
        mutex.acquire();     // Enter critical section
        try {
            buffer.add(item);
        } finally {
            mutex.release(); // Exit critical section
        }
        full.release();      // Signal item available
    }
    
    @Override
    public T take() throws InterruptedException {
        full.acquire();      // Wait for available item
        mutex.acquire();     // Enter critical section
        T item;
        try {
            item = buffer.poll();
        } finally {
            mutex.release(); // Exit critical section
        }
        empty.release();     // Signal slot available
        return item;
    }
}
```

---

## Approach 2: Concurrent Queue + Synchronized

```java
public class SynchronizedBuffer<T> implements IBuffer<T> {
    private final Queue<T> buffer = new ConcurrentLinkedQueue<>();
    private final int capacity;
    
    public SynchronizedBuffer(int capacity) {
        this.capacity = capacity;
    }
    
    @Override
    public void put(T item) throws InterruptedException {
        synchronized (buffer) {
            // Wait while buffer is full
            while (buffer.size() >= capacity) {
                buffer.wait();
            }
            buffer.add(item);
            buffer.notifyAll();  // Wake up waiting consumers
        }
    }
    
    @Override
    public T take() throws InterruptedException {
        synchronized (buffer) {
            // Wait while buffer is empty
            while (buffer.isEmpty()) {
                buffer.wait();
            }
            T item = buffer.poll();
            buffer.notifyAll();  // Wake up waiting producers
            return item;
        }
    }
}
```

---

## When to Use Each Approach

```
┌─────────────────────────────────────────────────────────────────┐
│                 DECISION FLOWCHART                               │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│                     ┌────────────────────────┐                  │
│                     │ Need Producer-Consumer? │                  │
│                     └───────────┬────────────┘                  │
│                                 │                                │
│                                 ▼                                │
│                     ┌────────────────────────┐                  │
│                     │ Is this production code?│                  │
│                     └───────────┬────────────┘                  │
│                                 │                                │
│                ┌────────────────┼────────────────┐              │
│                ▼                │                ▼              │
│               YES               │               NO              │
│                │                │                │              │
│                ▼                │                ▼              │
│    ┌───────────────────┐       │     ┌──────────────────────┐  │
│    │ Use BlockingQueue │       │     │ Learning/Demo?       │  │
│    │ (ArrayBlocking,   │       │     └─────────┬────────────┘  │
│    │  LinkedBlocking)  │       │               │               │
│    └───────────────────┘       │       ┌───────┴───────┐       │
│                                │       ▼               ▼       │
│                                │   Semaphores    synchronized   │
│                                │   (fine control) (simple)      │
│                                │                                │
└─────────────────────────────────────────────────────────────────┘
```

---

## Real-World Applications

### 1. Message Queue System

```
┌─────────────────────────────────────────────────────────────────┐
│                    MESSAGE QUEUE (Kafka-like)                    │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│   PRODUCERS                    BUFFER                CONSUMERS  │
│   (Web Servers)                (Topic)               (Workers)  │
│                                                                  │
│   Server 1 ──┐            ┌───────────────────┐                 │
│   Server 2 ──┼──────►     │  Message Queue    │ ──────► Worker 1│
│   Server 3 ──┘            │  [msg1][msg2]...  │ ──────► Worker 2│
│                           └───────────────────┘ ──────► Worker 3│
│                                                                  │
│   Use Case: Decoupling services, async processing               │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

### 2. Thread Pool Work Queue

```
┌─────────────────────────────────────────────────────────────────┐
│                    THREAD POOL EXECUTOR                          │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│   PRODUCERS                    QUEUE                  CONSUMERS │
│   (Task submitters)           (Work Queue)           (Threads)  │
│                                                                  │
│   submit(task1) ──┐        ┌─────────────────┐                  │
│   submit(task2) ──┼──────► │ BlockingQueue   │ ──────► Thread-1 │
│   submit(task3) ──┘        │ [t1][t2][t3]... │ ──────► Thread-2 │
│                            └─────────────────┘ ──────► Thread-3 │
│                                                                  │
│   This is EXACTLY how ExecutorService works internally!         │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

### 3. Logging Framework

```
┌─────────────────────────────────────────────────────────────────┐
│                    ASYNC LOGGING                                 │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│   PRODUCERS                    BUFFER               CONSUMER    │
│   (All app threads)            (Log buffer)         (Log writer)│
│                                                                  │
│   Thread-1: log("msg1") ──┐  ┌──────────────┐                   │
│   Thread-2: log("msg2") ──┼─►│ Ring Buffer  │──► Single writer  │
│   Thread-3: log("msg3") ──┘  │ [m1][m2][m3] │    thread to disk │
│                              └──────────────┘                   │
│                                                                  │
│   Why? Writing to disk is SLOW. Don't block app threads!        │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

---

## When to Use Producer-Consumer

### ✅ Good Use Cases

| Scenario | Why It Helps |
|----------|--------------|
| **Async task processing** | Decouple task creation from execution |
| **Message queuing** | Buffer bursts, smooth out load |
| **Logging** | Don't block app threads on I/O |
| **Data pipelines** | Stage-by-stage processing |
| **Rate limiting** | Control consumption speed |

### ❌ When NOT to Use

| Scenario | Why NOT | Use Instead |
|----------|---------|-------------|
| **Synchronous request/response** | Need immediate result | Direct method call |
| **Single-threaded app** | No concurrency benefit | Simple data structure |
| **Real-time constraints** | Queue adds latency | Direct processing |
| **Tiny buffer** | Overhead > benefit | Simple lock |

---

## Common Gotchas

### 1. Forgetting to Use While Loop for Conditions

```java
// ❌ BAD: Using 'if' instead of 'while'
synchronized (buffer) {
    if (buffer.isEmpty()) {  // WRONG!
        buffer.wait();
    }
    // After wakeup, buffer might be empty again!
    // (Another thread took the item - spurious wakeup)
}

// ✅ GOOD: Using 'while' loop
synchronized (buffer) {
    while (buffer.isEmpty()) {  // CORRECT!
        buffer.wait();
    }
    // Re-checks condition after wakeup
}
```

### 2. Semaphore Acquire/Release Order

```java
// ❌ BAD: Wrong order causes deadlock
mutex.acquire();
empty.acquire();  // If empty=0, we hold mutex and wait forever!

// ✅ GOOD: Acquire semaphores in correct order
empty.acquire();  // Wait for slot BEFORE locking
mutex.acquire();  // Then lock the buffer
```

### 3. Not Releasing Mutex on Exception

```java
// ❌ BAD: Mutex never released if exception occurs
mutex.acquire();
riskyOperation();  // Throws exception!
mutex.release();   // Never executed 💀

// ✅ GOOD: Always release in finally
mutex.acquire();
try {
    riskyOperation();
} finally {
    mutex.release();
}
```

---

## Summary

| Concept | Key Point |
|---------|-----------|
| **Producer-Consumer** | Pattern for safe data sharing between threads |
| **Three Semaphores** | mutex (protection) + empty (slots) + full (items) |
| **Bounded Buffer** | Fixed-size queue with blocking on full/empty |
| **BlockingQueue** | Java's built-in solution for production use |

### The Pattern in One Diagram

```
┌─────────────────────────────────────────────────────────────────┐
│                                                                  │
│   PRODUCER                                      CONSUMER         │
│       │                                             │            │
│       ▼                                             ▼            │
│   ╔═══════════╗                              ╔═══════════╗      │
│   ║ produce() ║                              ║  consume()║      │
│   ╚═════╤═════╝                              ╚═════╤═════╝      │
│         │                                          │            │
│         ▼                                          ▼            │
│   ┌───────────┐     ┌─────────────────┐     ┌───────────┐      │
│   │  put()    │────►│ BOUNDED BUFFER  │◄────│  take()   │      │
│   │ (blocks   │     │ ┌───┬───┬───┐   │     │ (blocks   │      │
│   │  if full) │     │ │ • │ • │   │   │     │  if empty)│      │
│   └───────────┘     │ └───┴───┴───┘   │     └───────────┘      │
│                     └─────────────────┘                         │
│                      Protected by:                               │
│                      • Mutex (access)                            │
│                      • Empty semaphore                           │
│                      • Full semaphore                            │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

---

## Next Chapter
→ [c13: Deadlocks](../c13_deadlocks/DeadlocksNotes.md) — What happens when semaphores go wrong
