package org.example.Q0521_LongestUncommonSubsequenceI;

// Brain teaser: If strings differ, the longer one IS the answer (can't be subsequence of shorter)
public class LongestUncommonSubsequenceI {

    public int findLUSlength(String a, String b) { // @viz:input
        // If equal, every subsequence of a exists in b (and vice versa) → no uncommon
        if (a.equals(b))
            return -1; // @viz:result(No uncommon subsequence)

        // If different, the longer string itself is uncommon (can't fit in shorter)
        return Math.max(a.length(), b.length()); // @viz:result(Longer string is uncommon)
    }

    public static void main(String[] args) {
        LongestUncommonSubsequenceI solution = new LongestUncommonSubsequenceI();

        System.out.println(solution.findLUSlength("aba", "cdc")); // Output: 3
    }
}
