# Reverse Bits - Explanation

> **Prerequisites**: Binary number representation, bitwise operators (`>>`, `<<`, `&`, `|`)  
> **Related Problems**:  
> - [LeetCode 191 - Number of 1 Bits](https://leetcode.com/problems/number-of-1-bits/) (Same `(n >> i) & 1` bit extraction technique — count 1-bits instead of reversing)  
> - [LeetCode 461 - Hamming Distance](https://leetcode.com/problems/hamming-distance/) (XOR two numbers, then count differing bits — same bit-by-bit analysis)  
> - [LeetCode 3314 - Construct Min Bitwise Array I](https://leetcode.com/problems/construct-the-minimum-bitwise-array-i/) | [Local](../Q3314_ConstructTheMinimumBitwiseArrayI/Explanation.md) (Bit manipulation to find min x where x OR (x+1) = n)  
> - [LeetCode 67 - Add Binary](https://leetcode.com/problems/add-binary/) | [Local](../Q0067_AddBinary/Explanation.md) (Binary string arithmetic — related binary processing)

---

## Problem in Simple Words

Given a 32-bit integer, flip its binary representation left-to-right — bit 0 goes to position 31, bit 1 to position 30, etc. For `n = 43261596` (binary `00000010100101000001111010011100`), reversing gives `964176192` (binary `00111001011110000010100101000000`).

---

## Solution 1: Brute Force (String Conversion) ❌

### The Natural Thought

"I know how to reverse a string. What if I convert the integer to its binary string, reverse that string, and parse it back to an integer?"

### Approach

```java
public int reverseBits(int n) {
    // Step 1: Convert to 32-char binary string (pad with leading zeros)
    String binary = String.format("%32s", Integer.toBinaryString(n)).replace(' ', '0');
    
    // Step 2: Reverse the string
    String reversed = new StringBuilder(binary).reverse().toString();
    
    // Step 3: Parse reversed string back to integer
    return (int) Long.parseLong(reversed, 2);  // use Long to handle unsigned
}
```

### Why It's Bad

The logic is correct, but it's wasteful — we're converting a number to text, manipulating text, and converting back to a number, when the operation is purely numerical.

### Example Where It's SLOW/Wasteful ❌

```
For n = 43261596, every single call does:

  1. Integer.toBinaryString(n) → allocates String "10100101000001111010011100"
  2. String.format("%32s", ...) → allocates String "      10100101000001111010011100"
  3. .replace(' ', '0')        → allocates String "00000010100101000001111010011100"
  4. new StringBuilder(...)    → allocates char[32] array
  5. .reverse()                → in-place on the char array
  6. .toString()               → allocates String "00111001011110000010100101000000"
  7. Long.parseLong(...)       → parses 32 characters one by one

  Total per call: ~5 object allocations + 32 chars to parse!

  For the follow-up ("called many times"):
    10,000,000 calls × 5 allocations = 50 MILLION temporary objects!
    GC pressure becomes a real bottleneck.

  Meanwhile, the actual work is just flipping 32 bits — something 
  bitwise operators can do with ZERO allocations.
```

### Complexity Analysis

| Solution | Time | Space | Correct? | Why? |
|----------|------|-------|----------|------|
| String Reversal | O(1) — 32 chars | O(1) — but 5+ allocs | ✅ Works | String/StringBuilder overhead, GC pressure |
| **Bit-by-Bit** | **O(1)** — 32 iters | **O(1)** — zero allocs | **✅ Optimal** | **Pure arithmetic, CPU register only** |
| D&C Swap | O(1) — 5 ops | O(1) | ✅ Fastest | Follow-up: constant 5 operations |

> 💭 **"We're creating strings just to reverse the order of 0s and 1s, then parsing back. But each 'character' is literally just a single bit. Can we extract bits directly from the integer using bitwise operators — without ever creating a string?"**

---

## Solution 2: Bit-by-Bit Extraction ✅ Optimal

### The Connection 🔗

Let's trace our thinking:
- **String conversion** worked but created ~5 temporary objects per call — we were treating a number problem as a text problem
- **What we need**: Extract each bit from `n` and place it in the mirror position in `rev`, using only bitwise arithmetic → **zero allocations, pure CPU register work!**

### The Key Insight 💡

> Think of `n` as a **queue** of bits (read from its right/LSB side) and `rev` as a **stack** (write to its right side, then shift everything left). Each iteration:
> 1. **Extract** the next bit from `n` using `(n >> i) & 1`
> 2. **Push** it into `rev` using `rev = (rev << 1) | bit`
>
> After 32 iterations, the first bit we read (bit 0 of `n`) has been shifted left 31 times → it's now at position 31 of `rev`. The last bit we read (bit 31 of `n`) stays at position 0. **Perfectly reversed!**

```
Why does the first bit end up at position 31?

  i=0:  rev = 0...0[bit₀]                     ← bit₀ at position 0
  i=1:  rev = 0...[bit₀][bit₁]                ← bit₀ shifted to position 1
  i=2:  rev = 0..[bit₀][bit₁][bit₂]           ← bit₀ shifted to position 2
  ...
  i=31: rev = [bit₀][bit₁]...[bit₃₀][bit₃₁]  ← bit₀ at position 31! ✅

  Each left-shift pushes bit₀ one position further left.
  After 31 more shifts, bit₀ is at the leftmost position (31).
```

### The Algorithm

```
1. Initialize rev = 0
2. For i = 0 to 31:
   a. Extract bit i from n:   bit = (n >> i) & 1
   b. Make room in rev:       rev = rev << 1
   c. Place the bit:          rev = rev | bit
   (Steps b and c combined:   rev = (rev << 1) | bit)
3. Return rev
```

### Step-by-Step Walkthrough

**Input**: `n = 43261596`

```
n in binary (32 bits):
  Position: 31 30 29 ... 5  4  3  2  1  0
  Bits:      0  0  0 ... 1  1  1  0  0  0
                                ↑ ↑ ↑ ↑ ↑
  Full:     00000010 10010100 00011110 10011100

We read from bit 0 (rightmost) of n, and build rev from right to left:

┌─────┬────────────────┬─────────┬──────────────────────────────────┐
│  i  │ (n >> i) & 1   │  bit    │ rev (shown as growing binary)   │
├─────┼────────────────┼─────────┼──────────────────────────────────┤
│  0  │ ...10011100>>0 │    0    │ rev = (0<<1)|0 = 0              │
│     │  & 1 = 0       │         │ rev = ...0                      │
│  1  │ ...10011100>>1 │    0    │ rev = (0<<1)|0 = 0              │
│     │  & 1 = 0       │         │ rev = ...00                     │
│  2  │ ...10011100>>2 │    1    │ rev = (0<<1)|1 = 1              │
│     │  & 1 = 1       │         │ rev = ...001                    │
│  3  │ ...10011100>>3 │    1    │ rev = (1<<1)|1 = 3              │
│     │  & 1 = 1       │         │ rev = ...0011                   │
│  4  │ ...10011100>>4 │    1    │ rev = (3<<1)|1 = 7              │
│     │  & 1 = 1       │         │ rev = ...00111                  │
│  5  │ ...10011100>>5 │    0    │ rev = (7<<1)|0 = 14             │
│     │  & 1 = 0       │         │ rev = ...001110                 │
│  6  │ ...10011100>>6 │    0    │ rev = (14<<1)|0 = 28            │
│     │  & 1 = 0       │         │ rev = ...0011100                │
│  7  │ ...10011100>>7 │    1    │ rev = (28<<1)|1 = 57            │
│     │  & 1 = 1       │         │ rev = ...00111001               │
│ ... │  ... continues for all 32 bits ...                         │
│ 31  │ ...00000010>>31│    0    │ rev = (...<<1)|0 = 964176192    │
│     │  & 1 = 0       │         │ rev = 00111001011110000010...00 │
└─────┴────────────────┴─────────┴──────────────────────────────────┘

Final rev = 964176192 ✅
```

### Visual Diagram

**How bit extraction + placement works**:

```
EXTRACTING bit i from n:
═══════════════════════════════════
n:     0 0 0 0 0 0 1 0 1 0 0 1 0 1 0 0 0 0 0 1 1 1 1 0 1 0 0 1 1 1 0 0
       bit31                                                        bit0
                                                                      ↑
       Step 1: n >> i    (shift right to bring bit i to position 0)
       Step 2: & 1       (mask everything except the last bit)

       Example — extract bit 2:
       n >> 2:  0 0 0 0 0 0 0 0 1 0 1 0 0 1 0 1 0 0 0 0 0 1 1 1 1 0 1 0 0 1 1 1
       & 1:                                                                     1
       bit = 1 ✅


BUILDING rev by pushing bits:
═══════════════════════════════════
       i=0: rev =                                                             0
       i=1: rev =                                                           0 0
       i=2: rev =                                                         0 0 1
       i=3: rev =                                                       0 0 1 1
       i=4: rev =                                                     0 0 1 1 1
       i=5: rev =                                                   0 0 1 1 1 0
       i=6: rev =                                                 0 0 1 1 1 0 0
       i=7: rev =                                               0 0 1 1 1 0 0 1
       ...
       i=31: rev = 0 0 1 1 1 0 0 1 0 1 1 1 1 0 0 0 0 0 1 0 1 0 0 1 0 1 0 0 0 0 0 0
              ↑                                                                     ↑
            bit₀ of n                                                        bit₃₁ of n
            (landed at pos 31)                                          (landed at pos 0)

       Each << 1 pushes ALL existing bits one position LEFT.
       The | bit places the new bit at position 0 (rightmost).
       After 32 iterations, bit₀ has been shifted left 31 times → position 31!


THE MIRROR:
═══════════════════════════════════
n:     0 0 0 0 0 0 1 0 1 0 0 1 0 1 0 0 0 0 0 1 1 1 1 0 1 0 0 1 1 1 0 0
       ↑ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ↑
       │                    mirror                                       │
       ↓ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ─ ↓
rev:   0 0 1 1 1 0 0 1 0 1 1 1 1 0 0 0 0 0 1 0 1 0 0 1 0 1 0 0 0 0 0 0
```

### Verification with Example 2

```
n = 2147483644
Binary: 01111111 11111111 11111111 11111100
                                        ↑↑
                            Last 2 bits: 00

Reversed: 00111111 11111111 11111111 11111110
          ↑↑                            ↑↑
  First 2 bits: 00            Last bit: 0 (from n's bit 31=0)
                              Bit 1: 1 (from n's bit 30=1)

rev = 1073741822 ✅

Makes sense: n has 0s at positions 0,1 and 31 → 
rev has 0s at positions 31,30 and 0.
The 28 ones in the middle stay in the middle (symmetric!).
```

---

> **Honorable Mention: Divide & Conquer Swap** (Follow-Up Answer)
>
> The follow-up asks: "If called many times, how would you optimize?"
>
> **Approach**: Instead of 32 loop iterations, use 5 fixed mask-shift-OR operations to swap increasingly smaller blocks of bits:
>
> ```java
> // Swap 16-bit halves
> n = ((n & 0xFFFF0000) >>> 16) | ((n & 0x0000FFFF) << 16);
> // Swap 8-bit chunks within each half
> n = ((n & 0xFF00FF00) >>> 8)  | ((n & 0x00FF00FF) << 8);
> // Swap 4-bit nibbles
> n = ((n & 0xF0F0F0F0) >>> 4)  | ((n & 0x0F0F0F0F) << 4);
> // Swap 2-bit pairs
> n = ((n & 0xCCCCCCCC) >>> 2)  | ((n & 0x33333333) << 2);
> // Swap adjacent bits
> n = ((n & 0xAAAAAAAA) >>> 1)  | ((n & 0x55555555) << 1);
> ```
>
> ```
> Visual (8-bit example, extends to 32):
> 
> Original:     [A B C D E F G H]
> Swap halves:  [E F G H │ A B C D]    ← swap 4-bit halves
> Swap pairs:   [G H E F │ C D A B]    ← swap 2-bit pairs
> Swap singles: [H G F E │ D C B A]    ← swap adjacent bits
> Result:       [H G F E D C B A]      ← fully reversed!
> ```
>
> **Why it's better for millions of calls**: Only 5 operations (no loop, no branch), fully pipelined by CPU. Another option: precompute a 256-entry byte-reversal lookup table, reverse 4 bytes independently → O(1) with a single table lookup per byte.

---

## Complexity Analysis (Optimal)

| Solution | Time | Space | Status | Why |
|----------|------|-------|--------|-----|
| String Reversal | O(1) — 32 chars | O(1) — but 5+ allocs | ✅ Works | String objects + GC overhead |
| **Bit-by-Bit Loop** | **O(1)** — 32 iters | **O(1)** — zero allocs | **✅ Optimal** | **Pure bitwise, CPU registers only** |
| D&C Swap | O(1) — 5 ops | O(1) | ✅ Fastest | Follow-up: no loop, 5 mask-shift-OR |

**Quantified**:
- All solutions are O(1) — always exactly 32 bits to process
- **String**: ~5 object allocations per call → 10M calls = 50M temp objects → GC pauses
- **Bit-by-bit**: 32 × 3 bitwise ops = **96 CPU instructions** → ~0.00001ms → no allocations
- **D&C**: 5 × 3 bitwise ops = **15 CPU instructions** → ~6× fewer ops than loop
- Space: just 2 integer variables (n and rev) → **8 bytes** total

---

## Key Takeaways

1. **Bit extraction pattern: `(n >> i) & 1`**. This is THE fundamental building block for bit manipulation. Shift the target bit to position 0, then mask with `& 1`. Used in LC 191 (count 1-bits), LC 461 (hamming distance), and virtually every bit manipulation problem.

2. **Build-by-shift pattern: `rev = (rev << 1) | bit`**. This is the "push onto result" idiom for bits. Left-shift makes room at the LSB, OR places the new bit. It's analogous to `result = result * 10 + digit` for decimal digits — same concept, different base.

3. **Avoid string conversion for numeric operations**. When the problem is purely about the number's structure (bits, digits), stay in the numeric domain. String conversion adds allocation overhead and obscures the direct relationship between input and output.

---

## The Journey (TL;DR)

```
🐢 String Reversal → Works but 5+ object allocations per call!
         ↓
💡 "Each 'character' is just a bit. Can we use bitwise ops directly?"
         ↓
✅ Bit-by-Bit Loop → 32 iterations, zero allocations, O(1)
         ↓
💡 "Follow-up: What if called millions of times?"
         ↓
⚡ D&C Swap → Only 5 mask-shift-OR ops, no loop!
```
