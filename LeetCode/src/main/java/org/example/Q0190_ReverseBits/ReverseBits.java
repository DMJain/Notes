package org.example.Q0190_ReverseBits;

/**
 * 190. Reverse Bits
 *
 * Approach: Bit-by-Bit Extraction
 *
 * Extract each bit from n (starting at LSB) and push it into rev
 * by shifting rev left and OR-ing the extracted bit.
 * After 32 iterations, bit 0 of n is at position 31 of rev, etc.
 *
 * (n >> i) & 1 → extract bit at position i from n
 * rev << 1 → make room for the next bit in rev
 * | bit → place the extracted bit at rev's LSB
 *
 * Time: O(1) — always exactly 32 iterations
 * Space: O(1) — only two integer variables
 */
public class ReverseBits {

    public int reverseBits(int n) {
        int rev = 0;
        for (int i = 0; i < 32; i++) {
            // Extract bit i from n, push it into rev
            rev = (rev << 1) | ((n >> i) & 1);
        }
        return rev;
    }

    // ==================== TEST CASES ====================
    public static void main(String[] args) {
        ReverseBits sol = new ReverseBits();

        // Example 1: 43261596 → 964176192
        int n1 = 43261596;
        System.out.println("Example 1: n = " + n1);
        System.out.println("Binary:    " + String.format("%32s", Integer.toBinaryString(n1)).replace(' ', '0'));
        int r1 = sol.reverseBits(n1);
        System.out.println("Reversed:  " + String.format("%32s", Integer.toBinaryString(r1)).replace(' ', '0'));
        System.out.println("Expected: 964176192, Got: " + r1);

        // Example 2: 2147483644 → 1073741822
        int n2 = 2147483644;
        System.out.println("\nExample 2: n = " + n2);
        System.out.println("Binary:    " + String.format("%32s", Integer.toBinaryString(n2)).replace(' ', '0'));
        int r2 = sol.reverseBits(n2);
        System.out.println("Reversed:  " + String.format("%32s", Integer.toBinaryString(r2)).replace(' ', '0'));
        System.out.println("Expected: 1073741822, Got: " + r2);

        // Edge: n = 0 → 0
        System.out.println("\nEdge 1: n = 0");
        System.out.println("Expected: 0, Got: " + sol.reverseBits(0));

        // Edge: n = 2 → 1073741824 (binary: 10 → 01000...0)
        int n3 = 2;
        int r3 = sol.reverseBits(n3);
        System.out.println("\nEdge 2: n = 2");
        System.out.println("Binary:    " + String.format("%32s", Integer.toBinaryString(n3)).replace(' ', '0'));
        System.out.println("Reversed:  " + String.format("%32s", Integer.toBinaryString(r3)).replace(' ', '0'));
        System.out.println("Expected: 1073741824, Got: " + r3);
    }
}
