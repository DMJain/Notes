# Longest Balanced Substring II - Explanation

> **Prerequisites**: Prefix sum + HashMap pattern (LC 525 Contiguous Array), frequency arrays, substring enumeration  
> **Related Problems**:  
> - [LeetCode 3713 - Longest Balanced Substring I](https://leetcode.com/problems/longest-balanced-substring-i/) | [Local](../Q3713_LongestBalancedSubstringI/Explanation.md) (Same problem, n ≤ 1000, O(n²) accepted)  
> - [LeetCode 525 - Contiguous Array](https://leetcode.com/problems/contiguous-array/) (Prefix sum + HashMap for equal 0s/1s — same diff-encoding trick we use in find2)  
> - [LeetCode 560 - Subarray Sum Equals K](https://leetcode.com/problems/subarray-sum-equals-k/) (Prefix sum + HashMap pattern — count subarrays with target sum)

---

## Problem in Simple Words

Find the longest substring of `s` (only 'a', 'b', 'c') where **every character that appears, appears the same number of times**. For `"abbac"`, the answer is 4 — substring `"abba"` has 'a' twice and 'b' twice.

---

## Solution 1: O(n²) from Q3713 ❌

### The Natural Thought

"We solved this in Q3713 with the `uniq == cntMax` trick — for each starting index, extend right and check balance in O(1). That's O(n²). Let's reuse it."

### Approach

```java
for (int l = 0; l < n; l++) {
    int[] freq = new int[26];
    int uniq = 0, maxF = 0, cntMax = 0;
    for (int r = l; r < n; r++) {
        // update freq, uniq, maxF, cntMax...
        if (uniq == cntMax) ans = max(ans, r - l + 1);
    }
}
```

This works correctly. `uniq == cntMax` means all distinct characters share the max frequency → balanced. (See [Q3713 Explanation](../Q3713_LongestBalancedSubstringI/Explanation.md) for the full derivation.)

### Why It's Bad

**Q3713 had n ≤ 1000 → O(n²) = 1 million ops → fine.** But Q3714 has n ≤ 100,000.

### Example Where It's SLOW ❌

```
For n = 100,000:
  Substring pairs = n(n+1)/2 = 100,000 × 100,001 / 2 = ~5 BILLION ops!
  Java does ~200M ops/sec → 25 seconds → TLE!

Even n = 50,000:
  = 50,000 × 50,001 / 2 = ~1.25 billion ops → still TLE!
```

### Complexity Analysis

| Solution | Time | Space | Status | Why |
|----------|------|-------|--------|-----|
| O(n²) uniq==cntMax | O(n²) | O(1) | ✅ TLE | 5B ops for n=10⁵ |
| Sliding Window | O(n) | O(1) | ❌ Wrong | Balance non-monotonic |
| **Decompose by Subset** | **O(n)** | **O(n)** | **✅ Accepted** | **7 O(n) sub-problems** |

> 💭 **"O(n²) is too slow for n=10⁵. The `uniq==cntMax` check is already O(1) — the bottleneck is the O(n²) enumeration itself. Can we avoid checking all n² substrings entirely? We only have 3 distinct characters. Can we exploit that?"**

---

## Solution 2: Sliding Window ❌

### The Natural Thought

"Sliding window solves most 'longest substring' problems in O(n). Expand right, shrink left when balance breaks."

### Example Where It FAILS ❌

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
  Expected: 4 ("abba") | Got: 3 ← WRONG!
```

### Why It Fails 🤯

Balance is **non-monotonic** — `"abba"` is balanced at `"ab"`, breaks at `"abb"`, then **heals** at `"abba"` when 'a' catches up. Sliding window shrinks at the break point and **permanently loses** the left 'a' that would have contributed to the healing.

(See [Q3713 Explanation § Solution 2](../Q3713_LongestBalancedSubstringI/Explanation.md) for the full proof with multiple examples.)

> 💭 **"Sliding window fails because balance isn't monotonic. O(n²) is too slow. But wait — we're only dealing with 3 characters: 'a', 'b', 'c'. Any balanced substring uses 1, 2, or 3 distinct chars. That's a finite, small number of cases. What if we solve each case separately, each in O(n)?"**

---

## Solution 3: Decompose by Character Subset — O(n) ✅

### The Connection 🔗

Let's trace our thinking:
- **O(n²)** was correct but too slow for n=10⁵ (5 billion ops)
- **Sliding Window** was O(n) but **wrong** (balance isn't monotonic — `"abba"` breaks then heals)
- **What we need**: O(n) AND correct → exploit the constraint that only 3 chars exist → **decompose into 7 independent sub-problems, each O(n)!**

### The Key Insight 💡

With only 3 characters ('a', 'b', 'c'), any balanced substring uses exactly **1, 2, or 3 distinct characters**. That gives us exactly **7 subsets** to check:

```
1-char (3 subsets):  {a}  {b}  {c}             → longest consecutive run
2-char (3 subsets):  {a,b}  {a,c}  {b,c}       → diff encoding + obstacle
3-char (1 subset):   {a,b,c}                   → composite state HashMap

Total: 7 sub-problems, each O(n) → O(7n) = O(n) overall
```

**Why this decomposition works**: Instead of asking "is this substring balanced?" for n² substrings, we ask "what's the longest substring balanced using THIS specific set of characters?" for 7 small sets. Each question can be answered in O(n) using prefix-sum tricks!

### Sub-problem 1: Single-Character Runs

The simplest case. A substring with only one distinct character (e.g., `"aaa"`) is trivially balanced — that character appears `k` times, which equals itself. We just find the longest consecutive run of each character.

```java
for (int i = 0; i < n; i++) {
    if (c[i] == 'a') {
        curA = (i > 0 && c[i-1] == 'a') ? curA + 1 : 1;
        maxA = max(maxA, curA);
    }
    // same for 'b' and 'c'
}
```

**Simple, O(n), no tricks needed.**

---

### Sub-problem 2: Two-Character Balanced — `find2(x, y)`

**Goal**: Find the longest substring that contains ONLY chars `x` and `y`, with equal counts of each.

This is essentially LC 525 (Contiguous Array) with an extra twist: **obstacle tracking**.

#### The Diff Encoding Trick (from LC 525)

Treat `x` as `+1` and `y` as `-1`. Maintain a running `diff`:
- `diff = count(x) - count(y)` in the current prefix
- If `diff` is the same at index `i` and index `j`, then between positions `i+1..j`, the counts of `x` and `y` are equal → **balanced!**
- To maximize length, store the **first** index where each diff value occurred.

```
Example: "abba", find2('a', 'b')
Index:   0   1   2   3
Char:    a   b   b   a
Diff:    1   0  -1   0
                      ↑ diff=0 seen at index -1 (before start) AND index 3
                        → length = 3 - (-1) = 4 → "abba" ✅
```

#### The Obstacle Problem 🚧

But we need substrings with ONLY `x` and `y` — no third character! If 'c' appears between indices `i` and `j`, the substring `[i+1..j]` contains 'c', so it's NOT a valid 2-char balanced substring.

**Concrete example where ignoring obstacles gives WRONG answer:**

```
Input: "abcab", find2('a', 'b')

Without obstacle tracking:
  Index:  0   1   2   3   4
  Char:   a   b   c   a   b
  Diff:   1   0  (c)  1   0
                           ↑ diff=0 last seen at index 1
                             → length = 4 - 1 = 3 → "cab" ← WRONG! Contains 'c'!

  Even worse: diff=1 seen at index 0 AND 3:
    → length = 3 - 0 = 3 → "bca" ← also contains 'c'!
```

#### Obstacle Tracking Solution

When we encounter the 3rd character (not `x` or `y`), we:
1. Record `clearIdx = i` (the obstacle position)
2. Reset `diff` to 0 (start fresh from the obstacle)
3. Store `first[0] = clearIdx` (anchor the new segment)

When checking if a stored diff is valid, we compare: `first[diff] < clearIdx`?
- **Yes** → that stored index is from BEFORE the obstacle → **stale, overwrite it**
- **No** → stored index is AFTER the obstacle → **valid match, compute length**

```
Input: "abcab", find2('a', 'b')

With obstacle tracking:
  i=0: 'a' → diff=1, first[1]=-2 (never seen) → store first[1]=0
  i=1: 'b' → diff=0, first[0]=-1 (before start), -1 ≥ clearIdx(-1) → MATCH!
       len = 1 - (-1) = 2 → "ab" ✅
  i=2: 'c' → OBSTACLE! clearIdx=2, diff=0, first[0]=2
  i=3: 'a' → diff=1, first[1]=0, but 0 < clearIdx(2) → STALE → overwrite first[1]=3
  i=4: 'b' → diff=0, first[0]=2, 2 ≥ clearIdx(2) → MATCH!
       len = 4 - 2 = 2 → "ab" ✅

  maxLen = 2 ✅ (correctly excludes the 'c'!)
```

#### Step-by-Step Walkthrough: `"abbac"`, find2('a', 'b')

```
clearIdx = -1, diff = n (offset for 0)
first[n] = -1 (diff=0 "seen" at virtual index -1)

┌─────┬──────┬───────────────────────┬──────────┬───────────────────────────────────────┐
│  i  │ c[i] │ Action                │ diff-n   │ Result                                │
├─────┼──────┼───────────────────────┼──────────┼───────────────────────────────────────┤
│  0  │  'a' │ diff++ (x=+1)         │   +1     │ first[+1]=-2 < clearIdx(-1)?          │
│     │      │                       │          │   -2 < -1 YES → store first[+1]=0     │
│  1  │  'b' │ diff-- (y=-1)         │    0     │ first[0]=-1 < clearIdx(-1)?            │
│     │      │                       │          │   -1 < -1 NO → MATCH! len=1-(-1)=2    │
│  2  │  'b' │ diff-- (y=-1)         │   -1     │ first[-1]=-2 < -1? YES → store=2      │
│  3  │  'a' │ diff++ (x=+1)         │    0     │ first[0]=-1 < -1? NO → MATCH!         │
│     │      │                       │          │   len = 3 - (-1) = 4 ← "abba"!        │
│  4  │  'c' │ OBSTACLE! clearIdx=4  │ reset→0  │ first[0]=4 (anchor new segment)        │
└─────┴──────┴───────────────────────┴──────────┴───────────────────────────────────────┘

maxLen = 4 ✅ (substring "abba" at indices 0-3)
```

#### Visual: Diff Values for `"abbac"`, find2('a', 'b')

```
String:  ┌─────┬─────┬─────┬─────┬─────┐
         │  a  │  b  │  b  │  a  │  c  │
         └─────┴─────┴─────┴─────┴─────┘
Index:     0     1     2     3     4

Diff:  0 → +1 → 0 → -1 → 0   ║ OBSTACLE → reset to 0
       ↑              ↑        ║
    (idx -1)       (idx 3)     ║   'c' invalidates everything
                               ║   before index 4
    diff=0 at idx -1 AND 3
    → len = 3 - (-1) = 4
    → "abba" = a×2, b×2 ✅

No 'c' between index 0 and 3 → valid!
```

---

### Sub-problem 3: Three-Character Balanced — `find3()`

**Goal**: Find the longest substring where `count(a) == count(b) == count(c)`.

We need TWO differences to be simultaneously zero:
- `diff_ab = count(a) - count(b) = 0`
- `diff_bc = count(b) - count(c) = 0`

(If both differences are 0, all three counts are equal.)

#### Composite State Encoding

Instead of a 2D HashMap `(diff_ab, diff_bc) → first index`, we encode both diffs into a **single long**:

```
'a' → state += 1_000_001     (diff_ab += 1, diff_bc unchanged)
'b' → state -= 1_000_000     (diff_ab -= 1, diff_bc += 1... wait, let's think)
'c' → state -= 1             (diff_ab unchanged, diff_bc -= 1)
```

**Why this works**: Think of `state` as a number where the "millions" digit stores `diff_ab` and the "ones" digit stores `diff_bc`. Since counts never exceed 10⁵, diffs stay in range `[-10⁵, 10⁵]`, so using `1_000_000` as the separator prevents overflow between the two fields.

- `'a'` increments `count(a)` → `diff_ab` goes up by 1, `diff_bc` stays same → `+1_000_001`? Let's verify:
  - Actually: `diff_ab = cnt_a - cnt_b`. If `cnt_a++` → `diff_ab += 1` → effect on state = `+1_000_000`
  - And `diff_bc = cnt_b - cnt_c`. `cnt_a++` → `diff_bc` unchanged → no effect on lower field
  - Wait — the encoding uses `state += 1_000_001` for 'a'. Let me re-derive...

Let's use: `state` encodes `(cnt_a - cnt_b)` in the high part and `(cnt_a - cnt_c)` in the low part:
- `'a'`: `cnt_a` increases → both `(cnt_a - cnt_b)` and `(cnt_a - cnt_c)` increase by 1 → `state += 1_000_001` ✅
- `'b'`: `cnt_b` increases → `(cnt_a - cnt_b)` decreases by 1 → `state -= 1_000_000` ✅ (low part unchanged)
- `'c'`: `cnt_c` increases → `(cnt_a - cnt_c)` decreases by 1 → `state -= 1` ✅ (high part unchanged)

Same state at indices `i` and `j` → both differences are unchanged between `i` and `j` → `cnt_a`, `cnt_b`, `cnt_c` all changed by the same amount between `i` and `j` → **balanced!**

#### Step-by-Step Walkthrough: `"aabcc"`, find3()

```
Initial state = S₀ (large offset), first = {S₀: -1}

┌─────┬──────┬────────────────────┬──────────────────────────────────────────┐
│  i  │ c[i] │ State change       │ Result                                   │
├─────┼──────┼────────────────────┼──────────────────────────────────────────┤
│  0  │  'a' │ state += 1_000_001 │ New state S₁. Not in map → store (S₁:0) │
│  1  │  'a' │ state += 1_000_001 │ New state S₂. Not in map → store (S₂:1) │
│  2  │  'b' │ state -= 1_000_000 │ New state S₃. Not in map → store (S₃:2) │
│  3  │  'c' │ state -= 1         │ New state S₄. Not in map → store (S₄:3) │
│  4  │  'c' │ state -= 1         │ New state = S₁!                          │
│     │      │                    │ S₁ in map at index 0 → len = 4-0 = 4?    │
└─────┴──────┴────────────────────┴──────────────────────────────────────────┘

Wait — let's verify: substring [1..4] = "abcc" → a×1, b×1, c×2 → NOT balanced!

Hmm, let me re-check. Substring (0..4] = indices 1-4 = "abcc" has a:1, b:1, c:2. That's not balanced.

Actually, the state matching means differences are the SAME, not zero. Let me trace more carefully:

After i=0: cnt_a=1, cnt_b=0, cnt_c=0 → diff_ab=1, diff_ac=1
After i=4: cnt_a=2, cnt_b=1, cnt_c=2 → diff_ab=1, diff_ac=0

These are NOT the same! So state CAN'T match at i=0 and i=4. Let me recompute:

state₀ = S  (represents diff_ab=0, diff_ac=0)
i=0 'a': S + 1_000_001 → diff_ab=1, diff_ac=1
i=1 'a': S + 2_000_002 → diff_ab=2, diff_ac=2
i=2 'b': S + 2_000_002 - 1_000_000 = S + 1_000_002 → diff_ab=1, diff_ac=2
i=3 'c': S + 1_000_002 - 1 = S + 1_000_001 → diff_ab=1, diff_ac=1
i=4 'c': S + 1_000_001 - 1 = S + 1_000_000 → diff_ab=1, diff_ac=0

No matches at all! So find3 returns 0 for "aabcc". That's correct — the answer 3 comes from find2('a','b') or find2('b','c') finding "abc", not from find3.

Let me redo with "abcabc":

state₀ = S
i=0 'a': S + 1_000_001 → (1,1). Store.
i=1 'b': S + 1_000_001 - 1_000_000 = S + 1 → (0,1). Store.
i=2 'c': S + 1 - 1 = S → (0,0). S in map at -1! len = 2-(-1) = 3. → "abc" ✅
i=3 'a': S + 1_000_001 → (1,1). In map at 0! len = 3-0 = 3.
i=4 'b': S + 1 → (0,1). In map at 1! len = 4-1 = 3.
i=5 'c': S → (0,0). In map at -1! len = 5-(-1) = 6 → "abcabc" ✅!

maxLen = 6 ✅
```

#### Better Walkthrough: `"abcabc"`, find3()

```
Initial state = S₀, map = {S₀: -1}

┌─────┬──────┬──────────────┬─────────┬──────────────────────────────────────┐
│  i  │ c[i] │ (diff_ab,    │  State  │ Result                               │
│     │      │  diff_ac)    │         │                                      │
├─────┼──────┼──────────────┼─────────┼──────────────────────────────────────┤
│  0  │  'a' │ (1, 1)       │  S₀+m+1 │ Not in map → store at 0             │
│  1  │  'b' │ (0, 1)       │  S₀+1   │ Not in map → store at 1             │
│  2  │  'c' │ (0, 0)       │  S₀     │ In map at -1! len = 2-(-1) = 3      │
│     │      │              │         │ → "abc" (a×1, b×1, c×1) ✅           │
│  3  │  'a' │ (1, 1)       │  S₀+m+1 │ In map at 0! len = 3-0 = 3          │
│     │      │              │         │ → "bca" (b×1, c×1, a×1) ✅           │
│  4  │  'b' │ (0, 1)       │  S₀+1   │ In map at 1! len = 4-1 = 3          │
│     │      │              │         │ → "cabc"? No, indices 2-4 = "cab"    │
│     │      │              │         │   c×1, a×1, b×1 ✅  len=3            │
│  5  │  'c' │ (0, 0)       │  S₀     │ In map at -1! len = 5-(-1) = 6      │
│     │      │              │         │ → "abcabc" (a×2, b×2, c×2) ✅        │
└─────┴──────┴──────────────┴─────────┴──────────────────────────────────────┘

maxLen = 6 ✅   (where m = 1_000_000)
```

#### Visual: State Transitions for `"abcabc"`, find3()

```
String:   ┌─────┬─────┬─────┬─────┬─────┬─────┐
          │  a  │  b  │  c  │  a  │  b  │  c  │
          └─────┴─────┴─────┴─────┴─────┴─────┘
Index:      0     1     2     3     4     5

State:  S₀ ─→ S₁ ─→ S₂ ─→ S₀ ─→ S₁ ─→ S₂ ─→ S₀
        ↑                    ↑                    ↑
      (0,0)               (0,0)               (0,0)
       idx=-1              idx=2               idx=5

       S₀ at -1 and 2 → len=3 ("abc")     ┐
       S₀ at -1 and 5 → len=6 ("abcabc")  ├─ All valid 3-char balanced!
       S₁ at  0 and 3 → len=3 ("bca")     │
       S₂ at  1 and 4 → len=3 ("cab")     ┘

   The CYCLE S₀→S₁→S₂→S₀ repeats every 3 chars because
   each "abc" cycle returns all diffs to their starting values!
```

---

### How It All Fits Together

```
longestBalanced("abbac"):

  Sub-problem 1 (single runs):
    max_a = 1 (no consecutive a's)
    max_b = 2 ("bb")
    max_c = 1

  Sub-problem 2 (two-char pairs):
    find2('a','b') = 4  ← "abba" (a×2, b×2) ← THE WINNER!
    find2('a','c') = 0  (a and c never adjacent without b)
    find2('b','c') = 0  (same)

  Sub-problem 3 (three-char):
    find3() = 0  (no substring with equal a,b,c counts)

  Result = max(2, 4, 0, 0, 0) = 4 ✅
```

---

## Complexity Analysis (Optimal)

| Solution | Time | Space | Status | Why |
|----------|------|-------|--------|-----|
| O(n²) uniq==cntMax | O(n²) | O(1) | ✅ TLE | 5B ops for n=10⁵ |
| Sliding Window | O(n) | O(1) | ❌ Wrong | Balance non-monotonic, shrink destroys future |
| **Decompose (7 × O(n))** | **O(n)** | **O(n)** | **✅ Accepted** | **7 single-pass sub-problems** |

**Quantified for n = 100,000:**
- O(n²): 100,000² / 2 = 5 billion ops → **25 seconds → TLE**
- O(n) decomposition: 7 × 100,000 = 700,000 ops → **~3.5 ms → 1,400× faster!**
- Space: `first[]` array of size 2n+1 ≈ 200,000 ints for find2, HashMap up to n entries for find3 → O(n)

---

## Key Takeaways

1. **Small alphabet → decompose by character subset.** With only 3 characters, there are just 7 possible subsets (3 singles + 3 pairs + 1 triple). Solving each independently turns an O(n²) obstacle into 7 × O(n). This pattern applies whenever the alphabet is tiny relative to n.

2. **Prefix-sum difference encoding + first-occurrence HashMap = O(n) "equal counts" check.** This is the same trick as LC 525 (Contiguous Array): encode balance as a running diff, same diff at two indices means equal counts between them. Generalizes to k differences via composite state encoding.

3. **Obstacle tracking for constrained substrings.** When looking for substrings that contain ONLY specific characters, a third character acts as an "obstacle" that invalidates the stored prefix states. Track the obstacle index (`clearIdx`) and reject any stored state from before it. Don't reset the entire data structure — just check `stored_index < clearIdx`.

---

## The Journey (TL;DR)

```
🐢 O(n²) uniq==cntMax → CORRECT but TLE for n=10⁵ (5 billion ops!)
         ↓
💡 "Can we use O(n) sliding window?"
         ↓
❌ Sliding Window → WRONG (balance not monotonic — "abba" heals!)
         ↓
💡 "Only 3 chars. What if we decompose by which chars the substring uses?"
         ↓
✅ Decompose into 7 subsets (3 singles + 3 pairs + 1 triple)
   Each solved in O(n) using diff-encoding + HashMap → ACCEPTED!
```
