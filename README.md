# Notes - The Ultimate Revision Hub

<p align="center">
  <strong>A comprehensive collection of LeetCode solutions, Low-Level Design patterns, and System Design concepts</strong>
</p>

<p align="center">
  <a href="#-leetcode-solutions">LeetCode</a> •
  <a href="#-low-level-design-lld">LLD</a> •
  <a href="#-high-level-design-hld">HLD</a> •
  <a href="#-resources">Resources</a>
</p>

---

## 🎯 What's Inside

| Section | Description | Status |
|---------|-------------|--------|
| **LeetCode** | Curated problem solutions with detailed explanations | ✅ Active |
| **LLD (Java)** | Object-Oriented Programming with Java examples | ✅ Active |
| **LLD (Node.js)** | OOP concepts implemented in JavaScript | ✅ Active |
| **LLD (Python)** | OOP patterns in Python | ✅ Active |
| **HLD** | High-Level Design & System Design | 🔜 Coming Soon |

---

## 💻 LeetCode Solutions

> Each problem includes: **Question**, **Solution Code**, and **Detailed Explanation**

### Problems by Topic

<details>
<summary><b>Arrays & Hashing</b></summary>

| # | Problem | Difficulty | Key Concept |
|---|---------|------------|-------------|
| 1 | [Two Sum](./LeetCode/src/main/java/org/example/Q0001_TwoSum) | Easy | HashMap lookup |

</details>

<details>
<summary><b>Strings & Substrings</b></summary>

| # | Problem | Difficulty | Key Concept |
|---|---------|------------|-------------|
| 3 | [Longest Substring Without Repeating Characters](./LeetCode/src/main/java/org/example/Q0003_LongestSubstringWithoutRepeatingCharacters) | Medium | Sliding Window |
| 521 | [Longest Uncommon Subsequence I](./LeetCode/src/main/java/org/example/Q0521_LongestUncommonSubsequenceI) | Easy | String Comparison |
| 522 | [Longest Uncommon Subsequence II](./LeetCode/src/main/java/org/example/Q0522_LongestUncommonSubsequenceII) | Medium | Subsequence Check |

</details>

<details>
<summary><b>Binary Search</b></summary>

| # | Problem | Difficulty | Key Concept |
|---|---------|------------|-------------|
| 4 | [Median of Two Sorted Arrays](./LeetCode/src/main/java/org/example/Q0004_MedianOfTwoSortedArray) | Hard | Binary Search on Answer |

</details>

<details>
<summary><b>Dynamic Programming</b></summary>

| # | Problem | Difficulty | Key Concept |
|---|---------|------------|-------------|
| 514 | [Freedom Trail](./LeetCode/src/main/java/org/example/Q0514_FreedomTrail) | Hard | DP + BFS |

</details>

<details>
<summary><b>Sliding Window</b></summary>

| # | Problem | Difficulty | Key Concept |
|---|---------|------------|-------------|
| 1423 | [Maximum Points from Cards](./LeetCode/src/main/java/org/example/Q1423_MaxPointsFromCards) | Medium | Fixed Window |

</details>

### Problems by Number

| # | Problem | Difficulty | Topics |
|---|---------|------------|--------|
| 1 | [Two Sum](./LeetCode/src/main/java/org/example/Q0001_TwoSum) | 🟢 Easy | Array, HashMap |
| 3 | [Longest Substring Without Repeating Characters](./LeetCode/src/main/java/org/example/Q0003_LongestSubstringWithoutRepeatingCharacters) | 🟡 Medium | String, Sliding Window |
| 4 | [Median of Two Sorted Arrays](./LeetCode/src/main/java/org/example/Q0004_MedianOfTwoSortedArray) | 🔴 Hard | Binary Search |
| 514 | [Freedom Trail](./LeetCode/src/main/java/org/example/Q0514_FreedomTrail) | 🔴 Hard | DP, BFS |
| 521 | [Longest Uncommon Subsequence I](./LeetCode/src/main/java/org/example/Q0521_LongestUncommonSubsequenceI) | 🟢 Easy | String |
| 522 | [Longest Uncommon Subsequence II](./LeetCode/src/main/java/org/example/Q0522_LongestUncommonSubsequenceII) | 🟡 Medium | String, Sorting |
| 1423 | [Maximum Points from Cards](./LeetCode/src/main/java/org/example/Q1423_MaxPointsFromCards) | 🟡 Medium | Sliding Window |

### Explanation Structure

Every problem follows a standardized explanation format:
1. **Problem in Simple Words** - Easy to understand problem statement
2. **Brute Force Approach** - With analysis of why it's suboptimal
3. **Intuitive/Greedy Approach** - Examples where it works and fails
4. **Optimal Solution** - With visualization and step-by-step walkthrough
5. **Complexity Analysis** - Time & Space for all approaches
6. **Key Takeaways** - Pattern recognition & what to remember

---

## Low-Level Design (LLD)

> Master Object-Oriented Programming with practical examples in **3 languages**

### OOP Fundamentals

The LLD section covers core OOP concepts with hands-on code examples:

| Chapter | Topic | Description |
|---------|-------|-------------|
| **c0** | [Introduction](./LLD/src/main/java/org/example/p1_oops/c0_introduction) | What is OOP? History, Why Abstraction matters |
| **c1** | [Access Modifiers](./LLD/src/main/java/org/example/p1_oops/c1_accessmodifiers) | public, private, protected, package-private |
| **c2** | [Constructors](./LLD/src/main/java/org/example/p1_oops/c2_constructors) | Default, Parameterized, Copy Constructors |
| **c3** | [Inheritance](./LLD/src/main/java/org/example/p1_oops/c3_inheritance) | extends, super, IS-A relationship |
| **c4** | [Polymorphism](./LLD/src/main/java/org/example/p1_oops/c4_polymorphism) | Method Overloading & Overriding |
| **c5** | [Interfaces](./LLD/src/main/java/org/example/p1_oops/c5_interfaces) | Contracts, Multiple Inheritance |
| **c6** | [Abstract Classes](./LLD/src/main/java/org/example/p1_oops/c6_abstract) | Partial Implementation, When to use |
| **c7** | [Composition](./LLD/src/main/java/org/example/p1_oops/c7_composition) | HAS-A relationship, Aggregation vs Composition |

### Key OOP Principle

```
                    ┌─────────────────────────┐
                    │      ABSTRACTION        │
                    │    (The Principle)      │
                    │  "Hide complexity,      │
                    │   show essentials"      │
                    └───────────┬─────────────┘
                                │
            ┌───────────────────┼───────────────────┐
            │                   │                   │
            ▼                   ▼                   ▼
    ┌───────────────┐   ┌───────────────┐   ┌───────────────┐
    │ ENCAPSULATION │   │  INHERITANCE  │   │ POLYMORPHISM  │
    │   (Pillar 1)  │   │   (Pillar 2)  │   │   (Pillar 3)  │
    └───────────────┘   └───────────────┘   └───────────────┘
```

### Multi-Language Support

| Language | Path | Status |
|----------|------|--------|
| ☕ **Java** | [LLD/](./LLD) | ✅ Complete |
| 🟨 **JavaScript (Node.js)** | [LLD NodeJs/](./LLD%20NodeJs) | ✅ Complete |
| 🐍 **Python** | [LLD Python/](./LLD%20Python) | ✅ Complete |

Each language implementation covers the same concepts with language-specific nuances and best practices.

---

## High-Level Design (HLD)

> System Design concepts, patterns, and case studies

### Coming Soon

- [ ] **System Design Fundamentals**
  - Scalability, Reliability, Availability
  - CAP Theorem
  - Load Balancing
  - Caching Strategies
  
- [ ] **Design Patterns**
  - Singleton, Factory, Builder
  - Observer, Strategy, Decorator
  
- [ ] **System Design Case Studies**
  - URL Shortener
  - Rate Limiter
  - Notification System
  - Chat Application

---

## Resources

> Curated collection of articles, books, blogs, and videos

### Coming Soon

<details>
<summary><b>Books</b></summary>

*To be added...*

</details>

<details>
<summary><b>Articles & Blogs</b></summary>

*To be added...*

</details>

<details>
<summary><b>Video Resources</b></summary>

*To be added...*

</details>

<details>
<summary><b>Useful Links</b></summary>

*To be added...*

</details>

---

## Getting Started

### Prerequisites

```bash
# For Java projects
Java 17+ (recommended: Java 21)
Maven 3.8+

# For Node.js projects
Node.js 18+

# For Python projects
Python 3.10+
```

### Running the Code

#### LeetCode (Java)
```bash
cd LeetCode
mvn compile
mvn exec:java -Dexec.mainClass="org.example.Main"
```

#### LLD Java
```bash
cd LLD
mvn compile
mvn exec:java -Dexec.mainClass="org.example.p1_oops.c0_introduction.Main"
```

#### LLD Node.js
```bash
cd "LLD NodeJs"
node p1_oops/c0_introduction/Main.js
```

#### LLD Python
```bash
cd "LLD Python"
python p1_oops/c0_introduction/main.py
```

---

## Project Structure

```
Notes/
├── 📂 LeetCode/                    # LeetCode solutions (Java)
│   └── src/main/java/org/example/
│       ├── Q0001_TwoSum/
│       │   ├── TwoSum.java         # Solution
│       │   ├── Question.md         # Problem statement
│       │   └── Explanation.md      # Detailed explanation
│       └── ...
│
├── 📂 LLD/                         # Low-Level Design (Java)
│   └── src/main/java/org/example/
│       └── p1_oops/
│           ├── c0_introduction/
│           ├── c1_accessmodifiers/
│           └── ...
│
├── 📂 LLD NodeJs/                  # Low-Level Design (JavaScript)
│   └── p1_oops/
│       ├── c0_introduction/
│       └── ...
│
├── 📂 LLD Python/                  # Low-Level Design (Python)
│   └── p1_oops/
│       ├── c0_introduction/
│       └── ...
│
└── 📄 README.md                    # You are here!
```

---

## Contributing

This is a personal learning repository. If you find any issues or have suggestions:

1. Feel free to open an issue
2. Suggestions for new problems/topics are welcome
3. Found a bug in an explanation? Let me know!

---

## Progress Tracker

### LeetCode
- [x] Easy: 2 solved
- [x] Medium: 3 solved
- [x] Hard: 2 solved
- **Total**: 7 problems

### LLD
- [x] OOP Fundamentals (8 chapters)
- [ ] Design Patterns (Coming Soon)
- [ ] SOLID Principles (Coming Soon)

### HLD
- [ ] System Design Basics (Coming Soon)
- [ ] Case Studies (Coming Soon)

---

<p align="center">
  <b>⭐ Star this repo if you find it helpful! ⭐</b>
</p>

<p align="center">
  Made with 💻 and ☕
</p>
