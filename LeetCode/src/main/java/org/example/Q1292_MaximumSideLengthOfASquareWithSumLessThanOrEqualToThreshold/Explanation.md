# 1292. Maximum Side Length of a Square with Sum Less than or Equal to Threshold - Explanation

> **Prerequisites**: [Prefix Sums](https://en.wikipedia.org/wiki/Prefix_sum), [Binary Search](https://en.wikipedia.org/wiki/Binary_search_algorithm)
> **Related Problems**: [LeetCode 2428 - Maximum Sum of an Hourglass](https://leetcode.com/problems/maximum-sum-of-an-hourglass/) | [LeetCode 221 - Maximal Square](https://leetcode.com/problems/maximal-square/) | [LeetCode 304 - Range Sum Query 2D - Immutable](https://leetcode.com/problems/range-sum-query-2d-immutable/)

## Problem in Simple Words

Given a grid of numbers and a threshold value, we need to find the largest square (e.g., 2x2, 3x3) inside the grid such that the sum of all numbers in that square is less than or equal to the threshold. If no such square exists (even 1x1), return 0.

**Example:**
```
Grid:
1 1 1
1 1 1
1 1 1
Threshold = 4

A 2x2 square has sum 1+1+1+1 = 4. 4 <= 4. Valid.
A 3x3 square has sum 9. 9 > 4. Invalid.
Output: 2
```

---

## Solution 1: Brute Force ❌

### Approach
We can iterate through every possible square in the matrix.
1. Pick every cell `(i, j)` as the top-left corner.
2. For each corner, try every possible side length `k`.
3. Calculate the sum of the square manually by iterating over its cells.
4. If the sum is <= threshold, update the maximum side length.

### Why It's Bad
We are re-calculating the sum for overlapping squares repeatedly.
The complexity is huge.

### Example Where It's SLOW ❌
```
Grid size: 300 x 300 (Input Constraints)
For each cell (300*300 = 90,000), we try side lengths up to 300.
Summing a 300x300 square takes 300*300 = 90,000 operations.
Total operations ≈ 300 * 300 * 300 * 300 * 300 ≈ 2.4 * 10^12 operations!
This will definitely Time Limit Exceed (TLE).
```

### Complexity Analysis

| Solution | Time | Space | Correct? | Why? |
|----------|------|-------|----------|------|
| Brute Force | O(m\*n\*min(m,n)^3) | O(1) | ✅ TLE | Re-calculating sums is expensive. |
| **Optimal** | **O(m\*n)** | **O(m\*n)** | ✅ **Optimal** | Prefix Sums + Smart Iteration. |

> 💭 **Calculating the sum of a square takes too long. Is there a way to get the sum of any sub-rectangle in O(1)?**

---

## Solution 2: 2D Prefix Sum + Binary Search ❌

### The Natural Thought
If we use a **2D Prefix Sum** array, we can calculate the sum of any square in `O(1)` time.
`P[i][j]` stores the sum of the rectangle from `(0,0)` to `(i,j)`.
Sum of square defined by `(r1, c1)` and `(r2, c2)` is:
`Sum = P[r2][c2] - P[r1-1][c2] - P[r2][c1-1] + P[r1-1][c1-1]`

With `O(1)` sum query, we can try to find the maximum side length.
Since the side length is monotonic (if size `k` works, maybe `k+1` works; if `k` fails, `k+1` definitely fails... actually not quite true, but we are looking for MAX k),
Wait, if a square of size `k` exists with sum <= threshold, it DOES NOT imply a square of size `k+1` exists.
However, we want the *maximum* `k`. We can binary search on the answer `k`.
Range for `k`: `[0, min(m, n)]`.
To check if a side length `k` is valid: Iterate all top-left corners, check if ANY square of size `k` has sum <= threshold.

### Approach
1. Build 2D Prefix Sum array `P`.
2. Binary Search for side length `k` from `0` to `min(m, n)`.
3. `check(k)` function: loops through all possible top-left corners `(i, j)`. If any square of size `k` has sum <= threshold, return `true`.
4. If `check(k)` is true, try larger `k` (move `low` up). Else, try smaller `k` (move `high` down).

### Complexity Analysis

| Solution | Time | Space | Correct? | Why? |
|----------|------|-------|----------|------|
| Brute Force | O(m\*n\*min^3) | O(1) | ✅ TLE | Sum calculation too slow. |
| Binary Search | O(m\*n\*log(min)) | O(m\*n) | ✅ Accepted | Much faster, but strictly not O(m*n). |
| **Optimal** | **O(m\*n)** | **O(m\*n)** | ✅ **Optimal** | Removes the log factor. |

> 💭 **Binary search checks `k` independently. But notice: if we found a valid square of size `k`, for the next position, we only care if we can find a square of size `k+1`. We don't need to check smaller sizes again!**

---

## Solution 3: Optimal (Smart Iteration) ✅

### The Connection 🔗
- **Binary Search** was efficient (`O(m*n*log(min))`), but we effectively restart the search for each `k`.
- **Insight**: We iterate through the grid once. As we move through the grid (say, iterating through bottom-right corners), the maximum possible side length found *so far* can only increase or stay the same. It never needs to decrease.
- **What we need**: A way to iterate the grid and maintain a "current maximum side length" `len`. If we can form a square of size `len + 1` ending at the current cell, we increment `len`.

### The Key Insight 💡
Let `ans` be the maximum side length found so far.
Iterate through each cell `(i, j)` and treat it as the **bottom-right** corner of a square.
Check if a square of size `ans + 1` ending at `(i, j)` has sum `<= threshold`.
- If **YES**: We found a bigger square! Increment `ans`.
- If **NO**: We keep `ans` as is. We don't need to check for size `ans` or smaller, because we already know such a square exists (somewhere else), and we only care about finding a *larger* one.

### The Algorithm
1. Create 2D Prefix Sum array `P`.
2. Initialize `maxSide = 0`.
3. Iterate `i` from `1` to `m`, `j` from `1` to `n`:
    - Let `currentLen = maxSide + 1`.
    - Check if a square of size `currentLen` ending at `(i, j)` exists (valid boundary) AND has sum `<= threshold`.
    - If valid, `maxSide++`.
4. Return `maxSide`.

### Step-by-Step Walkthrough
Input:
`mat = [[1, 1], [1, 1]]`, `threshold = 4`

1. **Prefix Sum P**:
   ```
   0 0 0
   0 1 2
   0 2 4
   ```
   (Indices 1-based)

2. **Iteration**:
   - `maxSide = 0`.
   - **(i=1, j=1)**: `currentLen = 1`. Square (1,1) to (1,1). Sum = 1. `1 <= 4`. **Valid**. `maxSide` becomes `1`.
   - **(i=1, j=2)**: `currentLen = 2`. Square (1,2) with len 2? No, `i=1` < `len=2` (out of bounds). Skip.
   - **(i=2, j=1)**: `currentLen = 2`. Square (2,1) with len 2? No, `j=1` < `len=2`. Skip.
   - **(i=2, j=2)**: `currentLen = 2`. Square ending at (2,2) size 2. Top-left (1,1).
     Sum = `P[2][2] - P[0][2] - P[2][0] + P[0][0]` = `4 - 0 - 0 + 0 = 4`.
     `4 <= 4`. **Valid**. `maxSide` becomes `2`.

3. **Return**: `2`.

### Visual Diagram
```
Checking (i, j) as Bottom-Right corner.
Current maxSide = 1.
We hope to find a square of size 2.

(i-1, j-1)      (i-1, j)
      ┌───┬───┐
      │ ? │ ? │
      ├───┼───┤
      │ ? │ • │ <--- (i, j)
      └───┴───┘
   Square of size 2
   
Sum query using Prefix Sums is O(1).
If Sum <= Threshold:
   Length 2 is possible!
   maxSide increases to 2.
   Next we will look for size 3.
```

---

## Complexity Analysis (Optimal)

| Solution | Time | Space | Status | Why |
|----------|------|-------|--------|-----|
| Brute Force | O(m\*n\*min^3) | O(1) | ✅ TLE | Re-summing is expensive. |
| Binary Search | O(m\*n\*log(min)) | O(m\*n) | ✅ PASS | Good, but checks redundant states. |
| **Optimal** | **O(m\*n)** | **O(m\*n)** | ✅ **PASS** | Evaluates each cell once. |

---

## Key Takeaways
1. **2D Prefix Sums**: Essential for O(1) sum queries in matrices.
2. **Monotonicity**: If we found a square of size `k`, we only care about finding `k+1`. We never need to check `k` again.
3. **Smart Iteration**: Instead of checking *all* sizes at each cell, standardizing the "candidate" size based on the *global* maximum found so far optimizes the search.

---

## The Journey (TL;DR)

```
🐢 Brute Force → Iterate all squares, sum loops
         ↓
💡 "Sum queries are repeated. Let's use Prefix Sums."
         ↓
🚀 Binary Search → O(m*n*log k) with O(1) sums
         ↓
💡 "We only care if we can BEAT the current max size."
         ↓
✅ Optimal → O(m*n) Single pass, increment size if valid
```
