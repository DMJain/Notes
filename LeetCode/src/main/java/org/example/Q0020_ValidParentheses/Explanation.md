# Valid Parentheses - Explanation

## Problem in Simple Words
Check if a string of brackets like `()[]{}` is mathematically valid.
- Every opening bracket `(`, `{`, `[` must have a matching closer `)`, `}`, `]`.
- They must close in the correct order (nested properly).

**Valid**: `([])`, `()[]{}`
**Invalid**: `([)]` (wrong order), `(]` (mismatch), `(` (unclosed)

---

## Solution 1: Brute Force (String Replacement) ❌ (Too Slow)

### Approach
Repeatedly find and remove adjacent matching pairs `()`, `[]`, `{}` until the string is empty or no more pairs exist.

```java
while (s.contains("()") || s.contains("[]") || s.contains("{}")) {
    s = s.replace("()", "");
    s = s.replace("[]", "");
    s = s.replace("{}");
}
return s.isEmpty();
```

### Why It's Bad
- String replacement creates a new string every time.
- **O(n²)** time complexity.
- n = 10,000 → 100,000,000 operations. Slow!

> 💭 **We're doing repeated passes over the string. Can we validate in a single pass? What if we just counted open vs closed brackets?**

---

## Solution 2: Counter / Two Pointers ❌ (Insufficient)

### The Natural Thought
"Just count brackets! If opens == closes, we're good, right?"

### Approach
```java
int open = 0;
for (char c : s) {
    if (c == '(') open++;
    else open--;
}
return open == 0;
```

### Example Where It WORKS ✅
```
s = "(())"
open: 1 → 2 → 1 → 0
Final: 0 ✅
```

### Example Where It FAILS ❌
```
s = "([)]"
Counts: 1 round open, 1 round close, 1 square open, 1 square close
All counts balance to 0!

But "([)]" is INVALID because ']' tries to close '[' 
but ')' came first — wrong ORDER!
```

### Why It Fails 🤯
Counting doesn't track **ORDER**. `[` must be closed by the NEXT matching closer, not just ANY closer somewhere in the string.

> 💭 **The issue is ORDER. The LAST opened bracket must be the FIRST one closed. That's LIFO — Last In, First Out. What data structure is LIFO?**

---

## Solution 3: Stack ✅ (Optimal)

### The Connection 🔗
Let's trace our thinking:
- **Brute Force** was slow because: multiple passes, string creation overhead
- **Counter** was wrong because: counts balance but order matters
- **What we need**: track WHICH bracket was opened last → **Stack!**

### The Key Insight 💡
The **Last** opened bracket must be the **First** one closed (LIFO = Stack!).

1. When we see an **OPEN** bracket, **push** it to stack (we're "waiting" for its closer)
2. When we see a **CLOSE** bracket, it **MUST** match the top of stack (most recently opened)
3. If it matches → **pop** (pair complete)
4. If it doesn't match (or stack empty) → **invalid!**

### Why It Works
```
Counter:                 Stack:
   ↓                        ↓
"Count balances"         "Order preserved"
([)] → counts ok         ([)] → ']' doesn't match '('
Result: WRONG            Result: CORRECT (invalid)
```

### Step-by-Step Walkthrough

**Input: `s = "([)]"`**

```
Stack: []

1. char = '(' (Open)
   Push '('
   Stack: ['(']

2. char = '[' (Open)
   Push '['
   Stack: ['(', '[']

3. char = ')' (Close)
   Check top of stack: '['
   Does ')' match '['? NO! ❌
   
   Return FALSE.
```

**Input: `s = "{[]}"`**

```
Stack: []

1. char = '{' (Open)
   Push '{'
   Stack: ['{']

2. char = '[' (Open)
   Push '['
   Stack: ['{', '[']

3. char = ']' (Close)
   Check top: '['
   Matches? YES! Pop.
   Stack: ['{']

4. char = '}' (Close)
   Check top: '{'
   Matches? YES! Pop.
   Stack: []

End of string. Stack is empty? YES.
Return TRUE. ✅
```

---

## Visual: Stack in Action

```
Input: "{[]}"

    {        [        ]        }
    ↓        ↓        ↓        ↓
  push     push     pop      pop
    
Stack:   Stack:   Stack:   Stack:
  ┌─┐     ┌─┐      ┌─┐      ┌─┐
  │{│     │[│      │ │      │ │
  └─┘     │{│      │{│      └─┘
          └─┘      └─┘      
          
                 ']' matches '[' ✅
                             '}' matches '{' ✅
                             
Final: Empty Stack → VALID!
```

---

## Edge Cases

1. **Empty String**: Valid (usually).
2. **Single char**: `[` → Invalid (stack not empty at end).
3. **Close first**: `]` → Invalid (stack empty when popping).
4. **Mismatch**: `(]` → Invalid.

---

## Complexity Analysis

| Solution | Time | Space | Correct? | Why? |
|----------|------|-------|----------|------|
| Brute Force | O(n²) | O(n) | ✅ TLE | Repeated string passes |
| Counter | O(n) | O(1) | ❌ Wrong | Ignores order |
| **Stack** | O(n) | O(n) | ✅ **Optimal** | Tracks order with LIFO |

- **Time**: We traverse the string once. Push/Pop is O(1).
- **Space**: Worst case `(((((` pushes all chars to stack → O(n).

---

## Key Takeaways

1. **LIFO property**: Nested structures (like code blocks, HTML tags, math expressions) usually require a Stack
2. **Matching pairs**: Always check if stack is empty before peeking/popping!
3. **Final check**: The stack must be **empty** at the end (all opened brackets were closed)

---

## The Journey (TL;DR)

```
🐢 Brute Force: Replace pairs repeatedly → TOO SLOW (O(n²))
         ↓
💡 "Can we just count? Opens should equal closes."
         ↓
🔢 Counter: Counts balance but ignores order → WRONG
         ↓
💡 "Order matters: last opened = first closed. That's LIFO!"
         ↓
✅ Stack: Tracks order perfectly → OPTIMAL (O(n))
```
