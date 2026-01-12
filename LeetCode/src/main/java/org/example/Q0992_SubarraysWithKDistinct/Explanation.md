# Subarrays with K Different Integers - Explanation

## Problem in Simple Words
Count all **contiguous subarrays** that have **exactly k different numbers**.

**Example**: `nums = [1,2,1,2,3], k = 2`
- `[1,2]` ✓, `[2,1]` ✓, `[1,2]` ✓, `[2,3]` ✓, `[1,2,1]` ✓, `[2,1,2]` ✓, `[1,2,1,2]` ✓
- **Answer: 7**

---

## Solution 1: Brute Force ❌ (Too Slow)

### Approach
Check every possible subarray. For each, count distinct elements.

```java
int count = 0;
for (int i = 0; i < n; i++) {
    for (int j = i; j < n; j++) {
        if (countDistinct(nums, i, j) == k) {
            count++;
        }
    }
}
```

### Why It's Bad
- O(n²) subarrays × O(n) to count distinct = **O(n³)**
- n = 20,000 → 8 trillion operations!

> 💭 **Checking each subarray from scratch is wasteful. What if we used a sliding window? But sliding windows usually find "at most k" or "at least k"... not "exactly k".**

---

## Solution 2: Sliding Window (At Most K) (Valid Alternative)

### The Natural Thought
"The formula `exactly(k) = atMost(k) - atMost(k-1)` would work!"

### Approach
- Count subarrays with ≤ k distinct
- Subtract subarrays with ≤ k-1 distinct
- Difference = exactly k distinct!

### Example
```
nums = [1,2,1,2,3], k = 2

atMost(2) = 12 subarrays
atMost(1) = 5 subarrays
exactly(2) = 12 - 5 = 7 ✅
```

This is a valid O(n) approach, but our solution uses a **different technique**!

> 💭 **The "at most" trick works, but let's try another approach: track how many valid starting positions we can shrink to while keeping exactly k distinct.**

---

## Solution 3: Sliding Window with Prefix Counting ✅ (Optimal)

### The Connection 🔗
Let's trace our thinking:
- **Brute Force** was slow because: O(n³) checking all subarrays
- **At Most trick** works, but we can also count directly with prefix counting
- **Key insight**: When we have exactly k distinct, multiple left positions may be valid!

### The Key Insight 💡

When we have exactly k distinct elements in window `[start, right]`:
- The current window is valid ✅
- We can also **shrink from the left** while keeping k distinct (if there are duplicates)!
- Each valid shrink position gives us another valid subarray

We track a `prefix` count = how many positions we can shrink from the left.

### The Algorithm

```
1. Expand right pointer, add element to window
2. If distinctCount > k: shrink from left (mandatory), reset prefix
3. While leftmost element has count > 1: shrink (optional), increment prefix
4. If distinctCount == k: add (prefix + 1) to result
```

### Why `prefix + 1`?

```
Window: [1, 2, 1, 2] with k=2 distinct

If we can shrink left without losing a distinct:
  [1, 2, 1, 2]  ← valid (prefix=0)
     [2, 1, 2]  ← valid (prefix=1) 
        [1, 2]  ← valid (prefix=2)

All 3 subarrays end at the same right position!
Count = prefix + 1 = 2 + 1 = 3
```

---

## Step-by-Step Walkthrough

**nums = `[1, 2, 1, 2, 3]`, k = 2**

```
Index:    0   1   2   3   4
nums:   [ 1,  2,  1,  2,  3 ]
```

---

### right = 0: Add nums[0] = 1

```
Window: [1]
countMap: {1: 1}
distinctCount: 1
prefix: 0

distinctCount < k → no result added

res = 0
```

---

### right = 1: Add nums[1] = 2

```
Window: [1, 2]
countMap: {1: 1, 2: 1}
distinctCount: 2
prefix: 0

distinctCount == k ✅
Can we shrink? countMap[nums[0]] = countMap[1] = 1 (not > 1) → NO

res += prefix + 1 = 0 + 1 = 1
res = 1
```

**Counted: `[1, 2]`**

---

### right = 2: Add nums[2] = 1

```
Window: [1, 2, 1]
countMap: {1: 2, 2: 1}
distinctCount: 2
prefix: 0

distinctCount == k ✅
Can we shrink? countMap[nums[0]] = countMap[1] = 2 > 1 → YES!
  Shrink: start=0 → start=1, countMap[1]-- → {1: 1, 2: 1}
  prefix = 1

Can we shrink more? countMap[nums[1]] = countMap[2] = 1 (not > 1) → NO

res += prefix + 1 = 1 + 1 = 2
res = 1 + 2 = 3
```

**Counted: `[2, 1]`, `[1, 2, 1]`**

---

### right = 3: Add nums[3] = 2

```
Window: [2, 1, 2] (start=1)
countMap: {1: 1, 2: 2}
distinctCount: 2
prefix: 1 (from before)

distinctCount == k ✅
Can we shrink? countMap[nums[1]] = countMap[2] = 2 > 1 → YES!
  Shrink: start=1 → start=2, countMap[2]-- → {1: 1, 2: 1}
  prefix = 2

Can we shrink more? countMap[nums[2]] = countMap[1] = 1 (not > 1) → NO

res += prefix + 1 = 2 + 1 = 3
res = 3 + 3 = 6
```

**Counted: `[1, 2]`, `[2, 1, 2]`, `[1, 2, 1, 2]`**

---

### right = 4: Add nums[4] = 3

```
Window: [1, 2, 3] (start=2), but wait...
countMap: {1: 1, 2: 1, 3: 1}
distinctCount: 3

distinctCount > k ❌ TOO MANY!
  Mandatory shrink: start=2 → start=3
  countMap[1]-- → {1: 0, 2: 1, 3: 1}
  distinctCount = 2
  prefix = 0 (RESET!)

Now window: [2, 3] (start=3)
distinctCount == k ✅

Can we shrink? countMap[nums[3]] = countMap[2] = 1 (not > 1) → NO

res += prefix + 1 = 0 + 1 = 1
res = 6 + 1 = 7
```

**Counted: `[2, 3]`**

---

### FINAL RESULT: 7 ✅

```
All 7 subarrays:
[1,2], [2,1], [1,2], [2,3], [1,2,1], [2,1,2], [1,2,1,2]
  ↑      ↑      ↑      ↑       ↑        ↑         ↑
 r=1    r=2    r=3    r=4     r=2      r=3       r=3
```

---

## Visual Summary of the Prefix Trick

```
At right=3, window = [1, 2, 1, 2]:

start can be at multiple positions while keeping k=2 distinct:

  start=0: [1, 2, 1, 2]  ← has {1, 2} ✅
  start=1:    [2, 1, 2]  ← has {1, 2} ✅  (shrink 1, prefix++)
  start=2:       [1, 2]  ← has {1, 2} ✅  (shrink 1 more, prefix++)
  
After shrinking: start=2, prefix=2
All 3 subarrays (prefix+1) end at right=3!

res += 3
```

---

## Complexity Analysis

| Solution | Time | Space | Correct? | Why? |
|----------|------|-------|----------|------|
| Brute Force | O(n³) | O(n) | ✅ But TLE | Check all subarrays |
| atMost(k) - atMost(k-1) | O(n) | O(n) | ✅ Alternative | Two pass approach |
| **Prefix Counting** | O(n) | O(n) | ✅ **Optimal** | Single pass |

---

## Key Takeaways

1. **prefix** = number of optional shrinks we can do from left
2. **Mandatory shrink** when distinctCount > k → reset prefix to 0
3. **Optional shrink** when leftmost has count > 1 → increment prefix
4. **Count = prefix + 1** for each valid window position
5. **HashMap** tracks frequency of each number in window

---

## The Journey (TL;DR)

```
🐢 Brute Force: Check all subarrays → TOO SLOW (O(n³))
         ↓
💡 "Sliding window! But how to count exactly k?"
         ↓
🔢 At Most Trick: atMost(k) - atMost(k-1) → WORKS (O(n))
         ↓
💡 "Or directly count with prefix positions!"
         ↓
✅ Prefix Counting: Track shrinkable positions → OPTIMAL (O(n))
```
