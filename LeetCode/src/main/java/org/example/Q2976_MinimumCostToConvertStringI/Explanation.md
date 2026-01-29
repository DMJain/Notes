# Minimum Cost to Convert String I - Explanation

> **Prerequisites**: 
> - Weighted directed graphs
> - All-pairs shortest path concept
> - Floyd-Warshall algorithm
>
> **Related Problems**: 
> - [LeetCode 743 - Network Delay Time](https://leetcode.com/problems/network-delay-time/) (Single-source shortest path with Dijkstra)
> - [LeetCode 787 - Cheapest Flights Within K Stops](https://leetcode.com/problems/cheapest-flights-within-k-stops/) (Modified shortest path)
> - [LeetCode 399 - Evaluate Division](https://leetcode.com/problems/evaluate-division/) (Graph-based variable relationships)
> - [LeetCode 2959 - Minimum Cost to Convert String II](https://leetcode.com/problems/minimum-cost-to-convert-string-ii/) (Follow-up with substrings)

## Problem in Simple Words

Convert string `source` to `target` character by character. You have transformation rules like "change 'a' to 'c' costs 1". Find the minimum total cost, considering you can chain transformations (a→c→b).

**Example**: source="aaaa", target="bbbb", rules: a→c (cost 1), c→b (cost 2)  
Answer: 12 (each 'a' becomes 'b' via a→c→b costing 3, total 3×4=12)

---

## Solution 1: Direct Edges Only ❌

### The Natural Thought
"Let me build a map of direct transformations. For each position, look up if a direct conversion exists."

### Approach
```java
Map<String, Integer> directCost = new HashMap<>();
for (int i = 0; i < original.length; i++) {
    String key = original[i] + "->" + changed[i];
    directCost.put(key, Math.min(directCost.getOrDefault(key, INF), cost[i]));
}

long totalCost = 0;
for (int i = 0; i < n; i++) {
    if (source.charAt(i) != target.charAt(i)) {
        String key = source.charAt(i) + "->" + target.charAt(i);
        if (!directCost.containsKey(key)) return -1;  // ← BUG!
        totalCost += directCost.get(key);
    }
}
return totalCost;
```

### Why It's Bad
This approach only considers direct edges between characters and completely misses multi-hop paths where you chain multiple transformations.

### Example Where It FAILS ❌
```
Input: 
  source = "aaaa", target = "bbbb"
  original = ['a', 'c'], changed = ['c', 'b'], cost = [1, 2]

Transformation graph:
  a ──(1)──→ c ──(2)──→ b

Looking for: a→b direct edge
Result: NOT FOUND!

Direct Edges returns: -1 ← WRONG!
Expected: 12

WHY WRONG: The path a→c→b exists with cost 1+2=3
           Total for 4 chars: 3 × 4 = 12 ✅
```

### Complexity Analysis

| Solution | Time | Space | Correct? | Why? |
|----------|------|-------|----------|------|
| Direct Edges | O(n + E) | O(E) | ❌ Wrong | Misses multi-hop paths |

> 💭 **Direct edges miss chained paths like a→c→b. What if we explore all possible paths using graph traversal?**

---

## Solution 2: Per-Character Graph Search ❌

### The Natural Thought
"OK, direct edges aren't enough. Let me build a proper graph and run Dijkstra/BFS for each character pair that needs conversion."

### Approach
```java
// Build graph: 26 nodes (a-z), edges from transformation rules
List<int[]>[] graph = new ArrayList[26];
for (int i = 0; i < 26; i++) graph[i] = new ArrayList<>();

for (int i = 0; i < original.length; i++) {
    int u = original[i] - 'a';
    int v = changed[i] - 'a';
    graph[u].add(new int[]{v, cost[i]});
}

// For each position, run Dijkstra to find shortest path
long totalCost = 0;
for (int i = 0; i < n; i++) {
    if (source.charAt(i) != target.charAt(i)) {
        int from = source.charAt(i) - 'a';
        int to = target.charAt(i) - 'a';
        long pathCost = dijkstra(graph, from, to);  // Run Dijkstra!
        if (pathCost == INF) return -1;
        totalCost += pathCost;
    }
}
return totalCost;
```

### Why It's Slow
This approach is **CORRECT** but extremely inefficient! We run Dijkstra for EVERY character in the string, even though the same (source_char, target_char) pairs repeat many times.

### Example Where It's SLOW ❌
```
Input:
  source = "aaaaaa...a" (100,000 'a's)
  target = "bbbbbb...b" (100,000 'b's)

Each character needs: Dijkstra from 'a' to 'b'
Dijkstra on 26 nodes: O(26² × log 26) ≈ O(26 × 26 × 5) = 3,380 operations

Total Dijkstra calls: 100,000
Total operations: 100,000 × 3,380 = 338,000,000 operations!

Result: TLE (Time Limit Exceeded)
```

### Complexity Analysis

| Solution | Time | Space | Correct? | Why? |
|----------|------|-------|----------|------|
| Direct Edges | O(n + E) | O(E) | ❌ Wrong | Misses paths |
| Per-char Dijkstra | O(n × 26² log 26) | O(26²) | ✅ TLE | Repeated graph searches |

> 💭 **We're running Dijkstra for 'a'→'b' 100,000 times when the answer is always the same! What if we precompute the minimum cost between ALL 26×26 character pairs just once?**

---

## Solution 3: Floyd-Warshall All-Pairs Shortest Path ✅

### The Connection 🔗
Let's trace our thinking:
- **Direct Edges** failed because: only checked direct edges, missed multi-hop paths like a→c→b
- **Per-char Dijkstra** was slow because: ran Dijkstra 100,000 times for the same repeated pairs
- **What we need**: Precompute the shortest path between ALL character pairs once → **Floyd-Warshall!**

### The Key Insight 💡

The alphabet has only **26 lowercase letters = 26 nodes** in our graph!

Floyd-Warshall algorithm computes the shortest path between **every pair** of nodes in O(V³) time.

For V = 26:
- Preprocessing: O(26³) = **17,576 operations** (effectively CONSTANT!)
- Each lookup: O(1)
- Total for n characters: O(26³ + n) ≈ **O(n)**

This is a massive improvement over O(n × 26²) from the per-character approach!

### The Algorithm

```
Step 1: Initialize distance matrix
        dist[i][j] = ∞ for all pairs
        dist[i][i] = 0 for same character

Step 2: Add direct edges from transformation rules
        For each rule (original[i], changed[i], cost[i]):
          u = original[i] - 'a'
          v = changed[i] - 'a'
          dist[u][v] = min(dist[u][v], cost[i])  // Keep minimum if duplicates

Step 3: Floyd-Warshall - try each intermediate node
        For k = 0 to 25:  // Try using node k as intermediate
          For i = 0 to 25:  // For each source
            For j = 0 to 25:  // For each destination
              dist[i][j] = min(dist[i][j], dist[i][k] + dist[k][j])

Step 4: Calculate total cost
        For each position i:
          If source[i] != target[i]:
            If dist[source[i]][target[i]] == ∞: return -1
            totalCost += dist[source[i]][target[i]]
        Return totalCost
```

### Step-by-Step Walkthrough

**Input**: source="abcd", target="acbe"  
original=['a','b','c','c','e','d'], changed=['b','c','b','e','b','e'], cost=[2,5,5,1,2,20]

```
Step 1: Build transformation graph

  Edges from input:
    a ──(2)──→ b    (original[0]='a', changed[0]='b', cost[0]=2)
    b ──(5)──→ c    (original[1]='b', changed[1]='c', cost[1]=5)
    c ──(5)──→ b    (original[2]='c', changed[2]='b', cost[2]=5)
    c ──(1)──→ e    (original[3]='c', changed[3]='e', cost[3]=1)
    e ──(2)──→ b    (original[4]='e', changed[4]='b', cost[4]=2)
    d ──(20)─→ e    (original[5]='d', changed[5]='e', cost[5]=20)

Step 2: Initial distance matrix (relevant chars only)

          a    b    c    d    e
      a   0    2    ∞    ∞    ∞
      b   ∞    0    5    ∞    ∞
      c   ∞    5    0    ∞    1
      d   ∞    ∞    ∞    0   20
      e   ∞    2    ∞    ∞    0

Step 3: Floyd-Warshall (k = intermediate node)

  k = 'e' (trying to go through 'e'):
    Check c→e→b: dist[c][b] = min(5, dist[c][e] + dist[e][b]) 
                            = min(5, 1 + 2) = min(5, 3) = 3 ✓ IMPROVED!
    
    Check d→e→b: dist[d][b] = min(∞, dist[d][e] + dist[e][b])
                            = min(∞, 20 + 2) = 22 ✓ NEW PATH!

  After all intermediate nodes:

          a    b    c    d    e
      a   0    2    7    ∞    8    (a→b=2, a→b→c=7, a→b→c→e=8)
      b   ∞    0    5    ∞    6    (b→c=5, b→c→e=6)
      c   ∞    3    0    ∞    1    (c→e→b=3 ← SHORTER than c→b=5!)
      d   ∞   22   ∞    0   20    (d→e→b=22, d→e=20)
      e   ∞    2    7    ∞    0    (e→b=2, e→b→c=7)

Step 4: Calculate total cost for "abcd" → "acbe"

  Position 0: 'a' → 'a' = SAME, cost = 0
  Position 1: 'b' → 'c' = dist[b][c] = 5
  Position 2: 'c' → 'b' = dist[c][b] = 3  (via c→e→b, not direct c→b=5!)
  Position 3: 'd' → 'e' = dist[d][e] = 20

  Total = 0 + 5 + 3 + 20 = 28 ✓
```

### Visual Diagram

```
┌─────────────────────────────────────────────────────────────────────┐
│                 CHARACTER TRANSFORMATION GRAPH                       │
├─────────────────────────────────────────────────────────────────────┤
│                                                                      │
│           ┌──────(2)──────→ b ──────(5)──────→ c                    │
│           │                 ↑                  │                     │
│           a                (2)                (1)                    │
│                             │                  ↓                     │
│           d ──────(20)────→ e ←───────────────┘                     │
│                                                                      │
│   Note: c→b has TWO paths:                                          │
│     - Direct: c ──(5)──→ b                                          │
│     - Via e:  c ──(1)──→ e ──(2)──→ b = 3 ← CHEAPER!                │
│                                                                      │
└─────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────┐
│                    FLOYD-WARSHALL IN ACTION                          │
├─────────────────────────────────────────────────────────────────────┤
│                                                                      │
│   Question: What's the cheapest way from 'c' to 'b'?                │
│                                                                      │
│   Direct edge: c ──(5)──→ b    Cost = 5                             │
│                                                                      │
│   When k = 'e' (trying 'e' as intermediate):                        │
│                                                                      │
│           c ──(1)──→ e ──(2)──→ b                                   │
│                                                                      │
│           dist[c][b] = min(5, dist[c][e] + dist[e][b])              │
│                      = min(5, 1 + 2)                                 │
│                      = min(5, 3)                                     │
│                      = 3  ✓ BETTER!                                  │
│                                                                      │
│   Floyd-Warshall tries ALL 26 possible intermediate nodes!          │
│   Finds the globally optimal path automatically.                    │
│                                                                      │
└─────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────┐
│                    FINAL DISTANCE MATRIX                             │
├─────────────────────────────────────────────────────────────────────┤
│                                                                      │
│   After Floyd-Warshall completes:                                   │
│                                                                      │
│   dist[a][b] = 2      (direct)                                      │
│   dist[b][c] = 5      (direct)                                      │
│   dist[c][b] = 3      (via e: c→e→b = 1+2)                          │
│   dist[c][e] = 1      (direct)                                      │
│   dist[d][e] = 20     (direct)                                      │
│   dist[e][b] = 2      (direct)                                      │
│                                                                      │
│   Now each lookup is O(1)!                                          │
│                                                                      │
│   "abcd" → "acbe":                                                  │
│   [a→a: 0] + [b→c: 5] + [c→b: 3] + [d→e: 20] = 28                  │
│                                                                      │
└─────────────────────────────────────────────────────────────────────┘
```

---

## Complexity Analysis (All Solutions)

| Solution | Time | Space | Status | Why |
|----------|------|-------|--------|-----|
| Direct Edges Only | O(n + E) | O(E) | ❌ Wrong | Misses multi-hop paths |
| Per-char Dijkstra | O(n × 26² log 26) | O(26²) | ✅ TLE | 338M ops for n=100K |
| **Floyd-Warshall** | **O(26³ + n)** | **O(26²)** | ✅ **Optimal** | 17K precompute + n lookups |

**Why Floyd-Warshall wins:**
- Precomputation: 26³ = 17,576 operations (constant!)
- Per-character lookup: O(1)
- Total: O(17,576 + n) ≈ O(n)

For n = 100,000:
- Per-char Dijkstra: 338,000,000 operations
- Floyd-Warshall: 117,576 operations
- Speedup: **~2,800× faster!**

---

## Key Takeaways

1. **Small Alphabet = Graph Opportunity**: When the "nodes" are limited (26 letters), all-pairs shortest path becomes feasible with constant preprocessing.

2. **Floyd-Warshall for Dense Small Graphs**: When V is small (≤100-200), O(V³) preprocessing beats running Dijkstra V times or on-demand.

3. **Precompute vs Repeat**: When the same queries repeat (many 'a'→'b' conversions), precompute ALL answers once and lookup in O(1).

4. **INF Handling**: Use `Long.MAX_VALUE / 2` to prevent overflow when adding distances.

---

## The Journey (TL;DR)

```
🐢 Direct Edges Only → WRONG (misses a→c→b paths)
          ↓
💭 "What if we explore paths using graph traversal?"
          ↓
❌ Per-char Dijkstra → TLE (338M ops for n=100K)
          ↓
💭 "Same pairs repeat. What if we precompute ALL 26×26 pairs once?"
          ↓
✅ Floyd-Warshall → O(26³ + n) = O(n) OPTIMAL!
```

