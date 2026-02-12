# Longest Balanced Substring I - Explanation

> **Prerequisites**: Substring enumeration, frequency arrays (`int[26]`), sliding window basics  
> **Related Problems**:  
> - [LeetCode 3 - Longest Substring Without Repeating Characters](https://leetcode.com/problems/longest-substring-without-repeating-characters/) | [Local](../Q0003_LongestSubstringWithoutRepeatingCharacters/Explanation.md) (Sliding window + set — works because "no duplicates" is monotonic)  
> - [LeetCode 2609 - Find the Longest Balanced Substring of a Binary String](https://leetcode.com/problems/find-the-longest-balanced-substring-of-a-binary-string/) (Different "balanced" def: equal 0s before 1s)  
> - [LeetCode 3714 - Longest Balanced Substring II](https://leetcode.com/problems/longest-balanced-substring-ii/) (Same problem, n ≤ 10⁵, needs better than O(n²))

---

## Problem in Simple Words

Find the longest substring where **every character that appears, appears the same number of times**. For `"abbac"`, the answer is 4 — substring `"abba"` has 'a' twice and 'b' twice.

---

## Solution 1: Naïve Brute Force O(n³) ❌

### The Natural Thought

"Try every possible substring. For each one, count character frequencies from scratch. If all non-zero frequencies are equal → it's balanced. Track the longest one."

### Approach

```python
def longestBalanced(s):
    n = len(s)
    ans = 1
    for l in range(n):
        for r in range(l, n):
            freq = [0] * 26
            for k in range(l, r + 1):          # ← Rebuild from scratch EVERY time!
                freq[ord(s[k]) - 97] += 1
            # Check if all non-zero frequencies are the same
            vals = [f for f in freq if f > 0]
            if len(set(vals)) <= 1:
                ans = max(ans, r - l + 1)
    return ans
```

### Why It's Bad

**Triple nested loop!** For each of the O(n²) substring pairs, we scan up to O(n) characters to rebuild the frequency array. We're doing the SAME counting work over and over — when we go from substring `[l..r]` to `[l..r+1]`, we throw away the previous freq array and start from scratch instead of just adding one character.

### Example Where It's SLOW ❌

```
For n = 1000:
  Substring pairs = n(n+1)/2 = 1000 × 1001 / 2 = 500,500
  Avg characters per pair = ~333
  Total ops ≈ 500,500 × 333 = ~166 MILLION ops → TLE!

For n = 500 (even moderate):
  Pairs = 125,250 × avg 167 = ~20.9 million ops
  Still a lot of WASTED rebuilds!
```

### Complexity Analysis

| Solution | Time | Space | Status | Why |
|----------|------|-------|--------|-----|
| Naïve Brute Force | O(n³) | O(26) = O(1) | ✅ TLE | Rebuild freq + scan all 26 per substring |
| Sliding Window | O(?) | O(26) = O(1) | ❌ Wrong | Balance non-monotonic, can't shrink |
| **O(n²) `uniq==cntMax`** | **O(n²)** | **O(26) = O(1)** | **✅ Accepted** | **Incremental freq + O(1) balance check** |

> 💭 **"We're rebuilding the frequency array from scratch for every substring. When we expand from [l..r] to [l..r+1], we only add ONE new character. What if we kept the freq array and just updated it incrementally as we extend right?"**

---

## Solution 2: Sliding Window ❌

### The Natural Thought

"Sliding window solves most 'longest substring with property X' problems. It worked for LC 3 (longest substring without repeating characters). Let me try: expand right as long as balanced, shrink left when it breaks."

### Approach

```python
def longestBalanced(s):
    n = len(s)
    freq = [0] * 26
    ans = 0
    l = 0
    
    for r in range(n):
        freq[ord(s[r]) - 97] += 1
        
        # "Not balanced" → shrink from left?
        while not is_balanced(freq) and l <= r:
            freq[ord(s[l]) - 97] -= 1
            l += 1
        
        if is_balanced(freq):
            ans = max(ans, r - l + 1)
    
    return ans

def is_balanced(freq):
    vals = [f for f in freq if f > 0]
    return len(set(vals)) <= 1
```

### Example Where It FAILS ❌

```
Input: "abcabc"
Full string: a=2, b=2, c=2 → BALANCED! Correct answer = 6

Sliding window trace:
  r=0: "a"        → {a:1}               → balanced ✅  len=1
  r=1: "ab"       → {a:1, b:1}          → balanced ✅  len=2
  r=2: "abc"      → {a:1, b:1, c:1}     → balanced ✅  len=3
  r=3: "abca"     → {a:2, b:1, c:1}     → NOT balanced ❌

  Shrink! Remove s[0]='a':
    l=0→1: "bca"  → {a:1, b:1, c:1}     → balanced ✅  len=3

  r=4: "bcab"     → {a:1, b:2, c:1}     → NOT balanced ❌

  Shrink! Remove s[1]='b':
    l=1→2: "cab"  → {a:1, b:1, c:1}     → balanced ✅  len=3

  r=5: "cabc"     → {a:1, b:1, c:2}     → NOT balanced ❌

  Shrink! Remove s[2]='c':
    l=2→3: "abc"  → {a:1, b:1, c:1}     → balanced ✅  len=3

  Final answer: 3

  Expected: 6 | Sliding window gives: 3 ← WRONG!
```

The window NEVER grows past 3 because at r=3, `"abca"` has `a:2, b:1, c:1` which is imbalanced. So we shrink — but if we had just waited for r=4 and r=5, the frequencies would have **self-healed** to `a:2, b:2, c:2`!

**The imbalance at r=3 was TEMPORARY. Shrinking destroyed the future solution.**

### Another Failing Example

```
Input: "abbac"   (Expected: 4)

  r=0: "a"       → {a:1}           → balanced ✅  len=1
  r=1: "ab"      → {a:1, b:1}      → balanced ✅  len=2
  r=2: "abb"     → {a:1, b:2}      → NOT balanced ❌

  Shrink! Remove 'a':
    l=0→1: "bb"  → {b:2}           → balanced ✅  len=2

  r=3: "bba"     → {a:1, b:2}      → NOT balanced ❌

  Shrink! Remove 'b':
    l=1→2: "ba"  → {a:1, b:1}      → balanced ✅  len=2

  r=4: "bac"     → {a:1, b:1, c:1} → balanced ✅  len=3

  Final answer: 3

  Expected: 4 ("abba") | Sliding window gives: 3 ← WRONG!

  We MISSED "abba" because we shrank past 'a' at index 0 when "abb" was imbalanced,
  but adding 'a' at index 3 would have FIXED it → "abba" = a:2, b:2!
```

### Why It Fails 🤯

1. **Balance is non-monotonic** — A substring can go balanced → imbalanced → balanced again as we extend right. `"abba"`: balanced at `"ab"`, breaks at `"abb"`, **heals** at `"abba"`. There's no point where "once broken, forever broken."

2. **No valid shrink condition** — In LC 3 (no repeats), we shrink when we see a duplicate — the property is monotonic (adding a char can only ADD duplicates, never remove them). Here, imbalance can be **temporary**. We don't know whether to shrink or to keep extending and wait for healing.

3. **Shrinking destroys future potential** — Removing a left character permanently loses a frequency that might have contributed to balance later. In `"abcabc"`, removing the first 'a' at r=3 prevents us from ever seeing `a:2, b:2, c:2`.

> 💭 **"Sliding window fails because balance isn't monotonic — it can break and re-heal as we extend (`"abba"` heals!). O(n) seems impossible for this problem. But we already know how to avoid the O(n³) rebuild — just keep the freq array as we extend right. The bottleneck is now checking 'are all frequencies equal?' For n² substrings, can we check this in O(1) instead of scanning all 26 slots?"**

---

## Solution 3: Optimized O(n²) with `uniq == cntMax` ✅

### The Connection 🔗

Let's trace our thinking:
- **O(n³) Brute Force** was slow because: we rebuilt freq arrays from scratch for every substring AND scanned all 26 slots to check "all equal"
- **Sliding Window** failed because: balance isn't monotonic — it can break and self-heal (like `"abba"`), so there's no valid shrink condition
- **What we need**: Keep the O(n²) two-loop structure (since O(n) is impossible), extend right incrementally so freq updates are O(1), AND check "all frequencies equal" in O(1) without scanning 26 slots → **Track `uniq`, `maxF`, `cntMax`!**

### The Key Insight 💡

Instead of scanning all 26 frequency slots each time, maintain **three extra variables**:

| Variable | Meaning |
|----------|---------|
| `uniq` | Number of **distinct** characters in current window |
| `maxF` | The **highest** frequency among all characters |
| `cntMax` | How many characters currently have frequency == `maxF` |

**Claim**: `uniq == cntMax` ⟺ all distinct characters have the same frequency ⟺ **balanced!**

**Why does this work?**

```
If uniq == cntMax:
  - There are 'uniq' distinct chars in total
  - 'cntMax' of them have the highest frequency (maxF)
  - Since uniq == cntMax, ALL distinct chars have frequency == maxF
  - If every char has the SAME frequency → balanced! ✅

If uniq != cntMax (i.e., uniq > cntMax):
  - Some chars have frequency < maxF
  - Frequencies are NOT all equal → NOT balanced ❌

(cntMax can never exceed uniq, since you can't have more chars at max
 than total distinct chars)
```

**Example**: `"abba"` at full length → `a:2, b:2`:
- `uniq = 2` (two distinct chars: a, b)
- `maxF = 2` (highest frequency is 2)
- `cntMax = 2` (both a and b have frequency 2)
- `uniq == cntMax` → 2 == 2 → **Balanced!** ✅

**Counter-example**: `"abb"` → `a:1, b:2`:
- `uniq = 2`, `maxF = 2`, `cntMax = 1` (only b has frequency 2)
- `uniq != cntMax` → 2 ≠ 1 → **Not balanced** ❌

### The Algorithm

1. Initialize `cnt = 1` (a single character is trivially balanced: 1 distinct char, all at frequency 1)
2. For each **starting index** `l` from `0` to `n-1`:
   - a. Reset: `freq[26] = {0}`, `uniq = 0`, `maxF = 0`, `cntMax = 0`
   - b. For each **ending index** `r` from `l` to `n-1`:
     - Increment `freq[s[r] - 'a']`; let `f` = the new frequency of `s[r]`
     - If `f == 1`: this is a **new** character entering the window → `uniq++`
     - If `f > maxF`: this char now holds a new maximum → `maxF = f`, `cntMax = 1`
     - Else if `f == maxF`: another char reached the max → `cntMax++`
     - If `uniq == cntMax`: all distinct chars share the same freq → **balanced!** → `cnt = max(cnt, r - l + 1)`
3. Return `cnt`

### Step-by-Step Walkthrough

**Example 1**: `"abbac"` → Expected: 4

```
l=0: freq=[], uniq=0, maxF=0, cntMax=0
┌───┬──────┬─────────────────────┬──────┬──────┬────────┬───────────────┬─────┐
│ r │ s[r] │ freq                │ uniq │ maxF │ cntMax │ balanced?     │ cnt │
├───┼──────┼─────────────────────┼──────┼──────┼────────┼───────────────┼─────┤
│ 0 │  'a' │ a:1                 │   1  │   1  │    1   │ 1==1 ✅ len=1│  1  │
│ 1 │  'b' │ a:1, b:1            │   2  │   1  │    2   │ 2==2 ✅ len=2│  2  │
│ 2 │  'b' │ a:1, b:2            │   2  │   2  │    1   │ 2≠1  ❌      │  2  │
│ 3 │  'a' │ a:2, b:2            │   2  │   2  │    2   │ 2==2 ✅ len=4│  4  │
│ 4 │  'c' │ a:2, b:2, c:1       │   3  │   2  │    2   │ 3≠2  ❌      │  4  │
└───┴──────┴─────────────────────┴──────┴──────┴────────┴───────────────┴─────┘
Key moment: r=2 "abb" is imbalanced (b jumped to 2, only 1 char at max).
            r=3 "abba" HEALS (a catches up to 2, now 2 chars at max = 2 distinct = balanced!)
            This is EXACTLY why sliding window fails — it would have shrunk at r=2 and missed this!

l=1: freq=[], uniq=0, maxF=0, cntMax=0
┌───┬──────┬─────────────────────┬──────┬──────┬────────┬───────────────┬─────┐
│ r │ s[r] │ freq                │ uniq │ maxF │ cntMax │ balanced?     │ cnt │
├───┼──────┼─────────────────────┼──────┼──────┼────────┼───────────────┼─────┤
│ 1 │  'b' │ b:1                 │   1  │   1  │    1   │ 1==1 ✅ len=1│  4  │
│ 2 │  'b' │ b:2                 │   1  │   2  │    1   │ 1==1 ✅ len=2│  4  │
│ 3 │  'a' │ b:2, a:1            │   2  │   2  │    1   │ 2≠1  ❌      │  4  │
│ 4 │  'c' │ b:2, a:1, c:1       │   3  │   2  │    1   │ 3≠1  ❌      │  4  │
└───┴──────┴─────────────────────┴──────┴──────┴────────┴───────────────┴─────┘

l=2: freq=[], uniq=0, maxF=0, cntMax=0
┌───┬──────┬─────────────────────┬──────┬──────┬────────┬───────────────┬─────┐
│ r │ s[r] │ freq                │ uniq │ maxF │ cntMax │ balanced?     │ cnt │
├───┼──────┼─────────────────────┼──────┼──────┼────────┼───────────────┼─────┤
│ 2 │  'b' │ b:1                 │   1  │   1  │    1   │ 1==1 ✅ len=1│  4  │
│ 3 │  'a' │ b:1, a:1            │   2  │   1  │    2   │ 2==2 ✅ len=2│  4  │
│ 4 │  'c' │ b:1, a:1, c:1       │   3  │   1  │    3   │ 3==3 ✅ len=3│  4  │
└───┴──────┴─────────────────────┴──────┴──────┴────────┴───────────────┴─────┘

l=3 and l=4: max possible length (2 and 1) can't beat cnt=4, no improvement.

Result: cnt = 4 ✅
```

**Example 2**: `"zzabccy"` → Expected: 4

```
l=0: freq=[], uniq=0, maxF=0, cntMax=0
┌───┬──────┬─────────────────────────┬──────┬──────┬────────┬───────────────┬─────┐
│ r │ s[r] │ freq                    │ uniq │ maxF │ cntMax │ balanced?     │ cnt │
├───┼──────┼─────────────────────────┼──────┼──────┼────────┼───────────────┼─────┤
│ 0 │  'z' │ z:1                     │   1  │   1  │    1   │ 1==1 ✅ len=1│  1  │
│ 1 │  'z' │ z:2                     │   1  │   2  │    1   │ 1==1 ✅ len=2│  2  │
│ 2 │  'a' │ z:2, a:1                │   2  │   2  │    1   │ 2≠1  ❌      │  2  │
│ 3 │  'b' │ z:2, a:1, b:1           │   3  │   2  │    1   │ 3≠1  ❌      │  2  │
│ 4 │  'c' │ z:2, a:1, b:1, c:1      │   4  │   2  │    1   │ 4≠1  ❌      │  2  │
│ 5 │  'c' │ z:2, a:1, b:1, c:2      │   4  │   2  │    2   │ 4≠2  ❌      │  2  │
│ 6 │  'y' │ z:2, a:1, b:1, c:2, y:1 │   5  │   2  │    2   │ 5≠2  ❌      │  2  │
└───┴──────┴─────────────────────────┴──────┴──────┴────────┴───────────────┴─────┘
Starting from l=0 with "zz..." means z gets ahead (freq=2) and no other combo catches up.

l=1: freq=[], uniq=0, maxF=0, cntMax=0
┌───┬──────┬─────────────────────────┬──────┬──────┬────────┬───────────────┬─────┐
│ r │ s[r] │ freq                    │ uniq │ maxF │ cntMax │ balanced?     │ cnt │
├───┼──────┼─────────────────────────┼──────┼──────┼────────┼───────────────┼─────┤
│ 1 │  'z' │ z:1                     │   1  │   1  │    1   │ 1==1 ✅ len=1│  2  │
│ 2 │  'a' │ z:1, a:1                │   2  │   1  │    2   │ 2==2 ✅ len=2│  2  │
│ 3 │  'b' │ z:1, a:1, b:1           │   3  │   1  │    3   │ 3==3 ✅ len=3│  3  │
│ 4 │  'c' │ z:1, a:1, b:1, c:1      │   4  │   1  │    4   │ 4==4 ✅ len=4│  4  │
│ 5 │  'c' │ z:1, a:1, b:1, c:2      │   4  │   2  │    1   │ 4≠1  ❌      │  4  │
│ 6 │  'y' │ z:1, a:1, b:1, c:2, y:1 │   5  │   2  │    1   │ 5≠1  ❌      │  4  │
└───┴──────┴─────────────────────────┴──────┴──────┴────────┴───────────────┴─────┘
✅ "zabc" (indices 1-4): z×1, a×1, b×1, c×1 — all freq=1, uniq=4, cntMax=4 → balanced!
   At r=5, 'c' gets a 2nd occurrence → c jumps to freq=2, only c has max → breaks balance.

Result: cnt = 4 ✅
```

### Visual Diagram

**`"abbac"` — The `uniq` vs `cntMax` dance, showing WHY sliding window fails:**

```
String:  ┌─────┬─────┬─────┬─────┬─────┐
         │  a  │  b  │  b  │  a  │  c  │
         └─────┴─────┴─────┴─────┴─────┘
Index:     0     1     2     3     4

l=0, scanning right →

  r=0: [a]              uniq=1  cntMax=1  ✅ BALANCED
       freq: a▓

  r=1: [a, b]           uniq=2  cntMax=2  ✅ BALANCED (both at freq=1)
       freq: a▓ b▓

  r=2: [a, b, b]        uniq=2  cntMax=1  ❌ IMBALANCED
       freq: a▓ b▓▓
                ↑ b jumped to freq=2, only b has max → 1 of 2 chars at max

       ⚠️ Sliding window would SHRINK here, removing 'a' → "bb"
       But that DESTROYS the window — we'd never see "abba"!

  r=3: [a, b, b, a]     uniq=2  cntMax=2  ✅ HEALED!
       freq: a▓▓ b▓▓
              ↑ a caught up to freq=2! Both chars at max → balanced!

       🎯 THIS is why O(n²) enumeration is necessary:
       the imbalance at r=2 was TEMPORARY and healed at r=3!

  r=4: [a, b, b, a, c]  uniq=3  cntMax=2  ❌ IMBALANCED
       freq: a▓▓ b▓▓ c▓
                       ↑ new char at freq=1, 2 of 3 chars at max
```

**`"zzabccy"` — Finding the balanced window starting from l=1:**

```
String:  ┌─────┬─────┬─────┬─────┬─────┬─────┬─────┐
         │  z  │  z  │  a  │  b  │  c  │  c  │  y  │
         └─────┴─────┴─────┴─────┴─────┴─────┴─────┘
Index:     0     1     2     3     4     5     6

l=1, scanning right →

  r=1: [z]              uniq=1  cntMax=1  ✅
  r=2: [z, a]           uniq=2  cntMax=2  ✅  (all at freq=1)
  r=3: [z, a, b]        uniq=3  cntMax=3  ✅  (all at freq=1)
  r=4: [z, a, b, c]     uniq=4  cntMax=4  ✅  (all at freq=1) ← maxLen=4!
                         ╰────── "zabc" ──────╯
  r=5: [z, a, b, c, c]  uniq=4  cntMax=1  ❌  (c→freq=2, only c has max)
       freq: z▓ a▓ b▓ c▓▓
                        ↑ c broke the balance!
```

---

## Complexity Analysis (Optimal)

| Solution | Time | Space | Status | Why |
|----------|------|-------|--------|-----|
| Naïve Brute Force | O(n³) | O(26) = O(1) | ✅ TLE | Rebuild freq + scan all 26 per substring |
| Sliding Window | O(?) | O(26) = O(1) | ❌ Wrong | Balance non-monotonic, shrink destroys future |
| **O(n²) `uniq==cntMax`** | **O(n²)** | **O(26) = O(1)** | **✅ Accepted** | **Incremental freq updates + O(1) balance check** |

**Quantified for n = 1000**:
- O(n³) Naïve: 500,500 substrings × 333 avg scan = ~166 million ops → **TLE**
- O(n²) Optimal: 500,500 iterations × O(1) work each = ~500,000 ops → **~332× faster**
- Space: only `freq[26]` + 3 ints = 29 variables → essentially **O(1)**

---

## Key Takeaways

1. **`uniq == cntMax` is an O(1) "all-frequencies-equal" check** — Instead of scanning 26 frequency slots every time, track how many characters have the current max frequency. If that count equals total distinct characters → all frequencies are equal → balanced. This trick works for ANY "uniform frequency" problem.

2. **Sliding window fails when the property is non-monotonic** — "All characters have the same frequency" can break and re-heal as you extend (like `"abba"`: breaks at `"abb"`, heals at `"abba"`). If the property can self-repair, there's no valid shrink condition, and sliding window gives wrong answers.

3. **Incremental state tracking > rebuild** — Maintain `freq[26]`, `uniq`, `maxF`, `cntMax` as you extend right. Each character addition requires only O(1) updates. This pattern applies whenever you enumerate substrings and need aggregate frequency statistics — avoid rebuilding from scratch.

---

## The Journey (TL;DR)

```
🐢 O(n³) Brute Force → Rebuild freq per substring (166M ops for n=1000)
         ↓
💡 "Why rebuild? Just extend right and UPDATE the freq array..."
         ↓
❌ Sliding Window → WRONG! (balance not monotonic — "abba" breaks at "abb" then HEALS!)
         ↓
💡 "O(n) impossible. Can we at least check 'all freq equal' in O(1)?"
         ↓
✅ O(n²) uniq==cntMax → Track chars at max freq. All at max = balanced. ACCEPTED!
```
