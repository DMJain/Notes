# Minimum ASCII Delete Sum for Two Strings - Explanation

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

## Solution 1: Direct DP (Min Cost) ❌ (Good but more code)

### Approach
`dp[i][j]` = min cost to make `s1[i:]` and `s2[j:]` equal.
- If `s1[i] == s2[j]`: No deletion needed, move both (`dp[i+1][j+1]`).
- If `s1[i] != s2[j]`:
  - Delete `s1[i]`: cost `s1[i] + dp[i+1][j]`
  - Delete `s2[j]`: cost `s2[j] + dp[i][j+1]`
  - Take min.

### Why It's Not "Optimal" (Subjective)
- Requires handling base cases (when one string is empty, cost = sum of remaining chars).
- Slightly more complex initialization.

---

## Solution 2: LCS Variation (Max Common ASCII) ✅ (Optimal & Elegant)

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

## Complexity Analysis

| Solution | Time | Space | Correct? |
|----------|------|-------|----------|
| Brute Force | O(2^n) | O(n) | ✅ TLE |
| **LCS Variation** | O(N × M) | O(N × M) | ✅ Optimal |

- **Time**: We fill an N x M table.
- **Space**: N x M table (can be optimized to O(min(N, M)) space since we only need previous row).

---

## Key Takeaways

1. **Dual Problem**: Min Delete Sum ⇔ Max Common Sum.
2. **LCS Pattern**: This is just LCS with weights.
3. **ASCII Values**: `char` in Java automatically casts to `int` for math operations.
