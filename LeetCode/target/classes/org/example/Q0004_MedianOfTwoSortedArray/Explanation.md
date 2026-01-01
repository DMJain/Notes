# Median of Two Sorted Arrays - Explanation

## Problem in Simple Words
You have **two sorted lists**. Find the **median** (middle value) as if they were merged into one big sorted list.

**Catch:** You must do it in **O(log(m+n))** time — no actual merging allowed!

**Example**: 
- `nums1 = [1, 3]`, `nums2 = [2]`
- Merged would be `[1, 2, 3]`
- **Median = 2**

---

## Solution 1: Brute Force (Merge) ❌ (Too Slow)

### Approach
Actually merge both arrays, then find the middle element.

```java
int[] merged = merge(nums1, nums2);
return merged[merged.length / 2];
```

### Why It's Bad
- **Time**: O(m + n) to merge
- **Space**: O(m + n) to store merged array
- Problem **demands O(log(m+n))** — this doesn't qualify!

---

## Solution 2: Two Pointers (Virtual Merge) ❌ (Still Too Slow)

### Approach
"Don't actually merge! Just use two pointers to track position until we hit the median."

```java
int i = 0, j = 0, count = 0;
int medianPos = (m + n) / 2;

while (count < medianPos) {
    if (nums1[i] < nums2[j]) i++;
    else j++;
    count++;
}
// median is around nums1[i] or nums2[j]
```

### Example Where It WORKS ✅

```
nums1 = [1, 3, 5]
nums2 = [2, 4, 6]
Total = 6, median position = 3

Step through with pointers:
  count=1: 1 < 2, i++
  count=2: 3 > 2, j++
  count=3: 3 < 4, i++  ← STOP!

Median = 3 ✅
```

### Example Where It's SLOW ❌

```
nums1 = [1, 2, 3, ..., 1000000]  ← 1 million elements
nums2 = [1000001]

Median position ≈ 500,000

Two pointers walk through 500,000 elements!
Time = O(m + n) = O(1,000,000)

But O(log(m+n)) = O(20) ← That's what we need!
```

### Why It Fails 🤯
Still **O(m + n)** because you're walking through elements one by one.

The problem specifically requires O(log) — that means **binary search**!

---

## Solution 3: Binary Search on Partition ✅ (Optimal)

### What is it?
Instead of finding the median value, find the **partition point**:
- Split both arrays so left half ≤ right half
- Left half has exactly half the total elements

### Why It Solves the Problem
```
Two Pointers:              Binary Search:
     ↓                          ↓
"Walk to median"          "Jump to partition"
O(m + n) steps            O(log(min(m,n))) jumps
```

### The Core Idea 💡

```
We want to find a CUT in both arrays:

nums1: [1, 3, |8, 9]      ← cut after index 1
nums2: [2, 7, |11, 15]    ← cut after index 1

LEFT side:  1, 3, 2, 7    (max = 7)
RIGHT side: 8, 9, 11, 15  (min = 8)

Is LEFT ≤ RIGHT? YES! 7 ≤ 8 ✅
Median = average(max_left, min_right) = (7 + 8) / 2 = 7.5
```

### Step-by-Step Walkthrough

**nums1 = [1, 2], nums2 = [3, 4]**, Total = 4 (even)

```
We need (4+1)/2 = 2 elements in left half.

Binary search on nums1 (smaller array):

═══════════════════════════════════════════════════
STEP 1: cut1 = 1 (take 1 element from nums1)
═══════════════════════════════════════════════════
nums1: [1 | 2]     ← take 1 element
nums2: [3 | 4]     ← take 2-1=1 element

l1 = 1 (max of nums1's left)
r1 = 2 (min of nums1's right)
l2 = 3 (max of nums2's left)  
r2 = 4 (min of nums2's right)

Check: Is l1 ≤ r2? 1 ≤ 4 ✅
Check: Is l2 ≤ r1? 3 ≤ 2 ❌ FAIL!

l2 > r1 means we need MORE from nums1!
Move cut1 right: low = cut1 + 1 = 2

═══════════════════════════════════════════════════
STEP 2: cut1 = 2 (take 2 elements from nums1)
═══════════════════════════════════════════════════
nums1: [1, 2 |]    ← take all 2 elements
nums2: [| 3, 4]    ← take 0 elements

l1 = 2 (rightmost of nums1's left)
r1 = +∞ (nothing on right, use MAX_VALUE)
l2 = -∞ (nothing on left, use MIN_VALUE)
r2 = 3 (leftmost of nums2's right)

Check: Is l1 ≤ r2? 2 ≤ 3 ✅
Check: Is l2 ≤ r1? -∞ ≤ +∞ ✅

FOUND the partition!

maxLeft = max(l1, l2) = max(2, -∞) = 2
minRight = min(r1, r2) = min(+∞, 3) = 3

Total is even → median = (2 + 3) / 2 = 2.5
```

### Visual Diagram

```
Binary Search on partition:

nums1: [1, 2]              nums2: [3, 4]
        ├─┤                       ├─┤
       cut1=1                   cut2=1
    
Left half:  {1, 3}    max = 3
Right half: {2, 4}    min = 2

3 > 2? YES! Invalid partition.
Need more from nums1...

        ├───┤                   ┤
       cut1=2                 cut2=0
       
Left half:  {1, 2}    max = 2
Right half: {3, 4}    min = 3

2 ≤ 3? YES! Valid!
Median = (2 + 3) / 2 = 2.5 ✅
```

---

## Why This Problem is HARD 🔥

1. **Partition concept is tricky** — You're not searching for a value, but a split point
2. **Edge cases are brutal** — What if one array is empty? What if all elements of one are smaller?
3. **Off-by-one errors** — Even/odd total, which element goes where?
4. **Binary search condition** — Understanding when to go left vs right

---

## Complexity Analysis

| Solution | Time | Space | Correct? |
|----------|------|-------|----------|
| Brute Force (Merge) | O(m + n) | O(m + n) | ✅ But too slow |
| Two Pointers | O(m + n) | O(1) | ✅ But too slow |
| **Binary Search** | O(log(min(m,n))) | O(1) | ✅ Optimal |

---

## Key Takeaways

1. **Binary search on smaller array** keeps complexity O(log(min(m,n)))
2. **Partition = finding where to split** so left ≤ right
3. Use `MIN_VALUE` and `MAX_VALUE` for empty partitions
4. **Even total**: median = average(maxLeft, minRight)
5. **Odd total**: median = maxLeft
6. Key insight: **We don't merge; we find where to split!**
