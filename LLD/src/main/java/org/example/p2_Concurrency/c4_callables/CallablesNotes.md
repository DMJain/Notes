# Callables & Futures in Java

## Prerequisites
- [c1: Introduction to Concurrency](../c1_introduction_to_concurrency/ConcurrencyNotes.md) — Understanding processes
- [c2: Threads in Java](../c2_threads_in_java/ThreadsNotes.md) — Runnable interface and thread lifecycle
- [c3: Executors](../c3_executors/ExecutorsNotes.md) — Thread pools and ExecutorService

---

## What You'll Learn

| Question | Answer |
|----------|--------|
| **WHAT** | Callable = a task that RETURNS a result; Future = a "promise" for that result |
| **WHY** | Because Runnable's `void run()` cannot return values |
| **WHEN** | When you need the result of a concurrent computation |
| **HOW** | Using `Callable<V>` interface and `Future<V>` to retrieve results |

---

## The Problem: Runnable Can't Return Values

In c2 and c3, we used Runnable for tasks:

```java
Runnable task = () -> {
    int result = 2 + 3;  // We computed something...
    // But how do we return 'result'? 😕
};
```

### Why This is Limiting

```
┌─────────────────────────────────────────────────────────────────┐
│            THE PROBLEM: RUNNABLE RETURNS VOID                    │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│   public interface Runnable {                                    │
│       void run();  ← Returns NOTHING!                            │
│   }                                                              │
│                                                                  │
│   ┌──────────────────────────────────────────────────────────┐  │
│   │  SCENARIO: Calculate sum of large array in parallel      │  │
│   │                                                           │  │
│   │  Main Thread: "Hey Thread 1, calculate sum of [0..499]"  │  │
│   │  Thread 1:    *calculates* 125,000                        │  │
│   │                                                           │  │
│   │  Main Thread: "Thread 1, what's the answer?"             │  │
│   │  Thread 1:    "Sorry, I'm Runnable. I can't tell you." 🤷│  │
│   │                                                           │  │
│   │  ❌ Runnable has no mechanism to return values!           │  │
│   └──────────────────────────────────────────────────────────┘  │
│                                                                  │
│   WORKAROUNDS (ugly and error-prone):                            │
│   ┌──────────────────────────────────────────────────────────┐  │
│   │  Option 1: Shared mutable variable                        │  │
│   │  ──────────────────────────────                          │  │
│   │  int[] result = new int[1];  // shared array              │  │
│   │  Runnable task = () -> result[0] = 2 + 3;                 │  │
│   │                                                           │  │
│   │  ❌ Thread safety issues!                                 │  │
│   │  ❌ No way to know when result is ready                   │  │
│   │  ❌ No exception handling                                 │  │
│   │                                                           │  │
│   │  Option 2: Pass a callback                                │  │
│   │  ──────────────────────────                               │  │
│   │  Runnable task = () -> callback.accept(2 + 3);            │  │
│   │                                                           │  │
│   │  ❌ Callback hell                                         │  │
│   │  ❌ Complex control flow                                  │  │
│   └──────────────────────────────────────────────────────────┘  │
│                                                                  │
│   💡 We need a better interface that CAN return values!          │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

---

## The Solution: Callable Interface

```java
public interface Callable<V> {
    V call() throws Exception;
}
```

### Callable vs Runnable: Side-by-Side

```
┌─────────────────────────────────────────────────────────────────┐
│              RUNNABLE vs CALLABLE COMPARISON                     │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│   RUNNABLE                        CALLABLE<V>                    │
│   ────────                        ───────────                   │
│                                                                  │
│   interface Runnable {            interface Callable<V> {        │
│       void run();                     V call() throws Exception; │
│   }                               }                              │
│                                                                  │
│   ┌─────────────────────┐        ┌─────────────────────┐        │
│   │ • Returns: void     │        │ • Returns: V (any!) │        │
│   │ • Exceptions: ❌ No  │        │ • Exceptions: ✅ Yes │        │
│   │ • Use: Side effects │        │ • Use: Computations │        │
│   └─────────────────────┘        └─────────────────────┘        │
│                                                                  │
│   // Example                      // Example                     │
│   Runnable r = () -> {            Callable<Integer> c = () -> {  │
│       System.out.println("Hi");       return 2 + 3;              │
│   };                              };                             │
│                                                                  │
│   // Cannot return value!         // Returns Integer!            │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

### Simple Callable Example

```java
// A Callable that returns the sum of two numbers
Callable<Integer> sumTask = () -> {
    return 2 + 3;  // Returns 5!
};

// A Callable that makes a network call and returns data
Callable<String> fetchUser = () -> {
    // Pretend this is a slow network call
    Thread.sleep(1000);
    return "{ \"name\": \"John\" }";
};

// A Callable that returns computation result
Callable<Double> calculatePi = () -> {
    // Complex calculation...
    return 3.14159265359;
};
```

---

## Future: The Promise of a Result

When you submit a Callable to an ExecutorService, you get back a **Future**:

```java
ExecutorService executor = Executors.newCachedThreadPool();
Future<Integer> future = executor.submit(() -> 2 + 3);
Integer result = future.get();  // Blocks until result is ready
```

### What is a Future?

```
┌─────────────────────────────────────────────────────────────────┐
│                       WHAT IS A FUTURE?                          │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│   A Future is a PROMISE that a result will be available later.   │
│                                                                  │
│   ┌──────────────────────────────────────────────────────────┐  │
│   │                   REAL WORLD ANALOGY                      │  │
│   │                                                           │  │
│   │   YOU: Place order at pizza counter                       │  │
│   │   CASHIER: "Here's your receipt #42. It'll be ready."    │  │
│   │                                                           │  │
│   │   Receipt #42 = FUTURE (a promise of pizza)               │  │
│   │                                                           │  │
│   │   You can:                                                │  │
│   │   • Wait at counter for pizza (future.get() - blocking)  │  │
│   │   • Check if ready (future.isDone())                      │  │
│   │   • Cancel order (future.cancel())                        │  │
│   │   • Wait max 10 minutes (future.get(10, MINUTES))         │  │
│   │                                                           │  │
│   │   When pizza is ready:                                    │  │
│   │   • Cashier: "Order #42!" (task completes)                │  │
│   │   • You: Pick up pizza (future.get() returns)             │  │
│   │                                                           │  │
│   └──────────────────────────────────────────────────────────┘  │
│                                                                  │
│   ┌──────────────────────────────────────────────────────────┐  │
│   │                    IN CODE                                │  │
│   │                                                           │  │
│   │   Future<Integer> future = executor.submit(() -> 2 + 3);  │  │
│   │                │                                          │  │
│   │                ▼                                          │  │
│   │   ┌──────────────────────────────────┐                   │  │
│   │   │  Future = a placeholder for      │                   │  │
│   │   │  a result that doesn't exist YET │                   │  │
│   │   │                                  │                   │  │
│   │   │  The result will appear when:    │                   │  │
│   │   │  • The Callable finishes running │                   │  │
│   │   │  • You call future.get()         │                   │  │
│   │   └──────────────────────────────────┘                   │  │
│   │                                                           │  │
│   └──────────────────────────────────────────────────────────┘  │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

### Future Methods

```
┌─────────────────────────────────────────────────────────────────┐
│                     FUTURE<V> METHODS                            │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│   V get()                                                        │
│   ───────                                                       │
│   • BLOCKS until result is ready                                 │
│   • Returns the computed value                                   │
│   • Throws ExecutionException if task threw exception            │
│                                                                  │
│   ┌─────────────────────────────────────────────────────────┐   │
│   │   Main:    ████──────────────────────────████████████   │   │
│   │                │                         ↑               │   │
│   │            get() called              get() returns       │   │
│   │            (BLOCKING)                   with value       │   │
│   │                │                         │               │   │
│   │   Worker:  ___█████████████████████████__               │   │
│   │            ↑                           ↑                 │   │
│   │         Task starts               Task ends              │   │
│   │                                   (result ready)         │   │
│   └─────────────────────────────────────────────────────────┘   │
│                                                                  │
│   V get(long timeout, TimeUnit unit)                             │
│   ──────────────────────────────────                            │
│   • Blocks for at most the specified time                        │
│   • Throws TimeoutException if result not ready in time          │
│                                                                  │
│   boolean isDone()                                               │
│   ────────────────                                              │
│   • Returns true if task completed (success, exception, cancel)  │
│   • Non-blocking check                                           │
│                                                                  │
│   boolean isCancelled()                                          │
│   ─────────────────────                                         │
│   • Returns true if task was cancelled before completion         │
│                                                                  │
│   boolean cancel(boolean mayInterruptIfRunning)                  │
│   ─────────────────────────────────────────────                 │
│   • Attempts to cancel the task                                  │
│   • If true: interrupt running thread                            │
│   • If false: only cancel if not started yet                     │
│   • Returns true if successfully cancelled                       │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

### Complete Example

```java
import java.util.concurrent.*;

public class CallableDemo {
    public static void main(String[] args) throws Exception {
        ExecutorService executor = Executors.newCachedThreadPool();
        
        // Submit a Callable that takes 2 seconds
        Future<Integer> future = executor.submit(() -> {
            System.out.println("Task started...");
            Thread.sleep(2000);  // Simulate work
            System.out.println("Task completed!");
            return 42;
        });
        
        System.out.println("Task submitted, doing other work...");
        
        // Check if done (non-blocking)
        System.out.println("Is done? " + future.isDone());  // false
        
        // Get result (BLOCKS until ready)
        Integer result = future.get();
        System.out.println("Result: " + result);  // 42
        
        System.out.println("Is done? " + future.isDone());  // true
        
        executor.shutdown();
    }
}
```

Output:
```
Task submitted, doing other work...
Task started...
Is done? false
Task completed!
Result: 42
Is done? true
```

---

## Timeline Visualization

```
┌─────────────────────────────────────────────────────────────────┐
│                 CALLABLE + FUTURE TIMELINE                       │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│   TIME ──────────────────────────────────────────────────────►  │
│   0s        1s        2s        3s        4s                    │
│                                                                  │
│   ┌──────────────────────────────────────────────────────────┐  │
│   │  Main Thread                                              │  │
│   │  ────────────                                            │  │
│   │                                                           │  │
│   │  [submit()] ──► [isDone?] ──► [get()] ────────► [result!]│  │
│   │      ↓            false         │      BLOCKING    │      │  │
│   │   returns                       │         ↑        ↓      │  │
│   │   Future                        │         │    use result │  │
│   │   immediately                   └─────────┴───────────────│  │
│   │                                                           │  │
│   └──────────────────────────────────────────────────────────┘  │
│                                                                  │
│   ┌──────────────────────────────────────────────────────────┐  │
│   │  Worker Thread                                            │  │
│   │  ─────────────                                           │  │
│   │                                                           │  │
│   │       ░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░░              │  │
│   │       ↑                                    ↑              │  │
│   │   call() starts                    call() returns         │  │
│   │   (doing computation)              (result available)     │  │
│   │                                                           │  │
│   └──────────────────────────────────────────────────────────┘  │
│                                                                  │
│   KEY INSIGHT:                                                   │
│   • submit() returns IMMEDIATELY with a Future                  │
│   • Main thread can do other work                                │
│   • get() blocks ONLY when you need the result                  │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

---

## submit() vs execute()

```
┌─────────────────────────────────────────────────────────────────┐
│                  execute() vs submit()                           │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│   execute(Runnable)              submit(Callable OR Runnable)    │
│   ─────────────────              ────────────────────────────   │
│                                                                  │
│   • Returns: void                • Returns: Future<V>            │
│   • For: fire-and-forget         • For: need result/status       │
│   • Exceptions: logged/lost      • Exceptions: via future.get() │
│                                                                  │
│   // execute()                   // submit()                     │
│   executor.execute(() -> {       Future<?> f = executor.submit( │
│       System.out.println("Hi");      () -> {                     │
│   });                                    return computeValue();  │
│   // No way to get result!               }                       │
│                                      );                          │
│                                      Integer val = f.get();      │
│                                                                  │
│   ┌─────────────────────────────────────────────────────────┐   │
│   │                    WHEN TO USE WHICH                     │   │
│   │                                                          │   │
│   │   execute():                                             │   │
│   │   • Logging                                              │   │
│   │   • Sending notifications                                │   │
│   │   • Fire-and-forget operations                           │   │
│   │                                                          │   │
│   │   submit():                                              │   │
│   │   • Calculations that return results                     │   │
│   │   • API calls that return data                           │   │
│   │   • Any time you need to know if task succeeded          │   │
│   │                                                          │   │
│   └─────────────────────────────────────────────────────────┘   │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

### ⚠️ Critical Gotcha: submit() Swallows Exceptions!

```java
// ❌ DANGER: Exception is SILENTLY SWALLOWED!
executor.submit(() -> {
    throw new RuntimeException("Oops!");
});
// No output! Exception is hidden inside Future!

// ✅ CORRECT: Call get() to see the exception
Future<?> future = executor.submit(() -> {
    throw new RuntimeException("Oops!");
});
try {
    future.get();  // This will throw!
} catch (ExecutionException e) {
    System.out.println("Task failed: " + e.getCause().getMessage());
}
```

---

## Exception Handling with Callables

```
┌─────────────────────────────────────────────────────────────────┐
│              EXCEPTION HANDLING IN CALLABLES                     │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│   Callable<V> can throw exceptions (unlike Runnable):            │
│                                                                  │
│   Callable<String> task = () -> {                                │
│       if (someCondition) {                                       │
│           throw new IOException("Network error!");               │
│       }                                                          │
│       return "Success";                                          │
│   };                                                             │
│                                                                  │
│   How exceptions propagate:                                      │
│   ┌──────────────────────────────────────────────────────────┐  │
│   │                                                           │  │
│   │   Callable throws   Wrapped in        Unwrapped when      │  │
│   │   IOException  ──►  ExecutionException ──► future.get()   │  │
│   │                                             called        │  │
│   │                                                           │  │
│   │   try {                                                   │  │
│   │       String result = future.get();                       │  │
│   │   } catch (ExecutionException e) {                        │  │
│   │       Throwable cause = e.getCause();  // Original!       │  │
│   │       if (cause instanceof IOException) {                 │  │
│   │           // Handle network error                         │  │
│   │       }                                                   │  │
│   │   }                                                       │  │
│   │                                                           │  │
│   └──────────────────────────────────────────────────────────┘  │
│                                                                  │
│   Exception Types from future.get():                             │
│   ┌─────────────────────┬────────────────────────────────────┐  │
│   │ InterruptedException│ Thread was interrupted while waiting│  │
│   │ ExecutionException  │ Task threw an exception (unwrap it!)│  │
│   │ CancellationException│ Task was cancelled                 │  │
│   │ TimeoutException    │ Timeout expired (timed get only)    │  │
│   └─────────────────────┴────────────────────────────────────┘  │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

---

## When to Use Callable vs Runnable

### Decision Flowchart

```
                    ┌────────────────────────────────────┐
                    │   Does your task return a value?   │
                    └───────────────────┬────────────────┘
                                        │
                    ┌───────────────────┴───────────────┐
                    │                                   │
                    ▼                                   ▼
                   YES                                 NO
                    │                                   │
                    ▼                                   │
            ┌───────────────┐                          │
            │   Callable<V> │                          │
            └───────────────┘                          │
                                                       ▼
                              ┌─────────────────────────────────────┐
                              │   Do you need to check completion?   │
                              │   Or handle task exceptions?         │
                              └──────────────────┬──────────────────┘
                                                 │
                              ┌──────────────────┴──────────────────┐
                              │                                     │
                              ▼                                     ▼
                             YES                                   NO
                              │                                     │
                              ▼                                     ▼
                      ┌───────────────┐                   ┌───────────────┐
                      │ submit() with │                   │   execute()   │
                      │   Runnable    │                   │ (fire-forget) │
                      │ (get Future)  │                   │               │
                      └───────────────┘                   └───────────────┘
```

### Summary Table

| Scenario | Use |
|----------|-----|
| Calculate sum, return result | `Callable<Integer>` |
| Fetch API data, return JSON | `Callable<String>` |
| Send email, don't care about result | `Runnable` + `execute()` |
| Send email, need to know if succeeded | `Runnable` + `submit()` |
| Log message to file | `Runnable` + `execute()` |

---

## Coding Problem: Multi-threaded Merge Sort

A powerful example combining everything we've learned:

```
┌─────────────────────────────────────────────────────────────────┐
│              MULTI-THREADED MERGE SORT                           │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│   Problem: Sort [7, 3, 1, 2, 4, 6, 17, 12] using multiple threads│
│                                                                  │
│   Strategy: Divide array, sort halves in parallel, merge         │
│                                                                  │
│                    [7, 3, 1, 2, 4, 6, 17, 12]                    │
│                              │                                    │
│              ┌───────────────┴───────────────┐                   │
│              ▼                               ▼                    │
│         [7, 3, 1, 2]                  [4, 6, 17, 12]             │
│         Thread 1                      Thread 2                   │
│              │                               │                    │
│         ┌────┴────┐                   ┌─────┴─────┐              │
│         ▼         ▼                   ▼           ▼              │
│      [7,3]     [1,2]               [4,6]      [17,12]            │
│      Thread    Thread              Thread     Thread             │
│         │         │                   │           │              │
│         ▼         ▼                   ▼           ▼              │
│      [3,7]     [1,2]               [4,6]      [12,17]            │
│         │         │                   │           │              │
│         └────┬────┘                   └─────┬─────┘              │
│              ▼                               ▼                    │
│         [1,2,3,7]                     [4,6,12,17]                │
│              │                               │                    │
│              └───────────────┬───────────────┘                   │
│                              ▼                                    │
│                    [1,2,3,4,6,7,12,17]                           │
│                                                                  │
│   Key Insight:                                                   │
│   • Each Sorter is a Callable<List<Integer>>                     │
│   • Returns sorted sublist                                       │
│   • Parent thread waits (future.get()) for both halves           │
│   • Then merges them                                             │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

### Implementation

**Sorter.java** - A Callable that sorts a list:

```java
import java.util.*;
import java.util.concurrent.*;

public class Sorter implements Callable<List<Integer>> {
    private List<Integer> arr;
    private ExecutorService executor;

    public Sorter(List<Integer> arr, ExecutorService executor) {
        this.arr = arr;
        this.executor = executor;
    }

    @Override
    public List<Integer> call() throws Exception {
        // Base case: already sorted
        if (arr.size() <= 1) {
            return arr;
        }

        // Divide
        int mid = arr.size() / 2;
        List<Integer> leftArr = new ArrayList<>(arr.subList(0, mid));
        List<Integer> rightArr = new ArrayList<>(arr.subList(mid, arr.size()));

        // Conquer: sort both halves in parallel threads!
        Sorter leftSorter = new Sorter(leftArr, executor);
        Sorter rightSorter = new Sorter(rightArr, executor);

        Future<List<Integer>> leftFuture = executor.submit(leftSorter);
        Future<List<Integer>> rightFuture = executor.submit(rightSorter);

        // Wait for results
        leftArr = leftFuture.get();   // Blocks until left half sorted
        rightArr = rightFuture.get(); // Blocks until right half sorted

        // Merge sorted halves
        return merge(leftArr, rightArr);
    }

    private List<Integer> merge(List<Integer> left, List<Integer> right) {
        List<Integer> result = new ArrayList<>();
        int i = 0, j = 0;
        while (i < left.size() && j < right.size()) {
            if (left.get(i) < right.get(j)) {
                result.add(left.get(i++));
            } else {
                result.add(right.get(j++));
            }
        }
        while (i < left.size()) result.add(left.get(i++));
        while (j < right.size()) result.add(right.get(j++));
        return result;
    }
}
```

**Main.java** - Using the Sorter:

```java
import java.util.*;
import java.util.concurrent.*;

public class Main {
    public static void main(String[] args) throws Exception {
        List<Integer> list = List.of(7, 3, 1, 2, 4, 6, 17, 12);
        System.out.println("Before: " + list);

        ExecutorService executor = Executors.newCachedThreadPool();

        Sorter sorter = new Sorter(new ArrayList<>(list), executor);
        Future<List<Integer>> future = executor.submit(sorter);

        List<Integer> sorted = future.get();  // Wait for result
        System.out.println("After:  " + sorted);

        executor.shutdown();
    }
}
```

Output:
```
Before: [7, 3, 1, 2, 4, 6, 17, 12]
After:  [1, 2, 3, 4, 6, 7, 12, 17]
```

---

## When NOT to Use Callable

### ❌ Anti-Patterns

| Scenario | Why Callable is Overkill |
|----------|-------------------------|
| **Just logging** | No return value needed → use Runnable |
| **Sending notifications** | Fire-and-forget → Runnable + execute() |
| **Very simple tasks** | Future overhead not worth it |
| **Result discarded** | Why compute what you won't use? |

### Example: Overkill

```java
// ❌ Overkill: Using Callable when we don't use the result
Future<Void> f = executor.submit(() -> {
    System.out.println("Hello");
    return null;  // Forced to return something
});
// Never call f.get()? Then why use Callable?

// ✅ Better: Just use Runnable
executor.execute(() -> System.out.println("Hello"));
```

---

## Common Gotchas

### 1. Forgetting to Call get()

```java
// ❌ Exception is silently lost!
executor.submit(() -> {
    throw new RuntimeException("Error!");
});
// Program continues, you never know it failed

// ✅ Always check Future if you care about success
Future<?> f = executor.submit(() -> {
    throw new RuntimeException("Error!");
});
try {
    f.get();
} catch (ExecutionException e) {
    logger.error("Task failed", e.getCause());
}
```

### 2. Blocking the Main Thread Unnecessarily

```java
// ❌ Defeats the purpose of async!
Future<Integer> f1 = executor.submit(slowTask1);
Integer r1 = f1.get();  // BLOCKS!

Future<Integer> f2 = executor.submit(slowTask2);
Integer r2 = f2.get();  // BLOCKS!
// Total time = task1 + task2 (sequential!)

// ✅ Submit all first, then collect results
Future<Integer> f1 = executor.submit(slowTask1);
Future<Integer> f2 = executor.submit(slowTask2);
// Both running in parallel now!

Integer r1 = f1.get();
Integer r2 = f2.get();
// Total time = max(task1, task2) (parallel!)
```

### 3. Not Handling InterruptedException

```java
// ❌ BAD: Swallowing interrupt
try {
    result = future.get();
} catch (InterruptedException e) {
    // ignored!
}

// ✅ GOOD: Restore interrupt status
try {
    result = future.get();
} catch (InterruptedException e) {
    Thread.currentThread().interrupt();
    throw new RuntimeException("Interrupted!", e);
}
```

---

## Summary

| Concept | Key Point |
|---------|-----------|
| **Runnable** | `void run()` — cannot return values |
| **Callable<V>** | `V call()` — returns a value, can throw exceptions |
| **Future<V>** | A "promise" for a result that will be available later |
| **get()** | Blocks until result is ready |
| **isDone()** | Non-blocking check if task completed |
| **cancel()** | Attempt to cancel the task |
| **submit()** | Returns Future, use for Callable or when you need status |
| **execute()** | Void return, use for fire-and-forget Runnables |

### The Big Picture

```
┌─────────────────────────────────────────────────────────────────┐
│              THREAD EVOLUTION IN THIS COURSE                     │
├─────────────────────────────────────────────────────────────────┤
│                                                                  │
│   c2: Thread + Runnable                                          │
│   ─────────────────────                                         │
│   • Manual thread management                                     │
│   • No return values                                             │
│   • No thread reuse                                              │
│                                                                  │
│   c3: ExecutorService + Runnable                                 │
│   ──────────────────────────────                                │
│   • Thread pools (reuse!)                                        │
│   • Still no return values                                       │
│   • Better resource management                                   │
│                                                                  │
│   c4: ExecutorService + Callable + Future  ← YOU ARE HERE       │
│   ────────────────────────────────────────                      │
│   • Thread pools ✅                                              │
│   • Return values ✅                                             │
│   • Exception handling ✅                                        │
│   • Task status/cancellation ✅                                  │
│                                                                  │
│   NEXT: Synchronization (when threads share data)               │
│                                                                  │
└─────────────────────────────────────────────────────────────────┘
```

---

## Project Demo Structure

```
c4_callables/
├── CallablesNotes.md           ← You are here
├── Main.java                   ← Entry point with all demos
└── examples/
    ├── SumCalculator.java      ← Simple Callable example
    ├── FutureDemo.java         ← Future methods demo
    └── MergeSorter.java        ← Multi-threaded merge sort
```

---

## Next Chapter
→ [Synchronization](../c5_synchronization/SynchronizationNotes.md) — Handling shared data between threads (Adder-Subtractor problem)
