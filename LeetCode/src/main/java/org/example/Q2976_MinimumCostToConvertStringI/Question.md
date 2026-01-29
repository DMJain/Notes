# 2976. Minimum Cost to Convert String I

**Difficulty**: Medium  
**Topics**: Graph, Shortest Path, Floyd-Warshall

## Problem

You are given two 0-indexed strings `source` and `target`, both of length `n` and consisting of lowercase English letters. You are also given two 0-indexed character arrays `original` and `changed`, and an integer array `cost`, where `cost[i]` represents the cost of changing the character `original[i]` to the character `changed[i]`.

You start with the string `source`. In one operation, you can pick a character `x` from the string and change it to the character `y` at a cost of `z` if there exists any index `j` such that `cost[j] == z`, `original[j] == x`, and `changed[j] == y`.

Return the minimum cost to convert the string `source` to the string `target` using any number of operations. If it is impossible to convert `source` to `target`, return `-1`.

**Note**: There may exist indices `i`, `j` such that `original[j] == original[i]` and `changed[j] == changed[i]`.

## Examples

### Example 1
```
Input: source = "abcd", target = "acbe", 
       original = ["a","b","c","c","e","d"], 
       changed = ["b","c","b","e","b","e"], 
       cost = [2,5,5,1,2,20]
Output: 28
Explanation: To convert "abcd" to "acbe":
- Change index 1 'b' → 'c' at cost 5
- Change index 2 'c' → 'e' at cost 1
- Change index 2 'e' → 'b' at cost 2
- Change index 3 'd' → 'e' at cost 20
Total: 5 + 1 + 2 + 20 = 28
```

### Example 2
```
Input: source = "aaaa", target = "bbbb", 
       original = ["a","c"], changed = ["c","b"], cost = [1,2]
Output: 12
Explanation: To change 'a' to 'b': a → c (cost 1) → b (cost 2) = 3
Total for 4 characters: 3 × 4 = 12
```

### Example 3
```
Input: source = "abcd", target = "abce", 
       original = ["a"], changed = ["e"], cost = [10000]
Output: -1
Explanation: Cannot change 'd' to 'e' (no path exists)
```

## Constraints

- `1 <= source.length == target.length <= 10^5`
- `source`, `target` consist of lowercase English letters
- `1 <= cost.length == original.length == changed.length <= 2000`
- `original[i]`, `changed[i]` are lowercase English letters
- `1 <= cost[i] <= 10^6`
- `original[i] != changed[i]`
