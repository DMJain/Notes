# Deadlocks in Java

## Prerequisites
- [c11: Semaphores](../c11_semaphores/SemaphoresNotes.md) — Permit-based synchronization
- [c6: Mutex Locks](../c6_mutex_locks/MutexLocksNotes.md) — ReentrantLock basics
- [c7: Synchronized Keyword](../c7_synchronized_keyword/SynchronizedNotes.md) — Intrinsic locks

---

## What You'll Learn

| Question | Answer |
|----------|--------|
| **WHAT** | Deadlock = two or more threads stuck forever, each waiting for the other |
| **WHY** | Because threads hold locks while waiting for other locks |
| **WHEN** | When all 4 Coffman conditions are met simultaneously |
| **HOW** | Prevention, avoidance, detection, or timeouts |

---

## What is a Deadlock?

A **deadlock** is a situation where two or more threads are blocked forever, waiting for each other.

```
┌─────────────────────────────────────────────────────────────────┐
│                    DEADLOCK VISUALIZATION                        │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│                     THE DEADLY EMBRACE                           │
│                                                                  │
│        Thread A                          Thread B                │
│       ┌─────────┐                       ┌─────────┐             │
│       │  Holds  │                       │  Holds  │             │
│       │ Lock 1  │                       │ Lock 2  │             │
│       └────┬────┘                       └────┬────┘             │
│            │                                 │                   │
│            │  Wants Lock 2                   │  Wants Lock 1    │
│            │      ↓                          │      ↓           │
│            └────────────► ⏳ ◄───────────────┘                   │
│                           │                                      │
│                           ▼                                      │
│                     💀 DEADLOCK!                                 │
│                                                                  │
│        Thread A waits for B to release Lock 2                    │
│        Thread B waits for A to release Lock 1                    │
│        Neither can proceed → STUCK FOREVER                       │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

---

## The 4 Coffman Conditions

A deadlock can ONLY occur when ALL FOUR conditions are met simultaneously.

```
┌─────────────────────────────────────────────────────────────────┐
│                  4 COFFMAN CONDITIONS                            │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│   All 4 must be TRUE for deadlock to occur:                      │
│                                                                  │
│   ┌─────────────────────────────────────────────────────────┐   │
│   │  1. MUTUAL EXCLUSION                                     │   │
│   │     Resource can be held by only one thread at a time   │   │
│   │                                                          │   │
│   │     Thread A: ████ Lock 1 ████                          │   │
│   │     Thread B: Cannot access Lock 1 ❌                    │   │
│   └─────────────────────────────────────────────────────────┘   │
│                                                                  │
│   ┌─────────────────────────────────────────────────────────┐   │
│   │  2. HOLD AND WAIT                                        │   │
│   │     Thread holds one resource while waiting for another │   │
│   │                                                          │   │
│   │     Thread A: Holds Lock 1, waiting for Lock 2 ⏳        │   │
│   │                                                          │   │
│   └─────────────────────────────────────────────────────────┘   │
│                                                                  │
│   ┌─────────────────────────────────────────────────────────┐   │
│   │  3. NO PREEMPTION                                        │   │
│   │     Resources cannot be forcibly taken from a thread    │   │
│   │                                                          │   │
│   │     OS cannot say: "Thread A, give up Lock 1!"          │   │
│   │     Only Thread A can release Lock 1                     │   │
│   └─────────────────────────────────────────────────────────┘   │
│                                                                  │
│   ┌─────────────────────────────────────────────────────────┐   │
│   │  4. CIRCULAR WAIT                                        │   │
│   │     Cycle of threads waiting for each other             │   │
│   │                                                          │   │
│   │     A waits for B ──► B waits for C ──► C waits for A   │   │
│   │           ▲                                    │         │   │
│   │           └────────────────────────────────────┘         │   │
│   └─────────────────────────────────────────────────────────┘   │
│                                                                  │
│   💡 Break ANY ONE condition → No deadlock possible!             │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

### Summary Table

| Condition | Meaning | Example |
|-----------|---------|---------|
| **Mutual Exclusion** | Only one thread can hold resource | `synchronized` block |
| **Hold and Wait** | Hold one, wait for another | Holding Lock A, acquiring Lock B |
| **No Preemption** | Can't forcibly take resources | Only owner can `unlock()` |
| **Circular Wait** | Cycle of waiting threads | A→B→C→A |

---

## Deadlock Example: The Dining Philosophers

```
┌─────────────────────────────────────────────────────────────────┐
│                 DINING PHILOSOPHERS PROBLEM                      │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│   5 philosophers sit at a round table.                           │
│   5 chopsticks, one between each pair.                           │
│   Each philosopher needs 2 chopsticks to eat.                    │
│                                                                  │
│              🍜                                                   │
│          ┌───────┐                                               │
│      🥢 │  P1   │ 🥢                                             │
│         └───────┘                                                │
│     P5 ┌───────────┐ P2                                          │
│    🥢  │   TABLE   │  🥢                                         │
│        │           │                                             │
│     P4 └───────────┘ P3                                          │
│      🥢           🥢                                              │
│                                                                  │
│   DEADLOCK SCENARIO:                                             │
│   ─────────────────                                             │
│   All philosophers pick up their LEFT chopstick simultaneously  │
│                                                                  │
│   P1: Has 🥢(left), waits for 🥢(right) held by P2              │
│   P2: Has 🥢(left), waits for 🥢(right) held by P3              │
│   P3: Has 🥢(left), waits for 🥢(right) held by P4              │
│   P4: Has 🥢(left), waits for 🥢(right) held by P5              │
│   P5: Has 🥢(left), waits for 🥢(right) held by P1              │
│                                                                  │
│   CIRCULAR WAIT = DEADLOCK! 💀                                   │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

---

## Code Example: Creating a Deadlock

```java
public class DeadlockDemo {
    private static final Object LOCK_A = new Object();
    private static final Object LOCK_B = new Object();
    
    public static void main(String[] args) {
        Thread thread1 = new Thread(() -> {
            synchronized (LOCK_A) {
                System.out.println("Thread 1: Holding Lock A");
                sleep(100);  // Give Thread 2 time to get Lock B
                
                System.out.println("Thread 1: Waiting for Lock B");
                synchronized (LOCK_B) {
                    System.out.println("Thread 1: Holding A and B");
                }
            }
        });
        
        Thread thread2 = new Thread(() -> {
            synchronized (LOCK_B) {
                System.out.println("Thread 2: Holding Lock B");
                sleep(100);  // Give Thread 1 time to get Lock A
                
                System.out.println("Thread 2: Waiting for Lock A");
                synchronized (LOCK_A) {  // 💀 DEADLOCK HERE!
                    System.out.println("Thread 2: Holding B and A");
                }
            }
        });
        
        thread1.start();
        thread2.start();
    }
}
```

### Why Does This Deadlock?

```
┌─────────────────────────────────────────────────────────────────┐
│                    EXECUTION TIMELINE                            │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│   TIME    Thread 1                    Thread 2                   │
│   ────    ────────                    ────────                  │
│                                                                  │
│   T1      Lock A acquired ✅                                     │
│                                                                  │
│   T2                                  Lock B acquired ✅         │
│                                                                  │
│   T3      sleep(100)                  sleep(100)                 │
│                                                                  │
│   T4      Waiting for Lock B ⏳       Waiting for Lock A ⏳      │
│           (held by Thread 2)          (held by Thread 1)        │
│                                                                  │
│   T5      💀 DEADLOCK: Neither can proceed!                      │
│                                                                  │
│   ✓ Mutual Exclusion: Each lock held by one thread              │
│   ✓ Hold and Wait: Both hold one lock, wait for other           │
│   ✓ No Preemption: Neither can force-release the other's lock   │
│   ✓ Circular Wait: T1 → T2 → T1                                  │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

---

## Tackling Deadlocks: 4 Strategies

```
┌─────────────────────────────────────────────────────────────────┐
│                 DEADLOCK HANDLING STRATEGIES                     │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│   ┌───────────────────────────────────────────────────────────┐ │
│   │  1. PREVENTION                                             │ │
│   │     Design system so deadlock is IMPOSSIBLE                │ │
│   │     → Break one of the 4 conditions by design              │ │
│   └───────────────────────────────────────────────────────────┘ │
│                        │                                        │
│                        ▼                                        │
│   ┌───────────────────────────────────────────────────────────┐ │
│   │  2. AVOIDANCE                                              │ │
│   │     Make runtime decisions to AVOID unsafe states          │ │
│   │     → Banker's Algorithm (check before allocating)         │ │
│   └───────────────────────────────────────────────────────────┘ │
│                        │                                        │
│                        ▼                                        │
│   ┌───────────────────────────────────────────────────────────┐ │
│   │  3. DETECTION & RECOVERY                                   │ │
│   │     Let deadlock happen, then DETECT and RECOVER           │ │
│   │     → Kill threads, rollback transactions                  │ │
│   └───────────────────────────────────────────────────────────┘ │
│                        │                                        │
│                        ▼                                        │
│   ┌───────────────────────────────────────────────────────────┐ │
│   │  4. IGNORANCE (Ostrich Algorithm)                          │ │
│   │     Ignore the problem, reboot if it happens               │ │
│   │     → Used by Windows, Linux for rare deadlocks            │ │
│   └───────────────────────────────────────────────────────────┘ │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

---

## Prevention Strategies

### Strategy 1: Lock Ordering (Break Circular Wait)

```
┌─────────────────────────────────────────────────────────────────┐
│                    LOCK ORDERING SOLUTION                        │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│   RULE: Always acquire locks in the SAME ORDER                   │
│                                                                  │
│   ❌ DEADLOCK-PRONE:                                             │
│   ─────────────────                                             │
│   Thread 1: Lock A → Lock B                                      │
│   Thread 2: Lock B → Lock A    ← Different order!               │
│                                                                  │
│   ✅ DEADLOCK-FREE:                                              │
│   ────────────────                                              │
│   Thread 1: Lock A → Lock B                                      │
│   Thread 2: Lock A → Lock B    ← Same order!                    │
│                                                                  │
│   WHY IT WORKS:                                                  │
│   ─────────────                                                 │
│   • If Thread 2 needs B, it must first get A                    │
│   • If Thread 1 holds A, Thread 2 waits for A (not B)           │
│   • No circular wait possible!                                   │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

```java
// ✅ DEADLOCK-FREE with lock ordering
public class DeadlockFreeSolution {
    private static final Object LOCK_A = new Object();
    private static final Object LOCK_B = new Object();
    
    // Both threads acquire locks in same order: A → B
    public void method1() {
        synchronized (LOCK_A) {
            synchronized (LOCK_B) {
                // Critical section
            }
        }
    }
    
    public void method2() {
        synchronized (LOCK_A) {  // Same order as method1!
            synchronized (LOCK_B) {
                // Critical section
            }
        }
    }
}
```

### Strategy 2: Try-Lock with Timeout (Break Hold and Wait)

```java
public class TimeoutSolution {
    private final Lock lockA = new ReentrantLock();
    private final Lock lockB = new ReentrantLock();
    
    public boolean doWork() {
        try {
            // Try to acquire Lock A with timeout
            if (lockA.tryLock(1, TimeUnit.SECONDS)) {
                try {
                    // Try to acquire Lock B with timeout
                    if (lockB.tryLock(1, TimeUnit.SECONDS)) {
                        try {
                            // Got both locks, do work
                            return true;
                        } finally {
                            lockB.unlock();
                        }
                    }
                } finally {
                    lockA.unlock();  // Release A if couldn't get B
                }
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        return false;  // Retry later
    }
}
```

### Strategy 3: Acquire All or None (Break Hold and Wait)

```
┌─────────────────────────────────────────────────────────────────┐
│                 ALL-OR-NOTHING APPROACH                          │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│   RULE: Don't hold any lock while requesting another            │
│                                                                  │
│   OPTION A: Request all locks atomically                         │
│   ─────────────────────────────────                             │
│   • Get a "master lock" that controls access to all resources   │
│   • Thread requests all needed resources at once                 │
│   • Either gets ALL or waits with NONE                           │
│                                                                  │
│   OPTION B: Release before requesting                            │
│   ────────────────────────────────                              │
│   • If can't get Lock B, release Lock A first                   │
│   • Retry from the beginning                                     │
│   • May cause livelock (keep retrying forever)                  │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

---

## Application-Level Tactics

```
┌─────────────────────────────────────────────────────────────────┐
│              APPLICATION-LEVEL DEADLOCK TACTICS                  │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│   1. SET TIMEOUTS                                                │
│   ───────────────                                               │
│   • Use tryLock(timeout) instead of lock()                      │
│   • If timeout, release all locks and retry                     │
│   • Add random backoff to prevent livelock                      │
│                                                                  │
│   2. USE HIGHER-LEVEL CONCURRENCY                                │
│   ────────────────────────────────                              │
│   • java.util.concurrent classes handle locking internally      │
│   • ConcurrentHashMap, BlockingQueue, etc.                      │
│   • Less chance to make locking mistakes                        │
│                                                                  │
│   3. MINIMIZE LOCK SCOPE                                         │
│   ──────────────────────                                        │
│   • Hold locks for shortest time possible                       │
│   • Reduce window for deadlock                                  │
│                                                                  │
│   4. AVOID NESTED LOCKS                                          │
│   ─────────────────────                                         │
│   • If you need 2 locks, question your design                   │
│   • Consider lock-free algorithms                               │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

---

## When to Use Each Strategy

| Strategy | Complexity | Runtime Cost | Best For |
|----------|------------|--------------|----------|
| **Lock Ordering** | Low | None | Known lock relationships |
| **Timeout/Retry** | Medium | Retry overhead | Dynamic lock patterns |
| **All-or-Nothing** | High | Lock contention | Transaction systems |
| **Detection** | High | Monitoring cost | Database systems |
| **Ignorance** | None | Reboot cost | Desktop applications |

---

## Common Gotchas

### 1. Nested Synchronized Blocks

```java
// ❌ DANGER: Different lock order in different methods
class A {
    synchronized void method1(B b) {
        b.method2(this);  // Calls synchronized method on B
    }
}

class B {
    synchronized void method2(A a) {
        a.method1(this);  // Calls synchronized method on A
    }
}
// Thread 1: A.method1() → B.method2() → A.method1() 💀
// Thread 2: B.method2() → A.method1() → B.method2() 💀
```

### 2. Calling External Methods While Holding Lock

```java
// ❌ BAD: Calling unknown code while holding lock
synchronized (lock) {
    callback.onComplete();  // What if callback acquires our lock?
}

// ✅ GOOD: Release lock before external call
Data data;
synchronized (lock) {
    data = prepareData();
}
callback.onComplete(data);  // Call outside synchronized block
```

### 3. Lock in Constructor

```java
// ❌ DANGER: Can deadlock if constructor called during lock
public class Bad {
    public synchronized Bad() {
        // Another thread holding lock can't create Bad
    }
}

// ✅ GOOD: Avoid synchronized constructors
public class Good {
    public Good() {
        // Fast initialization
    }
    public synchronized void init() {
        // Separate initialization if needed
    }
}
```

---

## Detecting Deadlocks

### Using jstack (Command Line)

```bash
# Get thread dump to see deadlocked threads
jstack <pid>

# Output shows:
# "Thread-1" waiting for lock held by "Thread-2"
# "Thread-2" waiting for lock held by "Thread-1"
# DEADLOCK DETECTED
```

### Using ThreadMXBean (Programmatic)

```java
ThreadMXBean bean = ManagementFactory.getThreadMXBean();
long[] deadlockedThreads = bean.findDeadlockedThreads();

if (deadlockedThreads != null) {
    System.out.println("DEADLOCK DETECTED!");
    for (long id : deadlockedThreads) {
        ThreadInfo info = bean.getThreadInfo(id);
        System.out.println("Thread: " + info.getThreadName());
        System.out.println("Blocked on: " + info.getLockName());
        System.out.println("Owner: " + info.getLockOwnerName());
    }
}
```

---

## Summary

| Concept | Key Point |
|---------|-----------|
| **Deadlock** | Threads stuck forever waiting for each other |
| **4 Conditions** | Mutual Exclusion, Hold & Wait, No Preemption, Circular Wait |
| **Prevention** | Lock ordering, timeouts, all-or-nothing |
| **Simple Fix** | Always acquire locks in consistent order |
| **Best Practice** | Use java.util.concurrent, minimize lock scope |

### Decision Flowchart

```
                       ┌────────────────────────────┐
                       │  Need multiple locks?      │
                       └───────────────┬────────────┘
                                       │
                       ┌───────────────┴───────────────┐
                       │                               │
                       ▼                               ▼
                      YES                             NO
                       │                               │
                       ▼                               ▼
          ┌────────────────────────┐         ┌─────────────────┐
          │ Can you define order?  │         │ No deadlock risk│
          └───────────┬────────────┘         └─────────────────┘
                      │
           ┌──────────┴──────────┐
           │                     │
           ▼                     ▼
          YES                   NO
           │                     │
           ▼                     ▼
    ┌───────────────┐    ┌────────────────┐
    │ LOCK ORDERING │    │ USE TIMEOUTS   │
    │ (consistent   │    │ + RETRY        │
    │  order)       │    └────────────────┘
    └───────────────┘
```

---

## Next Chapter
→ [c14: Wait/Notify](../c14_wait_notify/WaitNotifyNotes.md) — Inter-thread communication mechanisms
