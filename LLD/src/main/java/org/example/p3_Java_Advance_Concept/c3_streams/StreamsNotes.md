# Java Streams API

> **Prerequisites:** Understanding of [Lambdas & Functional Interfaces](../c2_lambdas/LambdasNotes.md) - you'll use lambdas heavily here!

---

## What You'll Learn
By the end of this chapter, you'll understand:
- **WHAT** streams are (data processing pipelines)
- **WHY** they exist (cleaner, more readable code)
- **WHEN** to use them (and when NOT to)
- **HOW** they work (lazy evaluation, intermediate vs terminal)

---

## 1. What is a Stream? (Visual Understanding)

### The Factory Assembly Line Analogy

Think of a Stream as a **factory assembly line** for processing data:

```
┌─────────────────────────────────────────────────────────────────────┐
│                    STREAM = ASSEMBLY LINE                           │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│   📦 Raw Materials        🔧 Processing Stations         📤 Output  │
│   (Source Data)           (Operations)                   (Result)   │
│                                                                     │
│   [1,2,3,4,5,6,7,8]                                                 │
│         │                                                           │
│         ▼                                                           │
│   ┌──────────┐  Items flow through, one by one                      │
│   │ filter() │  → Keep only even numbers                            │
│   └────┬─────┘                                                      │
│        │                                                            │
│   [2,4,6,8]                                                         │
│        │                                                            │
│        ▼                                                            │
│   ┌──────────┐                                                      │
│   │  map()   │  → Multiply each by 10                               │
│   └────┬─────┘                                                      │
│        │                                                            │
│   [20,40,60,80]                                                     │
│        │                                                            │
│        ▼                                                            │
│   ┌──────────┐                                                      │
│   │ collect()│  → Gather into a List                                │
│   └────┬─────┘                                                      │
│        │                                                            │
│        ▼                                                            │
│   List: [20, 40, 60, 80]  ✅ RESULT!                                │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```

### Key Insight: Streams Are NOT Data Structures!

```
┌─────────────────────────────────────────────────────────────────────┐
│              STREAM vs COLLECTION - CRUCIAL DIFFERENCE              │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│   COLLECTION (e.g., List)         │    STREAM                       │
│   ────────────────────────        │    ──────                       │
│                                   │                                 │
│   📦 Stores data                  │    🚿 Flows data                │
│   Has a fixed size                │    Doesn't store anything       │
│   Can be accessed repeatedly      │    Can only be consumed ONCE    │
│   Eager (data exists now)         │    Lazy (computed on demand)    │
│                                   │                                 │
│   Like a bucket of water 🪣       │    Like a flowing pipe 🚰       │
│                                   │                                 │
└─────────────────────────────────────────────────────────────────────┘
```

---

## 2. Why Streams? (The Motivation)

### The Problem: Imperative Code is Verbose

Let's say we want to: **Find all names starting with 'A', convert to uppercase, and collect them.**

**❌ BEFORE (Imperative - Java 7 style):**
```java
List<String> names = Arrays.asList("Alice", "Bob", "Anna", "Charlie", "Alex");

// Find names starting with 'A', uppercase them
List<String> result = new ArrayList<>();
for (String name : names) {
    if (name.startsWith("A")) {
        result.add(name.toUpperCase());
    }
}
// result = [ALICE, ANNA, ALEX]
```

**✅ AFTER (Declarative - Stream API):**
```java
List<String> result = names.stream()
    .filter(name -> name.startsWith("A"))
    .map(String::toUpperCase)
    .collect(Collectors.toList());
// result = [ALICE, ANNA, ALEX]
```

### Comparison Table

| Aspect | Imperative (for-loop) | Declarative (Stream) |
|--------|----------------------|---------------------|
| **Focus** | HOW to do it (loop mechanics) | WHAT to do |
| **Readability** | Have to trace through loop | Reads like English |
| **Mutability** | Modifies external list | Immutable pipeline |
| **Parallelism** | Manual thread management | `parallelStream()` |
| **Lines of code** | 5+ lines | 1-4 lines |

### Streams Read Like English!

```java
// "From the names, FILTER those starting with 'A', 
//  MAP them to uppercase, COLLECT into a list"

names.stream()
    .filter(name -> name.startsWith("A"))
    .map(String::toUpperCase)
    .collect(Collectors.toList());
```

---

## 3. Stream Creation

### From Collections (Most Common)

```java
List<String> list = Arrays.asList("a", "b", "c");
Stream<String> fromList = list.stream();

Set<Integer> set = new HashSet<>(Arrays.asList(1, 2, 3));
Stream<Integer> fromSet = set.stream();
```

### From Arrays

```java
String[] array = {"x", "y", "z"};
Stream<String> fromArray = Arrays.stream(array);

int[] intArray = {1, 2, 3, 4, 5};
IntStream fromIntArray = Arrays.stream(intArray);
```

### From Values Directly

```java
Stream<String> fromValues = Stream.of("one", "two", "three");

Stream<Integer> numbers = Stream.of(1, 2, 3, 4, 5);
```

### From Ranges (Primitive Streams)

```java
// Range: 1, 2, 3, 4 (exclusive end)
IntStream range = IntStream.range(1, 5);

// RangeClosed: 1, 2, 3, 4, 5 (inclusive end)
IntStream rangeClosed = IntStream.rangeClosed(1, 5);

// LongStream and DoubleStream available too
LongStream longRange = LongStream.range(1, 1000000);
```

### Infinite Streams

```java
// Generate infinite stream of random numbers
Stream<Double> randoms = Stream.generate(Math::random);

// Generate: 0, 1, 2, 3, 4, ...
Stream<Integer> counting = Stream.iterate(0, n -> n + 1);

// ⚠️ MUST use limit() or other short-circuiting operation!
List<Integer> first10 = counting.limit(10).collect(Collectors.toList());
```

### Creation Summary

```
┌─────────────────────────────────────────────────────────────────┐
│                    STREAM CREATION METHODS                      │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│   Collection.stream()      → list.stream()                      │
│   Arrays.stream(array)     → Arrays.stream(arr)                 │
│   Stream.of(values)        → Stream.of(1, 2, 3)                 │
│   IntStream.range(a, b)    → IntStream.range(1, 10)             │
│   Stream.generate(supplier)→ Stream.generate(Math::random)      │
│   Stream.iterate(seed, fn) → Stream.iterate(0, n -> n + 1)      │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

---

## 4. Stream Operations Overview

### Two Types of Operations

```
┌─────────────────────────────────────────────────────────────────────┐
│               INTERMEDIATE vs TERMINAL OPERATIONS                  │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│   SOURCE → [Intermediate] → [Intermediate] → ... → [Terminal]      │
│               │                    │                    │           │
│               │                    │                    │           │
│               ▼                    ▼                    ▼           │
│         Returns Stream      Returns Stream       Returns Result    │
│         (can chain more)    (can chain more)     (ends the stream) │
│                                                                     │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│   INTERMEDIATE              │   TERMINAL                            │
│   (Lazy - don't run yet)    │   (Eager - triggers processing)       │
│   ─────────────────────     │   ──────────────────────────          │
│   filter()                  │   forEach()                           │
│   map()                     │   collect()                           │
│   sorted()                  │   reduce()                            │
│   distinct()                │   count()                             │
│   limit()                   │   findFirst() / findAny()             │
│   skip()                    │   anyMatch() / allMatch()             │
│   flatMap()                 │   min() / max()                       │
│                             │   toArray()                           │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```

### The Magic: Lazy Evaluation

Intermediate operations are **lazy** - they don't execute until a terminal operation is called!

```java
// ❌ NOTHING HAPPENS YET - no terminal operation!
Stream<String> stream = names.stream()
    .filter(name -> {
        System.out.println("Filtering: " + name);
        return name.startsWith("A");
    })
    .map(name -> {
        System.out.println("Mapping: " + name);
        return name.toUpperCase();
    });

// Nothing is printed yet!

// ✅ NOW processing happens!
stream.forEach(System.out::println);
// Filtering: Alice
// Mapping: Alice
// ALICE
// Filtering: Bob      (filtered out, no mapping)
// Filtering: Anna
// Mapping: Anna
// ANNA
// ...
```

### Why Lazy Evaluation is Powerful

```
┌─────────────────────────────────────────────────────────────────┐
│              LAZY EVALUATION BENEFITS                           │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│   1. SHORT-CIRCUITING                                           │
│      ─────────────────                                          │
│      findFirst() stops as soon as it finds one match            │
│      No need to process the entire stream!                      │
│                                                                 │
│   2. LOOP FUSION                                                │
│      ─────────────                                              │
│      filter().map().forEach() = ONE pass through data           │
│      Not: filter all → then map all → then forEach all          │
│                                                                 │
│   3. MEMORY EFFICIENCY                                          │
│      ─────────────────                                          │
│      Elements processed one at a time                           │
│      No intermediate collections created                        │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

### Visualization: Loop Fusion

Without fusion (multiple passes):
```
[1,2,3,4,5] → filter → [2,4] (stored) → map → [20,40] (stored) → forEach
```

With fusion (stream - single pass):
```
1 → filter(skip) 
2 → filter(keep) → map(20) → forEach(print 20)
3 → filter(skip)
4 → filter(keep) → map(40) → forEach(print 40)
5 → filter(skip)
```

---

## 5. Intermediate Operations (Deep Dive)

### 5.1 filter() - Keep What Matches

**Purpose:** Keep only elements that pass a test (Predicate)

```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

// Keep only even numbers
List<Integer> evens = numbers.stream()
    .filter(n -> n % 2 == 0)
    .collect(Collectors.toList());
// [2, 4, 6, 8, 10]

// Chain multiple filters
List<Integer> evenAndGreaterThan5 = numbers.stream()
    .filter(n -> n % 2 == 0)
    .filter(n -> n > 5)
    .collect(Collectors.toList());
// [6, 8, 10]
```

**Visual:**
```
   Input: [1, 2, 3, 4, 5, 6, 7, 8]
             │
             ▼
   filter(n -> n % 2 == 0)
             │
   ┌─────────┼─────────┐
   │    KEEP │ REJECT  │
   │  ───────┼──────── │
   │   2     │   1     │
   │   4     │   3     │
   │   6     │   5     │
   │   8     │   7     │
   └─────────┴─────────┘
             │
             ▼
   Output: [2, 4, 6, 8]
```

### 5.2 map() - Transform Each Element

**Purpose:** Convert each element from type T to type R (Function)

```java
List<String> words = Arrays.asList("hello", "world", "java");

// String → Integer (length)
List<Integer> lengths = words.stream()
    .map(String::length)
    .collect(Collectors.toList());
// [5, 5, 4]

// String → String (uppercase)
List<String> upper = words.stream()
    .map(String::toUpperCase)
    .collect(Collectors.toList());
// ["HELLO", "WORLD", "JAVA"]
```

**Visual:**
```
   Input: ["hello", "world", "java"]
               │
               ▼
   map(String::toUpperCase)
               │
   ┌───────────┼───────────┐
   │   INPUT   │   OUTPUT  │
   │  ─────────┼────────── │
   │  "hello"  →  "HELLO"  │
   │  "world"  →  "WORLD"  │
   │  "java"   →  "JAVA"   │
   └───────────┴───────────┘
               │
               ▼
   Output: ["HELLO", "WORLD", "JAVA"]
```

### 5.3 flatMap() - Flatten Nested Structures

**Purpose:** When map() produces streams, flatMap() flattens them into one stream

```java
// Problem: Each word is a stream of characters
List<String> words = Arrays.asList("Hello", "World");

// ❌ map() gives Stream<Stream<Character>> - nested!
// ✅ flatMap() gives Stream<Character> - flattened!

List<Character> allChars = words.stream()
    .flatMap(word -> word.chars()
        .mapToObj(c -> (char) c))
    .collect(Collectors.toList());
// ['H', 'e', 'l', 'l', 'o', 'W', 'o', 'r', 'l', 'd']
```

**Visual:**
```
   map() produces nested streams:
   ┌──────────────────────────────┐
   │ "Hello" → [H, e, l, l, o]    │
   │ "World" → [W, o, r, l, d]    │
   └──────────────────────────────┘
            Result: [[H,e,l,l,o], [W,o,r,l,d]]  ← NESTED

   flatMap() flattens them:
   ┌──────────────────────────────┐
   │ [H, e, l, l, o]              │
   │        +                     │
   │ [W, o, r, l, d]              │
   │        ↓                     │
   │ [H, e, l, l, o, W, o, r, l, d] ← FLAT!
   └──────────────────────────────┘
```

### 5.4 sorted() - Order Elements

**Purpose:** Sort elements (natural order or custom Comparator)

```java
List<String> names = Arrays.asList("Charlie", "Alice", "Bob", "David");

// Natural order (alphabetical)
List<String> sorted = names.stream()
    .sorted()
    .collect(Collectors.toList());
// ["Alice", "Bob", "Charlie", "David"]

// Custom order (by length)
List<String> byLength = names.stream()
    .sorted((a, b) -> Integer.compare(a.length(), b.length()))
    .collect(Collectors.toList());
// ["Bob", "Alice", "David", "Charlie"]

// Reverse order
List<String> reversed = names.stream()
    .sorted(Comparator.reverseOrder())
    .collect(Collectors.toList());
// ["David", "Charlie", "Bob", "Alice"]
```

### 5.5 distinct() - Remove Duplicates

**Purpose:** Keep only unique elements (uses equals())

```java
List<Integer> numbers = Arrays.asList(1, 2, 2, 3, 3, 3, 4, 4, 4, 4);

List<Integer> unique = numbers.stream()
    .distinct()
    .collect(Collectors.toList());
// [1, 2, 3, 4]
```

### 5.6 limit() and skip() - Control Quantity

```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);

// Take first 3
List<Integer> first3 = numbers.stream()
    .limit(3)
    .collect(Collectors.toList());
// [1, 2, 3]

// Skip first 3
List<Integer> skipFirst3 = numbers.stream()
    .skip(3)
    .collect(Collectors.toList());
// [4, 5, 6, 7, 8, 9, 10]

// Pagination: skip 2, then take 3 (page 2, size 3)
List<Integer> page2 = numbers.stream()
    .skip(3)  // skip page 1
    .limit(3) // take page 2
    .collect(Collectors.toList());
// [4, 5, 6]
```

**Visual:**
```
   [1, 2, 3, 4, 5, 6, 7, 8, 9, 10]
    └─────────┘ └─────────────────┘
     limit(3)         skip(3)
        │                │
        ▼                ▼
     [1,2,3]      [4,5,6,7,8,9,10]
```

### Intermediate Operations Summary

| Operation | Purpose | Input → Output | Lambda Type |
|-----------|---------|----------------|-------------|
| `filter()` | Keep matching | T → T (fewer) | `Predicate<T>` |
| `map()` | Transform | T → R | `Function<T,R>` |
| `flatMap()` | Flatten nested | T → Stream<R> | `Function<T, Stream<R>>` |
| `sorted()` | Order elements | T → T (ordered) | `Comparator<T>` |
| `distinct()` | Remove duplicates | T → T (unique) | - |
| `limit(n)` | Take first n | T → T (max n) | - |
| `skip(n)` | Skip first n | T → T (after n) | - |

---

## 6. Terminal Operations (Deep Dive)

### 6.1 forEach() - Do Something With Each

**Purpose:** Perform an action for each element (no return value)

```java
List<String> names = Arrays.asList("Alice", "Bob", "Charlie");

// Print each name
names.stream()
    .forEach(System.out::println);
// Alice
// Bob
// Charlie

// Custom action
names.stream()
    .forEach(name -> System.out.println("Hello, " + name));
```

**⚠️ NOTE:** forEach is for **side effects** only. Don't use it to build results!

### 6.2 collect() - Gather Results

**Purpose:** Accumulate elements into a collection or other result

```java
List<String> names = Arrays.asList("Alice", "Bob", "Charlie");

// To List
List<String> list = names.stream()
    .collect(Collectors.toList());

// To Set (removes duplicates)
Set<String> set = names.stream()
    .collect(Collectors.toSet());

// To Map
Map<String, Integer> nameLengths = names.stream()
    .collect(Collectors.toMap(
        name -> name,           // key
        name -> name.length()   // value
    ));
// {Alice=5, Bob=3, Charlie=7}

// Join to String
String joined = names.stream()
    .collect(Collectors.joining(", "));
// "Alice, Bob, Charlie"
```

### 6.3 reduce() - Combine All Elements

**Purpose:** Reduce all elements to a single value

```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);

// Sum (with identity)
int sum = numbers.stream()
    .reduce(0, (a, b) -> a + b);
// 15

// Product
int product = numbers.stream()
    .reduce(1, (a, b) -> a * b);
// 120

// Max (without identity - returns Optional)
Optional<Integer> max = numbers.stream()
    .reduce(Integer::max);
// Optional[5]
```

**Visual: How reduce() works**
```
   Input: [1, 2, 3, 4, 5]
   Operation: reduce(0, (a, b) -> a + b)

   Step 1:  0 + 1 = 1   (identity + first element)
   Step 2:  1 + 2 = 3
   Step 3:  3 + 3 = 6
   Step 4:  6 + 4 = 10
   Step 5: 10 + 5 = 15  ← RESULT!

   ┌───┐   ┌───┐   ┌───┐   ┌───┐   ┌───┐
   │ 0 │ + │ 1 │ = │ 1 │   │   │   │   │
   └───┘   └───┘   └─┬─┘   │   │   │   │
                     │     │   │   │   │
                     ▼     │   │   │   │
                   ┌───┐   │   │   │   │
               ┌───│ 1 │ + │ 2 │ = │ 3 │
               │   └───┘   └───┘   └─┬─┘
               │                     │
               │                     ▼
               │                   ┌───┐
               │               ┌───│ 3 │ + ...
               │               │   └───┘
               ▼               ▼
             Final: 15
```

### 6.4 count(), sum(), average() - Aggregate Operations

```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);

// Count
long count = numbers.stream().count();
// 5

// Sum (use IntStream for sum)
int sum = numbers.stream()
    .mapToInt(Integer::intValue)
    .sum();
// 15

// Average
OptionalDouble avg = numbers.stream()
    .mapToInt(Integer::intValue)
    .average();
// OptionalDouble[3.0]
```

### 6.5 findFirst() and findAny()

```java
List<String> names = Arrays.asList("Alice", "Bob", "Anna", "Charlie");

// Find first starting with 'A'
Optional<String> first = names.stream()
    .filter(name -> name.startsWith("A"))
    .findFirst();
// Optional["Alice"]

// Find any starting with 'A' (faster in parallel)
Optional<String> any = names.parallelStream()
    .filter(name -> name.startsWith("A"))
    .findAny();
// Optional["Alice"] or Optional["Anna"] - order not guaranteed
```

### 6.6 anyMatch(), allMatch(), noneMatch()

```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4, 5);

// Is any number even?
boolean anyEven = numbers.stream()
    .anyMatch(n -> n % 2 == 0);
// true

// Are all numbers positive?
boolean allPositive = numbers.stream()
    .allMatch(n -> n > 0);
// true

// Are there no negative numbers?
boolean noNegatives = numbers.stream()
    .noneMatch(n -> n < 0);
// true
```

### Terminal Operations Summary

| Operation | Returns | Purpose |
|-----------|---------|---------|
| `forEach()` | void | Side effects (print, etc.) |
| `collect()` | Collection/R | Gather into container |
| `reduce()` | Optional<T>/T | Combine all to one |
| `count()` | long | Count elements |
| `findFirst()` | Optional<T> | First element |
| `findAny()` | Optional<T> | Any element (parallel-friendly) |
| `anyMatch()` | boolean | Any element matches? |
| `allMatch()` | boolean | All elements match? |
| `noneMatch()` | boolean | No elements match? |
| `min()`/`max()` | Optional<T> | Smallest/largest element |

---

## 7. Collectors (Advanced)

### Common Collectors

```java
List<Person> people = Arrays.asList(
    new Person("Alice", 30, "Engineering"),
    new Person("Bob", 25, "Marketing"),
    new Person("Charlie", 35, "Engineering"),
    new Person("Diana", 28, "Marketing")
);
```

### groupingBy() - Group by Category

```java
// Group people by department
Map<String, List<Person>> byDept = people.stream()
    .collect(Collectors.groupingBy(Person::getDepartment));

// {Engineering=[Alice, Charlie], Marketing=[Bob, Diana]}
```

**Visual:**
```
   Input: [Alice(Eng), Bob(Mkt), Charlie(Eng), Diana(Mkt)]
                           │
                           ▼
             groupingBy(Person::getDepartment)
                           │
           ┌───────────────┴───────────────┐
           │                               │
           ▼                               ▼
   ┌─────────────────┐           ┌─────────────────┐
   │   Engineering   │           │    Marketing    │
   │ ───────────────-│           │ ───────────────-│
   │  Alice          │           │  Bob            │
   │  Charlie        │           │  Diana          │
   └─────────────────┘           └─────────────────┘
```

### partitioningBy() - Split into Two Groups

```java
// Partition by age >= 30
Map<Boolean, List<Person>> byAge = people.stream()
    .collect(Collectors.partitioningBy(p -> p.getAge() >= 30));

// {true=[Alice, Charlie], false=[Bob, Diana]}
```

### Downstream Collectors

```java
// Count people per department
Map<String, Long> countByDept = people.stream()
    .collect(Collectors.groupingBy(
        Person::getDepartment,
        Collectors.counting()
    ));
// {Engineering=2, Marketing=2}

// Average age per department
Map<String, Double> avgAgeByDept = people.stream()
    .collect(Collectors.groupingBy(
        Person::getDepartment,
        Collectors.averagingInt(Person::getAge)
    ));
// {Engineering=32.5, Marketing=26.5}
```

### Collectors Quick Reference

| Collector | Purpose | Example |
|-----------|---------|---------|
| `toList()` | To List | `collect(toList())` |
| `toSet()` | To Set | `collect(toSet())` |
| `toMap()` | To Map | `collect(toMap(k, v))` |
| `joining()` | Concatenate strings | `collect(joining(", "))` |
| `groupingBy()` | Group by key | `collect(groupingBy(fn))` |
| `partitioningBy()` | Split into true/false | `collect(partitioningBy(pred))` |
| `counting()` | Count elements | Downstream collector |
| `summingInt()` | Sum integers | Downstream collector |
| `averagingInt()` | Average of integers | Downstream collector |

---

## 8. Sequential vs Parallel Streams

### Sequential Streams (Default)

```java
List<Integer> numbers = Arrays.asList(1, 2, 3, 4);

// Sequential - uses single thread
numbers.stream()
    .forEach(n -> System.out.println(
        n + " - " + Thread.currentThread().getName()
    ));
// 1 - main
// 2 - main
// 3 - main
// 4 - main
```

### Parallel Streams

```java
// Parallel - uses multiple threads
numbers.parallelStream()
    .forEach(n -> System.out.println(
        n + " - " + Thread.currentThread().getName()
    ));
// 3 - ForkJoinPool.commonPool-worker-1
// 1 - main
// 4 - ForkJoinPool.commonPool-worker-2
// 2 - ForkJoinPool.commonPool-worker-3
```

**Visual: Sequential vs Parallel**
```
┌─────────────────────────────────────────────────────────────────┐
│                 SEQUENTIAL vs PARALLEL                          │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│   SEQUENTIAL (stream())                                         │
│   ─────────────────────                                         │
│                                                                 │
│   Thread 1: [1] → [2] → [3] → [4]  →  Done                      │
│                                                                 │
│   Time: ████████████████████████████  (long)                    │
│                                                                 │
├─────────────────────────────────────────────────────────────────┤
│                                                                 │
│   PARALLEL (parallelStream())                                   │
│   ───────────────────────────                                   │
│                                                                 │
│   Thread 1: [1] → [2]  →  Done                                  │
│   Thread 2: [3] → [4]  →  Done                                  │
│                                ↓                                │
│                           Merge results                         │
│                                                                 │
│   Time: ████████████████  (shorter!)                            │
│                                                                 │
└─────────────────────────────────────────────────────────────────┘
```

### When to Use Parallel Streams ✅

| Use Parallel When... | Example |
|---------------------|---------|
| Large dataset (10,000+ elements) | Processing millions of records |
| CPU-intensive operations | Complex calculations per element |
| Independent operations | No shared mutable state |
| Order doesn't matter | Statistical calculations |

### When NOT to Use Parallel Streams ❌

| Avoid Parallel When... | Why |
|------------------------|-----|
| Small dataset | Thread overhead > benefit |
| I/O operations | Threads block waiting for I/O |
| Shared mutable state | Race conditions! |
| Order-dependent operations | Unpredictable results |
| Already in multi-threaded env | Thread pool exhaustion |

**⚠️ WARNING: Parallel doesn't always mean faster!**
```java
// ❌ BAD: Side effects with shared state
List<Integer> results = new ArrayList<>();  // NOT thread-safe!
numbers.parallelStream()
    .map(n -> n * 2)
    .forEach(results::add);  // RACE CONDITION!

// ✅ GOOD: Let stream handle collection
List<Integer> results = numbers.parallelStream()
    .map(n -> n * 2)
    .collect(Collectors.toList());  // Thread-safe!
```

---

## 9. Common Mistakes & Gotchas

### Gotcha 1: Stream Reuse

**❌ Streams can only be consumed ONCE!**

```java
Stream<String> stream = names.stream();

stream.forEach(System.out::println);  // ✅ Works
stream.forEach(System.out::println);  // ❌ IllegalStateException!
```

**✅ Create a new stream for each operation:**
```java
names.stream().forEach(System.out::println);
names.stream().count();  // Fresh stream
```

### Gotcha 2: Side Effects in Streams

**❌ Don't modify external state:**
```java
List<String> results = new ArrayList<>();

names.stream()
    .filter(name -> name.startsWith("A"))
    .forEach(results::add);  // ❌ Side effect!
```

**✅ Use collect() instead:**
```java
List<String> results = names.stream()
    .filter(name -> name.startsWith("A"))
    .collect(Collectors.toList());  // ✅ Clean!
```

### Gotcha 3: Infinite Streams Without Limit

```java
// ❌ NEVER ENDS!
Stream.iterate(0, n -> n + 1)
    .forEach(System.out::println);

// ✅ Use limit()
Stream.iterate(0, n -> n + 1)
    .limit(10)
    .forEach(System.out::println);
```

### Gotcha 4: Order of Operations Matters

```java
// ❌ INEFFICIENT: sort first, then limit
numbers.stream()
    .sorted()              // Sort ALL elements
    .limit(10)             // Then take 10
    .collect(toList());

// ✅ For large datasets, consider different approach
// (Though for this specific case, you'd need a different algorithm)
```

### Gotcha 5: Debugging is Harder

**Use peek() to inspect pipeline:**
```java
List<String> result = names.stream()
    .filter(name -> name.startsWith("A"))
    .peek(name -> System.out.println("Filtered: " + name))
    .map(String::toUpperCase)
    .peek(name -> System.out.println("Mapped: " + name))
    .collect(Collectors.toList());
```

---

## 10. Practical Examples

### Example 1: Find Top 3 Highest Salaries

```java
List<Employee> employees = ...;

List<Employee> top3 = employees.stream()
    .sorted(Comparator.comparingDouble(Employee::getSalary).reversed())
    .limit(3)
    .collect(Collectors.toList());
```

### Example 2: Group and Count

```java
Map<String, Long> wordCounts = words.stream()
    .collect(Collectors.groupingBy(
        word -> word.toLowerCase(),
        Collectors.counting()
    ));
```

### Example 3: Flatten Nested Lists

```java
List<List<Integer>> nested = Arrays.asList(
    Arrays.asList(1, 2),
    Arrays.asList(3, 4),
    Arrays.asList(5, 6)
);

List<Integer> flat = nested.stream()
    .flatMap(List::stream)
    .collect(Collectors.toList());
// [1, 2, 3, 4, 5, 6]
```

### Example 4: CSV to Map

```java
String csv = "name:Alice,age:30,city:NYC";

Map<String, String> parsed = Arrays.stream(csv.split(","))
    .map(s -> s.split(":"))
    .collect(Collectors.toMap(
        arr -> arr[0],
        arr -> arr[1]
    ));
// {name=Alice, age=30, city=NYC}
```

---

## 11. Summary

### The Stream Pipeline

```
┌─────────────────────────────────────────────────────────────────────┐
│                    COMPLETE STREAM PIPELINE                         │
├─────────────────────────────────────────────────────────────────────┤
│                                                                     │
│   ┌────────────┐    ┌────────────────────┐    ┌────────────┐        │
│   │   SOURCE   │ →  │   INTERMEDIATE     │ →  │  TERMINAL  │        │
│   │            │    │    OPERATIONS      │    │ OPERATION  │        │
│   └────────────┘    └────────────────────┘    └────────────┘        │
│                                                                     │
│   • Collection       • filter()               • collect()           │
│   • Array            • map()                  • forEach()           │
│   • Stream.of()      • flatMap()              • reduce()            │
│   • Files.lines()    • sorted()               • count()             │
│                      • distinct()             • findFirst()         │
│                      • limit()/skip()         • anyMatch()          │
│                                                                     │
│   ──────────────────────────────────────────────────────────        │
│   LAZY (nothing happens)    │    EAGER (triggers processing)       │
│   Returns Stream            │    Returns result/void                │
│                                                                     │
└─────────────────────────────────────────────────────────────────────┘
```

### Key Takeaways

| Concept | Remember |
|---------|----------|
| **Stream** | Data pipeline, not a data structure |
| **Lazy** | Intermediate ops don't run until terminal |
| **Once** | Streams can only be consumed once |
| **Immutable** | Streams don't modify source |
| **Parallel** | Use only when appropriate |

### Quick Reference

```java
// Common patterns
list.stream()
    .filter(x -> condition)     // Keep matching
    .map(x -> transform(x))     // Transform each
    .sorted(comparator)         // Order elements
    .limit(n)                   // Take first n
    .collect(Collectors.toList());  // Gather results

// Reduce to single value
list.stream().reduce(identity, accumulator);

// Check conditions
list.stream().anyMatch(predicate);
list.stream().allMatch(predicate);

// Group data
list.stream().collect(Collectors.groupingBy(classifier));
```

---

> **Remember:** Streams are about **WHAT you want**, not HOW to get it.
> Think in terms of transformations, not loops!

> **Next Steps:** Practice combining operations to build complex pipelines!

Happy streaming! 🚀
