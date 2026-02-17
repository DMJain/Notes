package org.example.Q0401_BinaryWatch;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

// Gosper's hack: iterate all k-bit subsets of a 10-bit number.
// Bits 9-6 = hour (0-11), bits 5-0 = minute (0-59).
public class BinaryWatch {

    public List<String> readBinaryWatch(int k) {
        if (k == 0)
            return Arrays.asList("0:00");
        if (k > 8)
            return new ArrayList<>(); // max valid: h=11(3 bits) + m=59(5 bits) = 8

        List<String> res = new ArrayList<>();
        int q = (1 << k) - 1; // smallest number with exactly k bits set

        while (q < (1 << 10)) {
            String time = isValid(q);
            if (!time.isEmpty())
                res.add(time);

            // Gosper's hack: compute next number with same popcount
            int r = q & -q; // rightmost set bit
            int n = q + r; // carry into next group
            q = (((n ^ q) >> 2) / r) | n; // pack flipped bits to low end
        }

        return res;
    }

    // Extract hour (top 4 bits) and minute (bottom 6 bits), validate ranges
    String isValid(int q) {
        int hour = q >> 6; // bits 9-6
        int min = q & 63; // bits 5-0 (0b111111 = 63)
        if (hour >= 12 || min >= 60)
            return "";
        return hour + ":" + (min < 10 ? "0" : "") + min;
    }

    public static void main(String[] args) {
        BinaryWatch solution = new BinaryWatch();

        System.out.println("turnedOn=0: " + solution.readBinaryWatch(0)); // [0:00]
        System.out.println("turnedOn=1: " + solution.readBinaryWatch(1)); // 10 results
        System.out.println("turnedOn=9: " + solution.readBinaryWatch(9)); // []
    }
}
