package org.example.Q0067_AddBinary;

// Two pointers from end of both strings, carry propagation.
// t = carry + digit_a + digit_b → append t%2, carry = t/2. Reverse at end.
public class AddBinary {

    public String addBinary(String a, String b) {
        StringBuilder ans = new StringBuilder();
        int c = 0; // carry
        int i = a.length() - 1;
        int j = b.length() - 1;

        // Process while digits remain or carry exists
        while (i >= 0 || j >= 0 || c > 0) {
            int t = c;
            if (i >= 0) {
                t += a.charAt(i) - '0';
                i--;
            }
            if (j >= 0) {
                t += b.charAt(j) - '0';
                j--;
            }
            ans.append(t % 2); // digit: 0 or 1
            c = t / 2; // carry: 0 or 1
        }

        return ans.reverse().toString();
    }

    public static void main(String[] args) {
        AddBinary solution = new AddBinary();

        System.out.println(solution.addBinary("11", "1")); // "100"
        System.out.println(solution.addBinary("1010", "1011")); // "10101"
        System.out.println(solution.addBinary("0", "0")); // "0"
        System.out.println(solution.addBinary("1", "111")); // "1000"
    }
}
