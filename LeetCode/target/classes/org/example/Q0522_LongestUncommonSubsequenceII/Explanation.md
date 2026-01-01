# Longest Uncommon Subsequence II - Explanation

## Problem in Simple Words
You have a bunch of strings. You need to find the **longest string** that is **NOT a subsequence** of any other string in the array.

**What is a subsequence?**
A subsequence is formed by deleting some (or no) characters from a string WITHOUT changing the order.
- "abc" is a subsequence of "aXbYc" ✅ (delete X and Y)
- "abc" is a subsequence of "abc" ✅ (delete nothing)  
- "abc" is NOT a subsequence of "acb" ❌ (order matters!)

**What is "uncommon"?**
A string is "uncommon" if it's NOT a subsequence of ANY other string in the array.

---

## The Smart Solution (Check Each String)

### The Core Idea 💡
Instead of generating all possible subsequences (exponential!), we realize:

**Key Insight**: The longest uncommon subsequence must be one of the original strings themselves!

Why? If a string S is uncommon, then S itself is the longest uncommon subsequence from S. There's no point looking at shorter subsequences of S.

**Analogy**: Imagine you're checking if anyone's signature can be forged from another person's handwriting. You don't check random combinations - you just check each complete signature against all others!

### Algorithm
1. For each string S in the array:
   - Check if S is a subsequence of any OTHER string
   - If S is NOT a subsequence of any other string → S is "uncommon"
   - Track the maximum length among all uncommon strings

2. Return the maximum length (or -1 if no uncommon string exists)

### Step-by-Step Walkthrough

**Example 1**: `strs = ["aba", "cdc", "eae"]`

| String | Is it a subsequence of others? | Uncommon? | Length |
|--------|-------------------------------|-----------|--------|
| "aba" | Not in "cdc" ✅, Not in "eae" ✅ | YES ✅ | 3 |
| "cdc" | Not in "aba" ✅, Not in "eae" ✅ | YES ✅ | 3 |
| "eae" | Not in "aba" ✅, Not in "cdc" ✅ | YES ✅ | 3 |

**Answer: 3** (all are uncommon, max length is 3)

---

**Example 2**: `strs = ["aaa", "aaa", "aa"]`

| String | Is it a subsequence of others? | Uncommon? | Length |
|--------|-------------------------------|-----------|--------|
| "aaa"₁ | IS subsequence of "aaa"₂ ❌ | NO ❌ | - |
| "aaa"₂ | IS subsequence of "aaa"₁ ❌ | NO ❌ | - |
| "aa" | IS subsequence of "aaa"₁ ❌ | NO ❌ | - |

**Answer: -1** (every string is a subsequence of some other string)

### How the Subsequence Check Works

```
Is "aba" a subsequence of "aebfac"?

str1 = "aba"
str2 = "aebfac"

Pointer i (str1): a → b → a
Pointer j (str2): a → e → b → f → a → c

Step 1: 'a' == 'a' ✅ Match! i++, j++
Step 2: 'b' != 'e' ❌ No match, j++
Step 3: 'b' == 'b' ✅ Match! i++, j++
Step 4: 'a' != 'f' ❌ No match, j++
Step 5: 'a' == 'a' ✅ Match! i++, j++

i reached end of str1 → "aba" IS a subsequence! Return TRUE
```

---

## Why Brute Force (Generate All Subsequences) Doesn't Cut It ❌

### Brute Force Approach
```java
// Generate ALL subsequences of ALL strings
// Check which subsequence is not found in any other string
// Return the longest such subsequence
```

### Why It's TERRIBLE
- Each string of length n has **2ⁿ subsequences**!
- For a string of length 10, that's 1024 subsequences
- For multiple strings, this explodes exponentially
- **Time**: O(n × 2ᵐ) where m is max string length
- Completely impractical!

---

## Why Sorting by Length Helps (Optimization) 🚀

### The Optimization
If we sort strings by length (descending), we can return early:
- The longest uncommon subsequence must be among the longest strings
- Once we find an uncommon string, no need to check shorter ones!

```java
Arrays.sort(strs, (a, b) -> b.length() - a.length());
// Now check from longest to shortest
```

This doesn't change worst-case complexity but helps in practice.

---

## Why We Check Full Strings Only

**Brilliant Insight**: 
If string S has an uncommon subsequence, then S itself is that subsequence!

Why? 
- Any subsequence of S is either:
  1. Equal to S → If S is uncommon, we found it
  2. Shorter than S → If shorter version is uncommon, S itself is also uncommon (and longer!)

So we never need to generate partial subsequences!

---

## Complexity Analysis

### Our Solution (Check Each String)
| Metric | Complexity | Explanation |
|--------|------------|-------------|
| **Time** | O(n² × m) | n = number of strings, m = max string length. For each pair, O(m) subsequence check |
| **Space** | O(1) | Only using a few variables |

### Brute Force (Generate All Subsequences)
| Metric | Complexity | Explanation |
|--------|------------|-------------|
| **Time** | O(n × 2ᵐ × n × m) | 2ᵐ subsequences per string, checking each against all others |
| **Space** | O(2ᵐ) | Storing all subsequences |

### Optimized with Sorting
| Metric | Complexity | Explanation |
|--------|------------|-------------|
| **Time** | O(n log n + n² × m) | Sorting + checking (but often returns early) |
| **Space** | O(1) or O(n) | Depends on sort implementation |

---

## Common Mistakes

1. **Confusing subsequence with substring**
   - Substring: Contiguous characters ("abc" in "xabcy")
   - Subsequence: Can skip characters ("ac" in "abc")

2. **Forgetting identical strings**
   - "aaa" is a subsequence of "aaa" → Both are NOT uncommon

3. **Checking against itself**
   - Always skip `i == j` comparison!

---

## Key Takeaways

1. **Don't generate all subsequences** - that's exponential!
2. **The longest uncommon subsequence must be a full string** from the input
3. **Two-pointer technique** for efficient subsequence checking
4. A string is uncommon if it's NOT a subsequence of ANY other string
5. Same strings (duplicates) can never be uncommon (they're subsequences of each other)
