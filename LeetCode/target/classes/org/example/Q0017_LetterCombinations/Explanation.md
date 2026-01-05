# Letter Combinations of a Phone Number - Explanation

## Problem in Simple Words
Given digits like "23", generate all possible letter combinations from a phone keypad.

```
2 → abc    3 → def
```

**Input: "23" → Output: ["ad","ae","af","bd","be","bf","cd","ce","cf"]**

---

## Solution 1: Brute Force with Nested Loops ❌ (Not Scalable)

### Approach
Use nested loops for each digit.

```java
// For "23":
for (char c1 : map.get('2')) {      // a, b, c
    for (char c2 : map.get('3')) {  // d, e, f
        result.add("" + c1 + c2);
    }
}
```

### Why It's Bad
- Need different code for different number of digits!
- 2 digits = 2 loops, 3 digits = 3 loops, etc.
- **Not flexible at all**

---

## Solution 2: Iterative BFS-style ❌ (Works but More Complex)

### Approach
Build combinations iteratively by extending previous results.

```java
List<String> result = [""];
for (char digit : digits) {
    List<String> newResult = [];
    for (String prefix : result) {
        for (char letter : map.get(digit)) {
            newResult.add(prefix + letter);
        }
    }
    result = newResult;
}
```

### Why It's Not Ideal
- Creates many intermediate strings
- Uses more memory
- Harder to understand than recursion

---

## Solution 3: Backtracking ✅ (Optimal)

### The Key Insight 💡
Build the string **one character at a time**:
1. Pick a letter for current digit
2. Recurse to next digit
3. **Backtrack**: undo the choice, try next letter

### The Pattern

```
Choose → Explore → Unchoose

temp.append(c);               // CHOOSE
helper(i + 1, ...);           // EXPLORE
temp.deleteCharAt(len - 1);   // UNCHOOSE
```

---

## Step-by-Step Walkthrough

**digits = "23"**

```
Digit mapping:
  '2' → ['a', 'b', 'c']
  '3' → ['d', 'e', 'f']
```

---

### The Recursion Tree

```
                          helper(i=0, temp="")
                    ┌─────────┼─────────┐
                 choose     choose    choose
                   'a'        'b'       'c'
                    ↓          ↓         ↓
             helper(1,"a") helper(1,"b") helper(1,"c")
             ┌──┼──┐       ┌──┼──┐       ┌──┼──┐
            'd''e''f'     'd''e''f'     'd''e''f'
             ↓  ↓  ↓       ↓  ↓  ↓       ↓  ↓  ↓
           i=2,temp=     i=2,temp=     i=2,temp=
           "ad""ae""af"  "bd""be""bf"  "cd""ce""cf"
             ↓  ↓  ↓       ↓  ↓  ↓       ↓  ↓  ↓
           ADD TO RESULT (base case: i == digits.length)
```

---

### Trace Through One Branch

```
═══════════════════════════════════════════════════════════════
CALL: helper(i=0, temp="")
═══════════════════════════════════════════════════════════════
digit = '2', letters = ['a', 'b', 'c']

Loop iteration 1: c = 'a'
  temp.append('a') → temp = "a"
  RECURSE: helper(i=1, temp="a")
  
  ─────────────────────────────────────────────────────────────
  CALL: helper(i=1, temp="a")
  ─────────────────────────────────────────────────────────────
  digit = '3', letters = ['d', 'e', 'f']
  
  Loop iteration 1: c = 'd'
    temp.append('d') → temp = "ad"
    RECURSE: helper(i=2, temp="ad")
    
    ───────────────────────────────────────────────────────────
    CALL: helper(i=2, temp="ad")
    BASE CASE: i == digits.length (2 == 2)
    answer.add("ad") ✅
    RETURN
    ───────────────────────────────────────────────────────────
    
    BACKTRACK: temp.deleteCharAt() → temp = "a"
  
  Loop iteration 2: c = 'e'
    temp.append('e') → temp = "ae"
    RECURSE → answer.add("ae") ✅
    BACKTRACK → temp = "a"
  
  Loop iteration 3: c = 'f'
    temp.append('f') → temp = "af"
    RECURSE → answer.add("af") ✅
    BACKTRACK → temp = "a"
  
  RETURN (finished all letters for digit '3')
  ─────────────────────────────────────────────────────────────
  
  BACKTRACK: temp.deleteCharAt() → temp = ""

Loop iteration 2: c = 'b'
  temp.append('b') → temp = "b"
  RECURSE: helper(i=1, temp="b")
  ... generates "bd", "be", "bf" ✅
  BACKTRACK → temp = ""

Loop iteration 3: c = 'c'
  temp.append('c') → temp = "c"
  RECURSE: helper(i=1, temp="c")
  ... generates "cd", "ce", "cf" ✅
  BACKTRACK → temp = ""

═══════════════════════════════════════════════════════════════
FINAL RESULT: ["ad","ae","af","bd","be","bf","cd","ce","cf"]
═══════════════════════════════════════════════════════════════
```

---

## Visual: The Backtracking Pattern

```
For digits = "23":

     Start: temp = ""
            ↓
     ┌──────┼──────┐
     a      b      c      ← Choose letter for digit '2'
     ↓      ↓      ↓
   ┌─┼─┐  ┌─┼─┐  ┌─┼─┐
   d e f  d e f  d e f   ← Choose letter for digit '3'
   ↓ ↓ ↓  ↓ ↓ ↓  ↓ ↓ ↓
  ad ae af bd be bf cd ce cf  ← BASE CASE: add to result!
            
After each leaf, we BACKTRACK:
  - Remove last char from temp
  - Try next letter
```

---

## Why StringBuilder?

```java
// BAD: String concatenation creates new objects
String temp = "";
temp = temp + c;  // Creates new String each time!

// GOOD: StringBuilder modifies in place
StringBuilder temp = new StringBuilder();
temp.append(c);                    // O(1) amortized
temp.deleteCharAt(temp.length()-1); // O(1) backtrack
```

---

## The Code Flow

```java
helper(int i, String digits, List<String> answer, StringBuilder temp) {
    
    // BASE CASE: processed all digits
    if (i == digits.length()) {
        answer.add(temp.toString());
        return;
    }

    // Get letters for current digit
    char digit = digits.charAt(i);
    char[] letters = map.get(digit);
    
    // Try each letter
    for (char c : letters) {
        temp.append(c);              // 1. CHOOSE
        helper(i + 1, ...);          // 2. EXPLORE
        temp.deleteCharAt(...);      // 3. UNCHOOSE
    }
}
```

---

## Complexity Analysis

| Solution | Time | Space | Correct? |
|----------|------|-------|----------|
| Nested Loops | O(4^n) | O(4^n) | ❌ Not scalable |
| Iterative | O(4^n) | O(4^n) | ✅ Works |
| **Backtracking** | O(4^n) | O(n)* | ✅ Optimal |

*Recursion stack depth = n, not counting output space

- **4^n** because each digit maps to at most 4 letters (for 7 and 9)
- **n** = number of digits (max 4)

---

## Key Takeaways

1. **Backtracking pattern**: Choose → Explore → Unchoose
2. **StringBuilder** for efficient string building
3. **Base case**: when index reaches string length
4. **Recursion tree**: each level = one digit, each branch = one letter
5. **Small n (≤4)** = backtracking is perfect (4^4 = 256 max combinations)
