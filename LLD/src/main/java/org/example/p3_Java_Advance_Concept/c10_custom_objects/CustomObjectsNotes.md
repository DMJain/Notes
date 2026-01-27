# Custom Objects in Collections

> **Prerequisites:** [HashSet](../c6_set_interface/HashSetNotes.md), [HashMap](../c8_map_interface/HashMapNotes.md)

---

## What You'll Learn

| Question | Answer |
|----------|--------|
| **WHAT** | Proper implementation of hashCode/equals for custom objects |
| **WHY** | Required for HashSet/HashMap to work correctly |
| **WHEN** | Using custom classes as Set elements or Map keys |
| **HOW** | Override both hashCode() AND equals() following the contract |

---

## 1. The Problem

```java
class Person {
    String name;
    int age;
    Person(String name, int age) { this.name = name; this.age = age; }
}

Set<Person> set = new HashSet<>();
set.add(new Person("Alice", 25));
set.add(new Person("Alice", 25));  // Both added! 💥

System.out.println(set.size());  // 2, not 1!
// Why? Default hashCode/equals use memory address, not content
```

---

## 2. The Contract

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                   hashCode/equals CONTRACT                                   │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│   RULE 1: Consistency                                                        │
│   • If a.equals(b) → a.hashCode() == b.hashCode() (ALWAYS)                  │
│   • If hashCode differs → objects are NOT equal                             │
│                                                                              │
│   RULE 2: Reflexivity                                                        │
│   • a.equals(a) must be true                                                 │
│                                                                              │
│   RULE 3: Symmetry                                                           │
│   • a.equals(b) ↔ b.equals(a)                                               │
│                                                                              │
│   RULE 4: Transitivity                                                       │
│   • a.equals(b) && b.equals(c) → a.equals(c)                                │
│                                                                              │
│   RULE 5: Non-nullity                                                        │
│   • a.equals(null) must be false                                            │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```

---

## 3. Correct Implementation

```java
class Person {
    private final String name;
    private final int age;
    
    Person(String name, int age) {
        this.name = name;
        this.age = age;
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Person person = (Person) o;
        return age == person.age && Objects.equals(name, person.name);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(name, age);
    }
}

// Now works correctly!
Set<Person> set = new HashSet<>();
set.add(new Person("Alice", 25));
set.add(new Person("Alice", 25));  // Duplicate rejected!
System.out.println(set.size());  // 1 ✅
```

---

## 4. ⚠️ Critical Gotcha: Mutable Keys

```java
// ❌ DANGEROUS: Mutable object as key
class MutablePerson {
    String name;  // mutable!
    
    @Override
    public int hashCode() { return name.hashCode(); }
    @Override  
    public boolean equals(Object o) { /* based on name */ }
}

Map<MutablePerson, String> map = new HashMap<>();
MutablePerson p = new MutablePerson("Alice");
map.put(p, "value");

p.name = "Bob";  // 💥 Hash code changes!

map.get(p);  // Returns null! Can't find it anymore
// Entry is in wrong bucket, effectively lost
```

**Solution: Use immutable objects as keys!**

---

## 5. Summary

```
┌─────────────────────────────────────────────────────────────────────────────┐
│                   CUSTOM OBJECTS SUMMARY                                     │
├─────────────────────────────────────────────────────────────────────────────┤
│                                                                              │
│   FOR HASHSET/HASHMAP:                                                       │
│   ✅ Override BOTH hashCode() AND equals()                                  │
│   ✅ If equals → same hashCode (REQUIRED)                                   │
│   ✅ Use Objects.hash() / Objects.equals()                                  │
│   ✅ Make keys immutable                                                    │
│                                                                              │
│   FOR TREESET/TREEMAP:                                                       │
│   ✅ Implement Comparable OR provide Comparator                             │
│   ✅ compareTo() should be consistent with equals()                         │
│                                                                              │
│   GENERATE WITH IDE:                                                         │
│   Most IDEs can generate hashCode/equals automatically                      │
│   (IntelliJ: Alt+Insert → equals() and hashCode())                         │
│                                                                              │
└─────────────────────────────────────────────────────────────────────────────┘
```
