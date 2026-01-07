package org.example.Q0524_LongestWordInDictionary;

import java.util.Arrays;
import java.util.List;

// Two pointers: For each dictionary word, check if it's a subsequence of s
public class LongestWordInDictionary {

    public String findLongestWord(String s, List<String> dictionary) { // @viz:input
        String ans = ""; // @viz:var(ans)

        for (String word : dictionary) {
            int a = word.length(), b = ans.length();

            // Skip if: current word is shorter, OR same length but lexicographically larger
            if (a < b || (a == b && word.compareTo(ans) > 0))
                continue;

            // Check if word can be formed from s
            if (isSubsequence(word, s)) {
                ans = word;
            }
        }
        return ans; // @viz:result(Longest word)
    }

    // Two-pointer check: is 'word' a subsequence of 's'?
    private boolean isSubsequence(String word, String s) {
        int i = 0, j = 0; // @viz:var(i) @viz:var(j)
        while (i < word.length() && j < s.length()) {
            if (word.charAt(i) == s.charAt(j)) {
                i++; // Matched! Move to next char in word
            }
            j++; // Always move forward in s
        }
        return i == word.length(); // All chars of word matched?
    }

    public static void main(String[] args) {
        LongestWordInDictionary solution = new LongestWordInDictionary();

        System.out.println(solution.findLongestWord("abpcplea",
                Arrays.asList("ale", "apple", "monkey", "plea"))); // Output: "apple"
    }
}
