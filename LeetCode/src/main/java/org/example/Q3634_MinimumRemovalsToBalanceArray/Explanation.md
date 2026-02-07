# Minimum Removals to Balance Array - Explanation

> **Prerequisites**: 
> - Sorting basics
> - Two Pointers / Sliding Window technique
>
> **Related Problems**: 
> - [LeetCode 1984 - Minimum Difference Between Highest and Lowest of K Scores](https://leetcode.com/problems/minimum-difference-between-highest-and-lowest-of-k-scores/) | [Local](../Q1984_MinimumDifferenceBetweenHighestAndLowestOfKScores/Explanation.md) (Sorting + fixed-size sliding window)
> - [LeetCode 1200 - Minimum Absolute Difference](https://leetcode.com/problems/minimum-absolute-difference/) | [Local](../Q1200_MinimumAbsoluteDifference/Explanation.md) (Sorting to find pairs)
> - [LeetCode 239 - Sliding Window Maximum](https://leetcode.com/problems/sliding-window-maximum/) (Window-based max tracking)

## Problem in Simple Words

Remove minimum elements so the remaining array has `max ≤ min × k`.

**Example**: `nums = [1, 6, 2, 9], k = 3` → Remove `1` and `9`, keep `[2, 6]` where `6 ≤ 2 × 3 = 6`. **Answer: 2 removals**.

---

## Solution 1: Brute Force ❌

### Approach
```java
// Check ALL possible subsets
int maxKept = 1;  // Single element always works

for (int mask = 1; mask < (1 << n); mask++) {
    // Build subset from bitmask
    List<Integer> subset = buildSubset(nums, mask);
    int min = Collections.min(subset);
    int max = Collections.max(subset);
    
    if (max <= min * k) {
        maxKept = Math.max(maxKept, subset.size());
    }
}
return n - maxKept;
```

### Why It's Bad
We generate **2^n** subsets and check each one for validity!

### Example Where It's SLOW ❌
```
Input: nums = [1, 2, 3, ..., 20], k = 2

Subsets to check: 2^20 - 1 = 1,048,575 subsets
Each subset: O(n) to find min/max = 20 ops

Total: 1,048,575 × 20 ≈ 21 MILLION operations (just n=20!)

For n = 100,000 (constraint max):
    2^100000 = ∞ (literally impossible to compute)
```

### Complexity Analysis

| Solution | Time | Space | Correct? | Why? |
|----------|------|-------|----------|------|
| **Brute Force** | O(2^n × n) | O(n) | ✅ TLE | Exponential subsets |
| Greedy Fixed Start | O(n log n) | O(log n) | ❌ Wrong | Misses better windows |
| **Optimal** | O(n log n) | O(log n) | ✅ **AC** | Sort + Two Pointers |

> 💭 **"2^n subsets is impossible. But wait... do we really need to check EVERY subset? What if elements that work together share some property that lets us find them faster?"**

---

## Solution 2: Greedy - Sort and Extend from Start ❌

### The Natural Thought
"After sorting, elements close in value are adjacent. If I always start from index 0 and extend right as far as valid, I'll find the largest balanced subarray!"

```
Sorted array: [a₀, a₁, a₂, ..., aₙ₋₁]
              ↑
          Start here, extend right while valid
```

### Approach
```java
Arrays.sort(nums);
int maxLen = 0;

// Always start from index 0
for (int j = 0; j < n; j++) {
    if (nums[j] <= nums[0] * k) {
        maxLen = j + 1;  // Extend window
    } else {
        break;  // Can't extend further
    }
}
return n - maxLen;
```

### Example Where It FAILS ❌
```
Input: nums = [1, 3, 4, 5], k = 2
Sorted: [1, 3, 4, 5]

Greedy (start at index 0):
  - Check [1]: 1 ≤ 1*2? ✅ maxLen = 1
  - Check [1, 3]: 3 ≤ 1*2 = 2? ❌ STOP!
  
  Result: maxLen = 1, removals = 3

But look at window starting at index 1:
  - [3, 4, 5]: max=5, min=3, check 5 ≤ 3*2 = 6? ✅
  - maxLen = 3, removals = 1 ← MUCH BETTER!

Expected: 1 | Got: 3 ← WRONG!
```

### Why It Fails 🤯
**Fixed starting point misses better windows!** The smallest element (index 0) might be an "outlier" that forces the window to stay small. By excluding it, we might keep MORE elements.

> 💭 **"OK, we can't fix the starting point. We need to try ALL possible starting positions. But that sounds like O(n²)... What if we could check all starting positions efficiently using two pointers that never backtrack?"**

---

## Solution 3: Optimal - Sorting + Two Pointers ✅

### The Connection 🔗
Let's trace our thinking journey:
- **Brute Force** was impossible because: checking 2^n subsets is exponential
- **Greedy Fixed Start** failed because: the best window might not start at index 0
- **What we need**: Check ALL starting positions, but efficiently → **Two Pointers!**

### The Key Insight 💡

**After sorting, any valid balanced subset becomes a CONTIGUOUS subarray!**

Why? If we keep elements `a ≤ b ≤ c` where `c ≤ a × k`, then:
- Any subset of `{a, b, c}` is also valid
- All valid elements are between `a` and `c` in sorted order
- They form a **contiguous block** in the sorted array!

```
Sorted: [... | a  b  c | ...]
             └─────────┘
              CONTIGUOUS valid subset!
              min=a, max=c
              Condition: c ≤ a × k
```

### The Algorithm
1. **Sort** the array
2. **Initialize**: left pointer `i = 0`, track `maxLen = 0`
3. **For each right pointer `j`** (0 to n-1):
   - **Shrink left** while `nums[j] > nums[i] × k`
   - **Update** `maxLen = max(maxLen, j - i + 1)`
4. **Return** `n - maxLen`

**Why two pointers work**: The `i` pointer only moves RIGHT, never back. For larger `j`, we might need to shrink more (increase `i`), but never less. This is **monotonic**!

### Step-by-Step Walkthrough

**Input**: `nums = [1, 6, 2, 9], k = 3`

```
Step 1: Sort
┌───┬───┬───┬───┐
│ 1 │ 2 │ 6 │ 9 │
└───┴───┴───┴───┘
  0   1   2   3

═══════════════════════════════════════════════════════════════

j=0: Consider element 1
     ┌───┐
     │ 1 │  2   6   9
     └───┘
     i=0, j=0
     
     Check: 1 > 1*3? NO → Window valid
     Window [0,0]: length = 1
     maxLen = 1

───────────────────────────────────────────────────────────────

j=1: Consider element 2
     ┌───────┐
     │ 1   2 │  6   9
     └───────┘
     i=0, j=1
     
     Check: 2 > 1*3 = 3? NO → Window valid
     Window [0,1]: length = 2
     maxLen = 2

───────────────────────────────────────────────────────────────

j=2: Consider element 6
     ┌───────────┐
     │ 1   2   6 │  9
     └───────────┘
     i=0, j=2
     
     Check: 6 > 1*3 = 3? YES ❌ → SHRINK!
     
         ┌───────┐
       1 │ 2   6 │  9
         └───────┘
         i=1, j=2
     
     Check: 6 > 2*3 = 6? NO → Window valid now!
     Window [1,2]: length = 2
     maxLen = max(2, 2) = 2

───────────────────────────────────────────────────────────────

j=3: Consider element 9
         ┌───────────┐
       1 │ 2   6   9 │
         └───────────┘
         i=1, j=3
     
     Check: 9 > 2*3 = 6? YES ❌ → SHRINK!
     
             ┌───────┐
       1   2 │ 6   9 │
             └───────┘
             i=2, j=3
     
     Check: 9 > 6*3 = 18? NO → Window valid!
     Window [2,3]: length = 2
     maxLen = max(2, 2) = 2

═══════════════════════════════════════════════════════════════

FINAL RESULT
┌────────────────────────────────────┐
│ Total elements:     4              │
│ Max balanced kept:  2              │
│ Min removals:       4 - 2 = 2  ✅  │
└────────────────────────────────────┘
```

### Visual: Two Pointer Movement
```
Index:     0    1    2    3
Value:   [ 1 ][ 2 ][ 6 ][ 9 ]
          
j=0:      i,j                    len=1
j=1:      i────j                 len=2
j=2:           i────j            len=2 (i shrunk!)
j=3:                i────j       len=2 (i shrunk again!)

i moves: 0 → 0 → 1 → 2  (monotonic, never goes back!)
```

---

## Complexity Analysis (Optimal)

| Solution | Time | Space | Status | Why |
|----------|------|-------|--------|-----|
| Brute Force | O(2^n × n) | O(n) | ✅ TLE | Exponential |
| Greedy Fixed | O(n log n) | O(log n) | ❌ Wrong | Misses windows |
| **Optimal** | **O(n log n)** | **O(log n)** | ✅ **AC** | Sort + Linear scan |

**Why O(n log n)?**
- Sorting: O(n log n)
- Two Pointers: Each pointer moves at most n times → O(n)
- Total: O(n log n) + O(n) = **O(n log n)**

**For n = 100,000**:
- Brute Force: 2^100000 = impossible
- Optimal: 100,000 × 17 ≈ **1.7 million ops** ✅ (runs in <100ms)

---

## Key Takeaways

1. **Sorting transforms structure**: After sorting, valid balanced subsets become CONTIGUOUS subarrays. This is the core insight that makes O(n) scanning possible.

2. **Two Pointers for monotonic windows**: When shrinking the window only helps (never hurts the right endpoint), two pointers give O(n) without backtracking.

3. **Flip the question**: "Minimize removals" = "Maximize kept elements". Finding the longest valid subarray answers both!

---

## The Journey (TL;DR)

```
🐢 Brute Force → TOO SLOW (2^n subsets)
         ↓
💡 "Valid subsets are contiguous after sorting..."
         ↓
❌ Greedy Fixed Start → WRONG (misses better windows)
         ↓
💡 "Try ALL starts with two pointers!"
         ↓
✅ Sort + Two Pointers → O(n log n) WORKS!
```

---

## Key Optimizations in Code

```java
// CRITICAL: Prevent integer overflow!
while ((long) nums[j] > (long) nums[i] * k) {
    i++;
}
```

Why `long`? 
- `nums[i]` can be up to 10^9
- `k` can be up to 10^5
- `nums[i] * k` = 10^9 × 10^5 = **10^14** > Integer.MAX_VALUE (≈2.1×10^9)

Without cast: Overflow → wrong comparisons → wrong answer!
