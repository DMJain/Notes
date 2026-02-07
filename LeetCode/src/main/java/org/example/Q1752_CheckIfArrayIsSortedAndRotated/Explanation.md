# Check if Array Is Sorted and Rotated - Explanation

> **Prerequisites**: Array traversal, Understanding of rotation operations  
> **Related Problems**:  
> - [LeetCode 33 - Search in Rotated Sorted Array](https://leetcode.com/problems/search-in-rotated-sorted-array/) (Binary search in rotated array)  
> - [LeetCode 81 - Search in Rotated Sorted Array II](https://leetcode.com/problems/search-in-rotated-sorted-array-ii/) (With duplicates)  
> - [LeetCode 153 - Find Minimum in Rotated Sorted Array](https://leetcode.com/problems/find-minimum-in-rotated-sorted-array/) (Find pivot)  
> - [LeetCode 189 - Rotate Array](https://leetcode.com/problems/rotate-array/) (Perform rotation)

## Problem in Simple Words

Check if an array could have been made by rotating a sorted array. For example, `[3,4,5,1,2]` is valid because rotating `[1,2,3,4,5]` by 2 positions gives `[3,4,5,1,2]`. But `[2,1,3,4]` is invalid—no sorted array when rotated gives this.

---

## Solution 1: Brute Force ❌

### The Natural Thought
"Let me try rotating the array by every possible position (0 to n-1) and check if any rotation results in a sorted array."

### Approach

```python
def check_brute_force(nums):
    n = len(nums)
    for rotation in range(n):
        # Create rotated version
        rotated = nums[rotation:] + nums[:rotation]
        # Check if sorted
        is_sorted = True
        for i in range(1, n):
            if rotated[i-1] > rotated[i]:
                is_sorted = False
                break
        if is_sorted:
            return True
    return False
```

### Why It's Bad

- **Time**: O(n²) — For each of n rotations, we check n elements
- **Space**: O(n) — We create a new rotated array each time

### Example Where It's SLOW ❌

```
Input: nums = [50, 51, 52, ..., 100, 1, 2, ..., 49]  (n = 100)

Work per rotation: 100 comparisons
Total rotations: 100
Total operations: 100 × 100 = 10,000 operations

For n = 1000: 1,000,000 operations!
For n = 10,000: 100,000,000 operations!
```

### Complexity Analysis

| Solution | Time | Space | Correct? | Why? |
|----------|------|-------|----------|------|
| **Brute Force** | O(n²) | O(n) | ✅ TLE | Try all rotations |
| Optimal | O(n) | O(1) | ✅ **Optimal** | Count breaks |

> 💭 **"Creating n rotations and checking each is wasteful. What property does a sorted-then-rotated array have that we can detect in ONE pass?"**

---

## Solution 2: Optimal - Count Break Points ✅

### The Connection 🔗

Let's trace our thinking:
- **Brute Force** was slow because: We created n rotations and checked each = O(n²)
- **What we need**: A single-pass property that distinguishes sorted-rotated arrays

Think about what happens when we rotate a sorted array:

```
Original sorted: [1, 2, 3, 4, 5]
                  ↗ ↗ ↗ ↗ ↗   (all increasing, 0 breaks)

Rotate by 2:    [3, 4, 5, 1, 2]
                  ↗ ↗ ↘ ↗      (ONE break at 5→1)
                     
Rotate by 4:    [5, 1, 2, 3, 4]
                  ↘ ↗ ↗ ↗      (ONE break at 5→1)
```

**AHA!** A sorted-rotated array has **at most ONE break point**!

### The Key Insight 💡

> **A "break point" is where `nums[i-1] > nums[i]`.**
> 
> - A fully sorted array: **0 breaks**
> - A sorted array rotated by k positions (k > 0): **exactly 1 break**
> - An unsorted array: **2 or more breaks**
>
> **BUT WAIT!** We must also check the "wrap-around": If `nums[last] > nums[first]`, that's also a break (because in a rotated sorted array, the end should connect back to the start).

### The Algorithm

```
1. Initialize breakPoints = 0
2. For i from 1 to n-1:
   - If nums[i-1] > nums[i]: breakPoints++
3. Check wrap-around: If nums[n-1] > nums[0]: breakPoints++
4. Return breakPoints <= 1
```

### Step-by-Step Walkthrough

#### Example 1: `[3, 4, 5, 1, 2]` → Expected: `true`

```
Initial: breakPoints = 0

Step 1: i=1, nums[0]=3 vs nums[1]=4 → 3 ≤ 4 ✓ (no break)
Step 2: i=2, nums[1]=4 vs nums[2]=5 → 4 ≤ 5 ✓ (no break)
Step 3: i=3, nums[2]=5 vs nums[3]=1 → 5 > 1 ✗ → breakPoints = 1
Step 4: i=4, nums[3]=1 vs nums[4]=2 → 1 ≤ 2 ✓ (no break)

Wrap-around: nums[4]=2 vs nums[0]=3 → 2 ≤ 3 ✓ (no break)

Final: breakPoints = 1 ≤ 1 → TRUE ✅
```

#### Example 2: `[2, 1, 3, 4]` → Expected: `false`

```
Initial: breakPoints = 0

Step 1: i=1, nums[0]=2 vs nums[1]=1 → 2 > 1 ✗ → breakPoints = 1
Step 2: i=2, nums[1]=1 vs nums[2]=3 → 1 ≤ 3 ✓ (no break)
Step 3: i=3, nums[2]=3 vs nums[3]=4 → 3 ≤ 4 ✓ (no break)

Wrap-around: nums[3]=4 vs nums[0]=2 → 4 > 2 ✗ → breakPoints = 2

Final: breakPoints = 2 > 1 → FALSE ❌
```

#### Example 3: `[1, 2, 3]` → Expected: `true`

```
Initial: breakPoints = 0

Step 1: i=1, nums[0]=1 vs nums[1]=2 → 1 ≤ 2 ✓
Step 2: i=2, nums[1]=2 vs nums[2]=3 → 2 ≤ 3 ✓

Wrap-around: nums[2]=3 vs nums[0]=1 → 3 > 1 ✗ → breakPoints = 1

Final: breakPoints = 1 ≤ 1 → TRUE ✅
```

Wait—why is Example 3 true when wrap-around added a break? Because the array is fully sorted (0 rotation), so having JUST the wrap-around break is fine. We allow **at most 1 break total**.

### Visual Diagram

```
SORTED ARRAY (0 rotation):
┌───┬───┬───┬───┬───┐
│ 1 │ 2 │ 3 │ 4 │ 5 │
└───┴───┴───┴───┴───┘
  ↗   ↗   ↗   ↗       ← 0 breaks in middle
              ↓
              └──────→ 5 > 1 (wrap break) = 1 total break ✅


ROTATED SORTED (k=3):
┌───┬───┬───┬───┬───┐
│ 4 │ 5 │ 1 │ 2 │ 3 │
└───┴───┴───┴───┴───┘
  ↗   ↘   ↗   ↗       ← 1 break at 5→1
              ↓
              └──────→ 3 ≤ 4 (no wrap break) = 1 total break ✅


INVALID ARRAY:
┌───┬───┬───┬───┐
│ 2 │ 1 │ 3 │ 4 │
└───┴───┴───┴───┘
  ↘   ↗   ↗           ← 1 break at 2→1
          ↓
          └──────────→ 4 > 2 (wrap break) = 2 total breaks ❌
```

### Why Wrap-Around Check is Critical

Without the wrap-around check, we'd incorrectly accept `[4, 5, 1, 2, 3]` AND `[2, 1, 3, 4]` equally—both have 1 internal break. But:

- `[4, 5, 1, 2, 3]`: Last=3, First=4 → 3 ≤ 4 ✓ (can connect back smoothly)  
- `[2, 1, 3, 4]`: Last=4, First=2 → 4 > 2 ✗ (can't form a cycle)

The wrap-around distinguishes valid from invalid!

---

## Complexity Analysis (Optimal)

| Solution | Time | Space | Status | Why |
|----------|------|-------|--------|-----|
| Brute Force | O(n²) | O(n) | ✅ TLE | Try all n rotations |
| **Count Break Points** | O(n) | O(1) | ✅ **Optimal** | Single pass, constant space |

**Quantified**:
- For n = 100: Only 100 comparisons (vs 10,000 for brute force) → **100× faster**
- For n = 10,000: Only 10,000 comparisons (vs 100,000,000) → **10,000× faster**

---

## Key Takeaways

1. **Rotated sorted arrays have at most 1 break point** — This property is the foundation for many rotated array problems (LC 33, 81, 153, 154)

2. **Don't forget wrap-around** — When checking circular properties, compare the last element with the first!

3. **Pattern recognition**: When asked "is this array special?", look for a **single-pass counting property** instead of brute-forcing all possibilities

---

## The Journey (TL;DR)

```
🐢 Brute Force → TOO SLOW (O(n²) try all rotations)
         ↓
💡 "What single-pass property detects rotation?"
         ↓
✅ Count Break Points → WORKS! O(n), O(1)
   - Count where nums[i-1] > nums[i]
   - Include wrap-around check
   - Valid if breaks ≤ 1
```
