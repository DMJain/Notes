# Minimum Pair Removal to Sort Array II - Explanation

> **Prerequisites**: 
> - Priority Queue (Min-Heap) operations
> - Doubly Linked List for efficient node removal
> - Understanding array "violations" (places where arr[i] > arr[i+1])

> **Related Problems**: 
> - [LeetCode 3509 - Minimum Pair Removal to Sort Array I](https://leetcode.com/problems/minimum-pair-removal-to-sort-array-i/) (Easier version, smaller constraints)
> - [LeetCode 23 - Merge K Sorted Lists](https://leetcode.com/problems/merge-k-sorted-lists/) (Heap for selecting minimum)
> - [LeetCode 146 - LRU Cache](https://leetcode.com/problems/lru-cache/) (Doubly linked list removal pattern)

## Problem in Simple Words

Repeatedly merge the adjacent pair with minimum sum until the array is non-decreasing (sorted). Count how many merges are needed.

**Example**: `[5,2,3,1]` → Merge `(3,1)=4` → `[5,2,4]` → Merge `(2,4)=6` → `[5,6]` ✅ Answer: **2 operations**

---

## Solution 1: Brute Force ❌ (TLE)

### The Natural Thought
"Just simulate it! Each step: scan all pairs, find minimum sum, merge, repeat until sorted."

### Approach
```java
while (!isSorted(arr)) {
    // Find pair with minimum sum (O(n) scan)
    int minIdx = 0;
    for (int i = 1; i < arr.length - 1; i++) {
        if (arr[i] + arr[i+1] < arr[minIdx] + arr[minIdx+1]) {
            minIdx = i;
        }
    }
    // Merge pair (costs O(n) to shift elements)
    arr[minIdx] = arr[minIdx] + arr[minIdx+1];
    removeElement(arr, minIdx + 1);
    ops++;
}
```

### Why It's Bad
Each operation:
- Scanning for min: **O(n)**
- Removing element (array shift): **O(n)**
- Checking if sorted: **O(n)**

Total per operation: **O(n)**

In worst case, we do **O(n)** operations (merge all pairs down to 1 element).

**Total: O(n) × O(n) = O(n²)**

### Example Where It's SLOW ❌
```
Input: n = 100,000 elements in descending order [100000, 99999, ..., 1]

Each step = O(n) work
Total steps ≈ n-1 ≈ 100,000 steps

Operations = 100,000 × 100,000 = 10 BILLION ops!
Time limit: ~100 seconds (way over 1-2 second limit)
```

### Complexity Analysis

| Solution | Time | Space | Correct? | Why? |
|----------|------|-------|----------|------|
| **Brute Force** | O(n²) | O(1) | ✅ TLE | 10B ops for n=10⁵ |
| Optimal | O(n log n) | O(n) | ✅ | See below |

> 💭 **The expensive part is finding the minimum pair each time. What if we used a heap to always get the minimum in O(log n)?**

---

## Solution 2: Naive Heap Without Staleness Check ❌ (Wrong Answer)

### The Natural Thought
"Use a min-heap! Push all pairs, pop the minimum, merge. O(log n) per operation instead of O(n)!"

### Approach
```java
PriorityQueue<int[]> heap = new PriorityQueue<>((a,b) -> a[0] - b[0]);
// {sum, leftIndex}

for (int i = 0; i < n - 1; i++) {
    heap.add(new int[]{nums[i] + nums[i+1], i});
}

while (!isSorted(nums)) {
    int[] pair = heap.poll();  // Get min sum pair
    int sum = pair[0], idx = pair[1];
    
    // Merge at idx
    nums[idx] = sum;
    removeElement(nums, idx + 1);
    
    // Add new pairs... BUT WAIT!
}
```

### Example Where It FAILS ❌
```
Input: [5, 2, 3, 1]

Initial heap: {(3,0), (5,1), (4,2)}  ← (sum, index)
               [5+2]  [2+3]  [3+1]

Step 1: Pop (3, 0) → means pair at index 0 = (5,2), sum=7? NO!
        The actual sum at index 0 is 5+2=7, but we stored 3!
        
WAIT - the heap entry (3,0) doesn't mean anything valid!
After removing element at index 2, indices SHIFT!

Old: [5, 2, 3, 1]     indices: 0, 1, 2, 3
New: [5, 2, 4]        indices: 0, 1, 2

But heap still has entries with OLD indices!
```

### Why It Fails 🤯
**Problem 1**: When we merge a pair and remove an element, **all indices shift**. Old heap entries become stale/wrong.

**Problem 2**: We can't easily update existing heap entries in O(1).

> 💭 **We need a way to (1) avoid index shifting, and (2) detect stale heap entries. What if we use a linked list to avoid shifting, and validate heap entries before using them?**

---

## Solution 3: Heap + Doubly Linked List ✅ (Optimal)

### The Connection 🔗
Let's trace our thinking:
- **Brute Force** was slow because: Finding min pair is O(n) each time
- **Naive Heap** failed because: Array indices shift after removal, heap entries become stale
- **What we need**: 
  1. A data structure where removal doesn't shift indices → **Doubly Linked List!**
  2. A way to detect stale heap entries → **Validate before using!**
  3. Avoid checking "is sorted?" every step (O(n)) → **Track violation count!**

### The Key Insight 💡

**Three Key Ideas:**

1. **Doubly Linked List**: Instead of shifting elements, just update `left[i]` and `right[i]` pointers. "Deleting" just means adjusting neighbor pointers!

2. **Stale Entry Detection**: When we pop from heap, verify:
   - Is `right[i]` still pointing to a valid neighbor?
   - Is `left[right[i]]` still `i`? (the link is bidirectional)
   - Is `a[i] + a[right[i]]` still equal to the stored sum?

3. **Violation Counter**: Instead of O(n) `isSorted()`, track how many adjacent pairs have `a[i] > a[i+1]`. When this counter reaches **0**, we're done!

### The Algorithm
```
1. Initialize:
   - Copy nums to long[] a (prevent overflow)
   - Create left[], right[] arrays as doubly linked list
   - Count initial violations (pairs where a[i] > a[i+1])
   - Push all pairs to min-heap

2. While violations > 0:
   a. Pop minimum pair (sum, i) from heap
   
   b. Validate entry:
      - Skip if right[i] == -1 (no neighbor)
      - Skip if left[right[i]] != i (link broken)
      - Skip if sum != a[i] + a[right[i]] (stale sum)
   
   c. Update violations (BEFORE merge):
      - If left neighbor existed and was a violation, decrement
      - If current pair was a violation, decrement
      - If right neighbor existed and was a violation, decrement
   
   d. Merge: a[i] = sum, update linked list pointers
   
   e. Update violations (AFTER merge):
      - Check if new left relationship is a violation
      - Check if new right relationship is a violation
      - Add new pair entries to heap
   
   f. ans++

3. Return ans
```

### Step-by-Step Walkthrough

**Input:** `[5, 2, 3, 1]`

```
Initial State:
┌───────────────────────────────────────┐
│  Values:    5      2      3      1    │
│  Indices:   0      1      2      3    │
│  left[]:   -1      0      1      2    │
│  right[]:   1      2      3     -1    │
└───────────────────────────────────────┘

Violations: (5>2)✓, (2>3)✗, (3>1)✓ → rest = 2

Heap: [(3,2), (5,1), (7,0)]  ← sorted by sum
       3+1=4   2+3=5  5+2=7
       Wait, should be sum 4, not 3!
       
Actually: [(4,2), (5,1), (7,0)]
```

**Step 1: Pop (4, 2)** → Pair at index 2 = (3,1), sum=4 ✅
```
Before merge:
- Check left[3] == 2? YES (valid link)
- Check a[2] + a[3] == 4? YES (3+1=4)

Violations update (BEFORE):
- (a[1]=2) > (a[2]=3)? NO → no change
- (a[2]=3) > (a[3]=1)? YES → rest-- (now rest=1)
- a[3] has no right neighbor

Merge: a[2] = 4, "delete" index 3
       right[2] = right[3] = -1
       
After:
┌───────────────────────────────────────┐
│  Values:    5      2      4     [X]   │
│  Indices:   0      1      2      3    │
│  left[]:   -1      0      1     -1    │
│  right[]:   1      2     -1     -1    │
└───────────────────────────────────────┘

Violations update (AFTER):
- New pair (1,2): (a[1]=2) > (a[2]=4)? NO → no new violation
- No right neighbor for index 2

Add new heap entry: (a[1]+a[2], 1) = (6, 1)
Heap: [(5,1), (7,0), (6,1)]  ← (5,1) is now STALE!

ans = 1
```

**Step 2: Pop (5, 1)** → STALE! ❌
```
Check: a[1] + a[right[1]] = 2 + 4 = 6 ≠ 5
Skip this entry!
```

**Step 3: Pop (6, 1)** → Pair at index 1 = (2,4), sum=6 ✅
```
Before merge:
- Check right[1] = 2, left[2] = 1? YES
- Check a[1] + a[2] = 2 + 4 = 6? YES

Violations update (BEFORE):
- (a[0]=5) > (a[1]=2)? YES → rest-- (now rest=0!)
- (a[1]=2) > (a[2]=4)? NO
- a[2] has no right neighbor

Merge: a[1] = 6, "delete" index 2
       right[1] = right[2] = -1
       
After:
┌───────────────────────────────────────┐
│  Values:    5      6     [X]    [X]   │
│  Indices:   0      1      2      3    │
│  left[]:   -1      0     -1     -1    │
│  right[]:   1     -1     -1     -1    │
└───────────────────────────────────────┘

Violations update (AFTER):
- (a[0]=5) > (a[1]=6)? NO → no new violation
- No right neighbor

rest = 0 → DONE!
ans = 2
```

### Visual Diagram

```
Initial:  [5] ←→ [2] ←→ [3] ←→ [1]
           ↓ violation  ↓ violation
          rest = 2

Step 1:   [5] ←→ [2] ←→ [4]        (merged 3+1)
           ↓ violation only
          rest = 1, ans = 1

Step 2:   [5] ←→ [6]               (merged 2+4)
           ✓ sorted!
          rest = 0, ans = 2

Linked List Pointer Update:
┌─────────────────────────────────────────────────────────┐
│   Before removing node r:                               │
│   ... ←→ [i] ←→ [r] ←→ [rr] ←→ ...                     │
│                                                         │
│   After removing node r:                                │
│   ... ←→ [i] ←────────→ [rr] ←→ ...                    │
│          a[i] = a[i] + a[r]                             │
│          right[i] = rr                                  │
│          left[rr] = i                                   │
└─────────────────────────────────────────────────────────┘
```

### Code
```java
public int minimumPairRemoval(int[] nums) {
    int n = nums.length;
    if (n <= 1) return 0;

    // Use long to prevent overflow
    long[] a = new long[n];
    for (int i = 0; i < n; i++) a[i] = nums[i];

    // Doubly linked list pointers
    int[] left = new int[n];
    int[] right = new int[n];
    for (int i = 0; i < n; i++) {
        left[i] = i - 1;
        right[i] = (i + 1 < n) ? i + 1 : -1;
    }

    // Min-heap: (sum, leftIndex), break ties by index
    PriorityQueue<long[]> heap = new PriorityQueue<>((p, q) -> {
        if (p[0] != q[0]) return Long.compare(p[0], q[0]);
        return Long.compare(p[1], q[1]);
    });
    for (int i = 0; i < n - 1; i++) 
        heap.add(new long[]{a[i] + a[i + 1], i});

    // Count violations
    int rest = 0;
    for (int i = 0; i < n - 1; i++) 
        if (a[i] > a[i + 1]) rest++;

    int ans = 0;

    while (rest > 0) {
        long[] cur = heap.poll();
        long v = cur[0];
        int i = (int) cur[1];
        int r = right[i];

        // Validate: skip stale entries
        if (r == -1 || left[r] != i || a[i] + a[r] != v) continue;

        int li = left[i], rr = right[r];

        // Decrement violations that will be resolved
        if (li != -1 && right[li] == i && a[li] > a[i]) rest--;
        if (a[i] > a[r]) rest--;
        if (rr != -1 && left[rr] == r && a[r] > a[rr]) rest--;

        // Merge: update value and linked list
        a[i] = v;
        right[i] = rr;
        if (rr != -1) left[rr] = i;
        left[r] = right[r] = -1;  // Mark as deleted

        // Increment if new violations created
        if (li != -1 && right[li] == i && a[li] > a[i]) rest++;
        if (rr != -1 && left[rr] == i && a[i] > a[rr]) rest++;

        // Add new pairs to heap
        if (li != -1 && right[li] == i) heap.add(new long[]{a[li] + a[i], li});
        if (rr != -1 && left[rr] == i) heap.add(new long[]{a[i] + a[rr], i});

        ans++;
    }

    return ans;
}
```

---

## Complexity Analysis

| Solution | Time | Space | Correct? | Why? |
|----------|------|-------|----------|------|
| Brute Force | O(n²) | O(1) | ✅ TLE | 10B ops for n=10⁵ |
| Naive Heap | O(n log n) | O(n) | ❌ Wrong | Stale indices |
| **Heap + Linked List** | **O(n log n)** | **O(n)** | ✅ **Optimal** | Each element processed once |

**Time Breakdown (Optimal)**:
- Each element is added to heap at most once → O(n) pushes
- Each element is merged at most once → O(n) pops
- Each heap operation is O(log n)
- Total: O(n log n)

**Space**: O(n) for the heap, left[], right[], and a[] arrays.

---

## Key Takeaways

1. **Doubly Linked List for Dynamic Removal**: When elements are removed frequently and you need to preserve neighbors, linked list beats array.

2. **Lazy Heap Deletion**: Instead of removing stale entries (expensive), just validate when popping. Skip invalid entries.

3. **Violation Counter**: Don't check O(n) condition every step. Track the invariant incrementally!

4. **Overflow Prevention**: With n=10⁵ and values up to 10⁹, sums can exceed int. Use `long`.

---

## The Journey (TL;DR)

```
🐢 Brute Force → O(n²) TLE (finding min is O(n) each step)
         ↓
💡 "Use a heap to get min in O(log n)!"
         ↓
❌ Naive Heap → WRONG (array shifts break indices)
         ↓
💡 "What if we don't shift? Use linked list pointers!"
💡 "What if we detect stale entries instead of updating?"
💡 "What if we count violations instead of checking sorted?"
         ↓
✅ Heap + Doubly Linked List → O(n log n) works!
```
