package org.example.Q0003_LongestSubstringWithoutRepeatingCharacters;

import java.util.HashSet;

// Sliding Window: Expand window when unique, shrink from left when duplicate found
public class LongestSubstringWithoutRepeatingCharacters {
    public static int lengthOfLongestSubstring(String s) { // @viz:input
        HashSet<Character> set = new HashSet<>(); // Tracks unique chars in current window
        int ans = 0; // @viz:var(ans) // Stores maximum length found
        int p2 = 0; // @viz:var(p2) // Left pointer of sliding window

        for (int i = 0; i < s.length(); i++) { // @viz:loop(i,s) // i = right pointer of window
            char ch = s.charAt(i); // @viz:var(ch)
            if (!set.contains(ch)) {
                // Unique char found: expand window
                set.add(ch);
                // Update max length: current window size = right - left + 1
                ans = Math.max(ans, i - p2 + 1);
            } else {
                // Duplicate found: shrink window from left until duplicate is removed
                while (s.charAt(p2) != ch) {
                    set.remove(s.charAt(p2)); // Remove leftmost char from set
                    p2++; // Move left pointer right
                }
                // Remove the duplicate char itself and move past it
                set.remove(s.charAt(p2));
                p2++;
                set.add(ch); // Re-add current char (it's now the only occurrence)
            }
        }

        return ans; // @viz:result(Longest substring length)
    }

    public static void main(String[] args) {
        String s = "abcasdbadasdcjbas";

        System.out.println(lengthOfLongestSubstring(s));
    }
}
