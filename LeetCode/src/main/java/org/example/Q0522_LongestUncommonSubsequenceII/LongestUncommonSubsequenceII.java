package org.example.Q0522_LongestUncommonSubsequenceII;

// For each string, check if it's a subsequence of any other string. If not, it's uncommon!
public class LongestUncommonSubsequenceII {

    public int findLUSlength(String[] strs) {
        int maxLen = -1;
        // Check each string to see if it qualifies as an uncommon subsequence
        for (int i = 0; i < strs.length; i++) {
            boolean isSubseq = false; // Flag: is strs[i] a subsequence of any other string?
            int currLen = strs[i].length();

            for (int j = 0; j < strs.length; j++) {
                // Skip self-comparison, check if strs[i] is subsequence of strs[j]
                if (i != j && check(strs[i], strs[j])) {
                    isSubseq = true; // Found! strs[i] IS a subsequence of strs[j]
                    break;
                }
            }
            // If strs[i] is NOT a subsequence of any other string, it's uncommon!
            if (!isSubseq) {
                maxLen = Math.max(maxLen, currLen);
            }
        }
        return maxLen;
    }

    // Returns true if str1 is a subsequence of str2
    public boolean check(String str1, String str2) {
        int A = str1.length(), B = str2.length();
        // Two pointer approach: try to match all chars of str1 within str2
        while (A > 0 && B > 0) {
            int i = str1.length() - A; // Current position in str1
            int j = str2.length() - B; // Current position in str2
            if (str1.charAt(i) == str2.charAt(j)) {
                A--; // Matched! Move to next char in str1
            }
            B--; // Always move forward in str2
        }
        return A == 0; // True if all chars of str1 were matched (it's a subsequence)
    }

    public static void main(String[] args) {
        LongestUncommonSubsequenceII solution = new LongestUncommonSubsequenceII();

        // Example 1
        String[] strs1 = { "aba", "cdc", "eae" };
        System.out.println("Example 1: " + solution.findLUSlength(strs1)); // Output: 3

        // Example 2
        String[] strs2 = { "aaa", "aaa", "aa" };
        System.out.println("Example 2: " + solution.findLUSlength(strs2)); // Output: -1
    }
}
