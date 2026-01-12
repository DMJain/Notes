# Beautiful Arrangement - Explanation

## Problem in Simple Words
You have numbers 1 to n. Arrange them so that at **each position i**:
- Either the number is divisible by i, OR
- i is divisible by the number

Count how many such arrangements exist.

**Example**: n = 2
- `[1, 2]`: 1%1=0 ✓, 2%2=0 ✓ → Valid!
- `[2, 1]`: 2%1=0 ✓, 2%1=0 ✓ → Valid!
- **Answer: 2**

---

## Solution 1: Brute Force (Generate All Permutations) ❌ (Too Slow)

### Approach
Generate all n! permutations, check each one.

```java
// Generate all permutations of [1, 2, ..., n]
// For each permutation, check if it's "beautiful"
// Count valid ones
```

### Why It's Bad
- n = 15 → 15! = 1,307,674,368,000 permutations!
- Most permutations are invalid, but we still generate them all
- **Massive waste!**

> 💭 **Generating ALL permutations then checking is wasteful. Most are invalid! What if we checked validity AS we build the permutation? Stop early when something can't work.**

---

## Solution 2: Greedy (Place Largest/Smallest First) ❌ (Wrong)

### The Natural Thought
"Maybe I should place numbers strategically? Like largest first, or numbers with most divisibility options first?"

### Why It Fails
- There's no greedy strategy that works
- The validity of placing a number depends on ALL other placements
- We MUST explore all possibilities

> 💭 **Greedy doesn't work because placing one number affects what positions remain for others. We need to try different placements and BACKTRACK when we hit dead ends.**

---

## Solution 3: Backtracking ✅ (Optimal)

### The Connection 🔗
Let's trace our thinking:
- **Brute Force** was slow because: generates all n! permutations, most invalid
- **Greedy** doesn't work because: placements depend on each other
- **What we need**: build incrementally + prune invalid branches early → **Backtracking!**

### The Key Insight 💡
Instead of generating all permutations then checking, we:
1. **Build the permutation one number at a time**
2. **Prune early**: If number `val` can't go at position `i`, don't even try!
3. **Backtrack**: Try different positions for each number

### The Algorithm

```
dfs(val):
    if val > n:
        Found valid arrangement! Count it.
        return
    
    for each position i from 1 to n:
        if position i is empty AND (val%i==0 OR i%val==0):
            place val at position i
            dfs(val + 1)
            remove val from position i (backtrack)
```

### Why Backtracking is Better

```
Brute Force:                Backtracking:
     ↓                           ↓
Generate ALL n!           Build incrementally
permutations              Prune invalid branches EARLY
Check each one            Only explore valid paths

n=15: 1.3 trillion       n=15: ~thousands of paths
```

---

## Step-by-Step Walkthrough

**n = 3**

We need to place values 1, 2, 3 at positions 1, 2, 3.

```
Position:    1    2    3
             ↓    ↓    ↓
           [   ][   ][   ]
```

---

### Place val = 1

```
Try position 1: 1%1=0 ✓ or 1%1=0 ✓ → VALID!
Try position 2: 1%2≠0, but 2%1=0 ✓ → VALID!
Try position 3: 1%3≠0, but 3%1=0 ✓ → VALID!

1 can go at ANY position! Let's try each:
```

---

### Branch 1: Place 1 at position 1

```
nums = [_, 1, _, _]  (1-indexed)
       pos: 1  2  3
```

#### Now place val = 2

```
Try position 1: OCCUPIED
Try position 2: 2%2=0 ✓ → VALID!
Try position 3: 2%3≠0, 3%2≠0 → INVALID ❌
```

Place 2 at position 2:
```
nums = [_, 1, 2, _]
```

#### Now place val = 3

```
Try position 1: OCCUPIED
Try position 2: OCCUPIED
Try position 3: 3%3=0 ✓ → VALID!
```

Place 3 at position 3:
```
nums = [_, 1, 2, 3]

val > n? YES! 
result++ → result = 1

Arrangement found: [1, 2, 3] ✅
```

Backtrack... (try other placements for val=3, but pos 3 was the only option)
Backtrack... (try other placements for val=2, but pos 2 was the only option)
Backtrack... (try other placements for val=1)

---

### Branch 2: Place 1 at position 2

```
nums = [_, _, 1, _]
```

#### Place val = 2

```
Try position 1: 2%1=0 ✓ → VALID!
Try position 2: OCCUPIED
Try position 3: 2%3≠0, 3%2≠0 → INVALID ❌
```

Place 2 at position 1:
```
nums = [_, 2, 1, _]
```

#### Place val = 3

```
Try position 1: OCCUPIED
Try position 2: OCCUPIED
Try position 3: 3%3=0 ✓ → VALID!
```

```
nums = [_, 2, 1, 3]

result++ → result = 2

Arrangement found: [2, 1, 3] ✅
```

---

### Branch 3: Place 1 at position 3

```
nums = [_, _, _, 1]
```

#### Place val = 2

```
Try position 1: 2%1=0 ✓ → VALID!
Try position 2: 2%2=0 ✓ → VALID!
Try position 3: OCCUPIED
```

##### Sub-branch 3a: 2 at position 1
```
nums = [_, 2, _, 1]
```

Place val = 3:
```
Try position 1: OCCUPIED
Try position 2: 3%2≠0, 2%3≠0 → INVALID ❌
Try position 3: OCCUPIED

NO valid position for 3! 
This branch FAILS. Backtrack.
```

##### Sub-branch 3b: 2 at position 2
```
nums = [_, _, 2, 1]
```

Place val = 3:
```
Try position 1: 3%1=0 ✓ → VALID!
Try position 2: OCCUPIED
Try position 3: OCCUPIED
```

```
nums = [_, 3, 2, 1]

result++ → result = 3

Arrangement found: [3, 2, 1] ✅
```

---

### FINAL RESULT: 3

```
All beautiful arrangements for n=3:
1. [1, 2, 3]
2. [2, 1, 3]
3. [3, 2, 1]
```

---

## Visual Backtracking Tree

```
                          START (place val=1)
                    ┌─────────┼─────────┐
                    ↓         ↓         ↓
               pos=1       pos=2      pos=3
              [1,_,_]     [_,1,_]    [_,_,1]
                 |           |          |
            place val=2  place val=2  place val=2
                 |           |       ┌─────┐
               pos=2       pos=1   pos=1  pos=2
              [1,2,_]     [2,1,_]  [2,_,1] [_,2,1]
                 |           |        |       |
            place val=3  place val=3  |    place val=3
                 |           |        |       |
               pos=3       pos=3   FAIL!    pos=1
              [1,2,3]✓    [2,1,3]✓        [3,2,1]✓
```

---

## Why This Solution is Efficient

```
The divisibility check prunes MANY branches:

For n=3, position 3 requires:
  - val%3=0 → only val=3 works, OR
  - 3%val=0 → val=1 or val=3 works
  
So position 3 can only hold 1 or 3!

This pruning eliminates most invalid permutations early.
```

---

## Complexity Analysis

| Solution | Time | Space | Correct? | Why? |
|----------|------|-------|----------|------|
| Brute Force (all perms) | O(n! × n) | O(n) | ✅ But TLE | Generate all, check all |
| Greedy | - | - | ❌ Wrong | Placements interdependent |
| **Backtracking** | O(k) where k << n! | O(n) | ✅ **Optimal** | Early pruning |

*k = number of valid arrangements (much smaller than n! due to pruning)*

---

## Key Takeaways

1. **Backtracking = build incrementally** with early pruning
2. **Don't generate then validate** — validate AS you build
3. **Small n (≤15)** is a hint for backtracking/bitmask DP
4. **Divisibility condition** creates sparse valid positions → efficient pruning
5. **nums[i] = 0** means position i is empty → simple "used" tracking

---

## The Journey (TL;DR)

```
🐢 Brute Force: Generate all n! permutations → TOO SLOW
         ↓
💡 "Can we be greedy? Place strategically?"
         ↓
🎯 Greedy: Doesn't work — placements are interdependent → WRONG
         ↓
💡 "Build incrementally, prune when invalid, backtrack!"
         ↓
✅ Backtracking: Early pruning → OPTIMAL
```
