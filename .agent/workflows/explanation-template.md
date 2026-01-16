---
description: How to write proper Explanation.md for LeetCode problems following the Connected Flow template
---

# LeetCode Explanation.md Writing Guide

## Core Principles

1. **Connected Flow**: Each solution naturally leads to the next through thought bubbles
2. **Show WHY**: Always explain WHY an approach fails, not just that it's slow
3. **Concrete Examples**: Use specific inputs, step-by-step traces, operation counts
4. **Visual First**: ASCII diagrams > walls of text
5. **Complete Journey**: Brute Force → Intermediate(s) → Optimal with all connections

---

## File Structure Template

```markdown
# Problem Name - Explanation

> **Prerequisites**: Link to foundational problems if applicable
> E.g., "This is an extension of [Q0001 Two Sum](../Q0001_TwoSum/Explanation.md)"

> **Related Problems**: Link to similar pattern problems
> E.g., "Same pattern as [Q0022 Generate Parentheses](../Q0022_GenerateParentheses/Explanation.md)"

> **Honorable Mention**: Alternative valid approaches that work but aren't optimal
> E.g., "*TreeMap approach* is valid O(n log n) but Monotonic Deque is O(n)"

## Problem in Simple Words
[One-liner explanation + concrete example with answer]

---

## Solution 1: Brute Force ❌ (Why Bad)

### Approach
[Code snippet or pseudocode]

### Why It's Bad
[General statement: O(n³), too slow, etc.]

### Example Where It's SLOW/FAILS ❌
[CONCRETE example with numbers showing the problem]
```
Input: [specific test case]
Work: X operations
For n=50000: Y billion operations!
```

> 💭 **Thought connecting to next solution. Always end with "What if we...?"**

---

## Solution 2 to N-1: Intuitive Solitions which could be Slow or Wrong ❌ (Why Not Ideal) [there can be one or more solutions some correct intermediate but not optimal or Intution solution first comes to ming but are wrong]

### The Natural Thought
"First thing someone would try after brute force"

### Approach
[Code or pseudocode]

### Example Where It FAILS ❌
[CONCRETE example showing WHERE it breaks]
```
Input: [test case]
Step 1: ...
Step 2: ...
Step 3: BREAKS because X
Expected: Y
Got: Z ← WRONG!
```

### Why It Fails 🤯
[Root cause explanation]

> 💭 **Another thought bubble connecting to optimal. "OK that doesn't work because X. What if we Y?"**

---

## Solution N: Optimal ✅

### The Connection 🔗
Let's trace our thinking:
- **Brute Force** was slow because: [X]
- **Previous** failed because: [Y]
- **What we need**: [the insight that leads to optimal] → **Solution Name!**

### The Key Insight 💡
[Core insight that makes this work]

### The Algorithm
[Step-by-step algorithm]

### Step-by-Step Walkthrough
[Detailed trace with specific input]

### Visual Diagram
[ASCII art showing how it works]

---

## Complexity Analysis

| Solution | Time | Space | Correct? | Why? |
|----------|------|-------|----------|------|
| Brute Force | O(...) | O(...) | ✅ TLE | [reason] |
| Intuitive | O(...) | O(...) | ❌ Wrong | [reason] |
| **Optimal** | O(...) | O(...) | ✅ **Optimal** | [reason] |

---

## Key Takeaways

1. [Pattern/technique learned]
2. [When to use this approach]
3. [Common pitfall to avoid]

---

## The Journey (TL;DR)

```
🐢 Approach 1 → TOO SLOW/WRONG (reason in 3 words)
         ↓
💡 "Thought that leads to next..."
         ↓
❌ Approach 2 → STILL BAD (reason)
         ↓
💡 "Better thought..."
         ↓
✅ Optimal → WORKS! (O(n) or whatever)
```
```

---

## Critical Rules for Each Section

### 1. Prerequisites/Related (Top)
- Link if problem DEPENDS on another (e.g., 3Sum depends on 2Sum)
- Link if same PATTERN (e.g., backtracking problems link to each other)
- Add Honorable Mention if there's a valid alternative approach

### 2. Problem in Simple Words
- One sentence max
- Include concrete example with answer
- NO algorithm details here

### 3. Brute Force Solution ❌
**MUST HAVE:**
- Pseudocode or code snippet
- Why it's bad (complexity)
- **CONCRETE FAILURE EXAMPLE** with numbers:
  ```
  For n=50000:
  Operations = 50000² = 2.5 billion!
  ```

### 4. Intermediate Solution(s) ❌
**MUST HAVE:**
- "The Natural Thought" - what would someone try next?
- Approach code
- **SPECIFIC EXAMPLE WHERE IT FAILS**:
  ```
  Input: [3, 2, 4], target = 6
  Approach gives: [wrong answer]
  Correct: [right answer]
  WHY: [root cause]
  ```
- Why It Fails explanation

### 5. Optimal Solution ✅
**MUST HAVE:**
- "The Connection 🔗" - trace the journey explicitly
- "The Key Insight 💡" - the AHA moment
- Algorithm steps
- Step-by-step walkthrough with real input
- Visual ASCII diagram

### 6. Thought Bubbles 💭
**MUST:**
- End EVERY non-optimal solution with a thought bubble
- Thought must CONNECT to next solution
- Always phrase as a QUESTION: "What if we...?"
- Never just state the next solution

**Good:**
> 💭 **Sorting destroys indices, but we still need O(1) lookup. What data structure gives O(1) lookup without changing positions?**

**Bad:**
> 💭 **Use a HashMap.**

### 7. Failure Examples
**Types of failures to show:**

| Problem Type | Show This Failure |
|--------------|-------------------|
| Wrong Answer | Concrete input where approach gives wrong output |
| TLE (slow) | Operation count for large n |
| Edge Case | Specific edge case that breaks approach |
| Duplicate handling | Show duplicates appearing |
| Index problem | Show indices getting lost |

### 8. Visual Diagrams
**When to include:**
- Tree/graph traversal
- Two pointers movement
- Sliding window
- DP table progression
- Stack/queue operations
- Before/after state

**Format:**
```
Use ASCII art:
  ┌───┬───┬───┐
  │ 1 │ 2 │ 3 │
  └───┴───┴───┘

Use arrows: →, ←, ↑, ↓, ↗, ↘
Use boxes for emphasis: [X]
Use emoji sparingly: ✅, ❌, 💡
```

### 9. Complexity Table
**Always include:**
- ALL solutions discussed (not just optimal)
- Mark wrong solutions as ❌ Wrong, not ✅ TLE
- Bold the **Optimal** row
- Short "Why" column

### 10. The Journey (TL;DR)
**Format:**
```
🐢 First attempt → 3-word reason
         ↓
💡 "Thought in quotes"
         ↓
❌/✅ → Result
```

---

## Quantification Standards

**Always quantify these:**

| What | How |
|------|-----|
| Time complexity | "For n=50000: 2.5 billion ops" |
| Space | "Stores 600² = 360,000 elements" |
| Comparisons | "8.5× slower than optimal" |
| Waste | "98% garbage generated" |
| Speedup | "50,000× faster with XOR" |

---

## Checklist Before Finishing

- [ ] Has Prerequisites/Related at top?
- [ ] Brute force has CONCRETE failure example?
- [ ] Every ❌ solution has thought bubble?
- [ ] Thought bubbles ASK questions, not state answers?
- [ ] Optimal has "The Connection 🔗"?
- [ ] Has step-by-step walkthrough with real input?
- [ ] Has ASCII diagram where helpful?
- [ ] Complexity table includes ALL solutions?
- [ ] Has "The Journey" TL;DR at end?
- [ ] Numbers are quantified (not just "slow")?

---

## Common Mistakes to Avoid

1. ❌ Jumping straight to optimal without brute force
2. ❌ Saying "it's slow" without showing HOW slow
3. ❌ Thought bubbles that state the answer instead of asking
4. ❌ Missing "The Connection" in optimal solution
5. ❌ No concrete failure examples
6. ❌ Walls of text without visuals
7. ❌ TL;DR that's too long (should be <10 lines)
8. ❌ Missing complexity table
9. ❌ Not explaining WHY an approach fails

---

## Example: Proper Thought Bubble Flow

```
Solution 1: Brute Force O(n²)
> 💭 **We're checking every pair. Can we avoid redundant checks with some data structure?**

Solution 2: HashMap O(n) but loses indices
> 💭 **HashMap is fast but sorting destroyed our indices. What if we hash the VALUES, not positions?**

Solution 3: HashMap with original indices ✅
"The Connection" explains the journey clearly.
```