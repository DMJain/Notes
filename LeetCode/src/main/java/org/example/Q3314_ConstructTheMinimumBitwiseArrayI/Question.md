# 3314. Construct the Minimum Bitwise Array I

**Difficulty**: Easy  
**Topics**: Array, Bit Manipulation

## Problem Statement

You are given an array `nums` consisting of `n` **prime** integers.

You need to construct an array `ans` of length `n`, such that, for each index `i`, the bitwise OR of `ans[i]` and `ans[i] + 1` is equal to `nums[i]`, i.e.:

```
ans[i] OR (ans[i] + 1) == nums[i]
```

Additionally, you must **minimize** each value of `ans[i]` in the resulting array.

If it is not possible to find such a value for `ans[i]` that satisfies the condition, then set `ans[i] = -1`.

## Examples

### Example 1:
```
Input: nums = [2,3,5,7]
Output: [-1,1,4,3]
```

**Explanation**:
- For `i = 0`: No value satisfies `ans[0] OR (ans[0] + 1) = 2`, so `ans[0] = -1`
- For `i = 1`: Smallest `ans[1]` is `1`, because `1 OR (1 + 1) = 1 OR 2 = 3`
- For `i = 2`: Smallest `ans[2]` is `4`, because `4 OR (4 + 1) = 4 OR 5 = 5`
- For `i = 3`: Smallest `ans[3]` is `3`, because `3 OR (3 + 1) = 3 OR 4 = 7`

### Example 2:
```
Input: nums = [11,13,31]
Output: [9,12,15]
```

**Explanation**:
- For `i = 0`: `9 OR (9 + 1) = 9 OR 10 = 11`
- For `i = 1`: `12 OR (12 + 1) = 12 OR 13 = 13`
- For `i = 2`: `15 OR (15 + 1) = 15 OR 16 = 31`

## Constraints

- `1 <= nums.length <= 100`
- `2 <= nums[i] <= 1000`
- `nums[i]` is a prime number
