# Wait/Notify in Java

## Prerequisites
- [c11: Semaphores](../c11_semaphores/SemaphoresNotes.md) — Permit-based synchronization
- [c12: Producer-Consumer](../c12_producer_consumer/ProducerConsumerNotes.md) — Bounded buffer pattern
- [c7: Synchronized Keyword](../c7_synchronized_keyword/SynchronizedNotes.md) — Intrinsic locks

---

## What You'll Learn

| Question | Answer |
|----------|--------|
| **WHAT** | wait/notify = mechanism for threads to communicate and coordinate |
| **WHY** | To let threads wait for specific conditions and signal each other |
| **WHEN** | Producer-Consumer, conditional waiting, thread coordination |
| **HOW** | Object's wait(), notify(), notifyAll() methods |

---

## Why Wait/Notify Exists

### The Problem: Busy Waiting (Polling)

```
┌─────────────────────────────────────────────────────────────────┐
│                    THE BUSY WAITING PROBLEM                      │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│   SCENARIO: Consumer waiting for producer to add items          │
│                                                                  │
│   ❌ BAD: Busy Waiting (Polling)                                 │
│   ─────────────────────────────                                 │
│                                                                  │
│   while (buffer.isEmpty()) {                                     │
│       // Check... empty                                          │
│       // Check... empty                                          │
│       // Check... empty                                          │
│       // Check... empty   ← CPU at 100% doing NOTHING useful!   │
│       // Check... got it!                                        │
│   }                                                              │
│                                                                  │
│   💀 PROBLEMS:                                                   │
│   • Wastes CPU cycles (100% usage)                               │
│   • Drains battery (mobile devices)                              │
│   • Steals CPU from producer (makes problem worse!)             │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

### The Solution: Wait/Notify

```
┌─────────────────────────────────────────────────────────────────┐
│                    THE WAIT/NOTIFY SOLUTION                      │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│   ✅ GOOD: Wait and be notified                                  │
│   ─────────────────────────────                                 │
│                                                                  │
│   Consumer:                         Producer:                    │
│   ─────────                         ─────────                   │
│   synchronized (buffer) {           synchronized (buffer) {      │
│       while (buffer.isEmpty()) {        buffer.add(item);        │
│           buffer.wait();  ← SLEEP       buffer.notify(); ← WAKE │
│       }                             }                            │
│       item = buffer.take();                                      │
│   }                                                              │
│                                                                  │
│   ✅ BENEFITS:                                                   │
│   • CPU is FREE while waiting (0% usage)                         │
│   • Thread wakes exactly when needed                             │
│   • Producer gets full CPU time                                  │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

---

## The Wait/Notify API

### Core Methods (from Object class)

```
┌─────────────────────────────────────────────────────────────────┐
│                    WAIT/NOTIFY METHODS                           │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│   wait()                                                         │
│   ──────                                                        │
│   • Releases the lock on the object                             │
│   • Puts thread to SLEEP (WAITING state)                        │
│   • Thread stays asleep until notified                          │
│   • After notify, thread re-acquires lock before continuing     │
│   • Must be called inside synchronized block!                   │
│                                                                  │
│   notify()                                                       │
│   ────────                                                      │
│   • Wakes up ONE waiting thread (random choice)                 │
│   • Does NOT release the lock immediately                       │
│   • Woken thread waits to re-acquire lock                       │
│   • If no threads waiting, does nothing                         │
│                                                                  │
│   notifyAll()                                                    │
│   ───────────                                                   │
│   • Wakes up ALL waiting threads                                │
│   • All threads compete to re-acquire lock                      │
│   • Only ONE will get it, others wait again                     │
│   • Safer than notify() when multiple conditions                │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

### Method Comparison

| Method | Wakes | Use When |
|--------|-------|----------|
| `notify()` | 1 random thread | Single waiter or all wait for same condition |
| `notifyAll()` | All threads | Multiple waiters with different conditions |

---

## Wait/Notify Execution Flow

```
┌─────────────────────────────────────────────────────────────────┐
│                    WAIT/NOTIFY TIMELINE                          │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│   CONSUMER                              PRODUCER                 │
│   ────────                              ────────                │
│                                                                  │
│   1. synchronized(buffer)                                        │
│      → HOLDS LOCK                                                │
│                                                                  │
│   2. while(isEmpty)                                              │
│      buffer.wait()                                               │
│      → RELEASES LOCK                    3. synchronized(buffer)  │
│      → GOES TO SLEEP 😴                    → Gets lock (free!)  │
│                                                                  │
│                                         4. buffer.add(item)      │
│                                                                  │
│                                         5. buffer.notify()       │
│                                            → Consumer wakes ⏰   │
│                                                                  │
│                                         6. Exits synchronized    │
│                                            → RELEASES LOCK       │
│                                                                  │
│   7. Re-acquires lock                                            │
│      → HOLDS LOCK again                                          │
│                                                                  │
│   8. while(isEmpty) – check again                                │
│      → Not empty! Continue                                       │
│                                                                  │
│   9. buffer.take()                                               │
│                                                                  │
│   10. Exits synchronized                                         │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

---

## The Standard Wait Pattern

### ⚠️ CRITICAL: Always Use While Loop!

```java
// ✅ CORRECT Pattern (with while loop)
synchronized (lock) {
    while (!condition) {  // MUST be while, not if!
        lock.wait();
    }
    // Condition is now true, proceed
}

// ❌ WRONG Pattern (with if)
synchronized (lock) {
    if (!condition) {  // DANGER!
        lock.wait();
    }
    // Condition might be FALSE here!
}
```

### Why While Loop is Essential

```
┌─────────────────────────────────────────────────────────────────┐
│                    WHY WHILE, NOT IF?                            │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│   PROBLEM 1: Spurious Wakeups                                    │
│   ───────────────────────────                                   │
│   JVM may wake up threads for no reason (implementation detail) │
│   While loop re-checks condition after each wakeup              │
│                                                                  │
│   PROBLEM 2: Race Condition with Multiple Consumers             │
│   ─────────────────────────────────────────────────             │
│                                                                  │
│   Producer: notifyAll() wakes Consumer A and B                   │
│                                                                  │
│   Consumer A:                       Consumer B:                  │
│   ───────────                       ───────────                 │
│   Re-acquires lock ✅                                            │
│   Takes the item                                                 │
│   Releases lock                                                  │
│                                     Re-acquires lock ✅          │
│                                     Buffer is EMPTY now!        │
│                                     Must wait() again!          │
│                                                                  │
│   If using 'if' instead of 'while':                              │
│   Consumer B would crash trying to take from empty buffer!      │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

---

## Producer-Consumer with Wait/Notify

```java
public class WaitNotifyBuffer<T> {
    private final Queue<T> buffer = new LinkedList<>();
    private final int capacity;
    
    public WaitNotifyBuffer(int capacity) {
        this.capacity = capacity;
    }
    
    public synchronized void put(T item) throws InterruptedException {
        // Wait while buffer is full
        while (buffer.size() >= capacity) {
            wait();  // Release lock, sleep until notified
        }
        
        buffer.add(item);
        
        // Wake up waiting consumers
        notifyAll();
    }
    
    public synchronized T take() throws InterruptedException {
        // Wait while buffer is empty
        while (buffer.isEmpty()) {
            wait();  // Release lock, sleep until notified
        }
        
        T item = buffer.poll();
        
        // Wake up waiting producers
        notifyAll();
        
        return item;
    }
}
```

---

## Semaphores vs Wait/Notify

```
┌─────────────────────────────────────────────────────────────────┐
│              SEMAPHORES vs WAIT/NOTIFY                           │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│   SEMAPHORE                          WAIT/NOTIFY                 │
│   ─────────                          ──────────                 │
│                                                                  │
│   • Count-based (permits)            • Condition-based           │
│   • acquire()/release()              • wait()/notify()           │
│   • No lock required                 • Must hold lock            │
│   • Separate from mutex              • Built into every Object   │
│                                                                  │
│   WHEN TO USE SEMAPHORE:                                         │
│   ─────────────────────                                         │
│   • Limiting concurrent access (N threads)                       │
│   • Resource pooling (connections, permits)                      │
│   • Signaling between unrelated threads                          │
│                                                                  │
│   WHEN TO USE WAIT/NOTIFY:                                       │
│   ────────────────────────                                      │
│   • Waiting for specific condition                               │
│   • Producer-Consumer with existing synchronized                 │
│   • Complex state-based coordination                             │
│                                                                  │
│   PRODUCER-CONSUMER COMPARISON:                                  │
│   ─────────────────────────────                                 │
│   Semaphore: 3 semaphores (mutex, empty, full)                  │
│   Wait/Notify: 1 synchronized block with while loops            │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

---

## Common Gotchas

### 1. Forgetting to Synchronize

```java
// ❌ IllegalMonitorStateException!
buffer.wait();  // Not inside synchronized block

// ✅ Correct
synchronized (buffer) {
    buffer.wait();
}
```

### 2. Using notify() Instead of notifyAll()

```java
// ❌ RISKY: May wake wrong thread
synchronized (buffer) {
    buffer.add(item);
    buffer.notify();  // What if it wakes a producer?
}

// ✅ SAFER: Wake everyone, let them re-check
synchronized (buffer) {
    buffer.add(item);
    buffer.notifyAll();  // All wake, correct one proceeds
}
```

### 3. Using if Instead of while

```java
// ❌ DANGEROUS
synchronized (buffer) {
    if (buffer.isEmpty()) {
        buffer.wait();
    }
    // Spurious wakeup or race condition = BUG!
    buffer.take();
}

// ✅ SAFE
synchronized (buffer) {
    while (buffer.isEmpty()) {
        buffer.wait();
    }
    buffer.take();
}
```

### 4. Holding Lock Too Long

```java
// ❌ BAD: Long operation while holding lock
synchronized (buffer) {
    while (buffer.isEmpty()) {
        buffer.wait();
    }
    T item = buffer.take();
    processItem(item);  // Takes 10 seconds! 💀
}

// ✅ GOOD: Process outside synchronized
T item;
synchronized (buffer) {
    while (buffer.isEmpty()) {
        buffer.wait();
    }
    item = buffer.take();
}
processItem(item);  // Lock released, others can proceed
```

---

## Modern Alternatives

### Condition Variables (from Lock API)

```java
import java.util.concurrent.locks.*;

Lock lock = new ReentrantLock();
Condition notEmpty = lock.newCondition();
Condition notFull = lock.newCondition();

// Producer
lock.lock();
try {
    while (isFull()) {
        notFull.await();  // Like wait()
    }
    buffer.add(item);
    notEmpty.signal();    // Like notify()
} finally {
    lock.unlock();
}
```

### Why Use Condition?

| Feature | wait/notify | Condition |
|---------|-------------|-----------|
| Multiple conditions per lock | ❌ | ✅ |
| Interruptible wait | ✅ | ✅ |
| Timed wait | ✅ | ✅ |
| Fair queuing | ❌ | ✅ (with fair lock) |

---

## Summary

| Concept | Key Point |
|---------|-----------|
| **wait()** | Release lock, sleep until notified |
| **notify()** | Wake ONE random waiting thread |
| **notifyAll()** | Wake ALL waiting threads (safer) |
| **While Loop** | ALWAYS use while, never if (spurious wakeups) |
| **Must Synchronize** | wait/notify must be inside synchronized |

### Decision Flowchart

```
                ┌─────────────────────────────────┐
                │  Need thread coordination?      │
                └───────────────┬─────────────────┘
                                │
               ┌────────────────┴────────────────┐
               │                                 │
               ▼                                 ▼
    Count-based (permits)?            Condition-based?
               │                                 │
               ▼                                 ▼
       ┌───────────────┐                ┌───────────────────┐
       │  SEMAPHORE    │                │  Need multiple     │
       │               │                │  conditions?       │
       └───────────────┘                └────────┬──────────┘
                                                 │
                                    ┌────────────┴────────────┐
                                    │                         │
                                    ▼                         ▼
                                   YES                       NO
                                    │                         │
                                    ▼                         ▼
                            ┌─────────────┐          ┌─────────────┐
                            │  Condition  │          │ wait/notify │
                            │  variables  │          │             │
                            └─────────────┘          └─────────────┘
```

---

## Practical Comparison: Same Problem, Two Solutions

### Semaphore Approach (from c12)
```java
public void put(T item) throws InterruptedException {
    empty.acquire();     // Wait for empty slot
    mutex.acquire();     // Enter critical section
    buffer.add(item);
    mutex.release();     // Exit critical section
    full.release();      // Signal item available
}
```

### Wait/Notify Approach
```java
public synchronized void put(T item) throws InterruptedException {
    while (buffer.size() >= capacity) {
        wait();          // Wait for space
    }
    buffer.add(item);
    notifyAll();         // Signal item available
}
```

Both are correct! Choose based on your needs and existing code style.

---

## End of Concurrency Module

🎉 **Congratulations!** You've completed the core concurrency concepts:
- c1-c4: Threads, Executors, Callables
- c5-c9: Synchronization, Locks, Atomic, Volatile
- c10: Concurrent Collections
- **c11: Semaphores**
- **c12: Producer-Consumer**
- **c13: Deadlocks**
- **c14: Wait/Notify**

Happy concurrent programming! 🚀
