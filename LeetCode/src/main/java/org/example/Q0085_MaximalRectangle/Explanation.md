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

> 💭 **Checking every possible rectangle is O(n⁴) for boundaries + O(n²) to verify. Way too slow. But what if we could reuse work from previous rows? Like... building up heights?**

---

## Solution 2: Histogram Approach ✅ (Optimal)

### The Connection 🔗
Let's trace our thinking:
- **Brute Force** was slow because: checking all rectangles independently = O((NM)³)
- **Key observation**: Each row can be treated as the "ground" of a histogram
- **What we need**: Solve "largest rectangle in histogram" for each row!

### The Key Insight 💡
We can convert this 2D problem into a **series of 1D problems**!

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

### Why This Works
```
Brute Force:                    Histogram:
     ↓                              ↓
"Check all rectangles"         "Build heights row by row"
O((NM)³) operations            Solve 1D problem per row
                               O(N × M) total
```

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

## Visual: Monotonic Stack Animation

```
Heights: [3, 1, 3, 2, 2, 0]  (0 is sentinel)

Index:    0  1  2  3  4  5

Step 1: Push 0           Stack: [0]
                                 ↑(h=3)

Step 2: 1 < 3, Pop 0     Stack: [1]
        area = 3*1 = 3           ↑(h=1)

Step 3: Push 2           Stack: [1, 2]
                                 ↑  ↑
                               (1)(3)

Step 4: 2 < 3, Pop 2     Stack: [1, 3]
        area = 3*1 = 3           ↑  ↑
                               (1)(2)
        
Step 5: Push 4           Stack: [1, 3, 4]
                               (1)(2)(2)

Step 6: Sentinel 0, pop all
        Pop 4: area = 2*1 = 2
        Pop 3: area = 2*3 = 6 ← MAX!
        Pop 1: area = 1*5 = 5
```

---

## Complexity Analysis

| Solution | Time | Space | Correct? | Why? |
|----------|------|-------|----------|------|
| Brute Force | O((NM)³) | O(1) | ✅ TLE | Check all rectangles |
| **Histogram (Stack)** | O(N × M) | O(M) | ✅ **Optimal** | Each cell processed twice max |

- **Time**: We iterate every cell once to update heights. The Stack processes each element at most twice (push/pop).
- **Space**: Height array O(M). Stack O(M).

---

## Key Takeaways

1. **2D → 1D Reduction**: Many 2D grid problems can be solved by iterating rows and solving a 1D problem
2. **Histograms**: Accumulating lengths of consecutive 1s vertically creates histograms
3. **Monotonic Stack**: The standard tool for "next smaller element" or "largest rectangle" problems

---

## The Journey (TL;DR)

```
🐢 Brute Force: Check all rectangles → TOO SLOW (O((NM)³))
         ↓
💡 "What if we reuse work from previous rows?"
         ↓
📊 Histogram: Each row = ground, heights build up
         ↓
💡 "Now it's 'largest rectangle in histogram' — use monotonic stack!"
         ↓
✅ Histogram + Stack: O(N × M) → OPTIMAL
```
