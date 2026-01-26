# 1200. Minimum Absolute Difference - Explanation

> **Prerequisites**: 
> - Basic Array Iteration
> - Sorting Basics (O(N log N))
> - Understanding Absolute Difference `|a - b|`
>
> **Related Problems**: 
> - [LeetCode 163 - Missing Ranges](https://leetcode.com/problems/missing-ranges/) (Process adjacent elements)
> - [LeetCode 532 - K-diff Pairs in an Array](https://leetcode.com/problems/k-diff-pairs-in-an-array/) (Finding specific differences)
> - [LeetCode 2613 - Beautiful Pairs](https://leetcode.com/problems/beautiful-pairs/) (Finding pairs with min dist)

## Problem in Simple Words

**Find all pairs of numbers in a list that have the smallest possible difference.**

Example: `[4, 2, 1, 3]` → Sorted: `[1, 2, 3, 4]`. Adjacent pairs `(1,2), (2,3), (3,4)` all have diff 1.
So output: `[[1, 2], [2, 3], [3, 4]]`.

---

## Solution 1: Brute Force ❌

### The Natural Thought
"To find the minimum difference, I need to check the difference between **EVERY** possible pair of numbers and find the smallest one!"

### Approach
```java
// 1. Check all pairs (i, j)
// 2. Find global Minimum Difference
// 3. Second Pass: Check all pairs again
// 4. Collect pairs that match min diff
```

```java
public List<List<Integer>> minimumAbsDifferenceBrute(int[] arr) {
    int minDiff = Integer.MAX_VALUE;
    
    // Pass 1: Find minimum difference
    for (int i = 0; i < arr.length; i++) {
        for (int j = i + 1; j < arr.length; j++) {
            int diff = Math.abs(arr[i] - arr[j]);
            minDiff = Math.min(minDiff, diff);
        }
    }
    
    // Pass 2: Collect pairs
    List<List<Integer>> result = new ArrayList<>();
    for (int i = 0; i < arr.length; i++) {
        for (int j = i + 1; j < arr.length; j++) {
            int diff = Math.abs(arr[i] - arr[j]);
            if (diff == minDiff) {
                // Determine smaller element first for ascending order
                int a = Math.min(arr[i], arr[j]);
                int b = Math.max(arr[i], arr[j]);
                result.add(Arrays.asList(a, b));
            }
        }
    }
    
    // Note: Result pairs need to be sorted, and list of pairs sorted.
    // Brute force is so messy it also needs extra sorting at the end!
    result.sort((a, b) -> a.get(0) - b.get(0));
    
    return result;
}
```

### Why It's Bad
We are comparing every element with every other element. This mimics the "Handshake Problem" - for `n` people, everyone shakes hands with everyone else.

### Example Where It's SLOW ❌
```
Input: arr with 100,000 unique integers (Constraints say N <= 10^5)

Pairs to check: N * (N - 1) / 2
              ≈ (100,000)^2 / 2
              = 5,000,000,000 (5 Billion) operations!

If computer does 10^8 ops/sec:
Time: 5,000,000,000 / 10^8 = 50 seconds!
LeetCode Limit: ~1-2 seconds.
Result: ❌ Time Limit Exceeded (TLE)
```

### Complexity Analysis

| Solution | Time | Space | Correct? | Why? |
|----------|------|-------|----------|------|
| Brute Force | O(N^2) | O(1) | ✅ TLE | comparing all pairs |
| **Optimal** | O(...) | O(...) | ✅ **Optimal** | ... |

> 💭 **Checking EVERY pair is too many comparisons. Do we really need to compare `1` with `1000000`? No! The smallest difference will likely be between numbers that are close in value. How can we bring close values together?**

---

## Solution 2: Linear Scan Without Sorting ❌

### The Natural Thought
"Comparing every pair is too slow. But wait, `1` and `100` are far apart. The minimum difference should be between neighbors, right? What if I just loop through the array once and check adjacent elements `arr[i]` and `arr[i+1]`?"

### Approach
```java
// Iterate i from 0 to N-2
// Check diff between arr[i] and arr[i+1]
// Return the minimum found
```

```java
public List<List<Integer>> minimumAbsDifferenceWrong(int[] arr) {
    int minDiff = Integer.MAX_VALUE;
    // Just blindly check neighbors
    for (int i = 0; i < arr.length - 1; i++) {
        minDiff = Math.min(minDiff, Math.abs(arr[i] - arr[i+1]));
    }
    // ... collect pairs ...
    return new ArrayList<>(); // Stub
}
```

### Example Where It FAILS ❌
```
Input: arr = [30, 80, 10, 50]
Target: Find min absolute diff

Neighbors Check:
|30 - 80| = 50
|80 - 10| = 70
|10 - 50| = 40
Min Found: 40

ACTUAL Minimum:
10 and 30 are both in the array.
|30 - 10| = 20!

Result: ❌ Wrong Answer
Why? The numbers closest in value (10 and 30) were NOT neighbors in the original list!
```

### Why It Fails 🤯
**Proximity in index != Proximity in value.**
Just because two numbers sit next to each other in the unsorted array doesn't mean they are close in value. The pair with the smallest difference could be at index `0` and index `100`.

### Complexity Analysis

| Solution | Time | Space | Correct? | Why? |
|----------|------|-------|----------|------|
| Brute Force | O(N^2) | O(1) | ✅ TLE | comparing all pairs |
| Linear Scan | O(N) | O(1) | ❌ Wrong | misses non-adjacent pairs |
| **Optimal** | O(...) | O(...) | ✅ **Optimal** | ... |


> 💭 **The problem is that "close values" are scattered everywhere. If we could just rearrange the array so that close values ARE neighbors... what technique does that?**

---

## Solution 3: Sort + Single Pass ✅

### The Connection 🔗
Let's trace our thinking:
- **Brute Force** was slow because: Logic checked unrelated numbers (like `1` and `10^6`).
- **Linear Scan** failed because: Close numbers weren't neighbors.
- **What we need**: A way to make "closest values" adjacent → **SORTING!**

### The Key Insight 💡
**In a sorted array, the minimum absolute difference MUST involve adjacent elements.**
Math Proof:
If `a <= b <= c`, then `c - a` is always greater than or equal to `b - a` and `c - b`.
So `(a, c)` can never have a smaller gap than `(a, b)` or `(b, c)`.

### The Algorithm
1. **Sort** the array (`[4, 2, 1, 3]` → `[1, 2, 3, 4]`).
2. **One Pass**: Iterate through the sorted array to find the global **minimum difference**.
3. **Reset**: Create a list of pairs.
4. **Collect**: If we find a pair with the *same* min diff again, add to list. If we find a *smaller* diff (not possible if we did step 2 right, but we can combine steps), restart list.
   *(Optimized One-Pass approach)*:
   - Calculate diff `d = arr[i] - arr[i-1]`.
   - If `d < minDiff`: Reset list, update `minDiff = d`, add pair.
   - If `d == minDiff`: Add pair.

### Step-by-Step Walkthrough
We need an example where the minimum difference **changes** or is **ignored** to see the logic.
Input: `arr = [30, 23, 15, 19, 10]`

1. **Sort**: `[10, 15, 19, 23, 30]`

2. **Initialize**: `minDiff = ∞`, `ans = []`

3. **Iterate** neighbors:
   
   - **Pair (10, 15)**: Diff = 5
     - `5 < ∞` → **NEW MIN!** 
     - Reset `ans`. Add `[10, 15]`.
     - `minDiff = 5`

   - **Pair (15, 19)**: Diff = 4
     - `4 < 5` (Current Min) → **NEW MIN!**
     - Reset `ans` (discard `[10, 15]`). Add `[15, 19]`.
     - `minDiff = 4`

   - **Pair (19, 23)**: Diff = 4
     - `4 == 4` (Current Min) → **SAME MIN!**
     - Append `[19, 23]`.
     - `ans = [[15, 19], [19, 23]]`

   - **Pair (23, 30)**: Diff = 7
     - `7 > 4` (Current Min) → **IGNORE**
     - Do nothing.

**Result**: `[[15, 19], [19, 23]]`

### Visual Diagram
```
Sorted Array:  [ 10,  15,  19,  23,  30 ]

Step 1: Compare 10, 15
        [ 10,  15 ] ... ... ...
           └─5─┘
           New Min! (∞ → 5)
           List: [[10, 15]]

Step 2: Compare 15, 19
        ... [ 15,  19 ] ... ...
               └─4─┘
               New Min! (5 → 4)  🚨 RESET LIST!
               List: [[15, 19]]

Step 3: Compare 19, 23
        ... ... [ 19,  23 ] ...
                   └─4─┘
                   Equal Min! (4 == 4)  ✅ APPEND
                   List: [[15, 19], [19, 23]]

Step 4: Compare 23, 30
        ... ... ... [ 23,  30 ]
                       └─7─┘
                       Larger Diff (7 > 4)  ❌ IGNORE
                       List: Unchanged

Final Answer: [[15, 19], [19, 23]]
```

### Complexity Analysis (Optimal)

| Solution | Time | Space | Status | Why |
|----------|------|-------|--------|-----|
| Brute Force | O(N^2) | O(1) | ✅ TLE | checking all pairs |
| Linear Scan | O(N) | O(1) | ❌ Wrong | unsorted neighbors |
| **Sort + Pass** | **O(N log N)** | **O(log N)** | ✅ **Optimal** | Sorting dominates |

*Space depends on sorting algorithm (Java dual-pivot quicksort: O(log N) stack).*

---

## Key Optimizations in Code

```java
if (temp < abs) {
    ans = new ArrayList<>(); // Found simpler diff? RESET list!
    abs = temp;
    ans.add(...);
}
```

**Why this matters:**
Instead of two passes (one to find min, one to collect), we do it in **one pass**.
- If we find a *smaller* difference, we know all previous pairs in `ans` are garbage (too big). So we throw them away (`new ArrayList<>`) and start fresh.
- This avoids iterating the array twice!

---

## Key Takeaways
1. **Sorting simplifies proximity**: Whenever you need to compare values based on closeness, sorting is often the first step.
2. **Local vs Global**: In a sorted list, global minimum gap is always found in a local adjacent gap.
3. **One-Pass Logic**: You can find min and collect items in one loop by resetting the result list when a new minimum is found.

---

## The Journey (TL;DR)
```
🐢 Brute Force → TOO SLOW (O(N^2) checks)
         ↓
💡 "Can we just check neighbors?"
         ↓
❌ Linear Scan (Unsorted) → WRONG (neighbors != close values)
         ↓
💡 "Sort it! Close values become neighbors!"
         ↓
✅ Sort + One Pass → WORKS! (O(N log N))
```
