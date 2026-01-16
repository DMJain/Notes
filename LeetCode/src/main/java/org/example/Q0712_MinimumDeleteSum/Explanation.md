# Minimum ASCII Delete Sum for Two Strings - Explanation

> **Prerequisites**: This problem is a variation of [Longest Common Subsequence (Q1143)](https://leetcode.com/problems/longest-common-subsequence/). If you understand LCS, this problem becomes straightforward — we're just maximizing ASCII sum instead of length.

## Problem in Simple Words
You have two strings, `s1` and `s2`. You want to make them **equal** by deleting characters.
Each deletion costs the **ASCII value** of the deleted character.
Find the **minimum total cost** to make them equal.

**Example**: `s1 = "sea"`, `s2 = "eat"`
- Common string: "ea"
- Delete 's' from "sea" (cost 115)
- Delete 't' from "eat" (cost 116)
- Total cost: 115 + 116 = **231**

---

## Solution 1: Brute Force (Try All Subsequences) ❌ (Too Slow)

### Approach
Generate all possible common subsequences, find the one with maximum ASCII sum.

### Why It's Bad
- Exponential combinations to check
- **O(2^n)** — way too slow!

> 💭 **Brute force is exponential. This looks like a variant of Longest Common Subsequence (LCS). Maybe we can use DP?**

---

## Solution 2: Direct DP (Min Cost to Match) ❌ (Good But More Complex)

### The Natural Thought
"Let's directly track the minimum cost to make prefixes equal."

### Approach
`dp[i][j]` = min cost to make `s1[0..i]` and `s2[0..j]` equal.
- If `s1[i] == s2[j]`: No deletion needed, move both (`dp[i-1][j-1]`)
- If `s1[i] != s2[j]`:
  - Delete `s1[i]`: cost `s1[i] + dp[i-1][j]`
  - Delete `s2[j]`: cost `s2[j] + dp[i][j-1]`
  - Take min.

### Why It's Not Ideal
- Requires handling base cases (when one string is empty, cost = sum of remaining chars)
- Slightly more complex initialization
- Works, but there's an elegant alternative!

### Example: Base Case Complexity ❌

```
Direct DP requires careful base case handling:

Base case 1: s1 is empty, s2 = "abc"
dp[0][j] = sum of all chars in s2[0..j]
dp[0][1] = 'a' = 97
dp[0][2] = 'a' + 'b' = 97 + 98 = 195
dp[0][3] = 'a' + 'b' + 'c' = 97 + 98 + 99 = 294

Base case 2: s2 is empty, s1 = "xyz"
dp[i][0] = sum of all chars in s1[0..i]
dp[1][0] = 'x' = 120
dp[2][0] = 'x' + 'y' = 120 + 121 = 241
...

Then fill the table with complex transitions!
```

**LCS Variation approach:**
```
Base case: dp[0][j] = 0, dp[i][0] = 0  ← Simple!
No ASCII addition needed for base cases.
Just subtract at the end: Total - 2*Common
```

> 💭 **Minimizing deletions is the same as MAXIMIZING what we keep. If we find the maximum common subsequence sum, we can compute the answer by subtraction!**

---

## Solution 3: LCS Variation (Max Common ASCII) ✅ (Optimal & Elegant)

### The Connection 🔗
Let's trace our thinking:
- **Brute Force** was slow because: exponential subsequence checking
- **Direct DP** works but is clunky because: complex base cases, tracking deletions
- **Key insight**: Minimizing deleted = Maximizing kept → Use LCS!

### The Key Insight 💡
Minimizing the **deleted** sum is mathematically equivalent to Maximizing the **kept** (common) sum.

```
Total Sum = (Sum of s1) + (Sum of s2)
Deleted Sum = (Sum of s1 - Common) + (Sum of s2 - Common)
            = (Sum of s1 + Sum of s2) - 2 * Common
```

So, if we find the **Longest Common Subsequence (LCS)** weighted by ASCII values, we can just subtract it!

### The Algorithm

1. **Calculate Total Sum**: Sum of all ASCII values in `s1` and `s2`.
2. **Find Max Common ASCII Sum (`dp`)**:
   - Standard LCS DP, but instead of adding `1` for a match, add `char_value`.
   - `dp[i][j]` = Max ASCII sum of common subsequence between `s1[0..i]` and `s2[0..j]`.
   - If `s1[i] == s2[j]`: `dp[i+1][j+1] = dp[i][j] + value`
   - Else: `dp[i+1][j+1] = max(dp[i+1][j], dp[i][j+1])`
3. **Result**: `Total Sum - 2 * dp[n][m]`

---

## Step-by-Step Walkthrough

**s1 = "sea", s2 = "eat"**

**1. Total Sum**
- "sea": 's'(115) + 'e'(101) + 'a'(97) = 313
- "eat": 'e'(101) + 'a'(97) + 't'(116) = 314
- Total = 313 + 314 = **627**

**2. DP Table (Max Common ASCII)**

Rows: `s` (115), `e` (101), `a` (97)
Cols: `e` (101), `a` (97), `t` (116)

| | "" | e | a | t |
|---|---|---|---|---|
| **""** | 0 | 0 | 0 | 0 |
| **s** | 0 | 0 | 0 | 0 |
| **e** | 0 | **101** | 101 | 101 |
| **a** | 0 | 101 | **198** | 198 |

- `(s, e)`: No match. Max(0, 0) = 0
- `(e, e)`: Match! `0 + 101` = 101
- `(a, a)`: Match! `dp[e][e] + 97` = 101 + 97 = 198
- Final `dp[3][3]` (after processing 't') will be **198** (since 't' matches nothing after 'a').

**3. Final Calculation**
- `Result = Total - 2 * Common`
- `Result = 627 - 2 * 198`
- `Result = 627 - 396`
- `Result = 231` ✅

---

## Visual: The Equation

```
      s1:  [ s ] [ e ] [ a ]
      s2:        [ e ] [ a ] [ t ]
                 └─┬─┘ └─┬─┘
                   │     │
             Common Subsequence
             Sum = 101 + 97 = 198

Deleted from s1: 's' (115)
Deleted from s2: 't' (116)
Total Deleted: 115 + 116 = 231

Formula Check:
(115+101+97) + (101+97+116) - 2*(101+97)
= 313 + 314 - 396
= 231
```

---

## Why This Approach is Elegant

```
Direct DP:                 LCS Variation:
    ↓                           ↓
Track deletions           Track what we KEEP
Complex base cases        Simple LCS formula
                          Just subtract at end!
```

---

## Honorable Mention: Edit Distance Variation

> 💡 **Related Problem**: This is similar to [Q72 Edit Distance](https://leetcode.com/problems/edit-distance/), but with weighted deletions only (no insertions/substitutions). Edit Distance uses the same DP structure with different transition rules. If you master one, the others become easier!

---

## Complexity Analysis

| Solution | Time | Space | Correct? | Why? |
|----------|------|-------|----------|------|
| Brute Force | O(2^n) | O(n) | ✅ TLE | Exponential |
| Direct DP | O(N × M) | O(N × M) | ✅ Works | Tracks deletions |
| **LCS Variation** | O(N × M) | O(N × M) | ✅ **Optimal** | Elegant formula |

- **Time**: We fill an N x M table.
- **Space**: N x M table (can be optimized to O(min(N, M)) space since we only need previous row).

---

## Key Takeaways

1. **Dual Problem**: Min Delete Sum ⇔ Max Common Sum
2. **LCS Pattern**: This is just LCS with weights
3. **ASCII Values**: `char` in Java automatically casts to `int` for math operations

---

## The Journey (TL;DR)

```
🐢 Brute Force: Try all subsequences → TOO SLOW (O(2^n))
         ↓
💡 "This is like LCS! Use DP."
         ↓
📊 Direct DP: Track deletions → WORKS but clunky
         ↓
💡 "Min delete = Max keep! Just use LCS!"
         ↓
✅ LCS Variation: Elegant formula → OPTIMAL
```
