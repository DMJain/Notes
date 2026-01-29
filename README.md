# Notes - The Ultimate Revision Hub

<p align="center">
  <strong>A comprehensive collection of LeetCode solutions, Low-Level Design patterns, and System Design concepts</strong>
</p>

<p align="center">
  <a href="#leetcode-solutions">LeetCode</a> •
  <a href="#dsa-visualizer">Visualizer</a> •
  <a href="#low-level-design-lld">LLD</a> •
  <a href="#high-level-design-hld">HLD</a> •
  <a href="#resources">Resources</a>
</p>

---

## What's Inside

| Section | Description | Status |
|---------|-------------|--------|
| **LeetCode** | Curated problem solutions with detailed explanations | ✅ Active |
| **Visualizer** | Interactive step-by-step algorithm visualization | ✅ **NEW** |
| **LLD (Java)** | Object-Oriented Programming with Java examples | ✅ Active |
| **LLD (Node.js)** | OOP concepts implemented in JavaScript | ✅ Active |
| **LLD (Python)** | OOP patterns in Python | ✅ Active |
| **HLD** | High-Level Design & System Design | 🔜 Coming Soon |

---

## LeetCode Solutions

> Each problem includes: **Question**, **Solution Code**, and **Detailed Explanation**

### Problems by Topic

<details>
<summary><b>Arrays & Hashing (click to expand)</b></summary>

| # | Problem | Description | Q-Card |
|---|---------|-------------|--------|
| 1 | [Two Sum](./LeetCode/src/main/java/org/example/Q0001_TwoSum) | Find 2 nums that add to target | Store num→idx in map, check if complement exists |
| 15 | [3Sum](./LeetCode/src/main/java/org/example/Q0015_ThreeSum) | Find triplets summing to 0 | Sort + fix one, two-pointer for rest, skip dupes |
| 1200 | [Minimum Absolute Difference](./LeetCode/src/main/java/org/example/Q1200_MinimumAbsoluteDifference) | Find pairs with min absolute difference | Sort array → check adjacent differences |

</details>

<details>
<summary><b>Stack (click to expand)</b></summary>

| # | Problem | Description | Q-Card |
|---|---------|-------------|--------|
| 20 | [Valid Parentheses](./LeetCode/src/main/java/org/example/Q0020_ValidParentheses) | Check if brackets are valid | Stack: push open, pop matching close |
| 85 | [Maximal Rectangle](./LeetCode/src/main/java/org/example/Q0085_MaximalRectangle) | Largest rect of 1s in matrix | Convert to histogram per row, use monotonic stack |

</details>

<details>
<summary><b>Strings & Substrings (click to expand)</b></summary>

| # | Problem | Description | Q-Card |
|---|---------|-------------|--------|
| 3 | [Longest Substring Without Repeating](./LeetCode/src/main/java/org/example/Q0003_LongestSubstringWithoutRepeatingCharacters) | Max substring with unique chars | Sliding window + set, shrink on duplicate |
| 521 | [Longest Uncommon Subsequence I](./LeetCode/src/main/java/org/example/Q0521_LongestUncommonSubsequenceI) | Longest seq not in both strings | If strings differ → longer one wins |
| 522 | [Longest Uncommon Subsequence II](./LeetCode/src/main/java/org/example/Q0522_LongestUncommonSubsequenceII) | Longest seq not subseq of others | Sort by len, check if subseq of any other |
| 524 | [Longest Word in Dictionary](./LeetCode/src/main/java/org/example/Q0524_LongestWordInDictionary) | Longest dict word from deleting chars | Two-pointer subseq check, pick longest/smallest |

</details>

<details>
<summary><b>Binary Search (click to expand)</b></summary>

| # | Problem | Description | Q-Card |
|---|---------|-------------|--------|
| 4 | [Median of Two Sorted Arrays](./LeetCode/src/main/java/org/example/Q0004_MedianOfTwoSortedArray) | Find median of 2 sorted arrays | Binary search on smaller array, partition both |
| 3453 | [Separate Squares I](./LeetCode/src/main/java/org/example/Q3453_SeparateSquaresI) | Min Y for equal area split | Monotonic area → Binary Search on Y |

</details>

<details>
<summary><b>Dynamic Programming (click to expand)</b></summary>

| # | Problem | Description | Q-Card |
|---|---------|-------------|--------|
| 10 | [Regular Expression Matching](./LeetCode/src/main/java/org/example/Q0010_RegularExpressionMatching) | Match string with `.` and `*` | 2D DP: `*` = zero or more of prev char |
| 514 | [Freedom Trail](./LeetCode/src/main/java/org/example/Q0514_FreedomTrail) | Min rotations to spell word on ring | DP on (ring pos, key idx), try all char positions |
| 1895 | [Largest Magic Square](./LeetCode/src/main/java/org/example/Q1895_LargestMagicSquare) | Largest k×k magic square in grid | Prefix sums for rows/cols, check all squares |
| 1937 | [Max Points with Cost](./LeetCode/src/main/java/org/example/Q1937_MaxPointsWithCost) | Max points from grid with penalty | DP with left/right pass optimization |
| 3651 | [Min Cost Path with Teleportations](./LeetCode/src/main/java/org/example/Q3651_MinimumCostPathWithTeleportations) | Grid path with k free teleports | 1D DP + Suffix Minimum for O(1) teleport |

</details>

<details>
<summary><b>Sliding Window (click to expand)</b></summary>

| # | Problem | Description | Q-Card |
|---|---------|-------------|--------|
| 992 | [Subarrays with K Distinct](./LeetCode/src/main/java/org/example/Q0992_SubarraysWithKDistinct) | Count subarrays with exactly K distinct | atMost(K) - atMost(K-1) trick |
| 995 | [Min K Bit Flips](./LeetCode/src/main/java/org/example/Q0995_MinKBitFlips) | Min flips to make all 1s | Greedy flip at each 0, track flips with queue |
| 1423 | [Max Points from Cards](./LeetCode/src/main/java/org/example/Q1423_MaxPointsFromCards) | Pick k cards from ends for max sum | Find min window of (n-k), answer = total - min |
| 1438 | [Longest Subarray With Limit](./LeetCode/src/main/java/org/example/Q1438_LongestSubarrayWithLimit) | Longest subarray where max-min ≤ limit | 2 monotonic deques: maxq (dec), minq (inc) |
| 1984 | [Min Diff Between K Scores](./LeetCode/src/main/java/org/example/Q1984_MinimumDifferenceBetweenHighestAndLowestOfKScores) | Min diff in k consecutive elements | Sort + sliding window of size k |

</details>

<details>
<summary><b>Two Pointers (click to expand)</b></summary>

| # | Problem | Description | Q-Card |
|---|---------|-------------|--------|
| 1877 | [Minimize Max Pair Sum](./LeetCode/src/main/java/org/example/Q1877_MinimizeMaximumPairSumInArray) | Min the max pair sum | Sort, pair smallest with largest |
| 1984 | [Min Diff Between K Scores](./LeetCode/src/main/java/org/example/Q1984_MinimumDifferenceBetweenHighestAndLowestOfKScores) | Min diff in k consecutive elements | Sort + sliding window of size k |

</details>

<details>
<summary><b>Greedy (click to expand)</b></summary>

| # | Problem | Description | Q-Card |
|---|---------|-------------|--------|
| 1007 | [Min Domino Rotations](./LeetCode/src/main/java/org/example/Q1007_MinimumDominoRotations) | Equal row by swapping top/bot | Check tops[0] & bottoms[0] as targets |
| 1877 | [Minimize Max Pair Sum](./LeetCode/src/main/java/org/example/Q1877_MinimizeMaximumPairSumInArray) | Min the max pair sum | Sort, pair smallest with largest |
| 2943 | [Maximize Square Hole Area](./LeetCode/src/main/java/org/example/Q2943_MaximizeSquareHoleArea) | Max square hole from removing bars | Sort bars, find max consecutive bars |
| 2975 | [Maximum Square Area](./LeetCode/src/main/java/org/example/Q2975_MaximizeSquareArea) | Max square from removing fences | All gaps: HashSet(H-gaps) ∩ V-gaps |

</details>

<details>
<summary><b>Backtracking (click to expand)</b></summary>

| # | Problem | Description | Q-Card |
|---|---------|-------------|--------|
| 17 | [Letter Combinations](./LeetCode/src/main/java/org/example/Q0017_LetterCombinations) | All letter combos from phone digits | Backtrack: choose→explore→unchoose for each digit |
| 22 | [Generate Parentheses](./LeetCode/src/main/java/org/example/Q0022_GenerateParentheses) | All valid parenthesis combos | Back track: open < n, close < open |
| 526 | [Beautiful Arrangement](./LeetCode/src/main/java/org/example/Q0526_BeautifulArrangement) | Count permutations where i%perm[i]==0 or vice versa | Backtrack, try each unused num at each pos |

</details>

<details>
<summary><b>Trees & Graphs (click to expand)</b></summary>

| # | Problem | Description | Q-Card |
|---|---------|-------------|--------|
| 865 | [Smallest Subtree with Deepest Nodes](./LeetCode/src/main/java/org/example/Q0865_SmallestSubtreeDeepestNodes) | LCA of all deepest nodes | DFS returns (depth, node), if L==R curr is LCA |
| 2976 | [Min Cost to Convert String I](./LeetCode/src/main/java/org/example/Q2976_MinimumCostToConvertStringI) | Min cost to convert string chars | Floyd-Warshall on 26-node graph, O(26³+n) |
| 3650 | [Min Cost Path with Edge Reversals](./LeetCode/src/main/java/org/example/Q3650_MinimumCostPathWithEdgeReversals) | Min cost with optional edge reversal | Add reverse edges (2× cost), run Dijkstra |
| 3651 | [Min Cost Path with Teleportations](./LeetCode/src/main/java/org/example/Q3651_MinimumCostPathWithTeleportations) | Grid path with k free teleports | 1D DP + Suffix Min by cell value |

</details>

<details>
<summary><b>Linked Lists (click to expand)</b></summary>

| # | Problem | Description | Q-Card |
|---|---------|-------------|--------|
| 23 | [Merge K Sorted Lists](./LeetCode/src/main/java/org/example/Q0023_MergeKSortedLists) | Merge k sorted lists into one | D&C or MinHeap: O(N log k) |

</details>

<details>
<summary><b>Math & Geometry (click to expand)</b></summary>

| # | Problem | Description | Q-Card |
|---|---------|-------------|--------|
| 1266 | [Min Time Visiting All Points](./LeetCode/src/main/java/org/example/Q1266_MinTimeVisitingAllPoints) | Min time to visit points (Chebyshev) | Max(abs(dx), abs(dy)) as diag = 1 sec |
| 3047 | [Largest Square in Two Rects](./LeetCode/src/main/java/org/example/Q3047_FindLargestAreaOfSquareInsideTwoRectangles) | Max square in intersection of any pair | Check all N² pairs, intersect is [maxL, minR] |

</details>

<details>
<summary><b>Matrix & Prefix Sum (click to expand)</b></summary>

| # | Problem | Description | Q-Card |
|---|---------|-------------|--------|
| 1895 | [Largest Magic Square](./LeetCode/src/main/java/org/example/Q1895_LargestMagicSquare) | Largest k×k magic square in grid | Prefix sums for rows/cols, check all squares |
| 1292 | [Max Side Length with Sum <= Threshold](./LeetCode/src/main/java/org/example/Q1292_MaximumSideLengthOfASquareWithSumLessThanOrEqualToThreshold) | Max square with sum <= K | Prefix Sums + Binary Search maxLen |

</details>

<details>
<summary><b>Design (click to expand)</b></summary>

| # | Problem | Description | Q-Card |
|---|---------|-------------|--------|
| 535 | [Encode Decode TinyURL](./LeetCode/src/main/java/org/example/Q0535_EncodeDecodeTinyURL) | URL shortener design | Counter + Base62, dual HashMaps |

</details>

<details>
<summary><b>Bit Manipulation (click to expand)</b></summary>

| # | Problem | Description | Q-Card |
|---|---------|-------------|--------|
| 3314 | [Construct Min Bitwise Array I](./LeetCode/src/main/java/org/example/Q3314_ConstructTheMinimumBitwiseArrayI) | Find min x where x OR (x+1) = n | Odd: clear rightmost bit of trailing 1s |

</details>

<details>
<summary><b>Sweep Line (click to expand)</b></summary>

| # | Problem | Description | Q-Card |
|---|---------|-------------|--------|
| 3454 | [Separate Squares II](./LeetCode/src/main/java/org/example/Q3454_SeparateSquaresII) | Min Y for equal area (overlaps=union) | Sweep line events (y, type), merge intervals |

</details>

### Problems by Number
<details>
<summary><b>Click to expand</b></summary>

| # | Problem | Difficulty | Q-Card 💡 |
|---|---------|------------|-----------|
| 1 | [Two Sum](./LeetCode/src/main/java/org/example/Q0001_TwoSum) | 🟢 Easy | Map lookup: `target - num` exists? |
| 3 | [Longest Substring Without Repeating](./LeetCode/src/main/java/org/example/Q0003_LongestSubstringWithoutRepeatingCharacters) | 🟡 Medium | Slide window + set, shrink on dupe |
| 4 | [Median of Two Sorted Arrays](./LeetCode/src/main/java/org/example/Q0004_MedianOfTwoSortedArray) | 🔴 Hard | Binary search smaller arr, partition both |
| 10 | [Regular Expression Matching](./LeetCode/src/main/java/org/example/Q0010_RegularExpressionMatching) | 🔴 Hard | 2D DP, handle `*` = 0 or more prev |
| 15 | [3Sum](./LeetCode/src/main/java/org/example/Q0015_ThreeSum) | 🟡 Medium | Sort, fix 1, two-ptr rest, skip dupes |
| 17 | [Letter Combinations](./LeetCode/src/main/java/org/example/Q0017_LetterCombinations) | 🟡 Medium | Backtrack: choose→explore→unchoose |
| 20 | [Valid Parentheses](./LeetCode/src/main/java/org/example/Q0020_ValidParentheses) | 🟢 Easy | Stack: push open, pop matching close |
| 22 | [Generate Parentheses](./LeetCode/src/main/java/org/example/Q0022_GenerateParentheses) | 🟡 Medium | Backtrack: open < n, close < open |
| 23 | [Merge K Sorted Lists](./LeetCode/src/main/java/org/example/Q0023_MergeKSortedLists) | 🔴 Hard | D&C or MinHeap: O(N log k) |
| 85 | [Maximal Rectangle](./LeetCode/src/main/java/org/example/Q0085_MaximalRectangle) | 🔴 Hard | Histogram per row + Monotonic Stack |
| 514 | [Freedom Trail](./LeetCode/src/main/java/org/example/Q0514_FreedomTrail) | 🔴 Hard | DP(pos, idx), try all matching chars |
| 521 | [Longest Uncommon Subsequence I](./LeetCode/src/main/java/org/example/Q0521_LongestUncommonSubsequenceI) | 🟢 Easy | Different strings? Longer wins |
| 522 | [Longest Uncommon Subsequence II](./LeetCode/src/main/java/org/example/Q0522_LongestUncommonSubsequenceII) | 🟡 Medium | Check each if subseq of any other |
| 524 | [Longest Word in Dictionary](./LeetCode/src/main/java/org/example/Q0524_LongestWordInDictionary) | 🟡 Medium | 2-ptr subseq check, longest/lex-smallest |
| 526 | [Beautiful Arrangement](./LeetCode/src/main/java/org/example/Q0526_BeautifulArrangement) | 🟡 Medium | Backtrack: try valid nums at each pos |
| 535 | [Encode Decode TinyURL](./LeetCode/src/main/java/org/example/Q0535_EncodeDecodeTinyURL) | 🟡 Medium | Counter + Base62, dual HashMaps |
| 712 | [Min ASCII Delete Sum](./LeetCode/src/main/java/org/example/Q0712_MinimumDeleteSum) | 🟡 Medium | LCS variation: Total - 2*LCS_ASCII |
| 865 | [Smallest Subtree with Deepest Nodes](./LeetCode/src/main/java/org/example/Q0865_SmallestSubtreeDeepestNodes) | 🟡 Medium | DFS return (depth, node), compare L/R depths |
| 992 | [Subarrays with K Distinct](./LeetCode/src/main/java/org/example/Q0992_SubarraysWithKDistinct) | 🔴 Hard | `atMost(K) - atMost(K-1)` |
| 995 | [Min K Bit Flips](./LeetCode/src/main/java/org/example/Q0995_MinKBitFlips) | 🔴 Hard | Greedy flip 0s, track with queue/diff |
| 1007 | [Min Domino Rotations](./LeetCode/src/main/java/org/example/Q1007_MinimumDominoRotations) | 🟡 Medium | Check tops[0] & bottoms[0] as targets |
| 1200 | [Minimum Absolute Difference](./LeetCode/src/main/java/org/example/Q1200_MinimumAbsoluteDifference) | 🟢 Easy | Sort + Single Pass to find min diff |
| 1292 | [Max Side Length with Sum <= Threshold](./LeetCode/src/main/java/org/example/Q1292_MaximumSideLengthOfASquareWithSumLessThanOrEqualToThreshold) | 🟡 Medium | Prefix Sums + Binary Search maxLen |
| 1266 | [Min Time Visiting All Points](./LeetCode/src/main/java/org/example/Q1266_MinTimeVisitingAllPoints) | 🟢 Easy | Max(abs(dx), abs(dy)) |
| 1423 | [Max Points from Cards](./LeetCode/src/main/java/org/example/Q1423_MaxPointsFromCards) | 🟡 Medium | Total - min window of (n-k) |
| 1438 | [Longest Subarray With Limit](./LeetCode/src/main/java/org/example/Q1438_LongestSubarrayWithLimit) | 🟡 Medium | Monotonic deques for max/min in window |
| 1877 | [Minimize Max Pair Sum](./LeetCode/src/main/java/org/example/Q1877_MinimizeMaximumPairSumInArray) | 🟡 Medium | Sort, pair smallest with largest |
| 1895 | [Largest Magic Square](./LeetCode/src/main/java/org/example/Q1895_LargestMagicSquare) | 🟡 Medium | Prefix sums for rows/cols, check all squares |
| 1937 | [Max Points with Cost](./LeetCode/src/main/java/org/example/Q1937_MaxPointsWithCost) | 🟡 Medium | DP with left/right pass optimization |
| 1984 | [Min Diff Between K Scores](./LeetCode/src/main/java/org/example/Q1984_MinimumDifferenceBetweenHighestAndLowestOfKScores) | 🟢 Easy | Sort + sliding window of size k |
| 2943 | [Maximize Square Hole Area](./LeetCode/src/main/java/org/example/Q2943_MaximizeSquareHoleArea) | 🟡 Medium | Sort bars, find max consecutive bars |
| 2975 | [Maximum Square Area](./LeetCode/src/main/java/org/example/Q2975_MaximizeSquareArea) | 🟡 Medium | All gaps: HashSet(H-gaps) ∩ V-gaps |
| 3047 | [Largest Square in Two Rects](./LeetCode/src/main/java/org/example/Q3047_FindLargestAreaOfSquareInsideTwoRectangles) | 🟡 Medium | Check all N² pairs, intersect is [maxL, minR] |
| 3314 | [Construct Min Bitwise Array I](./LeetCode/src/main/java/org/example/Q3314_ConstructTheMinimumBitwiseArrayI) | 🟢 Easy | Odd: clear rightmost bit of trailing 1s |
| 3453 | [Separate Squares I](./LeetCode/src/main/java/org/example/Q3453_SeparateSquaresI) | 🟡 Medium | Monotonic area → Binary Search on Y |
| 3454 | [Separate Squares II](./LeetCode/src/main/java/org/example/Q3454_SeparateSquaresII) | 🔴 Hard | Sweep line (y-events) + Interval merging |
| 3510 | [Min Pair Removal to Sort Array II](./LeetCode/src/main/java/org/example/Q3510_MinimumPairRemovalToSortArrayII) | 🔴 Hard | Heap + LinkedList for min pair sums |
| 2976 | [Min Cost to Convert String I](./LeetCode/src/main/java/org/example/Q2976_MinimumCostToConvertStringI) | 🟡 Medium | Floyd-Warshall on 26-node graph |
| 3650 | [Min Cost Path with Edge Reversals](./LeetCode/src/main/java/org/example/Q3650_MinimumCostPathWithEdgeReversals) | 🟡 Medium | Add reverse edges (2×), run Dijkstra |
| 3651 | [Min Cost Path with Teleportations](./LeetCode/src/main/java/org/example/Q3651_MinimumCostPathWithTeleportations) | 🔴 Hard | 1D DP + Suffix Min, k teleport layers |

</details>

### Explanation Structure

Every problem follows a standardized explanation format:
1. **Problem in Simple Words** - Easy to understand problem statement
2. **Brute Force Approach** - With analysis of why it's suboptimal
3. **Intuitive/Greedy Approach** - Examples where it works and fails
4. **Optimal Solution** - With visualization and step-by-step walkthrough
5. **Complexity Analysis** - Time & Space for all approaches
6. **Key Takeaways** - Pattern recognition & what to remember

---

## DSA Visualizer

> **Interactive step-by-step algorithm visualization** - Reads directly from your LeetCode folder!

### Single Source of Truth Architecture

```
LeetCode/               ← Your solutions (Question.md, Explanation.md, .java)
    ↓ API reads from
VisualizerBackend/      ← Express server (localhost:3001)
    ↓ Serves to
Visualizer/             ← React frontend (localhost:5173)
```

**No duplicate content** - Add questions to LeetCode folder, visualizer auto-detects them!

### Features

| Feature | Description |
|---------|-------------|
| 🔗 **Auto-Sync** | Reads Question.md, Explanation.md, and Java code from LeetCode folder |
| 🎬 **Step Animation** | Navigate through algorithm execution step-by-step |
| 📊 **Array Visualization** | Sliding window, pointers, deques |
| ✏️ **Monaco Editor** | Full code editor with syntax highlighting |
| ⌨️ **Keyboard Shortcuts** | `←` Prev | `→` Next | `Space` Play/Pause |

### Run the Visualizer

```bash
# Terminal 1: Backend
cd VisualizerBackend && npm install && npm run dev

# Terminal 2: Frontend
cd Visualizer && npm install && npm run dev
```

Then open **http://localhost:5173/**

### Tech Stack

- **Frontend**: React 18 + Vite + Tailwind CSS 4 + Monaco Editor
- **Backend**: Express.js (reads LeetCode folder)
- **Pattern Detection**: Auto-detects sliding-window, two-pointer, etc.

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

### Concurrency

| Chapter | Topic | Description |
|---------|-------|-------------|
| **c1** | [Introduction](./LLD/src/main/java/org/example/p2_Concurrency/c1_introduction_to_concurrency) | Processes, Threads, Parallelism vs Concurrency |
| **c2** | [Threads in Java](./LLD/src/main/java/org/example/p2_Concurrency/c2_threads_in_java) | Thread creation, Lifecycle, Runnable interface |
| **c3** | [Executors](./LLD/src/main/java/org/example/p2_Concurrency/c3_executors) | Thread pools, ExecutorService, Types of executors |
| **c4** | [Callables](./LLD/src/main/java/org/example/p2_Concurrency/c4_callables) | Callable interface, Future, Exception handling |
| **c5** | [Synchronization Problem](./LLD/src/main/java/org/example/p2_Concurrency/c5_synchronization_problem) | Race conditions, Critical sections |
| **c6** | [Mutex Locks](./LLD/src/main/java/org/example/p2_Concurrency/c6_mutex_locks) | ReentrantLock, Lock interface, tryLock |
| **c7** | [Synchronized Keyword](./LLD/src/main/java/org/example/p2_Concurrency/c7_synchronized_keyword) | Method & block synchronization, Intrinsic locks |
| **c8** | [Atomic Datatypes](./LLD/src/main/java/org/example/p2_Concurrency/c8_atomic_datatypes) | AtomicInteger, AtomicReference, CAS operations |
| **c9** | [Volatile Keyword](./LLD/src/main/java/org/example/p2_Concurrency/c9_volatile_keyword) | Memory visibility, Happens-before |
| **c10** | [Concurrent Collections](./LLD/src/main/java/org/example/p2_Concurrency/c10_concurrent_collections) | ConcurrentHashMap, CopyOnWriteArrayList |
| **c11** | [Semaphores](./LLD/src/main/java/org/example/p2_Concurrency/c11_semaphores) | Counting semaphores, Resource limiting |
| **c12** | [Producer Consumer](./LLD/src/main/java/org/example/p2_Concurrency/c12_producer_consumer) | BlockingQueue, Wait-notify pattern |
| **c13** | [Deadlocks](./LLD/src/main/java/org/example/p2_Concurrency/c13_deadlocks) | Detection, Prevention, Resource ordering |
| **c14** | [Wait Notify](./LLD/src/main/java/org/example/p2_Concurrency/c14_wait_notify) | Object.wait(), notify(), notifyAll() |

### Java Advanced Concepts

| Chapter | Topic | Description |
|---------|-------|-------------|
| **c1** | [Generics](./LLD/src/main/java/org/example/p3_Java_Advance_Concept/c1_generics) | Type parameters, Bounded types, Wildcards |
| **c2** | [Lambdas](./LLD/src/main/java/org/example/p3_Java_Advance_Concept/c2_lambdas) | Functional interfaces, Lambda syntax, Method references |
| **c3** | [Streams](./LLD/src/main/java/org/example/p3_Java_Advance_Concept/c3_streams) | Stream API, Operations, Collectors, Parallel streams |
| **c4** | [Collection Framework](./LLD/src/main/java/org/example/p3_Java_Advance_Concept/c4_collection_framework) | Collection hierarchy, Common interfaces |
| **c5** | [List Interface](./LLD/src/main/java/org/example/p3_Java_Advance_Concept/c5_list_interface) | ArrayList, LinkedList, Vector, Stack |
| **c6** | [Set Interface](./LLD/src/main/java/org/example/p3_Java_Advance_Concept/c6_set_interface) | HashSet, LinkedHashSet, TreeSet |
| **c7** | [Queue Interface](./LLD/src/main/java/org/example/p3_Java_Advance_Concept/c7_queue_interface) | PriorityQueue, Deque, ArrayDeque |
| **c8** | [Map Interface](./LLD/src/main/java/org/example/p3_Java_Advance_Concept/c8_map_interface) | HashMap, LinkedHashMap, TreeMap |
| **c9** | [Iterators](./LLD/src/main/java/org/example/p3_Java_Advance_Concept/c9_iterators) | Iterator, ListIterator, fail-fast vs fail-safe |
| **c10** | [Custom Objects](./LLD/src/main/java/org/example/p3_Java_Advance_Concept/c10_custom_objects) | equals(), hashCode(), Comparable, Comparator |

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
├── 📂 VisualizerBackend/           # Backend API (reads LeetCode folder)
│   └── server.js                   # Express server
│
├── 📂 Visualizer/                  # Frontend (React)
│   └── src/
│       ├── components/             # UI components
│       ├── engine/                 # Algorithm execution
│       └── hooks/                  # React hooks
│
├── 📂 LLD/                         # Low-Level Design (Java)
├── 📂 LLD NodeJs/                  # Low-Level Design (JavaScript)
├── 📂 LLD Python/                  # Low-Level Design (Python)
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
- [x] Easy: 7 solved
- [x] Medium: 23 solved
- [x] Hard: 9 solved
- **Total**: 39 problems

### LLD
- [x] OOP Fundamentals (8 chapters)
- [x] Concurrency (14 chapters)
- [x] Java Advanced Concepts (10 chapters: Generics, Lambdas, Streams, Collections)
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
