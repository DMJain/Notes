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
    s = s.replace("{}", "");
}
return s.isEmpty();
```

### Why It's Bad
- String replacement creates a new string every time.
- **O(n²)** time complexity.
- n = 10,000 → 100,000,000 operations. Slow!

---

## Solution 2: Counter / Two Pointers ❌ (Insufficient)

### Approach
"Just count open vs closed brackets?"

```java
int open = 0;
for (char c : s) {
    if (c == '(') open++;
    else open--;
}
return open == 0;
```

### Why It Fails
- Works for single type `((()))`.
- **Fails for mixed types**: `([)]`
  - Counts are balanced (1 round open, 1 round close, 1 square open, 1 square close).
  - But the **order** is wrong! `[` is closed by `)` which is illegal.

---

## Solution 3: Stack ✅ (Optimal)

### The Key Insight 💡
The **Last** opened bracket must be the **First** one closed (LIFO).
This screams **STACK**!

1. When we see an **OPEN** bracket (`(`, `{`, `[`), we keep it (push to stack). We are "waiting" for its closer.
2. When we see a **CLOSE** bracket (`)`, `}`, `]`), it **MUST** match the most recently opened bracket (top of stack).
3. If it matches, we are done with that pair (pop).
4. If it doesn't match (or stack is empty), invalid!

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

## Edge Cases

1. **Empty String**: Valid (usually).
2. **Single char**: `[` → Invalid (stack not empty at end).
3. **Close first**: `]` → Invalid (stack empty when popping).
4. **Mismatch**: `(]` → Invalid.

---

## Complexity Analysis

| Solution | Time | Space | Correct? |
|----------|------|-------|----------|
| Brute Force | O(n²) | O(n) | ✅ TLE |
| Counter | O(n) | O(1) | ❌ Wrong |
| **Stack** | O(n) | O(n) | ✅ Optimal |

- **Time**: We traverse the string once. Push/Pop is O(1).
- **Space**: Worst case `(((((` pushes all chars to stack → O(n).

---

## Key Takeaways

1. **LIFO property**: Nested structures (like code blocks, HTML tags, math expressions) usually require a Stack.
2. **Matching pairs**: Always check if stack is empty before peeking/popping!
3. **Final check**: The stack must be **empty** at the end (all opened brackets were closed).
