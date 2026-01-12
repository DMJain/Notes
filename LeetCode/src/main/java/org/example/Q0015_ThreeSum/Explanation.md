# 3Sum - Explanation

## Problem in Simple Words
Find all **triplets** in an array that sum to **zero**. No duplicate triplets allowed!

**Example**: `nums = [-1, 0, 1, 2, -1, -4]`
- `[-1, 0, 1]` → -1 + 0 + 1 = 0 ✓
- `[-1, -1, 2]` → -1 + (-1) + 2 = 0 ✓
- **Answer: [[-1,-1,2], [-1,0,1]]**

---

## Solution 1: Brute Force (Three Loops) ❌ (Too Slow)

### Approach
Check every possible triplet.

```java
for (int i = 0; i < n; i++) {
    for (int j = i + 1; j < n; j++) {
        for (int k = j + 1; k < n; k++) {
            if (nums[i] + nums[j] + nums[k] == 0) {
                // Add triplet (avoid duplicates somehow)
            }
        }
    }
}
```

### Why It's Bad
- **O(n³)** time complexity
- n = 3000 → 27 billion operations!
- Also need a Set to handle duplicates → messy

> 💭 **Three nested loops is brutal. We've seen Two Sum use a HashMap for O(1) lookup. Can we apply the same trick here — fix one number and find the other two?**

---

## Solution 2: HashMap for Third Element ❌ (Works, But Duplicates Are Messy)

### The Natural Thought
"For each pair (i, j), the third element we need is `-(nums[i] + nums[j])`. That's just like Two Sum! Use a HashMap to find it in O(1)."

### Approach
```java
for (int i = 0; i < n; i++) {
    Set<Integer> seen = new HashSet<>();
    for (int j = i + 1; j < n; j++) {
        int complement = -(nums[i] + nums[j]);
        if (seen.contains(complement)) {
            // Found triplet!
        }
        seen.add(nums[j]);
    }
}
```

### Why It's Not Ideal
- **O(n²)** time — good!
- But **handling duplicates is tricky**
- Need extra logic (sorting triplets, using a Set) to avoid `[-1,0,1]` and `[-1,0,1]` appearing twice
- Gets messy with edge cases

> 💭 **The HashMap approach works but duplicate handling is painful. What if the array was SORTED? Then duplicates would be ADJACENT and easy to skip. Plus, a sorted array lets us use two pointers instead of HashMap...**

---

## Solution 3: Sort + Two Pointers ✅ (Optimal)

### The Connection 🔗
Let's trace our thinking:
- **Brute Force** was slow because: checking all O(n³) triplets
- **HashMap** worked but was messy because: duplicates scattered everywhere
- **What we need**: O(n²) time + easy duplicate handling → **Sort first!**

### The Key Insight 💡
1. **Sort the array** first
2. **Fix one element** (nums[i])
3. Use **two pointers** (l, r) to find pairs that sum to -nums[i]
4. **Skip duplicates** easily because array is sorted!

### Why Sorting Helps

```
Before sort: [-1, 0, 1, 2, -1, -4]
After sort:  [-4, -1, -1, 0, 1, 2]

Duplicates are now ADJACENT!
Easy to skip: if nums[i] == nums[i-1], skip i
```

### The Algorithm

```
1. Sort nums
2. For each i from 0 to n-2:
     - If nums[i] > 0: break (can't sum to 0 with all positives)
     - If nums[i] == nums[i-1]: skip (avoid duplicate first element)
     
     - Set l = i+1, r = n-1
     - While l < r:
         sum = nums[i] + nums[l] + nums[r]
         if sum > 0: r-- (need smaller)
         if sum < 0: l++ (need bigger)
         if sum == 0:
             Add triplet
             l++
             Skip duplicates: while nums[l] == nums[l-1]: l++
```

---

## Step-by-Step Walkthrough

**nums = `[-1, 0, 1, 2, -1, -4]`**

### Step 1: Sort

```
Before: [-1, 0, 1, 2, -1, -4]
After:  [-4, -1, -1, 0, 1, 2]
         0   1   2  3  4  5
```

---

### i = 0: nums[i] = -4

```
Target: find two numbers that sum to 4

l = 1, r = 5
nums: [-4, -1, -1, 0, 1, 2]
       i   l           r

-1 + 2 = 1 < 4 → l++

l = 2, r = 5
-1 + 2 = 1 < 4 → l++

l = 3, r = 5
0 + 2 = 2 < 4 → l++

l = 4, r = 5
1 + 2 = 3 < 4 → l++

l = 5, r = 5
l >= r → EXIT while loop

No triplet with -4 found.
```

---

### i = 1: nums[i] = -1

```
Target: find two numbers that sum to 1

l = 2, r = 5
nums: [-4, -1, -1, 0, 1, 2]
            i   l        r

sum = -1 + (-1) + 2 = 0 ✅ FOUND!
Add [-1, -1, 2] to result
l++

l = 3, r = 5
Skip duplicates? nums[3]=0, nums[2]=-1, not equal, continue

sum = -1 + 0 + 2 = 1 > 0 → r--

l = 3, r = 4
sum = -1 + 0 + 1 = 0 ✅ FOUND!
Add [-1, 0, 1] to result
l++

l = 4, r = 4
l >= r → EXIT while loop

Result so far: [[-1,-1,2], [-1,0,1]]
```

---

### i = 2: nums[i] = -1

```
Check: nums[2] == nums[1]? -1 == -1? YES!
SKIP! (avoid duplicate first element)
```

---

### i = 3: nums[i] = 0

```
Target: find two numbers that sum to 0

l = 4, r = 5
nums: [-4, -1, -1, 0, 1, 2]
                   i  l  r

sum = 0 + 1 + 2 = 3 > 0 → r--

l = 4, r = 4
l >= r → EXIT while loop

No new triplet found.
```

---

### i = 4: nums[i] = 1

```
nums[4] = 1 > 0
BREAK! (can't have three positive numbers sum to 0)
```

---

### FINAL RESULT

```
[[-1, -1, 2], [-1, 0, 1]]
```

---

## Visual: Two Pointer Movement

```
Finding triplets for nums[i] = -1 (target sum = 1):

nums: [-4, -1, -1, 0, 1, 2]
            i   l        r
                ↓        ↓
           -1 + (-1) + 2 = 0 ✅ Found!
           
           l++, skip dups
           
            i       l     r
                    ↓     ↓
           -1 +  0  + 2 = 1 > 0, r--
           
            i       l  r
                    ↓  ↓
           -1 +  0  + 1 = 0 ✅ Found!
```

---

## Why Skip Duplicates?

```
Without skipping:
nums = [-1, -1, 0, 1]

i=0: [-1] + [0,1] → [-1, 0, 1] ✅
i=1: [-1] + [0,1] → [-1, 0, 1] ← DUPLICATE!

With skipping:
i=0: [-1] + [0,1] → [-1, 0, 1] ✅
i=1: nums[1]==nums[0]? YES → SKIP!
```

---

## Key Optimizations in Code

```java
if (nums[i] > 0) break;
// If first element is positive, sum can't be 0

if (i > 0 && nums[i-1] == nums[i]) continue;
// Skip duplicate first elements

while (nums[l] == nums[l-1] && l < r) l++;
// Skip duplicate second elements after finding a triplet
```

---

## Complexity Analysis

| Solution | Time | Space | Correct? | Why? |
|----------|------|-------|----------|------|
| Brute Force | O(n³) | O(1) | ✅ TLE | Check all triplets |
| HashMap | O(n²) | O(n) | ✅ But messy | Duplicate handling is complex |
| **Sort + Two Pointers** | O(n²) | O(1)* | ✅ **Optimal** | O(n) two-pointer per fixed element |

*Ignoring output space and sort space

---

## Key Takeaways

1. **Sort first** → enables two pointers AND easy duplicate skipping
2. **Fix one, find two** → reduce 3Sum to 2Sum
3. **Two pointers** → O(n) for finding pairs in sorted array
4. **Skip duplicates** → check if nums[i] == nums[i-1]
5. **Early break** → if nums[i] > 0, stop (no solution possible)

---

## The Journey (TL;DR)

```
🐢 Brute Force: Three loops → TOO SLOW (O(n³))
         ↓
💡 "Two Sum used HashMap! Fix one, find two."
         ↓
🗺️ HashMap: Works but duplicate handling is messy → NOT IDEAL
         ↓
💡 "What if we sorted? Duplicates become adjacent!"
         ↓
✅ Sort + Two Pointers: Easy duplicates + fast → OPTIMAL (O(n²))
```
