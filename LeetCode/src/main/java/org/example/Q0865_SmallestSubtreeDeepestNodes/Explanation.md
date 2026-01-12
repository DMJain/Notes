# Smallest Subtree with all the Deepest Nodes - Explanation

## Problem in Simple Words
Find the **Lowest Common Ancestor (LCA)** of all the **deepest nodes** in the tree.
- "Deepest nodes" = nodes with the maximum distance from the root.
- "Smallest subtree" = the node that is the common parent to all these deepest nodes, and is as deep as possible itself.

**Example**:
```
        3
       / \
      5   1
     / \
    6   2
       / \
      7   4
```
- Deepest nodes: 7, 4 (depth 3)
- Common ancestor of 7 and 4: **2**
- Subtree rooted at 2 contains both 7 and 4.
- **Answer: Node 2**

---

## Solution 1: Two Pass (Get Max Depth → Find LCA) ❌ (Slower)

### Approach
1. **Pass 1**: Find the maximum depth of the tree (`maxDepth`).
2. **Pass 2**: Find the LCA of all nodes that are at `maxDepth`.
   - Standard LCA logic:
     - If node is null, return null.
     - If node depth == maxDepth, return node.
     - Recurse left and right.
     - If both left and right return non-null, current node is LCA.
     - Else return the non-null child.

### Why It's Not Ideal
- Requires **two traversals** of the tree.
- Need to pass depth information around or recalculate it.
- **O(N)** time, but 2 passes.

> 💭 **Two passes work, but can we do it in one pass? What if we calculated height AND found the LCA simultaneously?**

---

## Solution 2: One Pass DFS with Pair ✅ (Optimal)

### The Connection 🔗
Let's trace our thinking:
- **Two Pass** works but is inefficient because: traverses tree twice
- **What we need**: calculate depth AND find LCA in one traversal
- **Key insight**: Return BOTH depth and LCA from each subtree!

### The Key Insight 💡
We can calculate **height** and find the **LCA** simultaneously!

Return a `Pair<Depth, Node>` from each node:
- `Depth`: The maximum depth of the subtree rooted at this node.
- `Node`: The LCA of the deepest nodes **within this subtree**.

**Logic**:
1. Get result from **Left** child: `(leftDepth, leftLCA)`
2. Get result from **Right** child: `(rightDepth, rightLCA)`
3. **Compare Depths**:
   - If `leftDepth > rightDepth`: The deepest nodes are ONLY on the left.
     - Current LCA is `leftLCA`.
     - Current Depth is `leftDepth + 1`.
   - If `rightDepth > leftDepth`: The deepest nodes are ONLY on the right.
     - Current LCA is `rightLCA`.
     - Current Depth is `rightDepth + 1`.
   - If `leftDepth == rightDepth`: Deepest nodes are on **BOTH** sides!
     - **Current node IS the LCA** (split point).
     - Current Depth is `leftDepth + 1`.

### Why This Works

```
Two Pass:                  One Pass:
    ↓                          ↓
Find max depth            Return (depth, LCA) pair
Then find LCA             Update both as we go up
2 traversals              1 traversal
```

### Step-by-Step Walkthrough

**Tree**:
```
      3
     / \
    5   1
   / \
  6   2
     / \
    7   4
```

**1. Leaf Nodes (7, 4, 6, 1)**
- `dfs(7)`: Left=0, Right=0. Equal! Return `(1, Node 7)`
- `dfs(4)`: Left=0, Right=0. Equal! Return `(1, Node 4)`
- `dfs(6)`: Left=0, Right=0. Equal! Return `(1, Node 6)`
- `dfs(1)`: Left=0, Right=0. Equal! Return `(1, Node 1)`

**2. Node 2 (Children: 7, 4)**
- Left (7): `(1, Node 7)`
- Right (4): `(1, Node 4)`
- **Depths Equal (1 == 1)**:
  - Deepest nodes are on both sides.
  - **I am the LCA!**
  - Return `(2, Node 2)` (Depth 1+1=2)

**3. Node 5 (Children: 6, 2)**
- Left (6): `(1, Node 6)`
- Right (2): `(2, Node 2)`
- **Right Depth > Left Depth (2 > 1)**:
  - Deepest nodes are on the right.
  - Inherit answer from right.
  - Return `(3, Node 2)` (Depth 2+1=3)

**4. Root 3 (Children: 5, 1)**
- Left (5): `(3, Node 2)`
- Right (1): `(1, Node 1)`
- **Left Depth > Right Depth (3 > 1)**:
  - Deepest nodes are on the left.
  - Inherit answer from left.
  - Return `(4, Node 2)` (Depth 3+1=4)

**Final Result**: Node 2. ✅

---

## Visual: Decision Tree

```
       [Node 3]
      /        \
   (3, N2)    (1, N1)
   Left > Right
   Return (4, N2)
     /
  [Node 5]
  /      \
(1, N6)  (2, N2)
         Right > Left
         Return (3, N2)
           /
        [Node 2]
        /      \
     (1, N7)  (1, N4)
     Left == Right
     Return (2, N2) ← NEW LCA FOUND HERE!
```

---

## The Core Logic

```
At each node:
┌─────────────────────────────────────────────────┐
│                                                 │
│   leftDepth > rightDepth?                       │
│         ↓                                       │
│   Deepest on LEFT → pass up leftLCA             │
│                                                 │
│   rightDepth > leftDepth?                       │
│         ↓                                       │
│   Deepest on RIGHT → pass up rightLCA           │
│                                                 │
│   leftDepth == rightDepth?                      │
│         ↓                                       │
│   Deepest on BOTH → I AM the LCA!               │
│                                                 │
└─────────────────────────────────────────────────┘
```

---

## Complexity Analysis

| Solution | Time | Space | Correct? | Why? |
|----------|------|-------|----------|------|
| Two Pass | O(N) | O(H) | ✅ Works | 2 traversals |
| **One Pass DFS** | O(N) | O(H) | ✅ **Optimal** | 1 traversal |

- **Time**: Visit every node once.
- **Space**: Recursion stack height (H).

---

## Key Takeaways

1. **LCA Logic**: If deepest nodes are on both sides, current node is LCA. If only on one side, pass that side's LCA up.
2. **Bottom-Up DFS**: Calculate depth and LCA on the way back up from recursion.
3. **Pair Return**: Returning multiple values (Depth + Node) simplifies state management.

---

## The Journey (TL;DR)

```
🐢 Two Pass: Find depth, then find LCA → WORKS but 2 passes
         ↓
💡 "Can we calculate both at once?"
         ↓
✅ One Pass: Return (depth, LCA) pair → OPTIMAL (1 traversal)
```
