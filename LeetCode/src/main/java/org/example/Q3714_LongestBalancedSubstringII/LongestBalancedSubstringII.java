package org.example.Q3714_LongestBalancedSubstringII;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 3714. Longest Balanced Substring II
 *
 * Approach: Decompose into 7 sub-problems by character subset, each O(n).
 *
 * Only 3 chars ('a','b','c'), so a balanced substring uses 1, 2, or 3 distinct
 * chars.
 * We solve each case independently and take the max:
 *
 * 1. Single-char: longest consecutive run of 'a', 'b', or 'c' — trivially
 * balanced.
 * 2. Two-char pairs (ab, ac, bc): prefix-sum difference encoding.
 * - Treat char x as +1, char y as -1 in a running diff.
 * - Same diff at indices i,j → equal counts of x,y between them → balanced.
 * - Obstacle tracking: if the 3rd char appears between i and j, the substring
 * contains a char not in {x,y}, so it's NOT a valid 2-char balanced substring.
 * We use clear_idx to invalidate stored diffs from before the obstacle.
 * 3. Three-char (abc): encode (cnt_a−cnt_b, cnt_b−cnt_c) as a single long.
 * - Same state at indices i,j → all 3 counts equal between them → balanced.
 *
 * Time: O(n) — each sub-problem is a single pass
 * Space: O(n) — diff array for find2, HashMap for find3
 */
public class LongestBalancedSubstringII {

    public int longestBalanced(String s) {
        char[] c = s.toCharArray();
        int n = c.length;

        // --- Sub-problem 1: Single-char runs ---
        // Track longest consecutive run of each character
        int curA = 0, curB = 0, curC = 0;
        int maxA = 0, maxB = 0, maxC = 0;

        for (int i = 0; i < n; i++) {
            if (c[i] == 'a') {
                curA = (i > 0 && c[i - 1] == 'a') ? curA + 1 : 1;
                maxA = Math.max(maxA, curA);
            } else if (c[i] == 'b') {
                curB = (i > 0 && c[i - 1] == 'b') ? curB + 1 : 1;
                maxB = Math.max(maxB, curB);
            } else {
                curC = (i > 0 && c[i - 1] == 'c') ? curC + 1 : 1;
                maxC = Math.max(maxC, curC);
            }
        }

        int res = Math.max(Math.max(maxA, maxB), maxC);

        // --- Sub-problem 2: Two-char balanced (3 pairs) ---
        res = Math.max(res, find2(c, 'a', 'b'));
        res = Math.max(res, find2(c, 'a', 'c'));
        res = Math.max(res, find2(c, 'b', 'c'));

        // --- Sub-problem 3: Three-char balanced ---
        res = Math.max(res, find3(c));

        return res;
    }

    /**
     * Finds longest substring containing ONLY chars x,y with equal counts.
     *
     * Diff encoding: x → +1, y → -1, other → obstacle (reset).
     * Same diff at two indices = balanced between them.
     * Obstacle (3rd char) invalidates all stored diffs from before it.
     */
    private int find2(char[] c, char x, char y) {
        int n = c.length, maxLen = 0;

        // first[diff] = earliest index where this diff was seen
        // diff ranges from -n to +n, so we offset by n → index 0..2n
        int[] first = new int[2 * n + 1];
        Arrays.fill(first, -2); // -2 = "never seen"

        int clearIdx = -1; // index of most recent obstacle (3rd char)
        int diff = n; // offset so diff=0 maps to index n
        first[diff] = -1; // diff=0 is "seen" at virtual index -1

        for (int i = 0; i < n; i++) {
            if (c[i] != x && c[i] != y) {
                // 3rd char = obstacle: invalidate everything before
                clearIdx = i;
                diff = n; // reset diff to 0 (offset)
                first[diff] = clearIdx; // anchor new segment at obstacle
            } else {
                if (c[i] == x)
                    diff++;
                else
                    diff--;

                if (first[diff] < clearIdx) {
                    // This diff was last seen BEFORE the obstacle → stale, overwrite
                    first[diff] = i;
                } else {
                    // Valid match: same diff seen after obstacle
                    maxLen = Math.max(maxLen, i - first[diff]);
                }
            }
        }

        return maxLen;
    }

    /**
     * Finds longest substring containing all 3 chars with equal counts.
     *
     * State = (cnt_a - cnt_b, cnt_b - cnt_c) encoded as a single long:
     * 'a' → state += 1_000_001 (adds 1 to both diffs conceptually)
     * 'b' → state -= 1_000_000 (subtracts 1 from diff_ab only)
     * 'c' → state -= 1 (subtracts 1 from diff_bc only)
     *
     * Same state at two indices → all 3 count-differences unchanged → balanced.
     */
    private int find3(char[] c) {
        long state = Long.MAX_VALUE / 2; // large offset to avoid negatives
        Map<Long, Integer> first = new HashMap<>();
        first.put(state, -1); // state before any chars = index -1

        int maxLen = 0;

        for (int i = 0; i < c.length; i++) {
            if (c[i] == 'a')
                state += 1_000_001;
            else if (c[i] == 'b')
                state -= 1_000_000;
            else
                state--;

            if (first.containsKey(state)) {
                maxLen = Math.max(maxLen, i - first.get(state));
            } else {
                first.put(state, i);
            }
        }

        return maxLen;
    }

    // ==================== TEST CASES ====================
    public static void main(String[] args) {
        LongestBalancedSubstringII sol = new LongestBalancedSubstringII();

        // Example 1: "abba" → a×2, b×2
        System.out.println("Example 1: \"abbac\"");
        System.out.println("Expected: 4, Got: " + sol.longestBalanced("abbac"));

        // Example 2: "abc" → a×1, b×1, c×1
        System.out.println("\nExample 2: \"aabcc\"");
        System.out.println("Expected: 3, Got: " + sol.longestBalanced("aabcc"));

        // Example 3: "ab" or "ba" → a×1, b×1
        System.out.println("\nExample 3: \"aba\"");
        System.out.println("Expected: 2, Got: " + sol.longestBalanced("aba"));

        // Edge: Single character
        System.out.println("\nEdge 1 (single char): \"a\"");
        System.out.println("Expected: 1, Got: " + sol.longestBalanced("a"));

        // Edge: All same character
        System.out.println("\nEdge 2 (all same): \"aaaa\"");
        System.out.println("Expected: 4, Got: " + sol.longestBalanced("aaaa"));

        // Edge: Full string balanced with 3 chars
        System.out.println("\nEdge 3 (all 3 balanced): \"abcabc\"");
        System.out.println("Expected: 6, Got: " + sol.longestBalanced("abcabc"));
    }
}
