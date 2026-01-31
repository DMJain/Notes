# Find Smallest Letter Greater Than Target - Explanation

> **Prerequisites**: Binary Search basics, understanding of sorted arrays  
> **Related Problems**: 
> - [LeetCode 35 - Search Insert Position](https://leetcode.com/problems/search-insert-position/) (Finding insertion point in sorted array)
> - [LeetCode 278 - First Bad Version](https://leetcode.com/problems/first-bad-version/) (Binary search for boundary)
> - [LeetCode 704 - Binary Search](https://leetcode.com/problems/binary-search/) (Basic binary search)

## Problem in Simple Words

Imagine you have a sorted list of letters like `['c', 'f', 'j']` and someone gives you a target letter `'c'`. Your job is to find the **smallest letter in the list that comes AFTER the target alphabetically**. 

So for target `'c'`, you need a letter that is GREATER than `'c'`. Looking at the list: `'c'` is not greater (it's equal), `'f'` is greater! So the answer is `'f'`.

**The twist**: If no letter is greater than the target (like if target is `'z'` and your biggest letter is `'y'`), you "wrap around" and return the very first letter in the list.

**Example**: `letters = ['c', 'f', 'j']`, `target = 'c'` → Answer is `'f'` (first letter > 'c')

---

## Solution 1: Brute Force - Linear Scan ❌

### The Natural Thought

The most obvious approach when you're looking for something in a list? **Just look at every single element one by one!** 

Start from the beginning, check each letter, and the moment you find one that's greater than the target, return it. If you reach the end without finding anything, wrap around and return the first letter.

This is exactly how you'd naturally search through a list if you didn't know anything special about it.

### Approach

```java
public char nextGreatestLetter(char[] letters, char target) {
    // Scan every letter from left to right, return first one > target
    for (char letter : letters) {
        if (letter > target) {
            return letter;
        }
    }
    // If we checked ALL letters and none was greater, wrap around
    return letters[0];
}
```

**How it works step by step:**
1. Start at index 0
2. Compare current letter with target
3. If current letter > target → Found it! Return immediately
4. Otherwise → Move to next index and repeat
5. If loop finishes (no letter was greater) → Return first letter (wrap around)

### Why It's Bad

The problem with this approach is that we're **completely ignoring** a very important piece of information: **the array is SORTED!**

Think about it. If the array is `['a', 'b', 'c', 'd', 'e', 'f', 'g']` and target is `'f'`, we check `'a'`, `'b'`, `'c'`, `'d'`, `'e'`, `'f'`... that's 6 comparisons before we finally find `'g'`.

But wait — since the array is sorted, if we looked at the middle element first (`'d'`), we'd know immediately that `'d' <= 'f'`, so the answer MUST be somewhere to the right. We could skip the entire left half! That's the key insight we're missing.

### Example Where It's SLOW ❌

```
Input: letters = ['a', 'b', 'c', ..., 'z'] (10,000 elements)
       target = 'z'

What happens:
- Check 'a' - not greater than 'z' ❌
- Check 'b' - not greater than 'z' ❌
- Check 'c' - not greater than 'z' ❌
- ... (keep checking every single element)
- Check element 10,000 - still not greater ❌
- Finally wrap around to letters[0]

Work: We checked ALL 10,000 elements!
      For n = 10,000: 10,000 comparisons

If you run this query repeatedly:
  Q queries × n elements = Q × n total operations
  For Q = 10,000 queries: 100,000,000 comparisons!
  That's 100 MILLION comparisons just because we ignored the sorted property!
```

### Complexity Analysis

| Solution | Time | Space | Correct? | Why? |
|----------|------|-------|----------|------|
| **Brute Force** | O(n) | O(1) | ✅ Correct but slow | Ignores sorted property |
| **Binary Search** | O(log n) | O(1) | ✅ **Optimal** | Uses sorted property |

> 💭 **Hold on — the array is SORTED! We're checking elements one by one like it's an unsorted mess. What if we could use the sorted property to skip elements? What algorithm is specifically designed to search efficiently in sorted arrays by eliminating half the elements at each step?**

---

## Solution 2: Binary Search (Optimal) ✅

### The Connection 🔗

Let's trace our thinking journey:

1. **Brute Force** was slow because: We checked every single element from left to right, completely ignoring that the array is sorted. This meant O(n) time — linear in the array size.

2. **The realization**: In a sorted array, if we look at ANY element, we immediately know which direction to go:
   - If `current > target` → Answer could be here OR to the left (we want the SMALLEST greater one)
   - If `current <= target` → Answer MUST be to the right (current isn't good enough)

3. **What we need**: An algorithm that uses this property to eliminate half the search space at each step → **Binary Search!**

### The Key Insight 💡

This problem is a classic **"Find Upper Bound"** problem in computer science:

**What is Upper Bound?** The upper bound of a target in a sorted array is the **first element that is STRICTLY GREATER than the target**. Not greater-or-equal, but strictly greater.

For example, in `['a', 'c', 'c', 'c', 'f']` with target `'c'`:
- The first element > 'c' is `'f'` at index 4
- Even though there are multiple `'c'`s, we skip ALL of them because we need STRICTLY greater

**Why Binary Search works here:**
- We start with the full array as our search space
- Check the middle element
- If `middle > target`: This COULD be our answer, but there might be a SMALLER one to the left. So we keep searching left, but remember this is a candidate
- If `middle <= target`: This is NOT our answer (it's not greater). The answer MUST be to the right
- Each step eliminates half the remaining elements!

**The wrap-around edge case**: Before doing binary search, we check if `target >= letters[last]`. If yes, no element can be greater, so we immediately return `letters[0]`.

### The Algorithm

Here's the step-by-step algorithm explained in plain English:

1. **Handle Edge Case First**: 
   - Look at the LAST element of the array (the biggest one, since it's sorted)
   - If even this biggest element is not greater than target, then NOTHING in the array is greater
   - In this case, wrap around and return the first element immediately

2. **Set Up Binary Search**:
   - `left = 0` (start of array)
   - `right = length - 1` (end of array)
   - We're going to narrow this window until we find our answer

3. **The Search Loop** (while `left <= right`):
   - Calculate `mid = left + (right - left) / 2` (find the middle)
   - **If `letters[mid] > target`**: 
     - Great! This letter IS greater than target, so it's a CANDIDATE
     - But wait — is it the SMALLEST such letter? There might be smaller candidates to the left!
     - So move `right = mid - 1` to check the left half
   - **Else `letters[mid] <= target`**:
     - This letter is not greater than target, so it can't be our answer
     - The answer must be somewhere to the RIGHT
     - So move `left = mid + 1` to check the right half

4. **Return `letters[left]`**:
   - When the loop ends, `left` points to the first element > target
   - This is because every time we found a valid candidate, we moved `right` past it, and every time we found an invalid element, we moved `left` past it
   - So `left` ends up at exactly the boundary — the first valid answer!

### Step-by-Step Walkthrough

**Input**: `letters = ['c', 'f', 'j']`, `target = 'c'`

Let's trace through the algorithm with a concrete example:

```
Initial State:
- Array: ['c', 'f', 'j'] (indices 0, 1, 2)
- Target: 'c'
- left = 0, right = 2

Edge case check: letters[2] = 'j', and 'j' > 'c'? YES
                 So we have at least one valid answer. Proceed with binary search.

┌─────────────────────────┐
│ Index:   0    1    2    │
│ Letter: [c]  [f]  [j]   │
│          ↑         ↑    │
│          L         R    │
│     (left=0)   (right=2)│
└─────────────────────────┘

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

ITERATION 1:
  mid = 0 + (2-0)/2 = 1
  letters[mid] = letters[1] = 'f'
  
  Question: Is 'f' > 'c'? 
  Answer: YES! 'f' comes after 'c' alphabetically
  
  Action: 'f' is a CANDIDATE answer, but maybe there's something 
          smaller on the left that's also > 'c'. Let's check!
          Move right = mid - 1 = 0

┌─────────────────────────┐
│ Index:   0    1    2    │
│ Letter: [c]  [f]  [j]   │
│          ↑              │
│         L,R             │
│   (left=0, right=0)     │
└─────────────────────────┘

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

ITERATION 2:
  mid = 0 + (0-0)/2 = 0
  letters[mid] = letters[0] = 'c'
  
  Question: Is 'c' > 'c'?
  Answer: NO! 'c' equals 'c', we need STRICTLY greater
  
  Action: 'c' is NOT valid. The answer must be to the RIGHT.
          Move left = mid + 1 = 1

┌─────────────────────────┐
│ Index:   0    1    2    │
│ Letter: [c]  [f]  [j]   │
│               ↑         │
│          R    L         │
│     (right=0, left=1)   │
└─────────────────────────┘

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

LOOP ENDS: right (0) < left (1), condition left <= right is FALSE

RESULT: Return letters[left] = letters[1] = 'f' ✅

Why 'f'? Because:
- We checked 'c' and found it wasn't > target
- We checked 'f' and found it WAS > target
- We checked if there was anything smaller than 'f' that's also > target (there wasn't)
- So 'f' is the smallest letter greater than 'c'!
```

### Visual Diagram - Binary Search Narrowing

Let's see another example to really understand how binary search narrows down:

```
Target = 'd', letters = ['c', 'f', 'j']

We want: smallest letter > 'd'
Expected answer: 'f' (since 'c' <= 'd' and 'f' > 'd')

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

STEP 1: Check middle element
┌─────┬─────┬─────┐
│  c  │  f  │  j  │
└─────┴─────┴─────┘
  L      ↑      R
        mid=1
        
Question: 'f' > 'd'? 
Answer: YES! 

Decision: 'f' is a candidate. But is there something smaller 
          that's also > 'd'? Check left half.
          right = mid - 1 = 0

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

STEP 2: Check left half
┌─────┬─────┬─────┐
│  c  │  f  │  j  │
└─────┴─────┴─────┘
  L,R
  mid=0
  
Question: 'c' > 'd'?
Answer: NO! 'c' comes before 'd'

Decision: 'c' is too small. Answer must be to the right.
          left = mid + 1 = 1

━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━━

STEP 3: left (1) > right (0), loop ends

left now points to index 1 → 'f'

Result: 'f' ✅ (smallest letter > 'd')

Summary of what happened:
- We eliminated 'j' without even checking it individually!
- We only made 2 comparisons instead of 3
- For larger arrays, this savings is MASSIVE
```

### Wrap-Around Case Explained

The wrap-around is a special edge case. Let's trace through it:

```
Target = 'z', letters = ['x', 'x', 'y', 'y']

Question: Is there ANY letter in the array greater than 'z'?

Step 1: Check the LAST element (the biggest one since array is sorted)
        letters[3] = 'y'
        
        Is 'y' > 'z'? NO! 'y' comes before 'z'

Step 2: Since even the BIGGEST letter isn't greater than 'z',
        NO letter in the entire array can be greater than 'z'.

Step 3: According to the problem, we "wrap around" and return 
        the first letter.
        
        Return: letters[0] = 'x' ✅

Why check the last element first?
- The array is SORTED in non-decreasing order
- So letters[last] is the BIGGEST element
- If even the biggest isn't > target, nothing will be
- This saves us from doing a binary search that would find nothing
```

---

## Complexity Analysis

| Solution | Time | Space | Status | Why |
|----------|------|-------|--------|-----|
| Brute Force | O(n) | O(1) | ✅ Slow | Checks every element one by one |
| **Binary Search** | **O(log n)** | **O(1)** | ✅ **Optimal** | Halves the search space with each comparison |

### Why O(log n)?

Let's understand why binary search is O(log n) with concrete numbers:

```
Array size n = 10,000 elements

Brute Force:
  - Worst case: check all 10,000 elements
  - Operations: 10,000

Binary Search:
  - Start with 10,000 elements
  - After 1 comparison: 5,000 elements remain
  - After 2 comparisons: 2,500 elements remain
  - After 3 comparisons: 1,250 elements remain
  - After 4 comparisons: 625 elements remain
  - After 5 comparisons: ~312 elements remain
  - After 6 comparisons: ~156 elements remain
  - After 7 comparisons: ~78 elements remain
  - After 8 comparisons: ~39 elements remain
  - After 9 comparisons: ~19 elements remain
  - After 10 comparisons: ~10 elements remain
  - After 11 comparisons: ~5 elements remain
  - After 12 comparisons: ~2 elements remain
  - After 13 comparisons: ~1 element remains
  - After 14 comparisons: DONE!
  
  Operations: just 14 comparisons!

Speedup: 10,000 / 14 ≈ 714× FASTER!
```

The pattern: we need log₂(n) comparisons because we're cutting the problem in half each time. That's the magic of binary search!

---

## Key Takeaways

1. **Upper Bound Pattern**: When you need to find the "first element strictly greater than X" in a sorted array, that's the classic "upper bound" binary search pattern. Memorize it!

2. **Sorted Array = Think Binary Search**: Whenever you see "sorted array" in a problem, your first thought should be "Can I use binary search here?" It turns O(n) into O(log n).

3. **Left Pointer After Loop**: In this style of binary search (with `left <= right` and moving `left = mid + 1` / `right = mid - 1`), the `left` pointer ends up at exactly where the target WOULD be inserted. This is incredibly useful for many problems.

4. **Handle Edge Cases First**: The wrap-around case is a special situation. By checking it BEFORE the binary search, we keep our main logic clean and simple.

5. **STRICTLY Greater vs Greater-or-Equal**: Pay attention to whether the problem wants `> target` or `>= target`. This changes how you handle the `=` case in your comparison!

---

## The Journey (TL;DR)

```
🐢 Brute Force → LINEAR SCAN (O(n), ignores sorting)
         ↓
💡 "Array is sorted! What if we use binary search?"
         ↓
✅ Binary Search → OPTIMAL (O(log n), 714× faster!)
```

---

## Key Optimizations in Code

```java
// Early exit for wrap-around case
// If largest element <= target, no element can be > target
if (letters[letters.length - 1] <= target) return letters[0];

// Standard upper-bound binary search
int l = 0, r = letters.length - 1;
while (r >= l) {
    int mid = l + (r - l) / 2;  // Avoids integer overflow
    if (letters[mid] > target) {
        r = mid - 1;  // Found candidate, check left for smaller
    } else {
        l = mid + 1;  // Too small, go right
    }
}
return letters[l];  // l = first index > target
```

**Why `l + (r-l)/2` instead of `(l+r)/2`?**  
In languages like Java/C++, if `l` and `r` are both large numbers, `l + r` might exceed `Integer.MAX_VALUE` (about 2 billion) and cause an overflow. By computing `(r-l)/2` first (which is always safe) and then adding `l`, we avoid this issue. It's a best practice for any binary search implementation.
