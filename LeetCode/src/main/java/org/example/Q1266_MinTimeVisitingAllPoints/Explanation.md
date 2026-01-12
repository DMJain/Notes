# Minimum Time Visiting All Points - Explanation

## Problem in Simple Words
You need to visit a list of points on a 2D grid in order.
- You can move Horizontal (1 sec), Vertical (1 sec), or Diagonal (1 sec).
- Diagonal move changes BOTH x and y by 1.
- Find minimum time to travel through all points.

**Example**: From `[1, 1]` to `[3, 4]`
- `x` diff: `|3 - 1| = 2`
- `y` diff: `|4 - 1| = 3`
- You need 2 diagonal steps (covers 2x and 2y) + 1 vertical step (covers remaining 1y).
- Total time = 3.

---

## Solution 1: Simulation (Step-by-Step) ❌ (Unnecessary)

### Approach
Actually simulate the movement.
- While `currX != targetX` or `currY != targetY`:
  - Move one step closer diagonally if possible, else horizontally/vertically.
  - Increment time.

### Why It's Bad
- Functional, but unnecessary loop overhead.
- We can calculate the distance mathematically in O(1).

> 💭 **Why simulate when we can just calculate? The answer is purely mathematical!**

---

## Solution 2: Chebyshev Distance (Math) ✅ (Optimal)

### The Connection 🔗
Let's trace our thinking:
- **Simulation** works but is overkill because: we're doing O(distance) work per pair
- **Key insight**: Diagonal moves cover BOTH axes simultaneously, so we want to use them as much as possible!

### The Key Insight 💡
Since a diagonal move costs 1 second and covers 1 unit in both X and Y:
- It is simply the "Best" move.
- We should make as many diagonal moves as possible until we align with the target on one axis.
- Then we make straight moves.

The time to travel between two points is limited by the **axis with the larger difference**.

- `dx = abs(x2 - x1)`
- `dy = abs(y2 - y1)`
- `Time = max(dx, dy)`

This is technically known as the **Chebyshev Distance** on a grid.

### Why This Works

```
Diagonal uses both:      Horizontal/Vertical uses one:
    ↗                         →  or  ↑
 +1 x, +1 y               +1 x     +1 y

If dx=2, dy=3:
- Use 2 diagonal moves: covers 2x, 2y
- Use 1 vertical move: covers remaining 1y
- Total = 3 = max(2, 3)
```

### The Algorithm

1. Initialize `time = 0`.
2. Iterate through points from `i = 1` to `n-1`.
3. For each pair `prev` and `curr`:
   - `dx = abs(curr.x - prev.x)`
   - `dy = abs(curr.y - prev.y)`
   - `time += max(dx, dy)`
4. Return `time`.

---

## Step-by-Step Walkthrough

**Input:** `[[1,1], [3,4], [-1,0]]`

**Step 1: [1,1] to [3,4]**
- `dx = |3 - 1| = 2`
- `dy = |4 - 1| = 3`
- `Time += max(2, 3) = 3`
- Current Total: 3

**Step 2: [3,4] to [-1,0]**
- `dx = |-1 - 3| = 4`
- `dy = |0 - 4| = 4`
- `Time += max(4, 4) = 4`
- Current Total: 3 + 4 = 7

**Final Output**: 7 ✅

---

## Visual: Why max(dx, dy)?

Target is to move `dx=2`, `dy=3`.

```
Start
  ↓
  [ ] → (1) Diagonal step: x+1, y+1  (x_rem=1, y_rem=2)
  [ ] → (2) Diagonal step: x+1, y+1  (x_rem=0, y_rem=1)
  [ ] → (3) Vertical step: y+1       (x_rem=0, y_rem=0)
  
Total Steps = 3
Which equals max(2, 3).
```

---

## Complexity Analysis

| Solution | Time | Space | Correct? | Why? |
|----------|------|-------|----------|------|
| Simulation | O(Sum of distances) | O(1) | ✅ Slow | Simulates each step |
| **Math (Chebyshev)** | O(N) | O(1) | ✅ **Optimal** | O(1) per pair |

- **Time**: We iterate through the points array once. Each step calculation is O(1).
- **Space**: No extra space needed.

---

## Key Takeaways

1. **Chebyshev Distance**: In grids with diagonal movement, distance = `max(|dx|, |dy|)`
2. **Diagonal is Free**: Diagonal moves are "basically free" horizontal moves combined with vertical moves (or vice versa). You always want to use them to reduce the larger dimension's gap.

---

## The Journey (TL;DR)

```
🐢 Simulation: Step by step → OVERKILL (O(distance per pair))
         ↓
💡 "Diagonal covers both axes! Use max(dx, dy)."
         ↓
✅ Chebyshev Distance: Just math → OPTIMAL (O(n))
```
