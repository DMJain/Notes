# Longest Uncommon Subsequence II - Explanation

> **Prerequisites**: This extends [Q0521 Longest Uncommon Subsequence I](../Q0521_LongestUncommonSubsequenceI/Explanation.md). Q521 is a "brain teaser" — this one requires actual implementation with the `isSubsequence` helper.

## Problem in Simple Words

You have a list of strings. Find the **longest string** that is **NOT a subsequence** of any other string.

**What is a subsequence?**
- Delete some (or no) characters WITHOUT changing order
- `"abc"` is a subsequence of `"aXbYc"` ✅ (delete X, Y)
- `"abc"` is NOT a subsequence of `"acb"` ❌ (order matters!)

**What is "uncommon"?**
A string is uncommon if it's NOT a subsequence of ANY other string in the array.

---

## Solution 1: Brute Force (Generate All Subsequences) ❌ (Way Too Slow)

### Approach
Generate ALL possible subsequences of ALL strings, then check which ones are unique.

```java
// For each string, generate all 2^n subsequences
// Check if any subsequence is not found in other strings
```

### Why It's Bad
- A string of length n has **2ⁿ subsequences**!
- Length 10 → 1024 subsequences
- Length 20 → 1 million subsequences
- **Completely impractical!**

> 💭 **Generating subsequences is exponential and unnecessary. The longest uncommon subsequence must be a FULL string — because if a string has an uncommon subsequence, the string itself is that subsequence!**

---

## Solution 2: Check Longest Strings First ❌ (Good Idea, But Misses Edge Case)

### The Natural Thought
"The longest uncommon subsequence must be one of the longest strings! Just check those first."

### Approach
```java
// Sort by length descending
// Check only the longest strings
// Return first one that's uncommon
```

### Example Where It WORKS ✅

**strs = `["aba", "cdc", "eae"]`**

```
All strings have length 3.

Is "aba" a subsequence of "cdc"? NO ✅
Is "aba" a subsequence of "eae"? NO ✅
→ "aba" is uncommon!

Answer: 3 ✅
```

### Example Where It FAILS ❌

**strs = `["aaa", "aaa", "aa"]`**

```
Longest strings: "aaa" and "aaa" (both length 3)

Check "aaa"₁:
  Is it a subsequence of "aaa"₂? YES! (they're identical!)
  → "aaa"₁ is NOT uncommon ❌

Check "aaa"₂:
  Is it a subsequence of "aaa"₁? YES! (they're identical!)
  → "aaa"₂ is NOT uncommon ❌

Check "aa":
  Is it a subsequence of "aaa"? YES! (delete one 'a')
  → "aa" is NOT uncommon ❌

NO uncommon string exists!
Answer: -1
```

### Why It Fails 🤯
**Identical strings are subsequences of each other!**

If you only check lengths, you miss that duplicates cancel each other out.

You MUST check a string against **ALL** other strings, including strings of the same length.

> 💭 **The issue is duplicates. Looking at length alone isn't enough — we need to check each string against EVERY other string, not just longer ones.**

---

## Solution 3: Check Each String Against All Others ✅ (Optimal)

### The Connection 🔗
Let's trace our thinking:
- **Brute Force** was overkill because: generating subsequences is exponential and unnecessary
- **Check longest** failed because: misses duplicates that cancel each other out
- **What we need**: check each FULL string against ALL others

### The Key Insight 💡
For each string, check if it's a subsequence of ANY other string:
- If NOT a subsequence of any other → it's uncommon!
- Track the maximum length among uncommon strings

### Why This Works
```
Only check longest:         Check against ALL:
       ↓                          ↓
"Skip duplicates"            "Duplicates detected"
Misses "aaa" = "aaa"         Catches "aaa" = "aaa"
```

### Step-by-Step Walkthrough

**strs = `["aaa", "aaa", "aa"]`**

```
═══════════════════════════════════════════════════
Check "aaa"₁ (index 0)
═══════════════════════════════════════════════════
Compare with "aaa"₂ (index 1):
  Is "aaa" a subsequence of "aaa"? 
  
  Pointer i (str1): a → a → a
  Pointer j (str2): a → a → a
  
  All matched! YES, it IS a subsequence ❌
  
→ "aaa"₁ is NOT uncommon (skip it)

═══════════════════════════════════════════════════
Check "aaa"₂ (index 1)
═══════════════════════════════════════════════════
Compare with "aaa"₁ (index 0):
  Same logic — YES, it IS a subsequence ❌
  
→ "aaa"₂ is NOT uncommon (skip it)

═══════════════════════════════════════════════════
Check "aa" (index 2)
═══════════════════════════════════════════════════
Compare with "aaa"₁ (index 0):
  Is "aa" a subsequence of "aaa"?
  
  Pointer i: a → a
  Pointer j: a → a → a
  
  After 2 steps, i finished! YES, it IS a subsequence ❌
  
→ "aa" is NOT uncommon (skip it)

═══════════════════════════════════════════════════
RESULT: No uncommon string found → return -1
═══════════════════════════════════════════════════
```

### Another Example Where It Works

**strs = `["aba", "cdc", "eae"]`**

```
Check "aba":
  vs "cdc": Is "aba" in "cdc"? Need a,b,a in order... NO! ✅
  vs "eae": Is "aba" in "eae"? Need a,b,a in order... NO! ✅
  → "aba" IS uncommon! Length = 3

Check "cdc":
  vs "aba": NO ✅
  vs "eae": NO ✅
  → "cdc" IS uncommon! Length = 3

Check "eae":
  vs "aba": NO ✅
  vs "cdc": NO ✅
  → "eae" IS uncommon! Length = 3

RESULT: max(3, 3, 3) = 3
```

### How Subsequence Check Works (Two Pointers)

```
Is "aba" a subsequence of "aebfac"?

str1 = "aba"
str2 = "aebfac"

i=0, j=0: 'a' == 'a' ✅ i++, j++
i=1, j=1: 'b' != 'e' ❌ j++
i=1, j=2: 'b' == 'b' ✅ i++, j++
i=2, j=3: 'a' != 'f' ❌ j++
i=2, j=4: 'a' == 'a' ✅ i++, j++

i reached end → "aba" IS a subsequence!
```

---

## Important Note 💡

**The longest uncommon subsequence must be a FULL string, not a partial one.**

Why?
- If string S has an uncommon subsequence, S itself is that subsequence!
- Any shorter subsequence of S would still be "contained" in S
- So we never need to generate partial subsequences!

---

## Complexity Analysis

| Solution | Time | Space | Correct? | Why? |
|----------|------|-------|----------|------|
| Brute Force (all subseq) | O(n × 2ᵐ) | O(2ᵐ) | ✅ But TLE | Exponential |
| Only check longest | O(n² × m) | O(1) | ❌ Misses dupes | Ignores duplicates |
| **Check all pairs** | O(n² × m) | O(1) | ✅ **Optimal** | Catches everything |

n = number of strings, m = max string length

---

## Key Takeaways

1. **Don't generate all subsequences** — that's exponential!
2. **Check FULL strings only** — the longest uncommon must be a full string
3. **Duplicates matter** — same strings are subsequences of each other
4. **Two-pointer technique** for efficient subsequence checking
5. Always check a string against **ALL** others, not just longer ones

---

## The Journey (TL;DR)

```
🐢 Brute Force: Generate all subsequences → OVERKILL (O(2^n))
         ↓
💡 "Check longest strings first?"
         ↓
📏 Check Longest: Works but misses duplicates → WRONG on edge cases
         ↓
💡 "Duplicates cancel each other! Must check ALL pairs."
         ↓
✅ Check All Pairs: Catches everything → OPTIMAL (O(n² × m))
```
