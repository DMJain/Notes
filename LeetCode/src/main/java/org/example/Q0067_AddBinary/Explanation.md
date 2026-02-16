# Add Binary - Explanation

> **Prerequisites**: Binary number system (base-2 arithmetic: 0+0=0, 0+1=1, 1+1=10), string traversal from end  
> **Related Problems**:  
> - [LeetCode 415 - Add Strings](https://leetcode.com/problems/add-strings/) (Identical pattern but base-10 instead of base-2 — same two-pointer + carry approach)  
> - [LeetCode 2 - Add Two Numbers](https://leetcode.com/problems/add-two-numbers/) (Same digit-by-digit carry addition, but on linked lists instead of strings)  
> - [LeetCode 43 - Multiply Strings](https://leetcode.com/problems/multiply-strings/) (String arithmetic without int conversion — harder extension of same idea)

---

## Problem in Simple Words

Add two binary numbers given as strings and return the result as a string. Like elementary school column addition, but in base 2 where `1 + 1 = 10` (carry the 1).

Example: `"1010" + "1011" = "10101"` (that's 10 + 11 = 21 in decimal).

---

## Solution 1: Integer Conversion ❌

### The Natural Thought

"Binary strings? Just convert them to integers, add them, and convert the result back to binary."

### Approach

```java
// Convert binary strings to integers, add, convert back
return Integer.toBinaryString(Integer.parseInt(a, 2) + Integer.parseInt(b, 2));
```

One line. Clean. But there's a fatal problem.

### Why It's Bad

Java `int` holds max **31 binary digits** (2³¹ − 1). Java `long` holds max **63 binary digits** (2⁶³ − 1). But the constraint says strings can be up to **10,000 characters long**!

### Example Where It FAILS ❌

```
Input: a = "111...1" (64 ones), b = "1"

Java long max = 2^63 - 1 = 63 binary digits
a has 64 binary digits → OVERFLOW!

Expected: "1000...0" (1 followed by 64 zeros)
Got: NumberFormatException!

For a.length = 10000:
  Number has 10000 binary digits ≈ 3010 decimal digits
  Java long max = 19 decimal digits
  10000 / 63 = 158× too large for even a long!
  
  Even BigInteger "works" but:
  - Violates the spirit of the problem (interviewer wants manual addition)
  - BigInteger arithmetic internally uses O(n²) for large numbers
```

### Complexity Analysis

| Solution | Time | Space | Correct? | Why? |
|----------|------|-------|----------|------|
| int() convert | O(n) | O(n) | ❌ Overflow | Breaks for n > 63 binary digits |
| R→L insert(0) | O(n²) | O(n) | ✅ TLE risk | O(n) per insert shifts all chars |
| **R→L append+rev** | **O(n)** | **O(n)** | **✅ Optimal** | **Single pass, O(1) per digit** |

> 💭 **"Integer conversion breaks for strings longer than 63 characters because numbers overflow. We need to work with characters directly — digit by digit. What if we simulate column addition like we do by hand, adding each pair of digits from right to left with a carry?"**

---

## Solution 2: Right-to-Left with `insert(0)` ❌

### The Natural Thought

"OK, process chars directly. Start from the rightmost digit. Add digits with carry. Since the most significant bit comes first in the result, I'll insert each new digit at position 0 of my StringBuilder."

### Approach

```java
StringBuilder sb = new StringBuilder();
int carry = 0, i = a.length() - 1, j = b.length() - 1;

while (i >= 0 || j >= 0 || carry > 0) {
    int t = carry;
    if (i >= 0) t += a.charAt(i--) - '0';
    if (j >= 0) t += b.charAt(j--) - '0';
    sb.insert(0, t % 2);  // ← INSERT AT FRONT: shifts everything!
    carry = t / 2;
}
return sb.toString();
```

The logic is correct! But it has a hidden performance trap.

### Example Where It's SLOW ❌

```
Input: a = "111...1" (n = 10000 ones), b = "1"

StringBuilder.insert(0, ch) must SHIFT all existing characters right by 1.

Step 1:    insert '0' → shift 0 chars    → sb = "0"
Step 2:    insert '0' → shift 1 char     → sb = "00"
Step 3:    insert '0' → shift 2 chars    → sb = "000"
...
Step 9999: insert '1' → shift 9998 chars → sb = "1000...0"
Step 10000: insert '1' → shift 9999 chars → sb = "10000...0"

Total character shifts = 0 + 1 + 2 + ... + 9999
                       = 10000 × 9999 / 2
                       = 49,995,000 shifts!
                       ≈ 50 MILLION character moves!

vs. append + reverse: 10000 appends + 1 reverse = ~20,000 ops
insert(0) is 2,500× slower!
```

### Why It's Slow 🤯

`StringBuilder.insert(0, ch)` is O(n) — it must shift every existing character one position to the right to make room at the front. Doing this n times gives O(n²) total work. The addition logic itself is fine — it's just the wrong data structure operation.

### Complexity Analysis

| Solution | Time | Space | Correct? | Why? |
|----------|------|-------|----------|------|
| int() convert | O(n) | O(n) | ❌ Overflow | n > 63 → crash |
| R→L insert(0) | O(n²) | O(n) | ✅ TLE risk | 50M shifts for n=10000 |
| **R→L append+rev** | **O(n)** | **O(n)** | **✅ Optimal** | **Single pass** |

> 💭 **"The addition logic is right, but `insert(0)` shifts the entire string every time — O(n) per step, O(n²) total. Appending to the END is O(1). What if we just `append` each digit to the end and reverse the whole result once at the end?"**

---

## Solution 3: Two-Pointer + Append + Reverse ✅

### The Connection 🔗

Let's trace our thinking:
- **int() conversion** was simple but **overflows** for strings > 63 chars — can't handle the constraint of n=10,000
- **R→L with insert(0)** got the addition logic right but used the **wrong operation** — `insert(0)` is O(n) per call → O(n²) total = 50M shifts for n=10,000
- **What we need**: Same right-to-left carry logic, but build the result in O(1) per digit → **append to end, then reverse once!**

### The Key Insight 💡

> `append()` is O(1) amortized. `insert(0)` is O(n). Build the result string backwards with `append`, then call `reverse()` once at the very end. Total cost: n × O(1) appends + 1 × O(n) reverse = **O(n)**.

This is exactly how you add numbers by hand in school:
1. Write digits right-to-left into the answer
2. Read the final answer left-to-right

### The Algorithm

```
1. Initialize: i = len(a) - 1, j = len(b) - 1, carry = 0, result = StringBuilder
2. While i >= 0 OR j >= 0 OR carry > 0:
   a. t = carry
   b. If i >= 0: t += a[i] as digit (0 or 1), i--
   c. If j >= 0: t += b[j] as digit (0 or 1), j--
   d. result.append(t % 2)   ← O(1) append (digit is 0 or 1)
   e. carry = t / 2           ← carry is 0 or 1
3. result.reverse()            ← O(n) one-time reversal
4. Return result as string
```

**Why the loop condition works**: `i >= 0 || j >= 0 || c > 0` handles THREE cases in one:
- Strings of different lengths (one pointer exhausted, other still going)
- Final carry (both exhausted, but carry = 1 → must prepend "1")
- No special-casing needed!

### Step-by-Step Walkthrough

**Input**: `a = "1010", b = "1011"` → Expected: `"10101"`

```
Initial:  a = "1 0 1 0"     b = "1 0 1 1"     carry = 0
               0 1 2 3           0 1 2 3
                     i=3               j=3

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

Step 1:  t = 0 + a[3]('0'→0) + b[3]('1'→1) = 1
         digit = 1 % 2 = 1,  carry = 1 / 2 = 0
         result = [1]
         i=2, j=2

Step 2:  t = 0 + a[2]('1'→1) + b[2]('1'→1) = 2
         digit = 2 % 2 = 0,  carry = 2 / 2 = 1
         result = [1, 0]
         i=1, j=1

Step 3:  t = 1 + a[1]('0'→0) + b[1]('0'→0) = 1
         digit = 1 % 2 = 1,  carry = 1 / 2 = 0
         result = [1, 0, 1]
         i=0, j=0

Step 4:  t = 0 + a[0]('1'→1) + b[0]('1'→1) = 2
         digit = 2 % 2 = 0,  carry = 2 / 2 = 1
         result = [1, 0, 1, 0]
         i=-1, j=-1

Step 5:  i < 0, j < 0, but carry = 1!
         t = 1
         digit = 1 % 2 = 1,  carry = 1 / 2 = 0
         result = [1, 0, 1, 0, 1]
         DONE (i < 0, j < 0, carry = 0)

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

Reverse: [1, 0, 1, 0, 1] → "10101" ✅
```

### Visual Diagram

```
         a:  ┌───┬───┬───┬───┐
             │ 1 │ 0 │ 1 │ 0 │
             └───┴───┴───┴─↑─┘
                            i (starts at end)

         b:  ┌───┬───┬───┬───┐
             │ 1 │ 0 │ 1 │ 1 │
             └───┴───┴───┴─↑─┘
                            j (starts at end)

              ←── process right to left ──

  Step   a[i]   b[j]   carry   t   digit   new_carry   result[]
  ─────  ─────  ─────  ─────  ───  ─────   ─────────   ──────────
    1      0      1      0     1     1        0         [1]
    2      1      1      0     2     0        1         [1,0]
    3      0      0      1     1     1        0         [1,0,1]
    4      1      1      0     2     0        1         [1,0,1,0]
    5      —      —      1     1     1        0         [1,0,1,0,1]

  Reverse: "10101" ✅
```

**Binary column addition visual**:
```
        1  0  1  0       (a = "1010")
    +   1  0  1  1       (b = "1011")
    ─────────────────
  carry: 1     1         (carries at positions 2 and 4)
    ─────────────────
     1  0  1  0  1       = "10101" ✅
```

### Key Optimizations in Code

1. **`t % 2` and `t / 2`**: `t` can only be 0, 1, 2, or 3 (max: digit 1 + digit 1 + carry 1 = 3). So `t % 2` gives the current digit (0 or 1), and `t / 2` gives the carry (0 or 1). Clean integer math, no if-else branching needed.

2. **Single `while` loop**: The condition `i >= 0 || j >= 0 || c > 0` handles unequal string lengths AND final carry elegantly. When one string is exhausted, its pointer is negative so its digit is skipped (treated as 0). When both are done but carry remains, one more iteration emits the carry. Zero special cases.

3. **`append` + `reverse`**: O(1) amortized per `append` × n iterations + O(n) `reverse` = O(n) total. vs. `insert(0)` which costs O(n) per call × n iterations = O(n²). For n=10,000: 20,000 ops vs. 50,000,000 ops — **2,500× faster**.

---

## Complexity Analysis (Optimal)

| Solution | Time | Space | Status | Why |
|----------|------|-------|--------|-----|
| int() conversion | O(n) | O(n) | ❌ Overflow | Can't handle n > 63 binary digits |
| R→L insert(0) | O(n²) | O(n) | ✅ TLE risk | 50M char shifts for n=10000 |
| **R→L append+rev** | **O(max(m,n))** | **O(max(m,n))** | **✅ Optimal** | **n appends + 1 reverse = O(n)** |

**Quantified for n = 10,000 (worst case)**:
- **Optimal**: 10,000 iterations × O(1) + 1 reverse × O(10,000) = **~20,000 ops** → instant ✅
- **insert(0)**: 10,000 iterations × O(5,000 avg shift) = **50,000,000 ops** → 2,500× slower
- **int() conversion**: `NumberFormatException` → crash ❌

---

## Key Takeaways

1. **Right-to-left for carry-based addition**: Carry flows from least significant bit (right) to most significant bit (left). Always process in the direction of carry flow — right to left.

2. **`append` + `reverse` > `insert(0)`**: Classic StringBuilder performance trap. `insert(0)` is O(n) per call (shifts everything), `append` is O(1) amortized. When building a string in reverse, always append then reverse once.

3. **Common pitfall — don't forget final carry**: For `"11" + "1"` the answer is `"100"` — that leading `1` comes from carry after all digits are done. The `|| c > 0` in the while condition catches this. Without it, `"11" + "1"` would give `"00"` → reversed `"00"` → wrong!

---

## The Journey (TL;DR)

```
🐢 int() conversion → OVERFLOW (n > 63 binary digits!)
         ↓
💡 "Process chars directly, digit by digit?"
         ↓
❌ R→L + insert(0) → O(n²) (50M shifts for n=10000!)
         ↓
💡 "append is O(1), just reverse once at end?"
         ↓
✅ R→L append+reverse → O(n) single pass, carry flows naturally!
```
