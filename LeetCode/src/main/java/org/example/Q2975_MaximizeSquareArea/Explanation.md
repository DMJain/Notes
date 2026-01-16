# Maximum Square Area by Removing Fences From a Field - Explanation

> **Prerequisites**: Understanding of **HashSet** for O(1) lookups. Related to [Q2943 Maximize Square Hole Area](../Q2943_MaximizeSquareHoleArea/Explanation.md) but with a key difference.

> **Honorable Mention**: *Naive Enumeration*. Try all possible pairs of horizontal fences and all pairs of vertical fences to check if they form a square. This is essentially what we do, but we optimize with a HashSet.

## Problem in Simple Words

You have a field of size `(m-1) × (n-1)` with fences.
- **Horizontal fences** run left-to-right at specific Y-coordinates.
- **Vertical fences** run top-to-bottom at specific X-coordinates.
- The **boundary** fences at coordinates `1` and `m` (horizontal) and `1` and `n` (vertical) **cannot be removed**.

You can **remove** some internal fences to create a large open square area.
Find the **maximum square area** possible, or return `-1` if no square is possible.

---

---

## Difference from Q2943 🔑

In **Q2943**, we needed **consecutive** removable bars → Find longest streak.  
In **Q2975**, we can remove **any** fences, not just consecutive ones!

This changes everything. We need a completely different approach!

---

## Solution 1: Brute Force (Try All Combinations) ❌ (Too Slow)

### Approach
Try every possible combination of:
- Two horizontal fences (or boundaries)
- Two vertical fences (or boundaries)
- Check if they form a square

```
for h1 in all horizontal positions:
    for h2 in all horizontal positions (h2 > h1):
        for v1 in all vertical positions:
            for v2 in all vertical positions (v2 > v1):
                if (h2-h1) == (v2-v1):  // Same distance = square
                    maxArea = max(maxArea, (h2-h1) * (h2-h1))
```

### Why It's Bad
- H positions: up to 602 (600 fences + 2 boundaries)
- V positions: up to 602
- Total combinations: 602² × 602² ≈ **131 billion** checks!
- Even with early termination, too slow

> 💭 **We're checking every pair of H-fences against every pair of V-fences. That's O(H² × V²). Can we reduce this? What if we precompute something?**

---

## Solution 2: Why Q2943's Approach Fails Here ❌

### The Natural Thought
"In Q2943, we found consecutive bars. Maybe we can do the same?"

### Example Where Consecutive Fails ❌

```
m=6, n=6
hFences = [2, 5]  (non-consecutive!)
vFences = [2, 5]

Horizontal positions: [1, 2, 5, 6]
Vertical positions: [1, 2, 5, 6]

Q2943 approach (consecutive only):
  H-consecutive: [1,2] gap=1, [5,6] gap=1
  V-consecutive: [1,2] gap=1, [5,6] gap=1
  Max square: 1×1 = 1 ❌ WRONG!

CORRECT approach (any fences):
  We CAN use fences [1, 5]:
    Gap = 5-1 = 4
  Both horizontal and vertical have gap 4!
  Max square: 4×4 = 16 ✅

Why consecutive fails:
  Fences at [1, 2, 5, 6] can create gap of 4 by using [1, 5]
  But [1, 2] and [5, 6] are NOT consecutive to each other!
```

### Why Non-Consecutive Matters

```
Visual: Using fences 1 and 5 (skipping 2)

  1   2       5   6
  |   |       |   |   ← Vertical fences
  
  ┌───────────┐       ← Gap of 4!
  │           │
1 ─           ─       ← Horizontal 1
  │  SQUARE  │
  │  Area=16 │
2 ─           ─       ← Horizontal can be at 5
  │           │
  └───────────┘       ← Gap of 4!
5 ─           ─
  |           |
6 ─           ─

We SKIP fence 2, use fences [1, 5]
This creates a 4×4 square!
```

> 💭 **We can't use Q2943's consecutive approach. We need to consider ALL possible gaps formed by ANY two fences. How do we check if a gap exists in both dimensions efficiently?**

---

## Solution 3: HashSet for Gap Intersection ✅ (Optimal)

### The Connection 🔗
Let's trace our thinking:
- **Brute Force** was slow because: checking all H-pairs × V-pairs = O(H² × V²)
- **Q2943 method** failed because: non-consecutive fences matter!
- **What we need**: Find all H-gaps, all V-gaps, find their intersection efficiently

### The Key Insight 💡

A square is formed by:
1. Two horizontal fences (or boundaries) at distance `d` apart.
2. Two vertical fences (or boundaries) at distance `d` apart.

**Strategy**:
1. Include boundaries (`1` and `m` for horizontal, `1` and `n` for vertical).
2. Compute **all possible horizontal gaps** (difference between any two horizontal positions).
3. Compute **all possible vertical gaps** (difference between any two vertical positions).
4. Find the **largest gap that exists in both** horizontal and vertical.

---

## Visual Diagram: Finding Gaps

**Scenario**: `m=4, n=3, hFences=[2,3], vFences=[2]`

### Horizontal positions (including boundaries):
`h = [1, 2, 3, 4]`

**All possible gaps**:
```
1 to 2: gap = 1
1 to 3: gap = 2
1 to 4: gap = 3
2 to 3: gap = 1
2 to 4: gap = 2
3 to 4: gap = 1
```
Unique horizontal gaps: `{1, 2, 3}`

### Vertical positions (including boundaries):
`v = [1, 2, 3]`

**All possible gaps**:
```
1 to 2: gap = 1
1 to 3: gap = 2
2 to 3: gap = 1
```
Unique vertical gaps: `{1, 2}`

### Find Common Gaps
Horizontal ∩ Vertical = `{1, 2, 3}` ∩ `{1, 2}` = `{1, 2}`

**Maximum common gap**: `2`

**Square area**: `2 × 2 = 4`

---

## The Algorithm

```
1. Create arrays h and v:
   - h includes [1, ...hFences, m]
   - v includes [1, ...vFences, n]

2. Sort both arrays.

3. Compute all horizontal gaps:
   for i in 0..h.length:
       for j in i+1..h.length:
           add (h[j] - h[i]) to HashSet

4. Find maximum vertical gap that exists in HashSet:
   maxSide = -∞
   for i in 0..v.length:
       for j in i+1..v.length:
           if (v[j] - v[i]) in HashSet:
               maxSide = max(maxSide, v[j] - v[i])

5. If maxSide still -∞, return -1.
   Otherwise, return (maxSide * maxSide) % MOD.
```

---

## Complexity Analysis

| Solution | Time | Space | Correct? | Why? |
|----------|------|-------|----------|------|
| **HashSet Gaps** | O(H² + V²) | O(H²) | ✅ Optimal | H, V ≤ 602 (600 + 2 boundaries) |

- **Time**: O(H²) to compute all horizontal gaps, O(V²) to check vertical gaps. Total: O(H² + V²).
- **Space**: O(H²) for the HashSet storing horizontal gaps.

Given constraints (≤ 600 fences), this is very efficient.

---

## Key Takeaways

1. **Include Boundaries**: The corners `1` and `m`/`n` are valid fence positions for forming squares.
2. **All Gaps Matter**: Unlike Q2943, we don't need consecutive fences. Any two fences at distance `d` create a gap of size `d`.
3. **HashSet for Intersection**: Store one set of gaps, check the other against it for O(1) lookup.

---

## The Journey (TL;DR)

```
🐢 Brute Force: Check all H-pairs × V-pairs → TOO SLOW (O(H²V²))
         ↓
💡 "Try Q2943's consecutive approach?"
         ↓
❌ Consecutive: Misses [1,5] gap when fences=[1,2,5,6] → WRONG ANSWER
         ↓
💡 "Need ALL gaps, not just consecutive! How to find intersection?"
         ↓
✅ HashSet: Store all H-gaps, check V-gaps against it → OPTIMAL (O(H²+V²))
```

