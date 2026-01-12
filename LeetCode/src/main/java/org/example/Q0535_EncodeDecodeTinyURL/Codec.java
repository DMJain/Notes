package org.example.Q0535_EncodeDecodeTinyURL;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicLong;

// Design: Counter-based Base62 encoding with dual HashMaps for O(1) encode/decode.
public class Codec {
    private HashMap<String, String> shortToLong = new HashMap<>();
    private HashMap<String, String> longToShort = new HashMap<>();
    private AtomicLong counter = new AtomicLong(1000);
    private static final String BASE62 = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String DOMAIN = "http://tiny.url/";

    // Encodes a URL to a shortened URL.
    public String encode(String longUrl) {
        // Return existing short URL if already encoded
        if (longToShort.containsKey(longUrl)) {
            return DOMAIN + longToShort.get(longUrl);
        }

        // Generate new short code using counter
        String shortCode = encodeBase62(counter.incrementAndGet());

        // Store bidirectional mapping
        shortToLong.put(shortCode, longUrl);
        longToShort.put(longUrl, shortCode);

        return DOMAIN + shortCode;
    }

    // Decodes a shortened URL to its original URL.
    public String decode(String shortUrl) {
        String shortCode = shortUrl.replace(DOMAIN, "");
        return shortToLong.getOrDefault(shortCode, "");
    }

    // Convert number to Base62 string
    private String encodeBase62(long num) {
        StringBuilder sb = new StringBuilder();
        do {
            sb.append(BASE62.charAt((int) (num % 62)));
            num /= 62;
        } while (num > 0);
        return sb.reverse().toString();
    }

    public static void main(String[] args) {
        Codec codec = new Codec();

        String url1 = "https://leetcode.com/problems/design-tinyurl";
        String tiny1 = codec.encode(url1);
        System.out.println("Encoded: " + tiny1);
        System.out.println("Decoded: " + codec.decode(tiny1));
        System.out.println("Match: " + url1.equals(codec.decode(tiny1)));

        // Test duplicate encoding (should return same short URL)
        String tiny1Again = codec.encode(url1);
        System.out.println("Same short for same long? " + tiny1.equals(tiny1Again));

        // Test another URL
        String url2 = "https://google.com";
        String tiny2 = codec.encode(url2);
        System.out.println("\nEncoded: " + tiny2);
        System.out.println("Decoded: " + codec.decode(tiny2));
    }
}
