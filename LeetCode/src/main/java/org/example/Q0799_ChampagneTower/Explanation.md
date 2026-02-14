# Champagne Tower - Explanation

> **Prerequisites**: Basic Dynamic Programming, Pascal's Triangle structure (each element depends on two parents above)  
> **Related Problems**:  
> - [LeetCode 118 - Pascal's Triangle](https://leetcode.com/problems/pascals-triangle/) (Same pyramid structure — each cell is sum of two cells above. Champagne Tower is Pascal's Triangle with a cap at 1.0)  
> - [LeetCode 62 - Unique Paths](https://leetcode.com/problems/unique-paths/) (Grid DP where each cell depends on neighbors above/left — same top-to-bottom propagation idea)  
> - [LeetCode 1223 - Dice Roll Simulation](https://leetcode.com/problems/dice-roll-simulation/) (Simulation DP with cascading state transitions)

---

## Problem in Simple Words

Pour `poured` cups of champagne into the top glass of a pyramid. Each glass holds 1 cup max — any overflow splits equally to the two glasses directly below it. Return how full glass `j` in row `i` is.

For example, after pouring 4 cups: the top glass and both row-1 glasses are full (3 full total), and row 2 has `[0.25, 0.50, 0.25]` — the middle glass gets overflow from **both** parents.

---

## Solution 1: Brute Force (Recursive) ❌

### The Natural Thought

"To find how much champagne is in glass `(i, j)`, I need to know how much overflowed from its two parent glasses: `(i-1, j-1)` (left parent) and `(i-1, j)` (right parent). Each parent's overflow depends on ITS parents. I'll recurse upward!"

### Approach

```java
// How much champagne reaches glass (row, glass)?
double amount(int poured, int row, int glass) {
    // Base case: top glass receives all poured champagne
    if (row == 0 && glass == 0) return poured;
    
    // Out of bounds (no glass here)
    if (glass < 0 || glass > row) return 0;
    
    // Left parent (row-1, glass-1) overflow: max(0, amount - 1) / 2
    double fromLeft = Math.max(0, amount(poured, row - 1, glass - 1) - 1) / 2;
    
    // Right parent (row-1, glass) overflow: max(0, amount - 1) / 2
    double fromRight = Math.max(0, amount(poured, row - 1, glass) - 1) / 2;
    
    return fromLeft + fromRight;
}

// Final answer: min(1.0, amount) since glass caps at 1.0
return Math.min(1.0, amount(poured, query_row, query_glass));
```

**How it works**: Each glass `(r, c)` receives champagne from its two parents. A parent at `(r-1, c-1)` or `(r-1, c)` overflows `max(0, its_amount - 1.0) / 2.0` into this glass. We recurse all the way up to `(0, 0)` which holds `poured` cups.

### Why It's Bad

The recursion tree **branches into 2 at every level**. Glass `(r, c)` asks two parents, each parent asks two grandparents... it's a binary tree of depth `row`.

### Example Where It's SLOW ❌

```
For poured = 100000009, query_row = 33, query_glass = 17:

  Glass (33, 17) calls → (32, 16) and (32, 17)
  Glass (32, 16) calls → (31, 15) and (31, 16)
  Glass (32, 17) calls → (31, 16) and (31, 17)
                                  ↑
                    (31, 16) is called TWICE!

  Total calls ≈ 2^33 = 8,589,934,592 recursive calls!
  Java handles ~200M calls/sec → ~43 seconds → TLE + StackOverflow!

  Even for query_row = 20:
    2^20 = 1,048,576 calls → borderline
  For query_row = 30:
    2^30 = 1,073,741,824 → definitely TLE!
```

**The re-computation explosion**: Glass `(31, 16)` is needed by BOTH `(32, 16)` and `(32, 17)`. It gets computed twice. Its parents get computed 4 times each. At row 0, the top glass gets computed `C(33, 17)` times — a combinatorial explosion!

```
              (33, 17)
             /        \
        (32, 16)      (32, 17)
        /     \        /     \
   (31, 15) (31, 16) (31, 16) (31, 17)
                 ↑         ↑
          SAME glass computed TWICE!
          
   At depth 33, this overlapping grows exponentially.
```

### Complexity Analysis

| Solution | Time | Space | Correct? | Why? |
|----------|------|-------|----------|------|
| Recursive | O(2^row) | O(row) stack | ✅ TLE | Exponential branching, massive re-computation |
| Memoized | O(row²) | O(row²) | ✅ Works | Cache eliminates overlapping calls |
| **Simulation DP** | **O(row²)** | **O(row²)** | **✅ Optimal** | **Simple loop, no recursion overhead** |

> 💭 **"The recursion re-computes the same glass over and over — glass (31, 16) is needed by both (32, 16) and (32, 17) but gets computed from scratch each time. What if we cache every glass's result so we only compute it once?"**

---

## Solution 2: Top-Down DP (Memoization) ❌

### The Natural Thought

"The brute force recurses into the same glass multiple times. Classic overlapping subproblems! I'll add a HashMap to cache each `(row, glass)` result. Before recursing, check the cache. After computing, store in cache."

### Approach

```java
Map<String, Double> memo = new HashMap<>();

double amount(int poured, int row, int glass) {
    if (row == 0 && glass == 0) return poured;
    if (glass < 0 || glass > row) return 0;
    
    String key = row + "," + glass;
    if (memo.containsKey(key)) return memo.get(key);  // ← Cache hit!
    
    double fromLeft = Math.max(0, amount(poured, row - 1, glass - 1) - 1) / 2;
    double fromRight = Math.max(0, amount(poured, row - 1, glass) - 1) / 2;
    double result = fromLeft + fromRight;
    
    memo.put(key, result);  // ← Store for future calls
    return result;
}
```

### Why It Works But Isn't Ideal

This correctly eliminates the re-computation. Each unique `(row, glass)` pair is computed exactly once.

```
Total unique glasses from row 0 to row 33:
  = 1 + 2 + 3 + ... + 34 = 34 × 35 / 2 = 595 computations

That's 595 vs. 8.6 BILLION in brute force — a 14,000,000× reduction!
```

**But it has overhead**:
```
Problems with memoization approach:
  1. HashMap string-key creation: "33,17" → new String object per call
  2. HashMap lookup: hash computation + equals check per call  
  3. Recursion stack: depth = query_row = up to 99 frames deep
  4. Autoboxing: double → Double for HashMap values
  
  These constant factors make it ~5-10× slower than array-based DP.
  
  More importantly: we're recursing UPWARD from target glass to the top,
  but champagne flows DOWNWARD from top to target. We're fighting gravity!
```

### Complexity Analysis

| Solution | Time | Space | Correct? | Why? |
|----------|------|-------|----------|------|
| Recursive | O(2^row) | O(row) stack | ✅ TLE | Exponential re-computation |
| Memoized | O(row²) | O(row²) | ✅ Works | Eliminates overlap, but HashMap + recursion overhead |
| **Simulation DP** | **O(row²)** | **O(row²)** | **✅ Optimal** | **Simple nested loop, direct array access** |

> 💭 **"Memoization works, but we're fighting the natural direction — recursing UP from the target while champagne flows DOWN from the top. What if instead of asking 'what flows INTO this glass?', we simulate the actual pouring process? Start at the top, pour champagne, let gravity do its thing row by row?"**

---

## Solution 3: Bottom-Up Simulation DP ✅

### The Connection 🔗

Let's trace our thinking:
- **Recursive** was correct but **exponential** — O(2^row) due to re-computing the same glass over and over
- **Memoized** fixed the overlap problem — O(row²) — but went against the natural direction (recursing UP while champagne flows DOWN) and had HashMap/recursion overhead
- **What we need**: Process in the natural direction — top to bottom — and simulate how champagne actually pours → **Simulation DP!**

### The Key Insight 💡

> Don't ask **"how much flows INTO this glass?"** (top-down thinking). Instead, ask **"how much flows OUT of each glass?"** (bottom-up simulation). Pour everything at the top, then process each glass row by row: if it has more than 1.0, cap it and send the excess downward.

This is exactly like **Pascal's Triangle** — but with a **cap at 1.0** and **overflow propagation** instead of simple addition.

```
Pascal's Triangle:           Champagne Tower:
     1                          1.0 (capped!)
    1 1                        1.0  1.0 (capped!)
   1 2 1                      0.25 0.50 0.25
  1 3 3 1                     ...

Pascal: each cell = sum of two parents above
Champagne: each cell = sum of overflow from two parents above
           But capped at 1.0, and only excess propagates!
```

### The Algorithm

```
1. Create A[102][102], set A[0][0] = poured
2. For each row r = 0 to query_row:
     For each glass c = 0 to r:
       If A[r][c] > 1.0:
         excess = (A[r][c] - 1.0) / 2.0
         A[r][c] = 1.0                    ← cap the glass
         A[r+1][c]   += excess            ← overflow to left child
         A[r+1][c+1] += excess            ← overflow to right child
3. Return A[query_row][query_glass]
```

**Why this order works**: We process row 0 completely before row 1, row 1 before row 2, etc. By the time we reach glass `(r, c)`, both of its parents `(r-1, c-1)` and `(r-1, c)` have already been processed and their overflow has been added to `(r, c)`. So `A[r][c]` contains the TOTAL champagne this glass receives.

### Step-by-Step Walkthrough

**Input**: `poured = 4, query_row = 2, query_glass = 1`

```
INITIAL STATE:
  A[0][0] = 4.0   (all 4 cups start in the top glass)

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

ROW 0, Glass (0,0):
  A[0][0] = 4.0 > 1.0?  YES
  excess = (4.0 - 1.0) / 2.0 = 1.5
  A[0][0] = 1.0          ← capped
  A[1][0] += 1.5          → A[1][0] = 0 + 1.5 = 1.5
  A[1][1] += 1.5          → A[1][1] = 0 + 1.5 = 1.5

  State after row 0:
      Row 0: [1.0]            ← full
      Row 1: [1.5, 1.5]       ← each got 1.5 cups (will overflow!)
      Row 2: [0, 0, 0]

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

ROW 1, Glass (1,0):
  A[1][0] = 1.5 > 1.0?  YES
  excess = (1.5 - 1.0) / 2.0 = 0.25
  A[1][0] = 1.0          ← capped
  A[2][0] += 0.25         → A[2][0] = 0 + 0.25 = 0.25
  A[2][1] += 0.25         → A[2][1] = 0 + 0.25 = 0.25

  State after glass (1,0):
      Row 0: [1.0]
      Row 1: [1.0, 1.5]       ← (1,0) capped, (1,1) not yet processed
      Row 2: [0.25, 0.25, 0]

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

ROW 1, Glass (1,1):
  A[1][1] = 1.5 > 1.0?  YES
  excess = (1.5 - 1.0) / 2.0 = 0.25
  A[1][1] = 1.0          ← capped
  A[2][1] += 0.25         → A[2][1] = 0.25 + 0.25 = 0.50  ← gets from BOTH parents!
  A[2][2] += 0.25         → A[2][2] = 0 + 0.25 = 0.25

  State after row 1:
      Row 0: [1.0]
      Row 1: [1.0, 1.0]       ← both full
      Row 2: [0.25, 0.50, 0.25]  ← middle gets from both parents!

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

ROW 2: Process glasses (2,0), (2,1), (2,2):
  A[2][0] = 0.25 > 1.0?  NO  → skip
  A[2][1] = 0.50 > 1.0?  NO  → skip
  A[2][2] = 0.25 > 1.0?  NO  → skip

RESULT: A[2][1] = 0.50 ✅
```

### Visual Diagram

**Champagne flow for `poured = 4`**:

```
    Pour 4 cups
         ↓
                      ┌─────────┐
          Row 0:      │   4.0   │
                      └────┬────┘
                      Cap at 1.0
                      excess = 3.0
                      each child gets 3.0/2 = 1.5
                      ┌─────────┐
          Row 0:      │   1.0   │  ✅ FULL
                      └────┬────┘
                     1.5 ↙   ↘ 1.5
               ┌─────────┐  ┌─────────┐
   Row 1:      │   1.5   │  │   1.5   │
               └────┬────┘  └────┬────┘
               Cap at 1.0   Cap at 1.0
               excess=0.5   excess=0.5
               each gets    each gets
               0.5/2=0.25   0.5/2=0.25
               ┌─────────┐  ┌─────────┐
   Row 1:      │   1.0   │  │   1.0   │  ✅ BOTH FULL
               └────┬────┘  └────┬────┘
              0.25↙  ↘0.25↙  ↘0.25
          ┌─────────┐ ┌─────────┐ ┌─────────┐
  Row 2:  │  0.25   │ │  0.50   │ │  0.25   │
          └─────────┘ └─────────┘ └─────────┘
             (2,0)       (2,1)       (2,2)
                           ↑
                    MIDDLE glass (2,1) gets
                    0.25 from left parent (1,0)
                  + 0.25 from right parent (1,1)
                  = 0.50
```

**Why the middle glass gets 0.50**: Glass `(2,1)` has TWO parents — `(1,0)` and `(1,1)`. Each parent overflows 0.25 to this glass. So it receives `0.25 + 0.25 = 0.50`. The edge glasses `(2,0)` and `(2,2)` have only ONE parent each, so they only get `0.25`.

**Visual: What happens with poured = 1 (Example 1)**:

```
    Pour 1 cup
         ↓
         ┌─────────┐
Row 0:   │   1.0   │  ← exactly full, no overflow!
         └─────────┘
    ┌─────────┐ ┌─────────┐
Row 1:  │   0.0   │ │   0.0   │  ← empty, nothing reached here
    └─────────┘ └─────────┘
       (1,0)       (1,1)

A[1][1] = 0.0 ✅  (no overflow from top)
```

**Visual: What happens with poured = 2 (Example 2)**:

```
    Pour 2 cups
         ↓
         ┌─────────┐
Row 0:   │   2.0   │  ← excess = (2-1)/2 = 0.5 each
         └────┬────┘
         Cap at 1.0
        0.5 ↙   ↘ 0.5
    ┌─────────┐ ┌─────────┐
Row 1:  │   0.5   │ │   0.5   │  ← half full each
    └─────────┘ └─────────┘
       (1,0)       (1,1)

A[1][1] = 0.5 ✅
```

### Key Optimizations in Code

1. **Array size 102×102**: `query_row` can be up to 99, and we write to `A[r+1]` which could be index 100. Size 102 provides a safe buffer — no bounds checking needed.

2. **Process only up to `query_row`**: We don't need to process rows below the query. Once we've propagated overflow through `query_row`, we're done.

3. **In-place capping**: We cap `A[r][c] = 1.0` immediately, so the array always contains the amount _actually in the glass_ (≤ 1.0), not the total amount that flowed through it. This means we can directly return `A[query_row][query_glass]` without a `min(1.0, ...)` at the end.

---

## Complexity Analysis (Optimal)

| Solution | Time | Space | Status | Why |
|----------|------|-------|--------|-----|
| Recursive | O(2^row) | O(row) | ✅ TLE | 2³³ = 8.6 billion calls for row=33 |
| Memoized | O(row²) | O(row²) | ✅ Works | HashMap overhead, recursion stack |
| **Simulation DP** | **O(row²)** | **O(row²)** | **✅ Optimal** | **Simple nested loop, direct array access** |

**Quantified for query_row = 99 (worst case)**:
- **Simulation DP**: Total operations = 1+2+3+...+100 = **5,050 array ops** → ~0.05ms → instant ✅
- **Memoized**: Same 5,050 unique states but ~5-10× slower due to HashMap/recursion overhead → still fast, just unnecessary
- **Recursive (no cache)**: 2⁹⁹ ≈ 6.3 × 10²⁹ calls → longer than the age of the universe
- **Space**: 102 × 102 doubles = 10,404 × 8 bytes = **~83 KB** → negligible

---

## Key Takeaways

1. **Simulate the natural direction**: When a process flows one way (top→bottom for gravity, left→right for time), simulate it in that direction. Champagne flows down — so process rows top-to-bottom. Don't recurse backwards against the flow.

2. **Cap & Propagate pattern**: "Each node takes what it can (cap at capacity), passes the rest to children." This pattern appears in: water flow problems, Pascal's Triangle variants, network bandwidth splitting, and probability distribution problems.

3. **Don't over-complicate simple problems**: The simulation fits in 10 lines of code. No HashMap, no recursion, no complex state encoding. When rows ≤ 100 and the total work is just 5,050 operations, a simple nested loop with a 2D array is the cleanest and fastest approach.

---

## The Journey (TL;DR)

```
🐢 Recursive → O(2^row) = 8.6 BILLION calls for row=33!
         ↓
💡 "Same glass computed many times. What if we cache?"
         ↓
❌ Memoized → O(row²) correct, but recursion + HashMap overhead
         ↓
💡 "Champagne flows DOWN. Why recurse UP? Simulate the pour!"
         ↓
✅ Simulation DP → O(row²) simple nested loop, 5050 ops, ACCEPTED!
```
