# Concurrent Collections in Java

## Prerequisites
- [c5: Synchronization Problem](../c5_synchronization_problem/SynchronizationProblemNotes.md) — Race conditions
- [c6: Mutex Locks](../c6_mutex_locks/MutexLocksNotes.md) — ReentrantLock
- [c7: synchronized](../c7_synchronized_keyword/SynchronizedNotes.md) — Built-in locks
- [c8: Atomic Datatypes](../c8_atomic_datatypes/AtomicDatatypesNotes.md) — Lock-free operations
- [c9: volatile](../c9_volatile_keyword/VolatileNotes.md) — Memory visibility

---

## What You'll Learn

| Question | Answer |
|----------|--------|
| **WHAT** | Thread-safe data structures for concurrent access |
| **WHY** | Regular HashMap is NOT thread-safe, causes data corruption |
| **WHEN** | Sharing collections (Map, List, Set) across multiple threads |
| **HOW** | `ConcurrentHashMap`, `Collections.synchronizedMap()`, etc. |

---

## The Problem: HashMap Is NOT Thread-Safe

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                  HashMap: DANGEROUS IN CONCURRENT ACCESS                     │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│   HashMap in Java is NOT thread-safe:                                        │
│                                                                              │
│   ┌─────────────────────────────────────────────────────────────────────┐   │
│   │  Problems with HashMap under concurrent access:                      │   │
│   │                                                                      │   │
│   │  1. LOST UPDATES:                                                    │   │
│   │     Thread A: put("key", value1)                                    │   │
│   │     Thread B: put("key", value2)  → One value is lost!              │   │
│   │                                                                      │   │
│   │  2. DATA CORRUPTION:                                                 │   │
│   │     During resize, internal structure can become corrupted          │   │
│   │     → NullPointerException, infinite loops!                         │   │
│   │                                                                      │   │
│   │  3. INFINITE LOOP (Pre-Java 8):                                      │   │
│   │     Concurrent put() during resize → circular linked list           │   │
│   │     → get() loops forever! 💀                                       │   │
│   │                                                                      │   │
│   └─────────────────────────────────────────────────────────────────────┘   │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## Thread-Safe Map Options

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                 THREE OPTIONS FOR THREAD-SAFE MAPS                           │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│   ┌─────────────────────────────────────────────────────────────────────┐   │
│   │  1️⃣  HASHTABLE (Legacy - DON'T USE)                                  │   │
│   │  ─────────────────────────────────────                               │   │
│   │  Map<K,V> map = new Hashtable<>();                                   │   │
│   │                                                                      │   │
│   │  • Locks ENTIRE map for EVERY operation                              │   │
│   │  • Only ONE thread can access at a time                              │   │
│   │  • Very slow under high concurrency                                  │   │
│   │  ❌ Deprecated - avoid in new code                                   │   │
│   │                                                                      │   │
│   └─────────────────────────────────────────────────────────────────────┘   │
│                                                                              │
│   ┌─────────────────────────────────────────────────────────────────────┐   │
│   │  2️⃣  Collections.synchronizedMap() (Simple Wrapper)                  │   │
│   │  ────────────────────────────────────────────────                   │   │
│   │  Map<K,V> map = Collections.synchronizedMap(new HashMap<>());        │   │
│   │                                                                      │   │
│   │  • Wraps HashMap with synchronized methods                           │   │
│   │  • Still locks ENTIRE map for each operation                         │   │
│   │  • Better than raw HashMap, but not optimal                          │   │
│   │  ⚠️ Use when simplicity is preferred over performance               │   │
│   │                                                                      │   │
│   └─────────────────────────────────────────────────────────────────────┘   │
│                                                                              │
│   ┌─────────────────────────────────────────────────────────────────────┐   │
│   │  3️⃣  ConcurrentHashMap (RECOMMENDED)                                 │   │
│   │  ────────────────────────────────────                               │   │
│   │  Map<K,V> map = new ConcurrentHashMap<>();                           │   │
│   │                                                                      │   │
│   │  • Uses SEGMENT/BUCKET-level locking                                 │   │
│   │  • Multiple threads can read/write DIFFERENT buckets simultaneously │   │
│   │  • Lock-free reads in Java 8+                                        │   │
│   │  ✅ Best performance for high concurrency!                           │   │
│   │                                                                      │   │
│   └─────────────────────────────────────────────────────────────────────┘   │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## ConcurrentHashMap: How It Works

```
┌─────────────────────────────────────────────────────────────────────────────┐
│              ConcurrentHashMap BUCKET-LEVEL LOCKING                          │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│   Hashtable / synchronizedMap:       ConcurrentHashMap:                      │
│   ─────────────────────────────      ───────────────────                    │
│                                                                              │
│   ┌─────────────────────┐            ┌──┬──┬──┬──┬──┬──┬──┬──┐              │
│   │    ENTIRE MAP       │            │B0│B1│B2│B3│B4│B5│B6│B7│              │
│   │    🔒 LOCKED        │            │🔒│  │🔒│  │  │  │  │  │              │
│   │                     │            └──┴──┴──┴──┴──┴──┴──┴──┘              │
│   │  Only ONE thread    │              ↑     ↑                              │
│   │  can access!        │            Thread Thread                          │
│   │                     │              A     B                              │
│   └─────────────────────┘                                                    │
│                                      Multiple threads access                 │
│   Very slow! 🐢                      DIFFERENT buckets in parallel! 🚀      │
│                                                                              │
│   ─────────────────────────────────────────────────────────────────────────  │
│                                                                              │
│   PERFORMANCE COMPARISON (High Contention):                                  │
│   ─────────────────────────────────────────                                 │
│                                                                              │
│   Hashtable:          █████████████████████████████████████  (100%)          │
│   synchronizedMap:    █████████████████████████████████      (90%)           │
│   ConcurrentHashMap:  ████████                               (20%)           │
│                                                                              │
│   ConcurrentHashMap is ~5x faster under high concurrency!                   │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## Usage Examples

### ConcurrentHashMap (Recommended)

```java
import java.util.concurrent.ConcurrentHashMap;

Map<String, Integer> map = new ConcurrentHashMap<>();

// All these are thread-safe:
map.put("key", 1);
map.get("key");
map.remove("key");
map.putIfAbsent("key", 1);  // Atomic check-then-insert
map.computeIfAbsent("key", k -> 1);  // Atomic compute

// Atomic update
map.compute("key", (k, v) -> v == null ? 1 : v + 1);
```

### Collections.synchronizedMap (Simpler but slower)

```java
import java.util.Collections;
import java.util.HashMap;

Map<String, Integer> map = Collections.synchronizedMap(new HashMap<>());

// Thread-safe, but with full map locking
map.put("key", 1);
map.get("key");
```

---

## Key Methods of ConcurrentHashMap

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                  ATOMIC METHODS (Interview Favorites!)                       │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│   putIfAbsent(key, value)                                                    │
│   ───────────────────────                                                   │
│   Atomically: if (!map.containsKey(key)) map.put(key, value);               │
│                                                                              │
│   // ❌ Race condition:                                                      │
│   if (!map.containsKey("k")) {     // Thread A checks                       │
│       map.put("k", 1);              // Thread B inserts between!            │
│   }                                                                          │
│                                                                              │
│   // ✅ Atomic:                                                              │
│   map.putIfAbsent("k", 1);          // Single atomic operation              │
│                                                                              │
│   ─────────────────────────────────────────────────────────────────────────  │
│                                                                              │
│   computeIfAbsent(key, function)                                             │
│   ──────────────────────────────                                            │
│   Atomically: if (!map.containsKey(key)) map.put(key, function(key));       │
│                                                                              │
│   // Create entry only if missing                                            │
│   map.computeIfAbsent("count", k -> new AtomicInteger(0));                  │
│                                                                              │
│   ─────────────────────────────────────────────────────────────────────────  │
│                                                                              │
│   compute(key, biFunction)                                                   │
│   ────────────────────────                                                  │
│   Atomically compute new value based on current value                        │
│                                                                              │
│   // Atomic increment                                                        │
│   map.compute("count", (k, v) -> v == null ? 1 : v + 1);                    │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## Comparison Table

| Feature | HashMap | Hashtable | synchronizedMap | ConcurrentHashMap |
|---------|---------|-----------|-----------------|-------------------|
| Thread-safe | ❌ | ✅ | ✅ | ✅ |
| Null keys | ✅ | ❌ | ✅ | ❌ |
| Null values | ✅ | ❌ | ✅ | ❌ |
| Lock scope | N/A | Entire map | Entire map | Per-bucket |
| Performance | Fast (single) | Slow | Slow | Fast (concurrent) |
| Recommended | Single-thread | Never | Simple cases | Production |

---

## When to Use What

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                     DECISION GUIDE                                           │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│   Is the map accessed by multiple threads?                                   │
│                     │                                                        │
│         ┌───────────┴───────────┐                                           │
│         NO                      YES                                          │
│         │                       │                                           │
│         ▼                       ▼                                           │
│      HashMap              High concurrency?                                  │
│      (fastest for                │                                          │
│       single thread)    ┌───────┴───────┐                                   │
│                         NO              YES                                  │
│                         │               │                                   │
│                         ▼               ▼                                   │
│              Collections.      ConcurrentHashMap                             │
│              synchronizedMap   (best performance)                            │
│              (simpler code)                                                  │
│                                                                              │
│   ┌─────────────────────────────────────────────────────────────────────┐   │
│   │  SUMMARY:                                                            │   │
│   │  • Single thread → HashMap                                          │   │
│   │  • Few threads, simple → synchronizedMap                            │   │
│   │  • Many threads, high perf → ConcurrentHashMap                      │   │
│   │  • Never use Hashtable in new code!                                 │   │
│   └─────────────────────────────────────────────────────────────────────┘   │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## Other Concurrent Collections

```
┌─────────────────────────────────────────────────────────────────────────────┐
│              java.util.concurrent COLLECTIONS                                │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│   MAPS:                                                                      │
│   ─────                                                                     │
│   ConcurrentHashMap<K,V>    ← Most common, bucket-level locking             │
│   ConcurrentSkipListMap<K,V> ← Sorted, concurrent                           │
│                                                                              │
│   QUEUES:                                                                    │
│   ───────                                                                   │
│   ConcurrentLinkedQueue<E>  ← Lock-free, unbounded                          │
│   LinkedBlockingQueue<E>    ← Blocking, bounded/unbounded                   │
│   ArrayBlockingQueue<E>     ← Blocking, fixed size                          │
│   PriorityBlockingQueue<E>  ← Blocking, priority-based                      │
│                                                                              │
│   SETS:                                                                      │
│   ─────                                                                     │
│   ConcurrentSkipListSet<E>  ← Sorted, concurrent                            │
│   CopyOnWriteArraySet<E>    ← Copies on write, good for read-heavy         │
│                                                                              │
│   LISTS:                                                                     │
│   ──────                                                                    │
│   CopyOnWriteArrayList<E>   ← Copies on write, good for read-heavy          │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## Common Gotchas

### ❌ Compound Operations Are NOT Atomic

```java
ConcurrentHashMap<String, Integer> map = new ConcurrentHashMap<>();

// ❌ NOT atomic! Check-then-act race condition!
if (!map.containsKey("key")) {
    map.put("key", 1);  // Another thread can insert between!
}

// ✅ Use atomic methods
map.putIfAbsent("key", 1);
map.computeIfAbsent("key", k -> expensiveComputation());
```

### ❌ Iterating While Modifying

```java
// ConcurrentHashMap does NOT throw ConcurrentModificationException
// BUT the iteration may see inconsistent state

for (String key : map.keySet()) {
    map.remove(key);  // Allowed, but may miss some entries
}

// ✅ Use removeIf for safe batch removal
map.entrySet().removeIf(e -> e.getValue() < 0);
```

---

## Summary

| Concept | Key Point |
|---------|-----------|
| **HashMap** | NOT thread-safe, use only in single-threaded code |
| **Hashtable** | Legacy, locks entire map - NEVER use in new code |
| **synchronizedMap** | Full map locking, simpler but slower |
| **ConcurrentHashMap** | Bucket-level locking, best for high concurrency |
| **putIfAbsent()** | Atomic check-then-insert |
| **computeIfAbsent()** | Atomic compute-if-missing |

### The Complete Picture

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                 CONCURRENCY SYNCHRONIZATION SUMMARY                          │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│   c5: Problem       → Race conditions, count++ is not atomic                │
│   c6: ReentrantLock → Explicit lock/unlock, tryLock, fairness               │
│   c7: synchronized  → Simpler syntax, auto-unlock                           │
│   c8: Atomic types  → Lock-free counters using hardware CAS                 │
│   c9: volatile      → Memory visibility (not atomicity!)                    │
│   c10: Collections  → Thread-safe data structures ← YOU ARE HERE            │
│                                                                              │
│   ✅ You now have the complete toolkit for thread-safe Java programming!    │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## Project Demo Structure

```
c10_concurrent_collections/
├── ConcurrentCollectionsNotes.md    ← You are here
├── Main.java
├── contracts/
│   └── IMapDemo.java
├── impl/
│   └── MapComparisonDemo.java
└── runner/
    ├── CollectionsRunner.java
    └── comparison/
        └── HashMapVsConcurrentHashMapDemo.java
```

---

## Previous Chapter
← [c9: volatile keyword](../c9_volatile_keyword/VolatileNotes.md)
