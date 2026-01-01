# Two Sum - Explanation

## Problem in Simple Words
You have a bag of numbers. You need to find **two numbers** that when added together give you a **target sum**. Return the positions (indices) of those two numbers.

Think of it like this: You have money in different pockets, and you need to find which two pockets together make exactly the amount you need.

---

## The Smart Solution (HashMap Approach)

### The Core Idea 💡
Instead of checking every pair of numbers (which is slow), we use a **HashMap** like a cheat sheet.

**Analogy**: Imagine you're looking for your friend in a crowd. Instead of asking everyone "Are you my friend?", you have a photo of your friend. You just look at the photo and scan the crowd once!

### Step-by-Step Walkthrough

Let's say `nums = [2, 7, 11, 15]` and `target = 9`

1. **Create an empty HashMap** (our cheat sheet)
   
2. **Go through each number one by one:**
   
   - **i=0, num=2**: 
     - What do we need to reach 9? We need `9 - 2 = 7`
     - Is 7 in our HashMap? NO (it's empty)
     - Add 2 to HashMap: `{2: 0}` (number 2 is at index 0)
   
   - **i=1, num=7**:
     - What do we need to reach 9? We need `9 - 7 = 2`
     - Is 2 in our HashMap? YES! It's at index 0
     - **FOUND IT!** Return `[0, 1]`

### Why This Works
We're basically asking: "For each number, have I already seen a number that completes the pair?"

---

## Why Brute Force Doesn't Cut It ❌

### Brute Force Approach
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
- **Two nested loops** = For each element, we check ALL other elements
- If you have 10,000 numbers, you're doing 10,000 × 10,000 = **100 million operations**!
- That's like asking everyone in a stadium to shake hands with everyone else

---

## Why Sorting Doesn't Work Here ❌

You might think: "Why not sort and use two pointers?"

**Problem**: Sorting **changes the indices**! The question asks for the **original positions**, not the sorted positions.

You could sort and then search for original indices, but that adds unnecessary complexity.

---

## Complexity Analysis

### Optimal Solution (HashMap)
| Metric | Complexity | Explanation |
|--------|------------|-------------|
| **Time** | O(n) | We traverse the array once. HashMap lookup is O(1) |
| **Space** | O(n) | In worst case, we store all n elements in HashMap |

### Brute Force (Two Loops)
| Metric | Complexity | Explanation |
|--------|------------|-------------|
| **Time** | O(n²) | Two nested loops checking every pair |
| **Space** | O(1) | No extra space used |

### Sorting + Two Pointers (if we didn't need indices)
| Metric | Complexity | Explanation |
|--------|------------|-------------|
| **Time** | O(n log n) | Sorting dominates the complexity |
| **Space** | O(1) or O(n) | Depends on sorting algorithm |

---

## Key Takeaways

1. **HashMap** is your best friend for "find a pair" problems
2. When you need to find a **complement** (target - current), think HashMap
3. Trading **space for time** (using HashMap) is usually worth it
4. Always consider: "Have I seen this before?" → HashMap!
