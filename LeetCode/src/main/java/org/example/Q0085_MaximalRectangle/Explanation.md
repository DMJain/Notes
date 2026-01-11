# Maximal Rectangle - Explanation

## Problem in Simple Words
Find the **largest rectangle** made of only `1`s in a binary matrix.

**Example**:
```
[
  ["1","0","1","0","0"],
  ["1","0","1","1","1"],
  ["1","1","1","1","1"],  ← Rectangle of area 6
  ["1","0","0","1","0"]
]
```

---

## Solution 1: Brute Force ❌ (So Slow)

### Approach
For every pair of corners (top-left, bottom-right), check if the rectangle is all 1s.

```java
for (r1...r2)
  for (c1...c2)
     checkIfAllOnes(matrix)
```

### Why It's Bad
- **O((NM)³)** complexity.
- With N=200, this is hopelessly slow.

---

## Solution 2: Histogram Approach ✅ (Optimal)

### The Key Insight 💡
We can convert this 2D problem into a Series of 1D problems!

Think of each row as the "ground". How high are the continuous `1`s rising from this ground? This forms a **Histogram**.

**Row 0**: `[1, 0, 1, 0, 0]`
- Histogram: `[1, 0, 1, 0, 0]`
- Max Rect: 1

**Row 1**: `[1, 0, 1, 1, 1]`
- Previous heights: `[1, 0, 1, 0, 0]`
- Add current row: `[1+1, 0, 1+1, 0+1, 0+1]`
  (If current is 0, height resets to 0)
- Histogram: `[2, 0, 2, 1, 1]`
- Max Rect: 3

**Row 2**: `[1, 1, 1, 1, 1]`
- Previous heights: `[2, 0, 2, 1, 1]`
- Add current: `[3, 1, 3, 2, 2]`
- Histogram: `[3, 1, 3, 2, 2]`
- **Max Rect**: The block `[3, 2, 2]` gives width 3 * height 2 = **6** ✅

### The Sub-Problem: Largest Rectangle in Histogram

For a given row's histogram (e.g., `[3, 1, 3, 2, 2]`), how do we find the largest rectangle efficiently?
Answer: **Monotonic Stack**.

1. Maintain a stack of indices with **increasing heights**.
2. If we see a height **smaller** than the top of the stack, it means the rectangle ending at the top CANNOT extend further right.
3. **Pop** and calculate area for that height:
   - `Height = heights[popped]`
   - `Width = current_index - new_top_stack - 1`
   - `Area = Height * Width`

---

## Step-by-Step Walkthrough (Row 2 Histogram)

**Heights**: `[3, 1, 3, 2, 2]` + (0 sentinel)

**1. Process 3 (index 0)**
- Stack empty, Push 0
- Stack: `[0]` (val: 3)

**2. Process 1 (index 1)**
- `1 < 3` (Stack top). Should Pop!
- Pop 0. Height = 3.
- Stack empty. Width = 1.
- Area = 3 * 1 = 3.
- Push 1.
- Stack: `[1]` (val: 1)

**3. Process 3 (index 2)**
- `3 > 1`. Push 2.
- Stack: `[1, 2]` (vals: 1, 3)

**4. Process 2 (index 3)**
- `2 < 3`. Pop 2. Height = 3.
- Stack top is 1. Width = 3 - 1 - 1 = 1.
- Area = 3 * 1 = 3.
- Push 3.
- Stack: `[1, 3]` (vals: 1, 2)

**5. Process 2 (index 4)**
- `2 == 2`. Push 4.
- Stack: `[1, 3, 4]` (vals: 1, 2, 2)

**6. Process 0 (Sentinel)**
- `0 < 2`. Pop 4. Height = 2.
- Stack top 3. Width = 5 - 3 - 1 = 1. Area = 2.
- `0 < 2`. Pop 3. Height = 2.
- Stack top 1. Width = 5 - 1 - 1 = 3. **Area = 2 * 3 = 6** ✅
- `0 < 1`. Pop 1. Height = 1.
- Stack empty. Width = 5. Area = 5.

**Max Area found = 6**

---

## Visual: How Histograms Build Up

```
Matrix:
1 0 1 0 0
1 0 1 1 1
1 1 1 1 1

Row 0 Hist:
█   █

Row 1 Hist:
█   █ █ █
█   █

Row 2 Hist:
█   █ █ █  
█   █ █ █
█ █ █ █ █  ← Note the 2x3 rectangle of 1s here!
```

---

## Complexity Analysis

| Solution | Time | Space | Correct? |
|----------|------|-------|----------|
| Brute Force | O((NM)³) | O(1) | ✅ TLE |
| **Histogram (Stack)** | O(N × M) | O(M) | ✅ Optimal |

- **Time**: We iterate every cell once to update heights. The Stack processes each element at most twice (push/pop).
- **Space**: Height array O(M). Stack O(M).

---

## Key Takeaways

1. **2D → 1D Reduction**: Many 2D grid problems can be solved by iterating rows and solving a 1D problem.
2. **Histograms**: Accumulating lengths of consecutive 1s vertically creates histograms.
3. **Monotonic Stack**: The standard tool for "next smaller element" or "largest rectangle" problems.
