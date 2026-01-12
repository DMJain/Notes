# Maximum Number of Points with Cost - Explanation

> **Prerequisites**: This uses a classic DP optimization: breaking an O(n²) transition into O(n) using left/right prefix scans. Similar pattern appears in [Q0042 Trapping Rain Water](https://leetcode.com/problems/trapping-rain-water/) and [Q0135 Candy](https://leetcode.com/problems/candy/).

## Problem in Simple Words

You have a grid of points. You need to pick **one cell from each row** to maximize your total score.

**The Catch:**
- You get points for the cell you pick.
- But you **lose points** based on how far you move horizontally between rows.
- Penalty = `abs(col_prev - col_curr)`

**Example**:
Row 0: Pick col 2 (value 3)
Row 1: Pick col 1 (value 5)
Score: 3 + 5 - abs(2 - 1) = 8 - 1 = 7

---

## Solution 1: Brute Force DP ❌ (Too Slow)

### Approach
For each cell in the current row, check **every** cell in the previous row to find the best transition.

`dp[r][c]` = Max score ending at row `r`, col `c`

```java
dp[r][c] = points[r][c] + max(dp[r-1][k] - abs(c - k))
// for all k from 0 to cols-1
```

### Why It's Bad
- For each cell, we iterate all columns `k`.
- Complexity: **O(Rows × Cols × Cols)**
- Constraints: Rows × Cols ≤ 10^5.
- If grid is 1 × 100,000 → 10^10 operations! **TLE**.

> 💭 **For each cell, we're checking ALL previous cells. The `abs(c - k)` makes it tricky. Can we break this into cases?**

---

## Solution 2: Optimized DP with Left/Right Passes ✅ (Optimal)

### The Connection 🔗
Let's trace our thinking:
- **Brute Force** was slow because: checking all previous columns = O(Cols²)
- **Key insight**: The `abs(c - k)` creates two cases — coming from left vs coming from right!
- **What we need**: precompute best value from left and best from right separately

### The Key Insight 💡
The penalty `abs(c - k)` makes the equation tricky. Let's break the absolute value!

We want to maximize: `dp[r-1][k] - abs(c - k)`

**Case 1: Previous cell `k` is to the LEFT (k ≤ c)**
- `abs(c - k) = c - k`
- Maximize: `dp[r-1][k] - (c - k)`
- Maximize: `(dp[r-1][k] + k) - c`
- Since `c` is constant for the current cell, we just need the max of `(dp[r-1][k] + k)` seen so far from the left!

**Case 2: Previous cell `k` is to the RIGHT (k > c)**
- `abs(c - k) = k - c`
- Maximize: `dp[r-1][k] - (k - c)`
- Maximize: `(dp[r-1][k] - k) + c`
- We need the max of `(dp[r-1][k] - k)` seen so far from the right!

### The Algorithm

For each row, we do two sweeps to calculate potential previous scores:

1. **Left-to-Right Pass**:
   - `leftMax[i] = max(leftMax[i-1], prev_dp[i] + i)`
   - Keeps track of best score coming from the left (accounting for distance decay).

2. **Right-to-Left Pass**:
   - `rightMax[i] = max(rightMax[i+1], prev_dp[i] - i)`
   - Keeps track of best score coming from the right.

3. **Calculate Current DP**:
   - `new_dp[i] = points[r][i] + max(leftMax[i] - i, rightMax[i] + i)`

---

## Step-by-Step Walkthrough

**points = `[[1, 2, 3], [1, 5, 1], [3, 1, 1]]`**

### Row 0 (Base Case)
`dp` = `[1, 2, 3]`

### Row 1: `[1, 5, 1]`

**1. Left Pass (Maximize `dp[k] + k`)**
- `dp` indices: 0, 1, 2
- `dp` values: 1, 2, 3
- `dp[k] + k`:
  - k=0: 1 + 0 = 1
  - k=1: 2 + 1 = 3
  - k=2: 3 + 2 = 5

`leftMax` (running max of above):
- i=0: **1**
- i=1: max(1, 3) = **3**
- i=2: max(3, 5) = **5**

**2. Right Pass (Maximize `dp[k] - k`)**
- `dp[k] - k`:
  - k=0: 1 - 0 = 1
  - k=1: 2 - 1 = 1
  - k=2: 3 - 2 = 1

`rightMax` (running max from right):
- i=2: **1**
- i=1: max(1, 1) = **1**
- i=0: max(1, 1) = **1**

**3. Calculate New DP**
`new_dp[i] = points[1][i] + max(leftMax[i] - i, rightMax[i] + i)`

- **i=0 (val=1)**:
  - From Left: `leftMax[0] - 0` = 1 - 0 = 1
  - From Right: `rightMax[0] + 0` = 1 + 0 = 1
  - Best Prev: 1
  - Total: 1 + 1 = **2**

- **i=1 (val=5)**:
  - From Left: `leftMax[1] - 1` = 3 - 1 = 2
  - From Right: `rightMax[1] + 1` = 1 + 1 = 2
  - Best Prev: 2
  - Total: 5 + 2 = **7**

- **i=2 (val=1)**:
  - From Left: `leftMax[2] - 2` = 5 - 2 = 3
  - From Right: `rightMax[2] + 2` = 1 + 2 = 3
  - Best Prev: 3
  - Total: 1 + 3 = **4**

`dp` is now `[2, 7, 4]`

### Row 2: `[3, 1, 1]`

**1. Left Pass (`dp[k] + k`)**
- k=0: 2+0=2
- k=1: 7+1=8
- k=2: 4+2=6
- `leftMax`: `[2, 8, 8]`

**2. Right Pass (`dp[k] - k`)**
- k=0: 2-0=2
- k=1: 7-1=6
- k=2: 4-2=2
- `rightMax`: `[6, 6, 2]`

**3. Calculate New DP**
- **i=0 (val=3)**: max(2-0, 6+0) + 3 = 6 + 3 = **9**
- **i=1 (val=1)**: max(8-1, 6+1) + 1 = 7 + 1 = **8**
- **i=2 (val=1)**: max(8-2, 2+2) + 1 = 6 + 1 = **7**

`dp` is now `[9, 8, 7]`

### Final Result
Max of `[9, 8, 7]` is **9**. ✅

---

## Visual: How Values Propagate

Think of `leftMax` as "carrying" the best value from the left, but it decays by -1 for every step it moves right.

```
Row 0:  [ 1,  2,  3 ]
          ↘   ↓   ↙
Row 1:    (decaying influence)

If we just look at the value '3' at col 2 in Row 0:
- At col 2 (below it): Value is 3
- At col 1 (left of it): Value is 3 - 1 = 2
- At col 0 (left of it): Value is 3 - 2 = 1

The left/right passes efficiently calculate this "decaying max" for ALL cells simultaneously!
```

---

## Complexity Analysis

| Solution | Time | Space | Correct? | Why? |
|----------|------|-------|----------|------|
| Brute Force | O(M × N²) | O(N) | ✅ TLE | Check all prev cols |
| **Left/Right Pass** | O(M × N) | O(N) | ✅ **Optimal** | 3 linear passes per row |

- **M** = Rows, **N** = Cols
- We do 3 passes (Left, Right, Final) per row → Linear time!

---

## Key Takeaways

1. **Absolute Difference** usually implies two cases: `x - y` and `y - x`
2. **Break the dependency**: Separate `dp[k]` and `k` terms
3. **Prefix/Suffix Max**: A common technique to optimize "best previous" queries
4. **Space Optimization**: We only need the previous row's DP array

---

## The Journey (TL;DR)

```
🐢 Brute Force: Check all previous columns → TOO SLOW (O(M × N²))
         ↓
💡 "The abs() creates two cases: left vs right!"
         ↓
📊 Left/Right Passes: Precompute best from each direction
         ↓
✅ Optimized DP: Linear per row → OPTIMAL (O(M × N))
```
