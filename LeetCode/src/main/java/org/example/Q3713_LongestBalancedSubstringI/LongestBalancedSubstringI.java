package org.example.Q3713_LongestBalancedSubstringI;

/**
 * 3713. Longest Balanced Substring I
 *
 * Approach: O(n²) enumeration with O(1) balance check via uniq == cntMax
 *
 * For each starting index l, extend right pointer r and maintain:
 * - freq[26]: character frequencies in current window
 * - uniq: count of distinct characters
 * - maxF: highest frequency among all characters
 * - cntMax: how many characters currently have frequency == maxF
 *
 * Key invariant: uniq == cntMax means ALL distinct characters share the
 * max frequency → all frequencies are equal → substring is balanced.
 *
 * Time: O(n²) — two nested loops, O(1) work per extension
 * Space: O(1) — freq[26] is constant size
 */
public class LongestBalancedSubstringI {

    public int longestBalanced(String s) {
        int cnt = 1, n = s.length();

        for (int l = 0; l < n; l++) {
            int[] freq = new int[26];
            int uniq = 0, maxF = 0, cntMax = 0;

            for (int r = l; r < n; r++) {
                int idx = s.charAt(r) - 'a';
                freq[idx]++;
                int f = freq[idx];

                // New distinct character
                if (f == 1)
                    uniq++;

                // Update max frequency tracking
                if (f > maxF) {
                    maxF = f;
                    cntMax = 1; // Only this char has new max
                } else if (f == maxF) {
                    cntMax++; // Another char reached max
                }

                // uniq == cntMax → all distinct chars have same freq → balanced
                if (uniq == cntMax) {
                    cnt = Math.max(cnt, r - l + 1);
                }
            }
        }
        return cnt;
    }

    // ==================== TEST CASES ====================
    public static void main(String[] args) {
        LongestBalancedSubstringI solution = new LongestBalancedSubstringI();

        // Example 1: "abba" → a×2, b×2
        System.out.println("Example 1: \"abbac\"");
        System.out.println("Expected: 4, Got: " + solution.longestBalanced("abbac"));

        // Example 2: "zabc" → z×1, a×1, b×1, c×1
        System.out.println("\nExample 2: \"zzabccy\"");
        System.out.println("Expected: 4, Got: " + solution.longestBalanced("zzabccy"));

        // Example 3: "ab" or "ba" → a×1, b×1
        System.out.println("\nExample 3: \"aba\"");
        System.out.println("Expected: 2, Got: " + solution.longestBalanced("aba"));

        // Edge: Single character → trivially balanced
        System.out.println("\nEdge 1 (single char): \"a\"");
        System.out.println("Expected: 1, Got: " + solution.longestBalanced("a"));

        // Edge: All same character → always balanced
        System.out.println("\nEdge 2 (all same): \"aaaa\"");
        System.out.println("Expected: 4, Got: " + solution.longestBalanced("aaaa"));

        // Edge: Alternating → full string balanced
        System.out.println("\nEdge 3 (alternating): \"abcabc\"");
        System.out.println("Expected: 6, Got: " + solution.longestBalanced("abcabc"));
    }
}
