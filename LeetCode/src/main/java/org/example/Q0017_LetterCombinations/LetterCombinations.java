package org.example.Q0017_LetterCombinations;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

// Backtracking: For each digit, try all its letters, recurse for remaining digits
public class LetterCombinations {

    HashMap<Character, char[]> map = new HashMap<>();

    public List<String> letterCombinations(String digits) {
        ArrayList<String> answer = new ArrayList<>();
        if (digits.equals(""))
            return answer;

        StringBuilder temp = new StringBuilder();
        putMap(map);
        helper(0, digits, answer, temp);
        return answer;
    }

    // Build digit → letters mapping
    private void putMap(HashMap<Character, char[]> map) {
        map.put('2', new char[] { 'a', 'b', 'c' });
        map.put('3', new char[] { 'd', 'e', 'f' });
        map.put('4', new char[] { 'g', 'h', 'i' });
        map.put('5', new char[] { 'j', 'k', 'l' });
        map.put('6', new char[] { 'm', 'n', 'o' });
        map.put('7', new char[] { 'p', 'q', 'r', 's' });
        map.put('8', new char[] { 't', 'u', 'v' });
        map.put('9', new char[] { 'w', 'x', 'y', 'z' });
    }

    // Backtrack: build combinations one letter at a time
    private void helper(int i, String digits, ArrayList<String> answer, StringBuilder temp) {
        // Base case: processed all digits
        if (i == digits.length()) {
            answer.add(temp.toString());
            return;
        }

        char digit = digits.charAt(i);
        char[] letters = map.get(digit);

        // Try each letter for current digit
        for (char c : letters) {
            temp.append(c); // Choose
            helper(i + 1, digits, answer, temp); // Explore
            temp.deleteCharAt(temp.length() - 1); // Unchoose (backtrack)
        }
    }

    public static void main(String[] args) {
        LetterCombinations solution = new LetterCombinations();

        System.out.println(solution.letterCombinations("23"));
        // Output: [ad, ae, af, bd, be, bf, cd, ce, cf]

        System.out.println(new LetterCombinations().letterCombinations("2"));
        // Output: [a, b, c]

        System.out.println(new LetterCombinations().letterCombinations(""));
        // Output: []
    }
}
