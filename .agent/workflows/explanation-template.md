---
description: How to write proper Explanation.md for LeetCode problems following the Connected Flow template
---

# LeetCode Explanation.md Writing Guide

## Score-Driven Workflow

```
- Divide task into sub-tasks, score each (0.0-1.0)
- Retry if score < 0.8
- Join sub-tasks, score final result
- Fix if final score < 0.8
```

**Phases**: Research → Brute Force → Intermediate(s) → Optimal → Polish

---

## Core Principles

1. **Connected Flow**: Each solution leads to next via thought bubbles
2. **Show WHY**: Explain WHY approach not works with concrete example. can be slow, or wrong.
3. **Show WHY NOT**: Why not always intuitive/ common optimsation approaches don't work
4. **Concrete Examples**: Specific inputs, step-by-step traces, operation counts
5. **Visual First**: ASCII diagrams > walls of text
6. **Complete Journey**: Brute Force → Intermediate(s) → Optimal with all connections
7. **Donkey-level clarity**: Explain like reader has Zero-context
8. **ASCII visuals**: State changes, pointer movements, data structures, detail step-by-step visuals.
9. **Check LeetCode Editorial**: LeetCode provides an Editorial for each question containing explanations of brute force and some optimal solutions. 
   - **Always read** the Editorial during the Research phase
   - **Use as reference only** — not always the best brute force or most optimal solution
   - **Implement selectively** — only include Editorial approaches if they make sense for the user's learning journey to reach the optimal solution
   - **Don't blindly copy** — our goal is a Connected Flow that guides the reader naturally

---

## File Structure Template

```markdown
# Problem Name - Explanation

> **Prerequisites**: Foundational concepts/problems
> **Related Problems**: Link to similar pattern problems web and if alredy added to rep(research via search_web)

## Problem in Simple Words
[≤2 sentences + concrete example with answer]

---

## Solution 1: Brute Force ❌
### Approach
[Code/pseudocode]

### Why It's Bad
[Complexity + CONCRETE failure: "For n=50000: 2.5 billion ops!"]

### Example Where It's SLOW/FAILS ❌
[CONCRETE example with numbers showing the problem]
```
Input: [specific test case]
Work: X operations
For n=50000: Y billion operations!
```

### Complexity Analysis

| Solution | Time | Space | Correct? | Why? |
|----------|------|-------|----------|------|
| Brute Force | O(...) | O(...) | ✅ TLE | [reason] |
| Intuitive | O(...) | O(...) | ❌ Wrong | [reason] |
| **Optimal** | O(...) | O(...) | ✅ **Optimal** | [reason] |

> 💭 **Thought connecting to next solution. Always end with "What if we...?"**

---

## Solution 2 to N-1: Intermediate ❌
### The Natural Thought
[What someone tries after brute force, Intuitive Solitions which could be Slow or Wrong] (Why Not Ideal) (there can be one or more solutions some correct intermediate but not optimal or Intution solution first comes to ming but are wrong)

### The Natural Thought
"Things someone would try to optimse the solution with some obvious or most used context"

### Approach
[Code/pseudocode]

### Example Where It FAILS ❌
Input: [test] → Step 1 → Step 2 → BREAKS
Expected: Y | Got: Z ← WRONG!

### Why It Fails 🤯
[Root cause explanation]

### Complexity Analysis

| Solution | Time | Space | Correct? | Why? |
|----------|------|-------|----------|------|
| Brute Force | O(...) | O(...) | ✅ TLE | [reason] |
| Intuitive | O(...) | O(...) | ❌ Wrong | [reason] |
| **Optimal** | O(...) | O(...) | ✅ **Optimal** | [reason] |


> 💭 **Another thought bubble connecting to next obvious solution. "OK that doesn't work because X. What if we Y?"**

> **Honorable Mention**: Valid but not optimal alternatives

---

## Solution N: Optimal ✅

### The Connection 🔗
Let's trace our thinking:
- **Brute Force** was slow because: [X]
- **Previous** failed because: [Y]
- **What we need**: [the insight that leads to optimal] → **Solution Name!**

### The Key Insight 💡
[Core insight that makes this work]]

### The Algorithm
[Step-by-step in detail]

### Step-by-Step Walkthrough
[Detailed Trace with specific input]

### Visual Diagram
[ASCII art showing in detail how it solution works step by step]

---

## Complexity Analysis (Optimal)

| Solution | Time | Space | Status | Why |
|----------|------|-------|--------|-----|
| Brute | O(...) | O(...) | ✅ TLE | reason |
| Intuitive | O(...) | O(...) | ❌ Wrong | reason |
| **Optimal** | O(...) | O(...) | ✅ | reason |

---

## Key Takeaways
1. [Pattern/technique learned]
2. [When to use this approach]
3. [Common Pitfall to avoid]

---

## The Journey (TL;DR)

```
🐢 Approach 1 → TOO SLOW/WRONG (reason in 3 words)
         ↓
💡 "Thought that leads to next..."
         ↓
❌ Approach(s) 2 to N-1 till optimal → STILL BAD (reason)
         ↓
💡 "Better thought..."
         ↓
✅ Optimal → WORKS! (O(n) or whatever)
```
```

---

## Critical Rules For Each Solution

### Prerequisites/Related
- Use `search_web`: "leetcode [number] [name] similar problems"
- Search on domain `leetcode.com` to find genuinely related problems

**What to include:**
- **Prerequisites**: Concepts AND LeetCode problems that MUST be understood first
  - **Concepts**: e.g., "Understanding of coordinate geometry", "Binary search basics", "Graph traversal (BFS/DFS)"
- **Related Problems**: Link to LeetCode problems with SAME PATTERN or TECHNIQUE
  - Examples: Other backtracking problems, other sliding window problems, other interval intersection problems
  - Format: `[LeetCode XXX - Problem Name](https://leetcode.com/problems/problem-slug/) (brief description)`
  - **If problem exists in repo**: Provide BOTH links: `[LeetCode XXX](https://...) | [Local](../QXXXX_ProblemName/Explanation.md) (brief description)

### Problem in Simple Words
- Three sentence max
- Include concrete example with answer not abstract description
- NO algorithm details here

### Brute Force Solution ❌
**MUST HAVE:**
- Pseudocode or code snippet
- Why it's bad (complexity)
- **CONCRETE FAILURE EXAMPLE** with numbers:
  ```
  For n=50000:
  Operations = 50000² = 2.5 billion!
  ```

### Intermediate Solution(s) ❌
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
- Add Honorable Mention if there's a valid alternative approach

### Optimal Solution ✅
**MUST HAVE:**
- "The Connection 🔗" - trace the journey explicitly
- "The Key Insight 💡" - the AHA moment
- Algorithm steps
- Step-by-step walkthrough with real input
- Visual ASCII diagram

### Thought Bubbles 💭
**MUST HAVE:**
- End EVERY non-optimal with thought bubble
- MUST be a QUESTION: "What if we...?"
- ✅ Good: "HashMap is O(1) but loses indices. What structure preserves order?"
- ❌ Bad: "Use a HashMap."

### Failure Examples (REQUIRED)
| Type | Show |
|------|------|
| TLE | Operation count for large n |
| Wrong Answer | Input → Expected → Got → WHY |
| Edge Case | Specific case that breaks |

### Visual Diagrams
Include for: tree/graph traversal, two pointers, sliding window, DP tables, stack/queue ops, before/after state
```
ASCII format:   
  ┌───┬───┬───┐
  │ 1 │ 2 │ 3 │
  └───┴───┴───┘
Arrows: → ← ↑ ↓  |  Emoji: ✅ ❌ 💡
lines: -, |, \, /
```

### Complexity Table
**Always include:**
- ALL solutions discussed (not just optimal)
- Mark wrong solutions as ❌ Wrong, not ✅ TLE
- Bold the **Optimal** row
- Short "Why" column

### The Journey (TL;DR)
**Format:**
```
🐢 First attempt → 3-word reason
         ↓
💡 "Thought in quotes"
         ↓
❌/✅ → Result
```

### Always Quantify
- Time Complexity: "For n=50000: 2.5 billion ops"
- Space: "Stores 360,000 elements"
- Comparisons: "8.5× slower than optimal"
- Waste: "98% garbage generated"
- Speedup: "50,000× faster with XOR"

---

## Final Checklist (Score ≥ 0.9 required)

- [ ] Prerequisites/Related researched and linked
- [ ] Brute force has CONCRETE failure example with numbers
- [ ] Every ❌ solution has thought bubble (as QUESTION)
- [ ] Thought bubbles ASK questions, not state answers?
- [ ] Optimal has "The Connection 🔗" tracing journey
- [ ] Step-by-step walkthrough with real input
- [ ] ASCII diagrams where helpful
- [ ] Complexity table includes ALL solutions
- [ ] "The Journey" TL;DR present (< 10 lines)
- [ ] Numbers are quantified (not just "slow")?
- [ ] Donkey-level clarity achieved
- [ ] LeetCode Editorial reviewed during Research phase
- [ ] Editorial approaches included ONLY if they fit the Connected Flow journey

---

## Common Mistakes to Avoid

- ❌ Jumping straight to optimal without brute force (if brute force is present)
- ❌ Saying "it's slow" without showing HOW slow
- ❌ Thought bubbles that state the answer instead of asking
- ❌ Missing "The Connection" in optimal solution
- ❌ No concrete failure examples
- ❌ Walls of text without visuals
- ❌ TL;DR that's too long (should be <10 lines)
- ❌ Missing complexity table
- ❌ Not explaining WHY an approach fails

---