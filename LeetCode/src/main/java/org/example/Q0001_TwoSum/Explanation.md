# Two Sum - Explanation

## Problem in Simple Words
You have a bag of numbers. Find **two numbers** that add up to a **target sum**. Return their positions (indices).

**Example**: `nums = [2, 7, 11, 15]`, `target = 9`
- 2 + 7 = 9 ✅
- Answer: `[0, 1]`

---

## Solution 1: Brute Force ❌ (Works but Slow)

### Approach
Check every possible pair of numbers:

```java
for (int i = 0; i < nums.length; i++) {
    for (int j = i + 1; j < nums.length; j++) {
        if (nums[i] + nums[j] == target) {
            return new int[]{i, j};
        }
    }
}
```

### Why It's Bad
- **Two nested loops** = Check ALL pairs
- 10,000 numbers → 10,000 × 10,000 = **100 million operations!**
- Like asking everyone in a stadium to shake hands with everyone else

> 💭 **OK so we're spending O(n) time for each element just to SEARCH for its complement. What if the array was sorted? Then we could use two pointers to find pairs faster...**

---

## Solution 2: Sorting + Two Pointers ❌ (Wrong for This Problem)

### The Natural Thought
"Two pointers on a sorted array can find a target sum in O(n)! That's way better than O(n²)."

### Approach
```java
Arrays.sort(nums);  // Sort first
int left = 0, right = nums.length - 1;
while (left < right) {
    int sum = nums[left] + nums[right];
    if (sum == target) return [left, right];  // WRONG!
    else if (sum < target) left++;
    else right--;
}
```

### Example Where It WORKS ✅

```
nums = [3, 2, 4], target = 6

After sorting: [2, 3, 4]

left=0, right=2: 2 + 4 = 6 ✅ Found!

Return [0, 2]... but wait!
```

### Example Where It FAILS ❌

```
Original: [3, 2, 4], target = 6
Indices:   0  1  2

After sorting: [2, 3, 4]
New indices:    0  1  2   ← INDICES CHANGED!

We find 2 + 4 = 6 at sorted positions [0, 2]
But in ORIGINAL array:
  - 2 was at index 1
  - 4 was at index 2

Correct answer: [1, 2]
Our answer: [0, 2] ← WRONG!
```

### Why It Fails 🤯
**Sorting changes the indices!** The problem asks for ORIGINAL positions, not sorted positions.

You could track original indices, but that adds complexity.

> 💭 **Hmm... sorting helps us FIND pairs faster, but destroys the indices we need to return. We need O(1) lookup WITHOUT changing positions. What data structure gives us O(1) lookup?**

---

## Solution 3: HashMap ✅ (Optimal)

### The Connection 🔗
Let's trace our thinking:
- **Brute Force** was slow because: searching for complement is O(n) per element
- **Sorting** couldn't work because: we need ORIGINAL indices, which sorting destroys
- **What we need**: O(1) lookup + preserve original positions → **HashMap!**

### The Key Insight 💡
Instead of searching for pairs, use a **HashMap as a cheat sheet**.
For each number, ask: "Have I already seen the number that completes my pair?"

### Why It Works
```
Brute Force:           HashMap:
   ↓                      ↓
"Check ALL pairs"    "Check my cheat sheet"
O(n) search each     O(1) lookup each
O(n²) total          O(n) total
```

### Step-by-Step Walkthrough

**nums = [2, 7, 11, 15], target = 9**

```
HashMap (our cheat sheet): {}

═══════════════════════════════════════════
STEP 1: Look at nums[0] = 2
═══════════════════════════════════════════
What do I need? target - 2 = 7
Is 7 in my HashMap? NO (it's empty)
Add 2 to HashMap: {2: 0}

═══════════════════════════════════════════
STEP 2: Look at nums[1] = 7
═══════════════════════════════════════════
What do I need? target - 7 = 2
Is 2 in my HashMap? YES! At index 0!

FOUND! Return [0, 1]
```

### Visual Diagram

```
Array:    [2,    7,    11,    15]
Index:     0     1      2      3
           ↓
           └── "I need 7 to make 9"
               HashMap: {2: 0}
                  ↓
                  └── "Someone already needs ME (2)!"
                      Return [0, 1]
```

---

## Complexity Analysis

| Solution | Time | Space | Correct? | Why? |
|----------|------|-------|----------|------|
| Brute Force | O(n²) | O(1) | ✅ But slow | O(n) search per element |
| Sorting + Two Pointers | O(n log n) | O(1) | ❌ Wrong | Loses original indices |
| **HashMap** | O(n) | O(n) | ✅ **Optimal** | O(1) lookup, preserves indices |

---

## Key Takeaways

1. **HashMap = O(1) lookup** → Perfect for "find complement" problems
2. **Sorting loses information** (original indices) — use with caution when indices matter
3. Trade **space for time** → O(n) space gives O(n) time
4. Pattern: "Have I seen X before?" → HashMap!

---

## The Journey (TL;DR)

```
🐢 Brute Force: Check all pairs → TOO SLOW (O(n²))
         ↓
💡 "Can we search faster? What about sorting?"
         ↓
🔀 Sorting + Two Pointers: Fast search → WRONG (loses indices)
         ↓
💡 "We need O(1) lookup WITHOUT changing positions..."
         ↓
✅ HashMap: O(1) lookup + preserves indices → OPTIMAL!
```
