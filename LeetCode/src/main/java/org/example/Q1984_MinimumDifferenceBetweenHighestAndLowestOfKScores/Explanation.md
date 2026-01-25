# 1984. Minimum Difference Between Highest and Lowest of K Scores - Explanation

> **Prerequisites**: 
> - Basic array manipulation
> - Understanding of sorting algorithms
> - Fixed-size sliding window concept
>
> **Related Problems**: 
> - [LeetCode 209 - Minimum Size Subarray Sum](https://leetcode.com/problems/minimum-size-subarray-sum/) (Sliding window fundamentals)
> - [LeetCode 1004 - Max Consecutive Ones III](https://leetcode.com/problems/max-consecutive-ones-iii/) (Fixed window optimization)
> - [LeetCode 1553 - Minimum Number of Days to Eat N Oranges](https://leetcode.com/problems/minimum-number-of-days-to-eat-n-oranges/) (Optimization with constraints)

## Problem in Simple Words

**Pick any `k` scores from an array, minimize the gap between highest and lowest picked.**

Example: `[9, 4, 1, 7]`, k=2 → Pick `9` and `7` → Difference = `9 - 7 = 2` (minimum possible!)

---

## Solution 1: Brute Force ❌

### The Natural Thought
"Let me try ALL possible combinations of picking `k` students, calculate the difference for each, and find the minimum!"

### Approach
```java
// Generate all C(n, k) combinations
// For each combination:
//   Find max and min in that combination
//   Track minimum difference
```

```java
public int minimumDifferenceBruteForce(int[] nums, int k) {
    int minDiff = Integer.MAX_VALUE;
    // Generate all combinations of size k
    List<List<Integer>> combinations = generateCombinations(nums, k);
    
    for (List<Integer> combo : combinations) {
        int max = Collections.max(combo);
        int min = Collections.min(combo);
        minDiff = Math.min(minDiff, max - min);
    }
    return minDiff;
}
```

### Why It's Bad
Generating all combinations of size `k` from `n` elements is `C(n, k)` which can be MASSIVE!

### Example Where It's SLOW ❌
```
Input: nums = [1, 2, 3, ..., 1000], k = 500

Combinations = C(1000, 500) = 1000! / (500! × 500!)
             ≈ 2.7 × 10^299 combinations!

For EACH combination:
  - Find max: O(k) = 500 operations
  - Find min: O(k) = 500 operations

Total: 2.7 × 10^299 × 1000 ≈ 10^302 operations!

Even at 10^9 ops/sec → 10^293 YEARS! 🤯
```

### Complexity Analysis

| Solution | Time | Space | Correct? | Why? |
|----------|------|-------|----------|------|
| Brute Force | O(C(n,k) × k) | O(k) | ✅ TLE | Exponential combinations |
| **Optimal** | O(n log n) | O(1) | ✅ **Optimal** | Sort + single pass |

> 💭 **The combinations explode exponentially. But wait... do we REALLY need to check UNRELATED elements together? What if the k elements we pick were somehow... close to each other?**

---

## Solution 2: Greedy Without Sorting ❌

### The Natural Thought
"Instead of all combinations, what if I just pick `k` elements that LOOK close together by scanning the array?"

### Approach
```java
// For each starting position i:
//   Pick k consecutive elements (without sorting)
//   Calculate max - min
//   Track minimum
```

```java
public int minimumDifferenceWrong(int[] nums, int k) {
    int minDiff = Integer.MAX_VALUE;
    for (int i = 0; i <= nums.length - k; i++) {
        int max = nums[i], min = nums[i];
        for (int j = i; j < i + k; j++) {
            max = Math.max(max, nums[j]);
            min = Math.min(min, nums[j]);
        }
        minDiff = Math.min(minDiff, max - min);
    }
    return minDiff;
}
```

### Example Where It FAILS ❌
```
Input: nums = [9, 4, 1, 7], k = 2

Window [9, 4] → max=9, min=4 → diff = 5
Window [4, 1] → max=4, min=1 → diff = 3
Window [1, 7] → max=7, min=1 → diff = 6

Our answer: 3
Correct answer: 2 (picking [9, 7])

WHY? We can freely pick ANY k elements, not just consecutive ones!
The pair [9, 7] (indices 0 and 3) gives difference = 2
But our approach missed it because they're not adjacent!
```

### Why It Fails 🤯
**We're constrained by original positions, but the problem allows picking ANY k elements!**

Elements that are far apart in the original array might actually be close in VALUE.

### Complexity Analysis

| Solution | Time | Space | Correct? | Why? |
|----------|------|-------|----------|------|
| Brute Force | O(C(n,k) × k) | O(1) | ✅ TLE | Exponential combinations |
| Greedy (no sort) | O(n × k) | O(1) | ❌ Wrong | Misses non-adjacent optimal pairs |
| **Optimal** | O(n log n) | O(1) | ✅ **Optimal** | Sort + single pass |

> 💭 **The problem is we're stuck with original positions. But we can pick ANY k elements! What if we first SORT the array so close-valued elements become neighbors?**

---

## Solution 3: Sort + Fixed-Size Sliding Window ✅

### The Connection 🔗
Let's trace our thinking:
- **Brute Force** was slow because: Exponential combinations to check
- **Greedy (no sort)** failed because: Close values aren't necessarily adjacent in original array
- **What we need**: A way to make "close values" adjacent → **SORT first, then slide a window!**

### The Key Insight 💡

Once sorted, the minimum difference for ANY k elements **MUST** come from **k CONSECUTIVE elements**!

**Why?** After sorting:
```
If you pick non-consecutive elements:
  [1, 4, 7, 9, 15] with k=2
  
  Pick [1, 9] (skipping 4, 7) → diff = 8
  Pick [7, 9] (consecutive)   → diff = 2  ✅ ALWAYS BETTER!

  The skipped elements are BETWEEN your picks in value!
  So picking consecutive = smaller gap!
```

### The Algorithm
1. **Edge case**: If `k == 1`, return `0` (only one element, no difference)
2. **Sort** the array → Close values become neighbors
3. **Slide a window of size k** → Check each consecutive k-group
4. **For each window**: `diff = nums[i + k - 1] - nums[i]` (last - first = max - min in sorted!)
5. **Track minimum difference**

### Step-by-Step Walkthrough

```
Input: nums = [9, 4, 1, 7], k = 2

Step 1: Sort the array
  [9, 4, 1, 7] → [1, 4, 7, 9]
                  ↑  ↑  ↑  ↑
                  0  1  2  3

Step 2: Slide window of size k=2

  Window 1: i=0
  ┌─────────────────────────────┐
  │  [1, 4, 7, 9]               │
  │   └──┘                      │
  │   i=0  i+k-1=1              │
  │   diff = 4 - 1 = 3          │
  └─────────────────────────────┘
  minDiff = 3

  Window 2: i=1
  ┌─────────────────────────────┐
  │  [1, 4, 7, 9]               │
  │      └──┘                   │
  │      i=1  i+k-1=2           │
  │      diff = 7 - 4 = 3       │
  └─────────────────────────────┘
  minDiff = min(3, 3) = 3

  Window 3: i=2
  ┌─────────────────────────────┐
  │  [1, 4, 7, 9]               │
  │         └──┘                │
  │         i=2  i+k-1=3        │
  │         diff = 9 - 7 = 2    │
  └─────────────────────────────┘
  minDiff = min(3, 2) = 2 ✅

Result: 2
```

### Visual Diagram

```
Original:     [9, 4, 1, 7]     (jumbled values)
               ↓  ↓  ↓  ↓
               SORT!
               ↓  ↓  ↓  ↓
Sorted:       [1, 4, 7, 9]     (ordered values)

Now slide window of size k=2:

Position 0:   [1, 4] 7, 9      diff = 4-1 = 3
Position 1:    1 [4, 7] 9      diff = 7-4 = 3  
Position 2:    1, 4 [7, 9]     diff = 9-7 = 2 ← MINIMUM! ✅

Why this works:
┌─────────────────────────────────────────────────────────┐
│  In a SORTED array, consecutive k elements will        │
│  ALWAYS have the smallest possible max-min difference! │
│                                                         │
│  Non-consecutive = gaps in between = larger diff       │
│  Consecutive     = no gaps        = smallest diff      │
└─────────────────────────────────────────────────────────┘
```

### Code
```java
public int minimumDifference(int[] nums, int k) {
    if (k == 1) return 0;                          // Edge case
    
    Arrays.sort(nums);                             // O(n log n)
    
    int minDiff = Integer.MAX_VALUE;
    for (int i = 0; i <= nums.length - k; i++) {   // O(n - k)
        int currentDiff = nums[i + k - 1] - nums[i];
        minDiff = Math.min(minDiff, currentDiff);
    }
    return minDiff;
}
```

---

## Complexity Analysis (Optimal)

| Solution | Time | Space | Status | Why |
|----------|------|-------|--------|-----|
| Brute Force | O(C(n,k) × k) | O(k) | ✅ TLE | Exponential combos |
| Greedy (no sort) | O(n × k) | O(1) | ❌ Wrong | Ignores value proximity |
| **Sort + Slide** | O(n log n) | O(1)* | ✅ **Optimal** | Sort once, slide once |

*Space is O(1) extra (sorting may use O(log n) for recursion stack depending on implementation)

**For n = 1000, k = 500:**
- Brute Force: ~10^299 operations → IMPOSSIBLE
- Optimal: 1000 × log(1000) ≈ 10,000 operations → INSTANT!

---

## Key Takeaways

1. **Sorting transforms the problem**: When looking for "close values," sort first to make them adjacent
2. **Fixed-size window on sorted array**: After sorting, optimal k elements are always consecutive
3. **Pattern Recognition**: "Pick k elements, minimize range" → Sort + Fixed Window

---

## The Journey (TL;DR)

```
🐢 Brute Force → EXPONENTIAL (C(n,k) combos)
         ↓
💡 "Do we need all combos? What if close values were adjacent?"
         ↓
❌ Greedy (no sort) → WRONG (misses non-adjacent close values)
         ↓
💡 "SORT first! Close values become neighbors!"
         ↓
✅ Sort + Slide Window → O(n log n) ✅
```
