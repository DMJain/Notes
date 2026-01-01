package org.example.Q0521_LongestUncommonSubsequenceI;

// Brain teaser: If strings differ, the longer one IS the answer (can't be subsequence of shorter)
public class LongestUncommonSubsequenceI {

    public int findLUSlength(String a, String b) {
        // If equal, every subsequence of a exists in b (and vice versa) → no uncommon
        if (a.equals(b))
            return -1;

        // If different, the longer string itself is uncommon (can't fit in shorter)
        return Math.max(a.length(), b.length());
    }

    public static void main(String[] args) {
        LongestUncommonSubsequenceI solution = new LongestUncommonSubsequenceI();

        System.out.println(solution.findLUSlength("aba", "cdc")); // Output: 3
        System.out.println(solution.findLUSlength("aaa", "bbb")); // Output: 3
        System.out.println(solution.findLUSlength("aaa", "aaa")); // Output: -1
    }
}
