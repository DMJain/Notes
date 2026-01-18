# Largest Magic Square - Explanation

> **Prerequisites**: 
> - Understanding of 2D prefix sums
> - Matrix traversal and subgrid enumeration
> - [LeetCode 304 - Range Sum Query 2D - Immutable](https://leetcode.com/problems/range-sum-query-2d-immutable/) (foundation for 2D prefix sums)

> **Related Problems**:
> - [LeetCode 840 - Magic Squares In Grid](https://leetcode.com/problems/magic-squares-in-grid/) (fixed 3×3 magic squares)
> - [LeetCode 2428 - Maximum Hourglass Sum](https://leetcode.com/problems/maximum-sum-of-an-hourglass/) (2D subgrid pattern sums)

## Problem in Simple Words

Find the **largest square subgrid** where all row sums, column sums, and both diagonal sums are equal. Every 1×1 is trivially a magic square, so answer ≥ 1.

```
Example: grid = [[7,1,4,5,6],[2,5,1,6,4],[1,5,4,3,2],[1,2,7,3,4]]

       The 3×3 magic square:
       ┌───┬───┬───┐
       │ 5 │ 1 │ 6 │  ← Row sum = 12
       ├───┼───┼───┤
       │ 5 │ 4 │ 3 │  ← Row sum = 12
       ├───┼───┼───┤
       │ 2 │ 7 │ 3 │  ← Row sum = 12
       └───┴───┴───┘
         ↓   ↓   ↓
        12  12  12    ← Column sums
       
       Diagonals: 5+4+3 = 12, 6+4+2 = 12 ✅
       
       Answer: 3
```

---

## Solution 1: Brute Force ❌

### The Natural Thought
"For each possible k×k square, compute all row sums, column sums, and diagonal sums. If all equal, it's magic!"

### Approach

```java
public int largestMagicSquare(int[][] grid) {
    int n = grid.length, m = grid[0].length;
    
    for (int k = Math.min(n, m); k > 1; k--) {           // Try k from largest
        for (int r = 0; r <= n - k; r++) {               // All top-left corners
            for (int c = 0; c <= m - k; c++) {
                if (isMagic(grid, r, c, k)) return k;
            }
        }
    }
    return 1;
}

private boolean isMagic(int[][] grid, int r, int c, int k) {
    // Compute first row sum as target
    long target = 0;
    for (int j = c; j < c + k; j++) target += grid[r][j];
    
    // Check all rows - O(k²)
    for (int i = 0; i < k; i++) {
        long rowSum = 0;
        for (int j = 0; j < k; j++) 
            rowSum += grid[r + i][c + j];      // <-- Recomputing entire row!
        if (rowSum != target) return false;
    }
    
    // Check all columns - O(k²)
    for (int j = 0; j < k; j++) {
        long colSum = 0;
        for (int i = 0; i < k; i++) 
            colSum += grid[r + i][c + j];      // <-- Recomputing entire column!
        if (colSum != target) return false;
    }
    
    // Check diagonals - O(k)
    long d1 = 0, d2 = 0;
    for (int i = 0; i < k; i++) {
        d1 += grid[r + i][c + i];
        d2 += grid[r + i][c + k - 1 - i];
    }
    return d1 == target && d2 == target;
}
```

### Why It's Bad 🐢

Each `isMagic()` check is **O(k²)** because we recompute every row and column sum from scratch!

```
For a 50×50 grid checking k=50:
- Number of squares: 1 (only one 50×50 square)
- Per square: k rows × k cells + k cols × k cells ≈ 2k² = 5,000 ops
- But for k=25: (50-25+1)² = 676 squares × 1250 ops = 845,000 ops

Total across all k values: ~1.5 million ops (acceptable but inefficient)
```

The real issue: **repeated redundant computation**. For overlapping squares, we recalculate sums we've already computed.

### Example Where It's SLOW ❌

```
Grid: 50×50 (max constraints)

For each position (r,c), for each k from 50 down to 2:
  - Checking k=50: 1 × 5000 = 5,000 ops
  - Checking k=49: 4 × 4802 = 19,208 ops
  - Checking k=25: 676 × 1250 = 845,000 ops
  - ...
  
Worst case (no magic square found until k=1):
  ≈ O(m × n × min(m,n)³) = 50 × 50 × 50³ = 312 million ops!
```

### Complexity Analysis

| Solution | Time | Space | Status | Why |
|----------|------|-------|--------|-----|
| **Brute Force** | O(m·n·k³) | O(1) | ✅ TLE-risk | Recalculates sums from scratch |
| Prefix Sum | O(m·n·k²) | O(m·n) | ✅ **Optimal** | O(1) row/col sum lookups |

> 💭 **The key bottleneck is recalculating row and column sums. We're doing O(k) work for each sum. What if we could compute any row/column sum in O(1)?**

---

## Solution 2: Prefix Sum Optimization ✅

### The Connection 🔗

Let's trace our thinking:
- **Brute Force** was slow because: computing each row/column sum took O(k)
- **What we need**: a way to get any "contiguous sum" in O(1) → **Prefix Sums!**

### The Key Insight 💡

**1D Prefix Sum Review:**
```
arr      = [3, 1, 4, 1, 5]
prefix   = [0, 3, 4, 8, 9, 14]
           ↑
        dummy 0 for easier indexing

Sum of arr[1..3] = prefix[4] - prefix[1] = 9 - 3 = 6  (which is 1+4+1)
```

**For 2D Grid:** We need:
- **Row prefix sums**: `rowPrefix[i][j+1] = sum of row i from column 0 to j`
- **Column prefix sums**: `colPrefix[i+1][j] = sum of column j from row 0 to i`

```
Grid:                Row Prefix:              Column Prefix:
┌───┬───┬───┐       ┌───┬───┬───┬───┐        ┌───┬───┬───┐
│ 7 │ 1 │ 4 │       │ 0 │ 7 │ 8 │12 │        │ 0 │ 0 │ 0 │
├───┼───┼───┤       ├───┼───┼───┼───┤        ├───┼───┼───┤
│ 2 │ 5 │ 1 │   →   │ 0 │ 2 │ 7 │ 8 │        │ 7 │ 1 │ 4 │
├───┼───┼───┤       ├───┼───┼───┼───┤        ├───┼───┼───┤
│ 1 │ 5 │ 4 │       │ 0 │ 1 │ 6 │10 │        │ 9 │ 6 │ 5 │
└───┴───┴───┘       └───┴───┴───┴───┘        ├───┼───┼───┤
                                              │10 │11 │ 9 │
                                              └───┴───┴───┘
```

**O(1) Row Sum:** `rowSum[row][c..c+k-1] = rowPrefix[row][c+k] - rowPrefix[row][c]`

**O(1) Col Sum:** `colSum[r..r+k-1][col] = colPrefix[r+k][col] - colPrefix[r][col]`

### The Algorithm

```
1. Build rowPrefix[n][m+1] and colPrefix[n+1][m]
2. For k from min(n, m) down to 2:
     For each top-left corner (r, c):
       a. target = rowPrefix[r][c+k] - rowPrefix[r][c]  // First row sum
       b. Check all k row sums in O(k) total (each is O(1))
       c. Check all k column sums in O(k) total (each is O(1))
       d. Check both diagonals in O(k) (no prefix trick, still O(k))
       e. If all match target → return k
3. Return 1 (1×1 always magic)
```

### Step-by-Step Walkthrough

Using `grid = [[7,1,4,5,6],[2,5,1,6,4],[1,5,4,3,2],[1,2,7,3,4]]`:

**Step 1: Build Prefix Sums**

```
rowPrefix[4][6]:
Row 0: [0, 7, 8, 12, 17, 23]     (7, 7+1, 7+1+4, ...)
Row 1: [0, 2, 7, 8, 14, 18]
Row 2: [0, 1, 6, 10, 13, 15]
Row 3: [0, 1, 3, 10, 13, 17]

colPrefix[5][5]:
        Col0 Col1 Col2 Col3 Col4
Row 0:   0    0    0    0    0
Row 1:   7    1    4    5    6
Row 2:   9    6    5   11   10
Row 3:  10   11    9   14   12
Row 4:  11   13   16   17   16
```

**Step 2: Try k=4 (largest possible)**

Check square at (0,0):
```
┌───┬───┬───┬───┐
│ 7 │ 1 │ 4 │ 5 │  Row sum = rowPrefix[0][4] - rowPrefix[0][0] = 17-0 = 17
├───┼───┼───┼───┤
│ 2 │ 5 │ 1 │ 6 │  Row sum = 14
└───┴───┴───┴───┘
Row 0 ≠ Row 1 → NOT magic ❌
```

**Step 3: Try k=3**

Check square at (0,1):
```
Top-left at (0,1), k=3:
┌───┬───┬───┐
│ 1 │ 4 │ 5 │  = rowPrefix[0][4] - rowPrefix[0][1] = 17-7 = 10
├───┼───┼───┤
│ 5 │ 1 │ 6 │  = rowPrefix[1][4] - rowPrefix[1][1] = 14-2 = 12
└───┴───┴───┘
Row 0 ≠ Row 1 → NOT magic ❌
```

Check square at (1,1):
```
Top-left at (1,1), k=3:
┌───┬───┬───┐
│ 5 │ 1 │ 6 │  = rowPrefix[1][4] - rowPrefix[1][1] = 14-2 = 12  ← target
├───┼───┼───┤
│ 5 │ 4 │ 3 │  = rowPrefix[2][4] - rowPrefix[2][1] = 13-1 = 12  ✓
├───┼───┼───┤
│ 2 │ 7 │ 3 │  = rowPrefix[3][4] - rowPrefix[3][1] = 13-1 = 12  ✓
└───┴───┴───┘

Column 1: colPrefix[4][1] - colPrefix[1][1] = 13-1 = 12 ✓
Column 2: colPrefix[4][2] - colPrefix[1][2] = 16-4 = 12 ✓
Column 3: colPrefix[4][3] - colPrefix[1][3] = 17-5 = 12 ✓

Diagonal 1: 5+4+3 = 12 ✓
Diagonal 2: 6+4+2 = 12 ✓

ALL MATCH! → Return k=3 ✅
```

### Visual Diagram

```
Original Grid (4×5):
    c=0  c=1  c=2  c=3  c=4
   ┌────┬────┬────┬────┬────┐
r=0│  7 │  1 │  4 │  5 │  6 │
   ├────┼────┼────┼────┼────┤
r=1│  2 │ ╔══╪════╪════╗  4 │    ← The 3×3 magic square
   ├────┼─╫──┼────┼────╫────┤       starting at (1,1)
r=2│  1 │ ║5 │  4 │  3 ║  2 │
   ├────┼─╫──┼────┼────╫────┤
r=3│  1 │ ╚══╪════╪════╝  4 │
   └────┴────┴────┴────┴────┘

Verification:
                    ┌─────────────────────────────────┐
                    │         MAGIC SQUARE            │
                    │   ╔═══╦═══╦═══╗                 │
   Row Sums:        │   ║ 5 ║ 1 ║ 6 ║ → 12           │
                    │   ╠═══╬═══╬═══╣                 │
                    │   ║ 5 ║ 4 ║ 3 ║ → 12           │
                    │   ╠═══╬═══╬═══╣                 │
                    │   ║ 2 ║ 7 ║ 3 ║ → 12           │
                    │   ╚═══╩═══╩═══╝                 │
   Col Sums:            ↓   ↓   ↓                    │
                       12  12  12                     │
                    └─────────────────────────────────┘
   Diag 1: 5→4→3 = 12  ↘
   Diag 2: 6→4→2 = 12  ↙
```

### Code (Optimal)

```java
public int largestMagicSquare(int[][] grid) {
    int n = grid.length, m = grid[0].length;
    long[][] rowPrefix = new long[n][m + 1];
    long[][] colPrefix = new long[n + 1][m];
    
    // Build row prefix sums
    for (int i = 0; i < n; i++) {
        for (int j = 0; j < m; j++) {
            rowPrefix[i][j + 1] = rowPrefix[i][j] + grid[i][j];
        }
    }
    
    // Build column prefix sums
    for (int j = 0; j < m; j++) {
        for (int i = 0; i < n; i++) {
            colPrefix[i + 1][j] = colPrefix[i][j] + grid[i][j];
        }
    }
    
    // Try from largest k down
    for (int k = Math.min(n, m); k > 1; k--) {
        for (int r = 0; r <= n - k; r++) {
            for (int c = 0; c <= m - k; c++) {
                
                long target = rowPrefix[r][c + k] - rowPrefix[r][c];  // O(1)
                boolean isMagic = true;
                
                // Check all rows - O(k) total, each O(1)
                for (int i = 0; i < k; i++) {
                    long currRowSum = rowPrefix[r + i][c + k] - rowPrefix[r + i][c];
                    if (currRowSum != target) {
                        isMagic = false;
                        break;
                    }
                }
                if (!isMagic) continue;
                
                // Check all columns - O(k) total, each O(1)
                for (int j = 0; j < k; j++) {
                    long currColSum = colPrefix[r + k][c + j] - colPrefix[r][c + j];
                    if (currColSum != target) {
                        isMagic = false;
                        break;
                    }
                }
                if (!isMagic) continue;
                
                // Check diagonals - O(k)
                long d1 = 0, d2 = 0;
                for (int i = 0; i < k; i++) {
                    d1 += grid[r + i][c + i];
                    d2 += grid[r + i][c + k - 1 - i];
                }
                
                if (d1 == target && d2 == target) {
                    return k;
                }
            }
        }
    }
    
    return 1;  // 1×1 always magic
}
```

---

## Complexity Analysis (Final)

| Solution | Time | Space | Status | Why |
|----------|------|-------|--------|-----|
| Brute Force | O(m·n·k³) | O(1) | ✅ TLE-risk | Each row/col sum is O(k) |
| **Prefix Sum** | **O(m·n·k²)** | **O(m·n)** | **✅ Optimal** | Row/col sums are O(1) |

**For 50×50 grid:**
- Brute Force: ~312 million ops worst case
- Prefix Sum: ~6.25 million ops worst case (50× faster!)

---

## Why NOT Other Approaches? 🤔

### "Can we use 2D prefix sum for areas?"

The standard 2D prefix sum gives you **rectangular submatrix sums**, not individual row/column sums. We need **each row** and **each column** separately, so separate 1D prefix arrays for rows and columns are more suitable.

### "Can we precompute diagonal prefix sums too?"

Yes! You could build diagonal prefix sums to make diagonal checks O(1) instead of O(k). But since:
- k ≤ 50 (small)
- Diagonals are only 2 checks vs 2k for rows/cols
- Diagonal prefixes add complexity

The gain is marginal. The current O(k) for diagonals is acceptable.

---

## Key Takeaways

1. **Prefix Sums for Range Queries**: When you need repeated contiguous sums, precompute prefix sums for O(1) lookups.

2. **Work Backwards from Largest**: Since we want the **largest** magic square, try k from `min(n,m)` down. Return immediately when found.

3. **Early Exit with `continue`**: If rows don't match, skip column/diagonal checks. Fast-fail pattern.

4. **Use `long` for Sums**: With `grid[i][j] ≤ 10^6` and `k ≤ 50`, sum can reach `50 × 10^6 = 5×10^7`. Use `long` to be safe.

---

## The Journey (TL;DR)

```
🐢 Brute Force → SLOW (recalculates sums O(k) each)
         ↓
💡 "What if we precompute row/column sums for O(1) lookup?"
         ↓
✅ Prefix Sum → WORKS! O(m·n·k²)
```
