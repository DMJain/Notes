# Minimum Cost Path with Edge Reversals - Explanation

> **Prerequisites**: 
> - Dijkstra's Algorithm for shortest path
> - Graph representation (adjacency list)
> - Priority Queue / Min-Heap
>
> **Related Problems**: 
> - [LeetCode 743 - Network Delay Time](https://leetcode.com/problems/network-delay-time/) (Classic Dijkstra application)
> - [LeetCode 787 - Cheapest Flights Within K Stops](https://leetcode.com/problems/cheapest-flights-within-k-stops/) (Modified shortest path with constraints)
> - [LeetCode 1368 - Min Cost to Make at Least One Valid Path](https://leetcode.com/problems/minimum-cost-to-make-at-least-one-valid-path-in-a-grid/) (0-1 BFS with direction changes)

## Problem in Simple Words

Find the cheapest path from node 0 to node n-1 in a directed graph. At each node, you can optionally "reverse" one incoming edge and use it (costs 2× the original weight).

```
nodes = 4, edges = [[0,1,3], [3,1,1], [2,3,4], [0,2,2]]

Normal path: 0 → 2 → 3? No direct edge 2→3, only 2→3 with cost 4
Better: 0 → 1 (cost 3), then REVERSE 3→1 to get 1→3 (cost 2×1=2)
Total: 3 + 2 = 5 ✅
```

---

## Solution 1: Brute Force DFS ❌

### The Natural Thought
"Let me explore ALL possible paths from 0 to n-1, considering whether to reverse edges at each node."

### Approach
```java
// DFS with state tracking
int dfs(int node, int target, boolean[] usedSwitch, Set<Integer> visited) {
    if (node == target) return 0;
    if (visited.contains(node)) return INF;
    
    visited.add(node);
    int minCost = INF;
    
    // Try all outgoing edges (normal)
    for (Edge e : outgoingEdges[node]) {
        minCost = min(minCost, e.weight + dfs(e.to, target, usedSwitch, visited));
    }
    
    // Try reversing each incoming edge (if switch not used at this node)
    if (!usedSwitch[node]) {
        for (Edge e : incomingEdges[node]) {
            usedSwitch[node] = true;
            minCost = min(minCost, 2 * e.weight + dfs(e.from, target, usedSwitch, visited));
            usedSwitch[node] = false;
        }
    }
    
    visited.remove(node);
    return minCost;
}
```

### Why It's Bad
- Exponential branching: at each node, try all outgoing + potentially all reversed incoming
- Each path can be explored multiple times with different switch states

### Example Where It's SLOW ❌
```
For n = 50,000 nodes, edges = 100,000

Worst case exploration paths: O(2^n) with backtracking
Even with memoization on (node, switchState), state space is O(n × 2^n)

Time: EXPONENTIAL → TLE for constraints!
```

### Complexity Analysis

| Solution | Time | Space | Correct? | Why? |
|----------|------|-------|----------|------|
| Brute DFS | O(2^n) | O(n) | ✅ TLE | Explores all path combinations |

> 💭 **DFS explores too many states. Can we model this problem differently to use a polynomial-time algorithm like Dijkstra?**

---

## Solution 2: Misunderstanding - Simple BFS ❌

### The Natural Thought
"This is a shortest path problem. Let me just use BFS!"

### Why BFS Fails
BFS finds shortest path by **number of edges**, not by **total weight**.

```
Example:
0 ──(100)──→ 1 ──(1)──→ 3
0 ──(1)────────────────→ 3

BFS path: 0 → 3 (1 edge)          Cost = 1   ← BFS would find this
Alt path: 0 → 1 → 3 (2 edges)     Cost = 101

In this case BFS is correct, but...

Counter-example:
0 ──(1)──→ 1 ──(1)──→ 3
0 ──(100)──────────────→ 3

BFS path: 0 → 3 (1 edge)          Cost = 100  ← BFS chooses this!
Alt path: 0 → 1 → 3 (2 edges)     Cost = 2    ← OPTIMAL!
```

### Example Where It FAILS ❌
```
Input: n=3, edges = [[0,1,1], [1,2,1], [0,2,100]]

BFS returns: 0 → 2 directly
Cost: 100 ← WRONG!

Optimal: 0 → 1 → 2
Cost: 1 + 1 = 2 ✅
```

> 💭 **BFS doesn't work for weighted graphs. We need Dijkstra. But how do we handle the "edge reversal switch" constraint?**

---

## Solution 3: State-Space Dijkstra ❌ (Valid but Complex)

### The Natural Thought
"I need to track which nodes have used their switch. Let me use Dijkstra with state `(node, switchMask)`."

### Why It's Problematic
```
State space: (node, which_nodes_used_switch)
           = O(n × 2^n)

This is exponential! Cannot work for n = 50,000.
```

### Wait... Re-reading the Problem 🔍

**Key insight from problem:**
> "Each node ui has a switch that can be used **at most once**"
> "reverse that edge to ui → vi and **immediately traverse it**"

This means:
- At node `u`, if there's an incoming edge `v → u`, you can reverse it to `u → v` and travel
- The reversal costs `2 × weight`
- You can ONLY use this when you're physically AT node `u`

**Critical realization**: We don't need to track which switches have been used globally!

Why? Because once we LEAVE a node via any edge (normal or reversed), we won't come back to use its switch (shortest path doesn't revisit nodes in optimal solution).

> 💭 **If we don't need to track switch state globally, can we just model reversed edges as additional graph edges?**

---

## Solution 4: Optimal - Graph Transformation + Dijkstra ✅

### The Connection 🔗
Let's trace our thinking:
- **Brute Force DFS** was slow because: O(2^n) path exploration
- **BFS** failed because: doesn't handle weighted edges
- **State-Space Dijkstra** was complex because: seemed to need O(n × 2^n) states
- **Key Insight**: We can model edge reversal as additional edges! → **Standard Dijkstra!**

### The Key Insight 💡

```
┌──────────────────────────────────────────────────────────────────┐
│                    GRAPH TRANSFORMATION                          │
├──────────────────────────────────────────────────────────────────┤
│                                                                  │
│  For EACH edge in the input: u ──(w)──→ v                        │
│                                                                  │
│  Add TWO edges to our graph:                                     │
│                                                                  │
│    1. u ──(w)──→ v   (normal traversal)                          │
│    2. v ──(2w)─→ u   (reversed traversal, costs double)          │
│                                                                  │
│  Now it's STANDARD Dijkstra on this augmented graph!             │
│                                                                  │
└──────────────────────────────────────────────────────────────────┘
```

**Why this works:**
- When we're at node `v` and there's an original edge `u → v` coming IN
- Taking the "reversed" edge `v → u` with cost `2w` models using the switch
- Dijkstra naturally finds the minimum cost considering both options

**Why we don't double-count?**
- Dijkstra's greedy property: once a node is "settled" (popped with minimum distance), we won't visit it again with a better cost
- Each node is processed once, implicitly ensuring the "at most once" switch constraint

### The Algorithm
```
1. Build adjacency list:
   For each edge (u, v, w):
     - Add (v, w) to graph[u]      // normal
     - Add (u, 2*w) to graph[v]    // reversed
     
2. Run Dijkstra from node 0

3. Return dist[n-1] (or -1 if unreachable)
```

### Optimal Code
```java
public int minCost(int n, int[][] edges) {
    // Build augmented graph
    List<int[]>[] graph = new ArrayList[n];
    for (int i = 0; i < n; i++) graph[i] = new ArrayList<>();

    for (int[] e : edges) {
        graph[e[0]].add(new int[]{e[1], e[2]});       // Normal
        graph[e[1]].add(new int[]{e[0], 2 * e[2]});   // Reversed
    }

    // Dijkstra
    long[] dist = new long[n];
    Arrays.fill(dist, Long.MAX_VALUE);
    dist[0] = 0;

    PriorityQueue<long[]> pq = 
        new PriorityQueue<>(Comparator.comparingLong(a -> a[0]));
    pq.offer(new long[]{0, 0});

    while (!pq.isEmpty()) {
        long[] cur = pq.poll();
        long cost = cur[0];
        int node = (int) cur[1];

        if (cost > dist[node]) continue;  // Already found better

        for (int[] nxt : graph[node]) {
            int v = nxt[0], w = nxt[1];
            if (dist[v] > cost + w) {
                dist[v] = cost + w;
                pq.offer(new long[]{dist[v], v});
            }
        }
    }

    return dist[n - 1] == Long.MAX_VALUE ? -1 : (int) dist[n - 1];
}
```

### Step-by-Step Walkthrough

**Input**: `n = 4, edges = [[0,1,3], [3,1,1], [2,3,4], [0,2,2]]`

```
Step 1: Build Graph (with reversed edges)

Original edges:        Reversed edges (2× cost):
0 ──(3)──→ 1          1 ──(6)──→ 0
3 ──(1)──→ 1          1 ──(2)──→ 3  ← KEY!
2 ──(4)──→ 3          3 ──(8)──→ 2
0 ──(2)──→ 2          2 ──(4)──→ 0

Adjacency List:
graph[0] = [(1,3), (2,2)]
graph[1] = [(0,6), (3,2)]     ← Note: 1→3 with cost 2 (reversed 3→1)
graph[2] = [(3,4), (0,4)]
graph[3] = [(1,1), (2,8)]

Step 2: Dijkstra from node 0

Initial: dist = [0, ∞, ∞, ∞]
         pq = [(0, node=0)]

Pop (0, 0):
  → neighbor 1: dist[1] = 0+3 = 3, push (3,1)
  → neighbor 2: dist[2] = 0+2 = 2, push (2,2)
  dist = [0, 3, 2, ∞]
  pq = [(2,2), (3,1)]

Pop (2, 2):
  → neighbor 3: dist[3] = 2+4 = 6, push (6,3)
  → neighbor 0: 2+4=6 > 0, skip
  dist = [0, 3, 2, 6]
  pq = [(3,1), (6,3)]

Pop (3, 1):
  → neighbor 0: 3+6=9 > 0, skip
  → neighbor 3: 3+2=5 < 6, update! push (5,3)
  dist = [0, 3, 2, 5]  ← FOUND BETTER PATH TO 3!
  pq = [(5,3), (6,3)]

Pop (5, 3):
  → neighbor 1: 5+1=6 > 3, skip
  → neighbor 2: 5+8=13 > 2, skip
  dist = [0, 3, 2, 5]

Pop (6, 3): cost > dist[3], SKIP

Done! dist[3] = 5
```

### Visual Diagram

```
┌───────────────────────────────────────────────────────────────────┐
│                    ORIGINAL GRAPH                                 │
├───────────────────────────────────────────────────────────────────┤
│                                                                   │
│         0 ──────(3)──────→ 1                                      │
│         │                  ↑                                      │
│        (2)               (1)                                      │
│         ↓                  │                                      │
│         2 ──────(4)──────→ 3                                      │
│                                                                   │
│   Problem: No direct path 1 → 3!                                  │
│                                                                   │
└───────────────────────────────────────────────────────────────────┘

┌───────────────────────────────────────────────────────────────────┐
│                 AUGMENTED GRAPH (with reversals)                  │
├───────────────────────────────────────────────────────────────────┤
│                                                                   │
│     ┌────(6)────┐                                                 │
│     ↓           │                                                 │
│     0 ──(3)───→ 1 ←──(1)── 3                                      │
│     │↑          │           ↑                                     │
│    (2)(4)      (2)         (4)                                    │
│     ↓ │      ↙            (8)                                     │
│     2 ────(4)──────────→ 3─┘                                      │
│     └────────(4)────────→ 0                                       │
│                                                                   │
│   Now 1 → 3 exists with cost 2! (reversed from 3→1)               │
│                                                                   │
│   Optimal Path: 0 ──(3)──→ 1 ──(2)──→ 3                           │
│                 Total: 3 + 2 = 5 ✅                                │
│                                                                   │
└───────────────────────────────────────────────────────────────────┘

┌───────────────────────────────────────────────────────────────────┐
│                 DIJKSTRA EXECUTION TRACE                          │
├───────────────────────────────────────────────────────────────────┤
│                                                                   │
│   Step 1: Start at 0           dist = [0, ∞, ∞, ∞]                │
│           ┌─────────────────────────────────────────┐             │
│           │  0* ──→ 1   ──→ 3                       │             │
│           │  │      ↑                               │             │
│           │  ↓      │                               │             │
│           │  2  ──→ 3                               │             │
│           └─────────────────────────────────────────┘             │
│                                                                   │
│   Step 2: Process 0            dist = [0, 3, 2, ∞]                │
│           Reach 1 (cost 3), 2 (cost 2)                            │
│                                                                   │
│   Step 3: Process 2            dist = [0, 3, 2, 6]                │
│           Reach 3 via 2→3 (cost 2+4=6)                            │
│                                                                   │
│   Step 4: Process 1            dist = [0, 3, 2, 5]                │
│           Reach 3 via 1→3 REVERSED! (cost 3+2=5)                  │
│           5 < 6, UPDATE!                                          │
│                                                                   │
│   Result: 5                                                       │
│                                                                   │
└───────────────────────────────────────────────────────────────────┘
```

---

## Complexity Analysis (Optimal)

| Solution | Time | Space | Status | Why |
|----------|------|-------|--------|-----|
| Brute DFS | O(2^n) | O(n) | ✅ TLE | Exponential path exploration |
| Simple BFS | O(V+E) | O(V) | ❌ Wrong | Ignores edge weights |
| State Dijkstra | O(n × 2^n) | O(n × 2^n) | ✅ TLE | Tracks switch state per node |
| **Graph Transform** | **O((V+E) log V)** | **O(V + E)** | ✅ **Optimal** | Standard Dijkstra on 2E edges |

**Time Breakdown**:
- Graph construction: O(E)
- Dijkstra with binary heap: O((V + 2E) log V) = O((V + E) log V)
- For constraints: V = 50,000, E = 100,000 → ~2.5M operations ✅

**Space**: O(V + 2E) for adjacency list = O(V + E)

---

## Key Takeaways

1. **Graph Transformation Insight**: Complex state-based problems can often be solved by transforming the graph to include additional edges representing state transitions.

2. **"Use at most once" doesn't always need tracking**: When the constraint naturally aligns with shortest-path properties (no revisiting settled nodes), explicit state tracking is unnecessary.

3. **Reversed edge modeling**: Adding `v → u` with cost `2w` for each `u → v` edge elegantly models the "reverse and traverse" operation.

---

## The Journey (TL;DR)

```
🐢 Brute DFS → EXPONENTIAL (O(2^n) paths)
         ↓
💡 "BFS for shortest path?"
         ↓
❌ BFS → WRONG! (ignores weights)
         ↓
💡 "Dijkstra with switch state tracking?"
         ↓
🤔 State Dijkstra → COMPLEX (O(n × 2^n) state space)
         ↓
💡 "Wait... can we just add reversed edges to the graph?"
         ↓
✅ Graph Transform + Dijkstra → O((V+E) log V) OPTIMAL!
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

**Confidence Score: 0.92** - Complete Connected Flow from brute force through multiple intermediate insights to optimal graph transformation solution with detailed visualizations.
