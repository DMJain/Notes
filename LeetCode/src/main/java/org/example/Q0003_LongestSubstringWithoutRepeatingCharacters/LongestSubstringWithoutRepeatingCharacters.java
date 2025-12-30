package org.example.Q0003_LongestSubstringWithoutRepeatingCharacters;

import java.util.HashSet;

public class LongestSubstringWithoutRepeatingCharacters {
    public static int lengthOfLongestSubstring(String s) {
        HashSet<Character> set = new HashSet<>();
        int ans = 0;
        int p1 = 0;
        int p2 = 0;

        for (int i = 0; i < s.length(); i++) {
            char ch = s.charAt(i);
            if (!set.contains(ch)) {
                // increment p1 for unique substring
                p1++;
                set.add(ch);
                // ans is max of ans till now, and current i - second pointer + 1
                ans = Math.max(ans, i - p2 + 1);
            } else {
                // when we find duplicate, we start reducing subarray size from second pointer
                // till we found the duplicate array
                // this will help us to maintain size of unique array at given index between
                // given window.
                while (s.charAt(p2) != ch) {
                    set.remove(s.charAt(p2));
                    p2++;
                }
                set.remove(s.charAt(p2));
                p2++;
                set.add(ch);
            }
        }

        return ans;
    }

    public static void main(String[] args) {
        String s = "abcasdbadasdcjbas";

        System.out.println(lengthOfLongestSubstring(s));
    }
}
