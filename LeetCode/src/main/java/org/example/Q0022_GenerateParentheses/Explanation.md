# Generate Parentheses - Explanation

## Problem in Simple Words
Generate all valid combinations of `n` pairs of parentheses.
Valid means every open bracket has a matching close bracket and they are properly nested.

**Example**: `n = 2`
- `(())` ✅
- `()()` ✅
- `)(` ❌ (starts with close)
- `(()` ❌ (unbalanced)

---

## Solution 1: Brute Force ❌ (Too Slow)

### Approach
Generate essentialy **all** strings of length `2n` composed of `(` and `)`. Then check if each one is valid.

### Why It's Bad
- Space of possible strings: `2^(2n)`
- n = 3 → 2^6 = 64 (manageable)
- n = 8 → 2^16 = 65,536 (starting to get big)
- Most strings are invalid like `))))((((`
- **Inefficient**.

---

## Solution 2: Backtracking with Pruning ✅ (Optimal)

### The Key Insight 💡
Instead of generating *everything* and checking, build the string **constructively** and only allow valid moves.

Rules for a Valid String:
1. **Open Brackets (`o`)**: Can add one if `o < n`. (We can't have more than n pairs).
2. **Close Brackets (`c`)**: Can add one ONLY if `c < o`. (We can't close a bracket that hasn't been opened!).

This ensures we **never** generate an invalid prefix like `)` or `())`.

### The Algorithm

```java
backtrack(openCount, closeCount, currentString):
    if length == 2*n:
        add to result
        return

    if openCount < n:
        add '('
        backtrack(openCount + 1, closeCount)
        remove '('

    if closeCount < openCount:
        add ')'
        backtrack(openCount, closeCount + 1)
        remove ')'
```

---

## Step-by-Step Walkthrough

**n = 2** (Target length 4)

```
Start: o=0, c=0, ""

1. Add '(': o=1, c=0, "("
   
   1.1 Add '(': o=2, c=0, "(("
       
       1.1.1 Add '(': o=2 !< 2 → Skip
       1.1.2 Add ')': c=0 < o=2 → Valid
             o=2, c=1, "(()"
             
             1.1.2.1 Add '(': Skip
             1.1.2.2 Add ')': c=1 < o=2 → Valid
                     o=2, c=2, "(())" → DONE! Add to list.
                     
   1.2 Add ')': c=0 < o=1 → Valid
       o=1, c=1, "()"
       
       1.2.1 Add '(': o=1 < 2 → Valid
             o=2, c=1, "()("
             
             1.2.1.1 Add ')': c=1 < o=2 → Valid
                     o=2, c=2, "()()" → DONE! Add to list.
                     
       1.2.2 Add ')': c=1 !< o=1 → Skip (Cannot add close if c == o)

Result: ["(())", "()()"]
```

---

## Visual: Recursion Tree

```
                      (0, 0, "")
                          |
                      (1, 0, "(")
                     /           \
           (2, 0, "((")         (1, 1, "()")
               |                   |
           (2, 1, "(()")        (2, 1, "()(")
               |                   |
           (2, 2, "(())")       (2, 2, "()()")
              ✅                   ✅
```

---

## Complexity Analysis

| Solution | Time | Space | Correct? |
|----------|------|-------|----------|
| Brute Force | O(2^(2n) * n) | O(2n) | ✅ TLE |
| **Backtracking** | O(4^n / √n) | O(n) | ✅ Optimal |

- **Time**: The number of valid parenthesis combinations is the **nth Catalan number**, which grows asymptotically as `4^n / (n√n)`.
- **Space**: Recursion stack depth is `2n` → O(n).

---

## Key Takeaways

1. **Constructive generation**: Don't generate garbage and filter; only generate gold.
2. **Pruning conditions**: 
   - `open < n`: limit total pairs.
   - `close < open`: ensure validity (no unmatched closes).
3. **Catalan Numbers**: The sequence 1, 1, 2, 5, 14... appears in many recursive problems (BSTs, polygon triangulation, parentheses).
