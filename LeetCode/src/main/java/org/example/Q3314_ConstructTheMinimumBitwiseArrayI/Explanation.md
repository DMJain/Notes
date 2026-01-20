# 3314. Construct the Minimum Bitwise Array I - Explanation

> **Prerequisites**: Understanding of bitwise operations (OR, AND, NOT, shift operators)
> **Related Problems**: 
> - [LeetCode 201 - Bitwise AND of Numbers Range](https://leetcode.com/problems/bitwise-and-of-numbers-range/) (Bit manipulation pattern)
> - [LeetCode 371 - Sum of Two Integers](https://leetcode.com/problems/sum-of-two-integers/) (Bit manipulation to avoid arithmetic)
> - [LeetCode 268 - Missing Number](https://leetcode.com/problems/missing-number/) (Bit manipulation tricks)

## Problem in Simple Words

For each number `n` in the array, find the **smallest** `x` such that `x | (x+1) = n`. If no such `x` exists, return `-1`.

**Example**: For `n = 7`, we need `x` where `x | (x+1) = 7`. Answer: `x = 3` because `3 | 4 = 011 | 100 = 111 = 7`.

---

## Solution 1: Brute Force ❌

### Approach
For each `n`, try all values of `x` from `0` to `n-1` and check if `x | (x+1) == n`.

```java
public int[] minBitwiseArray(List<Integer> nums) {
    int[] res = new int[nums.size()];
    for (int i = 0; i < nums.size(); i++) {
        int n = nums.get(i);
        res[i] = -1;  // default: no solution
        for (int x = 0; x < n; x++) {    // Try all candidates
            if ((x | (x + 1)) == n) {
                res[i] = x;
                break;  // First valid = minimum
            }
        }
    }
    return res;
}
```

### Why It's Bad
- **Time**: O(n × max(nums)) per element
- For each number, we iterate up to `n` times

### Example Where It's SLOW ❌
```
For nums = [1000]:
Operations = 100 × 1000 = 100,000 checks
For nums.length = 100 and max = 1000:
Operations = 100 × 1000 = 100,000
```

While this passes given constraints (max 100,000 ops), it's inefficient and doesn't reveal the mathematical insight.

> 💭 **"Can we understand the pattern of `x | (x+1)` to find `x` more intelligently?"**

---

## Understanding the Pattern 💡

Let's analyze what `x | (x+1)` actually produces:

```
x     | x+1   | x | (x+1)
------|-------|------------
0: 00 | 1: 01 | 01 = 1
1: 01 | 2: 10 | 11 = 3
2: 10 | 3: 11 | 11 = 3
3: 011| 4:100 | 111 = 7
4: 100| 5:101 | 101 = 5
5: 101| 6:110 | 111 = 7
6: 110| 7:111 | 111 = 7
7: 0111| 8:1000| 1111 = 15
```

### Key Observation 1: Even Numbers Are Impossible ❌

When `n` is **even**, there's no valid `x`.

Why? `x | (x+1)` always has bit 0 set:
```
x     ends in 0 → x+1 ends in 1 → OR has bit 0 = 1
x     ends in 1 → bit 0 is already 1 → OR has bit 0 = 1
```

**Result is always ODD!**

```
n = 2 (binary: 10) → bit 0 is 0 → IMPOSSIBLE
n = 4 (binary: 100) → bit 0 is 0 → IMPOSSIBLE
```

### Key Observation 2: The Trailing Ones Pattern

For odd numbers, look at the **trailing 1s**:

```
n = 3  = 011₂  → trailing 1s: 2 → result = 3 - 2 = 1
n = 5  = 101₂  → trailing 1s: 1 → result = 5 - 1 = 4
n = 7  = 111₂  → trailing 1s: 3 → result = 7 - 4 = 3
n = 13 = 1101₂ → trailing 1s: 1 → result = 13 - 1 = 12
```

**Insight**: We subtract the power of 2 corresponding to the rightmost 0 bit divided by 2!

---

## Solution 2: Iterative Trailing Ones ✅

### The Natural Thought
Instead of checking all candidates, we find the position of the first 0 bit (after trailing 1s) and use that to calculate the answer directly.

### Approach
```java
public int[] minBitwiseArray(List<Integer> nums) {
    int n = nums.size();
    int[] result = new int[n];
    for (int i = 0; i < n; i++) {
        int x = nums.get(i);
        int res = -1;
        int d = 1;                    // Start with bit 0 (value 1)
        while ((x & d) != 0) {        // While current bit is 1
            res = x - d;              // Subtract current power of 2
            d <<= 1;                  // Move to next bit
        }
        result[i] = res;
    }
    return result;
}
```

### Step-by-Step Walkthrough

**Example: x = 7 (binary: 111)**

```
Initial: x = 7 = 0111, d = 1, res = -1

Iteration 1:
  (x & d) = (0111 & 0001) = 0001 ≠ 0  → bit 0 is set
  res = 7 - 1 = 6
  d = 1 << 1 = 2

Iteration 2:
  (x & d) = (0111 & 0010) = 0010 ≠ 0  → bit 1 is set
  res = 7 - 2 = 5
  d = 2 << 1 = 4

Iteration 3:
  (x & d) = (0111 & 0100) = 0100 ≠ 0  → bit 2 is set
  res = 7 - 4 = 3  ← This is our answer!
  d = 4 << 1 = 8

Iteration 4:
  (x & d) = (0111 & 1000) = 0000 = 0  → bit 3 is NOT set
  STOP! Loop exits.

Final: res = 3

Verify: 3 | 4 = 011 | 100 = 111 = 7 ✅
```

**Example: x = 13 (binary: 1101)**

```
Initial: x = 13 = 1101, d = 1, res = -1

Iteration 1:
  (x & d) = (1101 & 0001) = 0001 ≠ 0  → bit 0 is set
  res = 13 - 1 = 12  ← This is our answer!
  d = 1 << 1 = 2

Iteration 2:
  (x & d) = (1101 & 0010) = 0000 = 0  → bit 1 is NOT set
  STOP! Loop exits.

Final: res = 12

Verify: 12 | 13 = 1100 | 1101 = 1101 = 13 ✅
```

**Example: x = 2 (binary: 10) - Even number**

```
Initial: x = 2 = 10, d = 1, res = -1

Iteration 1:
  (x & d) = (10 & 01) = 00 = 0  → bit 0 is NOT set
  STOP! Loop exits immediately.

Final: res = -1 (no valid answer) ✅
```

### Visual Diagram

```
For n = 7 = 0111:

  0  1  1  1    ← n in binary
  │  │  │  │
  │  │  │  └── d=1: bit set → res = 7-1 = 6
  │  │  └───── d=2: bit set → res = 7-2 = 5  
  │  └──────── d=4: bit set → res = 7-4 = 3 ★
  └─────────── d=8: bit NOT set → STOP

Result: x = 3, because 3 | 4 = 7
```

> 💭 **"Can we compute this even faster without a loop using pure bit manipulation?"**

---

## Solution 3: Optimal Bit Manipulation ✅

### The Connection 🔗

- **Brute Force** was slow because: searched all candidates O(max(nums))
- **Iterative** is better but: still loops through trailing 1s O(log n)
- **What we need**: Single operation to find the position

### The Key Insight 💡

The formula `n & ~(((n + 1) & ~n) >> 1)` does exactly what our loop does, but in O(1):

1. `(n + 1) & ~n` = finds the **lowest 0 bit** in `n` (it becomes 1 after adding 1)
2. `>> 1` = shift right to get the bit we need to clear (the one before the lowest 0)
3. `n & ~(...)` = clear that bit from `n`

### The Algorithm

```java
public int[] minBitwiseArray(List<Integer> nums) {
    int[] res = new int[nums.size()];
    for (int i = 0; i < nums.size(); i++) {
        int n = nums.get(i);
        if ((n & 1) == 1)                           // Odd: solution exists
            res[i] = n & ~(((n + 1) & ~n) >> 1);   // O(1) formula
        else
            res[i] = -1;                            // Even: impossible
    }
    return res;
}
```

### Step-by-Step Walkthrough

**Example: n = 7 (binary: 111)**

```
Step 1: (n + 1) = 8 = 1000
Step 2: ~n = ...11111000
Step 3: (n + 1) & ~n = 1000 & ...11111000 = 1000 (lowest 0 bit of n)
Step 4: >> 1 = 0100 (the bit we need to clear)
Step 5: ~(0100) = ...11111011
Step 6: n & ~(...) = 0111 & ...11111011 = 0011 = 3

Verify: 3 | 4 = 011 | 100 = 111 = 7 ✅
```

**Example: n = 13 (binary: 1101)**

```
Step 1: (n + 1) = 14 = 1110
Step 2: ~n = ...10010
Step 3: (n + 1) & ~n = 1110 & ...10010 = 0010 (lowest 0 bit)
Step 4: >> 1 = 0001
Step 5: ~(0001) = ...11111110
Step 6: n & ~(...) = 1101 & ...11111110 = 1100 = 12

Verify: 12 | 13 = 1100 | 1101 = 1101 = 13 ✅
```

### Visual Diagram

```
n = 7 = 0111
        ^^^^
        └──┴── trailing 1s (3 of them)
        
We need to find x where x | (x+1) = 7

x = 3 = 0011       x+1 = 4 = 0100
          ▲                    ▲
          │                    │
   trailing 1s = 2      carries into bit 2
   
0011 | 0100 = 0111 = 7 ✅

┌─────────────────────────────────────────┐
│  Formula breakdown for n = 7:           │
│                                         │
│  n     = 0111                           │
│  n+1   = 1000                           │
│  ~n    = 1000  (relevant bits)          │
│  (n+1) & ~n = 1000  (lowest 0 in n)     │
│  >> 1  = 0100  (bit to clear)           │
│  n & ~(0100) = 0111 & 1011 = 0011 = 3   │
└─────────────────────────────────────────┘
```

---

## Complexity Analysis

| Solution | Time | Space | Correct? | Why? |
|----------|------|-------|----------|------|
| Brute Force | O(n × m) | O(n) | ✅ TLE for large | Searches all candidates |
| Iterative Trailing 1s | O(n × log m) | O(n) | ✅ Better | Loops through bits |
| **Bit Manipulation** | **O(n)** | **O(n)** | ✅ **Optimal** | O(1) per element |

Where `n` = nums.length, `m` = max value in nums

---

## Key Takeaways

1. **Pattern**: `x | (x+1)` always produces an odd number (bit 0 is always set)
2. **Even Check**: If target is even, return -1 immediately
3. **Trailing 1s**: The answer involves the position of trailing 1s
4. **Bit Trick**: Use `(n+1) & ~n` to find the lowest unset bit in O(1)

---

## The Journey (TL;DR)

```
🐢 Brute Force → WORKS but searches all candidates
         ↓
💡 "What pattern does x | (x+1) follow?"
         ↓
✅ Iterative → O(log n) by tracking trailing 1s
         ↓
💡 "Can we do this without a loop?"
         ↓
✅ Optimal → O(1) with bit manipulation formula
```
