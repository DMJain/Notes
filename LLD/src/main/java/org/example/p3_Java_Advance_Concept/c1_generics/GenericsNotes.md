# Java Generics

> **Prerequisites:** Understanding of [Interfaces](../../p1_oops/c5_interfaces/InterfacesNotes.md), [Polymorphism](../../p1_oops/c4_polymorphism/PolymorphismNotes.md), and [Lambdas](../c2_lambdas/LambdasNotes.md)

---

## What You'll Learn

By the end of this chapter, you'll understand:
- **WHAT** generics are (parameterized types)
- **WHY** they were introduced (type safety without code duplication)
- **WHEN** to use them (and when NOT to)
- **HOW** they work under the hood (type erasure)

---

## 1. Why Do Generics Exist? (The Problem)

### The Dark Ages: Pre-Java 5 Collections

Before Java 5 (2004), collections stored everything as `Object`. This caused **two major problems**:

**Problem 1: No Compile-Time Type Safety**

```java
// 😰 BEFORE Java 5: ArrayList stores Objects
ArrayList list = new ArrayList();
list.add("Hello");
list.add(123);        // ← No error! Mixed types allowed
list.add(new Dog());  // ← Also allowed!

// Later... BOOM! 💥
String s = (String) list.get(1);  // ClassCastException at RUNTIME!
```

**Problem 2: Ugly Casting Everywhere**

```java
// Every time you retrieve, you MUST cast
ArrayList names = new ArrayList();
names.add("Alice");
names.add("Bob");

// Tedious and error-prone
String first = (String) names.get(0);  // Cast required
String second = (String) names.get(1); // Cast required
```

```
┌───────────────────────────────────────────────────────────────────┐
│                  The Pre-Generics Nightmare                        │
├───────────────────────────────────────────────────────────────────┤
│                                                                    │
│   ArrayList (stores Object)                                        │
│   ┌─────────┬─────────┬─────────┬─────────┐                       │
│   │ "Hello" │   123   │  Dog()  │  null   │                       │
│   │ String  │ Integer │   Dog   │   ???   │                       │
│   └─────────┴─────────┴─────────┴─────────┘                       │
│        ↑                                                           │
│        │                                                           │
│   String s = (String) list.get(0);  // Works!                     │
│   String s = (String) list.get(1);  // 💥 CRASH! Integer ≠ String │
│                                                                    │
│   ❌ No way to know what's inside at compile time                 │
│   ❌ Casts can fail at runtime                                    │
│   ❌ No IDE autocomplete help                                     │
│                                                                    │
└───────────────────────────────────────────────────────────────────┘
```

### The Solution: Generics (Java 5)

**The Generic Way:**

```java
// 😎 AFTER Java 5: Type-safe ArrayList
ArrayList<String> names = new ArrayList<String>();
names.add("Alice");
names.add("Bob");
// names.add(123);  // ❌ COMPILE ERROR! Only Strings allowed

// No casting needed!
String first = names.get(0);  // Compiler knows it's String
String second = names.get(1); // Safe and clean
```

```
┌───────────────────────────────────────────────────────────────────┐
│                  With Generics: Type Safety!                       │
├───────────────────────────────────────────────────────────────────┤
│                                                                    │
│   ArrayList<String> (stores ONLY String)                           │
│   ┌─────────┬─────────┬─────────┐                                 │
│   │ "Alice" │  "Bob"  │"Charlie"│                                 │
│   │ String  │ String  │ String  │  ← All same type, guaranteed!   │
│   └─────────┴─────────┴─────────┘                                 │
│                                                                    │
│   ✅ Compiler checks types at compile time                        │
│   ✅ No casts needed when retrieving                              │
│   ✅ IDE provides accurate autocomplete                           │
│   ✅ Bugs caught EARLY, not in production!                        │
│                                                                    │
└───────────────────────────────────────────────────────────────────┘
```

### Real-World Analogy

Think of a filing cabinet with labeled drawers:

| Approach | Analogy |
|----------|---------|
| **Without Generics** | One big drawer labeled "STUFF" - contains documents, snacks, keys, random objects. Every time you reach in, you might grab anything! |
| **With Generics** | Separate labeled drawers: "DOCUMENTS", "KEYS", "PHOTOS". You always know what you'll get! |

---

## 2. Generic Classes

### Definition

A **generic class** is a class that accepts one or more **type parameters**. The type is specified when you create an instance.

### Anatomy of a Generic Class

```
┌──────────────────────────────────────────────────────────────┐
│               Generic Class Structure                         │
├──────────────────────────────────────────────────────────────┤
│                                                               │
│   public class Box<T> {                                       │
│                   ↑                                           │
│                   └── Type Parameter (placeholder)            │
│                                                               │
│       private T content;    ← T used as field type           │
│                                                               │
│       public void add(T item) { ... }  ← T as parameter      │
│                                        ↑                      │
│       public T get() { ... }           │                      │
│              ↑                         │                      │
│              └── T as return type      │                      │
│                                        │                      │
│   }                                    │                      │
│                                        │                      │
│   Box<String> stringBox = new Box<>(); │                      │
│       ↑              ↑                 │                      │
│       │              └── Diamond operator (Java 7+)           │
│       └── T becomes String everywhere                         │
│                                                               │
└──────────────────────────────────────────────────────────────┘
```

### Basic Example: The Box<T>

```java
public class Box<T> {
    private T content;

    public void add(T content) {
        this.content = content;
    }

    public T get() {
        return content;
    }
    
    public boolean isEmpty() {
        return content == null;
    }
}
```

**Usage:**

```java
// String box
Box<String> stringBox = new Box<>();
stringBox.add("Hello, Generics!");
String message = stringBox.get();  // No casting!

// Integer box
Box<Integer> intBox = new Box<>();
intBox.add(42);
int number = intBox.get();  // Autoboxing/unboxing works!

// Custom type box
Box<Person> personBox = new Box<>();
personBox.add(new Person("Alice", 25));
Person p = personBox.get();
```

### Multiple Type Parameters

```java
public class Pair<K, V> {
    private K key;
    private V value;

    public Pair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public K getKey() { return key; }
    public V getValue() { return value; }
}

// Usage
Pair<String, Integer> entry = new Pair<>("age", 25);
String key = entry.getKey();      // "age"
Integer value = entry.getValue(); // 25
```

### Common Type Parameter Names

| Letter | Convention | Example |
|--------|------------|---------|
| `T` | Type | `Box<T>`, `List<T>` |
| `E` | Element | `Collection<E>` |
| `K` | Key | `Map<K, V>` |
| `V` | Value | `Map<K, V>` |
| `N` | Number | `NumberBox<N>` |
| `R` | Return | `Function<T, R>` |

### Before vs After Comparison

```
┌───────────────────────────────────────────────────────────────────┐
│                    Before vs After Generics                        │
├─────────────────────────────┬─────────────────────────────────────┤
│     BEFORE (Object-based)   │      AFTER (Generics)               │
├─────────────────────────────┼─────────────────────────────────────┤
│                             │                                      │
│ class Box {                 │ class Box<T> {                       │
│   private Object content;   │   private T content;                 │
│                             │                                      │
│   void add(Object item) {   │   void add(T item) {                 │
│     content = item;         │     content = item;                  │
│   }                         │   }                                  │
│                             │                                      │
│   Object get() {            │   T get() {                          │
│     return content;         │     return content;                  │
│   }                         │   }                                  │
│ }                           │ }                                    │
│                             │                                      │
│ // Usage:                   │ // Usage:                            │
│ Box box = new Box();        │ Box<String> box = new Box<>();       │
│ box.add("hello");           │ box.add("hello");                    │
│ String s = (String)         │ String s = box.get();                │
│             box.get();      │ // No cast! ✅                       │
│                             │                                      │
│ ❌ Runtime errors possible  │ ✅ Compile-time safety               │
│ ❌ Casting required         │ ✅ No casting needed                 │
│ ❌ No IDE help              │ ✅ Full autocomplete                 │
│                             │                                      │
└─────────────────────────────┴─────────────────────────────────────┘
```

---

## 3. Generic Methods

### What Are Generic Methods?

A **generic method** is a method that declares its own type parameter(s). The type parameter is declared **before the return type**.

### Why Use Generic Methods?

Sometimes you need a generic method inside a **non-generic class**, or you want a method's type parameter to be **independent** of the class's type parameter.

### Anatomy of a Generic Method

```
┌──────────────────────────────────────────────────────────────┐
│               Generic Method Structure                        │
├──────────────────────────────────────────────────────────────┤
│                                                               │
│   public <E> void printArray(E[] array) { ... }              │
│          ↑   ↑         ↑       ↑                             │
│          │   │         │       └── Parameter uses E          │
│          │   │         └── Method name                       │
│          │   └── Return type (void)                          │
│          └── Type parameter declaration                       │
│                                                               │
│   ┌─────────────────────────────────────────────────────┐    │
│   │ IMPORTANT: <E> comes BEFORE return type!            │    │
│   │                                                     │    │
│   │ ❌ public void <E> printArray(E[] array)  // WRONG  │    │
│   │ ✅ public <E> void printArray(E[] array)  // RIGHT  │    │
│   └─────────────────────────────────────────────────────┘    │
│                                                               │
└──────────────────────────────────────────────────────────────┘
```

### Examples

**Example 1: Print Any Array**

```java
public class ArrayUtils {
    
    // Generic method - works with ANY type of array
    public static <T> void printArray(T[] array) {
        for (T element : array) {
            System.out.print(element + " ");
        }
        System.out.println();
    }
}

// Usage
Integer[] intArray = {1, 2, 3, 4, 5};
String[] stringArray = {"apple", "banana", "orange"};

ArrayUtils.printArray(intArray);    // 1 2 3 4 5
ArrayUtils.printArray(stringArray); // apple banana orange
```

**Example 2: Swap Elements**

```java
public class SwapUtils {
    
    public static <T> void swap(T[] array, int i, int j) {
        T temp = array[i];
        array[i] = array[j];
        array[j] = temp;
    }
}

// Usage
String[] names = {"Alice", "Bob", "Charlie"};
SwapUtils.swap(names, 0, 2);  // Now: {"Charlie", "Bob", "Alice"}
```

**Example 3: Return Generic Type**

```java
public class GenericUtils {
    
    public static <T> T getFirst(List<T> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        return list.get(0);
    }
    
    public static <T> T getMiddle(T[] array) {
        return array[array.length / 2];
    }
}

// Usage - Type is inferred!
List<String> names = Arrays.asList("Alice", "Bob");
String first = GenericUtils.getFirst(names);  // "Alice"

Integer[] numbers = {1, 2, 3, 4, 5};
Integer middle = GenericUtils.getMiddle(numbers);  // 3
```

### Static vs Instance Generic Methods

```java
public class Example<T> {
    
    // Instance method using CLASS type parameter
    public T getInstance() {
        // Uses T from class
        return null;
    }
    
    // Static method CANNOT use class T!
    // Must declare its own type parameter
    public static <U> U getStatic(U item) {
        return item;
    }
}
```

**Why can't static methods use class type parameters?**

```
┌───────────────────────────────────────────────────────────────────┐
│         Why Static Methods Need Their Own Type Parameters          │
├───────────────────────────────────────────────────────────────────┤
│                                                                    │
│   class Box<T> {                                                   │
│       T content;                    ← Exists per INSTANCE          │
│                                                                    │
│       // ❌ WRONG - T belongs to instances, not the class         │
│       static T createDefault() { }                                 │
│                                                                    │
│       // ✅ RIGHT - Static declares its own parameter             │
│       static <U> U createDefault() { }                            │
│   }                                                                │
│                                                                    │
│   Box<String> box1 = new Box<>();   // T = String for box1        │
│   Box<Integer> box2 = new Box<>();  // T = Integer for box2       │
│                                                                    │
│   // Static method doesn't belong to any instance,                │
│   // so which T should it use? String? Integer?                   │
│   // Answer: Neither! It must declare its own type.               │
│                                                                    │
└───────────────────────────────────────────────────────────────────┘
```

---

## 4. Wildcards in Generics

### What is a Wildcard?

The **wildcard** (`?`) represents an **unknown type**. It's used when you want to work with generics but don't care about the specific type.

### Why Do Wildcards Exist?

**The Problem:**

```java
public void printList(List<Object> list) {
    for (Object item : list) {
        System.out.println(item);
    }
}

List<String> strings = Arrays.asList("a", "b", "c");
printList(strings);  // ❌ COMPILE ERROR!
```

**Wait, what?** `String` is a subtype of `Object`, so why doesn't this work?

```
┌───────────────────────────────────────────────────────────────────┐
│           Why List<String> is NOT a List<Object>                   │
├───────────────────────────────────────────────────────────────────┤
│                                                                    │
│   Object                          List<Object>                     │
│      ↑                                 ≠                           │
│      │ (is-a)                    List<String>                      │
│   String                                                           │
│                                                                    │
│   WHY? Because of what you could DO:                               │
│                                                                    │
│   List<String> strings = new ArrayList<>();                        │
│   List<Object> objects = strings;  // If this were allowed...      │
│   objects.add(123);                // You could add Integer!       │
│   String s = strings.get(0);       // 💥 ClassCastException!       │
│                                                                    │
│   Generics are INVARIANT to prevent this corruption!               │
│                                                                    │
└───────────────────────────────────────────────────────────────────┘
```

### The Three Wildcards

```
┌───────────────────────────────────────────────────────────────────┐
│                    The Three Wildcards                             │
├────────────────────┬──────────────────────────────────────────────┤
│     Wildcard       │            Meaning                           │
├────────────────────┼──────────────────────────────────────────────┤
│                    │                                               │
│   <?>              │   "Any type" - unbounded                     │
│                    │   Can read as Object, cannot write           │
│                    │                                               │
├────────────────────┼──────────────────────────────────────────────┤
│                    │                                               │
│   <? extends T>    │   "T or any subtype of T" - upper bound      │
│                    │   PRODUCER: Can read as T, cannot write      │
│                    │                                               │
├────────────────────┼──────────────────────────────────────────────┤
│                    │                                               │
│   <? super T>      │   "T or any supertype of T" - lower bound    │
│                    │   CONSUMER: Can write T, read only as Object │
│                    │                                               │
└────────────────────┴──────────────────────────────────────────────┘
```

### 4.1 Unbounded Wildcard: `<?>`

Use when you only need to read as `Object` or don't care about the type at all.

```java
// Works with List of ANY type
public void printList(List<?> list) {
    for (Object item : list) {
        System.out.println(item);
    }
}

List<String> strings = Arrays.asList("a", "b");
List<Integer> numbers = Arrays.asList(1, 2, 3);

printList(strings);   // ✅ Works!
printList(numbers);   // ✅ Works!
```

**What you CAN'T do with `<?>`:**

```java
public void addToList(List<?> list) {
    // list.add("hello");  // ❌ COMPILE ERROR!
    // list.add(123);      // ❌ COMPILE ERROR!
    list.add(null);        // ✅ Only null is allowed
}
```

### 4.2 Upper Bounded Wildcard: `<? extends T>`

Use when you want to **read** from a collection of T or its subtypes.

```java
// Accepts List<Number>, List<Integer>, List<Double>, etc.
public double sumOfList(List<? extends Number> list) {
    double sum = 0;
    for (Number n : list) {
        sum += n.doubleValue();  // Can call Number methods!
    }
    return sum;
}

List<Integer> integers = Arrays.asList(1, 2, 3);
List<Double> doubles = Arrays.asList(1.5, 2.5);

sumOfList(integers);  // ✅ Returns 6.0
sumOfList(doubles);   // ✅ Returns 4.0
```

**Real-World Example: Processing Buildings**

```java
class Building {
    void paint() { System.out.println("Painting building"); }
}

class House extends Building {
    void addGarden() { System.out.println("Adding garden"); }
}

// ❌ This only works with exactly Building
public void paintBuildings(List<Building> buildings) {
    buildings.forEach(Building::paint);
}

// ✅ This works with Building OR any subtype!
public void paintAllBuildings(List<? extends Building> buildings) {
    buildings.forEach(Building::paint);
}

List<House> houses = Arrays.asList(new House(), new House());
paintAllBuildings(houses);  // ✅ Works!
```

### 4.3 Lower Bounded Wildcard: `<? super T>`

Use when you want to **write** to a collection.

```java
// Can add Integer to List<Integer>, List<Number>, List<Object>
public void addNumbers(List<? super Integer> list) {
    list.add(1);
    list.add(2);
    list.add(3);
}

List<Integer> integers = new ArrayList<>();
List<Number> numbers = new ArrayList<>();
List<Object> objects = new ArrayList<>();

addNumbers(integers);  // ✅ Works!
addNumbers(numbers);   // ✅ Works!
addNumbers(objects);   // ✅ Works!
```

### The PECS Principle

**P**roducer **E**xtends, **C**onsumer **S**uper

```
┌─────────────────────────────────────────────────────────────────────┐
│                         PECS Principle                               │
├──────────────────────────────┬──────────────────────────────────────┤
│     PRODUCER (extends)       │      CONSUMER (super)                │
│     "I produce/give data"    │      "I consume/receive data"        │
├──────────────────────────────┼──────────────────────────────────────┤
│                              │                                       │
│   List<? extends Number>     │   List<? super Integer>              │
│                              │                                       │
│   ┌────────────────────┐     │   ┌────────────────────┐             │
│   │    Producer        │     │   │    Consumer        │             │
│   │  ┌──────────────┐  │     │   │  ┌──────────────┐  │             │
│   │  │ Integer(1)   │───────────▶│  │    ???       │  │             │
│   │  │ Double(2.5)  │  │     │   │  │    ???       │  │             │
│   │  │ Float(3.0f)  │  │     │   │  │    ???       │  │             │
│   │  └──────────────┘  │     │   │  └──────────────┘  │             │
│   └────────────────────┘     │   └────────────────────┘             │
│                              │                                       │
│   ✅ Can READ as Number      │   ✅ Can WRITE Integer               │
│   ❌ Cannot WRITE            │   ❌ Can only READ as Object         │
│                              │                                       │
│   Use for: source.get()      │   Use for: dest.add()                │
│                              │                                       │
└──────────────────────────────┴──────────────────────────────────────┘
```

**Real Example: Collections.copy()**

```java
// From Java's Collections class
public static <T> void copy(List<? super T> dest, List<? extends T> src) {
    for (T item : src) {      // src PRODUCES items (extends)
        dest.add(item);       // dest CONSUMES items (super)
    }
}

List<Number> destination = new ArrayList<>();
List<Integer> source = Arrays.asList(1, 2, 3);

Collections.copy(destination, source);  // ✅ Works!
```

### When to Use Which Wildcard?

| Scenario | Wildcard | Example |
|----------|----------|---------|
| Only reading | `? extends T` | `sumOfList(List<? extends Number>)` |
| Only writing | `? super T` | `addNumbers(List<? super Integer>)` |
| Both reading AND writing | No wildcard | `process(List<T>)` |
| Don't care about type | `?` | `printList(List<?>)` |

---

## 5. Bounded Generics

### What Are Bounded Generics?

**Bounded generics** restrict the types that can be used as type arguments.

### Upper Bound: `<T extends SomeClass>`

The type must be `SomeClass` or a **subclass** of it.

```java
public class NumberBox<T extends Number> {
    private T content;

    public void setContent(T content) {
        this.content = content;
    }

    public double getDoubleValue() {
        // Because T extends Number, we KNOW it has doubleValue()!
        return content.doubleValue();
    }
}

// Usage
NumberBox<Integer> intBox = new NumberBox<>();  // ✅ Integer extends Number
NumberBox<Double> doubleBox = new NumberBox<>(); // ✅ Double extends Number
// NumberBox<String> stringBox = new NumberBox<>(); // ❌ COMPILE ERROR!
```

### Why Use Bounded Generics?

Without bounds, you can only use `Object` methods:

```java
// Unbounded - can only use Object methods
public class Box<T> {
    private T content;
    
    public void printInfo() {
        // Only Object methods available
        System.out.println(content.toString());
        System.out.println(content.hashCode());
        // content.doubleValue(); // ❌ Can't call this!
    }
}

// Bounded - can use Number methods
public class NumberBox<T extends Number> {
    private T content;
    
    public void printInfo() {
        System.out.println(content.doubleValue());  // ✅ Available!
        System.out.println(content.intValue());     // ✅ Available!
    }
}
```

### Multiple Bounds

Type parameters can have **multiple bounds** using `&`:

```java
// T must extend Number AND implement Comparable
public class SortableNumberBox<T extends Number & Comparable<T>> {
    private T content;

    public boolean isGreaterThan(T other) {
        return content.compareTo(other) > 0;  // From Comparable
    }

    public double getDouble() {
        return content.doubleValue();  // From Number
    }
}
```

```
┌───────────────────────────────────────────────────────────────────┐
│                     Multiple Bounds Rules                          │
├───────────────────────────────────────────────────────────────────┤
│                                                                    │
│   <T extends Class1 & Interface1 & Interface2>                     │
│                                                                    │
│   Rules:                                                           │
│   1. At most ONE class bound (must come FIRST)                    │
│   2. Multiple interface bounds allowed (after class)              │
│   3. Use & to separate bounds                                      │
│                                                                    │
│   Examples:                                                        │
│   ✅ <T extends Number & Comparable<T>>                           │
│   ✅ <T extends Object & Serializable & Comparable<T>>            │
│   ✅ <T extends Comparable<T> & Serializable>  // Interfaces only │
│                                                                    │
│   ❌ <T extends Number & String>  // Two classes not allowed!      │
│   ❌ <T extends Comparable<T> & Number>  // Class must come first │
│                                                                    │
└───────────────────────────────────────────────────────────────────┘
```

### Bounded Generic Methods

```java
public class MathUtils {
    
    // T must be Comparable to itself
    public static <T extends Comparable<T>> T findMax(T[] array) {
        if (array == null || array.length == 0) return null;
        
        T max = array[0];
        for (T item : array) {
            if (item.compareTo(max) > 0) {
                max = item;
            }
        }
        return max;
    }
}

// Usage
Integer[] numbers = {3, 1, 4, 1, 5, 9};
Integer max = MathUtils.findMax(numbers);  // 9

String[] words = {"apple", "zebra", "banana"};
String maxWord = MathUtils.findMax(words);  // "zebra"
```

---

## 6. Generic Interfaces

### What Are Generic Interfaces?

Interfaces can also have type parameters, just like classes.

```java
public interface Pair<K, V> {
    K getKey();
    V getValue();
}
```

### Implementing Generic Interfaces

**Option 1: Preserve the type parameters**

```java
public class OrderedPair<K, V> implements Pair<K, V> {
    private K key;
    private V value;

    public OrderedPair(K key, V value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public K getKey() { return key; }

    @Override
    public V getValue() { return value; }
}

// Usage - specify types when creating
OrderedPair<String, Integer> age = new OrderedPair<>("Alice", 25);
```

**Option 2: Specify concrete types**

```java
public class StringIntPair implements Pair<String, Integer> {
    private String key;
    private Integer value;

    public StringIntPair(String key, Integer value) {
        this.key = key;
        this.value = value;
    }

    @Override
    public String getKey() { return key; }

    @Override
    public Integer getValue() { return value; }
}

// Usage - types already fixed
StringIntPair entry = new StringIntPair("count", 42);
```

### Common Java Generic Interfaces

| Interface | Type Parameters | Purpose |
|-----------|-----------------|---------|
| `List<E>` | Element | Ordered collection |
| `Map<K, V>` | Key, Value | Key-value mapping |
| `Comparable<T>` | Type to compare | Natural ordering |
| `Comparator<T>` | Type to compare | Custom ordering |
| `Function<T, R>` | Input, Return | Transform T to R |
| `Predicate<T>` | Input | Test condition |

### Real-World Example: Repository Pattern

```java
// Generic repository interface
public interface IRepository<T, ID> {
    T findById(ID id);
    List<T> findAll();
    void save(T entity);
    void delete(ID id);
}

// Implementation for User entity
public class UserRepository implements IRepository<User, Long> {
    private Map<Long, User> storage = new HashMap<>();

    @Override
    public User findById(Long id) {
        return storage.get(id);
    }

    @Override
    public List<User> findAll() {
        return new ArrayList<>(storage.values());
    }

    @Override
    public void save(User entity) {
        storage.put(entity.getId(), entity);
    }

    @Override
    public void delete(Long id) {
        storage.remove(id);
    }
}

// Implementation for Product entity
public class ProductRepository implements IRepository<Product, String> {
    // Same pattern, different types!
    // ...
}
```

```
┌───────────────────────────────────────────────────────────────────┐
│              Generic Interface Power: Repository Pattern           │
├───────────────────────────────────────────────────────────────────┤
│                                                                    │
│   IRepository<T, ID>         ← Generic contract                   │
│          │                                                         │
│          ├──────────────────────────────────────────┐              │
│          │                                          │              │
│          ▼                                          ▼              │
│   UserRepository                        ProductRepository          │
│   implements                            implements                 │
│   IRepository<User, Long>               IRepository<Product, String>
│                                                                    │
│   - findById(Long)                      - findById(String)         │
│   - save(User)                          - save(Product)            │
│                                                                    │
│   ONE interface, MANY type-safe implementations! 🎉               │
│                                                                    │
└───────────────────────────────────────────────────────────────────┘
```

---

## 7. Type Erasure

### What is Type Erasure?

**Type erasure** is how Java implements generics. The compiler:
1. Checks types at **compile time**
2. **Removes** (erases) all type information at **runtime**
3. Inserts **casts** where necessary

### Why Type Erasure?

**Backward compatibility!** Java had to work with existing non-generic code. If generic types existed at runtime, old code would break.

### How It Works

```
┌───────────────────────────────────────────────────────────────────┐
│                    Type Erasure Process                            │
├────────────────────────────┬──────────────────────────────────────┤
│     YOUR CODE              │     AFTER COMPILATION                │
├────────────────────────────┼──────────────────────────────────────┤
│                            │                                       │
│ class Box<T> {             │ class Box {                           │
│   private T content;       │   private Object content;             │
│                            │        ↑                              │
│   public T get() {         │   public Object get() {               │
│     return content;        │     return content;                   │
│   }                        │   }                                   │
│                            │        ↑                              │
│   public void set(T t) {   │   public void set(Object t) {         │
│     content = t;           │     content = t;                      │
│   }                        │   }                                   │
│ }                          │ }                                     │
│                            │                                       │
│ // Usage                   │ // What JVM actually sees             │
│ Box<String> box = new Box<>();                                    │
│ box.set("hello");          │ box.set("hello");                     │
│ String s = box.get();      │ String s = (String) box.get();        │
│                            │             ↑                         │
│                            │   Compiler inserts cast!              │
│                            │                                       │
└────────────────────────────┴──────────────────────────────────────┘
```

### Bounded Type Erasure

When you have bounds, the type is erased to the **first bound**:

```java
// Your code
class NumberBox<T extends Number> {
    private T value;
    public T get() { return value; }
}

// After erasure
class NumberBox {
    private Number value;  // Erased to Number, not Object!
    public Number get() { return value; }
}
```

### Implications and Limitations

#### 1. Cannot Use `instanceof` with Parameterized Types

```java
List<String> strings = new ArrayList<>();

// ❌ COMPILE ERROR - type info doesn't exist at runtime
if (strings instanceof ArrayList<String>) { }

// ✅ OK - check raw type
if (strings instanceof ArrayList) { }

// ✅ OK - unbounded wildcard
if (strings instanceof ArrayList<?>) { }
```

#### 2. Cannot Create Generic Arrays

```java
// ❌ COMPILE ERROR
T[] array = new T[10];

// ❌ COMPILE ERROR
List<String>[] arrayOfLists = new ArrayList<String>[10];

// ✅ WORKAROUND - use raw type and suppress warning
@SuppressWarnings("unchecked")
T[] array = (T[]) new Object[10];
```

#### 3. Cannot Create Instances of Type Parameters

```java
// ❌ COMPILE ERROR
public <T> T createInstance() {
    return new T();  // What constructor? T doesn't exist at runtime!
}

// ✅ WORKAROUND - use Class object
public <T> T createInstance(Class<T> clazz) throws Exception {
    return clazz.getDeclaredConstructor().newInstance();
}
```

#### 4. Cannot Use Primitives as Type Arguments

```java
// ❌ COMPILE ERROR
List<int> numbers = new ArrayList<>();

// ✅ Use wrapper class
List<Integer> numbers = new ArrayList<>();
```

### Bridge Methods

When a generic class extends a non-generic class, the compiler generates **bridge methods** to maintain polymorphism:

```java
public interface Comparable<T> {
    int compareTo(T o);
}

public class IntWrapper implements Comparable<IntWrapper> {
    private int value;
    
    @Override
    public int compareTo(IntWrapper other) {  // T = IntWrapper
        return Integer.compare(this.value, other.value);
    }
}

// After erasure, compiler generates:
/*
public int compareTo(Object other) {  // Bridge method
    return compareTo((IntWrapper) other);  // Delegates to typed method
}
*/
```

```
┌───────────────────────────────────────────────────────────────────┐
│                Type Erasure Limitations Summary                    │
├───────────────┬───────────────────────────────────────────────────┤
│ ❌ Cannot Do  │                   Reason                          │
├───────────────┼───────────────────────────────────────────────────┤
│ new T()       │ Type unknown at runtime                           │
│ new T[10]     │ Array stores type, but type is erased             │
│ instanceof T  │ Type info doesn't exist at runtime                │
│ List<int>     │ Primitives aren't objects                         │
│ T.class       │ Class object needs runtime type                   │
├───────────────┼───────────────────────────────────────────────────┤
│ ✅ Can Do     │                   How                             │
├───────────────┼───────────────────────────────────────────────────┤
│ Create via Class<T> │ Pass Class object as parameter             │
│ (T[]) new Object[n] │ Unchecked cast (with warning)              │
│ List<Integer> │ Use wrapper classes                               │
│ instanceof ?  │ Use unbounded wildcard                            │
└───────────────┴───────────────────────────────────────────────────┘
```

---

## 8. When to Use Generics (and When NOT To)

### ✅ USE Generics When:

| Scenario | Example |
|----------|---------|
| **Collections** | `List<User>`, `Map<String, Order>` |
| **Container classes** | `Box<T>`, `Optional<T>` |
| **Utility methods** | `<T> T findFirst(List<T> list)` |
| **Type-safe builders** | `Builder<ProductBuilder>` |
| **Repository pattern** | `IRepository<User, Long>` |
| **Callbacks/Handlers** | `Handler<RequestEvent>` |

### ❌ DON'T Use Generics When:

| Scenario | Why Not | Alternative |
|----------|---------|-------------|
| **Class has only Object methods** | No benefit | Use `Object` |
| **Single concrete type** | Over-engineering | Use concrete type |
| **Too many type parameters** | Unreadable | Simplify design |
| **Type not actually variable** | Misleading | Use fixed type |

### Example: When NOT to Use Generics

```java
// ❌ OVERKILL - username is ALWAYS String
public class User<T> {
    private T username;  // Why generic?
}

// ✅ BETTER - just use String
public class User {
    private String username;
}
```

---

## 9. Common Gotchas

### Gotcha 1: Raw Types

```java
// ❌ RAW TYPE - loses type safety
List list = new ArrayList();  // Warning!
list.add("hello");
list.add(123);  // No error, but defeats purpose

// ✅ PROPER GENERIC
List<String> list = new ArrayList<>();
list.add("hello");
// list.add(123);  // Compile error! ✅
```

### Gotcha 2: Generic Array Creation

```java
// ❌ CANNOT create generic arrays
List<String>[] array = new List<String>[10];  // Error!

// ✅ WORKAROUND 1: Use ArrayList of ArrayLists
List<List<String>> array = new ArrayList<>();

// ✅ WORKAROUND 2: Use raw type with warning suppression
@SuppressWarnings("unchecked")
List<String>[] array = (List<String>[]) new List[10];
```

### Gotcha 3: Overloading with Type Erasure

```java
public class Printer {
    // ❌ BOTH methods erase to print(List)!
    public void print(List<String> strings) { }
    public void print(List<Integer> integers) { }  // Compile error!
}

// ✅ FIX: Use different method names
public class Printer {
    public void printStrings(List<String> strings) { }
    public void printIntegers(List<Integer> integers) { }
}
```

### Gotcha 4: Static Context

```java
public class Container<T> {
    // ❌ Cannot use T in static context
    private static T value;  // Error!
    
    // ❌ Cannot use T in static method
    public static T getValue() { }  // Error!
    
    // ✅ Static methods must declare their own type parameter
    public static <U> U identity(U value) {
        return value;
    }
}
```

---

## 10. Summary

### Key Concepts Table

| Concept | Syntax | Purpose |
|---------|--------|---------|
| Generic Class | `class Box<T>` | Reusable type-safe container |
| Generic Method | `<T> void method(T t)` | Type-safe method |
| Unbounded Wildcard | `<?>` | Accept any type |
| Upper Bounded | `<? extends T>` | Accept T or subtypes (read) |
| Lower Bounded | `<? super T>` | Accept T or supertypes (write) |
| Bounded Type | `<T extends Number>` | Restrict allowed types |
| Type Erasure | Compile-time only | Backward compatibility |

### PECS Quick Reference

```
┌─────────────────────────────────────────────────────────────────────┐
│                     PECS: The Golden Rule                            │
├─────────────────────────────────────────────────────────────────────┤
│                                                                      │
│   If you want to GET (read) items → use EXTENDS                     │
│   If you want to PUT (write) items → use SUPER                      │
│   If you want BOTH → don't use wildcard, use <T>                    │
│                                                                      │
│   Memory trick: GET = E (getExtends), PUT = S (putSuper)            │
│                                                                      │
└─────────────────────────────────────────────────────────────────────┘
```

### The Big Picture

```
┌─────────────────────────────────────────────────────────────────────┐
│                    Java Generics Ecosystem                           │
├─────────────────────────────────────────────────────────────────────┤
│                                                                      │
│   ┌─────────────────┐                                                │
│   │ Generic Classes │     class Container<T> { ... }                 │
│   └────────┬────────┘                                                │
│            │                                                         │
│            ├──────────────┐                                          │
│            │              │                                          │
│   ┌────────▼─────┐  ┌────▼──────────┐                               │
│   │ Generic      │  │ Bounded       │                               │
│   │ Methods      │  │ Generics      │                               │
│   │ <T> T get()  │  │ <T extends X> │                               │
│   └──────────────┘  └───────────────┘                               │
│            │                                                         │
│            ▼                                                         │
│   ┌──────────────────────────────────────┐                          │
│   │        Wildcards (?, extends, super) │                          │
│   │     For flexible method parameters   │                          │
│   └──────────────────────────────────────┘                          │
│            │                                                         │
│            ▼                                                         │
│   ┌──────────────────────────────────────┐                          │
│   │         TYPE ERASURE                 │                          │
│   │   Compile-time checks → Runtime raw  │                          │
│   │   (Why: backward compatibility)      │                          │
│   └──────────────────────────────────────┘                          │
│                                                                      │
│   🎯 Result: TYPE SAFETY + CODE REUSE + READABLE APIS               │
│                                                                      │
└─────────────────────────────────────────────────────────────────────┘
```

---

## Next Steps

- Practice with the [runner scenarios](./runner/scenarios/)
- See generics in action with [Lambdas](../c2_lambdas/LambdasNotes.md) and [Streams](../c3_streams/StreamsNotes.md)
- Explore Java's generic collections in the `java.util` package
