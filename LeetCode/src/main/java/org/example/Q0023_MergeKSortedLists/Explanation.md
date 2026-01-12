# Merge k Sorted Lists - Explanation

> **Prerequisites**: [Q0021 Merge Two Sorted Lists](https://leetcode.com/problems/merge-two-sorted-lists/). If you can merge two lists, this problem is about scaling that up efficiently.

> **Related Problems**: [Q0004 Median of Two Sorted Arrays](../Q0004_MedianOfTwoSortedArray/Explanation.md), [Q0088 Merge Sorted Array](https://leetcode.com/problems/merge-sorted-array/)

> **Honorable Mention**: *Divide & Conquer* approach achieves O(N log k) like MinHeap but with O(1) extra space (excluding recursion stack). Same complexity as heap, different trade-offs.

## Problem in Simple Words

Merge `k` sorted linked lists into one big sorted linked list.

**Example**: `[[1,4,5], [1,3,4], [2,6]]` → `[1,1,2,3,4,4,5,6]`

---

## Solution 1: Brute Force (Collect All, Sort) ❌ (Too Slow)

### Approach
Traverse all lists, collect all values in an array, sort it, create new linked list.

```java
List<Integer> all = new ArrayList<>();
for (ListNode list : lists) {
    while (list != null) {
        all.add(list.val);
        list = list.next;
    }
}
Collections.sort(all);
// Create linked list from sorted array
```

### Why It's Bad
- Collect: O(N)
- Sort: O(N log N)
- **Ignores that lists are already sorted!** We're throwing away useful information.

> 💭 **The lists are ALREADY sorted! We should leverage that. What if we merged them two at a time, like we do with two sorted lists?**

---

## Solution 2: Sequential Merge (One by One) ❌ (Works but Suboptimal)

### The Natural Thought
"I know how to merge 2 sorted lists! Let me just merge list[0] with list[1], then merge result with list[2], etc."

### Approach

```java
ListNode result = lists[0];
for (int i = 1; i < k; i++) {
    result = mergeTwoLists(result, lists[i]);
}
```

### Step-by-Step

```
lists = [[1,4,5], [1,3,4], [2,6]]

Step 1: Merge [1,4,5] + [1,3,4] = [1,1,3,4,4,5]  ← Scan 3+3=6 nodes
Step 2: Merge [1,1,3,4,4,5] + [2,6] = [1,1,2,3,4,4,5,6]  ← Scan 6+2=8 nodes

Total scans: 6 + 8 = 14
```

### Example Where It's SLOW ❌

```
k = 1000 lists, each with 10 nodes (N = 10,000 total)

Merge 1: result = 10 nodes
Merge 2: result = 20 nodes (scan 10+10)
Merge 3: result = 30 nodes (scan 20+10)
...
Merge k: result = 10k nodes (scan 9990+10)

Total: 10 + 20 + 30 + ... + 10k ≈ k² work!
Time: O(kN) where N = total nodes
```

### Why It's Not Ideal
- Result grows after each merge
- Later merges scan the ENTIRE accumulated result
- **O(kN)** when we could do **O(N log k)**

> 💭 **The problem is that later merges scan the huge accumulated result. What if we could always merge lists of SIMILAR size? Like tournament brackets!**

---

## Solution 3: Divide & Conquer ✅ (Optimal)

### The Connection 🔗
Let's trace our thinking:
- **Brute Force** was slow because: ignored sorted property
- **Sequential Merge** was slow because: later merges scan entire accumulated result
- **What we need**: merge in a balanced way so no single merge is too big → **Divide & Conquer!**

### The Key Insight 💡
Instead of merging 1 + 2 + 3 + ... + k, merge in **pairs like a tournament**:

```
Round 1: Merge (list0, list1), (list2, list3), (list4, list5)...
Round 2: Merge pairs from Round 1
Round 3: Merge pairs from Round 2
...
Final: One merged list
```

Each round does O(N) work total, and there are O(log k) rounds!

### Why This Works

```
Sequential:                    Divide & Conquer:
     ↓                              ↓
L0 + L1 = R1  (size 2n)       L0+L1  L2+L3  L4+L5  L6+L7
R1 + L2 = R2  (size 3n)           \   /      \   /
R2 + L3 = R3  (size 4n)           R01   +   R23    ← Balanced!
...                                  \      /
Rk = result (O(kN) total)            Final
                                 O(N log k) total
```

### The Algorithm

```
function mergeKLists(lists, start, end):
    if start == end: return lists[start]
    if start > end: return null
    
    mid = (start + end) / 2
    left = mergeKLists(lists, start, mid)
    right = mergeKLists(lists, mid+1, end)
    return mergeTwoLists(left, right)
```

---

## Step-by-Step Walkthrough

**lists = [[1,4,5], [1,3,4], [2,6], [0,9]]**

```
Initial: 4 lists
         [1,4,5]  [1,3,4]  [2,6]  [0,9]
              \     /          \    /
Round 1:   [1,1,3,4,4,5]     [0,2,6,9]
                  \              /
Round 2:      [0,1,1,2,3,4,4,5,6,9]

Work per round:
  Round 1: 3+3 + 2+2 = 10 nodes
  Round 2: 6+4 = 10 nodes
  Total: 20 = 2 × N
  
Rounds: log₂(4) = 2
Total: O(N log k)
```

---

## Visual: Sequential vs Divide & Conquer

```
SEQUENTIAL (O(kN)):
L0 ──┬── Merge ── R1 ──┬── Merge ── R2 ──┬── Merge ── FINAL
L1 ──┘                 │                 │
L2 ────────────────────┘                 │
L3 ──────────────────────────────────────┘

Each merge scans EVERYTHING accumulated so far!

DIVIDE & CONQUER (O(N log k)):
L0 ──┬── Merge ──┐
L1 ──┘           ├── Merge ── FINAL
L2 ──┬── Merge ──┘
L3 ──┘

Balanced! At most log k levels.
```

---

## Honorable Mention: MinHeap Approach

```java
PriorityQueue<ListNode> heap = new PriorityQueue<>((a,b) -> a.val - b.val);

// Add first node of each list
for (ListNode list : lists) {
    if (list != null) heap.offer(list);
}

// Pop smallest, add its next
while (!heap.isEmpty()) {
    ListNode smallest = heap.poll();
    result.next = smallest;
    if (smallest.next != null) heap.offer(smallest.next);
}
```

- **Time**: O(N log k) — each of N nodes is pushed/popped once, heap ops are O(log k)
- **Space**: O(k) — heap holds up to k nodes
- Same time as D&C, but uses extra space for heap

---

## Complexity Analysis

| Solution | Time | Space | Correct? | Why? |
|----------|------|-------|----------|------|
| Brute Force | O(N log N) | O(N) | ✅ But ignores sorted | Sort all values |
| Sequential Merge | O(kN) | O(1) | ✅ But slow | Later merges scan huge result |
| MinHeap | O(N log k) | O(k) | ✅ **Optimal** | Each node: O(log k) heap op |
| **Divide & Conquer** | O(N log k) | O(log k)* | ✅ **Optimal** | log k merge rounds |

*Recursion stack depth

---

## Key Takeaways

1. **Leverage sorted property**: Don't throw away the fact that lists are already sorted
2. **Balanced merging**: Divide & Conquer prevents any single merge from being too big
3. **Tournament bracket pattern**: Merge pairs, then merge pairs of results... log k rounds
4. **MinHeap alternative**: Same complexity, trade-off between space and implementation style

---

## The Journey (TL;DR)

```
🐢 Brute Force: Collect all, sort → IGNORES SORTED (O(N log N))
         ↓
💡 "Lists are sorted! Merge them like Q21."
         ↓
🔁 Sequential Merge: One by one → SLOW (O(kN), later merges huge)
         ↓
💡 "Later merges scan too much! What if we merged in balanced pairs?"
         ↓
✅ Divide & Conquer: Tournament brackets → OPTIMAL (O(N log k))
```
