# Minimum Domino Rotations For Equal Row - Explanation

> **Prerequisites**: Basic array iteration. Understanding the "Pigeonhole Principle" or basic logic helps to deduce that the first element *must* be the key.

> **Honorable Mention**: *Try all 1-6*. Since domino values are only 1-6, we could run the `check` function for every number from 1 to 6. This is still O(N) (specifically 6N) but does unnecessary work.

## Problem in Simple Words

You have a row of dominoes (each has a top and bottom number).
You want to make **either** the entire top row OR the entire bottom row consist of the **same number**.
You can "rotate" (swap top/bottom) any domino.
Find the **minimum** rotations needed. Return -1 if impossible.

**Example**:
```
Tops:    [2, 1, 2, 4, 2, 2]
Bottoms: [5, 2, 6, 2, 3, 2]
Target:   2
```
Rotate index 1 (1/2) and index 3 (4/2) -> Top becomes all 2s. Cost = 2.

---

## Solution 1: Brute Force (Check 1-6) ❌ (Valid but Suboptimal)

### Approach
Try every possible target number from 1 to 6.
For each numbers, check if it's possible to make a full row.

### Why It's Not Ideal
- We check numbers that might not even be present.
- Although O(N), constant factor is 6.
- We can be smarter!

> 💭 **Wait, if a valid solution exists, that number MUST be present in EVERY column. This means it MUST be present in the very first column (index 0) too!**

---

## Solution 2: Greedy (Check Index 0) ✅ (Optimal)

### The Connection 🔗
Let's trace our thinking:
- **Brute force**: Check 1...6.
- **Key Logic**: If we can make ALL tops equal to X, then `tops[0]` must become X (either it was X, or `bottoms[0]` was X and we swapped).
- **Insight**: The target number **MUST** be either `tops[0]` or `bottoms[0]`. No other number is a candidate!

### The Key Insight 💡
We only need to check two possibilities:
1. Target is `tops[0]`
2. Target is `bottoms[0]`

If neither works, it's impossible.

### The Algorithm

1. **candidates** = `{tops[0], bottoms[0]}`
2. For each candidate `X`:
   - Checks if it's possible to make top row ALL `X` (count swaps).
   - Checks if it's possible to make bottom row ALL `X` (count swaps).
   - If impossible for a specific column (neither top nor bottom has `X`), stop.
3. Return the minimum swaps found.

### Step-by-Step Walkthrough

**Input**:
`tops = [2,1,2,4,2,2]`
`bottoms = [5,2,6,2,3,2]`

**Check Candidate 2 (`tops[0]`):**

| Index | Top | Bot | Needed? | Action |
|-------|-----|-----|---------|--------|
| 0 | **2** | 5 | Yes | Already Top (0 swaps) |
| 1 | 1 | **2** | No | Swap! (1 swap) |
| 2 | **2** | 6 | Yes | Already Top |
| 3 | 4 | **2** | No | Swap! (1 swap) |
| 4 | **2** | 3 | Yes | Already Top |
| 5 | **2** | 2 | Yes | Already Top |

Total Swaps to make Tops=2: **2**
Total Swaps to make Bottoms=2: We check similarly...

Since 2 worked, we return the minimum cost.

---

## Visual Diagram: The Rotation Process

**Scenario: Make top row all 2s** (Target = 2)

```
Index:      0      1      2      3      4      5
         ┌───┐  ┌───┐  ┌───┐  ┌───┐  ┌───┐  ┌───┐
Tops:    │ 2 │  │ 1 │  │ 2 │  │ 4 │  │ 2 │  │ 2 │
         └───┘  └───┘  └───┘  └───┘  └───┘  └───┘
         ┌───┐  ┌───┐  ┌───┐  ┌───┐  ┌───┐  ┌───┐
Bottoms: │ 5 │  │ 2 │  │ 6 │  │ 2 │  │ 3 │  │ 2 │
         └───┘  └───┘  └───┘  └───┘  └───┘  └───┘
           ✅      🔄      ✅      🔄      ✅      ✅
         OK     SWAP           SWAP

Step-by-Step Check:
i=0: Top is 2. OK.
i=1: Top is 1. Bottom is 2? Yes. ROTATE! (Cost = 1)
i=2: Top is 2. OK.
i=3: Top is 4. Bottom is 2? Yes. ROTATE! (Cost = 2)
i=4: Top is 2. OK.
i=5: Top is 2. OK.

Final Result: 2 Rotations needed.
```

## Logic Visual: Why only check Index 0?

If a solution exists for target `X`, then **every single domino** must have `X` somewhere (top or bottom).

```
Domino 0:  [ A | B ]   <-- X MUST be A or B!
Domino 1:  [ ? | ? ]
Domino 2:  [ ? | ? ]
...

If X is not in Domino 0, you can never make a full row of Xs, 
because Domino 0 will always be a "hole".

Therefore, we ONLY need to test candidate A and candidate B.
```

---

## Complexity Analysis

| Solution | Time | Space | Correct? | Why? |
|----------|------|-------|----------|------|
| Brute Force (1-6) | O(N) | O(1) | ✅ Valid | Checks 6 candidates |
| **Greedy (Index 0)** | O(N) | O(1) | ✅ **Optimal** | Checks only 2 candidates |

- **Time**: O(N) (We iterate the array up to 2 times).
- **Space**: O(1) (Only counters).

---

## Key Takeaways

1. **Intersection Logic**: For a value to be present everywhere, it must be present effectively "everywhere", including index 0.
2. **Greedy Reduction**: Reduce large search space (1-6 or infinite) to small candidates (tops[0], bottoms[0]).

---

## The Journey (TL;DR)

```
🐢 Brute Force: Try making row of 1s, then 2s... 6s → O(6N)
         ↓
💡 "If there IS a solution X, the first domino MUST contain X!"
         ↓
✅ Greedy: Only check tops[0] and bottoms[0] → OPTIMAL O(2N)
```
