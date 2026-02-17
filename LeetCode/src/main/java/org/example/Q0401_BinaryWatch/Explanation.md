# Binary Watch - Explanation

> **Prerequisites**: Bit manipulation (`&`, `>>`, `<<`, `Integer.bitCount()`), binary ↔ decimal conversion, backtracking basics (choose → explore → unchoose)
> **Related Problems**:
> - [LeetCode 17 - Letter Combinations of a Phone Number](https://leetcode.com/problems/letter-combinations-of-a-phone-number/) | [Local](../Q0017_LetterCombinations/Explanation.md) (Same backtracking pattern: enumerate combinations from groups)
> - [LeetCode 78 - Subsets](https://leetcode.com/problems/subsets/) (Enumerate all subsets — choosing which LEDs to turn on IS choosing a subset)
> - [LeetCode 338 - Counting Bits](https://leetcode.com/problems/counting-bits/) (`Integer.bitCount()` is the core building block for the brute force approach)
> - [LeetCode 190 - Reverse Bits](https://leetcode.com/problems/reverse-bits/) | [Local](../Q0190_ReverseBits/Explanation.md) (Bit extraction & shifting — same bit-level thinking)
> - [LeetCode 762 - Prime Number of Set Bits](https://leetcode.com/problems/prime-number-of-set-bits-in-binary-representation/) (Counting set bits in a range — related `bitCount` usage)

---

## Problem in Simple Words

A binary watch has **10 LEDs**: 4 represent hours (0–11), 6 represent minutes (0–59). Given that exactly `k` LEDs are ON, list every valid time the watch could show.

Example: `turnedOn = 1` → ten times: `0:01, 0:02, 0:04, 0:08, 0:16, 0:32, 1:00, 2:00, 4:00, 8:00` (each has exactly one LED on).

---

## Solution 1: Brute Force — Enumerate All 720 Times ❌

### The Natural Thought

"There are only 12 possible hours and 60 possible minutes. That's just 720 combinations. For each combo, count how many bits it takes to display that time, and keep the ones that match `turnedOn`."

### Approach

```java
public List<String> readBinaryWatch(int turnedOn) {
    List<String> res = new ArrayList<>();
    for (int h = 0; h < 12; h++) {
        for (int m = 0; m < 60; m++) {
            // Count total set bits across hour and minute
            if (Integer.bitCount(h) + Integer.bitCount(m) == turnedOn) {
                res.add(h + ":" + (m < 10 ? "0" : "") + m);
            }
        }
    }
    return res;
}
```

Simple! Two nested loops, one `bitCount` check per combo. But how efficient is this really?

### Why It's Bad

It **works** — the input space is tiny (720 combos max), so it passes easily. But it's fundamentally wasteful: you check ALL time combos blindly, even though most don't have the right number of bits.

### Example Where It's WASTEFUL ❌

```
Input: turnedOn = 2

Total combos checked:  12 × 60 = 720
Valid times found:     only 44 (see below)
Wasted checks:         720 - 44 = 676 iterations (93.9% waste!)

Some valid k=2 times: "0:03", "0:05", "0:06", "0:09", "0:10", "0:12",
                      "0:17", "0:18", "0:20", "0:24", "0:33", "0:34",
                      "0:36", "0:40", "0:48", "1:01", "1:02", "1:04",
                      "1:08", "1:16", "1:32", "2:01", "2:02", "2:04",
                      "2:08", "2:16", "2:32", "3:00", "4:01", ...
                      (44 total)

For k=0: checks 720 times to find just ONE ("0:00") 
         → 99.86% wasted iterations!

For k=8: checks 720 times to find just TWO ("7:31", "7:47")
         → 99.72% wasted iterations!
```

### Complexity Analysis

| Solution | Time | Space | Correct? | Why? |
|----------|------|-------|----------|------|
| Brute Force | O(720) | O(1) + output | ✅ Works | Checks all 12×60 combos, 93% wasted |
| Backtracking | O(C(10,k)) | O(k) stack | ✅ Works | Recursive choose-explore overhead |
| **Gosper's Hack** | **O(C(10,k))** | **O(1) + output** | **✅ Optimal** | **O(1) per step, pure arithmetic** |

> 💭 **"This works because 720 is tiny, but we're checking EVERY hour-minute combo blindly — 93% of iterations are wasted! The watch has 10 LEDs. Choosing `k` LEDs to turn on is exactly C(10,k) subsets. What if we directly pick WHICH LEDs to turn on, so we ONLY generate patterns with exactly k bits set?"**

---

## Solution 2: Backtracking — Choose k LEDs from 10 ❌

### The Natural Thought

"The binary watch has 10 LED positions. Turning on `k` LEDs is the same as choosing k positions from 10. I can use backtracking to enumerate all C(10,k) combinations — like LeetCode 77 (Combinations) or LeetCode 17 (Letter Combinations)."

Think of it as 10 switches in a row:

```
Position:   9    8    7    6    5    4    3    2    1    0
Meaning:  8hr  4hr  2hr  1hr  32m  16m  8m   4m   2m   1m
Switch:    □    □    □    □    □    □    □    □    □    □
           ↑─── hours ──↑    ↑───── minutes ─────────────↑
```

Choose exactly `k` switches to flip ON, then decode `hours + minutes` from the result.

### Approach

```java
public List<String> readBinaryWatch(int turnedOn) {
    List<String> res = new ArrayList<>();
    int[] values = {1, 2, 4, 8, 1, 2, 4, 8, 16, 32}; // 0-3=hours, 4-9=minutes
    backtrack(res, values, turnedOn, 0, 0, 0);
    return res;
}

void backtrack(List<String> res, int[] values, int k, int start, int hours, int mins) {
    if (k == 0) {
        if (hours < 12 && mins < 60)
            res.add(hours + ":" + (mins < 10 ? "0" : "") + mins);
        return;
    }
    for (int i = start; i < 10; i++) {
        // Choose
        if (i < 4) hours += values[i];
        else mins += values[i - 4 + 4]; // using values array offset
        
        // Explore
        backtrack(res, values, k - 1, i + 1, hours, mins);
        
        // Unchoose
        if (i < 4) hours -= values[i];
        else mins -= values[i - 4 + 4];
    }
}
```

### Example Trace for `turnedOn = 2`

```
backtrack(k=2, start=0, h=0, m=0)
├── Choose LED 0 (1hr): backtrack(k=1, start=1, h=1, m=0)
│   ├── Choose LED 1 (2hr): h=3,m=0 → "3:00" ✅
│   ├── Choose LED 2 (4hr): h=5,m=0 → "5:00" ✅
│   ├── Choose LED 3 (8hr): h=9,m=0 → "9:00" ✅
│   ├── Choose LED 4 (1min): h=1,m=1 → "1:01" ✅
│   ├── ... (6 more)
│   └── Choose LED 9 (32min): h=1,m=32 → "1:32" ✅
├── Choose LED 1 (2hr): backtrack(k=1, start=2, h=2, m=0)
│   ├── ...
│   └── 8 branches
├── ... (continues for all C(10,2) = 45 subsets)
```

### Why It's Not Ideal 🤯

The backtracking is **correct** and generates exactly C(10,k) subsets — no waste compared to brute force. But it has overhead:

```
For k=5 (worst case): C(10,5) = 252 subsets

Backtracking overhead per subset:
  - Up to 10 levels of recursion (stack depth = k = 5)
  - Each level: choose + recurse + unchoose = 3 operations
  - Total function calls ≈ sum of internal nodes in recursion tree
  - Roughly ~2500+ function calls for 252 leaves

Gosper's hack for same k=5:
  - 252 iterations × O(1) arithmetic = 252 ops total
  - No recursion, no stack frames, no choose/unchoose bookkeeping

Backtracking is ~10× more operations than necessary!
```

Also, the code is more complex: you need a `values[]` array, separate `hours`/`mins` tracking, and the recursive structure is harder to write bug-free under interview pressure.

### Complexity Analysis

| Solution | Time | Space | Correct? | Why? |
|----------|------|-------|----------|------|
| Brute Force | O(720) | O(1) + output | ✅ Works | 93% wasted iterations |
| Backtracking | O(C(10,k)) | O(k) stack | ✅ Works | Correct count but recursive overhead |
| **Gosper's Hack** | **O(C(10,k))** | **O(1) + output** | **✅ Optimal** | **O(1) per step, no recursion** |

> 💭 **"Backtracking generates exactly C(10,k) subsets — no waste! But each subset costs a full recursive call stack: choose, recurse, unchoose, unwinding. Can we iterate through ALL numbers that have exactly k bits set using pure arithmetic — no recursion, no arrays, no stack frames, just a single `while` loop with O(1) bitwise math per step?"**

---

## Solution 3: Gosper's Hack — Bit Enumeration ✅

### The Connection 🔗

Let's trace our thinking:
- **Brute Force** checked 720 combinations — 93% of iterations were wasted on combos with the wrong bit count
- **Backtracking** fixed the waste by generating only C(10,k) subsets — but used recursion, stack frames, and choose/unchoose bookkeeping (~10× overhead)
- **What we need**: iterate exactly C(10,k) bit patterns, each in O(1), with zero recursion
- → **Gosper's Hack**: a legendary bit manipulation trick (from the 1970s at MIT) that computes the **next integer with the same number of set bits** using pure arithmetic!

### The Key Insight 💡

> **Gosper's Hack**: Given a number `q` with exactly k bits set, you can compute the **next larger** number with exactly k bits set using 3 lines of bitwise math. Start from the smallest k-bit number `(1 << k) - 1` and iterate until you exceed the maximum (here, `1 << 10 = 1024`).

The formula:
```
r = q & -q           // Step 1: isolate the rightmost set bit
n = q + r            // Step 2: flip that bit and carry
q = (((n ^ q) >> 2) / r) | n   // Step 3: redistribute low bits
```

### Why This Works — Detailed Derivation

Let's trace through a concrete example to build intuition. Say we have `q = 0b00_0110` (decimal 6, two bits set). We want the next number with exactly 2 bits set.

```
q = 0b0110  (decimal 6)

┌─── Step 1: r = q & (-q) ─────────────────────────────────────┐
│  -q in two's complement = ~q + 1 = 0b1010                    │
│  q & (-q) = 0b0110 & 0b1010 = 0b0010                         │
│  r = 0b0010 (decimal 2) — the rightmost '1' bit              │
│                                                               │
│  What this does: isolates the LOWEST set bit of q.            │
│  q:    ...0 1 1 0                                             │
│             ↑                                                 │
│             └── rightmost '1' = position 1                    │
└───────────────────────────────────────────────────────────────┘

┌─── Step 2: n = q + r ────────────────────────────────────────┐
│  n = 0b0110 + 0b0010 = 0b1000 (decimal 8)                    │
│                                                               │
│  What this does: adds 1 at the rightmost set bit position,    │
│  which CARRIES through all consecutive 1s above it.           │
│                                                               │
│  Before: ...0 1 1 0    After: ...1 0 0 0                      │
│                ↑ ↑ add here     ↑ carry landed here           │
│                └─┘ consecutive   └ this '1' is "moved up"     │
│                  1s get cleared                                │
└───────────────────────────────────────────────────────────────┘

┌─── Step 3: q = (((n ^ q) >> 2) / r) | n ────────────────────┐
│  n ^ q = 0b1000 ^ 0b0110 = 0b1110 (bits that flipped)        │
│                                                               │
│  These flipped bits = the "cleared" 1s + the new carry bit.   │
│  We need to redistribute some of them to the LOWEST positions.│
│                                                               │
│  (n ^ q) >> 2 = 0b1110 >> 2 = 0b0011                         │
│  0b0011 / r = 0b0011 / 0b0010 = 0b0001 (1 remaining bit)     │
│                                                               │
│  The division by r right-shifts these bits down to position 0.│
│  This packs the "leftover" 1s to the very bottom.             │
│                                                               │
│  0b0001 | n = 0b0001 | 0b1000 = 0b1001 (decimal 9) ✅        │
│                                                               │
│  Result: q went from 0b0110 (6) to 0b1001 (9)                │
│  Both have exactly 2 bits set! ✅                             │
└───────────────────────────────────────────────────────────────┘
```

**Verification of the full k=2 sequence** (10-bit numbers):

```
0b0000000011 (  3) → 0b0000000101 (  5) → 0b0000000110 (  6)
→ 0b0000001001 (  9) → 0b0000001010 ( 10) → 0b0000001100 ( 12)
→ 0b0000010001 ( 17) → ... → 0b1100000000 (768) → STOP (next ≥ 1024)

Total: C(10,2) = 45 numbers generated, in ascending order, O(1) each!
```

### The Algorithm

```
1. Edge cases:
   - k = 0 → return ["0:00"] (no LEDs on, only time is midnight)
   - k > 8 → return [] (impossible: max valid = h=11 needs 3 bits + m=59 needs 5 bits = 8)

2. Start: q = (1 << k) - 1
   - This is the SMALLEST number with exactly k bits set
   - Example: k=3 → q = 0b111 = 7

3. While q < (1 << 10):     // 10 LEDs total
   a. Extract hour = q >> 6       (top 4 bits)
   b. Extract minute = q & 63     (bottom 6 bits, 0b111111 = 63)
   c. If hour < 12 AND minute < 60 → valid time, add to results
   d. Gosper's hack → compute next q:
      r = q & -q
      n = q + r
      q = (((n ^ q) >> 2) / r) | n

4. Return results
```

### Step-by-Step Walkthrough

**Input**: `turnedOn = 1`

```
Start: q = (1 << 1) - 1 = 1 = 0b0000000001
Limit: 1 << 10 = 1024

Binary Watch Bit Layout:
  Bit:  9   8   7   6   5   4   3   2   1   0
        ├─── hours ──┤   ├────── minutes ──────┤
        8h  4h  2h  1h  32m 16m  8m  4m  2m  1m

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

Iter 1:  q = 0b0000000001 = 1
         hour = 1 >> 6 = 0,  min = 1 & 63 = 1  → "0:01" ✅
         Gosper: r=1, n=2 → q = ((2^1)>>2)/1 | 2 = (3>>2)/1|2 = 0|2 = 2

Iter 2:  q = 0b0000000010 = 2
         hour = 0,  min = 2  → "0:02" ✅
         Gosper: r=2, n=4 → q = ((4^2)>>2)/2 | 4 = (6>>2)/2|4 = 1/2|4 = 0|4 = 4

Iter 3:  q = 0b0000000100 = 4
         hour = 0,  min = 4  → "0:04" ✅
         Gosper → q = 8

Iter 4:  q = 0b0000001000 = 8
         hour = 0,  min = 8  → "0:08" ✅
         Gosper → q = 16

Iter 5:  q = 0b0000010000 = 16
         hour = 0,  min = 16  → "0:16" ✅
         Gosper → q = 32

Iter 6:  q = 0b0000100000 = 32
         hour = 0,  min = 32  → "0:32" ✅
         Gosper → q = 64

Iter 7:  q = 0b0001000000 = 64
         hour = 64 >> 6 = 1,  min = 64 & 63 = 0  → "1:00" ✅
         Gosper → q = 128

Iter 8:  q = 0b0010000000 = 128
         hour = 128 >> 6 = 2,  min = 0  → "2:00" ✅
         Gosper → q = 256

Iter 9:  q = 0b0100000000 = 256
         hour = 256 >> 6 = 4,  min = 0  → "4:00" ✅
         Gosper → q = 512

Iter 10: q = 0b1000000000 = 512
         hour = 512 >> 6 = 8,  min = 0  → "8:00" ✅
         Gosper → q = 1024

q = 1024 ≥ 1024 → STOP

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

Result: ["0:01","0:02","0:04","0:08","0:16","0:32","1:00","2:00","4:00","8:00"]
Exactly C(10,1) = 10 iterations. Every single one was valid! ✅
```

### Visual Diagram

**Binary Watch LED Layout:**

```
┌──────────────────────────────────────────┐
│             BINARY WATCH                  │
│                                          │
│   HOURS (4 LEDs)    MINUTES (6 LEDs)     │
│  ┌───┬───┬───┬───┐ ┌───┬───┬───┬───┬───┬───┐
│  │ 8 │ 4 │ 2 │ 1 │ │32 │16 │ 8 │ 4 │ 2 │ 1 │
│  │b9 │b8 │b7 │b6 │ │b5 │b4 │b3 │b2 │b1 │b0 │
│  └───┴───┴───┴───┘ └───┴───┴───┴───┴───┴───┘
│                                          │
│  Example: q = 0b0001_110011              │
│  Hours:   0001 = 1                       │
│  Minutes: 110011 = 51                    │
│  Display: "1:51"                         │
└──────────────────────────────────────────┘
```

**Gosper's Hack Transitions for k=2 (first 12):**

```
  0b00_0000_0011 (0:03)    Two 1-bits in minute positions
         ↓ Gosper
  0b00_0000_0101 (0:05)    Bit shifts rightward
         ↓
  0b00_0000_0110 (0:06)    Adjacent bits
         ↓
  0b00_0000_1001 (0:09)    Gap widens
         ↓
  0b00_0000_1010 (0:10)
         ↓
  0b00_0000_1100 (0:12)
         ↓
  0b00_0001_0001 (0:17)    One bit crosses into next group
         ↓
      ... (33 more steps) ...
         ↓
  0b01_0100_0000 (5:00)    Both bits now in hour positions
         ↓
      ... 
         ↓
  0b11_0000_0000 (invalid: h=12 ≥ 12) ← skipped!
         ↓
  STOP (next q ≥ 1024)

  Total: C(10,2) = 45 iterations
  Valid times: 44 (1 filtered: h=12)
```

**Why k > 8 is impossible:**

```
Max valid hour:   11 = 0b1011 → 3 bits set (not 4!)
Max valid minute: 59 = 0b111011 → 5 bits set (not 6!)

Max LEDs on for a valid time = 3 + 5 = 8

  Hour 11: ●○●●  (3 LEDs)    Minute 59: ●●●○●● (5 LEDs)
           8 4 2 1              32 16 8  4 2  1

k=9 or k=10: IMPOSSIBLE to display a valid time
→ return [] immediately without any iteration!
```

### Key Optimizations in Code

1. **Edge cases first**: `k==0` returns immediately (no iteration needed). `k>8` returns empty immediately — avoids entering the loop at all. The maximum valid is `h=11` (3 bits) + `m=59` (5 bits) = 8 LEDs.

2. **`q >> 6` and `q & 63`**: Extracting hour and minute is just bit shifting and masking. No string parsing, no modular arithmetic. `63 = 0b111111` masks the bottom 6 bits (minute), and `>> 6` shifts away the minute bits to reveal the hour.

3. **`String.format` avoided**: The commented-out `String.format("%d:%02d", ...)` is ~5× slower than manual string concatenation `hour + ":" + (min < 10 ? "0" : "") + min`. For competitive programming, this matters.

4. **Gosper's hack in 3 lines**: The entire next-permutation logic is just `r`, `n`, and the reassignment to `q`. No helper functions, no temporary arrays, no recursion. Pure arithmetic: AND, XOR, shift, divide, OR.

---

## Complexity Analysis (Optimal)

| Solution | Time | Space | Status | Why |
|----------|------|-------|--------|-----|
| Brute Force (720) | O(720) | O(1) + output | ✅ Works | Fixed 720 iterations, 93% wasted |
| Backtracking | O(C(10,k)) | O(k) stack depth | ✅ Works | Correct count, recursive overhead |
| **Gosper's Hack** | **O(C(10,k))** | **O(1) + output** | **✅ Optimal** | **O(1) per step, no recursion** |

**Quantified for k=5 (worst case: C(10,5) = 252):**

```
Brute Force:   720 iterations × O(1) bitCount = 720 ops
               Only 252 have 5 bits → 468 wasted (65% waste)

Backtracking:  252 leaf calls + ~2500 internal calls ≈ 2750 function invocations
               Each call: push stack frame + choose + unchoose + pop
               Estimated: ~8000+ actual operations

Gosper's Hack: 252 iterations × O(1) arithmetic = 252 ops
               3 lines of bit math per iteration = ~756 arithmetic operations total
               No stack, no recursion, no arrays

Speedup: Gosper is ~3× fewer ops than brute force, ~10× fewer than backtracking
```

**Important context:** For this specific problem, ALL three approaches are blazing fast because 10 LEDs → max C(10,5) = 252. The differences are academic. But Gosper's hack is the **elegant interview answer** that demonstrates deep bit manipulation mastery.

---

## Key Takeaways

1. **Gosper's Hack** — The canonical trick for iterating all k-bit subsets of n bits: `r = q & -q; n = q + r; q = (((n^q)>>2)/r) | n`. Memorize it — it appears in competitive programming and interviews whenever you need "all combinations of exactly k items from n."

2. **Bit layout encoding** — Represent structured data (hours + minutes) as contiguous bit regions in a single integer. Top 4 bits = hours, bottom 6 bits = minutes. Decode with `>> 6` and `& 63`. This "pack data into bits" pattern appears in game state encoding, hash functions, and embedded systems.

3. **Know when brute force is fine** — 10 LEDs means max C(10,5) = 252 subsets. Even the O(720) brute force runs in microseconds. In interviews, acknowledge this ("brute force works here because n=10"), then show the optimal to demonstrate your skills. The brute force `bitCount` solution is actually the most common accepted answer on LeetCode for this problem.

---

## The Journey (TL;DR)

```
🐢 Enumerate all 720 h:m pairs → WORKS but 93% waste
         ↓
💡 "Why check all times? Pick which k LEDs to turn on directly!"
         ↓
❌ Backtracking (choose k from 10) → Correct but recursive overhead
         ↓
💡 "Can we iterate k-bit numbers with pure arithmetic, no recursion?"
         ↓
✅ Gosper's Hack → O(C(10,k)) with O(1) per step, 3 lines of bit magic!
```
