# Freedom Trail - Explanation

## Problem in Simple Words
You have a **circular ring** with letters. You need to spell a word.

**How it works:**
1. Ring starts with first character at top (12:00 position)
2. To spell each letter: rotate ring left OR right to bring that letter to top, then press button
3. Each rotation = 1 step, each button press = 1 step

**Goal:** Find **minimum steps** to spell the entire word.

---

## Solution 1: Brute Force ❌ (Too Slow)

### Approach
Try every possible path. Pick the minimum.

### Why It's Bad
- If a letter appears 5 times and word has 20 letters → 5²⁰ paths
- That's **95 TRILLION** paths to check!
- Time Limit Exceeded guaranteed

> 💭 **Exploring all paths is exponential. But we don't need to check EVERY path... what if we just picked the closest occurrence of each letter?**

---

## Solution 2: Greedy ❌ (Wrong Answer)

### The Natural Thought
"Always go to the NEAREST occurrence of the next letter!"

### Example Where Greedy WORKS ✅

**ring = `"godding"`, key = `"gd"`**

```
The ring looks like this (circular):

         [g]  ← You start here (position 0)
        /   \
      g       o
      |       |
      n       d
       \     /
        i - d

Positions: g(0), o(1), d(2), d(3), i(4), n(5), g(6)
```

**Greedy solves it:**
```
Step 1: Spell 'g'
  → 'g' is at position 0 and 6
  → Nearest from 0? Position 0 itself! Distance = 0
  → Cost: 0 rotations + 1 press = 1
  → Now at position 0

Step 2: Spell 'd'  
  → 'd' is at position 2 and 3
  → From position 0:
      • To position 2: go right 2 steps
      • To position 3: go right 3 OR left 4 steps → min = 3
  → Nearest is position 2! Distance = 2
  → Cost: 2 rotations + 1 press = 3
  → Now at position 2

TOTAL = 1 + 3 = 4 ✅
```

Greedy works here! But wait...

---

### Example Where Greedy FAILS ❌

**ring = `"abccbaxbe"`, key = `"abx"`**

```
The ring (length 9, circular):

        [a] ← Start here (position 0)
       /   \
      e     b
      |     |
      b     c
      |     |
      x     c
       \   /
        a-b

Positions: a(0), b(1), c(2), c(3), b(4), a(5), x(6), b(7), e(8)

Where each letter appears:
  'a' → positions [0, 5]
  'b' → positions [1, 4, 7]  ← THREE options!
  'x' → position [6]
```

**Greedy approach:**
```
Start at position 0

Step 1: Spell 'a'
  → 'a' at positions 0 and 5
  → From 0: distance to 0 = 0, distance to 5 = min(5, 4) = 4
  → GREEDY PICKS: position 0 (nearest!) ✅
  → Cost: 0 + 1 = 1
  → Now at position 0

Step 2: Spell 'b'
  → 'b' at positions 1, 4, 7
  → From position 0:
      • To 1: distance = 1         ← NEAREST!
      • To 4: distance = 4
      • To 7: distance = min(7, 2) = 2
  → GREEDY PICKS: position 1 (nearest!)
  → Cost: 1 + 1 = 2
  → Now at position 1

Step 3: Spell 'x'
  → 'x' only at position 6
  → From position 1:
      • To 6: distance = min(5, 4) = 4
  → Cost: 4 + 1 = 5
  → Now at position 6

GREEDY TOTAL = 1 + 2 + 5 = 8
```

**But is 8 the best we can do?** 🤔

What if instead of b(1), we picked b(7)?

```
Let's try: a(0) → b(7) → x(6)

Step 1: Spell 'a' at position 0
  → Cost: 0 + 1 = 1
  → At position 0

Step 2: Spell 'b' at position 7 (NOT nearest!)
  → From 0 to 7: distance = min(7, 2) = 2
  → Cost: 2 + 1 = 3
  → At position 7

Step 3: Spell 'x' at position 6
  → From 7 to 6: distance = 1  ← SO CLOSE!
  → Cost: 1 + 1 = 2

BETTER TOTAL = 1 + 3 + 2 = 6 ✅
```

**GREEDY GAVE 8, BUT OPTIMAL IS 6!**

### Why Did Greedy Fail? 🤯

```
Greedy's mistake:

  Position:  0 ---→ 1 --------→ 6
             a      b           x
             |  +1  |    +4     |
             └──────┴───────────┘  = 1 + 2 + 5 = 8

Optimal path:

  Position:  0 -------→ 7 → 6
             a          b   x
             |    +2    |+1 |
             └──────────┴───┘  = 1 + 3 + 2 = 6
```

**The lesson:** Going to the nearest 'b' (position 1) put us FAR from 'x'.
Going to a slightly farther 'b' (position 7) put us RIGHT NEXT to 'x'!

**Greedy only looks at the CURRENT step. It can't see the FUTURE!**

> 💭 **Greedy is shortsighted — it makes locally optimal choices that are globally suboptimal. We need to consider ALL paths and pick the best. But that's exponential... unless we use memoization!**

---

## Solution 3: DP with Memoization ✅ (Optimal)

### The Connection 🔗
Let's trace our thinking:
- **Brute Force** was slow because: exponential paths to explore
- **Greedy** was wrong because: ignores future costs, makes locally optimal but globally suboptimal choices
- **What we need**: explore all paths + remember results to avoid redundant work → **DP!**

### The Key Insight 💡
Instead of just picking the nearest (greedy), we **try ALL options** and pick the best.

But wait — isn't that brute force? 🤔

**The trick:** We use **memoization** (caching). Many paths lead to the same position, so we save the answer and reuse it!

### Why DP Solves Greedy's Problem

```
Greedy:                    DP:
   ↓                         ↓
"Pick nearest"         "Try ALL options"
   ↓                         ↓
Move on                For each option:
                         → Solve rest of problem
                         → Remember the best
                         
Greedy is BLIND         DP SEES EVERYTHING
to future costs!        by exploring all paths!
```

### How DP Solves the Failing Example

**ring = `"abccbaxbe"`, key = `"abx"`**

```
Positions reminder:
  'a' → [0, 5]
  'b' → [1, 4, 7]
  'x' → [6]
```

**DP explores EVERY path like a tree:**

```
                            START
                           pos = 0
                        spell "abx"
                              │
           ┌──────────────────┴──────────────────┐
           ▼                                     ▼
      a at pos 0                            a at pos 5
      cost = 0+1 = 1                        cost = 4+1 = 5
           │                                     │
     ┌─────┼─────┐                         (similar branches)
     ▼     ▼     ▼                               
   b(1)   b(4)   b(7)                      
   +1+1   +4+1   +2+1                      
    =3     =6     =4                       
     │     │      │                        
     ▼     ▼      ▼                        
   x(6)   x(6)   x(6)                      
   +4+1   +2+1   +1+1                      
    =5     =3     =2                       
     │     │      │                        
     ▼     ▼      ▼                        
  DONE   DONE   DONE                       
```

**Now DP adds up each complete path:**

```
Path 1: a(0) → b(1) → x(6)
         a(0): cost = 1
         b(1): dist from 0 to 1 = 1, cost = 1+1 = 2
         x(6): dist from 1 to 6 = 4, cost = 4+1 = 5
         TOTAL = 1 + 2 + 5 = 8

Path 2: a(0) → b(4) → x(6)
         a(0): cost = 1
         b(4): dist from 0 to 4 = 4, cost = 4+1 = 5
         x(6): dist from 4 to 6 = 2, cost = 2+1 = 3
         TOTAL = 1 + 5 + 3 = 9

Path 3: a(0) → b(7) → x(6)
         a(0): cost = 1
         b(7): dist from 0 to 7 = 2, cost = 2+1 = 3
         x(6): dist from 7 to 6 = 1, cost = 1+1 = 2
         TOTAL = 1 + 3 + 2 = 6 ✅ BEST!
```

**DP picks the minimum: 6**

### Visual Step-by-Step with Pointers

```
Ring: a(0)-b(1)-c(2)-c(3)-b(4)-a(5)-x(6)-b(7)-e(8)
      ↑
    START

═══════════════════════════════════════════════════════
STEP 1: Spell 'a' (DP tries both positions)
═══════════════════════════════════════════════════════

Option A: Stay at a(0)          Option B: Go to a(5)
          ↓                               ↓
    cost = 1                        cost = 5
    
DP picks Option A (will explore both, but A leads to best)

Ring: a(0)-b(1)-c(2)-c(3)-b(4)-a(5)-x(6)-b(7)-e(8)
      ★
    (pos=0)

═══════════════════════════════════════════════════════
STEP 2: Spell 'b' (DP tries ALL THREE positions)
═══════════════════════════════════════════════════════

From position 0:

Option A: Go to b(1)    → dist = 1 → cost = 2 → total so far = 3
Option B: Go to b(4)    → dist = 4 → cost = 5 → total so far = 6
Option C: Go to b(7)    → dist = 2 → cost = 3 → total so far = 4

Greedy would pick A (nearest).
DP tries all, continues to step 3 for each...

═══════════════════════════════════════════════════════
STEP 3: Spell 'x' (only one position: 6)
═══════════════════════════════════════════════════════

From b(1): to x(6) = dist 4 → cost = 5 → TOTAL = 3+5 = 8
From b(4): to x(6) = dist 2 → cost = 3 → TOTAL = 6+3 = 9
From b(7): to x(6) = dist 1 → cost = 2 → TOTAL = 4+2 = 6 ✅

═══════════════════════════════════════════════════════
DP RESULT: Path a(0)→b(7)→x(6) with cost 6
═══════════════════════════════════════════════════════

Final path on ring:

a(0)-b(1)-c(2)-c(3)-b(4)-a(5)-x(6)-b(7)-e(8)
 ★─────────────────────────────→★──→★
start                           b    x
         (skip b(1), go around to b(7)!)
```

### What is Memoization?

For longer keys, paths MERGE to same positions. We save work:

```
Example: key = "abxb"

Multiple paths might reach x(6), then need 'b' next.
Instead of recalculating "best way to spell 'b' from position 6"
multiple times, we calculate ONCE and cache it!

dp[3][6] = "min cost to spell key[3:] from position 6"
         = calculated once, reused everywhere!
```

---

## Complexity Analysis

| Solution | Time | Space | Correct? | Why? |
|----------|------|-------|----------|------|
| Brute Force | O(m^k) | O(k) | ✅ But TLE | Explores all paths |
| Greedy | O(k × r) | O(r) | ❌ Wrong | Ignores future costs |
| **DP + Memo** | O(k × r²) | O(k × r) | ✅ **Optimal** | Explores + caches |

k = key length, r = ring length, m = max occurrences of any char

---

## Key Takeaways

1. **Greedy = shortsighted** — picks nearest NOW, ignores FUTURE
2. **DP = explores all paths** — finds the global minimum
3. **Memoization = saves work** — same position? Reuse the answer!
4. **Circular distance** = min(|a-b|, n-|a-b|) where n = ring length

---

## The Journey (TL;DR)

```
🐢 Brute Force: Try all paths → TOO SLOW (exponential)
         ↓
💡 "Just pick the nearest letter each time?"
         ↓
🎯 Greedy: Nearest now → WRONG (ignores future)
         ↓
💡 "We need to see ALL paths but avoid redundant work..."
         ↓
✅ DP + Memoization: Explore all + cache → OPTIMAL
```
