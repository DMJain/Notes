# Longest Word in Dictionary through Deleting - Explanation

## Problem in Simple Words
Given a string `s` and a dictionary of words, find the **longest word** from the dictionary that can be formed by **deleting some characters** from `s` (without reordering).

If there's a tie, pick the **lexicographically smallest** one.

**Example**: `s = "abpcplea"`, dictionary = `["ale", "apple", "monkey", "plea"]`
- Can we form "apple" from "abpcplea"? YES! (keep a,p,p,l,e)
- **Answer: "apple"**

---

## Solution 1: Brute Force (Generate All Subsequences) ❌ (Too Slow)

### Approach
Generate ALL possible subsequences of `s`, then check which ones are in the dictionary.

```java
// Generate 2^n subsequences of s
// For each, check if it's in dictionary
// Return the longest (or lex smallest if tie)
```

### Why It's Bad
- String of length n has **2ⁿ subsequences**!
- n = 1000 → 2^1000 = ASTRONOMICAL!
- **Completely impractical**

> 💭 **Generating subsequences is exponential. But wait — we don't need to generate all subsequences! We just need to check if specific DICTIONARY WORDS can be formed from s. That's much fewer checks.**

---

## Solution 2: Sort Dictionary First ❌ (Unnecessary Work)

### The Natural Thought
"Sort dictionary by length (desc) and lexicographically. Then check each word — the first match is the answer!"

### Approach
```java
// Sort: longer first, then lex smaller first
Collections.sort(dictionary, (a, b) -> {
    if (a.length() != b.length()) return b.length() - a.length();
    return a.compareTo(b);
});

// Check each word, return first match
for (String word : dictionary) {
    if (isSubsequence(word, s)) return word;
}
```

### Example Where It WORKS ✅

```
s = "abpcplea"
dictionary = ["ale", "apple", "monkey", "plea"]

After sort: ["monkey", "apple", "plea", "ale"]
             (by length desc, then lex order)

Check "monkey": is "monkey" in "abpcplea"? NO (no 'm')
Check "apple": is "apple" in "abpcplea"? YES!

Return "apple" ✅
```

### Why It's Not Optimal ❌

```
Sorting takes O(n × m × log n) where:
  - n = dictionary size
  - m = max word length
  - Comparing strings takes O(m)

We can avoid sorting entirely!
```

> 💭 **Sorting guarantees we find the best answer first, but it's O(n log n) extra work. What if we just tracked the best answer as we go? Skip words that can't beat the current best!**

---

## Solution 3: Smart Iteration with Early Skip ✅ (Optimal)

### The Connection 🔗
Let's trace our thinking:
- **Brute Force** was overkill because: generating subsequences is exponential
- **Sorting** added unnecessary cost because: O(n log n) sorting + string comparisons
- **What we need**: check each word, but skip early if it can't beat current best

### The Key Insight 💡
We don't need sorting! Just track the best answer as we go:
- If current word is **shorter** than best → skip
- If current word is **same length but lex larger** than best → skip
- Otherwise, check if it's a subsequence and update best

### Why This Works
```
With Sorting:              Without Sorting:
     ↓                          ↓
O(n × m × log n) sort      Just iterate once!
+ O(n × m) check           O(n × m) total
```

### The Code Logic

```java
String ans = "";

for (String word : dictionary) {
    int a = word.length(), b = ans.length();
    
    // Skip if word can't beat current answer
    if (a < b) continue;                        // Shorter? Skip!
    if (a == b && word.compareTo(ans) > 0) continue;  // Same len but lex larger? Skip!
    
    // Check if word is a subsequence of s
    if (isSubsequence(word, s)) {
        ans = word;
    }
}
return ans;
```

---

## Step-by-Step Walkthrough

**s = `"abpcplea"`, dictionary = `["ale", "apple", "monkey", "plea"]`**

```
═══════════════════════════════════════════════════════════════
Initial: ans = "" (empty)
═══════════════════════════════════════════════════════════════

─────────────────────────────────────────────────────────────
Word 1: "ale"
─────────────────────────────────────────────────────────────
Length check: 3 > 0 (ans length) ✅ Proceed

Subsequence check: is "ale" in "abpcplea"?
  s: a b p c p l e a
     ↑       match 'a'
       ↑     skip 'b'
         ↑   skip 'p'
           ↑ skip 'c'
             ↑ skip 'p'
               ↑ match 'l'
                 ↑ match 'e'
  
  All matched! ✅
  
ans = "ale"

─────────────────────────────────────────────────────────────
Word 2: "apple"
─────────────────────────────────────────────────────────────
Length check: 5 > 3 ✅ Proceed (longer than "ale")

Subsequence check: is "apple" in "abpcplea"?
  s: a b p c p l e a
     ↑         match 'a'
       ↑       skip 'b'
         ↑     match 'p'
           ↑   skip 'c'
             ↑ match 'p'
               ↑ match 'l'
                 ↑ match 'e'
  
  All matched! ✅
  
ans = "apple"

─────────────────────────────────────────────────────────────
Word 3: "monkey"
─────────────────────────────────────────────────────────────
Length check: 6 > 5 ✅ Proceed (longer than "apple")

Subsequence check: is "monkey" in "abpcplea"?
  s: a b p c p l e a
  Looking for 'm'... not found! ❌
  
  Not a subsequence!

ans stays "apple"

─────────────────────────────────────────────────────────────
Word 4: "plea"
─────────────────────────────────────────────────────────────
Length check: 4 < 5 ❌ SKIP! (shorter than "apple")

═══════════════════════════════════════════════════════════════
FINAL ANSWER: "apple"
═══════════════════════════════════════════════════════════════
```

---

## How Subsequence Check Works (Two Pointers)

```java
private boolean isSubsequence(String word, String s) {
    int i = 0, j = 0;  // i for word, j for s
    
    while (i < word.length() && j < s.length()) {
        if (word.charAt(i) == s.charAt(j)) {
            i++;  // Match! Move word pointer
        }
        j++;  // Always move s pointer
    }
    
    return i == word.length();  // Did we match all of word?
}
```

**Visual:**
```
word = "apple"
s    = "abpcplea"

i=0  j=0   'a'=='a' ✓  →  i=1, j=1
i=1  j=1   'p'!='b'    →  j=2
i=1  j=2   'p'=='p' ✓  →  i=2, j=3
i=2  j=3   'p'!='c'    →  j=4
i=2  j=4   'p'=='p' ✓  →  i=3, j=5
i=3  j=5   'l'=='l' ✓  →  i=4, j=6
i=4  j=6   'e'=='e' ✓  →  i=5, j=7

i=5 == word.length() → TRUE! ✅
```

---

## Lexicographic Tie-Breaking

When two words have the **same length**, pick the **lexicographically smaller** one.

```
Example: ans = "apple", new word = "appla"

Same length (5 == 5)
Compare: "appla".compareTo("apple") 
         = 'a' - 'e' = negative → "appla" < "apple"

So "appla" would win!

But in our code:
  if (word.compareTo(ans) > 0) continue;  // Skip if word > ans
  
  "apple".compareTo("appla") > 0? 
  'e' - 'a' = positive → YES, skip!
  
We correctly keep "appla" as the answer.
```

---

## Complexity Analysis

| Solution | Time | Space | Correct? | Why? |
|----------|------|-------|----------|------|
| Brute Force (all subseq) | O(2^n) | O(n) | ✅ But TLE | Exponential |
| Sort + Check | O(n×m×log n + n×m) | O(1) | ✅ Slower | Sorting overhead |
| **Smart Iteration** | O(n × m) | O(1) | ✅ **Optimal** | No sorting needed |

Where: n = dictionary size, m = max(s.length, word.length)

---

## Key Takeaways

1. **Subsequence check = Two pointers** — O(m) time
2. **Skip early** — Don't check words that can't beat current best
3. **No sorting needed** — Just track best as you go
4. **Lex order tiebreaker** — Use `compareTo()` for string comparison
5. **Deletion = subsequence** — Same concept, different wording!

---

## The Journey (TL;DR)

```
🐢 Brute Force: Generate all subsequences → OVERKILL (O(2^n))
         ↓
💡 "Check dictionary words, not all subsequences!"
         ↓
🔢 Sort First: Works but adds O(n log n) → UNNECESSARY
         ↓
💡 "Just track the best as we go, skip losers early!"
         ↓
✅ Smart Iteration: No sort, early skip → OPTIMAL (O(n × m))
```
