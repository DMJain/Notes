# Find the Largest Area of Square Inside Two Rectangles - Explanation

> **Prerequisites**: Understanding of coordinate geometry and rectangle boundaries.

> **Related Problems**: 
> - [LeetCode 836 - Rectangle Overlap](https://leetcode.com/problems/rectangle-overlap/) (checking if two rectangles intersect)
> - [LeetCode 223 - Rectangle Area](https://leetcode.com/problems/rectangle-area/) (calculating total area covered by two rectangles)
> - [LeetCode 221 - Maximal Square](https://leetcode.com/problems/maximal-square/) (finding largest square in a matrix)


## Problem in Simple Words

You have rectangles on a plane. Pick any two, find where they overlap, then find the biggest square that fits inside that overlap.

**Example**: Two `2×2` squares at `[1,1]→[3,3]` and `[2,2]→[4,4]` overlap in a `1×1` region at `[2,2]→[3,3]`. Answer: `1`.

---

## Solution 1: Brute Force ❌ (Too Slow for Large N)

### Approach
Check every possible pair of rectangles (there are $\frac{N(N-1)}{2}$ pairs), calculate their intersection, find the square.

```java
for (int i = 0; i < n; i++) {
    for (int j = i + 1; j < n; j++) {
        // Calculate intersection
        // Find max square side
    }
}
```

### Why It's Bad
For $N = 1000$, we check:
```
1000 × 999 / 2 = ~500,000 pairs
```
Each check is O(1), so total is **O(N²)** = ~500,000 ops.

**Wait, that's actually FINE!** ✅

Modern computers handle $10^8$ ops/sec. $500,000$ ops takes ~0.005 seconds.

### The Twist 💡
This problem is a **trick question**. The "brute force" IS the optimal solution because $N \le 1000$.

> 💭 **But let's assume N was $10^5$. Then $N^2 = 10^{10}$ would TLE. What would we need? A Sweep Line algorithm. But for N=1000, don't overcomplicate!**

---

## Solution 2: Sweep Line ❌ (Overkill for This Problem)

### The Natural Thought
"For huge N, we could use a sweep line to find overlaps in $O(N \log N)$..."

### Why It's Not Needed Here
| N | Brute O(N²) | Sweep O(N log N) | Winner |
|---|-------------|------------------|--------|
| 1,000 | 1M ops (~0.01s) | 10K ops (~0.0001s) | Both fine! |
| 100,000 | 10B ops (TLE!) | 1.6M ops (✅) | Sweep wins |

**Verdict**: For $N \le 1000$, the brute force is simpler, more readable, and fast enough.

> 💭 **Let's stick with O(N²). But HOW do we check if two rectangles overlap?**

---

## Solution 3: The "If-Else Mess" Trap ❌ (Overcomplicated)

### The Natural Thought
"Check all the cases: is R1 left of R2? Above? Below?"

### Approach
```java
// BAD CODE
if (rect1.right < rect2.left) return false; // R1 is left of R2
else if (rect1.left > rect2.right) return false; // R1 is right of R2
else if (rect1.top < rect2.bottom) return false; // R1 is below R2
else if (rect1.bottom > rect2.top) return false; // R1 is above R2
else {
    // Calculate overlap boundaries... HOW?!
    // More if-else spaghetti!
}
```

### Example Where It's MESSY ❌
```
Rectangles at an angle, partially overlapping...
8 cases to handle manually!
Code balloons to 50+ lines.
```

### Why It Fails 🤯
You're thinking in terms of **"do they overlap?"** instead of **"where's the overlap?"**

> 💭 **There's a mathematical formula that directly gives overlap coordinates. What if we just compute `[max(starts), min(ends)]` for X and Y?**

---

## Solution 4: Intersection Formula ✅ (Optimal)

### The Connection 🔗
Let's trace our thinking:
- **Brute O(N²)** is fine for N=1000 → stick with it!
- **If-else checks** are messy → need a cleaner formula!
- **What we need**: Direct calculation of overlap boundaries → **Projection Math!**

### The Key Insight 💡
For any two intervals on a number line, their overlap is:
$$\text{Overlap} = [\max(\text{start}_1, \text{start}_2), \min(\text{end}_1, \text{end}_2)]$$

If $\max(\text{starts}) \ge \min(\text{ends})$, there's **no overlap** (invalid interval).

Rectangles are just 2D: apply this to **both X-axis and Y-axis**!

### The Algorithm
```
For each pair (i, j):
    1. Calculate X-overlap:
       left = max(BL[i].x, BL[j].x)
       right = min(TR[i].x, TR[j].x)
    
    2. Calculate Y-overlap:
       bottom = max(BL[i].y, BL[j].y)
       top = min(TR[i].y, TR[j].y)
    
    3. Check validity:
       if left < right AND bottom < top:
           width = right - left
           height = top - bottom
           squareSide = min(width, height)
           maxSide = max(maxSide, squareSide)
    
Return maxSide²
```

### Step-by-Step Walkthrough

**Input:**
```
Rect 0: BL=[1,1], TR=[3,3]
Rect 1: BL=[2,2], TR=[4,4]
Rect 2: BL=[5,5], TR=[7,7]
```

| Pair | Left | Right | Bottom | Top | Width | Height | Side | Max |
|------|------|-------|--------|-----|-------|--------|------|-----|
| **(0,1)** | max(1,2)=2 | min(3,4)=3 | max(1,2)=2 | min(3,4)=3 | 1 | 1 | **1** | 1 |
| **(0,2)** | max(1,5)=5 | min(3,7)=3 | - | - | **Invalid** (5>3) | - | - | 1 |
| **(1,2)** | max(2,5)=5 | min(4,7)=4 | - | - | **Invalid** (5>4) | - | - | 1 |

**Final Answer**: $1^2 = 1$

### Visual Diagram
```
         4 ┌────────┐ R1
           │ ┌──────┼──┐
         3 ├─┼──┬───┤  │
           │ │██│   │  │  ← Overlap (1×1)
         2 │ └──┼───┘  │
           │    │ R0   │
         1 └────┴──────┘
           1  2  3     4

R0 ∩ R1 = [2,3] × [2,3] → Square side = min(1,1) = 1
```

### Code
```java
public long largestSquareArea(int[][] bottomLeft, int[][] topRight) {
    long maxSide = 0;
    int n = bottomLeft.length;
    
    for (int i = 0; i < n; i++) {
        for (int j = i + 1; j < n; j++) {
            // X-axis projection
            int left = Math.max(bottomLeft[i][0], bottomLeft[j][0]);
            int right = Math.min(topRight[i][0], topRight[j][0]);
            
            // Y-axis projection
            int bottom = Math.max(bottomLeft[i][1], bottomLeft[j][1]);
            int top = Math.min(topRight[i][1], topRight[j][1]);
            
            // Valid overlap?
            if (left < right && bottom < top) {
                long width = right - left;
                long height = top - bottom;
                long side = Math.min(width, height);
                maxSide = Math.max(maxSide, side);
            }
        }
    }
    
    return maxSide * maxSide;
}
```

---

## Complexity Analysis

| Solution | Time | Space | Correct? | Why? |
|----------|------|-------|----------|------|
| Brute O(N²) | O(N²) | O(1) | ✅ Optimal for N≤1000 | 500K ops = fast enough |
| Sweep Line | O(N log N) | O(N) | ✅ But overkill | Only needed if N>10⁵ |
| **Projection Formula** | **O(N²)** | **O(1)** | ✅ **Best** | Clean code + optimal time |

---

## Key Takeaways

1. **Constraint Analysis First**: $N \le 1000$ → $O(N^2)$ is perfectly fine!
2. **Projection Formula**: Overlap = `[max(starts), min(ends)]` works for ANY interval intersection (1D, 2D, time ranges, etc.)
3. **Square in Rectangle**: Max square side = `min(width, height)`
4. **Don't Overcomplicate**: If-else chains for overlaps are error-prone. Use math!

---

## The Journey (TL;DR)

```
🐢 O(N²) Brute Force → Actually fine for N=1000!
         ↓
💡 "But HOW to check overlap efficiently?"
         ↓
❌ If-Else Spaghetti → Too messy (8+ cases!)
         ↓
💡 "Just calculate [max(starts), min(ends)] for X and Y!"
         ↓
✅ Projection Formula → Clean, fast, O(N²) is optimal!
```
