# Balanced Binary Tree - Explanation

> **Prerequisites**: Tree traversal (DFS), Recursion basics, Understanding of tree height vs depth  
> **Related Problems**:  
> - [LeetCode 104 - Maximum Depth of Binary Tree](https://leetcode.com/problems/maximum-depth-of-binary-tree/) (Core building block - calculate tree height)
> - [LeetCode 111 - Minimum Depth of Binary Tree](https://leetcode.com/problems/minimum-depth-of-binary-tree/) (Similar height traversal pattern)
> - [LeetCode 543 - Diameter of Binary Tree](https://leetcode.com/problems/diameter-of-binary-tree/) (Same bottom-up pattern with height)
> - [LeetCode 1382 - Balance a Binary Search Tree](https://leetcode.com/problems/balance-a-binary-search-tree/) (Constructing balanced tree)

---

## Problem in Simple Words

Check if a binary tree is **height-balanced**: for **every** node, the height difference between its left and right subtrees must be ≤ 1.

**Example**: Tree `[3,9,20,null,null,15,7]` is balanced (all nodes have height difference ≤ 1). Tree `[1,2,2,3,3,null,null,4,4]` is NOT balanced (node 2's left subtree has height 3, right has height 1 → difference = 2).

---

## Solution 1: Brute Force (Top-Down) ❌

### The Natural Thought

"For each node, I'll calculate the height of its left subtree, calculate the height of its right subtree, check if they differ by more than 1, then recursively check the children."

### Approach

```python
def isBalanced(root):
    if not root:
        return True
    
    # Calculate heights (SEPARATE recursive calls)
    left_height = getHeight(root.left)
    right_height = getHeight(root.right)
    
    # Check current node
    if abs(left_height - right_height) > 1:
        return False
    
    # Recursively check children
    return isBalanced(root.left) and isBalanced(root.right)

def getHeight(node):
    if not node:
        return 0
    return 1 + max(getHeight(node.left), getHeight(node.right))
```

### Why It's Bad

**Redundant Work**: For each node, we call `getHeight()` which traverses the ENTIRE subtree. Then we recursively call `isBalanced()` on children, which AGAIN calculates heights of the same nodes!

```
For node at level 0: getHeight traverses all n nodes
For nodes at level 1: getHeight traverses (n-1) nodes
For nodes at level 2: getHeight traverses (n-3) nodes
... and so on
```

### Example Where It's SLOW ❌

**Worst Case: Skewed Tree (n = 5000)**

```
         1        ← isBalanced calls getHeight on ALL 5000 nodes
        /
       2          ← isBalanced calls getHeight on 4999 nodes
      /
     3            ← isBalanced calls getHeight on 4998 nodes
    /
   ...
  /
5000

Operations = n + (n-1) + (n-2) + ... + 1
           = n(n+1)/2
           = 5000 × 5001 / 2
           = 12,502,500 operations!
```

For a balanced tree, it's O(n log n), but for a skewed tree: **O(n²) = 12.5 MILLION operations!**

### Complexity Analysis

| Solution | Time | Space | Status | Why |
|----------|------|-------|--------|-----|
| Top-Down (Brute) | O(n²) | O(n) | ✅ TLE | Recalculates height at every level |
| **Bottom-Up DFS** | **O(n)** | **O(n)** | **✅ Optimal** | Single traversal |

> 💭 **"We're recalculating the same heights over and over at each level. What if we calculate height ONCE per node and check balance AT THE SAME TIME during the height calculation?"**

---

## Solution 2: Bottom-Up DFS (Optimal) ✅

### The Connection 🔗

Let's trace our thinking:
- **Brute Force** was slow because: we calculated height at every node (O(n) work), then recursively did the same for children → O(n²)
- **What we need**: A way to compute height AND detect imbalance in a SINGLE traversal
- **The insight**: In post-order traversal, we already visit children before parent. When we return height from children, we can check balance RIGHT THERE!

### The Key Insight 💡

**Use `-1` as a sentinel value!**

During height calculation:
- If a subtree is unbalanced → return `-1` immediately (short-circuit)
- If balanced → return actual height

This way, `-1` **propagates up** the moment ANY node is unbalanced, and we stop all further computation!

```
Normal height:  0, 1, 2, 3, ...  (valid heights)
Sentinel:       -1               (means "already broken, stop checking")
```

### The Algorithm

1. **Base case**: `null` node → return height `0`
2. Get left subtree height (or `-1` if unbalanced)
3. **Short-circuit**: If `left == -1` → immediately return `-1` (don't even check right!)
4. Get right subtree height (or `-1` if unbalanced)
5. **Short-circuit**: If `right == -1` → immediately return `-1`
6. **Check current node**: If `|left - right| > 1` → return `-1` (unbalanced here)
7. **Return valid height**: `1 + max(left, right)`

```python
def isBalanced(root):
    def dfs(node):
        if not node:
            return 0
        
        left = dfs(node.left)
        if left == -1: return -1    # Short-circuit!
        
        right = dfs(node.right)
        if right == -1: return -1   # Short-circuit!
        
        if abs(left - right) > 1:
            return -1               # Unbalanced at this node
        
        return 1 + max(left, right) # Valid height
    
    return dfs(root) != -1
```

### Step-by-Step Walkthrough

**Example 2**: `[1,2,2,3,3,null,null,4,4]` → Expected: `false`

```
Tree Structure:
           1
          / \
         2   2
        / \
       3   3
      / \
     4   4
```

**Post-order DFS Traversal** (left → right → current):

| Step | Node | Left | Right | |Left-Right| | Return | Note |
|------|------|------|-------|-------------|--------|------|
| 1 | 4 (leftmost) | 0 | 0 | 0 ≤ 1 ✓ | 1 | Valid |
| 2 | 4 (right of 3) | 0 | 0 | 0 ≤ 1 ✓ | 1 | Valid |
| 3 | 3 (left of 2) | 1 | 1 | 0 ≤ 1 ✓ | 2 | Valid |
| 4 | 3 (right of 2) | 0 | 0 | 0 ≤ 1 ✓ | 1 | Valid |
| 5 | 2 (left of 1) | 2 | 1 | 1 ≤ 1 ✓ | 3 | Valid |
| 6 | 2 (right of 1) | 0 | 0 | 0 ≤ 1 ✓ | 1 | Valid |
| 7 | 1 (root) | 3 | 1 | **2 > 1** ❌ | **-1** | **UNBALANCED!** |

Result: `dfs(root) = -1`, so `isBalanced = false` ✅

### Visual Diagram

**Height Propagation (Post-order)**:

```
           1 [h=?]          STEP 7: |3-1|=2 > 1 ❌ → return -1
          / \
   [h=3] 2   2 [h=1]        STEP 5-6: heights bubble up
        / \
 [h=2] 3   3 [h=1]          STEP 3-4: heights bubble up
      / \
[h=1]4   4 [h=1]            STEP 1-2: base cases return 1

Processing Order (Post-order):
┌───────────────────────────────────────────────────────────┐
│  4(L) → 4(R) → 3(L) → 3(R) → 2(L) → 2(R) → 1(root)       │
│   ↑       ↑      ↑       ↑      ↑       ↑      ↑          │
│  h=1    h=1    h=2     h=1    h=3     h=1    -1 ❌        │
└───────────────────────────────────────────────────────────┘
```

**Short-Circuit in Action** (when unbalanced found early):

```
           1 [doesn't compute right subtree if left returns -1]
          /
         2      ← if this returns -1
        /
       3        
      /
     4

When dfs(left child of 1) returns -1:
  left = dfs(node.left)   → returns -1
  if left == -1: return -1  ← STOPS HERE! Never calls dfs(node.right)
```

---

## Complexity Analysis (Optimal)

| Solution | Time | Space | Status | Why |
|----------|------|-------|--------|-----|
| Top-Down (Brute) | O(n²) | O(n) | ✅ TLE | Height recalculated at each level |
| **Bottom-Up DFS** | **O(n)** | **O(n)** | **✅ Optimal** | Each node visited exactly once |

**Time**: O(n) — Visit each of the n nodes exactly once. Height computation happens during the same visit.

**Space**: O(n) — Recursion stack depth = height of tree. Worst case (skewed tree) = n. Average case (balanced) = O(log n).

**Comparison for n = 5000**:
- Top-Down: 12,502,500 operations
- Bottom-Up: 5,000 operations
- **Speedup: 2,500× faster!**

---

## Key Takeaways

1. **Bottom-up beats top-down** when computing cumulative tree properties (height, diameter, etc.) — avoid redundant traversals by computing child info first

2. **Sentinel values enable short-circuit optimization** — Using `-1` to mean "already broken" lets us stop early and propagate failure upward instantly

3. **Post-order traversal pattern** — When a problem needs "child info before parent decision," think post-order (left → right → current)

4. **Recognize the O(n²) trap** — Nested height calculations at each level is a common antipattern. Ask: "Am I recalculating the same thing?"

---

## The Journey (TL;DR)

```
🐢 Top-Down Brute Force → O(n²) recalculates heights at every level
         ↓
💡 "Can we compute height and check balance in ONE pass?"
         ↓
🤔 "Post-order gives us child heights before we process parent..."
         ↓
💡 "Use -1 as sentinel! Return -1 the moment we find imbalance."
         ↓
✅ Bottom-Up DFS → O(n) with immediate short-circuit!
```
