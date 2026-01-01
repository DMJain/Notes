package org.example.Q0522_LongestUncommonSubsequenceII;

public class LongestUncommonSubsequenceII {

    public int findLUSlength(String[] strs) {
        int maxLen = -1;
        for (int i = 0; i < strs.length; i++) {
            boolean flag = false;
            int currLen = strs[i].length();
            for (int j = 0; j < strs.length; j++) {
                if (i != j && check(strs[i], strs[j])) {
                    flag = true;
                    break;
                }
            }
            if (!flag) {
                maxLen = Math.max(maxLen, currLen);
            }
        }
        return maxLen;
    }

    public boolean check(String str1, String str2) {
        int A = str1.length(), B = str2.length();
        while (A > 0 && B > 0) {
            int i = str1.length() - A;
            int j = str2.length() - B;
            if (str1.charAt(i) == str2.charAt(j)) {
                A--;
            }
            B--;
        }
        return A == 0;
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
