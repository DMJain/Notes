# Longest Substring Without Repeating Characters - Explanation

## Problem in Simple Words
Given a string, find the **longest part** of it where **no letter repeats**.

**Example**: In `"abcabcbb"`
- `"abc"` has no repeats ✅ (length 3)
- `"abca"` has 'a' twice ❌
- **Answer: 3**

---

## Solution 1: Brute Force ❌ (Too Slow)

### Approach
Check every possible substring. For each, verify if all characters are unique.

```java
for (int i = 0; i < s.length(); i++) {
    for (int j = i; j < s.length(); j++) {
        if (allUnique(s, i, j)) {
            maxLen = Math.max(maxLen, j - i + 1);
        }
    }
}
```

### Why It's Bad
- Loop i: O(n)
- Loop j: O(n)
- Check uniqueness: O(n)
- **Total: O(n³)** — For 10,000 chars = 1 trillion operations!

---

## Solution 2: Two Pointers Without Set ❌ (Still Too Slow)

### Approach
"Use two pointers! When there's a duplicate, just scan the window to find it."

```java
int left = 0;
for (int right = 0; right < s.length(); right++) {
    // Check if s[right] exists in window [left, right-1]
    for (int k = left; k < right; k++) {
        if (s.charAt(k) == s.charAt(right)) {
            left = k + 1;  // Found duplicate, move left past it
            break;
        }
    }
}
```

### Example Where It WORKS ✅

```
s = "abcd"

right=0: 'a' - no duplicate in [] ✅
right=1: 'b' - scan [a], no 'b' ✅
right=2: 'c' - scan [a,b], no 'c' ✅
right=3: 'd' - scan [a,b,c], no 'd' ✅

Answer: 4 ✅
```

### Example Where It's SLOW ❌

```
s = "abcdefghij..." (1000 unique chars) + "a"

For every new character:
  - Scan the ENTIRE window to check duplicate
  - Window grows to size 1000
  - Each scan = 1000 comparisons

Total: 1000 × 1000 = 1 million operations just for this!
```

### Why It Fails 🤯
**No O(1) lookup** — You're scanning the window each time!

How do you know if a character is duplicate? You scan. That's O(n) per character = O(n²) total.

---

## Solution 3: Sliding Window + HashSet ✅ (Optimal)

### What is it?
Use a **HashSet** to track characters in the current window.
- Expand window when character is unique
- Shrink window when duplicate found

### Why It Solves the Problem
```
Two Pointers (no set):     Sliding Window + Set:
       ↓                          ↓
"Scan window for dupe"      "O(1) lookup in HashSet"
O(n) per character          O(1) per character
O(n²) total                 O(n) total
```

### Step-by-Step Walkthrough

**s = `"abcabcbb"`**

```
═══════════════════════════════════════════════════
STEP 1: right=0, char='a'
═══════════════════════════════════════════════════
Window: [ a ]
Set: {a}
Is 'a' in set? NO → add it, expand window
Max length: 1

═══════════════════════════════════════════════════
STEP 2: right=1, char='b'
═══════════════════════════════════════════════════
Window: [ a, b ]
Set: {a, b}
Is 'b' in set? NO → add it, expand window
Max length: 2

═══════════════════════════════════════════════════
STEP 3: right=2, char='c'
═══════════════════════════════════════════════════
Window: [ a, b, c ]
Set: {a, b, c}
Is 'c' in set? NO → add it, expand window
Max length: 3

═══════════════════════════════════════════════════
STEP 4: right=3, char='a'
═══════════════════════════════════════════════════
Is 'a' in set? YES! DUPLICATE!

Shrink from left until 'a' is gone:
  - Remove 'a' from set, left moves to 1
  
Window: [ b, c, a ]
Set: {b, c, a}
Max length: 3 (unchanged)
```

### Visual Diagram

```
String:  a  b  c  a  b  c  b  b
         ↑        ↑
        left    right

Step 3: Window = "abc", length = 3 ✅

         ↓  expand →
String:  a  b  c  a  b  c  b  b
         ↑        ↑
        left    right='a' DUPLICATE!

         shrink left →
String:  a  b  c  a  b  c  b  b
            ↑     ↑
          left  right

Window = "bca", still length 3
```

---

## Complexity Analysis

| Solution | Time | Space | Correct? |
|----------|------|-------|----------|
| Brute Force | O(n³) | O(n) | ✅ But TLE |
| Two Pointers (no set) | O(n²) | O(1) | ✅ But slow |
| **Sliding Window + Set** | O(n) | O(min(n,26)) | ✅ Optimal |

---

## Key Takeaways

1. **Sliding Window** = Perfect for "contiguous substring" problems
2. **HashSet** = O(1) lookup to check if character is in window
3. Each character visited at most **twice** (once by right, once by left)
4. Pattern: "Longest/shortest with condition" → Sliding Window!
