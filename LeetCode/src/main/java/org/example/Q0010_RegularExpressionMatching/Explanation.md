# Regular Expression Matching - Explanation

## Problem in Simple Words
You have a string `s` and a pattern `p`. Check if the pattern matches the **entire** string.

**Special characters in pattern:**
- `.` → matches ANY single character
- `*` → matches **zero or more** of the character BEFORE it

---

## Solution 1: Brute Force Recursion ❌ (Too Slow)

### Approach
Try all possible ways to match. When you see `*`, either:
- Use it zero times (skip the char+star)
- Use it one or more times (consume a character)

### Why It's Bad
Exponential paths: **O(2^(m+n))** — TLE!

Every `*` creates a branch in the recursion tree.

> 💭 **Recursion explores the same subproblems over and over. What if we remembered results we've already computed?**

---

## Solution 2: Greedy ❌ (Wrong)

### The Natural Thought
"Process left to right. When we see `*`, be greedy — match as many characters as possible!"

### Example Where It FAILS ❌

```
s = "aab", p = "a*ab"

Greedy approach:
- a* matches "aa" (greedy, take all a's)
- Now we need to match "b" with "ab"
- 'a' ≠ 'b' ❌ FAIL!

But the correct match is:
- a* matches "a" (just one)
- a matches "a"
- b matches "b"
- ✅ Should return TRUE!
```

### Why It Fails 🤯
Greedy consumes too many characters! `*` needs to consider **all possibilities**: zero, one, or many matches.

> 💭 **Greedy makes local decisions that may be globally wrong. We need to consider ALL possibilities. That means DP — but how do we structure the table?**

---

## Solution 3: Dynamic Programming ✅ (Optimal)

### The Connection 🔗
Let's trace our thinking:
- **Brute Force Recursion** was slow because: recomputes same subproblems (O(2^(m+n)))
- **Greedy** was wrong because: makes irrevocable choices that may be globally wrong
- **What we need**: consider all possibilities + remember results → **DP!**

### The Key Insight 💡
Define: `dp[i][j]` = Does `s[0..i-1]` match `p[0..j-1]`?

The magic is handling `*` — it creates THREE possibilities:
1. Match zero characters (skip the `x*`)
2. Match exactly one character 
3. Match multiple characters (keep consuming)

### The DP Table

**Note:** 
- `dp[0][...]` = empty string `""`
- `dp[...][0]` = empty pattern `""`

### The Code Logic (Exactly as Written!)

```java
// STEP 1: Base case
dp[0][0] = true;  // "" matches ""

// STEP 2: First row - patterns that match empty string
for (int i = 0; i < p.length(); i++) {
    if (p.charAt(i) == '*' && dp[0][i - 1]) {
        dp[0][i + 1] = true;
    }
}

// STEP 3: Fill the rest
for (int i = 0; i < s.length(); i++) {
    for (int j = 0; j < p.length(); j++) {
        
        // Case 1: '.' matches any character
        if (p.charAt(j) == '.') {
            dp[i + 1][j + 1] = dp[i][j];
        }
        
        // Case 2: Exact match
        if (p.charAt(j) == s.charAt(i)) {
            dp[i + 1][j + 1] = dp[i][j];
        }
        
        // Case 3: '*' handling
        if (p.charAt(j) == '*') {
            if (p.charAt(j - 1) != s.charAt(i) && p.charAt(j - 1) != '.') {
                // Preceding doesn't match → zero occurrences only
                dp[i + 1][j + 1] = dp[i + 1][j - 1];
            } else {
                // Preceding matches → three options
                dp[i + 1][j + 1] = dp[i + 1][j - 1]  // zero x's
                                || dp[i + 1][j]      // one x (single)
                                || dp[i][j + 1];     // multiple x's
            }
        }
    }
}
```

---

## FULL WALKTHROUGH: s = "aab", p = "c*a*b"

**Indexing reminder:**
- `s.charAt(i)` where i = 0,1,2 → 'a','a','b'
- `p.charAt(j)` where j = 0,1,2,3,4 → 'c','*','a','*','b'
- Store in `dp[i+1][j+1]`

---

### STEP 1: Initialize dp[0][0] = true

```
          j:   0     1     2     3     4     5
        p:   ""    'c'   '*'   'a'   '*'   'b'

i=0 s=""   [ T ]  [   ] [   ] [   ] [   ] [   ]
i=1 s="a"  [   ]  [   ] [   ] [   ] [   ] [   ]
i=2 s="aa" [   ]  [   ] [   ] [   ] [   ] [   ]
i=3 s="aab"[   ]  [   ] [   ] [   ] [   ] [   ]
```

---

### STEP 2: First Row Loop — Patterns matching empty string

**Loop: for i = 0 to 4 (p.length()-1)**

```
i=0: p.charAt(0) = 'c' ≠ '*'  → skip

i=1: p.charAt(1) = '*' ✓
     Check dp[0][i-1] = dp[0][0] = TRUE ✓
     Set dp[0][i+1] = dp[0][2] = TRUE
     
i=2: p.charAt(2) = 'a' ≠ '*'  → skip

i=3: p.charAt(3) = '*' ✓
     Check dp[0][i-1] = dp[0][2] = TRUE ✓
     Set dp[0][i+1] = dp[0][4] = TRUE

i=4: p.charAt(4) = 'b' ≠ '*'  → skip
```

```
          j:   0     1     2     3     4     5
        p:   ""    'c'   '*'   'a'   '*'   'b'

i=0 s=""   [ T ]  [ F ] [ T ] [ F ] [ T ] [ F ]
                        ↑           ↑
                    c* matches   c*a* matches
                    empty!       empty!

i=1 s="a"  [ F ]  [   ] [   ] [   ] [   ] [   ]
i=2 s="aa" [ F ]  [   ] [   ] [   ] [   ] [   ]
i=3 s="aab"[ F ]  [   ] [   ] [   ] [   ] [   ]
```

---

### STEP 3: Main Loop — Fill the table row by row

---

#### ROW i=0 (s.charAt(0) = 'a')

**j=0: p.charAt(0) = 'c'**
```
Case 2: p[0]='c', s[0]='a' → 'c' ≠ 'a' → skip
dp[1][1] stays FALSE
```

**j=1: p.charAt(1) = '*'**
```
Case 3: p[1] = '*'
Preceding: p[0] = 'c'
's[0]' = 'a'

Check: p[j-1] != s[i] && p[j-1] != '.'
       'c' != 'a' && 'c' != '.'  → TRUE!
       
So: dp[1][2] = dp[1][0] = FALSE
```

**j=2: p.charAt(2) = 'a'**
```
Case 2: p[2]='a', s[0]='a' → MATCH!
dp[1][3] = dp[0][2] = TRUE ✅
```

**j=3: p.charAt(3) = '*'**
```
Case 3: p[3] = '*'
Preceding: p[2] = 'a'
's[0]' = 'a'

Check: p[j-1] != s[i] && p[j-1] != '.'
       'a' != 'a'  → FALSE!
       
So use three options:
  dp[1][4] = dp[1][2] || dp[1][3] || dp[0][4]
           = FALSE   || TRUE    || TRUE
           = TRUE ✅
```

**j=4: p.charAt(4) = 'b'**
```
Case 2: p[4]='b', s[0]='a' → 'b' ≠ 'a' → skip
dp[1][5] stays FALSE
```

```
          j:   0     1     2     3     4     5
        p:   ""    'c'   '*'   'a'   '*'   'b'

i=0 s=""   [ T ]  [ F ] [ T ] [ F ] [ T ] [ F ]

i=1 s="a"  [ F ]  [ F ] [ F ] [ T ] [ T ] [ F ]
                              ↑     ↑
                           'a'='a' a* works!

i=2 s="aa" [ F ]  [   ] [   ] [   ] [   ] [   ]
i=3 s="aab"[ F ]  [   ] [   ] [   ] [   ] [   ]
```

---

#### ROW i=1 (s.charAt(1) = 'a')

**j=0: 'c'** 
```
'c' ≠ 'a' → dp[2][1] = FALSE
```

**j=1: '*'**
```
Preceding 'c' ≠ 'a' → dp[2][2] = dp[2][0] = FALSE
```

**j=2: 'a'**
```
'a' == 'a' → dp[2][3] = dp[1][2] = FALSE
```

**j=3: '*'** ⭐ KEY CELL!
```
Preceding 'a' == 'a' → use three options!

dp[2][4] = dp[2][2] || dp[2][3] || dp[1][4]
         = FALSE   || FALSE   || TRUE
         = TRUE ✅

🎯 This means: a* matched BOTH 'a's!
   dp[1][4] was TRUE (a* matched first 'a')
   Now dp[2][4] uses dp[1][4] to match second 'a'!
```

**j=4: 'b'**
```
'b' ≠ 'a' → dp[2][5] = FALSE
```

```
          j:   0     1     2     3     4     5
        p:   ""    'c'   '*'   'a'   '*'   'b'

i=0 s=""   [ T ]  [ F ] [ T ] [ F ] [ T ] [ F ]
i=1 s="a"  [ F ]  [ F ] [ F ] [ T ] [ T ] [ F ]

i=2 s="aa" [ F ]  [ F ] [ F ] [ F ] [ T ] [ F ]
                                    ↑
                                a* matched "aa"!

i=3 s="aab"[ F ]  [   ] [   ] [   ] [   ] [   ]
```

---

#### ROW i=2 (s.charAt(2) = 'b') — FINAL ROW!

**j=0: 'c'** → FALSE  
**j=1: '*'** → 'c' ≠ 'b' → dp[3][2] = dp[3][0] = FALSE  
**j=2: 'a'** → 'a' ≠ 'b' → FALSE  
**j=3: '*'** → 'a' ≠ 'b' and 'a' ≠ '.' → dp[3][4] = dp[3][2] = FALSE  

**j=4: 'b'** ⭐ THE ANSWER!
```
Case 2: p[4]='b', s[2]='b' → MATCH!

dp[3][5] = dp[2][4] = TRUE ✅

🎉 ANSWER: TRUE!
```

---

### FINAL TABLE

```
          j:   0     1     2     3     4     5
        p:   ""    'c'   '*'   'a'   '*'   'b'

i=0 s=""   [ T ]  [ F ] [ T ] [ F ] [ T ] [ F ]
i=1 s="a"  [ F ]  [ F ] [ F ] [ T ] [ T ] [ F ]
i=2 s="aa" [ F ]  [ F ] [ F ] [ F ] [ T ] [ F ]
i=3 s="aab"[ F ]  [ F ] [ F ] [ F ] [ F ] [ T ] ← ANSWER!
                                          ↑
                                    dp[3][5] = TRUE
```

---

## The Three Options for '*' (As Per Code!)

When `p.charAt(j) == '*'` and preceding char MATCHES s[i]:

```java
dp[i + 1][j + 1] = dp[i + 1][j - 1]  // Option 1: zero x's (skip x*)
                || dp[i + 1][j]      // Option 2: one x (use x, skip *)
                || dp[i][j + 1];     // Option 3: multiple x's (keep x*, consume s[i])
```

```
Option 1: dp[i+1][j-1] — "What if x* wasn't there?"
   s: [......X]
   p: [.....x*]  → skip x* entirely
            ↑↑
           j-1,j

Option 2: dp[i+1][j] — "Use exactly one x"  
   s: [......X]  → X matched by 'x'
   p: [.....x*]  → ignore the *
             ↑
             j

Option 3: dp[i][j+1] — "Use multiple x's"
   s: [......X]  → consume this X
   p: [.....x*]  → KEEP x* for more!
             ↑↑
             stays at j+1
```

---

## Complexity Analysis

| Solution | Time | Space | Correct? | Why? |
|----------|------|-------|----------|------|
| Brute Force | O(2^(m+n)) | O(m+n) | ✅ But TLE | Exponential branches |
| Greedy | O(m+n) | O(1) | ❌ Wrong | Makes locally wrong choices |
| **DP** | O(m×n) | O(m×n) | ✅ **Optimal** | Considers all paths efficiently |

---

## Key Takeaways

1. **Indexing**: Loop uses i,j but stores in dp[i+1][j+1]
2. **First row**: Special loop handles patterns like `a*b*` matching empty
3. **'*' has three options**: zero, one, or multiple — exactly as code shows!
4. **Order matters**: Check in order: `.` match, exact match, then `*`

---

## The Journey (TL;DR)

```
🐢 Brute Force Recursion: Try all paths → TOO SLOW (O(2^(m+n)))
         ↓
💡 "Can we be greedy? Just match as many as possible?"
         ↓
❌ Greedy: Local decisions are globally wrong → WRONG
         ↓
💡 "We need to consider ALL possibilities AND remember results..."
         ↓
✅ Dynamic Programming: Fill table systematically → OPTIMAL (O(m×n))
```
