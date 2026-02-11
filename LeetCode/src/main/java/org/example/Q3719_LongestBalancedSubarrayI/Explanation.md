# Longest Balanced Subarray I - Explanation

> **Prerequisites**: Array traversal, HashSet for distinct element tracking, Subarray enumeration patterns  
> **Related Problems**:  
> - [LeetCode 3 - Longest Substring Without Repeating Characters](https://leetcode.com/problems/longest-substring-without-repeating-characters/) | [Local](../Q0003_LongestSubstringWithoutRepeatingCharacters/Explanation.md) (Sliding window with distinct tracking — works here because chars are removed cleanly)  
> - [LeetCode 992 - Subarrays with K Different Integers](https://leetcode.com/problems/subarrays-with-k-different-integers/) | [Local](../Q0992_SubarraysWithKDistinct/Explanation.md) (Distinct counting in subarrays — uses atMost trick)  
> - [LeetCode 3721 - Longest Balanced Subarray II](https://leetcode.com/problems/longest-balanced-subarray-ii/) (Harder version: n ≤ 10⁵, needs segment tree)  
> - [LeetCode 3634 - Minimum Removals to Balance Array](https://leetcode.com/problems/minimum-removals-to-balance-array/) | [Local](../Q3634_MinimumRemovalsToBalanceArray/Explanation.md) (Even/odd balance concept)

---

## Problem in Simple Words

Find the longest subarray where the count of **unique even values** equals the count of **unique odd values**. For `[2, 5, 4, 3]`, the answer is 4 — it has 2 distinct evens `{2, 4}` and 2 distinct odds `{5, 3}`.

---

## Solution 1: Naïve Brute Force O(n³) ❌

### The Natural Thought

"For every possible subarray (l, r), I'll count distinct even and odd numbers by scanning all elements between l and r. If they're equal, I'll track the longest one."

### Approach

```python
def longestBalanced(nums):
    n = len(nums)
    maxLen = 0
    for l in range(n):
        for r in range(l, n):
            even_set = set()
            odd_set = set()
            for k in range(l, r + 1):        # ← Rebuild from scratch EVERY time!
                if nums[k] % 2 == 0:
                    even_set.add(nums[k])
                else:
                    odd_set.add(nums[k])
            if len(even_set) == len(odd_set):
                maxLen = max(maxLen, r - l + 1)
    return maxLen
```

### Why It's Bad

**Triple nested loop!** For each of the O(n²) subarray pairs, we scan up to O(n) elements to rebuild the even/odd sets. We're doing the SAME work over and over — when we go from subarray `[l..r]` to `[l..r+1]`, we throw away the previous sets and start from scratch instead of just adding one element.

### Example Where It's SLOW ❌

```
For n = 1500:
  Subarray pairs = n(n+1)/2 = 1500 × 1501 / 2 = 1,125,750
  Avg elements per pair = ~500
  Total ops ≈ 1,125,750 × 500 = 562,875,000 ops!
  ~563 MILLION operations → TLE!

For n = 1000:
  Pairs = 500,500 × avg 333 = ~166 million ops
```

### Complexity Analysis

| Solution | Time | Space | Status | Why |
|----------|------|-------|--------|-----|
| Naïve Brute Force | O(n³) | O(n) | ✅ TLE | Rebuild sets for every (l,r) pair |
| Sliding Window | O(n) | O(n) | ❌ Wrong | Can't handle "distinct" on shrink |
| **Optimized O(n²)** | **O(n²)** | **O(n)** | **✅ Accepted** | Incremental diff tracking |

> 💭 **"We're rebuilding even/odd sets from scratch for EVERY subarray. When going from [l..r] to [l..r+1], we only add ONE new element. What if we kept the sets and just added to them as we extend right?"**

---

## Solution 2: Sliding Window ❌

### The Natural Thought

"Sliding window works for most subarray + distinct problems (like LC 3 — Longest Substring Without Repeating Characters, or LC 992 — Subarrays with K Distinct). Let me try: expand right until balanced, shrink left to find longer windows..."

### Approach

```python
def longestBalanced(nums):
    n = len(nums)
    even_freq = {}    # value → count in current window
    odd_freq = {}
    maxLen = 0
    l = 0
    distinct_even = 0
    distinct_odd = 0
    
    for r in range(n):
        x = nums[r]
        if x % 2 == 0:
            even_freq[x] = even_freq.get(x, 0) + 1
            if even_freq[x] == 1:
                distinct_even += 1
        else:
            odd_freq[x] = odd_freq.get(x, 0) + 1
            if odd_freq[x] == 1:
                distinct_odd += 1
        
        # When to shrink? This is the problem!
        while distinct_even > distinct_odd and l <= r:
            # Try removing from left...
            remove(nums[l])
            l += 1
        
        if distinct_even == distinct_odd:
            maxLen = max(maxLen, r - l + 1)
    
    return maxLen
```

### Example Where It FAILS ❌

```
Input: [2, 3, 2, 5]

Step 1: r=0, x=2 (even) → distinct_even=1, distinct_odd=0, diff=+1
Step 2: r=1, x=3 (odd)  → distinct_even=1, distinct_odd=1, diff=0 ✅ maxLen=2
Step 3: r=2, x=2 (even, DUPLICATE) → distinct_even=1, distinct_odd=1, diff=0 ✅ maxLen=3
Step 4: r=3, x=5 (odd)  → distinct_even=1, distinct_odd=2, diff=-1

Now diff=-1 (more distinct odds). Try shrinking from left?

Shrink l=0→1: Remove nums[0]=2
  BUT nums[2] is ALSO 2! So distinct_even stays 1
  → distinct_even=1, distinct_odd=2 → STILL -1!

Shrink l=1→2: Remove nums[1]=3
  → distinct_even=1, distinct_odd=1 → diff=0 ✅ maxLen stays 3

But the CORRECT answer is 4: subarray [2,3,2,5] has
  distinct evens = {2} = 1
  distinct odds = {3,5} = 2 → NOT balanced!

And [2,3] = 2, already found.

Wait — for THIS input the answer IS 3. But the REAL problem 
is that sliding window doesn't know WHICH direction to shrink!
```

**A worse example**:
```
Input: [1, 2, 4, 3]

Full array: distinct_even={2,4}=2, distinct_odd={1,3}=2 → BALANCED, len=4

Sliding window:
  r=0: x=1 odd  → even=0, odd=1 → diff=-1
  r=1: x=2 even → even=1, odd=1 → diff=0 ✅ maxLen=2
  r=2: x=4 even → even=2, odd=1 → diff=+1

  Shrink? even > odd, so remove left:
  l=0→1: Remove 1 (odd) → even=2, odd=0 → diff=+2 → WORSE!

  The shrink made it WORSE! Sliding window assumes shrinking
  helps — but removing an odd made the imbalance bigger.

Expected: 4 | Sliding window gives: 2 ← WRONG!
```

### Why It Fails 🤯

1. **"Distinct" isn't monotonic on shrink** — Removing an element from the left might make things WORSE (removing an odd when we have too many evens increases imbalance!)

2. **No clear shrink direction** — In problems like LC 3, you always shrink left. Here, the "balance" can swing both ways. Shrinking might help OR hurt — you can't predict which.

3. **Shrinking doesn't cleanly undo distinct status** — Even with frequency maps, removing one occurrence of a value doesn't remove it from "distinct" count if there are more copies in the window.

> 💭 **"Sliding window fails because balance isn't monotonic — shrinking can make imbalance WORSE. And 'distinct' doesn't undo cleanly. O(n) seems impossible for this problem. Can we at least make the O(n²) approach fast by tracking the difference incrementally instead of rebuilding sets?"**

---

## Solution 3: Optimized O(n²) with Diff Trick ✅

### The Connection 🔗

Let's trace our thinking:
- **Naïve O(n³)** was slow because: we rebuilt even/odd sets from scratch for every (l, r) pair
- **Sliding window** failed because: "distinct" isn't monotonic — shrinking can make imbalance worse, and distinct status doesn't undo cleanly
- **What we need**: Keep the O(n²) two-loop structure (since O(n) is impossible), but eliminate the inner rebuild → **extend right and track diff incrementally!**

### The Key Insight 💡

Instead of maintaining two separate sets and comparing sizes, use **one variable**:

```
diff = distinctEven - distinctOdd
```

As we extend the right pointer:
- If `nums[r]` is **new** (not yet in `seen` set):
  - Even number → `diff += 1`
  - Odd number → `diff -= 1`
- If `nums[r]` is **already seen** → do nothing (it's a duplicate, doesn't change distinct count)

**`diff == 0` means balanced!** Because `distinctEven - distinctOdd = 0` ⟹ `distinctEven == distinctOdd`.

**Bonus optimization**: If we've already found a `maxLen` of, say, 10, then any starting index `l` where `n - l < 10` can never produce a longer subarray. So `if (l > n - maxLen) break`.

### The Algorithm

1. Initialize `maxLen = 0`
2. For each starting index `l` from `0` to `n-1`:
   - a. **Prune**: If `l > n - maxLen`, break — remaining elements can't beat current best
   - b. Reset `diff = 0`, clear `seen` set
   - c. For each ending index `r` from `l` to `n-1`:
     - If `nums[r]` NOT in `seen`: add it, update `diff` (+1 if even, -1 if odd)
     - If `diff == 0`: `maxLen = max(maxLen, r - l + 1)`
3. Return `maxLen`

### Step-by-Step Walkthrough

**Example 1**: `[2, 5, 4, 3]` → Expected: 4

```
l=0: seen={}, diff=0
┌─────────────────────────────────────────────────────────────────────────┐
│ r │ x │ New?  │ Parity │ diff change     │ seen        │ diff │ max  │
├───┼───┼───────┼────────┼─────────────────┼─────────────┼──────┼──────┤
│ 0 │ 2 │ ✅ Yes│ even   │ 0 + 1 = +1      │ {2}         │ +1   │  0   │
│ 1 │ 5 │ ✅ Yes│ odd    │ +1 - 1 = 0      │ {2,5}       │  0 ✅│  2   │
│ 2 │ 4 │ ✅ Yes│ even   │ 0 + 1 = +1      │ {2,5,4}     │ +1   │  2   │
│ 3 │ 3 │ ✅ Yes│ odd    │ +1 - 1 = 0      │ {2,5,4,3}   │  0 ✅│  4   │
└─────────────────────────────────────────────────────────────────────────┘

l=1: l=1, n-maxLen=4-4=0, l > 0? YES → BREAK (early termination!)

Result: maxLen = 4 ✅
```

**Example 2**: `[3, 2, 2, 5, 4]` → Expected: 5

```
l=0: seen={}, diff=0
┌─────────────────────────────────────────────────────────────────────────┐
│ r │ x │ New?  │ Parity │ diff change     │ seen        │ diff │ max  │
├───┼───┼───────┼────────┼─────────────────┼─────────────┼──────┼──────┤
│ 0 │ 3 │ ✅ Yes│ odd    │ 0 - 1 = -1      │ {3}         │ -1   │  0   │
│ 1 │ 2 │ ✅ Yes│ even   │ -1 + 1 = 0      │ {3,2}       │  0 ✅│  2   │
│ 2 │ 2 │ ❌ No │  —     │ no change       │ {3,2}       │  0 ✅│  3   │
│ 3 │ 5 │ ✅ Yes│ odd    │ 0 - 1 = -1      │ {3,2,5}     │ -1   │  3   │
│ 4 │ 4 │ ✅ Yes│ even   │ -1 + 1 = 0      │ {3,2,5,4}   │  0 ✅│  5   │
└─────────────────────────────────────────────────────────────────────────┘

l=1: l=1, n-maxLen=5-5=0, l > 0? YES → BREAK

Result: maxLen = 5 ✅

Notice r=2: nums[2]=2 is already in seen → diff stays 0 → still balanced!
The duplicate 2 extends the subarray length WITHOUT changing the distinct count.
```

**Example 3**: `[1, 2, 3, 2]` → Expected: 3

```
l=0: seen={}, diff=0
┌─────────────────────────────────────────────────────────────────────────┐
│ r │ x │ New?  │ Parity │ diff change     │ seen        │ diff │ max  │
├───┼───┼───────┼────────┼─────────────────┼─────────────┼──────┼──────┤
│ 0 │ 1 │ ✅ Yes│ odd    │ 0 - 1 = -1      │ {1}         │ -1   │  0   │
│ 1 │ 2 │ ✅ Yes│ even   │ -1 + 1 = 0      │ {1,2}       │  0 ✅│  2   │
│ 2 │ 3 │ ✅ Yes│ odd    │ 0 - 1 = -1      │ {1,2,3}     │ -1   │  2   │
│ 3 │ 2 │ ❌ No │  —     │ no change       │ {1,2,3}     │ -1   │  2   │
└─────────────────────────────────────────────────────────────────────────┘

l=1: seen={}, diff=0
┌─────────────────────────────────────────────────────────────────────────┐
│ r │ x │ New?  │ Parity │ diff change     │ seen        │ diff │ max  │
├───┼───┼───────┼────────┼─────────────────┼─────────────┼──────┼──────┤
│ 1 │ 2 │ ✅ Yes│ even   │ 0 + 1 = +1      │ {2}         │ +1   │  2   │
│ 2 │ 3 │ ✅ Yes│ odd    │ +1 - 1 = 0      │ {2,3}       │  0 ✅│  3   │
│ 3 │ 2 │ ❌ No │  —     │ no change       │ {2,3}       │  0 ✅│  3   │
└─────────────────────────────────────────────────────────────────────────┘

l=2: l=2, n-maxLen=4-3=1, l > 1? YES → BREAK

Result: maxLen = 3 ✅ (subarray [2,3,2] — 1 distinct even {2}, 1 distinct odd {3})
```

### Visual Diagram

**Example 1: `[2, 5, 4, 3]`**

```
Array:  ┌─────┬─────┬─────┬─────┐
        │  2  │  5  │  4  │  3  │
        │even │ odd │even │ odd │
        └─────┴─────┴─────┴─────┘
Index:    0     1     2     3

l=0, scanning right →

  r=0: [2]
       diff: ──→ +1
       ┌─────┐
       │  2  │  distinctEven=1, distinctOdd=0
       └─────┘

  r=1: [2, 5]
       diff: +1 ──→ 0 ✅ BALANCED (maxLen=2)
       ┌─────┬─────┐
       │  2  │  5  │  distinctEven=1, distinctOdd=1
       └─────┴─────┘

  r=2: [2, 5, 4]
       diff: 0 ──→ +1
       ┌─────┬─────┬─────┐
       │  2  │  5  │  4  │  distinctEven=2, distinctOdd=1
       └─────┴─────┴─────┘

  r=3: [2, 5, 4, 3]
       diff: +1 ──→ 0 ✅ BALANCED (maxLen=4)
       ┌─────┬─────┬─────┬─────┐
       │  2  │  5  │  4  │  3  │  distinctEven=2, distinctOdd=2
       └─────┴─────┴─────┴─────┘

Diff over time (l=0):
  +1 ──→ 0 ──→ +1 ──→ 0
   ↑      ↑      ↑      ↑
  r=0    r=1    r=2    r=3
         ✅            ✅
```

**Example 2: `[3, 2, 2, 5, 4]` — Showing duplicate behavior**

```
Array:  ┌─────┬─────┬─────┬─────┬─────┐
        │  3  │  2  │  2  │  5  │  4  │
        │ odd │even │even │ odd │even │
        └─────┴─────┴─────┴─────┴─────┘
                      ↑ DUPLICATE — doesn't change diff!

l=0, diff over time:
  -1 ──→ 0 ──→ 0 ──→ -1 ──→ 0
   ↑      ↑      ↑      ↑      ↑
  r=0    r=1    r=2    r=3    r=4
         ✅     ✅             ✅
         len=2  len=3          len=5

Key: r=2 (nums[2]=2) is already in seen → skip it!
     diff stays 0 → subarray [3,2,2] is still balanced
     because distinct counts haven't changed (still 1 even, 1 odd)
```

---

## Complexity Analysis (Optimal)

| Solution | Time | Space | Status | Why |
|----------|------|-------|--------|-----|
| Naïve Brute Force | O(n³) | O(n) | ✅ TLE | Rebuild sets for every (l,r) |
| Sliding Window | O(?) | O(n) | ❌ Wrong | Balance not monotonic, distinct not reversible |
| **Optimized O(n²)** | **O(n²)** | **O(n)** | **✅ Accepted** | Incremental diff + early termination |

**Quantified for n = 1500**:
- Naïve O(n³): ~563 million ops → TLE
- Optimized O(n²): ~1,125,000 ops (worst case without early termination) → **~500× faster**
- With early termination: even fewer iterations — once a large answer is found, remaining starting indices are pruned

---

## Key Takeaways

1. **"Distinct" breaks sliding window** — Unlike frequency-based problems (LC 3, LC 992 use tricks to handle this), you can't cleanly undo "distinct" status when shrinking. Removing an element might not change distinct count (duplicates), and balance can swing both ways.

2. **Diff trick: collapse two counters into one** — Instead of tracking `distinctEven` and `distinctOdd` separately and comparing, use `diff = distinctEven - distinctOdd`. Check `diff == 0` for balance. This pattern works for any "equal count of two categories" problem.

3. **Early termination prunes O(n²)** — `if (l > n - maxLen) break` skips all starting positions that can't possibly produce a longer subarray. In practice, this turns O(n²) closer to O(n) for inputs where the answer is close to n.

---

## The Journey (TL;DR)

```
🐢 Naïve O(n³) → Rebuild sets per pair (563M ops for n=1500)
         ↓
💡 "Why rebuild? Just extend right and ADD to the set..."
         ↓
❌ Sliding Window → WRONG (balance isn't monotonic, shrink makes it worse!)
         ↓
💡 "O(n) impossible for distinct. Optimize O(n²) — track diff incrementally?"
         ↓
✅ Optimized O(n²) → diff trick + early termination, ACCEPTED!
```
