# 1438. Longest Continuous Subarray With Absolute Diff Less Than or Equal to Limit

Given an array of integers `nums` and an integer `limit`, return the size of the **longest non-empty subarray** such that the absolute difference between any two elements of this subarray is less than or equal to `limit`.

## Example 1

**Input:** `nums = [8,2,4,7], limit = 4`  
**Output:** `2`  
**Explanation:** The subarray `[2,4]` and `[4,7]` have max absolute diff ≤ 4.

## Example 2

**Input:** `nums = [10,1,2,4,7,2], limit = 5`  
**Output:** `4`  
**Explanation:** The subarray `[2,4,7,2]` is the longest since max diff is |2-7| = 5 ≤ 5.

## Example 3

**Input:** `nums = [4,2,2,2,4,4,2,2], limit = 0`  
**Output:** `3`

## Constraints

- `1 <= nums.length <= 10^5`
- `1 <= nums[i] <= 10^9`
- `0 <= limit <= 10^9`
