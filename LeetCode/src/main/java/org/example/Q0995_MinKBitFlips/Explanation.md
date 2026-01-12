# Minimum Number of K Consecutive Bit Flips - Explanation

> **Honorable Mention**: *Queue-based approach* stores flip positions in a queue and removes expired ones. Same O(n) complexity, but uses deque instead of XOR + array. Some find it more intuitive!

## Problem in Simple Words

You have a binary array (0s and 1s). You can **flip k consecutive bits** (0→1, 1→0).
Find the **minimum flips** to make ALL bits = 1. Return -1 if impossible.

**Example**: `nums = [0,0,0,1,0,1,1,0], k = 3`
- Flip [0,1,2]: `[1,1,1,1,0,1,1,0]`
- Flip [4,5,6]: `[1,1,1,1,1,0,0,0]`
- Flip [5,6,7]: `[1,1,1,1,1,1,1,1]`
- **Answer: 3 flips**

---

## Solution 1: Brute Force (Actually Flip) ❌ (Too Slow)

### Approach
Scan left to right. When we see a 0, flip the next k bits.

```java
for (int i = 0; i <= n - k; i++) {
    if (nums[i] == 0) {
        // Actually flip k bits
        for (int j = i; j < i + k; j++) {
            nums[j] ^= 1;
        }
        res++;
    }
}
// Check if any 0 remains
```

### Why It's Bad
- Each flip is **O(k)** work
- Total: **O(n × k)**
- n = 10⁵, k = 10⁵ → 10¹⁰ operations! TLE

> 💭 **Actually flipping k bits each time is expensive. What if we just TRACKED which positions have been flipped, instead of actually doing it?**

---

## Solution 2: Track Flips But Still Slow ❌

### The Natural Thought
"What if I track number of flips affecting each position instead of actually flipping?"

### Approach
```java
int[] flips = new int[n];
for (int i = 0; i < n; i++) {
    // Count how many flips affect position i
    int totalFlips = sum of flips[i-k+1] to flips[i];
    // ...
}
```

### Why It's Still Bad
- Computing sum for each position is O(k)
- Still O(n × k) overall

> 💭 **Summing a range is O(k). But wait — we don't need the ACTUAL count, just whether it's ODD or EVEN (parity)! That's much simpler to track.**

---

## Solution 3: Greedy + Sliding Window with XOR ✅ (Optimal)

### The Connection 🔗
Let's trace our thinking:
- **Brute Force** was slow because: actually flipping k bits = O(k) work each time
- **Tracking flips** was still slow because: summing range of flips = O(k)
- **Key insight**: We only care about PARITY (odd/even flips), not the count!

### The Key Insight 💡

Instead of tracking total flips, track the **parity** (odd/even) of flips affecting current position!

```
Why parity matters:
- 0 flips = original value
- 1 flip = flipped value
- 2 flips = original value (back to start!)
- 3 flips = flipped value
- ...

Only ODD/EVEN matters, not the actual count!
```

### The XOR Trick

```
flipped = current flip parity (0 = even flips, 1 = odd flips)

Effective value at position i = nums[i] XOR flipped

If effective value = 0 → we need to flip!
If effective value = 1 → already good!

When do we need to flip?
  nums[i] = 0, flipped = 0 → effective = 0 → FLIP!
  nums[i] = 1, flipped = 1 → effective = 0 → FLIP!
  
  Condition: flipped == nums[i] → need flip!
```

### Managing the Window

```
When we flip at position i, it affects positions [i, i+k-1].
When we reach position i+k, the flip at i is no longer active!

isFlipped[i] = 1 means "we started a flip at position i"
At position i, remove effect of flip from position i-k:
  flipped ^= isFlipped[i - k]
```

---

## Step-by-Step Walkthrough

**nums = `[0, 0, 0, 1, 0, 1, 1, 0]`, k = 3**

```
Index:    0   1   2   3   4   5   6   7
nums:   [ 0,  0,  0,  1,  0,  1,  1,  0 ]
```

---

### i = 0

```
Initial: flipped = 0

Remove old flip? i=0 < k=3 → NO

Effective value: nums[0]=0, flipped=0
  flipped == nums[i]? 0 == 0? YES → Need to flip!

Can we flip? i + k = 0 + 3 = 3 ≤ 8 → YES!

Action: isFlipped[0] = 1, flipped ^= 1 = 1, res = 1

State: flipped=1, res=1, isFlipped=[1,0,0,0,0,0,0,0]
```

---

### i = 1

```
Remove old flip? i=1 < k=3 → NO

Effective value: nums[1]=0, flipped=1
  flipped == nums[i]? 1 == 0? NO → Already good!

No flip needed.

State: flipped=1, res=1
```

---

### i = 2

```
Remove old flip? i=2 < k=3 → NO

Effective value: nums[2]=0, flipped=1
  flipped == nums[i]? 1 == 0? NO → Already good!

No flip needed.

State: flipped=1, res=1
```

---

### i = 3

```
Remove old flip? i=3 >= k=3 → YES!
  flipped ^= isFlipped[3-3] = isFlipped[0] = 1
  flipped = 1 ^ 1 = 0

Effective value: nums[3]=1, flipped=0
  flipped == nums[i]? 0 == 1? NO → Already good!

No flip needed.

State: flipped=0, res=1
```

The flip from position 0 has "slid out" of the window!

---

### i = 4

```
Remove old flip? i=4 >= k=3 → YES!
  flipped ^= isFlipped[4-3] = isFlipped[1] = 0
  flipped = 0 ^ 0 = 0

Effective value: nums[4]=0, flipped=0
  flipped == nums[i]? 0 == 0? YES → Need to flip!

Can we flip? i + k = 4 + 3 = 7 ≤ 8 → YES!

Action: isFlipped[4] = 1, flipped ^= 1 = 1, res = 2

State: flipped=1, res=2, isFlipped=[1,0,0,0,1,0,0,0]
```

---

### i = 5

```
Remove old flip? flipped ^= isFlipped[2] = 0 → no change

Effective value: nums[5]=1, flipped=1
  flipped == nums[i]? 1 == 1? YES → Need to flip!

Can we flip? i + k = 5 + 3 = 8 ≤ 8 → YES!

Action: isFlipped[5] = 1, flipped ^= 1 = 0, res = 3

State: flipped=0, res=3, isFlipped=[1,0,0,0,1,1,0,0]
```

---

### i = 6

```
Remove old flip? flipped ^= isFlipped[3] = 0 → no change

Effective value: nums[6]=1, flipped=0
  flipped == nums[i]? 0 == 1? NO → Already good!

State: flipped=0, res=3
```

---

### i = 7

```
Remove old flip? flipped ^= isFlipped[4] = 1
  flipped = 0 ^ 1 = 1

Effective value: nums[7]=0, flipped=1
  flipped == nums[i]? 1 == 0? NO → Already good!

State: flipped=1, res=3
```

---

### FINAL RESULT: 3 ✅

```
The 3 flips started at positions: 0, 4, 5

Simulation:
  Original:     [0, 0, 0, 1, 0, 1, 1, 0]
  Flip at 0:    [1, 1, 1, 1, 0, 1, 1, 0]
  Flip at 4:    [1, 1, 1, 1, 1, 0, 0, 0]
  Flip at 5:    [1, 1, 1, 1, 1, 1, 1, 1] ✓
```

---

## Visual: The Sliding Window Concept

```
When we're at position i, which flips affect us?
Flips started at [i-k+1, i-k+2, ..., i-1, i] affect position i

                     k=3 window
                    ┌─────────┐
  ... [flip] [flip] [current i] ...
       ↑      ↑         ↑
     i-2    i-1         i
     
When we move to i+1:
  - Flip at i-k+1 "slides out" (no longer affects us)
  - We XOR it out: flipped ^= isFlipped[i-k+1]
```

---

## Why XOR Works

```
XOR properties:
  0 ^ 0 = 0
  0 ^ 1 = 1
  1 ^ 0 = 1
  1 ^ 1 = 0

To add a flip: flipped ^= 1  (toggles state)
To remove a flip: flipped ^= 1  (toggles back)

Track "is position i flipped an odd number of times?"
  flipped = 0 → even flips (or zero) → original value
  flipped = 1 → odd flips → flipped value
```

---

## When is it Impossible?

```
At position i, if we need to flip but i + k > n:
  We can't fit a k-length window!
  Return -1

Example: nums = [1,1,0], k = 2
  At i=2, we need to flip the 0
  But i + k = 2 + 2 = 4 > 3
  Can't flip! Return -1
```

---

## Complexity Analysis

| Solution | Time | Space | Correct? | Why? |
|----------|------|-------|----------|------|
| Brute Force | O(n × k) | O(1) | ✅ TLE | Actually flips k bits |
| Track All Flips | O(n × k) | O(n) | ✅ TLE | Sums range each time |
| **XOR Sliding Window** | O(n) | O(n) | ✅ **Optimal** | O(1) parity tracking |

---

## Key Takeaways

1. **Parity matters, not count** — XOR tracks odd/even flips
2. **Sliding window** — Flip effect "enters" and "exits" as we move
3. **Greedy works** — If current bit is 0, we MUST flip here (no choice)
4. **isFlipped array** — Tracks where we started flips
5. **Condition `flipped == nums[i]`** — Means effective value is 0

---

## The Journey (TL;DR)

```
🐢 Brute Force: Actually flip k bits → TOO SLOW (O(n×k))
         ↓
💡 "Just track the flips, don't do them!"
         ↓
🔢 Track Flips: Sum range each time → STILL SLOW (O(n×k))
         ↓
💡 "We only need parity! XOR handles that in O(1)."
         ↓
✅ XOR Sliding Window: Toggle parity → OPTIMAL (O(n))
```
