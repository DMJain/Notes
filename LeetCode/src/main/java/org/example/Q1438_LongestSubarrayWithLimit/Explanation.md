# Longest Continuous Subarray With Absolute Diff ≤ Limit - Explanation

> **Prerequisites**: Understanding **Monotonic Stack/Deque** is essential. This pattern also appears in [Q239 Sliding Window Maximum](https://leetcode.com/problems/sliding-window-maximum/). If you can solve "max in sliding window" in O(1), this problem becomes a natural extension.

## Problem in Simple Words
Find the **longest subarray** where `max - min ≤ limit`.

**Example**: `nums = [8,2,4,7], limit = 4`
- `[2,4]` → max=4, min=2, diff=2 ≤ 4 ✓
- `[4,7]` → max=7, min=4, diff=3 ≤ 4 ✓
- **Answer: 2**

---

## Solution 1: Brute Force ❌ (Too Slow)

### Approach
Check every subarray, track min/max for each.

```java
for (int i = 0; i < n; i++) {
    int min = nums[i], max = nums[i];
    for (int j = i; j < n; j++) {
        min = Math.min(min, nums[j]);
        max = Math.max(max, nums[j]);
        if (max - min <= limit) {
            ans = Math.max(ans, j - i + 1);
        }
    }
}
```

### Why It's Bad
- **O(n²)** time
- n = 10⁵ → 10¹⁰ operations!

> 💭 **Checking all subarrays is O(n²). What if we used a sliding window? The challenge is efficiently tracking min/max in the window.**

---

## Solution 2: Sliding Window + TreeMap ❌ (Works but Slower)

### The Natural Thought
"Use TreeMap to track element frequencies. Get min/max in O(log n)!"

### Approach
```java
TreeMap<Integer, Integer> map = new TreeMap<>();
// firstKey() = min, lastKey() = max
```

### Why It's Not Ideal
- Each insert/remove = **O(log n)**
- Total = **O(n log n)**
- Can do better!

> 💭 **TreeMap works but O(log n) per operation adds up. Is there a data structure that gives O(1) min/max in a sliding window?**

---

## Solution 3: Sliding Window + Monotonic Deques ✅ (Optimal)

### The Connection 🔗
Let's trace our thinking:
- **Brute Force** was slow because: checking all subarrays = O(n²)
- **TreeMap** was better but still slow because: O(log n) per insert/remove
- **What we need**: O(1) min/max queries → **Monotonic Deques!**

### The Key Insight 💡
We only need **max** and **min** of current window:
- **maxq**: Monotonic **decreasing** deque → front = max
- **minq**: Monotonic **increasing** deque → front = min

Both operations are **O(1)** amortized!

### What is a Monotonic Deque?

```
Monotonic DECREASING (for max):
  [8, 5, 3]  ← always decreasing left to right
  Front = 8 = MAX of window

  Add 6: remove elements < 6 from back
  [8, 5, 3] → [8] → [8, 6]
  
Monotonic INCREASING (for min):
  [2, 4, 7]  ← always increasing left to right
  Front = 2 = MIN of window
```

### The Algorithm

```
1. Expand: Add nums[i] to both deques (maintain monotonic property)
2. Check: If max - min > limit → shrink window (j++)
3. When shrinking: If nums[j] is at front of deque, remove it
4. Track max window size
```

---

## Step-by-Step Walkthrough

**nums = `[8, 2, 4, 7]`, limit = 4**

```
maxq = [] (decreasing, front = max)
minq = [] (increasing, front = min)
j = 0, ans = 0
```

---

### i = 0: nums[0] = 8

```
Add to maxq: 8 > nothing → maxq = [8]
Add to minq: 8 < nothing → minq = [8]

Check: max - min = 8 - 8 = 0 ≤ 4 ✓ (valid)

Window: [8], size = 1
ans = max(0, 1) = 1
```

---

### i = 1: nums[1] = 2

```
Add to maxq: 2 < 8 → just add → maxq = [8, 2]
Add to minq: 2 < 8 → remove 8 → minq = [2]

Check: max - min = 8 - 2 = 6 > 4 ❌ (invalid!)

Shrink: j = 0 → j = 1
  nums[0] = 8 = maxq.front? YES → remove → maxq = [2]
  nums[0] = 8 = minq.front? NO (front is 2)

Window: [2], size = 1
ans = max(1, 1) = 1
```

---

### i = 2: nums[2] = 4

```
Add to maxq: 4 > 2 → remove 2 → maxq = [4]
Add to minq: 4 > 2 → just add → minq = [2, 4]

Check: max - min = 4 - 2 = 2 ≤ 4 ✓ (valid)

Window: [2, 4], size = 2
ans = max(1, 2) = 2
```

---

### i = 3: nums[3] = 7

```
Add to maxq: 7 > 4 → remove 4 → maxq = [7]
Add to minq: 7 > 4 → just add → minq = [2, 4, 7]

Check: max - min = 7 - 2 = 5 > 4 ❌ (invalid!)

Shrink: j = 1 → j = 2
  nums[1] = 2 = maxq.front? NO (front is 7)
  nums[1] = 2 = minq.front? YES → remove → minq = [4, 7]

Window: [4, 7], size = 2
ans = max(2, 2) = 2
```

---

### FINAL RESULT: 2 ✅

---

## Visual: Monotonic Deques

```
At i=3, before shrink:

maxq (decreasing):     minq (increasing):
   ┌───┐                  ┌───┬───┬───┐
   │ 7 │                  │ 2 │ 4 │ 7 │
   └───┘                  └───┴───┴───┘
   front=max=7            front=min=2

   max - min = 7 - 2 = 5 > limit=4 → INVALID!

After shrinking (remove nums[j]=2):

maxq:                  minq:
   ┌───┐                  ┌───┬───┐
   │ 7 │                  │ 4 │ 7 │
   └───┘                  └───┴───┘
   max=7                  min=4

   max - min = 7 - 4 = 3 ≤ 4 → VALID!
```

---

## Why Monotonic Deques are O(1)?

```
Each element is added/removed at most ONCE:
- Added when we reach it (i)
- Removed either:
  - When a "better" element comes (from back)
  - When window shrinks past it (from front)

Total operations ≤ 2n = O(n)
Amortized O(1) per element!
```

---

## Why This Approach Works

```
For max - min ≤ limit:
  We only care about the CURRENT max and min!
  
Monotonic deque keeps "candidates" for max/min:
  - maxq: [8, 5, 3] → 8 is max, but 5 becomes max when 8 leaves
  - Elements smaller than new element can NEVER be max → discard!

When window shrinks:
  - Only remove from front IF the leaving element IS the front
  - Otherwise it was already removed when a bigger element came
```

---

## Honorable Mention: Multiset (C++) / TreeMap

> 💡 **TreeMap/Multiset approach**: In languages with balanced BST (C++ multiset, Java TreeMap), you can use `firstKey()`/`lastKey()` for O(log n) min/max. This gives O(n log n) total — perfectly acceptable! Choose Monotonic Deques when you need O(n), but TreeMap is simpler to code and often fast enough.

---

## Complexity Analysis

| Solution | Time | Space | Correct? | Why? |
|----------|------|-------|----------|------|
| Brute Force | O(n²) | O(1) | ✅ TLE | Check all subarrays |
| TreeMap | O(n log n) | O(n) | ✅ Works | O(log n) per op |
| **Monotonic Deques** | O(n) | O(n) | ✅ **Optimal** | O(1) amortized |

---

## Key Takeaways

1. **Max - Min in window** = need to track both efficiently
2. **Monotonic decreasing deque** → front = max
3. **Monotonic increasing deque** → front = min
4. **Each element removed at most once** → O(n) total
5. **Shrink from front** only when leaving element matches front

---

## The Journey (TL;DR)

```
🐢 Brute Force: Check all subarrays → TOO SLOW (O(n²))
         ↓
💡 "Sliding window! But how to track min/max?"
         ↓
🌳 TreeMap: O(log n) per operation → WORKS but slow (O(n log n))
         ↓
💡 "Monotonic deques give O(1) min/max!"
         ↓
✅ Monotonic Deques: Amortized O(1) → OPTIMAL (O(n))
```
