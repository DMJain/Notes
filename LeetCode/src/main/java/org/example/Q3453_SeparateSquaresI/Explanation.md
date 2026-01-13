# Separate Squares I - Explanation

> **Prerequisites**: Understanding **Binary Search on Answer**. If you know how to solve "Koko Eating Bananas" or similar range-reduction problems, this applies the same logic to geometry.

> **Honorable Mention**: *Sweep Line Algorithm*. We could collect all Y-coordinates, sort them, and process intervals. This gives an exact answer in O(N log N) but is more complex to implement than binary search.

## Problem in Simple Words

You have a bunch of squares on a 2D plane. (They can overlap!).
Find a **horizontal line** `y = ?` that cuts the total area **exactly in half**.
i.e., Area of squares **below** the line = Area of squares **above** the line = `Total Area / 2`.

**Note**: Overlapping regions count multiple times (like layers of paper).

---

## Solution 1: Brute Force (Iterate Y) ❌ (Impossible)

### Approach
Try every possible `y` value (0.0, 0.1, 0.2...) and check areas.

### Why It's Bad
- The Y-coordinates are continuous (real numbers).
- The range is up to $2 \times 10^9$.
- You cannot iterate infinite real numbers!

> 💭 **We can't check every number. But notice something: as we move the line UP, the area BELOW it always increases (or stays same). It never decreases. This monotonicity screams Binary Search!**

---

## Solution 2: Binary Search on Answer ✅ (Optimal)

### The Connection 🔗
Let's trace our thinking:
- **Goal**: Find `y` where `AreaBelow(y) == TotalArea / 2`.
- **Property**: `AreaBelow(y)` is a **monotonically increasing function**.
  - If `y` is too low, area is too small → move UP.
  - If `y` is too high, area is too big → move DOWN.
- **Result**: Binary Search allows us to pinpoint `y` with high precision efficiently.

### The Algorithm

1. **Range**: `low = min(y_vals)`, `high = max(y + side_vals)`.
2. **Target**: `TotalArea / 2`.
3. **Loop** 100 times (or until precision met):
   - `mid = (low + high) / 2`
   - Calculate `currentArea = AreaBelow(mid)`
   - If `currentArea >= Target`: `high = mid` (Line is too high or just right, try lower)
   - If `currentArea < Target`: `low = mid` (Line is too low, raise it)
4. Return `high` (or `low`, they are essentially same).

### Calculating `AreaBelow(currentY)`
For each square `(y, length)` with `top = y + length`:
- **Completely Below** (`top <= currentY`): Add full area `l * l`.
- **Completely Above** (`y >= currentY`): Add `0`.
- **Cut Through** (`y < currentY < top`): Add `l * (currentY - y)`.

### Step-by-Step Walkthrough

**Input**: `squares = [[0,0,2], [1,1,1]]`
Total Area:
- Sq1: 2x2 = 4 (y ranges 0 to 2)
- Sq2: 1x1 = 1 (y ranges 1 to 2)
- Total = 5. Target = 2.5.

**Bounds**: low=0, high=2.

**Iteration 1**:
- `mid = 1.0`
- Area Below 1.0:
  - Sq1 (0 to 2): Cut. Height below 1.0 = `1.0 - 0 = 1`. Area = `2 * 1 = 2`.
  - Sq2 (1 to 2): Above. Area = 0.
  - Total = 2.
- 2 < 2.5 (Too small) → `low = 1.0`

**Iteration 2**:
- `mid = 1.5`
- Area Below 1.5:
  - Sq1: Cut. Height `1.5 - 0 = 1.5`. Area `2 * 1.5 = 3`.
  - Sq2: Cut. Height `1.5 - 1 = 0.5`. Area `1 * 0.5 = 0.5`.
  - Total = 3.5.
- 3.5 >= 2.5 (Too big) → `high = 1.5`

**Iteration 3**:
- `mid = 1.25`
... converges to ~1.1666667 (7/6).

---

## Visual Diagram: How the Line cuts Shapes

We sweep a horizontal line `y = currentY` upwards.
For ANY square `(y, length)`, there are 3 cases:

### Case 1: Square is Completely BELOW Line
(`top <= currentY`)
```
          Line ──────────────────── (currentY)
    ┌─────┐
    │     │  Area = Full Square
    │     │  (length * length)
    └─────┘
```

### Case 2: Square is Completely ABOVE Line
(`y >= currentY`)
```
    ┌─────┐
    │     │  Area = 0
    │     │  (Line hasn't reached it)
    └─────┘
          Line ──────────────────── (currentY)
```

### Case 3: Line CUTS through Square
(`y < currentY < top`)
```
      ┌─────┐  (top = y + length)
      │     │
Line ─┼─────┼─ (currentY)        ↑
      │/////│  Area = shaded     | height = currentY - y
      └─────┘  (y)               ↓

Area = width * height
     = length * (currentY - y)
```

### Example Calculation
Input: `[[0,0,2]]` (One 2x2 square at 0,0)
Check `currentY = 1.5`

```
2.0 ┌──────────┐
    │          │
    │ Uncounted│
    │          │
1.5 ├──────────┤ <─── Line
    │//////////│
    │/Counted//│ height = 1.5 - 0 = 1.5
    │//////////│
0.0 └──────────┘
    0          2
    
Area Below = width * height
           = 2 * 1.5 
           = 3.0
```

---

## Complexity Analysis

| Solution | Time | Space | Correct? | Why? |
|----------|------|-------|----------|------|
| Brute Force | ∞ | O(1) | ❌ Impossible | Continuous range |
| **Binary Search** | O(N × log(Range/ε)) | O(1) | ✅ **Optimal** | Efficiently narrows real range |

- **Time**: Each iteration takes O(N) to sum areas. 100 iterations is constant. Total `O(100N)` ≈ `O(N)`.
- **Space**: O(1).

---

## Key Takeaways

1. **Continuous Range Search**: When looking for a real number `x` and the check function is monotonic → Binary Search.
2. **Fixed Iterations**: For `double` binary search, `for(i=0; i<100; i++)` is extremely robust and avoids epsilon tuning issues.
3. **Geometry Intersection**: Handled simply by checking bounds (Above/Below/Intersects).

---

## The Journey (TL;DR)

```
🐢 Brute Force: Iterate Y values? → IMPOSSIBLE (Infinite values)
         ↓
💡 "Area grows monotonically with Y! Area(Y) is increasing."
         ↓
✅ Binary Search: Pinpoint Y where Area(Y) == Total/2 → OPTIMAL O(N)
```
