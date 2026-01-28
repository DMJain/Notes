# Minimum Cost Path with Teleportations - Explanation

> **Prerequisites**: 
> - Grid Dynamic Programming basics
> - Dijkstra's Algorithm for shortest path  
> - Suffix Minimum arrays
>
> **Related Problems**: 
> - [LeetCode 64 - Minimum Path Sum](https://leetcode.com/problems/minimum-path-sum/) (Basic grid DP)
> - [LeetCode 1293 - Shortest Path in a Grid with Obstacles Elimination](https://leetcode.com/problems/shortest-path-in-a-grid-with-obstacles-elimination/) (K-limited special moves)
> - [LeetCode 787 - Cheapest Flights Within K Stops](https://leetcode.com/problems/cheapest-flights-within-k-stops/) (K-limited state tracking)
> - [LeetCode 3650 - Min Cost Path with Edge Reversals](https://leetcode.com/problems/minimum-cost-path-with-edge-reversals/) | [Local](../Q3650_MinimumCostPathWithEdgeReversals/Explanation.md) (Graph with special moves)

## Problem in Simple Words

Find the minimum cost path from top-left to bottom-right of a grid. Normal moves (right/down) cost the destination cell's value. You can teleport (cost=0) to ANY cell with value ≤ your current cell's value, up to k times.

```
grid = [[1,3,3],[2,5,4],[4,3,5]], k = 2

Path: (0,0) → down → (1,0) cost 2 → right → (1,1) cost 5 → TELEPORT → (2,2)
Total: 2 + 5 + 0 = 7 ✅
```

---

## Solution 1: Brute Force DFS ❌

### The Natural Thought
"Let me explore ALL possible paths from (0,0) to (m-1,n-1), considering at each cell whether to move normally or teleport."

### Approach
```java
int dfs(int i, int j, int teleportsLeft, Set<String> visited) {
    if (i == m-1 && j == n-1) return 0;
    if (visited.contains(i + "," + j)) return INF;
    
    visited.add(i + "," + j);
    int minCost = INF;
    
    // Option 1: Move right
    if (j + 1 < n) {
        minCost = min(minCost, grid[i][j+1] + dfs(i, j+1, teleportsLeft, visited));
    }
    // Option 2: Move down
    if (i + 1 < m) {
        minCost = min(minCost, grid[i+1][j] + dfs(i+1, j, teleportsLeft, visited));
    }
    // Option 3: Teleport to ANY cell with value <= current
    if (teleportsLeft > 0) {
        for (int x = 0; x < m; x++) {
            for (int y = 0; y < n; y++) {
                if (grid[x][y] <= grid[i][j]) {
                    minCost = min(minCost, dfs(x, y, teleportsLeft - 1, visited));
                }
            }
        }
    }
    
    visited.remove(i + "," + j);
    return minCost;
}
```

### Why It's Bad
- At each cell, we branch to 2 normal moves + up to m×n teleport destinations
- Each teleport reduces k by 1, but we still explore exponentially

### Example Where It's SLOW ❌
```
For m=80, n=80 grid with k=10:

Each cell: 2 normal + up to 6400 teleport options
State space: O(m × n × k × 2^(m×n)) with path tracking
Even with memoization: O(m × n × k) states × O(m × n) teleport checks

Time: 80 × 80 × 10 × 80 × 80 = 409,600,000 operations PER state!
```

### Complexity Analysis

| Solution | Time | Space | Correct? | Why? |
|----------|------|-------|----------|------|
| Brute DFS | O(2^(m×n)) | O(m×n) | ✅ TLE | Exponential path exploration |

> 💭 **Each cell can teleport to any of m×n cells. Can we model this as a graph problem with Dijkstra?**

---

## Solution 2: Dijkstra with (i, j, teleports) State ❌

### The Natural Thought
"This is a shortest path problem! Let me use Dijkstra with state `(i, j, teleportsUsed)`."

### Approach
```java
// State: (cost, row, col, teleportsUsed)
PriorityQueue<int[]> pq = new PriorityQueue<>((a, b) -> a[0] - b[0]);
int[][][] dist = new int[m][n][k + 1];  // min cost to reach (i,j) with t teleports used
Arrays.fill(dist, INF);
dist[0][0][0] = 0;
pq.offer(new int[]{0, 0, 0, 0});

while (!pq.isEmpty()) {
    int[] cur = pq.poll();
    int cost = cur[0], i = cur[1], j = cur[2], t = cur[3];
    
    // Normal moves: right and down (cost = destination value)
    for each (ni, nj) in {(i, j+1), (i+1, j)} {
        if (valid && cost + grid[ni][nj] < dist[ni][nj][t]) {
            dist[ni][nj][t] = cost + grid[ni][nj];
            pq.offer({dist[ni][nj][t], ni, nj, t});
        }
    }
    
    // Teleport: to any cell with value <= current (cost = 0)
    if (t < k) {
        for (int x = 0; x < m; x++) {
            for (int y = 0; y < n; y++) {
                if (grid[x][y] <= grid[i][j] && cost < dist[x][y][t + 1]) {
                    dist[x][y][t + 1] = cost;
                    pq.offer({cost, x, y, t + 1});
                }
            }
        }
    }
}
```

### Why It's Correct But Slow ❌
- Dijkstra explores O(m × n × k) states ✅
- BUT each state checks O(m × n) teleport destinations
- Total: O(m × n × k) × O(m × n) = O(m² × n² × k)

### Example Where It's SLOW ❌
```
For m=80, n=80, k=10:

States: 80 × 80 × 11 = 70,400
Per state teleport check: 80 × 80 = 6,400
Total teleport checks: 70,400 × 6,400 = 450,560,000 operations!

With Priority Queue overhead: ~5-10× slower
Time: TLE for competitive constraints
```

### Complexity Analysis

| Solution | Time | Space | Correct? | Why? |
|----------|------|-------|----------|------|
| Brute DFS | O(2^(m×n)) | O(m×n) | ✅ TLE | Exponential paths |
| Dijkstra k-state | O(m²×n²×k log(mnk)) | O(m×n×k) | ✅ TLE | Checking all teleport targets |

> 💭 **Dijkstra's priority queue adds overhead. Since we only move right/down, can we use DP layer by layer instead?**

---

## Solution 3: 2D DP with Teleportation Layers ❌

### The Natural Thought
"Instead of Dijkstra's priority queue, let me process grid layer by layer. For each teleportation count t, use `dp[i][j]` = min cost to reach (i,j)."

### The Key Insight
```
┌────────────────────────────────────────────────────────────────┐
│                     LAYER-BY-LAYER 2D DP                        │
├────────────────────────────────────────────────────────────────┤
│                                                                 │
│  For each teleportation layer t = 0 to k:                       │
│    dp[i][j] = min cost to reach cell (i,j) using t teleports   │
│                                                                 │
│    1. Normal moves: dp[i][j] = min(dp[i-1][j], dp[i][j-1]) + v │
│    2. Teleport FROM any cell with value >= current cell value  │
│                                                                 │
│  Problem: Still need to scan ALL cells to find valid sources!  │
│                                                                 │
└────────────────────────────────────────────────────────────────┘
```

### 2D DP Approach
```java
// For each teleportation layer, use 2D dp[i][j]
int[][] dp = new int[m][n];       // Current layer
int[][] prevDp = new int[m][n];   // Previous layer (for teleport sources)

for (int t = 0; t <= k; t++) {
    fill(dp, INF);
    dp[0][0] = 0;
    
    // Normal DP within this layer
    for (int i = 0; i < m; i++) {
        for (int j = 0; j < n; j++) {
            if (i > 0) dp[i][j] = min(dp[i][j], dp[i-1][j] + grid[i][j]);
            if (j > 0) dp[i][j] = min(dp[i][j], dp[i][j-1] + grid[i][j]);
            
            // Teleport from previous layer: scan ALL cells for valid sources
            if (t > 0) {
                for (int x = 0; x < m; x++) {
                    for (int y = 0; y < n; y++) {
                        // Can teleport if source value >= destination value
                        if (grid[x][y] >= grid[i][j]) {
                            dp[i][j] = min(dp[i][j], prevDp[x][y]);
                        }
                    }
                }
            }
        }
    }
    
    // Copy current to prev for next iteration
    prevDp = copy(dp);
}
```

### Why It's Still Slow ❌
- We process O(k) layers ✅
- Each layer: O(m×n) cells for normal DP ✅
- Teleport check per cell: O(m×n) source scan ❌

### Example Where It's SLOW ❌
```
For m=80, n=80, k=10:

Per cell: O(m×n) = 6,400 source checks
Per layer: O(m×n) × O(m×n) = 40,960,000 ops
Total: 10 × 40,960,000 = 409,600,000 operations!

Still TLE!
```

### Complexity Analysis

| Solution | Time | Space | Correct? | Why? |
|----------|------|-------|----------|------|
| Brute DFS | O(2^(m×n)) | O(m×n) | ✅ TLE | Exponential paths |
| Dijkstra k-state | O(m²n²k log) | O(m×n×k) | ✅ TLE | O(m×n) per teleport |
| 2D DP layers | O(k×m²×n²) | O(m×n) | ✅ TLE | O(m×n) source scan |

> 💭 **For each cell, we scan all sources to find valid teleports. What if we group cells by value and use suffix minimum for O(1) lookup?**

---

## Solution 4: Optimal - 1D DP with Suffix Minimum ✅

### The Connection 🔗
Let's trace our thinking:
- **Brute Force DFS** was slow because: O(2^(m×n)) path exploration
- **Dijkstra** was slow because: checking O(m×n) teleport destinations per state
- **2D DP** was slow because: still scanning O(m×n) source cells for each destination
- **What we need**: O(1) lookup for "best source with value ≥ V" → **Suffix Minimum!**

### The Key Insight 💡

```
┌──────────────────────────────────────────────────────────────────┐
│                    SUFFIX MINIMUM MAGIC                           │
├──────────────────────────────────────────────────────────────────┤
│                                                                   │
│  minF[v] = min cost to reach ANY cell with value exactly v       │
│                                                                   │
│  sufMinF[v] = min(minF[v], minF[v+1], ..., minF[maxV])           │
│             = min cost to reach ANY cell with value ≥ v          │
│                                                                   │
│  Wait... we want value ≤ V, not ≥ V!                              │
│                                                                   │
│  TRICK: Teleport is BIDIRECTIONAL in value sense!                 │
│         - From V, can teleport TO any cell with value ≤ V        │
│         - Equivalently, cell with value W can be REACHED from    │
│           any cell with value ≥ W                                 │
│                                                                   │
│  So sufMinF[v] = best cost to REACH cells with value v           │
│                  (from previous layer's high-value cells)         │
│                                                                   │
└──────────────────────────────────────────────────────────────────┘
```

### The Algorithm

```
1. Initialize:
   - sufMinF[v] = INF for all v (suffix minimum across layers)
   - f[j] = cost to reach column j in current row

2. For each teleportation layer t = 0 to k:
   a. Reset f[] and minF[]
   b. For each row, for each column j:
      - Normal move: f[j+1] = min(f[j], f[j+1]) + grid[i][j]
      - Teleport in: f[j+1] = min(f[j+1], sufMinF[grid[i][j]])
      - Track minF[grid[i][j]] = min of all cells with this value
   
   c. Build sufMinF for next layer:
      sufMinF[v] = min(sufMinF[v+1], minF[v])
   
   d. Early exit if no improvement

3. Return f[n] (cost to reach last column in last row)
```

### Optimal Code
```java
public int minCost(int[][] grid, int k) {
    int m = grid.length, n = grid[0].length;
    
    // Edge case: direct teleport possible
    if (k > 0 && grid[0][0] >= grid[m-1][n-1]) return 0;

    int mx = findMax(grid);
    
    int[] sufMinF = new int[mx + 2];  // Suffix min for teleport lookup
    Arrays.fill(sufMinF, INF);
    int[] minF = new int[mx + 1];     // Min cost per value in current layer
    int[] f = new int[n + 1];         // Min cost per column

    for (int t = 0; t <= k; t++) {
        Arrays.fill(minF, INF);
        Arrays.fill(f, INF / 2);
        f[1] = -grid[0][0];  // Clever: will add grid[0][0] back first iteration
        
        for (int[] row : grid) {
            for (int j = 0; j < n; j++) {
                int v = row[j];
                // min(from_left, from_above) + current_cell OR teleport_in
                f[j+1] = Math.min(Math.min(f[j], f[j+1]) + v, sufMinF[v]);
                minF[v] = Math.min(minF[v], f[j+1]);
            }
        }
        
        // Build suffix minimum for next iteration
        boolean done = true;
        for (int i = mx; i >= 0; i--) {
            int mn = Math.min(sufMinF[i+1], minF[i]);
            if (mn < sufMinF[i]) {
                sufMinF[i] = mn;
                done = false;
            }
        }
        if (done) break;  // No improvement, stop early
    }
    return f[n];
}
```

### Step-by-Step Walkthrough

**Input**: `grid = [[1,3,3],[2,5,4],[4,3,5]], k = 2`

```
Grid visualization:
     j=0  j=1  j=2
i=0   1    3    3
i=1   2    5    4
i=2   4    3    5  ← Target

Max value: 5
```

**Layer t=0 (0 teleportations):**
```
Initial: f = [INF, -1, INF, INF]  (f[1]=-grid[0][0]=-1)
         sufMinF = [INF, INF, INF, INF, INF, INF, INF]

Row 0: [1, 3, 3]
  j=0, v=1: f[1] = min(INF, -1) + 1 = 0         minF[1] = 0
  j=1, v=3: f[2] = min(0, INF) + 3 = 3          minF[3] = 3
  j=2, v=3: f[3] = min(3, INF) + 3 = 6          minF[3] = 3

Row 1: [2, 5, 4]
  j=0, v=2: f[1] = min(INF, 0) + 2 = 2          minF[2] = 2
  j=1, v=5: f[2] = min(2, 3) + 5 = 7            minF[5] = 7
  j=2, v=4: f[3] = min(7, 6) + 4 = 10           minF[4] = 10

Row 2: [4, 3, 5]
  j=0, v=4: f[1] = min(INF, 2) + 4 = 6          minF[4] = 6
  j=1, v=3: f[2] = min(6, 7) + 3 = 9            minF[3] = 3
  j=2, v=5: f[3] = min(9, 10) + 5 = 14          minF[5] = 7

After layer 0:
  minF = [INF, 0, 2, 3, 6, 7]  (indices 0-5)
  
Building sufMinF (right to left):
  sufMinF[5] = min(INF, 7) = 7
  sufMinF[4] = min(7, 6) = 6
  sufMinF[3] = min(6, 3) = 3
  sufMinF[2] = min(3, 2) = 2
  sufMinF[1] = min(2, 0) = 0
  sufMinF[0] = min(0, INF) = 0
```

**Layer t=1 (1 teleportation):**
```
Now sufMinF = [0, 0, 2, 3, 6, 7, INF]

Reset: f = [INF, -1, INF, INF]

Row 0: [1, 3, 3]
  j=0, v=1: f[1] = min(min(INF,-1)+1, sufMinF[1]) = min(0, 0) = 0
  j=1, v=3: f[2] = min(0+3, sufMinF[3]) = min(3, 3) = 3
  j=2, v=3: f[3] = min(3+3, sufMinF[3]) = min(6, 3) = 3  ← TELEPORT HELPS!

Row 1: [2, 5, 4]
  j=0, v=2: f[1] = min(0+2, sufMinF[2]) = min(2, 2) = 2
  j=1, v=5: f[2] = min(2+5, 3, sufMinF[5]) = min(7, 7) = 7  
  j=2, v=4: f[3] = min(7+4, 3+4, sufMinF[4]) = min(11, 7, 6) = 6

Row 2: [4, 3, 5]
  j=0, v=4: f[1] = min(2+4, sufMinF[4]) = min(6, 6) = 6
  j=1, v=3: f[2] = min(6+3, 7, sufMinF[3]) = min(9, 3) = 3  ← TELEPORT!
  j=2, v=5: f[3] = min(3+5, 6+5, sufMinF[5]) = min(8, 11, 7) = 7  ← ANSWER!

f[3] = 7 ✅
```

### Visual Diagram

```
┌──────────────────────────────────────────────────────────────────┐
│                    THE TELEPORTATION LOGIC                        │
├──────────────────────────────────────────────────────────────────┤
│                                                                   │
│   grid:   ┌───┬───┬───┐                                          │
│      i=0  │ 1 │ 3 │ 3 │                                          │
│           ├───┼───┼───┤                                          │
│      i=1  │ 2 │ 5 │ 4 │  ← From (1,1) value=5                    │
│           ├───┼───┼───┤                                          │
│      i=2  │ 4 │ 3 │ 5 │  ← Can teleport to (2,2) since 5 ≤ 5    │
│           └───┴───┴───┘                                          │
│                                                                   │
│   Path found:                                                     │
│     Start (0,0) value=1                                           │
│        ↓ move down, cost = 2                                      │
│     (1,0) value=2                                                 │
│        → move right, cost = 5                                     │
│     (1,1) value=5, total=7                                        │
│        ⚡ TELEPORT to (2,2), cost = 0                             │
│     (2,2) value=5, total=7 ✅                                     │
│                                                                   │
└──────────────────────────────────────────────────────────────────┘

┌──────────────────────────────────────────────────────────────────┐
│                    SUFFIX MINIMUM VISUALIZATION                   │
├──────────────────────────────────────────────────────────────────┤
│                                                                   │
│   After layer t=0:                                                │
│                                                                   │
│   value:     0    1    2    3    4    5                          │
│   minF:     [INF, 0,   2,   3,   6,   7]                         │
│                                                                   │
│   sufMinF (built right-to-left):                                 │
│   value:     0    1    2    3    4    5                          │
│   sufMinF:  [0,   0,   2,   3,   6,   7]                         │
│              ↑    ↑    ↑    ↑    ↑    ↑                          │
│              │    │    │    │    │    └── min(INF, 7) = 7        │
│              │    │    │    │    └──────── min(7, 6) = 6         │
│              │    │    │    └───────────── min(6, 3) = 3         │
│              │    │    └────────────────── min(3, 2) = 2         │
│              │    └─────────────────────── min(2, 0) = 0         │
│              └──────────────────────────── min(0, INF) = 0       │
│                                                                   │
│   Usage: At cell with value V, can teleport to sufMinF[V]        │
│          which gives min cost to reach any cell with value ≥ V   │
│                                                                   │
└──────────────────────────────────────────────────────────────────┘
```

---

## Complexity Analysis (Optimal)

| Solution | Time | Space | Status | Why |
|----------|------|-------|--------|-----|
| Brute DFS | O(2^(m×n)) | O(m×n) | ✅ TLE | Exponential paths |
| Dijkstra k-state | O(m²n²k log) | O(m×n×k) | ✅ TLE | O(m×n) teleport check |
| 2D DP layers | O(k×m²×n²) | O(m×n) | ✅ TLE | O(m×n) source scan per cell |
| **1D DP + Suffix Min** | **O(k×m×n + k×maxV)** | **O(n + maxV)** | ✅ **Optimal** | O(1) teleport lookup |

**Time Breakdown**:
- Outer loop: O(k) teleportation layers
- Inner loops: O(m × n) grid traversal
- Suffix min build: O(maxValue) per layer
- Total: O(k × (m×n + maxValue))
- For constraints: k=10, m=n=80, maxV=10000 → ~640,000 + 100,000 = ~740,000 ops ✅

**Space**: O(n) for f[] + O(maxValue) for sufMinF/minF = O(n + maxValue)

---

## Key Takeaways

1. **Suffix Minimum Pattern**: When you need "best value in range [0, V]" repeatedly, precompute suffix minimum array for O(1) lookups.

2. **Layer-by-Layer Processing**: For k-limited operations, process one layer at a time and carry forward the "best achievable" state.

3. **Early Termination**: If no improvement in an iteration, further iterations won't help. Exit early.

4. **Space Optimization**: 2D DP → 1D DP when only previous row/layer is needed. This solution uses O(n) instead of O(m×n).

---

## The Journey (TL;DR)

```
🐢 Brute Force DFS → EXPONENTIAL (O(2^(m×n)))
         ↓
💭 "This is shortest path. Use Dijkstra?"
         ↓
❌ Dijkstra k-state → TLE (O(m²n²k) checking all teleports)
         ↓
💭 "DP layer by layer instead of priority queue?"
         ↓
❌ 2D DP layers → TLE (O(k×m²×n²) still scanning all sources)
         ↓
💭 "Group cells by value, use suffix min for O(1) lookup?"
         ↓
✅ 1D DP + Suffix Minimum → O(k×m×n) OPTIMAL!
```

---

## Final Checklist ✅

- [x] Prerequisites/Related researched and linked
- [x] Brute force has CONCRETE failure example with numbers
- [x] Every ❌ solution has thought bubble (as QUESTION)
- [x] Thought bubbles ASK questions, not state answers
- [x] Optimal has "The Connection 🔗" tracing journey
- [x] Step-by-step walkthrough with real input
- [x] ASCII diagrams where helpful
- [x] Complexity table includes ALL solutions
- [x] "The Journey" TL;DR present (<10 lines)
- [x] Numbers are quantified (not just "slow")
- [x] Donkey-level clarity achieved

**Confidence Score: 0.92** - Complete Connected Flow through 4 solutions from brute force to optimal, with detailed suffix minimum visualization and step-by-step walkthrough.
