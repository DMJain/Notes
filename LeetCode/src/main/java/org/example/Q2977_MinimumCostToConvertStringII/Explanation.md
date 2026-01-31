# Minimum Cost to Convert String II - Explanation

> **Prerequisites**: 
> - [LeetCode 2976 - Minimum Cost to Convert String I](https://leetcode.com/problems/minimum-cost-to-convert-string-i/) | [Local](../Q2976_MinimumCostToConvertStringI/Explanation.md) (Single-char version with Floyd-Warshall)
> - Trie data structure basics
> - Floyd-Warshall all-pairs shortest path
> - Dynamic Programming (interval/substring DP)
>
> **Related Problems**: 
> - [LeetCode 208 - Implement Trie](https://leetcode.com/problems/implement-trie-prefix-tree/) (Trie fundamentals)
> - [LeetCode 743 - Network Delay Time](https://leetcode.com/problems/network-delay-time/) (Shortest path)
> - [LeetCode 72 - Edit Distance](https://leetcode.com/problems/edit-distance/) (String transformation DP)
> - [LeetCode 139 - Word Break](https://leetcode.com/problems/word-break/) (Similar DP pattern with Trie)

## Problem in Simple Words

Convert `source` string to `target` by replacing **substrings** (not just single characters). You have transformation rules like "bcd"→"cde" with specific costs. Operations must use **disjoint** OR **identical** indices—meaning you can't have two operations that partially overlap.

**Example**: source="abcdefgh", target="acdeeghh", rules: "bcd"→"cde" (cost 1), "fgh"→"thh"→"ghh" (cost 3+5)  
Answer: 9 (replace "bcd" with "cde", then chain "fgh"→"thh"→"ghh")

---

## What Makes This Different from String I?

Before diving in, let's understand why this problem is MUCH harder than String I. In String I, we simply converted character-by-character independently. Here, we're dealing with **variable-length substrings** that can overlap, and we must choose which substrings to transform without creating invalid overlaps.

| Aspect | String I (Q2976) | String II (Q2977) |
|--------|------------------|-------------------|
| Transformation unit | **Single characters** (a→b) | **Variable-length substrings** ("bcd"→"cde") |
| Graph nodes | Fixed 26 nodes (a-z) | Variable nodes (all unique strings) |
| Constraint | Each position independent | Substrings must be **disjoint or identical** |
| Additional technique | Floyd-Warshall only | **Trie** + Floyd-Warshall + **DP** |

Think of it this way: In String I, you have 26 "buckets" (letters a-z) and you precompute how to get from any bucket to any other bucket. In String II, you have potentially hundreds of "buckets" (all unique substrings), and you also need to figure out HOW to cut the string into non-overlapping pieces.

---

## Solution 1: Process Each Position Independently (Greedy) ❌

### The Natural Thought

The first instinct when approaching this problem is to think: "This is just like String I, but with substrings instead of characters! I'll precompute the shortest path between all substring pairs using Floyd-Warshall, then greedily apply transformations at each position."

This seems reasonable because:
1. We know Floyd-Warshall works for finding cheapest transformations in String I
2. We can build a graph where nodes are substrings instead of characters
3. For each position, we pick the best available transformation

### Approach
```java
// Build Floyd-Warshall for all substring pairs
// For each position i, greedily pick the best transformation

for (int i = 0; i < n; i++) {
    // Find any transformation that starts at i
    // Pick the one with minimum cost
    totalCost += bestTransformationAt(i);
}
```

### Why It's Wrong

This approach has a **fatal flaw**: it completely ignores the **non-overlapping constraint**! The problem clearly states that any two operations must either:
1. Cover **disjoint** index ranges (like [1,3] and [5,7])
2. Cover **identical** index ranges (like [1,3] and [1,3] again)

You **cannot** have operations on [1,3] and [3,7] because they share index 3 but aren't identical. The greedy approach blindly picks transformations without checking if they overlap with previously chosen ones.

Why is this constraint even there? Think about it physically: if you transform "bcd" to "ddd", the 'd' at position 3 is now DIFFERENT from the original 'd'. If another operation tries to use positions [3,7], it would be operating on a string that's been partially modified—which the problem forbids.

### Example Where It FAILS ❌
```
Input:
  source = "abcdefgh", target = "addddddd"
  original = ["bcd", "defgh"], changed = ["ddd", "ddddd"], cost = [100, 1578]

Let's trace the greedy approach:

Step 1: At position 1, we can transform "bcd" → "ddd" (cost 100)
        This covers indices [1, 2, 3]
        source becomes: a [d d d] e f g h  (conceptually)
        
Step 2: At position 3, we want to transform "defgh" → "ddddd" (cost 1578)
        This needs indices [3, 4, 5, 6, 7]
        
OVERLAP DETECTED!
  Operation 1 used: [1, 2, 3]
  Operation 2 needs: [3, 4, 5, 6, 7]
  Index 3 is in BOTH! But they're not identical ranges!
  
Greedy returns: 100 + 1578 = 1678 ← WRONG!
Expected: -1 (impossible)

The greedy approach doesn't realize that picking [1,3] makes [3,7] impossible!
```

### Complexity Analysis

| Solution | Time | Space | Correct? | Why? |
|----------|------|-------|----------|------|
| Greedy Position | O(n × m) | O(m²) | ❌ Wrong | Ignores overlap constraint |

> 💭 **Greedy fails because we can't just pick transformations independently—they might overlap! What if we use Dynamic Programming to explicitly track which positions we've already "used", ensuring we only commit to non-overlapping transformations?**

---

## Solution 2: DP with String Hashing ❌

### The Natural Thought

After realizing greedy doesn't work, the natural next step is: "I need to track which positions I've committed to. Dynamic Programming is perfect for this! Let dp[i] represent the minimum cost to convert source[0..i-1] to target[0..i-1]. For each position, I'll try all possible substring transformations that END at this position."

This fixes the overlap problem because DP naturally ensures we don't double-count positions. If I'm at dp[i] and I apply a transformation of length L that goes from i to i+L-1, my next state is dp[i+L], completely skipping the intermediate positions.

### Approach
```java
// Precompute shortest paths between all substrings using HashMap-based Floyd-Warshall
Map<String, Map<String, Long>> shortestPath = new HashMap<>();
// ... build the graph with strings as keys ...

long[] dp = new long[n + 1];
Arrays.fill(dp, Long.MAX_VALUE);
dp[0] = 0;  // Empty prefix costs 0

for (int i = 0; i < n; i++) {
    if (dp[i] == Long.MAX_VALUE) continue;  // Can't reach this state
    
    // Option A: If characters match, take for free
    if (source.charAt(i) == target.charAt(i)) {
        dp[i + 1] = Math.min(dp[i + 1], dp[i]); // Free match
    }
    
    // Option B: Try all possible substring lengths starting at i
    for (int len = 1; len <= n - i; len++) {
        String srcSub = source.substring(i, i + len);  // O(len) operation!
        String tgtSub = target.substring(i, i + len);  // O(len) operation!
        
        if (shortestPath.containsKey(srcSub) && 
            shortestPath.get(srcSub).containsKey(tgtSub)) {
            long pathCost = shortestPath.get(srcSub).get(tgtSub);
            dp[i + len] = Math.min(dp[i + len], dp[i] + pathCost);
        }
    }
}
```

### Why It's Slow

While this approach is **logically correct**, it has a severe performance problem: **string operations are expensive!**

Let's break down the costs:
1. **substring() is O(len)**: In modern Java (9+), creating a substring copies characters, taking O(len) time
2. **HashMap lookup with String key is O(len)**: Computing the hash code of a string is O(len)
3. **We try ALL lengths**: For each of n positions, we try up to n different lengths

This creates a nasty nested loop where the inner operations themselves are O(len), leading to roughly O(n³) behavior.

### Example Where It's SLOW ❌
```
Input:
  source = "aaa...a" (1000 'a's)
  target = "bbb...b" (1000 'b's)
  Many transformation rules covering various lengths (say, 50 rules)

Let's count the operations:

For position i = 0:
  len = 1:   substring("a") → O(1)
  len = 2:   substring("aa") → O(2)
  len = 3:   substring("aaa") → O(3)
  ...
  len = 1000: substring("aaa...") → O(1000)
  
  Total for position 0: 1 + 2 + 3 + ... + 1000 = 1000 × 1001 / 2 = 500,500 ops

For ALL 1000 positions (though not all try full length):
  Average work per position: ~250,000 substring operations
  Total string operations: 1000 × 250,000 = 250,000,000 operations!

Plus HashMap lookups (also O(len) each):
  Another 250,000,000 operations!

Grand Total: ~500,000,000 operations

Time allowed: ~10^8 operations
Result: TLE (Time Limit Exceeded) ⏱️
```

### The Root Cause 🔍

The fundamental issue is that we're treating strings as first-class objects when we should be treating them as **sequences of characters**. Every time we create a substring or hash a string, we're re-reading all those characters. 

What we really need is a data structure that lets us check "does this sequence of characters exist in our transformation rules?" in O(1) time per character, not O(len) time for the whole string.

### Complexity Analysis

| Solution | Time | Space | Correct? | Why? |
|----------|------|-------|----------|------|
| Greedy Position | O(n × m) | O(m²) | ❌ Wrong | Ignores overlap |
| String HashMap DP | O(n² × L) | O(m² × L) | ✅ TLE | substring() and hash are O(len) |

Where L = average string length, m = number of rules

> 💭 **String operations are O(len) per call. What if we could check string membership character-by-character, paying O(1) per character? A Trie data structure does exactly that! We traverse the Trie one character at a time, and at each step we know if we're on a valid path.**

---

## Solution 3: Trie + Floyd-Warshall + DP ✅

### The Connection 🔗

Let's trace the journey that led us here:

1. **Greedy** failed because: It didn't respect the non-overlapping constraint. We can't just pick transformations position-by-position without considering how they interact.

2. **String HashMap DP** was correct but too slow because: Every time we want to check if a substring exists in our transformation rules, we create a new String object (O(len)) and compute its hash (O(len)). With n positions and up to n lengths each, this blows up to O(n² × L).

3. **What we actually need**:
   - A way to respect non-overlapping: ✅ DP handles this
   - A way to precompute shortest paths: ✅ Floyd-Warshall handles this
   - A way to CHECK substring existence in O(1) per character: ❓ We need something new!

That "something new" is a **Trie**. A Trie lets us traverse character-by-character, and at each step we immediately know if we're still on a valid path. Instead of checking "does 'bcd' exist?" all at once, we check 'b'→'c'→'d' one letter at a time.

### The Key Insight 💡

The solution combines THREE techniques, each solving a specific sub-problem:

**Part 1: Trie for String-to-Integer Mapping**
- Insert all strings from original[] and changed[] into a Trie
- Assign each COMPLETE word a unique integer ID (0, 1, 2, ...)
- Now instead of a graph with string nodes, we have a graph with INTEGER nodes
- Benefit: Checking if a substring exists takes O(1) per character, not O(len) total

**Part 2: Floyd-Warshall for Shortest Transformation Paths**
- Build a distance matrix: dist[id1][id2] = min cost to transform string1 to string2
- Initialize direct edges from the transformation rules
- Run Floyd-Warshall to compute shortest paths between ALL pairs
- Benefit: After O(K³) preprocessing, any lookup is O(1)

**Part 3: DP with Simultaneous Trie Traversal**
- dp[i] = minimum cost to convert source[0..i-1] to target[0..i-1]
- At each position i, traverse the Trie from BOTH source and target simultaneously
- When both pointers land on complete words (have valid IDs), we have a potential transformation!
- Benefit: We check ALL possible substring lengths in one traversal, O(maxLen) per position

The magic is in Part 3: Instead of creating substrings and looking them up, we WALK through the Trie character-by-character. We maintain two pointers—one following source, one following target. When BOTH land on nodes with valid IDs at the same length, we've found a valid (source_substring, target_substring) pair!

### The Algorithm

```
Step 1: Build Trie from all original[] and changed[] strings
        ─────────────────────────────────────────────────────
        For each string s in original[] ∪ changed[]:
          - Create path in Trie: root → s[0] → s[1] → ... → s[len-1]
          - At the final node, assign a unique ID if not already assigned
        
        Result: Every transformation string has a unique integer ID

Step 2: Build distance matrix using string IDs
        ─────────────────────────────────────────────────────
        Initialize dist[i][j] = ∞ for all pairs
        Set dist[i][i] = 0 (self-loops)
        
        For each transformation rule (original[k], changed[k], cost[k]):
          - u = ID of original[k]
          - v = ID of changed[k]
          - dist[u][v] = min(dist[u][v], cost[k])  // Keep minimum if duplicates

Step 3: Run Floyd-Warshall on the integer graph
        ─────────────────────────────────────────────────────
        For k from 0 to numNodes-1:     // Try node k as intermediate
          For i from 0 to numNodes-1:   // Source node
            For j from 0 to numNodes-1: // Destination node
              dist[i][j] = min(dist[i][j], dist[i][k] + dist[k][j])
        
        Result: dist[i][j] now holds the SHORTEST path cost from i to j

Step 4: DP with simultaneous Trie traversal
        ─────────────────────────────────────────────────────
        Initialize dp[0] = 0, dp[1..n] = ∞
        
        For i from 0 to n-1:
          If dp[i] == ∞: continue  // Can't reach this state
          
          // Option A: If characters match, take for free
          If source[i] == target[i]:
            dp[i+1] = min(dp[i+1], dp[i])
          
          // Option B: Try all substrings starting at i
          Let p1 = root (source Trie pointer)
          Let p2 = root (target Trie pointer)
          
          For j from i to n-1:
            Move p1 along source[j] in Trie
            Move p2 along target[j] in Trie
            
            If p1 becomes null OR p2 becomes null: break  // No such prefix
            
            If p1.id != -1 AND p2.id != -1:  // Both are complete words!
              If dist[p1.id][p2.id] != ∞:    // Transformation exists
                dp[j+1] = min(dp[j+1], dp[i] + dist[p1.id][p2.id])
        
        Return dp[n] if dp[n] != ∞, else -1
```

### Step-by-Step Walkthrough

Let's trace through Example 2 in complete detail.

**Input**: 
- source = "abcdefgh"
- target = "acdeeghh"
- original = ["bcd", "fgh", "thh"]
- changed = ["cde", "thh", "ghh"]
- cost = [1, 3, 5]

```
═══════════════════════════════════════════════════════════════════════
STEP 1: BUILD TRIE AND ASSIGN IDs
═══════════════════════════════════════════════════════════════════════

We need to insert ALL strings from both original[] and changed[]:
  - "bcd", "fgh", "thh" (from original)
  - "cde", "thh", "ghh" (from changed)
  
Note: "thh" appears in both, but we only assign ONE ID.

Building the Trie:

Insert "bcd":
  root → 'b' → 'c' → 'd' ← Assign ID 0

Insert "cde":
  root → 'c' → 'd' → 'e' ← Assign ID 1
  
Insert "fgh":
  root → 'f' → 'g' → 'h' ← Assign ID 2
  
Insert "thh":
  root → 't' → 'h' → 'h' ← Assign ID 3
  
Insert "ghh":
  root → 'g' → 'h' → 'h' ← Assign ID 4

Final Trie structure:

                        root
                     /  |  |  \  \
                    b   c  f   g   t
                    |   |  |   |   |
                    c   d  g   h   h
                    |   |  |   |   |
                    d   e  h   h   h
                   [0] [1][2] [4] [3]
                   
ID Mapping:
  "bcd" → 0
  "cde" → 1
  "fgh" → 2
  "thh" → 3
  "ghh" → 4

═══════════════════════════════════════════════════════════════════════
STEP 2: INITIALIZE DISTANCE MATRIX FROM TRANSFORMATION RULES
═══════════════════════════════════════════════════════════════════════

We have 5 unique strings, so we need a 5×5 distance matrix.
Initialize all to ∞, except diagonal (self-loops) = 0.

Initial matrix:
              0(bcd) 1(cde) 2(fgh) 3(thh) 4(ghh)
    0(bcd)      0      ∞      ∞      ∞      ∞
    1(cde)      ∞      0      ∞      ∞      ∞
    2(fgh)      ∞      ∞      0      ∞      ∞
    3(thh)      ∞      ∞      ∞      0      ∞
    4(ghh)      ∞      ∞      ∞      ∞      0

Now add edges from transformation rules:

Rule 1: "bcd" → "cde", cost 1
        dist[0][1] = min(∞, 1) = 1

Rule 2: "fgh" → "thh", cost 3
        dist[2][3] = min(∞, 3) = 3
        
Rule 3: "thh" → "ghh", cost 5
        dist[3][4] = min(∞, 5) = 5

After adding direct edges:
              0(bcd) 1(cde) 2(fgh) 3(thh) 4(ghh)
    0(bcd)      0      1      ∞      ∞      ∞     ← bcd→cde = 1
    1(cde)      ∞      0      ∞      ∞      ∞
    2(fgh)      ∞      ∞      0      3      ∞     ← fgh→thh = 3
    3(thh)      ∞      ∞      ∞      0      5     ← thh→ghh = 5
    4(ghh)      ∞      ∞      ∞      ∞      0

═══════════════════════════════════════════════════════════════════════
STEP 3: FLOYD-WARSHALL TO FIND ALL SHORTEST PATHS
═══════════════════════════════════════════════════════════════════════

Floyd-Warshall tries each node k as a potential "middle point" in paths.
For each pair (i, j), it checks: is i→k→j cheaper than the current i→j?

When k = 0 (bcd): No improvements (0 has no outgoing paths that help)
When k = 1 (cde): No improvements (1 has no outgoing paths)
When k = 2 (fgh): No improvements (2→3 doesn't help other pairs)

When k = 3 (thh): 💡 IMPORTANT!
  Check i = 2 (fgh), j = 4 (ghh):
    Current: dist[2][4] = ∞
    Via k=3: dist[2][3] + dist[3][4] = 3 + 5 = 8
    
    dist[2][4] = min(∞, 8) = 8 ← NEW PATH DISCOVERED!
    
  This means: fgh → thh → ghh costs 3 + 5 = 8
  There's no DIRECT fgh→ghh rule, but we found an INDIRECT path!

When k = 4 (ghh): No improvements

Final distance matrix after Floyd-Warshall:
              0(bcd) 1(cde) 2(fgh) 3(thh) 4(ghh)
    0(bcd)      0      1      ∞      ∞      ∞
    1(cde)      ∞      0      ∞      ∞      ∞
    2(fgh)      ∞      ∞      0      3      8     ← fgh→ghh = 8 (via thh!)
    3(thh)      ∞      ∞      ∞      0      5
    4(ghh)      ∞      ∞      ∞      ∞      0

═══════════════════════════════════════════════════════════════════════
STEP 4: DP WITH SIMULTANEOUS TRIE TRAVERSAL
═══════════════════════════════════════════════════════════════════════

source = "abcdefgh"   (indices 0-7)
target = "acdeeghh"   (indices 0-7)

Position:  0  1  2  3  4  5  6  7
source:    a  b  c  d  e  f  g  h
target:    a  c  d  e  e  g  h  h

Initialize: dp = [0, ∞, ∞, ∞, ∞, ∞, ∞, ∞, ∞]
                  ^
                 i=0

────────────────────────────────────────────────────────────────────
POSITION i = 0
────────────────────────────────────────────────────────────────────
source[0] = 'a', target[0] = 'a'

Check 1: Characters match! ✅
  dp[1] = min(∞, dp[0] + 0) = min(∞, 0) = 0
  
Check 2: Try Trie traversal for substrings starting at 0
  j = 0: source[0] = 'a'
         p1 = root.next['a'] = null ← 'a' not in Trie!
         STOP. No substrings starting with 'a' exist in our rules.

After i=0: dp = [0, 0, ∞, ∞, ∞, ∞, ∞, ∞, ∞]

────────────────────────────────────────────────────────────────────
POSITION i = 1
────────────────────────────────────────────────────────────────────
source[1] = 'b', target[1] = 'c'

Check 1: Characters DON'T match! ❌
  Cannot take for free.
  
Check 2: Try Trie traversal for substrings starting at 1
  
  j = 1: source[1] = 'b', target[1] = 'c'
         p1 = root.next['b'] → node exists (path to "bcd")
         p2 = root.next['c'] → node exists (path to "cde")
         p1.id = -1 (not a complete word yet)
         p2.id = -1 (not a complete word yet)
         Continue...
         
  j = 2: source[2] = 'c', target[2] = 'd'
         p1 = p1.next['c'] → node exists
         p2 = p2.next['d'] → node exists
         p1.id = -1, p2.id = -1
         Continue...
         
  j = 3: source[3] = 'd', target[3] = 'e'
         p1 = p1.next['d'] → node exists, this is "bcd"! 
         p2 = p2.next['e'] → node exists, this is "cde"!
         p1.id = 0 ✓ (complete word "bcd")
         p2.id = 1 ✓ (complete word "cde")
         
         Both are complete! Check distance matrix:
         dist[0][1] = 1 (bcd → cde costs 1)
         
         dp[4] = min(∞, dp[1] + 1) = min(∞, 0 + 1) = 1
         
         Continue to check longer substrings...
         
  j = 4: source[4] = 'e', target[4] = 'e'
         p1 = p1.next['e'] = null ← "bcde" not in Trie!
         STOP.

After i=1: dp = [0, 0, ∞, ∞, 1, ∞, ∞, ∞, ∞]
                              ^
                          bcd→cde applied!

────────────────────────────────────────────────────────────────────
POSITIONS i = 2, 3
────────────────────────────────────────────────────────────────────
dp[2] = ∞, dp[3] = ∞

These states are UNREACHABLE! We can't get to position 2 or 3 with finite cost.
Why? Because from position 0, we either:
  - Match 'a' and go to position 1
  - Or there's no transformation rule for substrings starting with 'a'
  
From position 1, we can only use "bcd"→"cde" which jumps to position 4.
There's no way to land on position 2 or 3.

Skip these positions.

────────────────────────────────────────────────────────────────────
POSITION i = 4
────────────────────────────────────────────────────────────────────
source[4] = 'e', target[4] = 'e'

Check 1: Characters match! ✅
  dp[5] = min(∞, dp[4] + 0) = min(∞, 1 + 0) = 1
  
Check 2: Try Trie traversal for substrings starting at 4
  j = 4: source[4] = 'e'
         p1 = root.next['e'] = null ← 'e' not in Trie!
         STOP.

After i=4: dp = [0, 0, ∞, ∞, 1, 1, ∞, ∞, ∞]
                                 ^
                             free match!

────────────────────────────────────────────────────────────────────
POSITION i = 5
────────────────────────────────────────────────────────────────────
source[5] = 'f', target[5] = 'g'

Check 1: Characters DON'T match! ❌
  
Check 2: Try Trie traversal for substrings starting at 5
  
  j = 5: source[5] = 'f', target[5] = 'g'
         p1 = root.next['f'] → node exists (path to "fgh")
         p2 = root.next['g'] → node exists (path to "ghh")
         p1.id = -1, p2.id = -1
         Continue...
         
  j = 6: source[6] = 'g', target[6] = 'h'
         p1 = p1.next['g'] → node exists
         p2 = p2.next['h'] → node exists
         p1.id = -1, p2.id = -1
         Continue...
         
  j = 7: source[7] = 'h', target[7] = 'h'
         p1 = p1.next['h'] → node exists, this is "fgh"!
         p2 = p2.next['h'] → node exists, this is "ghh"!
         p1.id = 2 ✓ (complete word "fgh")
         p2.id = 4 ✓ (complete word "ghh")
         
         Both are complete! Check distance matrix:
         dist[2][4] = 8 (fgh → ghh costs 8, via thh!)
         
         dp[8] = min(∞, dp[5] + 8) = min(∞, 1 + 8) = 9
         
         j = 8 would be out of bounds, STOP.

After i=5: dp = [0, 0, ∞, ∞, 1, 1, ∞, ∞, 9]
                                          ^
                                      fgh→ghh applied!

────────────────────────────────────────────────────────────────────
POSITIONS i = 6, 7
────────────────────────────────────────────────────────────────────
dp[6] = ∞, dp[7] = ∞ → UNREACHABLE, skip.

────────────────────────────────────────────────────────────────────
FINAL ANSWER
────────────────────────────────────────────────────────────────────
dp[8] = 9 ✓

Reconstruction of the optimal path:
  Position 0: 'a' = 'a' → free match
  Position 1-3: "bcd" → "cde" → cost 1
  Position 4: 'e' = 'e' → free match
  Position 5-7: "fgh" → "ghh" → cost 8 (via fgh→thh→ghh)
  
Total: 0 + 1 + 0 + 8 = 9 ✓
```

### Visual Diagram

```
┌─────────────────────────────────────────────────────────────────────┐
│                         TRIE STRUCTURE                               │
├─────────────────────────────────────────────────────────────────────┤
│                                                                      │
│   The Trie maps each transformation string to a unique integer ID.  │
│   This allows us to use integer-based Floyd-Warshall instead of     │
│   expensive string-based operations.                                │
│                                                                      │
│                              root                                    │
│                           /  |  |  \  \                              │
│                          b   c  f   g   t                            │
│                          |   |  |   |   |                            │
│                          c   d  g   h   h                            │
│                          |   |  |   |   |                            │
│                          d   e  h   h   h                            │
│                        [0] [1][2] [4] [3]                            │
│                                                                      │
│   ID Assignments:                                                    │
│     "bcd" → 0    "cde" → 1    "fgh" → 2                             │
│     "thh" → 3    "ghh" → 4                                          │
│                                                                      │
│   Traversal example: To check if "bcd" exists:                      │
│     root → 'b' (exists) → 'c' (exists) → 'd' (exists, ID=0) ✓       │
│     Total: 3 steps, O(1) per step = O(3) total                      │
│                                                                      │
└─────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────┐
│                      STRING TRANSFORMATION GRAPH                     │
├─────────────────────────────────────────────────────────────────────┤
│                                                                      │
│   Direct edges from transformation rules:                           │
│                                                                      │
│   [0]"bcd" ───(cost 1)───→ [1]"cde"                                 │
│                                                                      │
│   [2]"fgh" ───(cost 3)───→ [3]"thh" ───(cost 5)───→ [4]"ghh"        │
│                                                                      │
│   After Floyd-Warshall discovers indirect paths:                    │
│                                                                      │
│   [2]"fgh" ─────────(cost 8)─────────→ [4]"ghh"                     │
│            \                           /                             │
│             \─(3)─→ [3]"thh" ─(5)─→──/                              │
│                                                                      │
│   Key insight: There's NO direct fgh→ghh rule in the input!        │
│   Floyd-Warshall found the path fgh→thh→ghh = 3+5 = 8              │
│                                                                      │
└─────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────┐
│                    DP STATE TRANSITIONS                              │
├─────────────────────────────────────────────────────────────────────┤
│                                                                      │
│   source: a  b  c  d  e  f  g  h                                    │
│   target: a  c  d  e  e  g  h  h                                    │
│   index:  0  1  2  3  4  5  6  7                                    │
│                                                                      │
│   dp[]:  [0, 0, ∞, ∞, 1, 1, ∞, ∞, 9]                                │
│           ↑  ↑        ↑  ↑        ↑                                  │
│           │  │        │  │        │                                  │
│           │  │        │  │        └─ dp[5] + dist[fgh][ghh]         │
│           │  │        │  │           = 1 + 8 = 9                     │
│           │  │        │  │                                           │
│           │  │        │  └─ dp[4] + 0 (char 'e' = 'e')              │
│           │  │        │     = 1 + 0 = 1                              │
│           │  │        │                                              │
│           │  │        └─ dp[1] + dist[bcd][cde]                     │
│           │  │           = 0 + 1 = 1                                 │
│           │  │                                                       │
│           │  └─ dp[0] + 0 (char 'a' = 'a')                          │
│           │     = 0 + 0 = 0                                          │
│           │                                                          │
│           └─ base case                                               │
│                                                                      │
│   Optimal path visualization:                                       │
│                                                                      │
│   source: [a] [b  c  d] [e] [f  g  h]                               │
│   target: [a] [c  d  e] [e] [g  h  h]                               │
│            ↓      ↓       ↓      ↓                                   │
│          FREE  bcd→cde  FREE  fgh→ghh                               │
│           $0     $1      $0     $8                                   │
│                                                                      │
│   Total cost: 0 + 1 + 0 + 8 = 9 ✓                                   │
│                                                                      │
└─────────────────────────────────────────────────────────────────────┘

┌─────────────────────────────────────────────────────────────────────┐
│                  WHY IMPOSSIBLE CASE RETURNS -1                      │
├─────────────────────────────────────────────────────────────────────┤
│                                                                      │
│   Example 3: source = "abcdefgh", target = "addddddd"               │
│              rules: bcd→ddd (100), defgh→ddddd (1578)               │
│                                                                      │
│   source: a  b  c  d  e  f  g  h                                    │
│   target: a  d  d  d  d  d  d  d                                    │
│                                                                      │
│   The problem: These rules MUST overlap at index 3!                 │
│                                                                      │
│   Option A: Use bcd→ddd first                                       │
│             ┌───────┐                                                │
│         a  [b  c  d] e  f  g  h                                     │
│             └───────┘                                                │
│         Covers indices [1, 2, 3]                                    │
│         Now need to change "efgh" → "dddd" at [4,7]?                │
│         But there's NO rule for that! ❌                             │
│                                                                      │
│   Option B: Use defgh→ddddd first                                   │
│                      ┌───────────────┐                               │
│         a  b  c  d  [e  f  g  h]                                    │
│                      └───────────────┘                               │
│         Covers indices [4, 5, 6, 7] ... wait, "defgh" starts at 3!  │
│         So it actually covers [3, 4, 5, 6, 7]                       │
│         Now need bcd→ddd at [1, 2, 3]?                              │
│         But index 3 is ALREADY USED! ❌                              │
│                                                                      │
│   No matter what we try, the rules overlap at index 3.              │
│   DP correctly finds: dp[8] = ∞ → return -1                         │
│                                                                      │
└─────────────────────────────────────────────────────────────────────┘
```

### Key Optimizations in Code

```java
// 1. TRIE NODE with integer ID
class TrieNode {
    TrieNode[] next = new TrieNode[26];
    int id = -1;  // -1 means "not a complete word"
}

// 2. SIMULTANEOUS TRAVERSAL - the key trick
for (int j = i; j < n; j++) {
    int charS = source.charAt(j) - 'a';
    int charT = target.charAt(j) - 'a';
    
    p1 = p1.next[charS];  // Follow source path
    p2 = p2.next[charT];  // Follow target path simultaneously
    
    if (p1 == null || p2 == null) break;  // Early exit if either falls off
    
    // Only update DP when BOTH are complete words
    if (p1.id != -1 && p2.id != -1) {
        // This is O(1) lookup thanks to Floyd-Warshall preprocessing!
        if (dist[p1.id][p2.id] != Long.MAX_VALUE) {
            dp[j + 1] = Math.min(dp[j + 1], dp[i] + dist[p1.id][p2.id]);
        }
    }
}

// 3. OVERFLOW PREVENTION in Floyd-Warshall
if (dist[i][k] == Long.MAX_VALUE) continue;  // Skip before adding
if (dist[k][j] == Long.MAX_VALUE) continue;  // Prevents Long.MAX_VALUE + positive overflow
```

---

## Complexity Analysis (All Solutions)

| Solution | Time | Space | Status | Why |
|----------|------|-------|--------|-----|
| Greedy Position | O(n × m) | O(m²) | ❌ Wrong | Ignores overlap constraint |
| String HashMap DP | O(n² × L) | O(m² × L) | ✅ TLE | substring() is O(len), hash is O(len) |
| **Trie + FW + DP** | **O(n × L + K³)** | **O(K² + n)** | ✅ **Optimal** | Trie gives O(1)/char, FW is O(K³) |

Where:
- n = length of source/target (≤ 1000)
- m = number of transformation rules (≤ 100)
- L = max length of any string in rules (≤ 1000)
- K = number of unique strings (≤ 2m ≤ 200)

**Detailed breakdown of Optimal solution:**
- **Trie insertion**: O(total characters) = O(m × L) = O(100 × 1000) = 100,000 ops
- **Floyd-Warshall**: O(K³) where K ≤ 200 → 200³ = 8,000,000 ops
- **DP with Trie traversal**: O(n × L) = O(1000 × 1000) = 1,000,000 ops (worst case)
- **Total**: ~9,100,000 ops ✓ (well under 10^8 limit)

**Comparison with String HashMap approach:**
```
String HashMap DP: O(n² × L) = O(1000² × 1000) = 1,000,000,000 ops → TLE!
Trie + FW + DP:    O(n × L + K³) ≈ 9,000,000 ops → Passes! (100× faster)
```

---

## Key Takeaways

1. **Trie for String-to-ID Mapping**: When your graph has variable-length strings as nodes (not just single characters), use a Trie to assign unique integer IDs. This converts expensive O(len) string operations into O(1) integer operations.

2. **Floyd-Warshall on Transformed Graph**: Once strings become integers, standard shortest-path algorithms apply cleanly. K unique strings = a K×K distance matrix, which is manageable when K ≤ 200.

3. **Simultaneous Trie Traversal**: The key insight is to traverse the Trie from BOTH source and target at the same time. You follow source[i..j] in one Trie pointer and target[i..j] in another. When both land on complete words, you've found a valid transformation pair.

4. **DP for Non-Overlapping Selection**: The dp[i] = "min cost for prefix [0..i-1]" pattern naturally handles the non-overlapping constraint. When you apply a transformation from i to j, the next state is dp[j+1], completely skipping intermediate positions. There's no way to "go back" and overlap.

5. **Chained Transformations via Floyd-Warshall**: Even if there's no direct "fgh"→"ghh" rule, Floyd-Warshall discovers the indirect path "fgh"→"thh"→"ghh" with combined cost. This is crucial for problems where intermediate transforms are cheaper.

6. **Early Exit in Trie Traversal**: If either the source or target path falls off the Trie (reaches null), we can immediately stop trying longer substrings. There's no point checking "bcde" if "bcd" already led to a dead end.

---

## The Journey (TL;DR)

```
🐢 Greedy Position → WRONG (ignores overlap constraint)
          ↓
💭 "Overlapping substrings aren't allowed. What if we use DP to track committed positions?"
          ↓
❌ String HashMap DP → TLE (substring ops are O(len), 500M ops for n=1000)
          ↓
💭 "String operations are slow. What if we traverse character-by-character using a Trie?"
          ↓
✅ Trie + Floyd-Warshall + DP → O(n×L + K³) ≈ 9M ops, OPTIMAL!
```
