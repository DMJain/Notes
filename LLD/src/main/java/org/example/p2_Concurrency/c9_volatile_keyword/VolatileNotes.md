# volatile Keyword in Java

## Prerequisites
- [c5: Synchronization Problem](../c5_synchronization_problem/SynchronizationProblemNotes.md) — Race conditions
- [c6: Mutex Locks](../c6_mutex_locks/MutexLocksNotes.md) — ReentrantLock
- [c7: synchronized](../c7_synchronized_keyword/SynchronizedNotes.md) — Built-in locks
- [c8: Atomic Datatypes](../c8_atomic_datatypes/AtomicDatatypesNotes.md) — Lock-free operations

---

## What You'll Learn

| Question | Answer |
|----------|--------|
| **WHAT** | Ensures changes to a variable are VISIBLE to all threads immediately |
| **WHY** | Without volatile, threads may see STALE (old) values from their local cache |
| **WHEN** | Simple flags, state indicators (NOT for compound operations!) |
| **HOW** | `volatile` modifier: `volatile boolean flag = false;` |

---

## A DIFFERENT Problem: Memory Visibility

```
┌─────────────────────────────────────────────────────────────────────────────┐
│            volatile SOLVES A DIFFERENT PROBLEM THAN synchronized!            │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│   synchronized / Atomic:       volatile:                                     │
│   ─────────────────────        ─────────                                    │
│   Solves: ATOMICITY            Solves: VISIBILITY                            │
│   Problem: count++ is 3 ops    Problem: Thread sees stale data              │
│   Solution: Make it one op     Solution: Force read from main memory        │
│                                                                              │
│   ┌─────────────────────────────────────────────────────────────────────┐   │
│   │  VISIBILITY PROBLEM:                                                 │   │
│   │  ─────────────────────                                              │   │
│   │                                                                      │   │
│   │  Thread A: flag = true;   // Writes to its LOCAL CACHE               │   │
│   │  Thread B: while(!flag);  // Reads from ITS OWN CACHE → sees false!  │   │
│   │                                                                      │   │
│   │  Thread B may NEVER see the change because it reads from cache,      │   │
│   │  not main memory!                                                    │   │
│   │                                                                      │   │
│   └─────────────────────────────────────────────────────────────────────┘   │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## WHY Does This Happen? Thread-Local Caches

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                    MEMORY ARCHITECTURE                                       │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│   Each CPU core has its own CACHE for speed:                                 │
│                                                                              │
│   ┌─────────────────────────────────────────────────────────────────────┐   │
│   │                                                                      │   │
│   │      Thread A                              Thread B                  │   │
│   │    ┌──────────┐                          ┌──────────┐               │   │
│   │    │  CPU 1   │                          │  CPU 2   │               │   │
│   │    │  ┌─────┐ │                          │  ┌─────┐ │               │   │
│   │    │  │Cache│ │                          │  │Cache│ │               │   │
│   │    │  │flag │ │                          │  │flag │ │               │   │
│   │    │  │=true│ │                          │  │=false│← STALE!       │   │
│   │    │  └─────┘ │                          │  └─────┘ │               │   │
│   │    └────┬─────┘                          └────┬─────┘               │   │
│   │         │                                     │                      │   │
│   │         │          MAIN MEMORY                │                      │   │
│   │         │        ┌───────────┐                │                      │   │
│   │         └───────►│ flag=true │◄───────────────┘                      │   │
│   │                  └───────────┘                                       │   │
│   │                  (eventually updated)                                │   │
│   │                                                                      │   │
│   └─────────────────────────────────────────────────────────────────────┘   │
│                                                                              │
│   PROBLEM: JVM/CPU may NOT flush cache to main memory immediately!          │
│   Thread B continues reading FALSE from its cache!                          │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## The Problem: Infinite Loop

```java
public class StopFlag {
    private boolean flag = false;  // NOT volatile!
    
    public void toggleFlag() {
        flag = true;  // Thread A sets flag
    }
    
    public boolean getFlag() {
        return flag;  // Thread B reads flag
    }
}

// Main
StopFlag sf = new StopFlag();

// Thread B: Wait for flag
Thread threadB = new Thread(() -> {
    while (!sf.getFlag()) {
        // Busy wait - reading from cache!
    }
    System.out.println("Flag is true!");  // May NEVER print!
});

threadB.start();

// After 2 seconds, Thread A sets flag
Thread.sleep(2000);
sf.toggleFlag();
System.out.println("Flag set to true!");

// PROBLEM: Thread B may loop FOREVER because it never sees the change!
```

---

## The Solution: volatile

```java
public class StopFlag {
    private volatile boolean flag = false;  // ✅ VOLATILE!
    
    public void toggleFlag() {
        flag = true;  // Writes directly to main memory
    }
    
    public boolean getFlag() {
        return flag;  // Reads directly from main memory
    }
}

// Now Thread B will SEE the change immediately!
```

---

## How volatile Works

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                    volatile GUARANTEES                                       │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│   1. WRITE: Always writes to MAIN MEMORY (flushes cache)                     │
│   2. READ: Always reads from MAIN MEMORY (bypasses cache)                    │
│                                                                              │
│   ┌─────────────────────────────────────────────────────────────────────┐   │
│   │                                                                      │   │
│   │  WITHOUT volatile:                                                   │   │
│   │  ──────────────────                                                 │   │
│   │  Thread A: flag = true  →  [A's Cache: true] → (maybe) [Memory]     │   │
│   │  Thread B: read flag    ←  [B's Cache: false] (stale!)              │   │
│   │                                                                      │   │
│   │  WITH volatile:                                                      │   │
│   │  ───────────────                                                    │   │
│   │  Thread A: flag = true  →  [Memory: true] (immediate!)               │   │
│   │  Thread B: read flag    ←  [Memory: true] (always fresh!)            │   │
│   │                                                                      │   │
│   └─────────────────────────────────────────────────────────────────────┘   │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## volatile Does NOT Provide Atomicity!

```
┌─────────────────────────────────────────────────────────────────────────────┐
│              ⚠️ CRITICAL: volatile ≠ ATOMIC!                                 │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│   volatile ensures VISIBILITY, not ATOMICITY!                                │
│                                                                              │
│   ❌ WRONG - Still a race condition!                                         │
│   ────────────────────────────────────                                      │
│   private volatile int count = 0;                                            │
│                                                                              │
│   public void increment() {                                                  │
│       count++;  // NOT ATOMIC! Still READ → MODIFY → WRITE                   │
│   }                                                                          │
│                                                                              │
│   ┌─────────────────────────────────────────────────────────────────────┐   │
│   │  count++ with volatile:                                              │   │
│   │                                                                      │   │
│   │  Step 1: READ from main memory              ← visible to all        │   │
│   │  Step 2: MODIFY (add 1)                     ← local operation        │   │
│   │  Step 3: WRITE to main memory               ← visible to all        │   │
│   │                                                                      │   │
│   │  Another thread can interrupt between steps! Race condition! 💥      │   │
│   │                                                                      │   │
│   └─────────────────────────────────────────────────────────────────────┘   │
│                                                                              │
│   For count++, use AtomicInteger instead!                                   │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## When to Use volatile

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                     WHEN TO USE volatile                                     │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│   ✅ USE WHEN:                                                               │
│   ────────────                                                              │
│   • Simple boolean flags (stop signal, ready flag)                           │
│   • Single writer, multiple readers                                          │
│   • No compound operations (just read or just write)                         │
│                                                                              │
│   // Good examples:                                                          │
│   volatile boolean stopRequested = false;                                    │
│   volatile boolean initialized = false;                                      │
│   volatile Object cachedResult = null;                                       │
│                                                                              │
│   ─────────────────────────────────────────────────────────────────────────  │
│                                                                              │
│   ❌ DO NOT USE WHEN:                                                        │
│   ──────────────────                                                        │
│   • You need count++ or count-- (use AtomicInteger)                         │
│   • Multiple variables must be updated together (use synchronized)          │
│   • Check-then-act patterns like "if empty, then add"                       │
│                                                                              │
│   // BAD - race condition!                                                   │
│   volatile int count = 0;                                                    │
│   count++;  // NOT safe!                                                     │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## Comparison Table

| Feature | synchronized | volatile | Atomic |
|---------|-------------|----------|--------|
| **Atomicity** | ✅ Yes | ❌ No | ✅ Yes |
| **Visibility** | ✅ Yes | ✅ Yes | ✅ Yes |
| **Blocking** | ✅ Yes | ❌ No | ❌ No |
| **Use case** | Complex ops | Simple flags | Counters |
| **Overhead** | High | Low | Medium |

---

## Common Gotchas

### ❌ Using volatile for Counters

```java
// ❌ WRONG!
private volatile int count = 0;

void increment() {
    count++;  // Race condition!
}

// ✅ CORRECT
private AtomicInteger count = new AtomicInteger(0);

void increment() {
    count.incrementAndGet();  // Atomic!
}
```

### ❌ Assuming volatile Replaces synchronized

```java
// ❌ WRONG - two variables must update atomically!
private volatile int balance = 100;
private volatile int lastTxAmount = 0;

void withdraw(int amount) {
    balance -= amount;     // Thread can be interrupted here!
    lastTxAmount = amount; // Inconsistent state visible to others
}

// ✅ CORRECT
synchronized void withdraw(int amount) {
    balance -= amount;
    lastTxAmount = amount;  // Both update atomically
}
```

---

## Summary

| Concept | Key Point |
|---------|-----------|
| **volatile** | Ensures visibility between threads (reads/writes go to main memory) |
| **Visibility** | All threads see the latest value immediately |
| **NOT atomic** | count++ on volatile still has race conditions |
| **Use case** | Simple boolean flags, single-writer scenarios |
| **Alternative** | Use AtomicInteger for counters, synchronized for complex ops |

### The Big Picture

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                    CHOOSING THE RIGHT TOOL                                   │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│   PROBLEM                          SOLUTION                                  │
│   ───────                          ────────                                 │
│   Multiple ops must be atomic  →   synchronized / ReentrantLock (c6, c7)    │
│   Single counter increment     →   AtomicInteger (c8)                       │
│   Thread visibility only       →   volatile (c9)  ← YOU ARE HERE            │
│   Thread-safe collections      →   ConcurrentHashMap (c10)                  │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## Project Demo Structure

```
c9_volatile_keyword/
├── VolatileNotes.md              ← You are here
├── Main.java
├── contracts/
│   └── IFlag.java
├── impl/
│   ├── VolatileFlag.java
│   └── NonVolatileFlag.java
└── runner/
    ├── VolatileRunner.java
    └── scenarios/
        └── MemoryVisibilityScenario.java
```

---

## Next Chapter
→ [c10: Concurrent Collections](../c10_concurrent_collections/ConcurrentCollectionsNotes.md) — Thread-safe data structures
